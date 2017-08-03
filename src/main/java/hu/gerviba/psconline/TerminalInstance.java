package hu.gerviba.psconline;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

public class TerminalInstance {

	private static ScheduledExecutorService PROCESSES = Executors.newScheduledThreadPool(Configuration.MAX_CONCURRENT_TERMINAL_COUNT);
	private static ExecutorService ANSWER_THREAD = Executors.newSingleThreadExecutor();
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static HashSet<PosixFilePermission> PERMISSIONS = new HashSet<>(Arrays.asList(
			PosixFilePermission.GROUP_READ,
			PosixFilePermission.GROUP_WRITE,
			PosixFilePermission.GROUP_EXECUTE, 
			PosixFilePermission.OWNER_READ,
			PosixFilePermission.OWNER_WRITE,
			PosixFilePermission.OWNER_EXECUTE,
			PosixFilePermission.OTHERS_READ,
			PosixFilePermission.OTHERS_EXECUTE));
	
	private volatile Session session;
	private volatile Process process = null;
	private volatile String lang = null;
	
	public TerminalInstance(Session userSession) {
		this.session = userSession;
	}

	public void free() {
		if (process != null && process.isAlive())
			sendStatus("Process killed");
		process.destroy();
		Util.deleteDirectory(Configuration.WORKING_DIR + session.getId());
	}
	
	public void setLanguage(String lang) {
		this.lang = lang;
	}

	public void sendStdout(String message) {
		ANSWER_THREAD.submit(new Runnable() {
			@Override
			public void run() {
				JsonObject object = Json.createObjectBuilder()
						.add("action", "stdout")
						.add("message", message)
						.build();
				
				try {
					session.getBasicRemote().sendText(object.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void sendStatus(String message) {
		ANSWER_THREAD.submit(new Runnable() {
			@Override
			public void run() {
				JsonObject object = Json.createObjectBuilder()
						.add("action", "status")
						.add("message", "[" + DATE_FORMAT.format(System.currentTimeMillis()) + "] " + message)
						.build();

				try {
					session.getBasicRemote().sendText(object.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void sendNote(String message) {
		ANSWER_THREAD.submit(new Runnable() {
			@Override
			public void run() {
				JsonObject object = Json.createObjectBuilder()
						.add("action", "note")
						.add("message", message)
						.build();

				try {
					session.getBasicRemote().sendText(object.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void sendLine(String message) {
		ANSWER_THREAD.submit(new Runnable() {
			@Override
			public void run() {
				JsonObject object = Json.createObjectBuilder()
						.add("action", "line")
						.add("message", message)
						.build();

				try {
					session.getBasicRemote().sendText(object.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void startApplication(String code) {
		String fileName = Configuration.WORKING_DIR + session.getId() + "/source.pss";
		Util.createDirIfNotExists(Configuration.WORKING_DIR + session.getId());
		try {
			Files.write(Paths.get(fileName), 
					new String(Base64.getDecoder().decode(code))
					.replace("&gt;", ">")
					.replace("&lt;", "<")
					.getBytes());
			Files.setPosixFilePermissions(Paths.get(fileName), PERMISSIONS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final ProcessBuilder builder = new ProcessBuilder(
				buildStartQuery("-cr", "--input-file", fileName, "--nogui", "--jailed"));
		builder.redirectErrorStream(true);
		
		PROCESSES.submit(new Callable<Integer>() {
			@Override
			public Integer call() {
				try {
					long startTime = System.currentTimeMillis();
					process = builder.start();
					try (Scanner scanner = new Scanner(process.getInputStream())) {
				        while (scanner.hasNextLine()) {
				            sendStdout(scanner.nextLine());
				        }
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					long endTime = System.currentTimeMillis();
					sendStatus("Exit code: " + process.waitFor() + ", (" + ((endTime - startTime) / 1000F) + "s)");
					sendTerminated();
					
					return process.exitValue();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
				return Integer.MIN_VALUE;
			}
		});
		
		
	}

	public void sendTerminated() {
		ANSWER_THREAD.submit(new Runnable() {
			@Override
			public void run() {
				JsonObject object = Json.createObjectBuilder()
						.add("action", "terminated")
						.build();

				try {
					session.getBasicRemote().sendText(object.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public String[] buildStartQuery(String... args) {
		final String[] result = new String[Configuration.LAUNCH_PREFIX.length + args.length];
		System.arraycopy(Configuration.LAUNCH_PREFIX, 0, result, 0, Configuration.LAUNCH_PREFIX.length);
		System.arraycopy(args, 0, result, Configuration.LAUNCH_PREFIX.length, args.length);
		return result;
	}
	
	private String fixCode(String code) {
		return Base64.getEncoder().encodeToString(
				new String(Base64.getDecoder().decode(code))
				.replace("&gt;", ">")
				.replace("&lt;", "<").getBytes());
	}

	public void sendToApp(String message) {
		String line = new String(Base64.getDecoder().decode(message));
		sendLine(line);
		try {
			process.getOutputStream().write((line + "\n").getBytes());
			process.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
