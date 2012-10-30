package brandon.gui;

import javax.swing.JApplet;
import java.applet.AudioClip;
import java.awt.Toolkit;
import java.io.IOException;

import brandon.utils.Log;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.net.URL;

public class AudioResource extends MediaResource {
	AudioClip audioClip = null; // For wav files
	Sequencer sequencer = null; // For midi files

	public AudioResource(String name, String location) {
		super(name, location);
	}

	public Object get() {
		return audioClip;
	}

	/** Load the sound. Supports mid and wav files */
	public void load() {
		URL url = null;
        
		if (1==1) {
			try {
                switch(media.getImagePackaging()) {
                    case FILE_SYSTEM:
                        url = new URL("file:" + location);
                        audioClip = java.applet.Applet.newAudioClip(url);
                        break;
                    case WEBSTART_JAR:
                        ClassLoader classLoader = media.getClassLoader();
                        if (classLoader == null) {
                            Log.error(this, "load", "ClassLoader is NULL! " + 
                                "Cannot load image [" + location + "]");
                        } else {
                            url = media.getClassLoader().getResource(location);
                            audioClip = java.applet.Applet.newAudioClip(url);
                        }
                        break;
                    case JAR_FILE:
                        break;
                }
			} catch (Exception e) {
				Log.error(this, "load", "Exception caught in AudioResource.load:" + e);
				e.printStackTrace();
			}
			return;
		}
		if (location.endsWith(".mid")) {
	    	Log.info(this, "load", "Loading a midi file");
	    	try {
                switch(media.getImagePackaging()) {
                case FILE_SYSTEM:
                    Log.comment(this, "load", "Loading file system file: [" + location + "]");
                    audioClip = java.applet.Applet.newAudioClip(new URL("file:" + location));
                    break;
                case WEBSTART_JAR:
                    ClassLoader classLoader = media.getClassLoader();
                    if (classLoader == null) {
                        Log.error(this, "load", "ClassLoader is NULL! " + 
                            "Cannot load image [" + location + "]");
                    } else {
                        url = media.getClassLoader().getResource(location);
                        audioClip = java.applet.Applet.newAudioClip(url);
                    }
                    break;
                case JAR_FILE:
                    break;
                }   
                
                //URL url = media.getClassLoader().getResource(location);
	    		//Sequencer sequencer = MidiSystem.getSequencer();
	    		//sequencer.setSequence(MidiSystem.getSequence(url));
	    		//sequencer.open();
	    		
	    	//} catch(MidiUnavailableException mue) {
	    	//	System.out.println("Midi device unavailable!");
	    	//} catch(InvalidMidiDataException imde) {
	    	//	System.out.println("Invalid Midi data!");
	    	//} catch(IOException ioe) {
	    	//	System.out.println("I/O Error!");
	    	} catch (Exception e) {
	    	    Log.error(this, "load", "Audio Exception: " + e);
            }
	    }
	}

	public void modify() {
	}

	public void play() {
        if (audioClip == null) {
            Log.error(this, "play", "ERROR! Could not play audio clip! Does not exist!");
            return;
        }
		if (1==1) {
			audioClip.play();
			return;
		}
		
		if (sequencer != null) {
				sequencer.start();
				while(true) {
					if(sequencer.isRunning()) {
						try {
							Thread.sleep(1000); // Check every second
						} catch(InterruptedException ignore) {
							break;
						}
					} else {
						break;
					}
				}
        
				// Close the MidiDevice & free resources
				sequencer.stop();
				sequencer.close();
		}
		
		if (audioClip != null) {
			audioClip.play();
		} else {
			Log.error(this, "play", "audioClip is NULL");
		}
	}

	public void stop() {
		if (1==1) {
			audioClip.stop();
			return;
		}
		if (sequencer != null) {
			// Close the MidiDevice & free resources
			sequencer.stop();
			sequencer.close();
		}
		if (audioClip != null) {
			audioClip.stop();
		}
	}
}
