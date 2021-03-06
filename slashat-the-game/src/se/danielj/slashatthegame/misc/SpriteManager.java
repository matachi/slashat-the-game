package se.danielj.slashatthegame.misc;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/**
 * 
 * @author Daniel Jonsson
 * @license GNU GPLv3
 *
 */
public class SpriteManager {
	
	private static TextureAtlas textureAtlas;
	private static Map<String, AtlasRegion> sprites;

	public static void init() {
		if (textureAtlas == null) {
			sprites = new HashMap<String, AtlasRegion>();
	        textureAtlas = new TextureAtlas(Gdx.files.internal("sprites/sprites.atlas"), Gdx.files.internal("sprites"));
	        for (AtlasRegion r : textureAtlas.getRegions()) {
	        	sprites.put(r.name, r);
	        }
		}
	}
	
	public static AtlasRegion getSprite(String sprite) {
		return sprites.get(sprite);
	}
	
	public static void dispose() {
		textureAtlas.dispose();
		textureAtlas = null;
		sprites.clear();
		sprites = null;
	}
}
