package controller;

import java.util.HashMap;
import java.util.Map;

import model.Block;
import model.Bob;
import model.Bob.State;
import model.Bullet;
import model.Medusa;
import model.World;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class WorldController {

	enum Keys {
		LEFT, RIGHT, JUMP, FIRE
	}

	private static final long 	MAX_JUMP_PRESS 	= 120l;
	private static final float 	ACCELERATION 	= 20f;
	private static final float 	GRAVITY 		= -10f;
	private static final float 	MAX_JUMP_VEL 	= 5f;
	private static final float 	DAMP 			= .9f;
	private static final float 	MAX_VEL 		= 5f;
	private World 		world;
	private Bob 		bob;
	private Bullet 		bullet;
	private Medusa		medusa;
	private float 		deltaTotal;
	private boolean 	grounded;
	private boolean 	canJump;
	private long		timeJump;
	
	static Map<Keys, Boolean> keys = new HashMap<WorldController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.JUMP, false);
		keys.put(Keys.FIRE, false);
	};
	private Array<Block> collides = new Array<Block>();
	
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	public WorldController(World world) {
		this.world = world;
		this.bob = world.getBob();
		this.bullet = world.getBullet();
		this.medusa = world.getMedusa();
	}

	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}

	public void jumpPressed() {
		keys.get(keys.put(Keys.JUMP, true));
	}

	public void firePressed() {
		keys.get(keys.put(Keys.FIRE, true));
	}

	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}

	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}

	public void jumpReleased() {
		keys.get(keys.put(Keys.JUMP, false));
	}

	public void fireReleased() {
		keys.get(keys.put(Keys.FIRE, false));
	}

	public void fontshit(){
		
	}
	/** The main update method **/
	public void update(float delta) {
		if (grounded && bob.getState().equals(State.JUMPING)) {
			bob.setState(State.IDLE);
		}
	
		// ensure terminal velocity is not exceeded
		if (bob.getVelocity().x > MAX_VEL) {
			bob.getVelocity().x = MAX_VEL;
		}
		if (bob.getVelocity().x < -MAX_VEL) {
			bob.getVelocity().x = -MAX_VEL;
		}
		deltaTotal += delta;
		bulletHit(bullet, medusa);
		bobHit(bob, medusa);
		
		if(bob.isAlive()){
			bob.update(delta);
			bob.getAcceleration().y = GRAVITY;
			bob.getAcceleration().mul(delta);
			bob.getVelocity().add(bob.getAcceleration().x, bob.getAcceleration().y);
			checkCollisionWithBlocks(delta);
			bob.getVelocity().x *= DAMP;
			processInput();
		}
		
	}
	
	public void medusaRender(Medusa m, float delta, float speed, float amplitude, float period){
		if(medusa.getVelocity().x > 0){
			medusa.setFacingLeft(false);
		}else{
			medusa.setFacingLeft(true);
		}
		if(bob.getPosition().x > 40){
		m.getVelocity().x = (float) (amplitude * 1.75*  Math.sin(deltaTotal  * .25 *period));
		m.getVelocity().y = (float) (amplitude * Math.cos(deltaTotal * period));
		}
	}
	
	
	private void processInput() {
		if(keys.get(Keys.FIRE)){
			if(bullet.getCharging() >= 150){
				bullet.fire(bob.getPosition().x, bob.getPosition().y, bob.getBounds(), bob.getFacingLeft());
			}
		}
		if (keys.get(Keys.JUMP)) {
			if (!bob.getState().equals(State.JUMPING)) {
				canJump = false;
				timeJump = System.currentTimeMillis();
				bob.setState(State.JUMPING);
				bob.getVelocity().y = MAX_JUMP_VEL; 
				grounded = false;
				
			} else {
				if (canJump && ((System.currentTimeMillis() - timeJump) >= MAX_JUMP_PRESS)) {
					canJump = false;
			
				}
			}
		}
		if (keys.get(Keys.RIGHT)) {
			bob.setFacingLeft(false);
			if(!bob.getState().equals(State.JUMPING)){
				bob.setState(State.WALKING);
			}
			bob.getAcceleration().x = ACCELERATION;
		}else if (keys.get(Keys.LEFT)) {
			bob.setFacingLeft(true);
			if(!bob.getState().equals(State.JUMPING)){
				bob.setState(State.WALKING);
			}
			bob.getAcceleration().x = -ACCELERATION;
		}else {
			if (!bob.getState().equals(State.JUMPING)) {
				bob.setState(State.IDLE);
			}
			bob.getAcceleration().x = 0;
		}
	
	}
	private void populateCollides(int startX, int startY, int endX, int endY){
		collides.clear();
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				if (x >= 0 && x < world.getWidth() && y >=0 && y < world.getHeight()) {
					collides.add(world.get(x, y));
				}
			}
		}
	}
	public void bobHit(Bob b, Medusa m){
		if(b.getPosition().y <0){
			b.setAlive(false);
		}
		if(Intersector.intersectRectangles(b.getBounds(), m.getBounds())){
			if(m.isAlive()){
			b.setAlive(false);
			}
		}
		if(Intersector.intersectRectangles(m.getBounds(), b.getBounds())){
			if(m.isAlive()){
				b.setAlive(false);
				}
		}
	}
	public void bulletHit(Bullet b, Medusa m){
		if(Intersector.intersectRectangles(b.getBounds(), m.getBounds())){
			m.hit(1);
			b.getPosition().x = -100;
		}
		
	}
	private void checkCollisionWithBlocks(float delta) {
		bob.getVelocity().mul(delta);
		Rectangle bobRect = rectPool.obtain();
		bobRect.set(bob.getBounds().x, bob.getBounds().y, bob.getBounds().width, bob.getBounds().height);
		int startX, endX;
		int startY = (int) bob.getBounds().y;
		int endY = (int) (bob.getBounds().y + bob.getBounds().height);
		if (bob.getVelocity().x < 0) {
			startX = endX = (int) Math.floor(bob.getBounds().x + bob.getVelocity().x);
		} else {
			startX = endX = (int) Math.floor(bob.getBounds().x + bob.getBounds().width + bob.getVelocity().x);
		}
		populateCollides(startX, startY, endX, endY);
		bobRect.x += bob.getVelocity().x;
		world.getCollisionRects().clear();
		for (Block block : collides) {
			if (block == null) continue;
			if (Intersector.intersectRectangles(bobRect, block.getBounds())) {
				bob.getVelocity().x = 0;
				world.getCollisionRects().add(block.getBounds());
				break;
			}
		}
		bobRect.x = bob.getPosition().x;
		startX = (int) bob.getBounds().x;
		endX = (int) (bob.getBounds().x + bob.getBounds().width);
		if (bob.getVelocity().y < 0) {
			startY = endY = (int) Math.floor(bob.getBounds().y + bob.getVelocity().y);
		} else {
			startY = endY = (int) Math.floor(bob.getBounds().y + bob.getBounds().height + bob.getVelocity().y);
		}
		populateCollides(startX, startY, endX, endY);
		bobRect.y += bob.getVelocity().y;
		for (Block block : collides) {
			if (block == null) continue;
			if (Intersector.intersectRectangles(bobRect, block.getBounds())) {
				if (bob.getVelocity().y < 0) {
					grounded = true;
				}
				bob.getVelocity().y = 0;
				world.getCollisionRects().add(block.getBounds());
				break;
			}
		}
		bobRect.y = bob.getPosition().y;
		bob.getPosition().add(bob.getVelocity());
		
		bob.getBounds().x = bob.getPosition().x;
		bob.getBounds().y = bob.getPosition().y;
		bob.getVelocity().mul(1 / delta);
		if(medusa.isAlive()){
			medusaRender(medusa, delta, 3f, 4, 5);
			medusa.update(delta);
		}
		if(bullet.isFired()){
			bullet.update(delta);
		}
	}
	

}
