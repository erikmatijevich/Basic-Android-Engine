package model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
	
	public static final float SIZE = 0.2f;
	public static final float SPEED = 7f;

	Vector2		position = new Vector2();
	Vector2		velocity = new Vector2();
	Rectangle	bounds = new Rectangle();
	Integer 	charging = 150;
	boolean		fired = false;
	
	public Bullet(Vector2 position){
		this.position = position;
		this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
		
	}

	
	public void update(float delta){
		position.add(new Vector2(velocity.cpy().tmp().mul(delta).x, velocity.cpy().tmp().mul(delta).y));
		this.charging++;
		this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
	}
	
	
	public void fire(float posX, float posY, Rectangle b, boolean direction) {
		this.position.y = posY + (bounds.height + this.getBounds().height)/2;
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
		if(direction){
			this.position.x = posX - b.width / 2;
			this.velocity.x = -SPEED;
		}else{
			this.position.x = posX + b.width / 2;
			this.velocity.x = SPEED;
		}
		setFired(true);
		this.charging = 0;
	}

	public Vector2 getPosition() {
		return position;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setFired(boolean b){
		this.fired = b;
	}

	public boolean isFired(){
		return fired;
	}
	public Vector2 getVelocity() {
		return velocity;
	}

	
	public int getCharging() {
		return charging;
	}
}

