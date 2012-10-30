package brandon.gameClient.test;

import brandon.gameClient.ServerConnectionDialog;

public class TestServerConnectionDialog {
	public static void main(String[] args) {
		String[] data = new String[3];
		data[0] = "www.brooksriver.com";
		data[1] = "localhost";
		data[2] = "spirit";
		new ServerConnectionDialog("/.settlersConfig", data);
		
	}
}
