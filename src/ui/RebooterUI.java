package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import core.ADBSession;
import core.AdbCommandBuilder;

public class RebooterUI extends JFrame implements ActionListener{
	//VM will generate serialVersiuonUID
	private static final long serialVersionUID = 1L;

	public static final int LOCALE_KO = 1;
	public static final int LOCALE_JP = 2;
	public static final int LOCALE_US = 3;
	
	// UI components
	JPanel buttonBox;
	JButton btnShutdown;		final String str_shutdown = "ShutDown";
	JButton btnLaunch;			final String str_launch = "Launch"; 
	JButton btnRestart;			final String str_restart = "ReStart"; 
	JButton btnScreenShot;		final String str_screenshot = "ScreenCapture";
	JButton btnScreenShotClear;	final String str_screenshotclear = "Remove ScreenShot";
	JPanel radioButtonBox;
	ButtonGroup radioGroup;
	JRadioButton radioKO;		final String str_radioko = "Korea";
	JRadioButton radioJP;		final String str_radiojp = "Japen";
	JRadioButton radioUS;		final String str_radious = "USA";
	JTextArea consoleLog;
	JScrollPane scroll;
	
	// Data Attributes
	int localeCode = 1;
	ADBSession session;	
	
	public RebooterUI() {
		initComponents();
		initActions();
		makeAdbSession();
	}
	
	public void initComponents(){
		this.setLayout(new BorderLayout());
		this.setSize(640, 480);
		this.setTitle("PAD REBOOTER V1.0");
		radioButtonBox = new JPanel();
		buttonBox = new JPanel();
		consoleLog = new JTextArea();
		scroll = new JScrollPane (consoleLog, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(radioButtonBox, BorderLayout.WEST);
		this.add(buttonBox, BorderLayout.EAST);
		this.add(scroll,BorderLayout.CENTER);
		radioButtonBox.setLayout(new GridLayout(3, 1));
		buttonBox.setLayout(new GridLayout(5, 1));

		//buttons
		btnShutdown = new JButton(str_shutdown);
		btnShutdown.setActionCommand(str_shutdown);
		btnShutdown.addActionListener(this);
		btnLaunch = new JButton(str_launch);
		btnLaunch.setActionCommand(str_launch);
		btnLaunch.addActionListener(this);
		btnRestart = new JButton(str_restart);
		btnRestart.setActionCommand(str_restart);
		btnRestart.addActionListener(this);
		btnScreenShot = new JButton(str_screenshot);
		btnScreenShot.setActionCommand(str_screenshot);
		btnScreenShot.addActionListener(this);
		btnScreenShotClear = new JButton(str_screenshotclear);
		btnScreenShotClear.setActionCommand(str_screenshotclear);
		btnScreenShotClear.addActionListener(this);
		//put buttons to panel
		buttonBox.add(btnShutdown);
		buttonBox.add(btnLaunch);
		buttonBox.add(btnRestart);
		buttonBox.add(btnScreenShot);
		buttonBox.add(btnScreenShotClear);
		
		//radiobuttons
		radioKO = new JRadioButton(str_radioko);
		radioKO.setMnemonic(KeyEvent.VK_K);
		radioKO.setActionCommand(str_radioko);
		radioKO.setSelected(true);
		radioKO.addActionListener(this);
		radioJP = new JRadioButton(str_radiojp);
		radioJP.setMnemonic(KeyEvent.VK_J);
		radioJP.setActionCommand(str_radiojp);
		radioJP.addActionListener(this);
		radioUS = new JRadioButton(str_radious);
		radioUS.setMnemonic(KeyEvent.VK_U);
		radioUS.setActionCommand(str_radious);
		radioUS.addActionListener(this);

		//radiobutton group
		radioGroup = new ButtonGroup();
		radioGroup.add(radioKO);
		radioGroup.add(radioJP);
		radioGroup.add(radioUS);

		//put radio_buttons to panel
        radioButtonBox.add(radioKO);
        radioButtonBox.add(radioJP);
        radioButtonBox.add(radioUS);
	}// end of initComponents()
	
	public void initActions(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void makeAdbSession() {
		try {
			session = new ADBSession(consoleLog);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				this.finalize();
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch (cmd) {
		case str_shutdown:
			this.session.putCommand(AdbCommandBuilder.shutdownPad(localeCode));
			break;
		case str_launch:
			this.session.putCommand(AdbCommandBuilder.launchPad(localeCode));
			break;
		case str_restart:
			new Thread()
			{
			    public void run() {
					RebooterUI.this.session.putCommand(AdbCommandBuilder.shutdownPad(localeCode));
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					RebooterUI.this.session.putCommand(AdbCommandBuilder.launchPad(localeCode));
			    }
			}.start();
			break;
		case str_screenshot:
			this.session.putCommand(AdbCommandBuilder.takeScreenshot());
			break;
		case str_screenshotclear:
			this.session.putCommand(AdbCommandBuilder.removeScreenshot());
			break;
		case str_radioko:
			this.localeCode = LOCALE_KO;
			break;
		case str_radiojp:
			this.localeCode = LOCALE_JP;
			break;
		case str_radious:
			this.localeCode = LOCALE_US;
			break;

		default:
			break;
		}
	}// end of actionPerformed()

}
