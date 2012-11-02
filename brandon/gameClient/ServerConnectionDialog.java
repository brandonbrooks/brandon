package brandon.gameClient;

import brandon.utils.ConfigFile;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import brandon.utils.Log;

/** Shows a dialog box which queries to which server we should
 *  connect to. In addition, asks for proxy name and port if necessary.
 *  This dialog box is also smart enough stay hidden if it's not needed
 */
public class ServerConnectionDialog implements ActionListener {
	public final static int WIDTH = 400;
	public final static int HEIGHT = 200;
	
	private JFrame frame;
	private JRadioButton proxyButton;
	private JButton okButton;
	private ConfigFile file;
	private JTextField proxyNameField;
	private JTextField proxyPortField;
	private JLabel proxyNameLabel;
	private JLabel proxyPortLabel;
	private JComboBox serverList;
	private JCheckBox rememberCheckBox;
	
	private String[] data;
	
	private String serverName;
	private boolean useProxy = false;
	private String proxyName = null;
	private String proxyPort = null;
	private boolean remember = false;
	
	private boolean finished = false;
	
	public ServerConnectionDialog(String configFilename, String[] data) {
		final String methodName = "ServerConnectionDialog";
		this.data = data;
		
		file = new ConfigFile(configFilename);
		HashMap<String, String> map = file.read();
		String remember = map.get("remember");
		if ((remember != null) && (remember.equals("yes"))) {
			return;
		} else {
			// We need to show the dialog
			String serverName = map.get("server");
			int selectedServer = 0;
			for(int i=0; (i < data.length); i++) {
				String server = data[i];
				if (server.equals(serverName)) {
					selectedServer = i;
					Log.comment(this, methodName, "Selected server is "  + i);
				}
			}
			
			serverName = data[selectedServer];
		
			proxyName = map.get("proxyname");
			if (proxyName == null) { proxyName = ""; }
			proxyPort = map.get("proxyport");
			if (proxyPort == null) { proxyPort = ""; }
			
			
			switch((map.get("proxy") == null) ? "no" : map.get("proxy")) {
				case "yes" : useProxy = true;
				case "no" : default: useProxy = false;
			}
			
			// 4 lines above equivalent to:
			//String useMapString = map.get("proxy");
			//if (useMapString == null) {
			//	useProxy = false;
			//} else {
			//	if (useMapString.equals("yes")) {
			//		useProxy = true;
			//	} else {
			//		useProxy = false;
			//	}
			//}
			
			JPanel useServerPanel = new JPanel();
			useServerPanel.setLayout(new BoxLayout(useServerPanel, BoxLayout.X_AXIS));
			useServerPanel.add(new JLabel("Use server "));
			useServerPanel.setMaximumSize(new Dimension(400, 24));
			
			serverList = new JComboBox(data);
			serverList.setSelectedIndex(selectedServer);
			useServerPanel.add(serverList);
			useServerPanel.add(Box.createHorizontalGlue());
			
			JPanel useProxyPanel = new JPanel();
			useProxyPanel.setLayout(new BoxLayout(useProxyPanel, BoxLayout.X_AXIS));
			
			proxyButton = new JRadioButton(" Use Proxy");
			useProxyPanel.add(proxyButton);
			
			useProxyPanel.add(Box.createHorizontalGlue());
			
			JPanel proxyPanel = new JPanel();
			proxyPanel.setLayout(new BoxLayout(proxyPanel, BoxLayout.X_AXIS));
			proxyNameLabel = new JLabel("Name ");
			proxyPanel.add(proxyNameLabel);
			proxyNameField = new JTextField(20);
			proxyNameField.setText(proxyName);
			proxyPanel.add(proxyNameField);
			proxyPanel.add(Box.createHorizontalStrut(12));
			proxyPortLabel = new JLabel("Port ");
			proxyPanel.add(proxyPortLabel);
			proxyPortField = new JTextField(5);
			proxyPortField.setText(proxyPort);
			proxyPanel.add(proxyPortField);
			proxyPanel.setMaximumSize(new Dimension(400, 24));
			
			JPanel rememberPanel = new JPanel();
			rememberPanel.setLayout(new BoxLayout(rememberPanel, BoxLayout.X_AXIS));
			
			rememberCheckBox = new JCheckBox(" Remember my settings");
			rememberPanel.add(rememberCheckBox);
			rememberPanel.add(Box.createHorizontalGlue());
		    okButton = new JButton(" OK ");
			
			rememberPanel.add(okButton);
			
			JPanel outsideProxyPanel = new JPanel();
			outsideProxyPanel.setLayout(new BoxLayout(outsideProxyPanel, BoxLayout.Y_AXIS));
			outsideProxyPanel.setBorder(new CompoundBorder(
					new LineBorder(Color.BLACK, 1), new EmptyBorder(4, 4, 4, 4)));
			outsideProxyPanel.add(useProxyPanel);
			outsideProxyPanel.add(Box.createVerticalGlue());
			outsideProxyPanel.add(proxyPanel);
			outsideProxyPanel.add(Box.createVerticalStrut(4));
			
			frame = new JFrame();
			frame.setTitle("Server Configuration");
			
	    	Toolkit toolkit = Toolkit.getDefaultToolkit();
	        Dimension screenDimension = toolkit.getScreenSize();
	        frame.setLocation(((screenDimension.width - WIDTH) / 2),
	        		((screenDimension.height - HEIGHT) / 2));
	        
			frame.setSize(new Dimension(WIDTH, HEIGHT));
			
			
			JPanel innerPanel = new JPanel();
			frame.add(innerPanel);
			BoxLayout outerLayout = new BoxLayout(innerPanel, BoxLayout.Y_AXIS);
			innerPanel.setLayout(outerLayout);
			innerPanel.setBorder(new EmptyBorder( 8, 8, 8, 8));
			innerPanel.add(useServerPanel);
			innerPanel.add(Box.createVerticalStrut(12));
			innerPanel.add(outsideProxyPanel);
			innerPanel.add(Box.createVerticalStrut(12));
			innerPanel.add(rememberPanel);
			
			frame.setVisible(true);
			
			proxyButton.addActionListener(this);
			okButton.addActionListener(this);
			serverList.addActionListener(this);

			proxyButton.setSelected(useProxy);
			
			if (proxyButton.isSelected()) {
				proxyNameLabel.setEnabled(true);
				proxyNameField.setEnabled(true);
				proxyPortLabel.setEnabled(true);
				proxyPortField.setEnabled(true);
			} else {
				proxyNameLabel.setEnabled(false);
				proxyNameField.setEnabled(false);
				proxyPortLabel.setEnabled(false);
				proxyPortField.setEnabled(false);
			}
			
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		final String methodName = "actionPerformed";
		
		String actionCommand = e.getActionCommand().trim();

		if (actionCommand.equals("OK")) {
			Log.comment(this, methodName, "okButton pressed");
			// Close the dialog - and save the data
			HashMap<String, String> values = new HashMap<String, String>();

			proxyName = proxyNameField.getText();
			if (proxyName == null) proxyName = "";
			proxyPort = proxyPortField.getText();
			if (proxyPort == null) proxyPort = "";

			remember =  rememberCheckBox.isSelected();
			
			serverName = data[serverList.getSelectedIndex()];
			values.put("server", serverName);
			values.put("proxy", useProxy ? "yes" : "no");
			values.put("proxyname", proxyName);
			values.put("proxyport", proxyPort);
			values.put("remember", remember ? "yes" : "no");
			boolean successful = file.save(values);
			if (successful) {
				Log.comment(this, methodName, "saved successfully");
				close();
				finished = true;
				return;				
			} else {
				// Print a requester - could not save!
				Log.error(this, methodName, "Could not save!");
				close();
				finished = true;
				return;
			}
		} else if (actionCommand.equals("Use Proxy")) {
			Log.comment(this, methodName, "proxyButton pressed");
			// Dim / Undim the information below
			if (proxyButton.isSelected()) {
				useProxy = true;
				proxyNameLabel.setEnabled(true);
				proxyNameField.setEnabled(true);
				proxyPortLabel.setEnabled(true);
				proxyPortField.setEnabled(true);
			} else {
				useProxy = false;
				proxyNameLabel.setEnabled(false);
				proxyNameField.setEnabled(false);
				proxyPortLabel.setEnabled(false);
				proxyPortField.setEnabled(false);
				
			}
		} else if (actionCommand.equals("comboBoxChanged")) {
			serverName = data[serverList.getSelectedIndex()];
		} else {
			Log.comment(this, methodName, "ACTION: " + e);
		}
	}
	
	public String[] getData() {
		return data;
	}

	public boolean useProxy() {
		return useProxy;
	}
	
	public String getProxyName() {
		return proxyName;
	}
	
	public String getProxyPort() {
		return proxyPort;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void close() {
		proxyButton.removeActionListener(this);
		okButton.removeActionListener(this);
		serverList.removeActionListener(this);
		frame.setVisible(false);
		frame.dispose();
		frame = null;
	}
	
}
