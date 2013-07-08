package se.danielj.slashatthegame.systems;

import se.danielj.slashatthegame.components.Light;
import se.danielj.slashatthegame.components.Sprite;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

/**
 * 
 * @author Daniel Jonsson
 * @license GNU GPLv3
 *
 */
public class LightSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Light> lm;
	@Mapper ComponentMapper<Sprite> sm;

	@SuppressWarnings("unchecked")
	public LightSystem() {
		super(Aspect.getAspectForAll(Light.class, Sprite.class));
	}

	@Override
	protected void process(Entity e) {
		Light light = lm.get(e);
		Sprite sprite = sm.get(e);
		light.addTime(world.getDelta() * light.getSpeed());
		sprite.setAlpha(light.getAlpha() * ((float) Math.sin(light.getTime()) + 1));
	}
}
