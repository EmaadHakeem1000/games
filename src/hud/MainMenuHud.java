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

public class MainMenuHud {
	
	public Stage stage;
	private Viewport viewport;
	
	boolean beginGame = false;
	boolean gameQuit = false;
	
	public MainMenuHud(SpriteBatch batch) {
		
		viewport = new FitViewport(MainGame.wWidth, MainGame.wHeight, new OrthographicCamera());
		stage = new Stage(viewport, batch);
		
		Table table = new Table(); 
		table.left();
		table.setFillParent(true);
	
		Image GameLogo = new Image(new Texture("GameLogo.png")); 
		
		Image playButtonImg = new Image(new Texture("playButton.png"));
		playButtonImg.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				beginGame = true;
				return true;
			}
		});
		
		Image quitButtonImg = new Image(new Texture("quitButton.png")); 
		quitButtonImg.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				gameQuit = true;
				return true;
			}
		});
		
		table.add(GameLogo).padTop(10).padLeft(450);
		table.row(); 
		table.add(playButtonImg).padTop(270).padLeft(450);
		table.row();
		table.add(quitButtonImg).padTop(10).padLeft(450);
		
		stage.addActor(table);
	}
	
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	public boolean isBeginGame() {
		return beginGame;
	}
	
	public boolean isGameQuit() {
		return gameQuit;
	}
}
