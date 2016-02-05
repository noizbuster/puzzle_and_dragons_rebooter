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
	private final String ls = System.getProperty("line.separator");

	private int commandFlag = 0;
	private String command;
	private JTextArea outputView = null;
	private Process adbProcess = null;
	private InputStreamReader is;
	private OutputStreamWriter os;
	private BufferedReader reader;
	private BufferedWriter writer;

	public static Process makeAdbProcess(String... path) throws IOException {
		Process adbp = new ProcessBuilder(path).start();
		return adbp;
	}

	public static Process makeAdbProcess() throws IOException {
		return makeAdbProcess("./adb", "shell");
	}

	public ADBSession(JTextArea view) throws IOException {
		this.outputView = view;
		this.adbProcess = makeAdbProcess();
		this.is = new InputStreamReader(adbProcess.getInputStream());
		this.os = new OutputStreamWriter(adbProcess.getOutputStream());
		this.reader = new BufferedReader(this.is);
		this.writer = new BufferedWriter(this.os);
		this.commandFlag = CMD_NOP;
	}

	@Override
	public void run() {
		boolean amigo = true;
		while (amigo) {
			// check command
			switch (this.commandFlag) {
			case CMD_NOP:
				System.out.println("nop");
				break;
			case CMD_STR:
				this.commandFlag = CMD_NOP;
				try {
					this.writer.write(this.command + this.ls);
					this.writer.flush();
					String bf = reader.readLine();
					if (bf != null) {
						this.printOut(bf);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case CMD_END:
				amigo = false;
				break;
			}
			System.out.println("hit here");
			System.out.println("but not here");
		} // end loop
		this.printOut("ended adb session");
	}

	public void putCommand(int cmdFlag, String cmd) {
		this.command = cmd;
		this.commandFlag = cmdFlag;
		this.outputView.append(cmd + ls);
	}

	public void printOut(String msg) {
		if (outputView != null) {
			outputView.append(msg);
		}
		System.out.println(msg);
	}

}
