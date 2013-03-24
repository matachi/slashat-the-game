package se.danielj.slashatthegame.misc;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {

	public static final String THEME = "a";
	public static final String TOMMIE = "b";
	public static final String JEZPER = "c";
	private static Map<String, Music> tracks;
	
	public static void init() {
		if (tracks == null) {
			tracks = new HashMap<String, Music>();
			tracks.put(THEME, Gdx.audio.newMusic(Gdx.files.internal("music/AlexandrZhelanovThemeMenu.ogg")));
			tracks.put(TOMMIE, Gdx.audio.newMusic(Gdx.files.internal("music/YubatakeDissonantWaltz.ogg")));
			tracks.put(JEZPER, Gdx.audio.newMusic(Gdx.files.internal("music/ClearsideBelowTheShift.ogg")));
		}
	}
	
	public static Music getSong(String song) {
		return tracks.get(song);
	}
	
	public static void dispose() {
		for (Music m : tracks.values()) {
			m.dispose();
		}
		tracks = null;
	}
}
