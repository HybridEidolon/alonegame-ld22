package alonegame;

import java.util.List;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class BlockDestroyButtonControl extends ButtonControl {

	public BlockDestroyButtonControl() {
		super(false);
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		BlockDestroyButtonControl r = new BlockDestroyButtonControl();
		r.setEnabled(enabled);
		r.setPressed(pressed);
		r.setSpatial(spatial);
		return r;
	}

	@Override
	public void buttonPressedAction(Node ground) {
		List<Spatial> spatials = ground.getChildren();
		for (Spatial s : spatials) {
			if (s.getName().equals("ButtonBlock")) {
				s.removeFromParent();
			}
		}
	}

	@Override
	public void buttonReleasedAction(Node ground) {
		// nothing to do here
	}

}
