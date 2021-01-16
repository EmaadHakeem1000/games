package screens;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import maingame.MainGame;
import objects.MapBodyBuilder;
import objects.Player;

public class Screen2 implements Screen, ContactListener{
	
private SpriteBatch batch;
	
	MainGame game;
	
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	
	private OrthogonalTiledMapRenderer mapRenderer; 
	private TiledMap map; 
	private TmxMapLoader mapLoader;
	
	private World world;
	
	private RayHandler rayHandler;
	private ConeLight cLight1, cLight2, cLight3, cLight4, cLight5;
	Set<ConeLight> cLset = new HashSet<ConeLight>();
	
	private Player player;
	
	private Texture playerTex, doNotTouchTex;
	
	public Screen2(MainGame game) {
		this.game = game;
		
		batch = new SpriteBatch();
		
		gameCam = new OrthographicCamera(); 
		gamePort = new FitViewport(MainGame.wWidth/MainGame.PPM, MainGame.wHeight/MainGame.PPM, gameCam);
		
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("map2.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/MainGame.PPM);
		gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
		
		world = new World(new Vector2(0, MainGame.gravity), true);
		world.setContactListener(this);
		
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.1f);
		rayHandler.useCustomViewport(gamePort.getScreenX(), gamePort.getScreenY(), MainGame.wWidth, MainGame.wHeight);
		
		cLight1 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 20/MainGame.PPM, 570/MainGame.PPM, 320, 30);
		cLight2 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 1320/MainGame.PPM, 570/MainGame.PPM, 220, 30);
		cLight3 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 1420/MainGame.PPM, 570/MainGame.PPM, 320, 30);
		cLight4 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 2000/MainGame.PPM, 570/MainGame.PPM, 320, 30);
		cLight5 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 3420/MainGame.PPM, 570/MainGame.PPM, 220, 30);
		cLset.add(cLight1);cLset.add(cLight2);cLset.add(cLight3);cLset.add(cLight4);cLset.add(cLight5);
		
		MapBodyBuilder ground = new MapBodyBuilder(world, map);
		ground.defineStaticThing("Ground", 1);
		
		MapBodyBuilder box = new MapBodyBuilder(world, map);
		box.defineStaticThing("StaticBox", 2);
		
		MapBodyBuilder door = new MapBodyBuilder(world, map);
		door.defineStaticThing("EndDoor", 3);
		
		MapBodyBuilder thorns = new MapBodyBuilder(world, map); 
		thorns.defineStaticThing("playerKill", 4);
		
		player = new Player(world, new Vector2(50, 200), new Vector2(20, 20));
		player.definePlayer(); 
		
		playerTex = new Texture("player.png");
		doNotTouchTex = new Texture("doNotTouch.png");
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	
	public void update(float deltaTime) {
		// logic
		world.step(1/60f, 6, 2);
		rayHandler.update();
		
		player.playerControls(15, 300, deltaTime);

		gameCam.position.x = player.getPosition().x;
		
		gameCam.update();
		mapRenderer.setView(gameCam);
	}

	@Override
	public void render(float delta) {
		update(delta);
		
		// rendering and stuff
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		
		batch.setProjectionMatrix(gameCam.combined);
		mapRenderer.render();
		rayHandler.setCombinedMatrix(gameCam);
		
		batch.begin();
		
		batch.draw(playerTex, player.getPosition().x- player.getSize().x/2, player.getPosition().y - player.getSize().y/2,
				player.getSize().x, player.getSize().y);
		
		batch.draw(doNotTouchTex, 1100/MainGame.PPM, 120/MainGame.PPM, 200/MainGame.PPM, 200/MainGame.PPM);
		
		batch.end();
		
		rayHandler.render();
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height, false);
		rayHandler.useCustomViewport(gamePort.getScreenX(), gamePort.getScreenY(), gamePort.getScreenWidth(), gamePort.getScreenHeight());
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
		rayHandler.dispose();
		world.dispose(); 
		batch.dispose();
		playerTex.dispose();
		
		for (ConeLight c: cLset) {c.dispose();}
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if (fa.getUserData() == "Player" || fb.getUserData() == "Player") {
			if (fa.getUserData() == "EndDoor" || fa.getUserData() == "EndDoor") {
				game.setScreen(new Screen3(game));
			}
			
			if (fa.getUserData() == "playerKill" || fb.getUserData() == "playerKill") {
				game.setScreen(new Screen2(game));
			}
		}
		
		if (fa.getUserData() == "groundCheck" || fb.getUserData() == "groundCheck") {
			if (fa.getUserData() == "Ground" || fb.getUserData() == "Ground" || fa.getUserData() == "StaticBox" || fb.getUserData() == "StaticBox") {
				player.numJump = 3;
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
