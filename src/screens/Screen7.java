package screens;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import maingame.MainGame;

import objects.*;

public class Screen7 implements Screen, ContactListener{
	MainGame game;
	
	private SpriteBatch batch;
		
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	
	private OrthogonalTiledMapRenderer mapRenderer; 
	private TiledMap map; 
	private TmxMapLoader mapLoader;
	
	private World world; 
		
	private RayHandler rayHandler;
	Set<ConeLight> cLset = new HashSet<>();
	
	private Player player;
	private SmithTheBoss boss;

	private Texture playerTex, hittheheadTex, bossTex, dlhit;
	
	public Screen7(MainGame game) {
		this.game = game;
		
		batch = new SpriteBatch();
		
		gameCam = new OrthographicCamera(); 
		gamePort = new FitViewport(MainGame.wWidth/MainGame.PPM, MainGame.wHeight/MainGame.PPM, gameCam);
		
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("map7.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/MainGame.PPM);
		gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
		
		world = new World(new Vector2(0, MainGame.gravity), true);
		world.setContactListener(this);
		
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.1f);
		rayHandler.useCustomViewport(gamePort.getScreenX(), gamePort.getScreenY(), MainGame.wWidth, MainGame.wHeight);
		
		ConeLight cLight1 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 33/MainGame.PPM, 560/MainGame.PPM, 320, 30);
		ConeLight cLight2 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 978/MainGame.PPM, 560/MainGame.PPM, 220, 30);
		cLset.add(cLight1);cLset.add(cLight2);
		
		MapBodyBuilder ground = new MapBodyBuilder(world, map);
		ground.defineStaticThing("Ground", 1);
		
		player = new Player(world, new Vector2(50, 200), new Vector2(20, 20));
		player.definePlayer();

		boss = new SmithTheBoss(world, new Vector2(700, 500), new Vector2(100, 100), player);
		boss.defineBoss();

		playerTex = new Texture("player.png"); 
		bossTex = new Texture("boss.png");
		hittheheadTex = new Texture("hitthehead.png");
		dlhit = new Texture("hitYou.png");
	}
	
	@Override
	public void show() {

	}

	public void update(float deltaTime) {
		// logic
		world.step(1/60f, 6, 2);
		rayHandler.update();
	
		player.playerControls(15, 300, deltaTime);

		if (boss.health <= 0){
			game.setScreen(new ThankYouScreen(game));
		}

		if (player.health <= 0){
			game.setScreen(new Screen7(game));
		}

		boss.controlAI(5, 600, deltaTime);
		
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

		batch.draw(bossTex, boss.getPosition().x- boss.getSize().x/2, boss.getPosition().y - boss.getSize().y/2,
			boss.getSize().x, boss.getSize().y);

		batch.draw(hittheheadTex, 40/MainGame.PPM, 400/MainGame.PPM, 300/MainGame.PPM, 100/MainGame.PPM);
		batch.draw(dlhit, 400/MainGame.PPM, 400/MainGame.PPM, 300/MainGame.PPM, 100/MainGame.PPM);

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
		bossTex.dispose();
		playerTex.dispose();
		hittheheadTex.dispose();
		dlhit.dispose();

		for (ConeLight c: cLset) {c.dispose();}
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA(); 
		Fixture fb = contact.getFixtureB();
		
		if (fa.getUserData() == "groundCheck" || fb.getUserData() == "groundCheck") {
			if (fa.getUserData() == "Ground" || fb.getUserData() == "Ground" || fa.getUserData() == "StaticBox" || fb.getUserData() == "StaticBox") {
				player.numJump = 3;
			}
		}

		if (fa.getUserData() == "groundCheckSmith" || fb.getUserData() == "groundCheckSmith"){
			if (fa.getUserData() == "Ground" || fb.getUserData() == "Ground"){
				boss.numJumps = 1;
			}

			if (fa.getUserData() == "Player" || fb.getUserData() == "Player"){
				boss.getBody().applyLinearImpulse(new Vector2(0, 1400 * Gdx.graphics.getDeltaTime()), boss.getBody().getWorldCenter(), true);
			}
		}

		if (fa.getUserData() == "hitBoxSmith" || fb.getUserData() == "hitBoxSmith"){
			if (fa.getUserData() == "Player" || fb.getUserData() == "Player"){
				if (player.getBody().getPosition().x < 500/MainGame.PPM){
					player.getBody().applyLinearImpulse(new Vector2(1300 * Gdx.graphics.getDeltaTime(), 1200 * Gdx.graphics.getDeltaTime()), player.getBody().getWorldCenter(), true);
				}else if (player.getBody().getPosition().x > 500/MainGame.PPM){
					player.getBody().applyLinearImpulse(new Vector2(-1300 * Gdx.graphics.getDeltaTime(), 1200 * Gdx.graphics.getDeltaTime()), player.getBody().getWorldCenter(), true);
				}
				boss.health -= 1;
			}
		}

		if (fa.getUserData() == "SmithBoss" || fb.getUserData() == "SmithBoss"){
			if (fa.getUserData() == "Player" || fb.getUserData() == "Player"){
				if (player.getBody().getPosition().x < boss.getPosition().x){
					player.getBody().applyLinearImpulse(new Vector2(-1300 * Gdx.graphics.getDeltaTime(), 0), player.getBody().getWorldCenter(), true);
				}else if (player.getBody().getPosition().x > boss.getPosition().x){
					player.getBody().applyLinearImpulse(new Vector2(1300 * Gdx.graphics.getDeltaTime(), 0), player.getBody().getWorldCenter(), true);
				}

				player.health -= 1;
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
