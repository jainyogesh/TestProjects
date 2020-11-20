/*
 *  Copyright (c) 2010 - 2030 by ACI Worldwide Inc.                 
 *  All rights reserved.                                            
 *                                                                  
 *  This software is the confidential and proprietary information   
 *  of ACI Worldwide Inc ("Confidential Information").  You shall   
 *  not disclose such Confidential Information and shall use it     
 *  only in accordance with the terms of the license agreement      
 *  you entered with ACI Worldwide Inc.
 */
package org.jainy.personal.tools;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//import com.aciworldwide.aps.adf.generate.propertyConfig.PropertyConfigUtils;

/**
 * Validates property config information
 * 
 * @author ivascaf
 *
 */
public class ValidatePropertyConfig {
    private static final String APSF_PLUGIN_ARCHETYPE = "APSF-Plugin-Archetype";
    private static final String SRC_MAIN_RESOURCES_INSTALL_UPDATES = "src\\main\\resources\\install-updates";
    private static final String MAIN_RESOURCES_INSTALL_UPDATES = "main\\resources\\install-updates";
    private static final String PROPERTY_CONFIG_XML = "propertyConfig.xml";
    private static final File CODE_BASE = new File("/home/jainy/YJ/CB/Trunk");
    //private static Logger logger = LoggerFactory.getLogger(ValidatePropertyConfig.class);

    public static void main(String[] args) {

        System.out.println("Searching for all " + SRC_MAIN_RESOURCES_INSTALL_UPDATES + "\\" + PROPERTY_CONFIG_XML + " files in "
                + CODE_BASE.getAbsolutePath() + " ... ");
                
        long startTime = System.nanoTime();  
        
        /*System.out.println(Arrays.toString(CODE_BASE.listFiles(new FileFilter() {
          public boolean accept(File file) {
            return file.isDirectory();
          }
        })));*/
        
        List propertyConfigFiles = new LinkedList<>();
        searchDir(CODE_BASE, propertyConfigFiles);
        
       /* Collection<File> propertyConfigFiles = FileUtils.listFiles(CODE_BASE, new IOFileFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return false;
            }

            @Override
            public boolean accept(File file) {
                if (file.getName().endsWith(PROPERTY_CONFIG_XML) && file.getAbsolutePath().contains(SRC_MAIN_RESOURCES_INSTALL_UPDATES)
                        && !file.getAbsolutePath().contains(APSF_PLUGIN_ARCHETYPE)) {
                    return true;
                } else {
                    return false;
                }
            }
        },  FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("target")));*/
        
        
        
        long endTime = System.nanoTime();

        System.out.println("Found " + propertyConfigFiles.size() + " matching files in " + ((endTime - startTime)/1000000) + " milli seconds");

        /*for (File propertyConfig : propertyConfigFiles) {
            System.out.println(" - Checking file " + propertyConfig.getAbsolutePath());
            PropertyConfigUtils.unmarshalPropertyConfigFile(logger, propertyConfig.getAbsolutePath());
        }

        System.out.println("All files are valid!");*/

    }
    
    private static void searchDir(File root, List list){
      
      File [] dirList = root.listFiles(new FileFilter() {
        public boolean accept(File file) {
          return file.isDirectory() && !file.getName().equals("target");
        }
      });
      
      //See if we have src as one of the child folders and if yes, note down the index
      int srcDirIdx = -1;
      for (int i=0; i < dirList.length; i++){
        if(dirList[i].getName().equals("src"))
          srcDirIdx = i;
      }
      
      // If src is present, no need to recurse for other folders, directly look at the file
      if (srcDirIdx > -1){
        File finalDir = new File(dirList[srcDirIdx].getAbsolutePath() + "\\" + MAIN_RESOURCES_INSTALL_UPDATES);
        if(finalDir.exists()){


          Collection<File> propertyConfigFiles = FileUtils.listFiles(finalDir, new IOFileFilter() {
            @Override
            public boolean accept(File dir, String name) {
              return false;
            }

            @Override
            public boolean accept(File file) {
              if (file.getName().endsWith(PROPERTY_CONFIG_XML) && !file.getAbsolutePath().contains(APSF_PLUGIN_ARCHETYPE)) {
                return true;
              } else {
                return false;
              }
            }
          },  DirectoryFileFilter.DIRECTORY);
          list.addAll(propertyConfigFiles);
        }
      }else{
        //src is not present as sibling so go through everything. if src would have been parent, we would not have landed in this situation
        for (File dir : dirList){
          searchDir(dir, list);
        }
      }
      
    }
}
