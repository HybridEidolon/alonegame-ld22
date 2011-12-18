package alonegame;

import com.jme3.input.controls.ActionListener;

public class PlayerInputListener implements ActionListener {

	private PlayerControl playerControl;
	public PlayerInputListener(PlayerControl player) {
		playerControl = player;
	}
	
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("MoveLeft")) {
			playerControl.movingLeft(isPressed);
		}
		if (name.equals("MoveRight")) {
			playerControl.movingRight(isPressed);
		}
		if (name.equals("MoveUp")) {
			playerControl.movingUp(isPressed);
		}
		if (name.equals("MoveDown")) {
			playerControl.movingDown(isPressed);
		}
	}

}
