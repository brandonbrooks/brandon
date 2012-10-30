package brandon.gui;

/** Represents a generic media item. This item can be either found
 *  at a web URL or inside the JAR file this program contains. */
public abstract class MediaResource {
    public String name;
    public String location;
    protected Media media;
    
    public MediaResource(String name, String location) {
        this.name = name;
        this.location = location;
    }
  
    public String getName() {
 	    return name;
    }

    /** operation to load the media object */
    public abstract void load();

    /** returns the media data object after being loaded */
    public abstract Object get();

    /** Modify the media item */
    public abstract void modify();
    
    /** Set the class loader */
    public void setMedia(Media media) {
        this.media = media;
    }
}
