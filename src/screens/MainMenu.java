package screens;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import hud.MainMenuHud;
import maingame.MainGame;

public class MainMenu implements Screen{
	MainGame game;
	
	private SpriteBatch batch; 
	
	private OrthographicCamera gameCam; 
	private Viewport gamePort;
	
	private World world; 
	
	private RayHandler rayHandler;
	private ConeLight cLight1, cLight2;
	Set<ConeLight> cLset = new HashSet<ConeLight>();
	
	MainMenuHud hud;
	
	public MainMenu(MainGame game) {
		this.game = game;
		
		batch = new SpriteBatch();
		
		gameCam = new OrthographicCamera(); 
		gamePort = new FitViewport(MainGame.wWidth/MainGame.PPM, MainGame.wHeight/MainGame.PPM, gameCam);
		gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
		
		world = new World(new Vector2(0, MainGame.gravity), true);
		
		hud = new MainMenuHud(batch);
		
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.1f);
		rayHandler.useCustomViewport(gamePort.getScreenX(), gamePort.getScreenY(), MainGame.wWidth, MainGame.wHeight);
		
		cLight1 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 20/MainGame.PPM, 570/MainGame.PPM, 320, 30);
		cLight2 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 990/MainGame.PPM, 570/MainGame.PPM, 220, 30);
		cLset.add(cLight1); cLset.add(cLight2);
		
		Gdx.input.setInputProcessor(hud.stage);
	}
	
	@Override
	public void show() {
		
	}

	public void update() {
		// logic
		world.step(1/60f, 6, 2);
		rayHandler.update();
		
		if (hud.isBeginGame()) game.setScreen(new Screen1(game)); 
		if (hud.isGameQuit())  Gdx.app.exit();
		
		gameCam.update();
	}
	
	@Override
	public void render(float delta) {
		update();
		
		// rendering and stuff
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		
		batch.setProjectionMatrix(hud.stage.getCamera().combined);
		rayHandler.setCombinedMatrix(gameCam);
	
		hud.stage.draw();
		
		batch.begin();
		
		batch.end();

		rayHandler.render();
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height, false);
		rayHandler.useCustomViewport(gamePort.getScreenX(), gamePort.getScreenY(), gamePort.getScreenWidth(), gamePort.getScreenHeight());
		hud.resize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		rayHandler.dispose();
		
		for(ConeLight c: cLset) {c.dispose();}
	}

}
