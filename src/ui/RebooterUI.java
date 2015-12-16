package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class RebooterUI extends JFrame implements ActionListener{
	//VM will generate serialVersiuonUID
	private static final long serialVersionUID = 1L;

	JPanel buttonBox;
	JButton btnShutdown;		final String str_shutdown = "ShutDown";
	JButton btnRestart;			final String str_restart = "Re-Launch"; 
	JButton btnScreenShot;		final String str_screenshot = "ScreenCapture";
	JButton btnScreenShotClear;	final String str_screenshotclear = "Remove ScreenShot";
	JPanel radioButtonBox;
	ButtonGroup radioGroup;
	JRadioButton radioKO;		final String str_radioko = "Korea";
	JRadioButton radioJP;		final String str_radiojp = "Japen";
	JRadioButton radioUS;		final String str_radious = "USA";
	JTextArea consoleLog;
	
	public RebooterUI() {
		initComponents();
		initActions();
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
		buttonBox.setLayout(new GridLayout(4, 1));

		//buttons
		btnShutdown = new JButton(str_shutdown);
		btnShutdown.setActionCommand(str_shutdown);
		btnShutdown.addActionListener(this);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch (cmd) {
		case str_shutdown:
			System.out.println("s1");
			break;
		case str_restart:
			System.out.println("s2");
			break;
		case str_screenshot:
			System.out.println("s3");
			break;
		case str_screenshotclear:
			System.out.println("s4");
			break;
		case str_radioko:
			System.out.println("s5");
			break;
		case str_radiojp:
			System.out.println("s6");
			break;
		case str_radious:
			System.out.println("s7");
			break;

		default:
			break;
		}
	}// end of actionPerformed()

}
