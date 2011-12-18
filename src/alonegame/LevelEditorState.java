package alonegame;

import java.io.File;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class LevelEditorState extends AbstractAppState implements
		ActionListener {

	private GameWorldState gameWorld;
	private SimpleApplication sapp;
	private BitmapText info;

	private AmbientLight al;

	private Tile currentTile = Tile.Wall;

	enum Tile {
		Wall, Floor, Button, ButtonPressure, ShootOrb, ShootButton, TorchButton,

		ButtonBlock, Shooter, ShooterBlock, Torch, TriggerTorch, TorchPermButton, Key, Door, EndPortal
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		// Get the game world state
		sapp = (SimpleApplication) app;

		gameWorld = sapp.getStateManager().getState(GameWorldState.class);
		sapp.getInputManager().addListener(this, "PlaceTile", "RemoveTile",
				"EditorUpTile", "EditorDownTile", "EditorSaveMap");

		BitmapFont font = sapp.getAssetManager().loadFont(
				"Interface/Fonts/Default.fnt");
		info = new BitmapText(font);
		sapp.getGuiNode().attachChild(info);

		// Add the ambient light so you can actually see shit
		al = new AmbientLight();
		al.setColor(new ColorRGBA(4f, 4f, 4f, 1f));
		setup();
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		info.setText("Level Editor Active. Room " + gameWorld.getWorldX() + "x"
				+ gameWorld.getWorldY() + "  Tile " + currentTile.name());
		info.setLocalTranslation(0, 40, 0);
	}

	public void save() {
		// Remove all lights and Torch controls due to bugs
		List<Spatial> spatials = ((Node) gameWorld.getWorld()
				.getChild("Ground")).getChildren();
		for (Spatial s : spatials) {
			if (s.getName().equals("Torch")) {
				GameWorldState.removeTorchLight(gameWorld.getWorld(), (Node) s);
			}
		}

		Node world = gameWorld.getWorld();
		BinaryExporter exporter = BinaryExporter.getInstance();
		File file = new File("assets/Models/" + gameWorld.getWorldX() + "."
				+ gameWorld.getWorldY() + ".j3o");
		try {
			exporter.save(world, file);
		} catch (Exception ex) {
			System.err.println("What the poop couldn't save world");
		}

		// Reload world to regenerate torches
		gameWorld.moveWorld();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (!enabled) {
			takedown();
		}
		if (enabled) {
			setup();
		}
	}

	private void setup() {
		sapp.getGuiNode().attachChild(info);
		sapp.getInputManager().addListener(this, "PlaceTile", "RemoveTile",
				"EditorUpTile", "EditorDownTile", "EditorSaveMap");
		sapp.getRootNode().addLight(al);
	}

	private void takedown() {
		info.removeFromParent();
		sapp.getInputManager().removeListener(this);
		sapp.getRootNode().removeLight(al);
	}

	@Override
	public void stateDetached(AppStateManager stateManager) {
		super.stateDetached(stateManager);
		takedown();
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("PlaceTile") && isPressed) {
			// Get the click world coordinate
			Vector2f mouse = sapp.getInputManager().getCursorPosition();
			mouse.setX((Math.round(mouse.getX() / 32) * 32));
			mouse.setY((Math.round(mouse.getY() / 32) * 32));

			Vector3f pos = new Vector3f(mouse.getX(), mouse.getY(), 0);

			// Create the tile
			Node tile = new Node("Tile");
			tile.setLocalTranslation(pos);
			switch (currentTile) {
			case Wall: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 16));
				tile.attachChild(g);
				tile.move(0, 0, 12);
				g.setMaterial(sapp.getAssetManager().loadMaterial(
						"Materials/Wall.j3m"));
				break;
			}
			case Floor: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 0));
				tile.attachChild(g);
				tile.move(0, 0, -8);
				g.setMaterial(sapp.getAssetManager().loadMaterial(
						"Materials/Floor.j3m"));
				break;
			}
			case Button: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 0));
				tile.attachChild(g);
				g.setMaterial(sapp.getAssetManager().loadMaterial(
						"Materials/ButtonUp.j3m"));
				ButtonControl b = new BlockDestroyButtonControl();
				tile.addControl(b);
				break;
			}
			case ButtonPressure: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 0));
				tile.attachChild(g);
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/ButtonUp.j3m"));
				ButtonControl b = new BlockPressureButtonControl();
				tile.addControl(b);
				break;
			}
			case TorchButton: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 0));
				tile.attachChild(g);
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/ButtonUp.j3m"));
				tile.addControl(new TorchButtonControl());
				break;
			}
			case TorchPermButton: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 0));
				tile.attachChild(g);
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/ButtonUp.j3m"));
				tile.addControl(new TorchPermButtonControl());
				break;
			}
			case ShootButton: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 0));
				tile.attachChild(g);
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/ButtonUp.j3m"));
				tile.addControl(new ShooterButtonControl());
				break;
			}
			case Torch: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 0));
				tile.attachChild(g);
				g.setMaterial(sapp.getAssetManager().loadMaterial(
						"Materials/Torch.j3m"));
				GameWorldState.addTorchLight(gameWorld.getWorld(), tile);
				break;
			}
			case TriggerTorch: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 0));
				tile.attachChild(g);
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/Torch.j3m"));
				break;
			}
			case ButtonBlock: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 16));
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/Block.j3m"));
				tile.attachChild(g);
				tile.move(0, 0, 12);
				break;
			}
			case ShooterBlock: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 16));
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/Block.j3m"));
				tile.attachChild(g);
				tile.move(0, 0, 12);
				break;
			}
			case ShootOrb: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 1));
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/ShootOrb.j3m"));
				tile.attachChild(g);
				tile.addControl(new ShootOrbControl());
				break;
			}
			case Shooter: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 1));
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/Shooter.j3m"));
				tile.attachChild(g);
				tile.addControl(new ShooterControl());
				break;
			}
			case Key: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 0));
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/Key.j3m"));
				tile.attachChild(g);
				tile.addControl(new KeyControl());
				break;
			}
			case Door: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 16));
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/Door.j3m"));
				tile.attachChild(g);
				tile.addControl(new DoorControl());
				tile.move(0, 0, 12);
				break;
			}
			case EndPortal: {
				tile.setName(currentTile.name());
				Geometry g = new Geometry("Geom", new Box(16, 16, 1));
				g.setMaterial(AloneGame.assets
						.loadMaterial("Materials/EndPortal.j3m"));
				tile.attachChild(g);
				tile.addControl(new EndPortalControl());
				break;
			}
			}

			Node ground = (Node) gameWorld.getWorld().getChild("Ground");
			ground.attachChild(tile);
		}
		if (name.equals("RemoveTile") && isPressed) {
			// Get the click world coordinate
			Vector2f mouse = sapp.getInputManager().getCursorPosition();

			// Picking ray
			Ray pick = new Ray(new Vector3f(mouse.getX(), mouse.getY(), 256),
					new Vector3f(0, 0, -1));
			CollisionResults cr = new CollisionResults();
			gameWorld.getWorld().collideWith(pick, cr);
			CollisionResult r = cr.getClosestCollision();
			if (r != null && r.getGeometry() != null) {
				Node parent = r.getGeometry().getParent();

				// special case delete light for torches
				if (parent.getName().endsWith("Torch")) {
					GameWorldState.removeTorchLight(gameWorld.getWorld(),
							parent);
				}

				parent.removeFromParent();
			}
		}
		if (name.equals("EditorUpTile")) {
			switch (currentTile) {
			case Wall:
				currentTile = Tile.Floor;
				break;
			case Floor:
				currentTile = Tile.Button;
				break;
			case Button:
				currentTile = Tile.ButtonPressure;
				break;
			case ButtonPressure:
				currentTile = Tile.ShootOrb;
				break;
			case ShootOrb:
				currentTile = Tile.ShootButton;
				break;
			case ShootButton:
				currentTile = Tile.TorchButton;
				break;
			case TorchButton:
				currentTile = Tile.ButtonBlock;
				break;
			case ButtonBlock:
				currentTile = Tile.Shooter;
				break;
			case Shooter:
				currentTile = Tile.ShooterBlock;
				break;
			case ShooterBlock:
				currentTile = Tile.Torch;
				break;
			case Torch:
				currentTile = Tile.TriggerTorch;
				break;
			case TriggerTorch:
				currentTile = Tile.TorchPermButton;
				break;
			case TorchPermButton:
				currentTile = Tile.Key;
				break;
			case Key:
				currentTile = Tile.Door;
				break;
			case Door:
				currentTile = Tile.EndPortal;
				break;
			case EndPortal:
				currentTile = Tile.Wall;
				break;
			}
		}
		if (name.equals("EditorDownTile")) {
			switch (currentTile) {
			case Wall:
				currentTile = Tile.EndPortal;
				break;
			case Floor:
				currentTile = Tile.Wall;
				break;
			case Button:
				currentTile = Tile.Floor;
				break;
			case ButtonPressure:
				currentTile = Tile.Button;
				break;
			case ShootOrb:
				currentTile = Tile.ButtonPressure;
				break;
			case ShootButton:
				currentTile = Tile.ShootOrb;
				break;
			case TorchButton:
				currentTile = Tile.ShootButton;
				break;
			case ButtonBlock:
				currentTile = Tile.TorchButton;
				break;
			case Shooter:
				currentTile = Tile.ButtonBlock;
				break;
			case ShooterBlock:
				currentTile = Tile.Shooter;
				break;
			case Torch:
				currentTile = Tile.ShooterBlock;
				break;
			case TriggerTorch:
				currentTile = Tile.Torch;
				break;
			case TorchPermButton:
				currentTile = Tile.TriggerTorch;
				break;
			case Key:
				currentTile = Tile.TorchPermButton;
				break;
			case Door:
				currentTile = Tile.Key;
				break;
			case EndPortal:
				currentTile = Tile.Door;
				break;
			}
		}
		if (name.equals("EditorSaveMap") && isPressed) {
			save();
		}
	}
}
