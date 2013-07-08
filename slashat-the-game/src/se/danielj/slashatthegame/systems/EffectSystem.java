package se.danielj.slashatthegame.systems;

import se.danielj.slashatthegame.components.Effect;
import se.danielj.slashatthegame.components.Position;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

/**
 * 
 * @author Daniel Jonsson
 * @license GNU GPLv3
 *
 */
public class EffectSystem extends EntityProcessingSystem {
	@Mapper
	ComponentMapper<Position> pm;
	@Mapper
	ComponentMapper<Effect> em;

	private SpriteBatch batch;
	private OrthographicCamera camera;

	@SuppressWarnings("unchecked")
	public EffectSystem() {
		super(Aspect.getAspectForAll(Position.class, Effect.class));
		this.batch = new SpriteBatch();
		camera = new OrthographicCamera(1600, 900);
		camera.position.set(1600 / 2, 900 / 2, 0);
		camera.update();
	}

	@Override
	protected void begin() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
	}

	@Override
	protected void process(Entity e) {
		Position position = pm.get(e);
		Matrix4 m = new Matrix4();
		m.translate(position.getX() * 10 + 75, position.getY() * 10 + 75, 0);
		batch.setTransformMatrix(m);
		ParticleEffect particleEffect = em.get(e).getParticleEffect();
		particleEffect.draw(batch, world.getDelta());
//		if (particleEffect.isComplete()) {
//			em.get(e).dispose();
//			world.deleteEntity(e);
//		}
		m.translate(-position.getX(), -position.getY(), 0);
		batch.setTransformMatrix(m);
	}

	@Override
	protected void end() {
		batch.end();
	}

	public void dispose() {
		batch.dispose();
	}
}
