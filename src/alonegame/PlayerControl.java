package alonegame;

import java.util.LinkedList;
import java.util.List;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class PlayerControl extends AbstractControl {

	enum Direction {
		Up, Down, Left, Right
	}

	public static final Vector3f dup = new Vector3f(0, 1, 0);
	public static final Vector3f dleft = new Vector3f(-1, 0, 0);
	public static final Vector3f ddown = new Vector3f(0, -1, 0);
	public static final Vector3f dright = new Vector3f(1, 0, 0);

	private Direction dir = Direction.Down;
	private boolean movingUp, movingDown, movingLeft, movingRight;
	private float speed = 256; // units per second

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		PlayerControl r = new PlayerControl();
		return r;
	}

	@Override
	protected void controlUpdate(float tpf) {
		float m = speed * tpf;
		Vector3f direction = new Vector3f(0, 0, 0);
		if (movingUp) {
			direction.addLocal(dup);
			dir = Direction.Up;
		}
		if (movingDown) {
			direction.addLocal(ddown);
			dir = Direction.Down;
		}
		if (movingLeft) {
			direction.addLocal(dleft);
			dir = Direction.Left;
		}
		if (movingRight) {
			direction.addLocal(dright);
			dir = Direction.Right;
		}
		direction.normalizeLocal().multLocal(m);
		spatial.move(direction);
		
		LevelEditorState les = AloneGame.states.getState(LevelEditorState.class);
		if (les != null && les.isEnabled()) {
			return; // no collision checks in editor mode
		}

		/* Collision Checking for Walls */
		Ray rleft = new Ray(spatial.getWorldTranslation(), dleft);
		Ray rright = new Ray(spatial.getWorldTranslation(), dright);
		Ray rup = new Ray(spatial.getWorldTranslation(), dup);
		Ray rdown = new Ray(spatial.getWorldTranslation(), ddown);

		CollisionResults cr = new CollisionResults();
		Node walls = (Node) ((Node) spatial.getParent().getChild("World"))
				.getChild("Ground");

		List<Spatial> solids = null;
		if (walls != null)
			solids = walls.getChildren();
		else
			solids = new LinkedList<Spatial>();

		boolean pLeft = false, pRight = false, pUp = false, pDown = false;
		for (Spatial s : solids) {
			String name = s.getName();
			if (!name.equals("Wall") && !name.contains("Block") && !name.equals("Door"))
				continue;
			// Push Left Test
			cr = new CollisionResults();
			rleft.collideWith(s.getWorldBound(), cr);
			CollisionResult r = cr.getClosestCollision();

			if (!pLeft && r != null) {
				float push = 0;
				push = r.getDistance();
				if (push < 16) {
					spatial.move(16 - push, 0, 0);
					pLeft = true;
				}
			}

			// Push Right Test
			cr = new CollisionResults();
			rright.collideWith(s.getWorldBound(), cr);
			r = cr.getClosestCollision();

			if (!pRight && r != null) {
				float push = 0;
				push = r.getDistance();
				if (push < 16) {
					spatial.move(-(16 - push), 0, 0);
					pRight = true;
				}
			}

			// Push Up Test
			cr = new CollisionResults();
			rup.collideWith(s.getWorldBound(), cr);
			r = cr.getClosestCollision();

			if (!pUp && r != null) {
				float push = 0;
				push = r.getDistance();
				if (push < 16) {
					spatial.move(0, -(16 - push), 0);
					pUp = true;
				}
			}

			// Push Down Test
			cr = new CollisionResults();
			rdown.collideWith(s.getWorldBound(), cr);
			r = cr.getClosestCollision();

			if (!pDown && r != null) {
				float push = 0;
				push = r.getDistance();
				if (push < 16) {
					spatial.move(0, 16 - push, 0);
					pDown = true;
				}
			}
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	public void setDirection(Direction d) {
		this.dir = d;
	}

	public Direction getDirection() {
		return dir;
	}

	public void setSpeed(float s) {
		speed = s;
	}

	public float getSpeed() {
		return speed;
	}

	public void movingUp(boolean is) {
		movingUp = is;
	}

	public void movingDown(boolean is) {
		movingDown = is;
	}

	public void movingLeft(boolean is) {
		movingLeft = is;
	}

	public void movingRight(boolean is) {
		movingRight = is;
	}

}
