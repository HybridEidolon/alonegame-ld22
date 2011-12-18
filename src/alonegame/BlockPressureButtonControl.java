package alonegame;

import java.util.LinkedList;
import java.util.List;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class BlockPressureButtonControl extends ButtonControl {

	List<Spatial> blocks = new LinkedList<Spatial>();
	public BlockPressureButtonControl() {
		super(true);
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		BlockPressureButtonControl r = new BlockPressureButtonControl();
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
				blocks.add(s);
				s.removeFromParent();
			}
		}
	}

	@Override
	public void buttonReleasedAction(Node ground) {
		for (Spatial s : blocks) {
			ground.attachChild(s);
		}
	}

}
