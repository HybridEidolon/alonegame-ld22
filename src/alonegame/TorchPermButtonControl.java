package alonegame;

import java.util.List;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class TorchPermButtonControl extends ButtonControl {

	public TorchPermButtonControl() {
		super(false);
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		TorchPermButtonControl r = new TorchPermButtonControl();
		r.setEnabled(enabled);
		r.setPressed(pressed);
		r.setSpatial(spatial);
		return r;
	}

	@Override
	public void buttonPressedAction(Node ground) {
		List<Spatial> spatials = ground.getChildren();
		for (Spatial s : spatials) {
			if (s.getName().equals("TriggerTorch")) {
				GameWorldState.addTorchLight(ground.getParent(), s);
			}
		}
		
	}

	@Override
	public void buttonReleasedAction(Node ground) {
		// nothing
	}

}
