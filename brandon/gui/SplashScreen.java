package brandon.gui;

import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.Collection;
import brandon.utils.Log;

public class SplashScreen extends JFrame {
    private Image splashImage;
    private String version;
    private int splashImageWidth = 0;
    private int splashImageHeight = 0;
    private int loadedImages = 0;
    private boolean isFinished = false;
    private Collection<ImageResource> imageVector = new ArrayList<ImageResource>();
    private Collection<AudioResource> soundVector = new ArrayList<AudioResource>();
    private JProgressBar progressBar = null;
    private Font versionFont = new Font("Arial", Font.ITALIC, 10);
    private Font brooksriverFont = new Font("Arial", Font.BOLD, 10);
    private String serverName = null;
    
    /** Displays the splash screen.
     *  @param applet   		the main applet of this program
     *  @param splashImageURL   Image to be displayed as the 'splash screen'.
     *                          Size of image is the splash screen size.
     *  @param media			Media (image files and sound files) to load in.
     *                          Loaded media are stored in the same structure passed in.
     *  @param version  		the version of this program
     */
    public SplashScreen(ClassLoader classLoader, String splashImageName,
                        Media media, String version) {
    	String methodName = "SplashScreen(" + splashImageName + ",Media, String, String, String[])";
    	this.version = version;
    	Log.info(this, methodName, "entered");
    	if (media == null) {
    		Log.error(this, methodName, "Media is NULL - no media to load in SplashScreen");
    		return;
    	}
    	
    	// Grab the Splash Screen image so we can show this immediately
    	Toolkit toolkit = Toolkit.getDefaultToolkit();
    	//URL splashImageURL = ClassLoader.getSystemClassLoader().getResource(splashImageName);
        if (classLoader == null) {
            splashImage = Toolkit.getDefaultToolkit().createImage(splashImageName);
        } else {
            splashImage = toolkit.createImage(classLoader.getResource(splashImageName));
        }
    	MediaTracker mediaTracker = new MediaTracker(this);
    	mediaTracker.addImage(splashImage, 0);
    	try {
    		mediaTracker.waitForID(0);
    	} catch (InterruptedException ie) {
    		// Display an error message if we can't load the image
    		Log.error(this, methodName, "Media Tracker interrupted while loading media.");
    		return;
    	}
        
        splashImageWidth = splashImage.getWidth(this) + 8;
        splashImageHeight = splashImage.getHeight(this) + 24;

        // Initial Splash Screen Image is now loaded -
        // create the Splash Screen
        JPanel panel = new JPanel(new BorderLayout());
        setContentPane(panel);
        panel.setBorder(new LineBorder(Color.black, 4));

        JPanel imagePanel = new ImagePanel();
        panel.add(imagePanel, BorderLayout.CENTER);
        imagePanel.setPreferredSize(new Dimension(splashImageWidth, splashImageHeight));

        setUndecorated(true);
        setResizable(false);

        setSize(splashImageWidth, splashImageHeight + 40);

        // Create the progress bar
        progressBar = new JProgressBar(0, media.size());
        progressBar.setValue(0);
        progressBar.setIndeterminate(false);
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        panel.add(progressBar, BorderLayout.SOUTH);

        Dimension screenDimension = toolkit.getScreenSize();
        setLocation(((screenDimension.width - splashImageWidth) / 2),
        		((screenDimension.height - splashImageHeight) / 2));

        setVisible(true);
		
        // Display the server connection dialog (if necessary)
        /*ServerConnectionDialog dialog = new ServerConnectionDialog(configFileName, data);
        
        try {
        	while(dialog.isFinished() == false) {
        		Thread.sleep(500);
        	}
        } catch (Exception e) {
        	
        }
        
        serverName = dialog.getServerName();
        
        
		if (dialog.useProxy()) {
			Properties props = System.getProperties( );
			props.put("proxySet", "true");
			props.put("proxyHost", dialog.getProxyName());
			props.put("proxyPort", dialog.getProxyPort());
			System.setProperties(props);
		
			Log.info(this, methodName, "Set proxy to be " + dialog.getProxyName() +
					":" + dialog.getProxyPort());
	
		}
        
        // Wait - display for 1/2 a second so we can see the splash screen
        // Speeded up quite a bit, since all the images are now in the 
        // jar file
        try {
        	Thread.sleep(500);
        } catch (Exception e) {
        	
        }
        */
        
        // Now Proceed to load all the resources.
        int imageResources = 0;

        mediaTracker = new MediaTracker(this);

        for(MediaResource resource : media.getValues()) {
        	resource.load();
            if (resource instanceof ImageResource) {
                mediaTracker.addImage( (Image) resource.get(), imageResources++);
            }        	
        }

        // Wait for all the images resources to load
        // (the only images we can track are image resources)
        for(int i=0;(i < imageResources);i++) {
        	try {
        		mediaTracker.waitForID(i);
        	} catch (InterruptedException ie) {
        		return;
        	}
        	loadedImages++;
        	progressBar.setValue(loadedImages);
        }

        // Modify each resource as necessary
        for(MediaResource resource : media.getValues()) {
        	resource.modify();
        	progressBar.setValue(loadedImages);
        }

        isFinished = true;
	}

    /** Overriden so the screen doesn't blank when repainting */
    public void update(Graphics g) {
    	paint(g);
    }

    /** Inner class is an the ImagePanel inside the Splash Screen JFrame */
    class ImagePanel extends JPanel {
    	/** Overriden to paint the splash screen */
    	public void paint(Graphics g) {
    		// Allow Java to draw it's own components
    		super.paint(g);

    		// Now, draw my stuff
    		g.drawImage(splashImage, 0, 24, null);

    		// Write the heading
    		g.setColor(Color.blue);
    		g.fillRect(0,0,splashImageWidth, 24);
    		g.setColor(Color.white);
    		g.setFont(brooksriverFont);
    		g.drawString("BROOKSRIVER GAMES", 50, 10);
    		g.drawString("PRESENTS", 120, 20);
    		
    		// Write the Version of this applet
    		//g.setColor(Color.white);
    		//g.setFont(versionFont);
    		//g.drawString("Version: " + version, 10, splashImage.getHeight(null) - 30);
    	}
    }

    /** Returns TRUE if the splash screen has finished loading all the media, FALSE otherwise */
    public boolean isFinished() {
		return isFinished;
	}

    /** Returns a collection of image objects loaded for this applet */
    public Collection<ImageResource> getImageList() {
    	return imageVector;
    }

    /** Returns a collection of sound objects loaded for this applet */
    public Collection<AudioResource> getSoundList() {
    	return soundVector;
    }
    
}
