package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JTextArea;

public class ADBSession {

	private static final String adbinWindows = "adb.exe shell ";
	private static final String adbinLinux = "./adb shell ";
	private final String ls = System.getProperty("line.separator");

	private static boolean isWindows = false;
	private JTextArea outputView = null;

	public ADBSession(JTextArea view) throws IOException {
		this.outputView = view;
	}

	public void putCommand(String cmd) {
		String wholeCmd;
		if (isWindows) {
			wholeCmd = adbinWindows + cmd;
		} else {
			wholeCmd = adbinLinux + cmd;
		}

		printOut("CMD", wholeCmd);
		new Thread() {
			@Override
			public void run() {
				try {
					String line;
					Process p = Runtime.getRuntime().exec(wholeCmd);
					BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					while ((line = bri.readLine()) != null) {
						printOut("OUT", line);
					}
					bri.close();
					while ((line = bre.readLine()) != null) {
						printOut("ERR", line);
					}
					bre.close();
				} catch (Exception E) {
					E.printStackTrace();
				}
				super.run();
			}
		}.start();
	}

	public void printOut(String tag, String msg) {
		if (outputView != null) {
			outputView.append(tag + ": " + msg + ls);
			outputView.setCaretPosition(outputView.getDocument().getLength());
		}
		System.out.println(tag + ": " + msg);
	}

	public static String getOS() {
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
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}
}
