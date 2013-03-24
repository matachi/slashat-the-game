package se.danielj.slashatthegame.misc;

import com.badlogic.gdx.audio.Music;

public class Song {
	private Music song;
	private float volume;
	public Song(Music song) {
		this.song = song;
		volume = 0;
		song.setVolume(volume);
		song.setLooping(true);
	}
	public float getVolume() {
		return volume;
	}
	public void setVolume(float volume) {
		this.volume = volume;
		song.setVolume(volume);
	}
	public void play() {
		song.play();
	}
	public void stop() {
		song.stop();
	}
}
