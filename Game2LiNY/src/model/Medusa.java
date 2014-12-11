package model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Medusa {
	
	public static final float SIZE = 1.0f;
	
	Vector2		position = new Vector2();
	Vector2		acceleration = new Vector2();
	Vector2		velocity = new Vector2();
	Integer 	health = 4;
	Rectangle	bounds = new Rectangle();
	boolean 	facingLeft = true; 
	boolean 	alive = true;
	
	
	public Medusa(Vector2 position){
		this.position = position;
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
	}

	public void update(float delta) {
		position.add(new Vector2(velocity.cpy().tmp().mul(delta).x, velocity.cpy().tmp().mul(delta).y ));
		this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
	}
	
	public boolean isFacingLeft(){
		return this.facingLeft;
	}
	
	public void setFacingLeft(boolean b){
		this.facingLeft = b;
	}

	public Vector2 getPosition() {
		return position;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	public void hit(int amount){
		this.health -= amount;
		if(this.health <= 0){
			setAlive(false);
			this.bounds.height = 0;
			this.bounds.width = 0;
		}
	}
	public void setAlive(boolean b){
		this.alive = b;
	}
	
	public boolean isAlive(){
		return this.alive;
	}
	public Vector2 getVelocity() {
		return velocity;
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}
}
