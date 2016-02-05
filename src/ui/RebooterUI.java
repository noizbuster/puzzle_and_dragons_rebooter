package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import core.ADBSession;

public class RebooterUI extends JFrame implements ActionListener{
	//VM will generate serialVersiuonUID
	private static final long serialVersionUID = 1L;
	
	public static final int LOCALE_KO = 1;
	public static final int LOCALE_JP = 2;
	public static final int LOCALE_US = 3;
	public static final String packageBase = "jp.gungho.";
	public static final String shutdownBase = "am force-stop ";
	public static final String launchBase = "monkey -p ";
	public static final String launchEnd = " -c android.intent.category.LAUNCHER 1";
	public static final String screenshotBase = "screencap -p /storage/emulated/0/Pictures/Screenshots/wwpad";
	public static final String LOCALE_KO_STR = "padKO";
	public static final String LOCALE_JP_STR = "pad";
	public static final String LOCALE_US_STR = "padUS";

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
	
	// Data Attributes
	int screenshotCnt = 1;
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
		radioButtonBox = new JPanel();
		buttonBox = new JPanel();
		consoleLog = new JTextArea();
		this.add(radioButtonBox, BorderLayout.WEST);
		this.add(buttonBox, BorderLayout.EAST);
		this.add(consoleLog,BorderLayout.CENTER);
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
			session.start();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				this.finalize();
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private String getPackageName(){
		String output = packageBase;
		switch(this.localeCode){
		case LOCALE_KO:
			output += LOCALE_KO_STR;
			break;
		case LOCALE_JP:
			output += LOCALE_JP_STR;
			break;
		case LOCALE_US:
			output += LOCALE_US_STR;
			break;
		}
		return output;
	}

	private String getShutdownCmd(){
		return shutdownBase + getPackageName();
	}

	private String getLaunchCmd(){
		return launchBase + getPackageName() + launchEnd;
	}
	
	private String getScreenshotCmd(){
		return screenshotBase + screenshotCnt + ".png";
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch (cmd) {
		case str_shutdown:
			System.out.println("str_shutdown clicked");
			this.session.putCommand(ADBSession.CMD_STR, getShutdownCmd());
			break;
		case str_launch:
			System.out.println("str_launch clicked");
			this.session.putCommand(ADBSession.CMD_STR, getLaunchCmd());
			break;
		case str_restart:
			System.out.println("str_restart clicked");
			new Thread()
			{
			    public void run() {
					RebooterUI.this.session.putCommand(ADBSession.CMD_STR, getShutdownCmd());
					try {
						this.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					RebooterUI.this.session.putCommand(ADBSession.CMD_STR, getLaunchCmd());					
			    }
			}.start();
			break;
		case str_screenshot:
			if(screenshotCnt >= 10){
				for(int cnt = 0; cnt < 10; cnt++){
					this.session.putCommand(ADBSession.CMD_STR, "rm /storage/emulated/0/Pictures/Screenshots/wwpad"+cnt+".png");
				}
			}
			else{
				this.session.putCommand(ADBSession.CMD_STR, getScreenshotCmd());
				screenshotCnt++;
			}
			System.out.println("screenshot clicked");
			break;
		case str_screenshotclear:
			this.session.putCommand(ADBSession.CMD_STR, "rm /storage/emulated/0/Pictures/Screenshots/wwpad*");
			System.out.println("s4");
			break;
		case str_radioko:
			this.localeCode = LOCALE_KO;
			System.out.println("str_radioko clicked");
			break;
		case str_radiojp:
			this.localeCode = LOCALE_JP;
			System.out.println("str_radiojp clicked");
			break;
		case str_radious:
			this.localeCode = LOCALE_US;
			System.out.println("str_radious clicked");
			break;

		default:
			break;
		}
	}// end of actionPerformed()

}
