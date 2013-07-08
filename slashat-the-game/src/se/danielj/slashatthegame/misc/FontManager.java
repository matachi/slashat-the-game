package se.danielj.slashatthegame.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * 
 * @author Daniel Jonsson
 * @license GNU GPLv3
 *
 */
public class FontManager {

	private static BitmapFont normalFont;
	private static BitmapFont titleFont;
	private static BitmapFont creditsFont;
	
	public static void init() {
		if (normalFont == null) {
			FreeTypeFontGenerator g = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Rase-GPL.otf"));
			normalFont = g.generateFont(40);
			titleFont = g.generateFont(80);
			g = new FreeTypeFontGenerator(Gdx.files.internal("fonts/EptKazoo.ttf"));
			creditsFont = g.generateFont(40);
			g.dispose();
		}
	}
	
	public static void dispose() {
		normalFont.dispose();
		normalFont = null;
		titleFont.dispose();
		titleFont = null;
		creditsFont.dispose();
		creditsFont = null;
	}
	
	public static BitmapFont getNormalFont() {
		return normalFont;
	}
	
	public static BitmapFont getTitleFont() {
		return titleFont;
	}
	
	public static BitmapFont getCreditsFont() {
		return creditsFont;
	}
}
