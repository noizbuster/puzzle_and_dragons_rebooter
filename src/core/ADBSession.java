package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JTextArea;

public class ADBSession extends Thread {

	public static final int CMD_NOP = 0;
	public static final int CMD_STR = 1;
	public static final int CMD_END = 999;
	private static final String adbinWindows = "adb.exe";
	private static final String adbinLinux = "./adb";
	private final String ls = System.getProperty("line.separator");

	private static boolean isWindows = false;
	private int commandFlag = 0;
	private String command;
	private JTextArea outputView = null;
	private Process adbProcess = null;
	private InputStreamReader is;
	private InputStreamReader es;
	private OutputStreamWriter os;
	private BufferedReader reader;
	private BufferedReader error;	
	private BufferedWriter writer;

	public static Process makeAdbProcess(String... path) throws IOException {
		Process adbp = new ProcessBuilder(path).start();
//		try {
//			adbp.waitFor();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return adbp;
	}

	public static Process makeAdbProcess() throws IOException {
		if(isUnix()){
			isWindows = false;
			return makeAdbProcess(adbinLinux, "shell");
		} else if (isWindows()){
			isWindows = true;
			return makeAdbProcess(adbinWindows, "shell");
		} else {
			System.out.println("Impossible case Hit, #00000002");
			return null;
		}
	}

	public ADBSession(JTextArea view) throws IOException {
		this.outputView = view;
		this.adbProcess = makeAdbProcess();
		this.is = new InputStreamReader(adbProcess.getInputStream());
		this.es = new InputStreamReader(adbProcess.getErrorStream());
		this.os = new OutputStreamWriter(adbProcess.getOutputStream());
		this.reader = new BufferedReader(this.is);
		this.error = new BufferedReader(this.es);
		this.writer = new BufferedWriter(this.os);
		this.commandFlag = CMD_NOP;
	}

	@Override
	public void run() {
		boolean amigo = true;
		
		while (amigo) {
			try {
				if(reader.ready()){
					String bf = reader.readLine();
					if(bf != null){
						this.printOut(bf);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(error.ready()){
					String bf = error.readLine();
					if(bf != null){
						this.printOut(bf);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			// check command
			switch (this.commandFlag) {
			case CMD_NOP:
				break;
			case CMD_STR:
				this.commandFlag = CMD_NOP;
				this.outputView.append("command execute: " + this.command + ls);
				String wholeCmd;
				if (isWindows) {
					wholeCmd = adbinWindows + " " + command;
				} else {
					wholeCmd = adbinLinux + " " + command;
				}
				
				new Thread(){
					@Override
					public void run() {
						try {
							String line;
							Process p = Runtime.getRuntime().exec(wholeCmd);
							BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
							BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
							while ((line = bri.readLine()) != null) {
								printOut(line + ls);
							}
							bri.close();
							while ((line = bre.readLine()) != null) {
								printOut(line + ls);
							}
							bre.close();
							printOut(command + ls);
						} catch (Exception E) {
							E.printStackTrace();
						}
						super.run();
					}
				}.start();
				
				break;
			case CMD_END:
//				amigo = false;
				break;
			default:
				System.err.println("Impossible Case Hit! : #00000001");
				break;
			}
			System.out.println("loop");
		} // end loop
		this.printOut("ended adb session");
	}

	public void putCommand(int cmdFlag, String cmd) {
		this.command = cmd;
		this.commandFlag = cmdFlag;
		String wholeCmd;
		if (isWindows) {
			wholeCmd = adbinWindows + " shell " + command;
		} else {
			wholeCmd = adbinLinux + " shell " + command;
		}

		System.out.println("@@@@@@@@"+wholeCmd);
		new Thread(){
			@Override
			public void run() {
				try {
					String line;
					Process p = Runtime.getRuntime().exec(wholeCmd);
					BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					while ((line = bri.readLine()) != null) {
						printOut(line + ls);
					}
					bri.close();
					while ((line = bre.readLine()) != null) {
						printOut(line + ls);
					}
					bre.close();
					printOut(command + ls);
				} catch (Exception E) {
					E.printStackTrace();
				}
				super.run();
			}
		}.start();
		this.outputView.append("command recv: " + cmd + ls);
	}

	public void printOut(String msg) {
		if (outputView != null) {
			outputView.append(msg + ls);
		}
		System.out.println("printout: " + msg);
	}

	public static String getOS(){
		return System.getProperty("os.name").toLowerCase();
	}

	public static boolean isWindows() {
		String OS = getOS();
		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {
		String OS = getOS();
		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {
		String OS = getOS();
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}
	
	public void windowsCommand() {
		System.out.println("wincommand called : "+"adb shell " + this.command);
	}
}
