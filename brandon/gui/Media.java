package brandon.gui;

import brandon.utils.Log;

import java.awt.Image;
import java.awt.MediaTracker;
import java.util.TreeMap;
import java.util.Set;
import java.util.Collection;
import javax.swing.JButton;

/** Media Class holds all the media (images, sounds) in the application */
public class Media {
	protected TreeMap<String, MediaResource> media = new TreeMap<String, MediaResource>();
    public enum ImagePackaging { FILE_SYSTEM, JAR_FILE, WEBSTART_JAR };
    private ImagePackaging packaging = ImagePackaging.FILE_SYSTEM;
    private ClassLoader classLoader = null;
    
    public Media(ImagePackaging packaging, ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.packaging = packaging;
    }
    
	/** Add a new MediaResource to the Media set */
	public void add(MediaResource resource) {
		if (resource == null) {
			Log.error(this, "add(MediaResource)", "Trying to add a NULL MediaResource!");
			return;
		}

		String name = resource.getName();
		media.put(name, resource);
        resource.setMedia(this);
	}

	/** Returns a list of all names of the media */
	public Set getNames() {
		return media.keySet();
	}

	/** Get a MediaResource in this set given it's name */
	public MediaResource get(String name) {
		return (MediaResource) media.get(name);
	}

	/** Get an ImageResource Object in this set. This is a helper function.
	 *  It's not good object-orientation, but it helps the calling code
	 *  avoid a cast to an ImageResource object.
	 */
	public ImageResource getImage(String name) {
		String methodName = "getImage(" + name + ")";
		if (name == null) {
			Log.warning(this, methodName, "Can not retrieve a NULL image! USING ImageNotFound instead");
			name = "ImageNotFound";
		}
		ImageResource ir = null;
		try {
			ir = (ImageResource) media.get(name);
			if (ir == null) {
				Log.warning(this, methodName, "Image [" + name + "] could not be found! USING ImageNotFound instead");
				ir = (ImageResource) media.get("ImageNotFound");
			}
		} catch (Exception e) {
			Log.warning(this, methodName, "Exception occured while retrieving Image: " + e);
		}
		return ir;
	}

	/** Get an AudioResource Object in this set. This is a helper function.
	 *  It's not good object-orientation, but it helps the calling code
   	*  avoid a cast to an ImageResource object.
   	*/
	public AudioResource getAudio(String name) {
		String methodName = "getAudio(" + name + ")";
		AudioResource ar = null;
		try {
			ar = (AudioResource) media.get(name);
			if (ar == null) {
				Log.warning(this, methodName, "Audio could not be found!");
			}
		} catch (Exception e) {
			Log.warning(this, methodName, "Exception occured while retrieving Audio:" + e);
		}
		return ar;
	}

	/** Returns the number of media elements */
	public int size() {
		return media.size();
	}

	/** Returns the names of all the media elements */
	public Collection<MediaResource> getValues() {
		return media.values();
	}

	public Set<String> keySet() {
		return media.keySet();
	}
    
    /** Load all the media */
    public void load() {
        for(MediaResource resource : media.values()) {
            resource.load();
        }
    }

    public void loadAndWait() {
        // Now Proceed to load all the resources.
        int imageResources = 0;

        MediaTracker mediaTracker = new MediaTracker(new JButton());

        for(MediaResource resource : media.values()) {
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
        }
    }

    public ImagePackaging getImagePackaging() {
        return packaging;
    }
    
    public ClassLoader getClassLoader() {
        return classLoader;
    }

}
