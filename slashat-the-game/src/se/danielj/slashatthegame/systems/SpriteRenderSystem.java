package se.danielj.slashatthegame.systems;

import se.danielj.slashatthegame.components.Position;
import se.danielj.slashatthegame.components.Rotation;
import se.danielj.slashatthegame.components.Size;
import se.danielj.slashatthegame.components.Sprite;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteRenderSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Size> sim;
	@Mapper ComponentMapper<Sprite> sm;
	@Mapper ComponentMapper<Rotation> rm;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;

	@SuppressWarnings("unchecked")
	public SpriteRenderSystem() {
		super(Aspect.getAspectForAll(Sprite.class, Size.class, Position.class));
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera(160, 90);
		batch = new SpriteBatch();
		camera.position.set(160 / 2, 90 / 2, 0);
		camera.update();
	}

	@Override
	protected void process(Entity e) {
		Sprite sprite = sm.get(e);
		Size size = sim.get(e);
		Position position = pm.get(e);
		Rotation rotation = rm.get(e);
		sprite.update();
		TextureRegion atlasSprite = sprite.getSprite();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.setColor(sprite.getColor());
		if (rotation != null) {
			batch.draw(atlasSprite, position.getX(), position.getY(), size.getWidth() / 2, size.getHeight() / 2, size.getWidth(), size.getHeight(), 1, 1, rotation.getAngle());
		} else {
			batch.draw(atlasSprite, position.getX(), position.getY(), size.getWidth(), size.getHeight());
		}
		batch.end();
	}
	
	public void dispose() {
		batch.dispose();
	}
}
