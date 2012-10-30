package brandon.utils.test;

import brandon.utils.ConfigFile;
import java.util.HashMap;

public class TestConfigFile {
	public static void main(String[] args) {
		String configFile = "/.settlersConfig";
		System.out.println("name/value pairs in " + configFile);
		ConfigFile file = new ConfigFile(configFile);
		HashMap<String, String> map = file.read();
		for(String name : map.keySet()) {
			String value = map.get(name);
			System.out.println(name + " = " + value);
		}
	}
}
