package brandon.gameClient;

import javax.swing.*;
import java.awt.*;

import javax.swing.border.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;

/** Queries the server passed in, and makes sure we have a compatible environment */
public class CheckServerDialog implements ActionListener {
	public final static int WIDTH = 300;
	public final static int HEIGHT = 150;
	
	JFrame frame = null;
	JLabel infoLabel;
	JButton cancelButton;
	
	boolean done = false;
	boolean found = false;
	
	ServerRequest request;
	String versionRequired;
	String clientVersion;
	
	public CheckServerDialog(ServerRequest request,
			String versionRequired, String clientVersion) {
		this.request = request;
		this.versionRequired = versionRequired;
		this.clientVersion = clientVersion;
		
		showDialog();
	}
	
	public void showDialog() {
		frame = new JFrame();
		frame.setTitle("Testing server connection");
		
    	Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimension = toolkit.getScreenSize();
        frame.setLocation(((screenDimension.width - WIDTH) / 2),
        		((screenDimension.height - HEIGHT) / 2));
        
		frame.setSize(new Dimension(WIDTH, HEIGHT));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		
		panel.setBorder(new EmptyBorder(8,8,8,8));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(new JLabel("Connecting to server..."));
		
		// Add a 'connecting' picture, along with something moving.
		JPanel connectingGraphic = new JPanel();
		connectingGraphic.setLayout(new GridLayout(1, 6));
		panel.add(connectingGraphic);

		String dotSmall = "images/red_dot.jpg";
		String dotLarge = "images/large_red_dot.jpg";

    	URL dotSmallURL = ClassLoader.getSystemClassLoader().getResource(dotSmall);
    	Image dotSmallImage = toolkit.getImage(dotSmallURL);
    	ImageIcon dotSmallIcon = new ImageIcon(dotSmallImage);

    	URL dotLargeURL = ClassLoader.getSystemClassLoader().getResource(dotLarge);
    	Image dotLargeImage = toolkit.getImage(dotLargeURL);
    	ImageIcon dotLargeIcon = new ImageIcon(dotLargeImage);
    	
		JLabel dot[] = new JLabel[6];
		for(int i=0; (i < 6); i++) {
			dot[i] = new JLabel(dotSmallIcon);
			connectingGraphic.add(dot[i]);
		}
		
		infoLabel = new JLabel("Searching....");
		panel.add(infoLabel);

		// Add a 'cancel' button to return to the previous screen
		cancelButton = new JButton(" CANCEL ");
		panel.add(cancelButton);
		
		frame.setVisible(true);
		
		cancelButton.addActionListener(this);
		
		int animationDot = 0;
		int cycles = 0;
		
		new FindConnection().start();
		
		// Make sure that we cycle at least ONCE while looking for a server.
		// This is done to assure that the window doesn't flash really quickly
		// when a server is found immediately (i.e. finding localhost)
		while ((done == false) || ((done == true) && (cycles < 1))) {
			
			dot[animationDot].setIcon(dotLargeIcon);
			
			if (animationDot > 0) {
				dot[animationDot - 1].setIcon(dotSmallIcon);
			} else {
				dot[dot.length - 1].setIcon(dotSmallIcon);
			}
			
			animationDot++;
			if (animationDot > dot.length - 1) {
				animationDot = 0;
				cycles++;
			}

			// Wait between movements of the 'dot'
			try {
				Thread.sleep(300);
			} catch (Exception e) {
				// Most likely, the cancel button has been pressed.
			}
			
		}
		
		// Check for CANCEL button being pressed
		if (found == false) {
			notifyResult("Server not found.");		
			exit();
		}

		notifyResult("Connection established!");
	}
	
	public void notifyResult(String text) {
		infoLabel.setText(text);
		infoLabel.setForeground(Color.RED);
		
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			
		}

		frame.setVisible(false);
		frame.dispose();
		frame = null;
		
	}
	
	public void actionPerformed(ActionEvent e) {
		done = true;
	}
	
	public void exit() {
		cancelButton.removeActionListener(this);
		frame.setVisible(false);
		frame.dispose();
		frame = null;		
	}
	
	public void foundConnection() {
		done = true;
		found = true;
	}
	
	public void noConnection() {
		done = true;
		found = false;
	}
	
	class FindConnection extends Thread {
		FindConnection() {
			
		}
		
		public void run() {
			// Check to see if I can find this server
			try {
				String version = request.getServerVersion();
			} catch (Exception e) {
				noConnection();
			}
			
			foundConnection();
		}
		
	}
	
} 
