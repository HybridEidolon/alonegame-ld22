package alonegame;

import java.util.List;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class TorchButtonControl extends ButtonControl {

	public TorchButtonControl() {
		super(true);
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		TorchButtonControl t = new TorchButtonControl();
		t.setEnabled(enabled);
		t.setPressed(pressed);
		t.setSpatial(spatial);
		return t;
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
		List<Spatial> spatials = ground.getChildren();
		for (Spatial s : spatials) {
			if (s.getName().equals("TriggerTorch")) {
				GameWorldState.removeTorchLight(ground.getParent(), s);
			}
		}
	}

}
