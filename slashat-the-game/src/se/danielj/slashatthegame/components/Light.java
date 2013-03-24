package se.danielj.slashatthegame.components;

import com.artemis.Component;

public class Light extends Component {
	private float speed;
	private float alpha;
	private float time;
	public Light(float speed, float alpha) {
		this.speed = speed;
		this.alpha = alpha;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	public float getTime() {
		return time;
	}
	public void addTime(float time) {
		this.time += time;
	}
}
