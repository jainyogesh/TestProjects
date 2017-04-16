package com.aciworldwide.utility.dbdependency;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.exception.FlywayException;
import com.googlecode.flyway.core.metadatatable.MetaDataTableRow;


public class FlywayWrapper {
	
	private String operation;
	private String inputDir;
	private String skipFlag;
	private Flyway flyway;
	private Properties moduleProps = new Properties();
	private Properties excludePackagesProps = new Properties();
	private Properties jdbcProps = new Properties();
	private static final String CLEAN = "clean";
	private static final String STATUS = "status";
	private static final String APPLY = "apply";
	private static final String META_INFO_TABLE_PREFIX = "AMETA";
	private static final String INITIAL_VERSION_DESC = "InitialVersion";
	private static final String DELIM = "_";
	private static final String SQL_RESOURCE_PATH = "sql";
	
	private static final Log LOG = LogFactory.getLog(FlywayWrapper.class);
	private static String schemaName = "";
	private static String schemaPassword = "";
	private static String jdbcURL = "";
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		FlywayWrapper wrapper = new FlywayWrapper(args[0], args[1], args[2]);
		wrapper.process();
		/*if(args[0].equalsIgnoreCase("apply")){
			wrapper.writeToPropertyFile();
		}*/
	}
	
	FlywayWrapper(String operation, String inputDir, String skipFlag){
		this.operation = operation;
		this.inputDir = inputDir;
		this.skipFlag = skipFlag;
		this.flyway = new Flyway();
		initialize();
	}
	
	private void initialize(){
		try {			
			this.jdbcProps.load(this.getClass().getClassLoader().getResourceAsStream("jdbc.properties"));
			this.flyway.configure(jdbcProps);
			schemaName = jdbcProps.getProperty("flyway.user");
			jdbcURL = jdbcProps.getProperty("flyway.url");
			LOG.info("JDBC URL : "+jdbcURL);
			LOG.info("USERNAME : "+schemaName);
			LOG.info("Connected to "+schemaName+" at"+jdbcURL);
			createModulePropFile();
			this.moduleProps.load(this.getClass().getClassLoader().getResourceAsStream("modulename.properties"));
			this.excludePackagesProps.load(this.getClass().getClassLoader().getResourceAsStream("excludedpackages.properties"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	private void createModulePropFile() {
		File modulePropFile = new File("conf/modulename.properties");
		if(!(modulePropFile.exists())){
			try {
				modulePropFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void process(){
		if(CLEAN.equals(this.operation)){
			clean();
		}else if(STATUS.equals(this.operation)){
			status();
		}else if(APPLY.equals(this.operation)){
			apply();
		}else{
			System.err.println("No Valid Operation specified for FlyWay. DB Scripts have not been applied!!!");
		}
	}
	
	private void clean(){
		if ((jdbcURL.equals("jdbc:oracle:thin:@oma2apsfv04:1521:apsfuni1"))&&(schemaName.equals("apsfdev1"))){
			LOG.info("Trying to clean Developers Schema. Clean Operation Not possible for this schema. Please specify the right schema details and try again....");
		} else if ((jdbcURL.equals("jdbc:oracle:thin:@172.21.55.156:1521:EB8I11G"))&&(schemaName.equals("apsfdevemfstudio"))){
			LOG.info("Trying to clean Developers Schema. Clean Operation Not possible for this schema. Please specify the right schema details and try again....");
		} else {
			this.flyway.clean();
			cleanModulePropFile();
			LOG.info(schemaName+" at "+jdbcURL+" CLEANED!");
		}
	}
	
	private void cleanModulePropFile() {
		File modulePropFile = new File("conf/modulename.properties");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(modulePropFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		writer.print("");
		writer.close();
	}

	private void status() {
		System.out.println(this.flyway.status());
	}
	
	private void apply() {
		File baseDir = new File(this.inputDir);
		
		File[] children = baseDir.listFiles();
		File[] sortedChildren = new File[children.length];
		for(File child : children){
			int index = getIndex(child);
			sortedChildren[index] = child;
		}
		
		if(skipFlag.equalsIgnoreCase("YES")){
			LOG.info("SKIPPING TESTS");
		}
		MetaDataTableRow status = this.flyway.status();
		
		if(status == null){
			DBUtils.executeMandatorySQLs();
		}
		
		for(File child : sortedChildren){
			if(isFileSkipped(child)){
				LOG.info(child.getName().toUpperCase() + " SKIPPED.");
				continue;
			}
			String metaInfoTableName = getMetaInfoTableName(getModuleName(child));
			LOG.info("META TABLE : "+metaInfoTableName);
			writeToPropertyFile();
			this.flyway.setTable(metaInfoTableName);
			if(this.flyway.getTable().equals(META_INFO_TABLE_PREFIX + DELIM + "INT_TST")){
				continue;
			}
			
			if(skipFlag.equalsIgnoreCase("YES")){
				if(child.getName().contains("tests")){
					LOG.info(child.getName().toUpperCase() + " SKIPPED (TESTS).");
					continue;
				} else {
					LOG.info(child.getName() +" PROCESSED");
					processSQLs(child);
				}
				
			} else {
				processSQLs(child);
			}
		}
		LOG.info("All SQLs Processed.");	
	}
	
	private void processSQLs(File child){
		MetaDataTableRow status = this.flyway.status();
		if(status == null){
			this.flyway.setInitialDescription(flyway.getTable() + DELIM + INITIAL_VERSION_DESC);
			this.flyway.init();
		}
			this.flyway.setLocations(child.getName() + File.separatorChar + SQL_RESOURCE_PATH + File.separatorChar);
			this.flyway.setSqlMigrationPrefix(getSqlMigrationPrefix(child));
			try{
				this.flyway.migrate();
				LOG.info(child.getName().toUpperCase() + " PROCESSED.");
			}catch(FlywayException e){
				LOG.info("Exception encountered in Module : " + child.getName());
				LOG.info(e);
				throw e;
			}
	}
	

	private boolean isFileSkipped(File child) {
		return this.excludePackagesProps.containsKey(child.getName().substring(child.getName().indexOf('_') + 1));
	}

	private String getModuleName(File child) {
		return child.getName().substring(child.getName().indexOf('_') + 1).toUpperCase();
	}
	
	private int getIndex(File child){
		return Integer.parseInt(child.getName().substring(0, child.getName().indexOf('_'))) - 1;
	}
	
	public String getMetaInfoTableName(String moduleName) {
		String finalModuleName = "";
		final int MAX_MODULENAME_LENGTH = 22;
		if(this.moduleProps.containsKey(moduleName)){
			finalModuleName = (String) this.moduleProps.getProperty(moduleName);
		} else {
			String tempName = moduleName;
			String tempArr1[] = tempName.split("-");
			final int MAX_ALLOWED_LENGTH = MAX_MODULENAME_LENGTH - (META_INFO_TABLE_PREFIX.length() + 1);
			int useUnderScore = 0;
			// (tempArr1.length<<1) same as (tempArr1.length * 2) but faster
			if((tempArr1.length<<1) < MAX_ALLOWED_LENGTH){
				useUnderScore = MAX_ALLOWED_LENGTH - tempArr1.length<<1;
			}
			for(int i=0;i<tempArr1.length && finalModuleName.length() <=  MAX_ALLOWED_LENGTH;i++){
				tempArr1[i] = tempArr1[i].substring(0, 2);
				if(useUnderScore-- > 0){
					finalModuleName = finalModuleName +"_"+tempArr1[i];
				}else{
					finalModuleName = finalModuleName + tempArr1[i];
				}
			}
			finalModuleName = META_INFO_TABLE_PREFIX+finalModuleName;
			if(!isUnique(finalModuleName)){
				finalModuleName = getUnique(finalModuleName);
			}
			/*if(!(this.moduleProps.containsKey(moduleName))){
				finalModuleName = getMetaInfoTableName(moduleName);
			} else if(!isUnique(finalModuleName)){
				finalModuleName = getUnique(finalModuleName);
			}*/
			this.moduleProps.setProperty(moduleName, finalModuleName);
		}
		return finalModuleName;
	}
	
	private String getUnique(String finalModuleName){
		String[] letters = {"A","B","C","D","E","F","G","H","I","J"};
		int pos1 = (int)Math.random()*10;
		int pos2 = (int)Math.random()*10;
		finalModuleName = finalModuleName.substring(0, (finalModuleName.length()-2))+letters[pos1]+letters[pos2]+pos1;
		if(!(isUnique(finalModuleName))){
			finalModuleName = getUnique(finalModuleName);
		}
		return finalModuleName;
	}

	private boolean isUnique(String finalModuleName) {
		if(this.moduleProps.containsValue(finalModuleName)){
			return false;
		}
		return true;
	}

	private void writeToPropertyFile() {
		try {
			this.moduleProps.store(new BufferedWriter( new FileWriter("conf/modulename.properties",false)), "ModuleName=MetaTable");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getSqlMigrationPrefix(File child) {
		return getModuleName(child).replace('-', '_').toUpperCase()+"_V";
	}
}
