package se.danielj.slashatthegame.components;

import com.artemis.Component;

public class People extends Component {

	private float speed;
	private float position;
	private float height;
	public People() {
		this(0, 0, 0);
	}
	public People(float position, float speed, float height) {
		this.position = position;
		this.speed = speed;
		this.height = height;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float getPosition() {
		return position;
	}
	public void setPosition(float position) {
		this.position = position;
	}
}
