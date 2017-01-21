import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SyncMain {

	public static void main(String[] args) {
		if (args == null || args.length < 2) {
			System.err.println("Usage: SyncMain <dir1> <dir2> [option]");
			return;
		}

		File dir1 = new File(args[0]);
		File dir2 = new File(args[1]);

		if (!dir1.exists() || dir1.isFile()) {
			System.err.println("Directory 1 does not exists or is a file");
			return;
		}

		if (!dir2.exists() || dir2.isFile()) {
			System.err.println("Directory 2 does not exists or is a file");
			return;
		}

	}

	private static Map<String,FileInfo> parseDir(File dir) {
		Map<String,FileInfo> map = new HashMap<String,FileInfo>();
		
		
		
		return map;
	}
	
	private static void parseDir(File dir, Map<String,FileInfo> map) {
		if(dir.isDirectory()){
			File[] children = dir.listFiles();
			for(File child : children){
				if(child.isDirectory()){
					parseDir(child, map);
					continue;
				}
				
				FileInfo childFile = new FileInfo(child.getName(), child.getParent(), child.length());
				//map.put(child.g)
			}
		}
	}

	private static final class FileInfo {
		private String name;
		private String path;
		private long size;

		FileInfo(String name, String path, long size) {
			this.name = name;
			this.path = path;
			this.size = size;

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

	}

}
