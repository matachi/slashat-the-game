package se.danielj.slashatthegame.misc;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * 
 * @author Daniel Jonsson
 * @license GNU GPLv3
 *
 */
public class SongAccessor implements TweenAccessor<Song> {
	public static final int VOLUME = 1;
	public static final int PLAY = 2;
	public static final int STOP = 3;
	@Override
	public int getValues(Song target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case VOLUME: returnValues[0] = target.getVolume(); return 1;
		default: return -1;
		}
	}
	@Override
	public void setValues(Song target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case VOLUME: target.setVolume(newValues[0]); break;
		case PLAY: target.play(); break;
		case STOP: target.stop(); break;
		default: break;
		}
	}
}
