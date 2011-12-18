package alonegame;

import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class KeyControl extends AbstractControl {

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		KeyControl r = new KeyControl();
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

		float distance = pos.distance(playerPos);
		if (distance < 16) {
			spatial.removeFromParent();
			AudioNode sound = new AudioNode(AloneGame.assets,
					"Sounds/PickupKey.wav");
			sound.play();
			int keys = player.getUserData("numKeys");
			player.setUserData("numKeys", keys + 1);
			GameWorldState.keysOwned.setProperty(GameWorldState.worldX+"."+GameWorldState.worldY, "true");
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}

}
