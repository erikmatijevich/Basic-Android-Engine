package model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bob { //Player character
	
	public enum State{ //States for Animation
		IDLE, WALKING, JUMPING, DYING 
	}

	public static final float SIZE = 0.5f;
	
	Vector2		position = new Vector2(); 
	Vector2		acceleration = new Vector2();
	Vector2		velocity = new Vector2();
	Rectangle	bounds = new Rectangle();
	State       state = State.IDLE;
	boolean		facingLeft = false;
	boolean 	isJumping = false;
	float		stateTime = 0;
	boolean		longJump = false;
	boolean 	alive = true;
	
	public Bob(Vector2 position){
		this.position = position;
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
	}


public void setState(State newState) {
	this.state = newState;
}

	public void update(float delta) {
		stateTime += delta;
	}

	public boolean isAlive() {
		return this.alive;
	}
	public void setAlive(boolean b){
		this.alive = b;
	}
	


	public Vector2 getPosition() {
		return position;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}


	public void setFacingLeft(boolean b) {
		this.facingLeft = b;
	}

	public void setJumping(boolean b){
		this.isJumping = b;
	}
	
	public boolean getJumping(){
		return isJumping;
	}
	 
	public boolean getFacingLeft(){
		return facingLeft;
	}
	public Vector2 getVelocity() {
		return velocity;
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}


	public State getState() {
		return this.state;
	}


	public float getStateTime() {
		return this.stateTime;
	}
}
