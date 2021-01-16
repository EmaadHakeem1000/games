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
import objects.BulletSpawner;
import objects.MapBodyBuilder;
import objects.Player;

public class Screen4 implements Screen, ContactListener{
	
private SpriteBatch batch;
	
	MainGame game;
	
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	
	private OrthogonalTiledMapRenderer mapRenderer; 
	private TiledMap map; 
	private TmxMapLoader mapLoader;
	
	private World world;
	
	private RayHandler rayHandler;
	Set<ConeLight> cLset = new HashSet<ConeLight>();
	
	private Player player;
	private BulletSpawner bS;
	
	private Texture bulletTex, playerTex, jumpPadTex;
	
	public Screen4(MainGame game) {
		this.game = game;
		
		batch = new SpriteBatch();
		
		gameCam = new OrthographicCamera(); 
		gamePort = new FitViewport(MainGame.wWidth/MainGame.PPM, MainGame.wHeight/MainGame.PPM, gameCam);
		
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("map4.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/MainGame.PPM);
		gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
		
		world = new World(new Vector2(0, MainGame.gravity), true);
		world.setContactListener(this);
		
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.1f);
		rayHandler.useCustomViewport(gamePort.getScreenX(), gamePort.getScreenY(), MainGame.wWidth, MainGame.wHeight);
		
		ConeLight cLight1 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 20/MainGame.PPM, 570/MainGame.PPM, 320, 30);
		ConeLight cLight2 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 1420/MainGame.PPM, 570/MainGame.PPM, 220, 30);
		ConeLight cLight3 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 1600/MainGame.PPM, 570/MainGame.PPM, 320, 30);
		ConeLight cLight4 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 3420/MainGame.PPM, 570/MainGame.PPM, 220, 30);
		ConeLight cLight5 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 2800/MainGame.PPM, 570/MainGame.PPM, 220, 30);
		ConeLight cLight6 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 3600/MainGame.PPM, 570/MainGame.PPM, 320, 30);
		ConeLight cLight7 = new ConeLight(rayHandler, 100, Color.WHITE, 10, 4970/MainGame.PPM, 570/MainGame.PPM, 220, 30);
		
		cLset.add(cLight1);cLset.add(cLight2);cLset.add(cLight3);cLset.add(cLight4);cLset.add(cLight5);cLset.add(cLight6);cLset.add(cLight7); 
		
		MapBodyBuilder ground = new MapBodyBuilder(world, map);
		ground.defineStaticThing("Ground", 1);
		
		MapBodyBuilder jumppad = new MapBodyBuilder(world, map);
		jumppad.defineStaticThing("JumpPad", 2);
		
		MapBodyBuilder thorns = new MapBodyBuilder(world, map); 
		thorns.defineStaticThing("playerKill", 3);
		
		MapBodyBuilder door = new MapBodyBuilder(world, map);
		door.defineStaticThing("EndDoor", 4);
		
		player = new Player(world, new Vector2(50, 200), new Vector2(20, 20));
		player.definePlayer();
		
		bS = new BulletSpawner(world, new Vector2(3500, 70), new Vector2(10, 10), -1, "Bullet");
		bS.defineSpawner();
		
		bulletTex = new Texture("bullet.png");
		playerTex = new Texture("player.png");
		jumpPadTex = new Texture("jumppadT.png");
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
		
		bS.moveBullets(5f, deltaTime);
		
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
		batch.draw(bulletTex, bS.getBody().getPosition().x-bS.getSize().x/2, bS.getBody().getPosition().y-bS.getSize().y/2,
				bS.getSize().x, bS.getSize().y);
		
		batch.draw(jumpPadTex, 3900/MainGame.PPM, 50/MainGame.PPM, 200/MainGame.PPM, 200/MainGame.PPM);
		
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
		bulletTex.dispose(); 
		playerTex.dispose();
		
		for (ConeLight c: cLset) {c.dispose();}
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if (fa.getUserData() == "Player" || fb.getUserData() == "Player") {
			if (fa.getUserData() == "EndDoor" || fa.getUserData() == "EndDoor") {
				game.setScreen(new Screen5(game));
			}
			if (fa.getUserData() == "playerKill" || fb.getUserData() == "playerKill" || fa.getUserData() == "Bullet" || fb.getUserData() == "Bullet") {
				game.setScreen(new Screen4(game));
			}
		}
		
		if (fa.getUserData() == "groundCheck" || fb.getUserData() == "groundCheck") {
			if (fa.getUserData() == "Ground" || fb.getUserData() == "Ground" || fa.getUserData() == "StaticBox" || fb.getUserData() == "StaticBox") {
				player.numJump = 3;
			}
			if (fa.getUserData() == "JumpPad" || fb.getUserData() == "JumpPad") {
				player.getBody().applyLinearImpulse(new Vector2(0, 1900 * Gdx.graphics.getDeltaTime()), player.getBody().getWorldCenter(), true);
			}
		}
		
		if (fa.getUserData() == "Ground" || fb.getUserData() == "Ground") {
			if (fa.getUserData() == "Bullet" || fb.getUserData() == "Bullet") {
				bS.setDir(bS.getDir() * -1);
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
		
	}

}