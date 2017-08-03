package hu.gerviba.psconline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

public class Configuration {

	public volatile static String VERSION = "Beta";
	public static String SERVER_HOME;

	public static String WORKING_DIR;
	public static String LOG_DIR;
	public static String HOST_INFO;
	public static String DEFAULT_LANG;
	public static int MAX_CONCURRENT_TERMINAL_COUNT = 2;
	public static String[] LAUNCH_PREFIX;
	public static String COMPILER_VERSION = "No Compiler Version Found";
	
	private static final HashMap<String, HashMap<String, String>> LANG = new HashMap<>();
	
	public String getMessage(String lang, String key) {
		return LANG.getOrDefault(lang, LANG.get(DEFAULT_LANG)).get(key);
	}
	
	static {
		loadServerInfo();
		
		createDefaultPropertiesFile();
		loadProperties();
		loadLanguages();
	}
	
	public static void init() {}

	private static void loadServerInfo() {
		try (InputStream input = Configuration.class.getClassLoader()
				.getResourceAsStream("psc-online.properties")) {
			
			Properties prop = new Properties();
			prop.load(input);
			SERVER_HOME = System.getProperty("server.home", prop.getProperty("server.home"));
			SERVER_HOME = SERVER_HOME.endsWith("/") || SERVER_HOME.endsWith("\\") 
					? SERVER_HOME : SERVER_HOME + "/";
			Util.createDirIfNotExists(SERVER_HOME);

			VERSION = prop.getProperty("version");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createDefaultPropertiesFile() {
		if (new File(SERVER_HOME + "config.properties").exists())
			return;
		
		try (OutputStream output = new FileOutputStream(SERVER_HOME + "config.properties")) {
			Properties prop = new Properties();

			prop.setProperty("server.workingDir", SERVER_HOME + "virtual/");
			prop.setProperty("server.logDir", SERVER_HOME + "log/");
			prop.setProperty("server.hostInfo", "Default Testing Host");
			prop.setProperty("server.maxTermInstances", "20");
			prop.setProperty("server.defaultLanguage", "en");
			prop.setProperty("compiler.launchPrefix", "psre");
			prop.setProperty("compiler.versionCommand", "psre --pure-version");
			
			prop.store(output, "PSeudoCode Online Console Properties");
			
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	private static void loadProperties() {
		try (InputStream input = new FileInputStream(SERVER_HOME + "config.properties")) {
			Properties prop = new Properties();
			prop.load(input);

			WORKING_DIR = prop.getProperty("server.workingDir");
			WORKING_DIR = WORKING_DIR.endsWith("/") || WORKING_DIR.endsWith("\\") 
					? WORKING_DIR : WORKING_DIR + "/";
			Util.createDirIfNotExists(WORKING_DIR);
			
			LOG_DIR = prop.getProperty("server.logDir");
			LOG_DIR = LOG_DIR.endsWith("/") || LOG_DIR.endsWith("\\") 
					? LOG_DIR : LOG_DIR + "/";
			Util.createDirIfNotExists(LOG_DIR);
			
			HOST_INFO = prop.getProperty("server.hostInfo");
			MAX_CONCURRENT_TERMINAL_COUNT = Integer.parseInt(prop.getProperty("server.maxTermInstances"));
			DEFAULT_LANG = prop.getProperty("server.defaultLanguage");
			LAUNCH_PREFIX = prop.getProperty("compiler.launchPrefix").split(" ");
			COMPILER_VERSION = getProcessResult(prop.getProperty("compiler.versionCommand"));
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private static String getProcessResult(String command) {
		try {
			Process p = new ProcessBuilder(Arrays.asList(command.split(" "))).start();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
				String line;
				if ((line = br.readLine()) != null)
					return line;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return COMPILER_VERSION;
	}

	private static void loadLanguages() {
		//TODO: Load Languages
		
	}

}
