package se.danielj.slashatthegame;

import se.danielj.slashatthegame.components.Effect;
import se.danielj.slashatthegame.components.Light;
import se.danielj.slashatthegame.components.People;
import se.danielj.slashatthegame.components.Position;
import se.danielj.slashatthegame.components.Rotation;
import se.danielj.slashatthegame.components.Size;
import se.danielj.slashatthegame.components.Sprite;
import se.danielj.slashatthegame.components.Sprite.Updater;
import se.danielj.slashatthegame.components.Table;
import se.danielj.slashatthegame.misc.Constants;
import se.danielj.slashatthegame.misc.Progress;
import se.danielj.slashatthegame.misc.SpriteManager;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

public class EntityCreator {
	
	public static Entity createButton(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(5, 5));
		e.addComponent(new Size(24, 24));
        e.addComponent(new Sprite("button"));
		return e;
	}
	
	public static Entity createRoomT(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(0, 0));
		e.addComponent(new Size(160, 90));
        e.addComponent(new Sprite("tommie_room"));
		return e;
	}
	
	public static Entity createTableT(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(86, 6));
		e.addComponent(new Size(31, 46));
        e.addComponent(new Sprite("tommie_table"));
		e.addComponent(new Table());
		return e;
	}
	
	public static Entity createTommieT(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(79, 11));
		e.addComponent(new Size(18, 37));
		
		TextureRegion[][] tmp = SpriteManager.getSprite("tommie_char").split(18, 37);
		Animation a = new Animation(1, tmp[0]);
		a.setPlayMode(Animation.LOOP_PINGPONG);
		final Sprite sprite = new Sprite(a);
		sprite.setUpdater(new Updater() {
        	@Override
        	public void update() {
        		sprite.setSprite(sprite.getAnimation().getKeyFrame((int)Progress.getProgress()));
        	}
		});
        e.addComponent(sprite);
		return e;
	}
	
	public static Entity createFire(World world, float x, float y) {
		Entity e = world.createEntity();
		e.addComponent(new Effect("fire"));
		e.addComponent(new Position(x, y));
		world.getManager(GroupManager.class).add(e, Constants.EFFECTS);
		return e;
	}
	
	public static Entity createBlood(World world, int x, int y) {
		Entity e = world.createEntity();
		e.addComponent(new Effect("blood"));
		e.addComponent(new Position(x, y));
		world.getManager(GroupManager.class).add(e, Constants.EFFECTS);
		return e;
	}
	
	public static Entity createGlow(World world, int x, int y) {
		Entity e = world.createEntity();
		e.addComponent(new Effect("glow"));
		e.addComponent(new Position(x, y));
		world.getManager(GroupManager.class).add(e, Constants.EFFECTS);
		return e;
	}
	
	public static Entity createLegs(World world, com.badlogic.gdx.physics.box2d.World box2dWorld) {
		/*
		 * World
		 */
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);
		bodyDef.angularDamping = 0;
		bodyDef.linearDamping = 0;
		Body body = box2dWorld.createBody(bodyDef);

		EdgeShape shape = new EdgeShape();
		shape.set(0, 1, 16, 0);
		body.createFixture(shape, 0);
		
		shape = new EdgeShape();
		shape.set(0, 1, 0, 9);
		body.createFixture(shape, 0);
		
		shape = new EdgeShape();
		shape.set(16, 1, 16, 9);
		body.createFixture(shape, 0);
		
		shape = new EdgeShape();
		shape.set(0, 9, 16, 9);
		body.createFixture(shape, 0);
		
		/*
		 * Legs
		 */
		Entity e = world.createEntity();
		
		float x = 85;
		float y = 20;
		
		e.addComponent(new Position(x, y));
		e.addComponent(new Size(4, 18));
		e.addComponent(new Rotation());
        e.addComponent(new Sprite("tommie_legs"));
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x / 10, y / 10);
		body = box2dWorld.createBody(bodyDef);
		body.setUserData(e);
//		body.applyForce(100, 100, 1, 1);
		body.applyLinearImpulse(1, -3, 1, 1);
		
		PolygonShape polygonShape = new PolygonShape();
		Vector2[] vertices = {new Vector2(0, 0.9f), new Vector2(-0.1f, 0.9f), new Vector2(-0.2f, -0.5f), new Vector2(0.2f, -0.9f)};
		polygonShape.set(vertices);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.9f;
		fixtureDef.restitution = 0.9f;
		body.createFixture(fixtureDef);
		
//		bodyDef.type = BodyType.StaticBody;
//		bodyDef.position.set(x / 60, 9);
//		Body body2 = box2dWorld.createBody(bodyDef);
//		DistanceJointDef distanceJointDef = new DistanceJointDef();
//		distanceJointDef.bodyA = body;
//		distanceJointDef.bodyB = body2;
//		distanceJointDef.length = 9 - y / 10;
//		distanceJointDef.dampingRatio = 0.1f;
//		distanceJointDef.frequencyHz = 0.6f;
		
//		box2dWorld.createJoint(distanceJointDef);
		
		return e;
	}
	
	public static Entity createPeople1(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(0, -15));
		e.addComponent(new Size(160, 53));
		e.addComponent(new People(-15, 3, 7));
        e.addComponent(new Sprite("airbase_people1"));
		return e;
	}
	
	public static Entity createPeople2(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(0, -24));
		e.addComponent(new Size(160, 64));
		e.addComponent(new People(-24, 2.1f, 6));
        e.addComponent(new Sprite("airbase_people2"));
		return e;
	}
	
	public static Entity createPeople3(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(0, -15));
		e.addComponent(new Size(160, 38));
		e.addComponent(new People(-15, 2.5f, 5));
        e.addComponent(new Sprite("airbase_people3"));
		return e;
	}
	
	public static Entity createScene(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(0, 0));
		e.addComponent(new Size(160, 90));
        e.addComponent(new Sprite("airbase_scene"));
		return e;
	}
	
	public static Entity createLight1(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(0, 0));
		e.addComponent(new Size(160, 90));
		e.addComponent(new Light(1, 0.2f));
        e.addComponent(new Sprite("airbase_light1", new Color(1, 1, 1, 0.4f)));
		return e;
	}
	
	public static Entity createLight2(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(0, 0));
		e.addComponent(new Size(160, 90));
		e.addComponent(new Light(5.1f, 0.2f));
        e.addComponent(new Sprite("airbase_light2", new Color(1, 1, 1, 0.4f)));
		return e;
	}
	
	public static Entity createLight3(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(0, 0));
		e.addComponent(new Size(160, 90));
		e.addComponent(new Light(0.45f, 0.2f));
        e.addComponent(new Sprite("airbase_light3", new Color(1, 1, 1, 0.4f)));
		return e;
	}
	
	public static Entity createLight4(World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(0, 0));
		e.addComponent(new Size(160, 90));
		e.addComponent(new Light(0.25f, 0.2f));
        e.addComponent(new Sprite("airbase_light4", new Color(1, 1, 1, 0.4f)));
		return e;
	}
	
	public static Entity createJezper(final World world) {
		Entity e = world.createEntity();
		e.addComponent(new Position(70, 48));
		e.addComponent(new Size(23, 17));
		
		TextureRegion[][] tmp = SpriteManager.getSprite("airbase_jezper").split(23, 17);
		Animation a = new Animation(0.5f, tmp[0]);
		a.setPlayMode(Animation.LOOP);
		final Sprite sprite = new Sprite(a);
		sprite.setUpdater(new Updater() {
        	@Override
        	public void update() {
        		sprite.setSprite(sprite.getAnimation().getKeyFrame((float)Progress.getProgress()));
        	}
		});
        e.addComponent(sprite);
		return e;
	}
	
	public static void createWorld(com.badlogic.gdx.physics.box2d.World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);
		bodyDef.angularDamping = 0;
		bodyDef.linearDamping = 0;
		Body body = world.createBody(bodyDef);

		EdgeShape shape = new EdgeShape();
		shape.set(0, 0, 16, 0);
		body.createFixture(shape, 0);
		
		shape = new EdgeShape();
		shape.set(0, 0, 0, 9);
		body.createFixture(shape, 0);
		
		shape = new EdgeShape();
		shape.set(16, 0, 16, 9);
		body.createFixture(shape, 0);
		
		shape = new EdgeShape();
		shape.set(0, 9, 16, 9);
		body.createFixture(shape, 0);
	}
	
	public static Entity createBall(World world, com.badlogic.gdx.physics.box2d.World box2dWorld, float x, float y) {
		Entity e = world.createEntity();
		
		e.addComponent(new Position(x, y));
		e.addComponent(new Size(15, 15));
		e.addComponent(new Rotation());
        e.addComponent(new Sprite("airbase_ball"));
		e.addComponent(new Effect("effect"));
		world.getManager(GroupManager.class).add(e, Constants.EFFECTS);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x / 10, y / 10);
		Body body = box2dWorld.createBody(bodyDef);
		body.setUserData(e);
//		body.applyForce(100, 100, 1, 1);
		body.applyLinearImpulse(3, 3, 1, 1);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(0.75f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 1;
		body.createFixture(fixtureDef);
		
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(x / 10, 9);
		Body body2 = box2dWorld.createBody(bodyDef);
		DistanceJointDef distanceJointDef = new DistanceJointDef();
		distanceJointDef.bodyA = body;
		distanceJointDef.bodyB = body2;
		distanceJointDef.length = 9 - y / 10;
		distanceJointDef.dampingRatio = 0.1f;
		distanceJointDef.frequencyHz = 0.6f;
		
		box2dWorld.createJoint(distanceJointDef);
		
		return e;
	}
}
