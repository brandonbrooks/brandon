package brandon.utils;

/** Simple Logging class. Logging can be turned off by
 *  setting the boolean flags INFO, WARNING, ERROR to false
 */
public class Log {
	public enum LogLevel { COMMENT, INFO, WARNING, ERROR };
	
	public static boolean comment = true;
    public static boolean info = true;
    public static boolean warning = true;
    public static boolean error = true;

    public static void comment(Object object, String method, String string) {
    	if (Log.comment) {
    		System.out.println("COMMENT: [" + object.getClass().toString() + ":" + method + "] " + string);
    	}
    }
    
    public static void info(Object object, String method, String string) {
        if (Log.info) {
    	    System.out.println("INFO:    [" + object.getClass().toString() + ":" + method + "] " + string);
        }
    }

    public static void warning(Object object, String method, String string) {
        if (Log.warning) {
        	System.out.println("WARNING: [" + object.getClass().toString() + ":" + method + "] " + string);
        			
        }
    }

    public static void error(Object object, String method, String string) {
        if (Log.error) {
        	System.out.println("ERROR:   [" + object.toString() + ":" + method + "] " + string);
        }
    }

    public static void setLevel(LogLevel level) {
    	switch (level) {
    		case COMMENT: comment = true;  info = true;  warning = true;  error = true; break;
    		case INFO:    comment = false; info = true;  warning = true;  error = true; break;
    		case WARNING: comment = false; info = false; warning = true;  error = true; break;
    		case ERROR:   comment = false; info = false; warning = false; error = true; break;
    	}
    	
    	
    }
    
}
