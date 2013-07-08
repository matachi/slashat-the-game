package se.danielj.slashatthegame;

import java.util.Iterator;
import java.util.List;

import se.danielj.slashatthegame.components.Effect;
import se.danielj.slashatthegame.misc.Constants;
import se.danielj.slashatthegame.misc.FontManager;
import se.danielj.slashatthegame.misc.LabelAccessor;
import se.danielj.slashatthegame.misc.MusicManager;
import se.danielj.slashatthegame.misc.Progress;
import se.danielj.slashatthegame.misc.Song;
import se.danielj.slashatthegame.misc.SongAccessor;
import se.danielj.slashatthegame.misc.SoundEffectsManager;
import se.danielj.slashatthegame.misc.SpriteManager;
import se.danielj.slashatthegame.systems.EffectSystem;
import se.danielj.slashatthegame.systems.LightSystem;
import se.danielj.slashatthegame.systems.PeopleSystem;
import se.danielj.slashatthegame.systems.PhysicsSystem;
import se.danielj.slashatthegame.systems.SpriteRenderSystem;
import se.danielj.slashatthegame.systems.TableSystem;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Quart;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 
 * @author Daniel Jonsson
 * @license GNU GPLv3
 *
 */
public class Game implements ApplicationListener, InputProcessor {
	private World world;
	private com.badlogic.gdx.physics.box2d.World box2dWorld;
	private Stage stage;
	private TweenManager tweenManager;
	private Logic logic;
	
	private InputMultiplexer inputMultiplexer;
	
	private int level = 0;
	
	private boolean won;
	private double timer;
	
	private double progressSpeed;
	private boolean progressing;
	private int health;
	
	@Override
	public void create() {		
		SpriteManager.init();
		FontManager.init();
		SoundEffectsManager.init();
		MusicManager.init();
		Gdx.input.setCatchBackKey(true);
		
		if (world != null) {
			disposeSystems(world);
		}
		world = new World();
		world.setManager(new GroupManager());
		box2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -3), true);
		stage = new Stage(960, 540, false);
		if (tweenManager == null) {
			tweenManager = new TweenManager();
		}
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		final LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = FontManager.getNormalFont();
		labelStyle.fontColor = new Color(0, 0, 0, 1);
		
		if (level == 0) {
			final Song song = new Song(MusicManager.getSong(MusicManager.THEME));
			Tween.registerAccessor(Song.class, new SongAccessor());
			Timeline.createSequence()
				.push(Tween.set(song, SongAccessor.PLAY))
				.push(Tween.set(song, SongAccessor.VOLUME).target(0))
				.push(Tween.to(song, SongAccessor.VOLUME, 2f).target(1f).ease(Quart.INOUT))
				.start(tweenManager);
					
			ButtonStyle style = new ButtonStyle();
			style.up = new TextureRegionDrawable(SpriteManager.getSprite("logo"));
			style.checked = new TextureRegionDrawable(SpriteManager.getSprite("logo"));
			final Button button = new Button(style);
			button.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					event.getListenerActor().setTouchable(Touchable.disabled);
					level = 1;
					create();
					Timeline.createSequence()
						.push(Tween.to(song, SongAccessor.VOLUME, 3f).target(0f).ease(Quart.INOUT))
						.push(Tween.set(song, SongAccessor.STOP))
						.start(tweenManager);
					return false;
				}
			});
			button.setBounds(0, 0, stage.getWidth(), stage.getHeight());
			stage.addActor(button);
			
			logic = new Logic() {
				@Override
				public void render() {
					timer += Gdx.graphics.getDeltaTime();
					tweenManager.update(Gdx.graphics.getDeltaTime());
					stage.draw();
					stage.act(world.getDelta());
				}
			};
		} else if (level == 1) {
			Progress.init();
			world.setSystem(new SpriteRenderSystem());
			world.setSystem(new EffectSystem());
			world.setSystem(new TableSystem());
			world.setSystem(new PhysicsSystem(box2dWorld));
			world.initialize();
			EntityCreator.createRoomT(world).addToWorld();
			EntityCreator.createTableT(world).addToWorld();
			EntityCreator.createTommieT(world).addToWorld();
			final Entity glow = EntityCreator.createGlow(world, 132, 6);
			glow.addToWorld();
			glow.disable();
			
			timer = 0;
			health = 3;
			progressSpeed = -2;
			progressing = false;
			won = false;
			
			final Song song = new Song(MusicManager.getSong(MusicManager.TOMMIE));
			Tween.registerAccessor(Song.class, new SongAccessor());
			Timeline.createSequence()
				.push(Tween.set(song, SongAccessor.PLAY))
				.push(Tween.set(song, SongAccessor.VOLUME).target(0))
				.push(Tween.to(song, SongAccessor.VOLUME, 3f).target(1f).ease(Quart.INOUT))
				.start(tweenManager);
			
			ButtonStyle style = new ButtonStyle();
			style.up = new TextureRegionDrawable(SpriteManager.getSprite("button"));
			style.checked = new TextureRegionDrawable(SpriteManager.getSprite("button_pressed"));
			final Button button = new Button(style);
			button.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					event.getListenerActor().setTouchable(Touchable.disabled);
					progressing = true;
					progressSpeed = -progressSpeed;
					if (health == 3 && Math.abs(progressSpeed) == 2.0) {
						glow.enable();
					}
					return false;
				}
			});
			button.setBounds(10, 10, 170, 170);
			stage.addActor(button);
			
			style = new ButtonStyle();
			style.up = new TextureRegionDrawable(SpriteManager.getSprite("tommie_button_green"));
			style.checked = new TextureRegionDrawable(SpriteManager.getSprite("tommie_button_red"));
			final Button speedButton = new Button(style);
			speedButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					if (glow.isActive()) {
						glow.disable();
					}
					if (Math.abs(progressSpeed) == 10.0) {
						progressSpeed /= 5;
					} else {
						progressSpeed *= 5;
					}
					return false;
				}
			});
			speedButton.setBounds(805, 40, 70, 70);
			stage.addActor(speedButton);
			
			logic = new Logic() {
				@Override
				public void render() {
					world.setDelta(Gdx.graphics.getDeltaTime());
					world.process();
					tweenManager.update(world.getDelta());
					stage.draw();
					stage.act(world.getDelta());
					if (progressing) {
						Progress.setProgress(Progress.getProgress() + progressSpeed * world.getDelta());
						if (progressSpeed > 0 && Progress.getProgress() > 4) {
							button.setTouchable(Touchable.enabled);
							button.setChecked(false);
							progressing = false;
						} else if (progressSpeed < 0 && Progress.getProgress() < 1) {
							button.setTouchable(Touchable.enabled);
							button.setChecked(false);
							progressing = false;
						}
						if (!progressing && Math.abs(progressSpeed) == 10.0) {
							--health;
							if (health <= 0 && progressSpeed > 0) {
								Progress.setProgress(Progress.getProgress() + 1);
								blowUp();
								button.setTouchable(Touchable.disabled);
								button.setChecked(true);
								won = true;
								Label label = new Label("Tommie has been defeated!", labelStyle);
								stage.addActor(label);
								movingLabel(label);
								SoundEffectsManager.explode();
							}
						}
					}
					if (won) {
						timer += world.getDelta();
						if (timer > 5) {
							level = 2;
							create();
							Timeline.createSequence()
								.push(Tween.to(song, SongAccessor.VOLUME, 3f).target(0f).ease(Quart.INOUT))
								.push(Tween.set(song, SongAccessor.STOP))
								.start(tweenManager);
//							disposeSystems(world);
						}
					}
				}
			};
				
			Label label = new Label("Meet the evil Tommie", labelStyle);
			stage.addActor(label);
			movingLabel(label);
		} else if (level == 2) {
			world.setSystem(new PeopleSystem());
			world.setSystem(new SpriteRenderSystem());
			world.setSystem(new EffectSystem());
			world.setSystem(new LightSystem());
			world.setSystem(new PhysicsSystem(box2dWorld));
			world.initialize();
			
			EntityCreator.createScene(world).addToWorld();
			EntityCreator.createJezper(world).addToWorld();
			EntityCreator.createPeople1(world).addToWorld();
			EntityCreator.createPeople2(world).addToWorld();
			EntityCreator.createPeople3(world).addToWorld();
			EntityCreator.createLight1(world).addToWorld();
			EntityCreator.createLight2(world).addToWorld();
			EntityCreator.createLight3(world).addToWorld();
			
			EntityCreator.createWorld(box2dWorld);
			EntityCreator.createBall(world, box2dWorld, 15, 60).addToWorld();
			EntityCreator.createBall(world, box2dWorld, 50, 75).addToWorld();
			EntityCreator.createBall(world, box2dWorld, 100, 80).addToWorld();
			EntityCreator.createBall(world, box2dWorld, 150, 75).addToWorld();
			
			timer = 0;
			won = false;
			health = 0;
			Progress.setProgress(0);
			
			final Song song = new Song(MusicManager.getSong(MusicManager.JEZPER));
			Tween.registerAccessor(Song.class, new SongAccessor());
			Timeline.createSequence()
				.push(Tween.set(song, SongAccessor.PLAY))
				.push(Tween.set(song, SongAccessor.VOLUME).target(0))
				.push(Tween.to(song, SongAccessor.VOLUME, 3f).target(1f).ease(Quart.INOUT))
				.start(tweenManager);
			
			ButtonStyle style = new ButtonStyle();
			style.up = new TextureRegionDrawable(SpriteManager.getSprite("button"));
			style.checked = new TextureRegionDrawable(SpriteManager.getSprite("button_pressed"));
			final Button button = new Button(style);
			button.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					event.getListenerActor().setTouchable(Touchable.disabled);
					EntityCreator.createFire(world, 90, 40).addToWorld();
					EntityCreator.createFire(world, 60, 40).addToWorld();
					won = true;
					timer = 0;
					
					SoundEffectsManager.explode();
					
					Label label = new Label("Jezper has been defeated!", labelStyle);
					stage.addActor(label);
					movingLabel(label);
					
					Iterator<Body> bodies = box2dWorld.getBodies();
					while (bodies.hasNext()) {
						Body body = bodies.next();
						if (body.getUserData() instanceof Entity) {
							List<JointEdge> joints = body.getJointList();
							for (int i = 0; i < joints.size(); ++i) {
								box2dWorld.destroyJoint(joints.get(0).joint);
							}
						}
					}
					
					box2dWorld.setContactListener(new ContactListener() {
						@Override
						public void beginContact(Contact contact) {
							if (health < 10) {
								Vector2 p = contact.getFixtureA().getBody().getPosition();
								Body a = contact.getFixtureA().getBody();
								Body b = contact.getFixtureB().getBody();
								Body body = null;
								if (a.getUserData() instanceof Entity) {
									body = a;
								}
								if (b.getUserData() instanceof Entity) {
									// Entity B collided with wall
									if (body == null) {
										p = b.getPosition();
										EntityCreator.createFire(world, p.x * 10, p.y * 10).addToWorld();
									// A collided with B
									} else {
										EntityCreator.createFire(world, (a.getPosition().x + b.getPosition().x) * 10 / 2, (a.getPosition().y + b.getPosition().y) * 10 / 2).addToWorld();
									}
								} else {
									// A collided with wall
									p = a.getPosition();
									EntityCreator.createFire(world, p.x * 10, p.y * 10).addToWorld();
								}
								SoundEffectsManager.explode();
								++health;
							}
						}
						@Override
						public void endContact(Contact contact) {
						}
						@Override
						public void preSolve(Contact contact,
								Manifold oldManifold) {
						}
						@Override
						public void postSolve(Contact contact,
								ContactImpulse impulse) {
						}
					});
					return false;
				}
			});
			button.setBounds(10, 10, 170, 170);
			button.setTouchable(Touchable.disabled);
			button.setChecked(true);
			stage.addActor(button);
			
			Label label = new Label("Meet the evil Jezper", labelStyle);
			stage.addActor(label);
			movingLabel(label);
			
			logic = new Logic() {
				@Override
				public void render() {
					world.setDelta(Gdx.graphics.getDeltaTime());
					world.process();
					tweenManager.update(world.getDelta());
					stage.draw();
					stage.act(world.getDelta());
					
					timer += world.getDelta();
					if (won) {
						if (timer > 8) {
							level = 3;
							create();
							Timeline.createSequence()
								.push(Tween.to(song, SongAccessor.VOLUME, 3f).target(0f).ease(Quart.INOUT))
								.push(Tween.set(song, SongAccessor.STOP))
								.start(tweenManager);
//							disposeSystems(world);
						}
					} else {
						// wait 3 seconds until enabling the button
						Progress.setProgress(Progress.getProgress() + world.getDelta());
						if (timer > 3 && button.getTouchable() == Touchable.disabled) {
							button.setTouchable(Touchable.enabled);
							button.setChecked(false);
						}
					}
				}
			};
		} else if (level == 3) {
			final LabelStyle creditsLabelStyle = new LabelStyle();
			creditsLabelStyle .font = FontManager.getCreditsFont();
			creditsLabelStyle .fontColor = new Color(0, 0, 0, 1);
			
			final Label label = new Label("Congratulations! You have defeated the Slashat crew!\nThe people of earth have been saved and can once again\nlive in peace and harmony\n\n\n\nCredits\n\nMade by, programming & graphics:\n Daniel \"MaTachi\" Jonsson, http://danielj.se\n\nJava libraries:\n LibGDX, Artemis & Tween Engine\nSoftware used:\n Eclipse, GIMP & Aseprite on Ubuntu\nFonts:\n Rase GPL & EptKazoo\nMusic:\n Theme Music by Alexandr Zhelanov\n Dissonant Waltz by Yubatake\n Below The Shift by Clearside\nSound effect:\n Explode by Michel Baradari\n\n\nThanks Slashat for all years of podcast!\n\n\nDisclaimer: This game is just something silly and shouldn't\nbe taken seriously. I have no connection with Slashat\nother than being a fan and a longtime listener. :)\nIf I had had more time I would have made levels for\nMagnus and Johan too.", creditsLabelStyle);
			stage.addActor(label);
					
			Song song = new Song(MusicManager.getSong(MusicManager.THEME));
			Tween.registerAccessor(Song.class, new SongAccessor());
			Timeline.createSequence()
				.push(Tween.set(song, SongAccessor.PLAY))
				.push(Tween.set(song, SongAccessor.VOLUME).target(0))
				.push(Tween.to(song, SongAccessor.VOLUME, 2f).target(1.0f).ease(Quart.INOUT))
				.start(tweenManager);
			
			logic = new Logic() {
				float timer = 0;
				@Override
				public void render() {
					timer += Gdx.graphics.getDeltaTime();
					label.setPosition(20, 40 * timer - 1540);
					tweenManager.update(Gdx.graphics.getDeltaTime());
					
					stage.draw();
					stage.act(world.getDelta());
				}
			};
		}
	}
	
	private void movingLabel(Label label) {
		Tween.registerAccessor(Label.class, new LabelAccessor());
		Timeline.createSequence()
			.push(Tween.set(label, LabelAccessor.POSITION_XY).target(0, 540))
			.push(Tween.set(label, LabelAccessor.SCALE).target(0.5f))
			.beginParallel()
				.push(Tween.to(label, LabelAccessor.POSITION_X, 1f).target(100).ease(Quad.IN))
				.push(Tween.to(label, LabelAccessor.POSITION_Y, 1f).target(300).ease(Linear.INOUT))
				.push(Tween.to(label, LabelAccessor.SCALE, 1f).target(1).ease(Bounce.OUT))
			.end()
			.beginParallel()
				.push(Tween.to(label, LabelAccessor.POSITION_X, 2f).target(50).ease(Back.INOUT))
				.push(Tween.to(label, LabelAccessor.POSITION_Y, 2f).target(310).ease(Linear.INOUT))
			.end()
			.beginParallel()
				.push(Tween.to(label, LabelAccessor.POSITION_X, 2f).target(1000).ease(Quart.IN))
				.push(Tween.to(label, LabelAccessor.POSITION_Y, 2f).target(600).ease(Back.INOUT))
			.end()
			.start(tweenManager);
	}
	
	private void disposeSystems(World world) {
		// Delete entities with Effects
		ImmutableBag<Entity> entities = world.getManager(GroupManager.class).getEntities(Constants.EFFECTS);
		for (int i = 0; i < entities.size(); ++i) {
			Bag<Component> components = new Bag<Component>();
			world.getComponentManager().getComponentsFor(entities.get(i), components);
			for (int j = 0; j < components.size(); ++j) {
//				System.out.print("C: ");
				Component c = components.get(j);
//				System.out.println(c.getClass());
				if (c instanceof Effect) {
//					System.out.println("REMOVE");
					((Effect) c).dispose();
				}
			}
		}
		// Delete systems
		ImmutableBag<EntitySystem> entitySystems = world.getSystems();
		for (int i = 0; i < entitySystems.size(); ++i) {
			EntitySystem e = entitySystems.get(i);
			if (e instanceof EffectSystem) {
				((EffectSystem) e).dispose();
			} else if (e instanceof SpriteRenderSystem) {
				((SpriteRenderSystem) e).dispose();
			}
		}
	}
	
	private interface Logic {
		public void render();
	}
	
	private void blowUp() {
		EntityCreator.createFire(world, 30, 38).addToWorld();
		EntityCreator.createFire(world, 18, 30).addToWorld();
		EntityCreator.createFire(world, 86, 26).addToWorld();
		EntityCreator.createFire(world, 100, 41).addToWorld();
		EntityCreator.createFire(world, 135, 8).addToWorld();
		EntityCreator.createBlood(world, 77, 23).addToWorld();
		EntityCreator.createLegs(world, box2dWorld).addToWorld();
	}

	@Override
	public void dispose() {
		FontManager.dispose();
		SoundEffectsManager.dispose();
		SpriteManager.dispose();
		MusicManager.dispose();
		box2dWorld.dispose();
		stage.dispose();
		disposeSystems(world);
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		logic.render();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
//		SpriteManager.init();
//		FontManager.init();
//		SoundEffectsManager.init();
//		MusicManager.init();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
			if (level != 0) {
				tweenManager.killAll();
				MusicManager.getSong(MusicManager.JEZPER).stop();
				MusicManager.getSong(MusicManager.TOMMIE).stop();
				MusicManager.getSong(MusicManager.THEME).stop();
				level = 0;
				create();
			} else {
				Gdx.app.exit();
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
