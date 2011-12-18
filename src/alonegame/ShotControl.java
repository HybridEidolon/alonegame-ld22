package alonegame;

import java.util.List;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.LightNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class ShotControl extends AbstractControl {

	private Vector3f direction;
	private float velocity;
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		ShotControl r = new ShotControl();
		r.setEnabled(enabled);
		r.setSpatial(spatial);
		r.setDirection(direction.clone());
		r.setVelocity(velocity);
		return r;
	}

	@Override
	protected void controlUpdate(float tpf) {
		Vector3f displacement = direction.mult(velocity*tpf);
		spatial.move(displacement);
		
		/* Collision Check */
		Ray ray = new Ray(spatial.getWorldTranslation(), direction);
		Node ground = spatial.getParent();
		List<Spatial> spatials = ground.getChildren();
		for (Spatial s : spatials) {
			String name = s.getName();
			if (name.equals("Wall") || name.endsWith("Block")) {
				CollisionResults cr = new CollisionResults();
				ray.collideWith(s.getWorldBound(), cr);
				CollisionResult r = cr.getClosestCollision();
				if (r != null && r.getDistance() < 4) {
					spatial.removeFromParent();
					
					// remove light
					Node world = ground.getParent();
					LightNode lightNode = (LightNode) ((Node) spatial).getChild("Light");
					world.removeLight(lightNode.getLight());
					return; // fuckin OWWWWNED
				}
			}
			if (name.equals("ShootOrb")) {
				CollisionResults cr = new CollisionResults();
				ray.collideWith(s.getWorldBound(), cr);
				CollisionResult r = cr.getClosestCollision();
				if (r != null && r.getDistance() < 4) {
					spatial.removeFromParent();
					s.getControl(ShootOrbControl.class).activate(ground);
					
					// remove light
					Node world = ground.getParent();
					LightNode lightNode = (LightNode) ((Node) spatial).getChild("Light");
					world.removeLight(lightNode.getLight());
					return;
				}
			}
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
	
	

}
