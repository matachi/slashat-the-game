package se.danielj.slashatthegame.components;

import se.danielj.slashatthegame.misc.SpriteManager;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprite extends Component {

	private Color color;
	protected Animation animation;
	private TextureRegion sprite;
	private Updater updater;
	public Sprite() {
		this("");
	}
	public Sprite(String sprite) {
		this(sprite, new Color(1, 1, 1, 1));
	}
	public Sprite(String sprite, Color color) {
		this.sprite = SpriteManager.getSprite(sprite);
		this.color = color;
		updater = new Updater() {
			@Override
			public void update() {
			}
		};
	}
	public Sprite(String sprite, boolean animation, int frames) {
		this.sprite = SpriteManager.getSprite(sprite);
		color = new Color(1, 1, 1, 1);
		updater = new Updater() {
			@Override
			public void update() {
			}
		};
	}
	public Sprite(Animation animation) {
		this(animation, new Updater() {
			@Override
			public void update() {
			}
		});
	}
	public Sprite(Animation animation, Updater updater) {
		this.animation = animation;
		this.updater = updater;
		color = new Color(1, 1, 1, 1);
	}
	public TextureRegion getSprite() {
		return sprite;
	}
	public void setSprite(TextureRegion sprite) {
		this.sprite = sprite;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public void setAlpha(float alpha) {
		this.color.a = alpha;
	}
	public Animation getAnimation() {
		return animation;
	}
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	public Updater getUpdater() {
		return updater;
	}
	public void setUpdater(Updater updater) {
		this.updater = updater;
	}
	public void update() {
		updater.update();
	}
	public interface Updater {
		public void update();
	}
}
