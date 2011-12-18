package alonegame;

import java.util.List;

import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public abstract class ButtonControl extends AbstractControl {

	protected boolean pressed;
	protected boolean pressureSensitive;

	public ButtonControl(boolean pressureSensitive) {
		this.pressureSensitive = pressureSensitive;
	}

	@Override
	protected void controlUpdate(float tpf) {
		// Check for player collision
		Vector3f pos = spatial.getWorldTranslation();
		
		boolean pressedByPlayer = false;
		boolean pressedByWorld = false;

		Node ground = spatial.getParent();
		Node player = (Node) spatial.getParent().getParent().getParent()
				.getChild("Player"); // ground world root
		Vector3f direction = pos.subtract(player.getWorldTranslation());
		if (direction.length() < 16) {
			// The button has been pressed
			if (!pressed) {
				doPressedResponse(ground);
			}
			pressedByPlayer = true;
		}

		// Check for world collision with any other node
		List<Spatial> spatials = ground.getChildren();
		for (Spatial s : spatials) {
			if (s == spatial) continue;
			if (s.getName().equals("Floor")) continue;
			// TODO only allow pressing by certain named nodes
			direction = pos.subtract(s.getWorldTranslation());
			if (direction.length() < 15) {
				if (!pressed) {
					doPressedResponse(ground);
				}
				pressedByWorld = true;
			}
		}
		if (pressureSensitive && pressed && !pressedByPlayer && !pressedByWorld) {
			// Nothing is holding this button down
			doReleasedResponse(ground);
		}
	}

	private void doPressedResponse(Node ground) {
		pressed = true;
		Geometry geom = (Geometry) ((Node) spatial).getChild("Geom");
		Material m = AloneGame.assets.loadMaterial("Materials/ButtonDown.j3m");
		geom.setMaterial(m);
		AudioNode pressedSound = new AudioNode(AloneGame.assets,
				"Sounds/ButtonPress.wav");
		pressedSound.play();

		buttonPressedAction(ground);
	}

	private void doReleasedResponse(Node ground) {
		pressed = false;
		Geometry geom = (Geometry) ((Node) spatial).getChild("Geom");
		Material m = AloneGame.assets.loadMaterial("Materials/ButtonUp.j3m");
		geom.setMaterial(m);
		AudioNode pressedSound = new AudioNode(AloneGame.assets,
				"Sounds/ButtonPress.wav");
		pressedSound.play();

		buttonReleasedAction(ground);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public boolean isPressureSensitive() {
		return pressureSensitive;
	}

	public void setPressureSensitive(boolean pressureSensitive) {
		this.pressureSensitive = pressureSensitive;
	}

	public abstract void buttonPressedAction(Node ground);

	public abstract void buttonReleasedAction(Node ground);
}
