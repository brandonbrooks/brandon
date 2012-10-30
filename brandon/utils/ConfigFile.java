package brandon.utils;

import java.util.HashMap;
import java.io.*;

public class ConfigFile {
	String filename;
	HashMap<String, String> values = new HashMap<String, String>();
	
	public ConfigFile(String filename) {
		this.filename = filename;
	}
	
	private void process(String line) {
		String lineParts[] = line.split("=");
		
		if (lineParts.length != 2) {
			// Invalid name=value pair!
		} else {
			values.put(lineParts[0].trim(), lineParts[1].trim());
		}		
	}
	
	
	/** Reads the config file and returns a HashMap of the name value pairs */
	public HashMap read() {
		String line;
		
		File file = new File(filename);
		if (file.canRead()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				while((line = reader.readLine()) != null) {
					process(line);
				}
			reader.close();
			
			} catch (FileNotFoundException e) {
			
			} catch (IOException e) {
			
			}
		} else {
			Log.info(this, "read", "File is not readable!");
		}
		
		return values;
	}
	
	public boolean save(HashMap<String, String> myValues) {
		File file = new File(filename);
		if (file.canWrite()) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				for(String name : myValues.keySet()) {
					writer.write(name + "=" + myValues.get(name) + "\n");
				}
				writer.close();
			} catch (IOException e) {
				return false;
			}
			return true;
		}
	
        return false;
        
	}
	
}
