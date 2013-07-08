package se.danielj.slashatthegame.components;

import com.artemis.Component;

/**
 * 
 * @author Daniel Jonsson
 * @license GNU GPLv3
 *
 */
public class Rotation extends Component {

	private float angle;
	public Rotation() {
		this(0);
	}
	public Rotation(float angle) {
		this.angle = angle;
	}
	public float getAngle() {
		return angle;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
}
