package alonegame;

import java.util.List;
import java.util.Properties;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.LightNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class GameWorldState extends AbstractAppState {

	private Node root;
	private Node player;
	private PlayerControl playerControl;
	public static int worldX = 0;
	public static int worldY = 0;
	private Node world;
	
	private BitmapText uiKeys;
	
	public static Properties keysOwned;
	public static Properties doorsOpened;

	public Node getWorld() {
		return world;
	}

	public int getWorldX() {
		return worldX;
	}

	public int getWorldY() {
		return worldY;
	}

	private SimpleApplication sapp;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		sapp = (SimpleApplication) app;
		root = sapp.getRootNode();

		// create the player
		player = new Node("Player");
		Geometry playerGeom = new Geometry("Player Geometry",
				new Box(16, 16, 0));
		playerGeom.setMaterial(sapp.getAssetManager().loadMaterial(
				"Materials/Player.j3m"));
		player.attachChild(playerGeom);

		playerControl = new PlayerControl();
		player.addControl(playerControl);

		player.setLocalTranslation(256f, 256f, 2f);
		player.setUserData("numKeys", 0);

		// add light to player
		PointLight l = new PointLight();
		l.setColor(new ColorRGBA(2f, 2f, 2f, 1f));
		l.setRadius(256);
		LightNode ln = new LightNode("PlayerLight", l);
		player.attachChild(ln);
		ln.setLocalTranslation(0, 0, 32);
		root.addLight(l);
		root.attachChild(player);

		// add ambient lighting
		//AmbientLight al = new AmbientLight();
		//al.setColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
		//root.addLight(al);

		// create the player control listener
		sapp.getInputManager().addListener(
				new PlayerInputListener(playerControl), "MoveLeft",
				"MoveRight", "MoveUp", "MoveDown");
		world = new Node("World");
		moveWorld();
		
		// Set up UI
		BitmapFont font = AloneGame.assets.loadFont("Interface/Fonts/Default.fnt");
		uiKeys = new BitmapText(font);
		uiKeys.setLocalTranslation(0, 512, 0);
		sapp.getGuiNode().attachChild(uiKeys);
		
		keysOwned = new Properties();
		doorsOpened = new Properties();
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		// World Bounds Check
		float x = player.getWorldTranslation().getX();
		float y = player.getWorldTranslation().getY();
		if (x > 512) {
			player.move(-512, 0, 0);
			worldX += 1;
			moveWorld();
		}
		if (x < 0) {
			player.move(512, 0, 0);
			worldX -= 1;
			moveWorld();
		}
		if (y > 512) {
			player.move(0, -512, 0);
			worldY += 1;
			moveWorld();
		}
		if (y < 0) {
			player.move(0, 512, 0);
			worldY -= 1;
			moveWorld();
		}
		
		uiKeys.setText("Keys: " + player.getUserData("numKeys"));
	}
	
	public void moveWorld() {

		// clear the world
		world.removeFromParent();

		// reload the world
		try {
//			BinaryImporter im = new BinaryImporter();
//			im.setAssetManager(AloneGame.assets);
//			Spatial load = (Spatial) im.load(new File("assets/Models/" + worldX
//					+ "." + worldY + ".j3o"));
//			world = (Node) load;
			
			world = (Node) AloneGame.assets.loadModel("Models/"+worldX+"."+worldY+".j3o");
			
			// add torch lights if there are any
			List<Spatial> spatials = ((Node) world.getChild("Ground")).getChildren();
			for (Spatial s : spatials) {
				if (s.getName().equals("Torch")) {
					addTorchLight(world, (Node)s);
				}
			}
			
			// check to make sure we don't own the key or have opened the door
			for (Spatial s : spatials) {
				String name = s.getName();
				if (name.equals("Key")) {
					if (keysOwned.getProperty(worldX+"."+worldY, "false").equals("true")) {
						s.removeFromParent();
					}
				}
				if (name.equals("Door")) {
					if (doorsOpened.getProperty(worldX+"."+worldY, "false").equals("true")) {
						s.removeFromParent();
					}
				}
			}
		} catch (Exception ex) {
			// generate a new empty map
			world = new Node("World");
			world.attachChild(new Node("Ground"));
		}
		root.attachChild(world);
	}
	
	public static void addTorchLight(Node world, Spatial torch) {
		PointLight pl = new PointLight();
		pl.setColor(new ColorRGBA(2.4f, 1.4f, 0.8f, 1f));
		world.addLight(pl);
		torch.addControl(new TorchLightFlickerControl(pl));
	}
	
	public static void removeTorchLight(Node world, Spatial torch) {
		PointLight pl = torch.getControl(TorchLightFlickerControl.class).getLight();
		world.removeLight(pl);
		torch.removeControl(TorchLightFlickerControl.class);
	}
}
