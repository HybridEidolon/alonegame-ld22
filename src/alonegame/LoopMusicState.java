package alonegame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;

public class LoopMusicState extends AbstractAppState {
	private AudioNode song;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		song = new AudioNode(AloneGame.assets, "Music/Music.ogg");
		song.setLooping(true);
		song.setReverbEnabled(false);
		song.setVolume(0.6f);
		song.play();
	}
	
	@Override
	public void stateDetached(AppStateManager stateManager) {
		song.stop();
	}
	
}
