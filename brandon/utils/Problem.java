package brandon.utils;

import javax.swing.*;
import java.awt.Component;

/** The Problem class is a simple class which displays a dialog box
 *  when there's an error that we need to tell the user, or used during
 *  debugging to avoid print statements in the log files
 */
public class Problem {
    public static ExitableApplet applet = null;
    public static ExitableApplication application = null;

    private static Component getParent() {
        // Assume at first that errors will come from the settlersFrame
        // (This is the main window of the game after the game has started)
        //Component parent = applet.settlersFrame;
        //if (parent == null) {
        // If this assumption is not correct, maybe we're viewing the
        // gameOptionsRequester. Try this as the parent frame.
        //parent = applet.gameOptionsRequester;
        //if (parent == null) {
        // If the frame is STILL null, then we are even earlier in
        // the program than first thought. Use the applet's root pane
        //Component parent = applet.getRootPane();
        //}
        //}
        //return parent;
	    return null;
    }

    public static void info(String infoString) {
        JOptionPane.showConfirmDialog(getParent(), infoString,
                                  "Settlers Information", JOptionPane.OK_OPTION,
                                  JOptionPane.INFORMATION_MESSAGE);
    }

    public static void warning(String infoString) {
        JOptionPane.showConfirmDialog(getParent(), infoString,
                                  "Settlers Warning", JOptionPane.OK_OPTION,
                                  JOptionPane.WARNING_MESSAGE);
    }


    /** Shows an Error dialog box. Program will end after clicking OK button */
    public static void error(String errorString) {
        JOptionPane.showConfirmDialog(getParent(), errorString,
                                  "ERROR", JOptionPane.OK_OPTION,
                                  JOptionPane.ERROR_MESSAGE);
        exit();
    }

    /** Shows an Exception dialog box. Program will end after clicking OK button,
     *  but continue if the CANCEL button is pressed */
    public static void exception(String errorString) {
        int result = JOptionPane.showConfirmDialog(getParent(), errorString,
                                               "Exception", JOptionPane.OK_CANCEL_OPTION,
                                               JOptionPane.WARNING_MESSAGE);
        switch (result) {
            case JOptionPane.CANCEL_OPTION: break;
            case JOptionPane.OK_OPTION: exit(); break;
        }
    }

    /** Shows a Server Error dialog box. Program will end immediately after closing the box */
    public static void serverError(String errorString) {
        JOptionPane.showMessageDialog(getParent(), errorString,
        		                      "Connection Problem with server", JOptionPane.ERROR_MESSAGE);
        exit();
    }
  
    /** Shows a Server Warning dialog box. Program will try to continue if YES is hit. */
    public static void serverWarning(String warningString) {
    	int result = JOptionPane.showConfirmDialog(getParent(), warningString + " Try to continue?",
    				"Connection Problem with server",
    				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    	switch (result) {
    		case JOptionPane.YES_OPTION: break;
    		case JOptionPane.NO_OPTION: exit(); break;
    	}
    }
    
    private static void exit() {
        if (application != null) {
        	application.exitGracefully();
        }
        
        if (applet != null) {
        	applet.exitGracefully();
        }    			
    }
  
}
