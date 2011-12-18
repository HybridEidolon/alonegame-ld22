package alonegame;

import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.LightNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;

public class ShooterControl extends AbstractControl {

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		ShooterControl r = new ShooterControl();
		r.setEnabled(enabled);
		r.setSpatial(spatial);
		return r;
	}

	@Override
	protected void controlUpdate(float tpf) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}
	
	public void shoot(Node ground) {
		Node shot = new Node("Shot");
		Geometry g = new Geometry("Geom", new Box(8, 8, 1));
		g.setMaterial(AloneGame.assets.loadMaterial("Materials/Shot.j3m"));
		shot.attachChild(g);
		shot.setLocalTranslation(spatial.getWorldTranslation());
		ShotControl c = new ShotControl();
		c.setDirection(new Vector3f(0,1,0));
		c.setVelocity(256);
		shot.addControl(c);
		
		// add light
		PointLight pl = new PointLight();
		pl.setColor(new ColorRGBA(1.5f, 1f, 1f, 1f));
		pl.setRadius(128);
		ground.getParent().addLight(pl);
		LightNode light = new LightNode("Light", pl);
		light.setLocalTranslation(0f, 0f, 32f);
		shot.attachChild(light);
		
		ground.attachChild(shot);
	}

}
