package hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import maingame.MainGame;

public class ThankYouScreenHud {
	
	public Stage stage;
	private Viewport viewport;
	
	boolean gameQuit = false;
	boolean backToMenu = false; 
	
	public ThankYouScreenHud(SpriteBatch batch) {
		
		viewport = new FitViewport(MainGame.wWidth, MainGame.wHeight, new OrthographicCamera());
		stage = new Stage(viewport, batch);
		
		Table table = new Table(); 
		table.left();
		table.setFillParent(true);
		
		Image quitButtonImg = new Image(new Texture("quitButton.png")); 
		quitButtonImg.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				gameQuit = true;  
				return true;
			}
		});
		
		Image menuButtonImg = new Image(new Texture("menuButton.png"));
		menuButtonImg.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				backToMenu = true;
				return true;
			}
			
		});
		
		table.add(quitButtonImg).padTop(400).padLeft(800);
		table.add(menuButtonImg).padTop(400).padLeft(-300);
		
		stage.addActor(table);
	}
	
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	public boolean isGameQuit() {
		return gameQuit;
	}
	
	public boolean tMenu() {
		return backToMenu;
	}
}
