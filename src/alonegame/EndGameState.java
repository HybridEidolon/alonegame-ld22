package alonegame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class EndGameState extends AbstractAppState {

	private Node fade;
	private Material fadeMat;
	private ColorRGBA fadeColor;
	private SimpleApplication sapp;
	private boolean textAdded = false;
	private boolean text2Added = false;
	private boolean text3Added = false;
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		fade = new Node("Fade");
		fadeMat = new Material(AloneGame.assets, "Common/MatDefs/Misc/Unshaded.j3md");
		fadeColor = new ColorRGBA(0, 0, 0, 0);
		fadeMat.setColor("Color", fadeColor);
		Geometry g = new Geometry("Geom", new Box(512, 512, 1));
		g.setMaterial(fadeMat);
		sapp = (SimpleApplication)app;
		sapp.getRootNode().detachAllChildren();
		sapp.getGuiNode().detachAllChildren();
		
		sapp.getRootNode().attachChild(fade);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		
		fadeColor.a += 0.25f*tpf;
		if (fadeColor.a > 1.0f && !textAdded) {
			BitmapFont font = AloneGame.assets.loadFont("Interface/Fonts/Default.fnt");
			BitmapText text = new BitmapText(font);
			text.setText("The game is over. You may not be alone anymore.");
			text.setLocalTranslation(-(text.getLineWidth()/2)+256, 256, 0);
			sapp.getGuiNode().attachChild(text);
			textAdded = true;
		}
		if (fadeColor.a > 2.0f && !text2Added) {
			BitmapFont font = AloneGame.assets.loadFont("Interface/Fonts/Default.fnt");
			BitmapText text = new BitmapText(font);
			text.setText("Maybe you've found yourself, and aren't alone because of that.");
			text.setLocalTranslation(-(text.getLineWidth()/2)+256, 256+text.getLineHeight(), 0);
			sapp.getGuiNode().attachChild(text);
			text2Added = true;
		}
		if (fadeColor.a > 3.0f && !text3Added) {
			BitmapFont font = AloneGame.assets.loadFont("Interface/Fonts/Default.fnt");
			BitmapText text = new BitmapText(font);
			text.setText("Or perhaps, loneliness is what you always sought?");
			text.setLocalTranslation(-(text.getLineWidth()/2)+256, 256+(text.getLineHeight()*2), 0);
			sapp.getGuiNode().attachChild(text);
			AloneGame.states.detach(this);
		}
	}
	
}
