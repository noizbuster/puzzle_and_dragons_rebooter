package core;

public class AdbCommandBuilder {
	public static final int LOCALE_KO = 1;
	public static final int LOCALE_JP = 2;
	public static final int LOCALE_US = 3;
	public static final String LOCALE_KO_STR = "padKO";
	public static final String LOCALE_JP_STR = "pad";
	public static final String LOCALE_US_STR = "padUS";
	public static final String packageBase = "jp.gungho.";
	public static final String shutdownBase = "am force-stop ";
	public static final String launchBase = "monkey -p ";
	public static final String launchEnd = " -c android.intent.category.LAUNCHER 1";
	public static final String screenshotBase = "screencap -p ";
	public static 	    String screenshotPath = "/storage/emulated/0/Pictures/Screenshots/";
	public static final String screenshotPrefix = "wwpad";
	public static final String removeBase = "rm ";
	public static final String removeEnd = "*";

	public static String getPackageName(int localeCode){
		String output = packageBase;
		switch(localeCode){
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

	public static String shutdownPad(int localeCode){
		return shutdownBase + getPackageName(localeCode);
	}

	public static String launchPad(int localeCode){
		return launchBase + getPackageName(localeCode) + launchEnd;
	}
	
	public static String takeScreenshot(){
		return screenshotBase + screenshotPath + screenshotPrefix + System.currentTimeMillis() + ".png";
	}
	
	public static String removeScreenshot(){
		return removeBase + screenshotPath + screenshotPrefix + removeEnd;
	}
}
