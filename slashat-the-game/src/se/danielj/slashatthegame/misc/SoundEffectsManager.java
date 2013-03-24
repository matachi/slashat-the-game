package se.danielj.slashatthegame.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundEffectsManager {

	private static Sound explodeSound;
	
	public static void init() {
		if (explodeSound == null) {
			explodeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explode.wav"));
		}
	}
	
	public static void explode() {
		explodeSound.play();
	}
	
	public static void dispose() {
		explodeSound.dispose();
		explodeSound = null;
	}
}
