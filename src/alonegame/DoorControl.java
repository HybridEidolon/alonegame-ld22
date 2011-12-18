package alonegame;

import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class DoorControl extends AbstractControl {

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		DoorControl r = new DoorControl();
		r.setEnabled(enabled);
		r.setSpatial(spatial);
		return r;
	}

	@Override
	protected void controlUpdate(float tpf) {
		Vector3f pos = spatial.getWorldTranslation();
		Spatial player = spatial.getParent().getParent().getParent()
				.getChild("Player");
		Vector3f playerPos = player.getWorldTranslation();
		
		int keys = player.getUserData("numKeys");
		if (keys > 0) {
			if (pos.distance(playerPos) < 40) {
				player.setUserData("numKeys", keys-1);
				spatial.removeFromParent();
				AudioNode pressedSound = new AudioNode(AloneGame.assets,
						"Sounds/ButtonPress.wav");
				pressedSound.play();
				GameWorldState.doorsOpened.setProperty(GameWorldState.worldX+"."+GameWorldState.worldY, "true");
			}
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}

}
