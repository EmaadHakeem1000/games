package objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import maingame.MainGame;

public class MapBodyBuilder {
	private World world; 
	private TiledMap map;
	
	public MapBodyBuilder(World world, TiledMap map) {
		this.world = world; 
		this.map = map; 
	}
	
	public void defineStaticThing(String id, int layer) {
		BodyDef bDef = new BodyDef(); 
		FixtureDef fDef = new FixtureDef();	
		PolygonShape shape = new PolygonShape();
		Body body;
		
		for (MapObject obj: map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) obj).getRectangle();
			
			bDef.type = BodyDef.BodyType.StaticBody;
			bDef.position.set((rect.getX() + rect.getWidth()/2)/MainGame.PPM, (rect.getY() + rect.getHeight()/2)/MainGame.PPM);
			
			body = world.createBody(bDef);
			
			if (id.equals("EndDoor")) {
				fDef.isSensor = true;
			}
			
			shape.setAsBox(rect.getWidth()/2/MainGame.PPM, rect.getHeight()/2/MainGame.PPM);
			fDef.shape = shape;
			body.createFixture(fDef).setUserData(id);
		}
	}
}











