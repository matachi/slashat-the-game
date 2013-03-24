package se.danielj.slashatthegame.systems;

import se.danielj.slashatthegame.components.People;
import se.danielj.slashatthegame.components.Position;
import se.danielj.slashatthegame.misc.Progress;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;

public class PeopleSystem extends EntitySystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<People> pem;
	
	@SuppressWarnings("unchecked")
	public PeopleSystem() {
		super(Aspect.getAspectForAll(Position.class, People.class));
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); ++i) {
			process(entities.get(i), (float)Progress.getProgress());
		}
	}

	private void process(Entity e, float time) {
		Position position = pm.get(e);
		People people = pem.get(e);
		position.setY(people.getPosition() + people.getHeight() * (float)Math.abs(Math.cos(people.getSpeed() * time)));
	}
}
