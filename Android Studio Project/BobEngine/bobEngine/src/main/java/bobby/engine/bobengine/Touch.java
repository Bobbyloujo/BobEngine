/**
 * BobEngine - 2D game engine for Android
 * 
 * Copyright (C) 2014, 2015 Benjamin Blaszczak
 * 
 * BobEngine is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser Public License
 * version 2.1 as published by the free software foundation.
 * 
 * BobEngine is provided without warranty; without even the implied
 * warranty of merchantability or fitness for a particular 
 * purpose. See the GNU Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with BobEngine; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301 USA
 * 
 */

package bobby.engine.bobengine;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * This class listens for and handles touch input.
 * 
 * @author Ben
 * @modified 9/21/15
 */
public class Touch implements OnTouchListener {

	// Constants
	public final static int MAX_FINGERS = 10;       // The max number of fingers that can touch the screen.

	// Variables
	private int numTouches;                         // Current number of pointers on the screen
	private boolean held[];

	/** X positions of the pointers currently touching the screen. */
	private float X[];                                // Touch X positions
	/** Y positions of the pointers currently touching the screen. (0 is at the bottom of the screen)*/
	private float Y[];                                // Touch Y positions; NOTE: 0 at the TOP of the screen. That's the opposite of the graphics.

	// Objects
	private BobView view;                          // BobView that contains this touch listener.

	/**
	 * Create a new BobEngine touch listener.
	 */
	public Touch(BobView view) {
		held = new boolean[MAX_FINGERS];
		X = new float[MAX_FINGERS];
		Y = new float[MAX_FINGERS];
		this.view = view;
		numTouches = 0;

		for (int i = 0; i < MAX_FINGERS; i++) {
			X[i] = -1;
			Y[i] = -1;
			held[i] = false;
		}
	}

	/**
	 * Returns true if the area described by x1, y1, x2, and y2 is being touched.
	 * 
	 * @param x1
	 *            - X coord of the top-left corner of the box
	 * @param y1
	 *            - Y coord of the top-left corner
	 * @param x2
	 *            - X coord of the bottom-right corner
	 * @param y2
	 *            - Y coord of teh bottom-right corner
	 * @return true if touched by any finger, false otherwise
	 */
	public boolean areaTouched(double x1, double y1, double x2, double y2) {
		for (int i = 0; i < MAX_FINGERS; i++) {
			if (X[i] > x1 && X[i] < x2 && X[i] >= 0) {
				if (Y[i] < y1 && Y[i] > y2 && X[i] >= 0) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns the ID of the pointer if the index touching the area described by x1, y1, x2, and y2.
	 *
	 * @param x1
	 *            - X coord of the top-left corner of the box
	 * @param y1
	 *            - Y coord of the top-left corner
	 * @param x2
	 *            - X coord of the bottom-right corner
	 * @param y2
	 *            - Y coord of teh bottom-right corner
	 * @return ID of the pointer touching the area or -1 if the area is not being touched.
	 */
	public int areaTouchedByIndex(double x1, double y1, double x2, double y2) {
		for (int i = 0; i < MAX_FINGERS; i++) {
			if (X[i] > x1 && X[i] < x2 && X[i] >= 0) {
				if (Y[i] < y1 && Y[i] > y2 && X[i] >= 0) {
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * Determines if the pointer is touching the area.
	 *
	 * @param index The index of the pointer to check
	 * @param x1
	 *            X coord of the top-left corner of the box
	 * @param y1
	 *            Y coord of the top-left corner
	 * @param x2
	 *            X coord of the bottom-right corner
	 * @param y2
	 *            Y coord of the bottom-right corner
	 * @return True if the pointer's last coordinates fall within the specified
	 *         specified area.
	 */
	public boolean areaTouched(int index, double x1, double y1, double x2, double y2) {
		if (X[index] > x1 && X[index] < x2 && X[index] >= 0) {
			if (Y[index] < y1 && Y[index] > y2 && X[index] >= 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if GameObject o is touched.
	 * 
	 * @param o
	 *            - GameObject to check if is touched
	 * @return True if o is being touched by any pointer, false otherwise.
	 */
	public boolean objectTouched(GameObject o) {
		if (!o.followCamera) {
			double camLeft = o.getRoom().getCameraLeftEdge();
			double camBot = o.getRoom().getCameraBottomEdge();

			return areaTouched(o.x - Math.abs(o.width) / 2 - camLeft, o.y + Math.abs(o.height) / 2 - camBot, o.x + Math.abs(o.width) / 2 - camLeft, o.y - Math.abs(o.height) / 2 - camBot);
		}

		return areaTouched(o.x - Math.abs(o.width) / 2, o.y + Math.abs(o.height) / 2, o.x + Math.abs(o.width) / 2, o.y - Math.abs(o.height) / 2);
	}

	/**
	 * Returns the ID of the pointer touching object o.
	 *
	 * @param o GameObject to check if is touched
	 * @return The ID of the pointer touching object o, -1 if o is not being touched.
	 */
	public int objectTouchedByIndex(GameObject o) {
		if (!o.followCamera) {
			double camLeft = o.getRoom().getCameraLeftEdge();
			double camBot = o.getRoom().getCameraBottomEdge();

			return areaTouchedByIndex(o.x - Math.abs(o.width) / 2 - camLeft, o.y + Math.abs(o.height) / 2 - camBot, o.x + Math.abs(o.width) / 2 - camLeft, o.y - Math.abs(o.height) / 2 - camBot);
		}

		return areaTouchedByIndex(o.x - Math.abs(o.width) / 2, o.y + Math.abs(o.height) / 2, o.x + Math.abs(o.width) / 2, o.y - Math.abs(o.height) / 2);
	}

	/**
	 * Determines if the object is being touched by the specified pointer.
	 *
	 * @param o GameObject to check if is touched
	 * @return True if the coordinates of the pointer's last position fall inside the bounds of GameObject o
	 *         as defined by o's x, y, height, and width.
	 */
	public boolean objectTouched(int index, GameObject o) {
		if (!o.followCamera) {
			double camLeft = o.getRoom().getCameraLeftEdge();
			double camBot = o.getRoom().getCameraBottomEdge();

			return areaTouched(index, o.x - Math.abs(o.width) / 2 - camLeft, o.y + Math.abs(o.height) / 2 - camBot, o.x + Math.abs(o.width) / 2 - camLeft, o.y - Math.abs(o.height) / 2 - camBot);
		}

		return areaTouched(index, o.x - Math.abs(o.width) / 2, o.y + Math.abs(o.height) / 2, o.x + Math.abs(o.width) / 2, o.y - Math.abs(o.height) / 2);
	}

	/**
	 * Get the number of fingers, styli, etc touching the screen.
	 *
	 * @return number of pointers touching the screen.
	 */
	public int getNumTouches() {
		return numTouches;
	}
	
	/**
	 * Determine if any fingers, styli, etc are currently touching the screen.
	 *
	 * @return true if any pointers are currently touching the screen, false otherwise.
	 */
	public boolean held() {
		if (held(0)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if a number of fingers, styli, etc are touching the screen.
	 * 
	 * @param index - the number of fingers to check
	 * @return True if index number of fingers are touching the screen, false otherwise.
	 */
	public boolean held(int index) {
		if (index < 0 || index >= MAX_FINGERS) return false;

		if (held[index]) {
			return true;
		}
		
		return false;
	}

	/**
	 * Get the X position of the first (oldest) pointer on the screen. If the pointer is no longer on
	 * the screen, the X position of the pointer just before it was lifted will be returned.
	 *
	 * @return X position of pointer 0.
	 */
	public float getX() {
		return getX(0);
	}

	/**
	 * Get the last X position of pointer i. The pointer that has been on the screen the longest will
	 * be pointer 0, second longest will be 1, so on. If the pointer is no longer on the screen, the X
	 * position of the pointer just before it was lifted will be returned.
	 *
	 * @param i The index of the pointer
	 * @return The last X position of pointer i.
	 */
	public float getX(int i) {
		return X[i];
	}

	public float getY() {
		return getY(0);
	}

	public float getY(int i) {
		return Y[i];
	}

	/**
	 * Handle touch events.
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// Variables
		numTouches = event.getPointerCount();            // The current number of touches
		int index = event.getActionIndex();              // The finger that is touching the screen

		switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			for (int i = 0; i < MAX_FINGERS; i++) {
				if(i < numTouches) {
					held[i] = true;
				} else {
					held[i] = false;
				}
			}

			X[0] = event.getX();
			Y[0] = view.getHeight() - event.getY();

			if (view.getCurrentRoom() != null) view.getCurrentRoom().signifyNewpress(index);
			break;

		case MotionEvent.ACTION_MOVE:
			for (int i = 0; i < numTouches; i++) {
				X[i] = event.getX(i);
				Y[i] = view.getHeight() - event.getY(i);
			}

			break;

		case MotionEvent.ACTION_UP:


            if (view.getCurrentRoom() != null) view.getCurrentRoom().signifyReleased(index);

			for (int i = 0; i < MAX_FINGERS; i++) {
				if(i < numTouches - 1) {
					held[i] = true;
				} else {
					held[i] = false;
				}
			}

			numTouches = 0;

			break;

		case MotionEvent.ACTION_POINTER_DOWN:

			for (int i = 0; i < MAX_FINGERS; i++) {
				if(i < numTouches) {
					held[i] = true;
				} else {
					held[i] = false;
				}
			}

			X[index] = event.getX(index);
			Y[index] = view.getHeight() - event.getY(index);

			if (view.getCurrentRoom() != null) view.getCurrentRoom().signifyNewpress(index);
			break;

		case MotionEvent.ACTION_POINTER_UP:

            if (view.getCurrentRoom() != null) view.getCurrentRoom().signifyReleased(index);

			for (int i = 0; i < MAX_FINGERS; i++) {
				if(i < numTouches - 1) {
					held[i] = true;
				} else {
					held[i] = false;
				}
			}

			break;
		}

		return true;
	}
}
