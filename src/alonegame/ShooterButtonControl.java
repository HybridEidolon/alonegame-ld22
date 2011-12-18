package alonegame;

import java.util.List;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class ShooterButtonControl extends ButtonControl {

	public ShooterButtonControl() {
		super(true);
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		ShooterButtonControl r = new ShooterButtonControl();
		r.setEnabled(enabled);
		r.setPressed(pressed);
		r.setSpatial(spatial);
		return r;
	}
	
	@Override
	public void buttonPressedAction(Node ground) {
		// SHOOT THE BOOLETT
		List<Spatial> spatials = ground.getChildren();
		for (Spatial s : spatials) {
			if (s.getName().equals("Shooter")) {
				s.getControl(ShooterControl.class).shoot(ground);
			}
		}
	}

	@Override
	public void buttonReleasedAction(Node ground) {
		// Nothing happens, just the sound effect and release
	}

}
