package se.danielj.slashatthegame.components;

import com.artemis.Component;

/**
 * 
 * @author Daniel Jonsson
 * @license GNU GPLv3
 *
 */
public class Size extends Component {

	private float width;
	private float height;
	public Size() {
		this(0, 0);
	}
	public Size(float width, float height) {
		this.width = width;
		this.height = height;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
}
