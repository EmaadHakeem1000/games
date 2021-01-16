package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import maingame.MainGame;

public class Player {
	private World world;
	private Body body; 
	
	private Vector2 pos, size;
	
	public int numJump;
	public int health;
	
	public Player(World world, Vector2 pos, Vector2 size) {
		this.world = world;
		this.pos = pos; 
		this.size = size;
		
		numJump = 0;
		health = 20;
	}
	
	public void definePlayer() {
		BodyDef bDef = new BodyDef(); 
		FixtureDef fDef = new FixtureDef(); 
		PolygonShape shape = new PolygonShape(); 
	
		FixtureDef fDefGroundCheck = new FixtureDef();
		
		bDef.type = BodyDef.BodyType.DynamicBody; 
		bDef.position.set(pos.x/MainGame.PPM, pos.y/MainGame.PPM);
		
		body = world.createBody(bDef);
		
		shape.setAsBox((size.x/2)/MainGame.PPM, (size.y/2)/MainGame.PPM);
		fDef.shape = shape; 
		body.createFixture(fDef).setUserData("Player");

		EdgeShape groundCheck = new EdgeShape();
		groundCheck.set(new Vector2((-size.x * 0.4f) / MainGame.PPM, (-size.y * 0.5f) / MainGame.PPM), 
            new Vector2((size.x * 0.4f) / MainGame.PPM, (-size.y * 0.5f) / MainGame.PPM ));
		
		fDefGroundCheck.shape = groundCheck;
		body.createFixture(fDefGroundCheck).setUserData("groundCheck");
		
		body.setLinearDamping(5f);
	}
	
	public void playerControls(float speed, float jumpForce, float deltaTime) {
		if (Gdx.input.isKeyPressed(Keys.A)) {
			body.applyLinearImpulse(new Vector2(-speed*deltaTime, 0), body.getWorldCenter(), true);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			body.applyLinearImpulse(new Vector2(speed*deltaTime, 0), body.getWorldCenter(), true);
		} 
		
		if (numJump >= 1) {
			if (Gdx.input.isKeyJustPressed(Keys.W)) {
				body.applyLinearImpulse(new Vector2(0, jumpForce*deltaTime), body.getWorldCenter(), true);
				numJump -= 1;
			}
			else if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				body.applyLinearImpulse(new Vector2(0, jumpForce*deltaTime), body.getWorldCenter(), true);
				numJump -= 1;
			}
		}
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public Vector2 getSize() {
		Vector2 nVecSize = new Vector2(size.x/MainGame.PPM, size.y/MainGame.PPM);
		
		return nVecSize;
	}
	
	public Body getBody() {
		return body;
	}
}
