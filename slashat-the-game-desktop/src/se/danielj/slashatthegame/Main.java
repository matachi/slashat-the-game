package se.danielj.slashatthegame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Slashat: The Game";
		cfg.useGL20 = false;
		cfg.width = 960;
		cfg.height = 540;
		
		new LwjglApplication(new Game(), cfg);
	}
}
