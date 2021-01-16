package objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import maingame.MainGame;

public class BulletSpawner {
	private World world;
	private Body body;
	
	private Vector2 pos;
	private Vector2 size;

	private float dir;
	
	private String id;
	
	public BulletSpawner(World world, Vector2 pos, Vector2 size, float dir, String id) {
		this.world = world; 
		this.pos = pos; 
		this.size = size;
		this.dir = dir;
		this.id = id;
	}

	BodyDef bDef = new BodyDef(); 
	PolygonShape shape = new PolygonShape();
	FixtureDef fDef = new FixtureDef();
	
	public void defineSpawner() {
		bDef.type = BodyDef.BodyType.DynamicBody;
		bDef.gravityScale = 0;
		bDef.position.set(pos.x/MainGame.PPM, pos.y/MainGame.PPM);
		
		body = world.createBody(bDef);
		
		shape.setAsBox((size.x/2)/MainGame.PPM, (size.y/2)/MainGame.PPM);
		fDef.shape = shape; 
		body.createFixture(fDef).setUserData(id);
	}
	
	public void moveBullets(float speed, float deltaTime) { 							
		body.applyLinearImpulse(new Vector2(speed * deltaTime * dir, 0), body.getWorldCenter(), true);
	}
	
	public Body getBody() {
		return body;
	}
	
	public Vector2 getSize() {
		return new Vector2(size.x/MainGame.PPM, size.y/MainGame.PPM);
	}
	
	public float getDir() {
		return dir;
	}
	
	public void setDir(float dir) {
		this.dir = dir;
	}
}
