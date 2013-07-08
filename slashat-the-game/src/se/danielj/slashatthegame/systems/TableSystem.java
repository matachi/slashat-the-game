package se.danielj.slashatthegame.systems;

import se.danielj.slashatthegame.components.Position;
import se.danielj.slashatthegame.components.Table;
import se.danielj.slashatthegame.misc.Progress;

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
public class TableSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> pm;

	@SuppressWarnings("unchecked")
	public TableSystem() {
		super(Aspect.getAspectForAll(Position.class, Table.class));
	}

	@Override
	protected void process(Entity e) {
		Position position = pm.get(e);
		position.setY(6 + (int)Progress.getProgress());
	}
}
