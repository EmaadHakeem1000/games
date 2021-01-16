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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import maingame.MainGame;
import objects.BulletSpawner;
import objects.MapBodyBuilder;
import objects.Player;

public class Screen5 implements Screen, ContactListener{
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
	private BulletSpawner bullet1, bullet2, bullet3, bullet4, bullet5;

	private Texture bTex, playerTex;
	
	public Screen5(MainGame game) {
		this.game = game;
		
		batch = new SpriteBatch();
		
		gameCam = new OrthographicCamera(); 
		gamePort = new FitViewport(MainGame.wWidth/MainGame.PPM, MainGame.wHeight/MainGame.PPM, gameCam);
		
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("map5.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/MainGame.PPM);
		gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
		
		world = new World(new Vector2(0, MainGame.gravity), true);
		world.setContactListener(this);
		
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.1f);
		rayHandler.useCustomViewport(gamePort.getScreenX(), gamePort.getScreenY(), MainGame.wWidth, MainGame.wHeight);
		
		ConeLight cLight1 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 20/MainGame.PPM, 570/MainGame.PPM, 320, 90);
		ConeLight cLight2 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 978/MainGame.PPM, 570/MainGame.PPM, 220, 90);
		cLset.add(cLight1);cLset.add(cLight2);
		
		MapBodyBuilder ground = new MapBodyBuilder(world, map);
		ground.defineStaticThing("Ground", 1);
		
		MapBodyBuilder door = new MapBodyBuilder(world, map);
		door.defineStaticThing("EndDoor", 2);
		
		player = new Player(world, new Vector2(50, 200), new Vector2(20, 20));
		player.definePlayer();

		bullet1 = new BulletSpawner(world, new Vector2(500, 100), new Vector2(10, 10), -1, "b1");
		bullet2 = new BulletSpawner(world, new Vector2(200, 170), new Vector2(10, 10), -1, "b2");
		bullet3 = new BulletSpawner(world, new Vector2(300, 230), new Vector2(10, 10), -1, "b3");
		bullet4 = new BulletSpawner(world, new Vector2(900, 360), new Vector2(10, 10), -1, "b4");
		bullet5 = new BulletSpawner(world, new Vector2(100, 420), new Vector2(10, 10), -1, "b5");
		
		bullet1.defineSpawner();
		bullet2.defineSpawner();
		bullet3.defineSpawner();
		bullet4.defineSpawner();
		bullet5.defineSpawner();
		
		bTex = new Texture("bullet.png");
		playerTex = new Texture("player.png"); 
	}
	
	@Override
	public void show() {

	}

	public void update(float deltaTime) {
		// logic
		world.step(1/60f, 6, 2);
		rayHandler.update();
	
		player.playerControls(15, 300, deltaTime);
		
		if (Gdx.input.isKeyJustPressed(Keys.V)) {
			System.out.println(Gdx.graphics.getFramesPerSecond()); 
		}
		
		bullet1.moveBullets(5, deltaTime);
		bullet2.moveBullets(5, deltaTime);
		bullet3.moveBullets(5, deltaTime);
		bullet4.moveBullets(5, deltaTime);
		bullet5.moveBullets(5, deltaTime);
		
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
		
		batch.draw(bTex, bullet1.getBody().getPosition().x-bullet1.getSize().x/2, bullet1.getBody().getPosition().y-bullet1.getSize().y/2,
				bullet1.getSize().x, bullet1.getSize().y);
		batch.draw(bTex, bullet2.getBody().getPosition().x-bullet2.getSize().x/2, bullet2.getBody().getPosition().y-bullet2.getSize().y/2,
				bullet2.getSize().x, bullet2.getSize().y);
		batch.draw(bTex, bullet3.getBody().getPosition().x-bullet3.getSize().x/2, bullet3.getBody().getPosition().y-bullet3.getSize().y/2,
				bullet3.getSize().x, bullet3.getSize().y);
		batch.draw(bTex, bullet4.getBody().getPosition().x-bullet4.getSize().x/2, bullet4.getBody().getPosition().y-bullet4.getSize().y/2,
				bullet4.getSize().x, bullet4.getSize().y);
		batch.draw(bTex, bullet5.getBody().getPosition().x-bullet5.getSize().x/2, bullet5.getBody().getPosition().y-bullet5.getSize().y/2,
				bullet5.getSize().x, bullet5.getSize().y);
		
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
		bTex.dispose();
		playerTex.dispose();
		
		for (ConeLight c: cLset) {c.dispose();}
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA(); 
		Fixture fb = contact.getFixtureB();
		
		if (fa.getUserData() == "groundCheck" || fb.getUserData() == "groundCheck") {
			if (fa.getUserData() == "Ground" || fb.getUserData() == "Ground" || fa.getUserData() == "StaticBox" || fb.getUserData() == "StaticBox" 
					|| fa.getUserData() == "Muremano" || fb.getUserData() =="Muremano") {
				player.numJump = 3;
			}
		}
		
		if (fa.getUserData() == "Ground" || fb.getUserData() == "Ground") {
			if (fa.getUserData() == "b1" || fb.getUserData() == "b1") {				
				bullet1.setDir(bullet1.getDir() * -1);
			}
			if (fa.getUserData() == "b2" || fb.getUserData() == "b2") {				
				bullet2.setDir(bullet2.getDir() * -1);
			}
			if (fa.getUserData() == "b3" || fb.getUserData() == "b3") {				
				bullet3.setDir(bullet3.getDir() * -1);
			}
			if (fa.getUserData() == "b4" || fb.getUserData() == "b4") {				
				bullet4.setDir(bullet4.getDir() * -1);
			}
			if (fa.getUserData() == "b5" || fb.getUserData() == "b5") {				
				bullet5.setDir(bullet5.getDir() * -1);
			}
		}
		
		if (fa.getUserData() == "Player" || fb.getUserData() == "Player") {
			if (fa.getUserData() == "EndDoor" || fb.getUserData() == "EndDoor") {
				game.setScreen(new Screen6(game));
			}
	
			if (fa.getUserData() == "b1" || fb.getUserData() == "b1") {				
				game.setScreen(new Screen5(game));
			}
			if (fa.getUserData() == "b2" || fb.getUserData() == "b2") {				
				game.setScreen(new Screen5(game));
			}
			if (fa.getUserData() == "b3" || fb.getUserData() == "b3") {				
				game.setScreen(new Screen5(game));
			}
			if (fa.getUserData() == "b4" || fb.getUserData() == "b4") {				
				game.setScreen(new Screen5(game));
			}
			if (fa.getUserData() == "b5" || fb.getUserData() == "b5") {				
				game.setScreen(new Screen5(game));
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
