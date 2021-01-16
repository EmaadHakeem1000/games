package maingame;

import com.badlogic.gdx.Game;

import screens.*;

public class MainGame extends Game {
	public static final String title = "THE DOOR";
	
	public static final int wWidth = 1000; 
	public static final int wHeight = 600;
	public static final float PPM = 100;
	
	public static final float gravity = -13;
	
	@Override
	public void create () {
		setScreen(new MainMenu(this));
	}
	
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
