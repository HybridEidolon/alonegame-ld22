package alonegame;

import com.jme3.light.PointLight;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class TorchLightFlickerControl extends AbstractControl {

	protected float flicker;
	protected PointLight light;
	
	public TorchLightFlickerControl() {
		
	}
	
	public TorchLightFlickerControl(PointLight l) {
		light = l;
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		TorchLightFlickerControl r = new TorchLightFlickerControl();
		r.setEnabled(enabled);
		r.setSpatial(spatial);
		r.setLight(light);
		return r;
	}

	@Override
	protected void controlUpdate(float tpf) {
		light.setPosition(spatial.getWorldTranslation().add(0, 0, 32));
		flicker += 8*tpf;
		float radius = FastMath.sin(FastMath.tan(FastMath.cos(flicker+(FastMath.rand.nextFloat()*2))))*20 + 200;
		light.setRadius(radius);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}

	public float getFlicker() {
		return flicker;
	}

	public void setFlicker(float flicker) {
		this.flicker = flicker;
	}

	public PointLight getLight() {
		return light;
	}

	public void setLight(PointLight light) {
		this.light = light;
	}

	
}
