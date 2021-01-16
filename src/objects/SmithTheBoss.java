package objects; 

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;

import maingame.MainGame;

public class SmithTheBoss{
    private World world; 
    private Body body;
    private BodyDef bDef;

    private Vector2 pos;
    private Vector2 size;

    public int numJumps;
    public int health;

    private Player player;

    public SmithTheBoss(World world, Vector2 pos, Vector2 size, Player player){
        this.world = world; 
        this.pos = pos; 
        this.size = size;
        this.player = player;

        numJumps = 0;
        health = 5;
    }

    public void defineBoss(){   
        bDef = new BodyDef(); 
		FixtureDef fDef = new FixtureDef(); 
		PolygonShape shape = new PolygonShape(); 
	
		FixtureDef fDefGroundCheck = new FixtureDef();
        FixtureDef fDefHitBox = new FixtureDef();

        bDef.type = BodyDef.BodyType.DynamicBody; 
        bDef.position.set(pos.x/MainGame.PPM, pos.y/MainGame.PPM);
		
		body = world.createBody(bDef);
		
		shape.setAsBox((size.x/2)/MainGame.PPM, (size.y/2)/MainGame.PPM);
		fDef.shape = shape; 
		body.createFixture(fDef).setUserData("SmithBoss");

        EdgeShape groundCheck = new EdgeShape();
		groundCheck.set(new Vector2((-size.x * 0.45f) / MainGame.PPM, (-size.y * 0.5f) / MainGame.PPM), 
            new Vector2((size.x * 0.45f) / MainGame.PPM, (-size.y * 0.5f) / MainGame.PPM ));
		
		fDefGroundCheck.shape = groundCheck;
		body.createFixture(fDefGroundCheck).setUserData("groundCheckSmith");

        EdgeShape hitBox = new EdgeShape(); 
        hitBox.set(new Vector2((-size.x * 0.45f) / MainGame.PPM, (size.y * 0.5f) / MainGame.PPM), 
            new Vector2((size.x * 0.45f) / MainGame.PPM, (size.y * 0.5f) / MainGame.PPM));

        fDefHitBox.shape = hitBox; 
        body.createFixture(fDefHitBox).setUserData("hitBoxSmith");  

        body.setLinearDamping(5);
    }

    public void controlAI(float speed, float jumpForce, float deltaTime){
        if (numJumps == 1){
            body.applyLinearImpulse(new Vector2(0, jumpForce * deltaTime), body.getWorldCenter(), true);
            numJumps = 0;
        }

        if (body.getPosition().x < player.getPosition().x){
            body.applyLinearImpulse(new Vector2(speed*deltaTime, 0), body.getWorldCenter(), true);
        }else if (body.getPosition().x > player.getPosition().x) {
            body.applyLinearImpulse(new Vector2(-speed*deltaTime, 0), body.getWorldCenter(), true);
        }
    }

    public Body getBody(){
        return body;
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public Vector2  getSize(){
        return new Vector2(size.x/MainGame.PPM, size.y/MainGame.PPM);
    }
}