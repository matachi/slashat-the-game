package se.danielj.slashatthegame.misc;

public class Progress {
	private static double progress;
	public static void init() {
		 progress = 0;
	}
	public static double getProgress() {
		return progress;
	}
	public static void setProgress(double progress) {
		Progress.progress = progress;
	}
}
