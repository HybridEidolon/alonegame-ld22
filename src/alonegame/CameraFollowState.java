package alonegame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

public class CameraFollowState extends AbstractAppState {

	public static final Vector3f LEFT = new Vector3f(-1, 0, 0);
	public static final Vector3f UP = new Vector3f(0, -1, 0);
	public static final Vector3f FORWARD = new Vector3f(0, 0, 1);
	private Spatial follow;
	private Camera camera;
	private float aspectRatio;
	private float size = 256f;
	private float zoom = 1.0f;
	private Node root;

	public CameraFollowState(Spatial follow) {
		this.follow = follow;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);

		SimpleApplication sapp = (SimpleApplication) app;

		camera = sapp.getCamera();

		// disable the flyby camera in simple app
		root = sapp.getRootNode();
		sapp.getRootNode().setCullHint(CullHint.Never);

		// calculate the aspect ratio
		aspectRatio = camera.getWidth() / camera.getHeight();
		camera.setParallelProjection(true);
		camera.setFrustum(-1000, 1000, -aspectRatio * size, aspectRatio
				* size, size , -size);

		if (follow != null && !sapp.getRootNode().hasChild(follow)) {
			throw new IllegalArgumentException(
					"Given spatial is not in the root node");
		}
	}

	@Override
	public void update(float tpf) {
		// we're just going to set the position
		super.update(tpf);

		if (follow != null) {
			Vector3f pos = follow.getWorldTranslation();
			Vector3f camPos = new Vector3f(pos.x, pos.y, 0);

			camera.setFrame(camPos, LEFT, UP, FORWARD);
		} else {
			camera.setFrame(camera.getLocation(), LEFT, UP, FORWARD);
		}
	}

	public void setSpatial(Spatial s) {
		if (s == null || root.hasChild(s)) {
			follow = s;
		} else {
			throw new IllegalArgumentException("Spatial is not in the scene");
		}
	}

	public Spatial getSpatial() {
		return follow;
	}

	public void setZoom(float z) {
		zoom = z;
	}

	public float getZoom() {
		return zoom;
	}
}
