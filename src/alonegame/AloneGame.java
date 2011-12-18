package alonegame;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;

public class AloneGame extends SimpleApplication implements ActionListener {

	public Spatial box;

	public static AssetManager assets;
	public static AppStateManager states;
	
	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);
		AloneGame g = new AloneGame();
		AppSettings a = new AppSettings(true);
		a.setResolution(512, 512);
		a.setFullscreen(false);
		a.setTitle("AloneGame : Ludum Dare 22");
		g.setSettings(a);
		g.setShowSettings(false);
		g.start(Type.Display);
	}

	@Override
	public void simpleInitApp() {
		// Add our game states
		setDisplayFps(false);
		setDisplayStatView(false);
		flyCam.setEnabled(false);
		createBindings();

		cam.setParallelProjection(true);
		cam.setFrame(new Vector3f(256, 256, 0), new Vector3f(-1, 0, 0),
				new Vector3f(0, 1, 0), new Vector3f(0, 0, -1));
		cam.setFrustum(-1000, 1000, -256, 256, 256,
				-256);

		stateManager.attach(new ScreenshotAppState());
		stateManager.attach(new LoopMusicState());
		stateManager.attach(new GameWorldState()); // TODO: change to title

		// attach our controller for the FollowNode button
		inputManager.addListener(this, "Exit", "WorldEditor", "ToggleMusic");
		assets = assetManager;
		states = stateManager;
	}

	private void createBindings() {
		inputManager.clearMappings(); // default stuff is useless to us
		inputManager.addMapping("Exit", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addMapping("MoveLeft", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager
				.addMapping("MoveRight", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("MoveUp", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("MoveDown", new KeyTrigger(KeyInput.KEY_DOWN));
		//inputManager.addMapping("WorldEditor", new KeyTrigger(KeyInput.KEY_L));
		inputManager.addMapping("PlaceTile", new MouseButtonTrigger(
				MouseInput.BUTTON_LEFT));
		inputManager.addMapping("PlaceTile", new KeyTrigger(KeyInput.KEY_Z));
		inputManager.addMapping("RemoveTile", new MouseButtonTrigger(
				MouseInput.BUTTON_RIGHT));
		inputManager.addMapping("RemoveTile", new KeyTrigger(KeyInput.KEY_X));
		inputManager.addMapping("EditorSaveMap", new KeyTrigger(KeyInput.KEY_S));
		
		inputManager.addMapping("EditorUpTile", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addMapping("EditorDownTile", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		
		inputManager.addMapping("ToggleMusic", new KeyTrigger(KeyInput.KEY_M));
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("Exit")) {
			stop();
		}
		if (name.equals("WorldEditor") && isPressed) {
			LevelEditorState l = stateManager.getState(LevelEditorState.class);
			if (l == null) {
				l = new LevelEditorState();
				stateManager.attach(l);
			} else {
				if (l.isEnabled()) {
					l.setEnabled(false);
				} else {
					l.setEnabled(true);
				}
			}
		}
		if (name.equals("ToggleMusic") && isPressed) {
			LoopMusicState lms = stateManager.getState(LoopMusicState.class);
			if (lms == null) {
				lms = new LoopMusicState();
				stateManager.attach(lms);
			} else {
				states.detach(lms);
			}
		}
	}
}
