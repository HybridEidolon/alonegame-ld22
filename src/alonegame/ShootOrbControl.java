package alonegame;

import java.util.List;

import com.jme3.audio.AudioNode;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class ShootOrbControl extends AbstractControl {

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		ShootOrbControl r = new ShootOrbControl();
		r.setEnabled(enabled);
		r.setSpatial(spatial);
		return r;
	}

	@Override
	protected void controlUpdate(float tpf) {
		
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		
	}

	public void activate(Node ground) {
		Geometry g = (Geometry) ((Node) spatial).getChild("Geom");
		g.setMaterial(AloneGame.assets.loadMaterial("Materials/ShootOrbActivated.j3m"));
		AudioNode sound = new AudioNode(AloneGame.assets, "Sounds/OrbActivate.wav");
		sound.play();
		List<Spatial> spatials = ground.getChildren();
		for (Spatial s : spatials) {
			if (s.getName().equals("ShooterBlock")) {
				s.removeFromParent();
			}
		}
	}
}
