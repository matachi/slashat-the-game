package se.danielj.slashatthegame.systems;

import java.util.Iterator;

import se.danielj.slashatthegame.components.Position;
import se.danielj.slashatthegame.components.Rotation;
import se.danielj.slashatthegame.components.Size;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 
 * @author Daniel Jonsson
 * @license GNU GPLv3
 *
 */
public class PhysicsSystem extends VoidEntitySystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Size> sm;
	@Mapper ComponentMapper<Rotation> rm;
	
	private World world;
//	private Box2DDebugRenderer debugRenderer;
	
	private OrthographicCamera camera;

	public PhysicsSystem(World world) {
		this.world = world;
//		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(16, 9);
		camera.position.set(16 / 2, 9.0f / 2, 0);
		camera.update();
	}
	
	@Override
	protected void processSystem() {
		world.step(super.world.getDelta(), 6, 2);
		Iterator<Body> bodies = world.getBodies();
		while(bodies.hasNext()) {
			Body body = bodies.next();
			if (body.getUserData() instanceof Entity) {
				Entity entity = (Entity)body.getUserData();
				Position position = pm.get(entity);
				Size size = sm.get(entity);
				Rotation rotation = rm.get(entity);
				position.setX(body.getPosition().x * 10 - size.getWidth() / 2);
				position.setY(body.getPosition().y * 10 - size.getHeight() / 2);
				rotation.setAngle((float) Math.toDegrees(body.getAngle()));
			}
		}
//		debugRenderer.render(world, camera.combined);
	}
}
