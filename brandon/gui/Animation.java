package brandon.gui;

import java.awt.*;
import brandon.utils.Log;
import java.util.Iterator;
import java.util.ArrayList;

public class Animation extends Thread {
	private Animation animationThread = null;
	
	private SpriteEnabled component;
  	private boolean animationActive = false;
  	private boolean threadAlive = true;
  	private int spriteNumber;
  	private ArrayList currentAnimations = new ArrayList();
  	private int animationNumber = 0;
  	
  	private class AnimationParameters {
  		public int animationNumber;
  		public Image[] images;
  		public int[] delay;
  		public int loops;
  		public int currentFrame;
  		public int currentLoop;
  		public Sprite sprite;
  		public int delaySinceLast;
  	}
  
  	/** Start an animation Generically on a component.
  	 *  images = List of images to cycle through
  	 *  delay = delay in milliseconds after displaying each image
  	 *  loops = number of times to loop. 0=forever
  	 *  x,y = location on component to draw the images
  	 */
  	public Animation(SpriteEnabled component) {
  		this.component = component;

  		// Start the animation thread
  		new Thread(this).start();
  	}

  	/** Start a new animation. This implies a stop of the previous
  	 *  animation and this animation replaces it.
  	 */
  	public synchronized int startAnimation(Image[] images, int[] delay,
                                          int loops, int x, int y) {
  		// Set up the animation
  		AnimationParameters animParams = new AnimationParameters();
  		animParams.images = images;
  		animParams.delay = delay;
  		animParams.loops = loops;
  		animParams.currentFrame = 0;
  		animParams.currentLoop = 0;
  		animParams.delaySinceLast = 0;
  		animParams.animationNumber = animationNumber++;
  		
  		animParams.sprite = new Sprite(x, y, images[0]);
  		spriteNumber = component.addSprite(animParams.sprite);
  		
		animParams.sprite.image = animParams.images[0];
  		currentAnimations.add(animParams);

  		animationActive = true;
  		animationThread.interrupt();
  		
  		return animParams.animationNumber;
  	}

  	/** Stops all animation on the game board */
  	public synchronized void stopAnimation() {
  		animationActive = false;
  	}
  
  	/** Stop one particular animation */
  	public synchronized void stopAnimation(int animationNumber) {

  		for(Iterator i = currentAnimations.iterator(); i.hasNext();) {
  			AnimationParameters animParams = (AnimationParameters) i.next();
  			if (animParams.animationNumber == animationNumber) {
  				component.removeSprite(animParams.sprite.number);
  				i.remove();
  				break;
  			}
  		}
  		
  		if (currentAnimations.size() == 0) {
  			animationActive = false;
  		}
  	}

  	/** Stop the Animation thread */
  	public void stopAnimationThread() {
  		threadAlive = false;
  	}

  	/** Main animation thread */
  	public void run() {
  		final String methodName = "run";
  		/** buffer = image to draw to
  		 *  background = background image to draw on the buffer first
  		 *      before we draw our animation
    	 */
  		Log.comment(this, methodName, "thread started");
  		
  		animationThread = this;
  		setName("AnimationThread");
  		
  		int sleepTime = 100;
  		
  		threadAlive = true;

  		while(threadAlive) {
  			Log.comment(this, methodName, "threadAlive = true");
  			while (animationActive) {
  				Log.comment(this, methodName, "animationActive = true");
  				
  				synchronized(this) {
  					
  					// Go through each of the animations
  					for(Iterator i = currentAnimations.iterator(); i.hasNext();) {
  						AnimationParameters animParams = (AnimationParameters) i.next();

  						// If delay = null, then we don't loop or change images
  						// This is a STATIC image
  						if (animParams.delay == null) {
  							continue;
  						}
  						
  						animParams.delaySinceLast++;

  						// Do we need to go to the next image?
  						if (animParams.delay[animParams.currentFrame] >= animParams.delaySinceLast) {
  							animParams.currentFrame++;
  							animParams.delaySinceLast = 0;
  						}

  						
  	  					// Handle Loops  						
  	  					if (animParams.currentFrame >= animParams.images.length) {
  	  						animParams.currentLoop++;
  	  						animParams.currentFrame = 0;
  	  						
  	  						// If we have reached the number of loops - 
  	  						// delete this animation
  	  						// Note: -1 means an infinite loop
  	  						if ((animParams.loops != -1) && 
							    (animParams.currentLoop > animParams.loops)) {
  	  							stopAnimation(animParams.animationNumber);
  	  						}
  	  					}
  	  					
  						// Change the sprite image
  						animParams.sprite.image = animParams.images[animParams.currentFrame];
  					}
  					
  					// TODO: If using animation, we need to refigure out this method call
  					// component.drawSprites();

  				}
  				
  				// Sleep until we need to draw the next image
  				try {
  					Thread.sleep(sleepTime);
  				} catch (InterruptedException e) {
  					// Do nothing if this exception occurs - we expect this one
  				}
  			}
  			
  			synchronized(this) {
  				while (!animationActive) {
  					// Wait until animation is active again
  					try {
  						wait();
  					} catch (InterruptedException e) {
  						// Do nothing if this exception occurs - we expect this one
  					}
  				}
  			}
  		}
  	}
}
