package hu.gerviba.psconline;

import java.io.File;

public final class Util {

	public static void createDirIfNotExists(String dir) {
		File file = new File(dir);
		if (!file.exists())
			file.mkdirs();
	}

	public static boolean deleteDirectory(String directory) {
		return deleteDirectory(new File(directory));
	}

	public static boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			if (null != directory.listFiles()) {
				for (File file : directory.listFiles()) {
					if (file.isDirectory()) {
						deleteDirectory(file);
					} else {
						file.delete();
					}
				}
			}
		}
		return directory.delete();
	}

}
