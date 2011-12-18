package alonegame;

import com.jme3.audio.AudioNode;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class EndPortalControl extends AbstractControl {
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		EndPortalControl e = new EndPortalControl();
		e.setEnabled(enabled);
		e.setSpatial(spatial);
		return e;
	}

	@Override
	protected void controlUpdate(float tpf) {
		spatial.rotate(0, 0, FastMath.DEG_TO_RAD*-180*tpf);
		Vector3f pos = spatial.getWorldTranslation();
		Spatial player = spatial.getParent().getParent().getParent().getChild("Player");
		Vector3f playerPos = player.getWorldTranslation();
		
		if (pos.distance(playerPos) < 32) {
			// We've reached the end of the game...
			AudioNode sound = new AudioNode(AloneGame.assets, "Sounds/Teleport.wav");
			sound.play();
			AloneGame.states.detach(AloneGame.states.getState(GameWorldState.class));
			AloneGame.states.detach(AloneGame.states.getState(LoopMusicState.class));
			AloneGame.states.attach(new EndGameState());
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}

}
