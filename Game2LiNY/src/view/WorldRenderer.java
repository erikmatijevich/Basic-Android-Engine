package view;

import model.Block;
import model.Bob;
import model.Bob.State;
import model.Bullet;
import model.Medusa;
import model.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

public class WorldRenderer {

	private static final float CAMERA_WIDTH = 16f;
	private static final float CAMERA_HEIGHT = 9f;
	private static final float FRAME_DUR = 0.06f;
	
	private World world;
	private OrthographicCamera cam;
	
	private TextureRegion bobCurFrame;
	private TextureRegion blockTexture;
	private TextureRegion bobLeft;
	private TextureRegion bobRight;
	private Texture medusaLeftTexture;
	private Texture medusaRightTexture;

	private Texture bulletTexture;
	private Animation walkLeft;
	private Animation walkRight;
	private Bob bob;
	private Medusa medusa;
	private Bullet bullet;

	private SpriteBatch spriteBatch;
	private int width;
	private int height;
	private float ppuX;	// pixels per unit on the X axis
	private float ppuY;	// pixels per unit on the Y axis
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;
		ppuX = (float)width / CAMERA_WIDTH;
		ppuY = (float)height / CAMERA_HEIGHT;
	}
	private float getCamX(){
		float x = 0;
		x = world.getBob().getPosition().x;
		if(x < CAMERA_WIDTH/2f){
			x = CAMERA_WIDTH/2f;
		}
		return x;
	}
	public WorldRenderer(World world) {
		this.world = world;
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
		this.bullet = world.getBullet();
		this.medusa = world.getMedusa();
		this.bob = world.getBob();
		spriteBatch = new SpriteBatch();
		loadTextures();
	}

	private void loadTextures() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures/textures.pack"));
		bobLeft = atlas.findRegion("bob-01");
		bobRight = atlas.findRegion("bob-01");
		bobRight.flip(true, false);
		blockTexture = atlas.findRegion("block");
		TextureRegion[] bobLeft = new TextureRegion[5];
		for(int i = 0; i < 5; i++){
			bobLeft[i] = atlas.findRegion("bob-0" + (i+2));
		}
		walkLeft = new Animation(FRAME_DUR, bobLeft);
		
		TextureRegion[] bobRight = new TextureRegion[5];
		for(int i = 0; i < 5; i++){
			bobRight[i] = new TextureRegion(bobLeft[i]);
			bobRight[i].flip(true,false);
		}
		walkRight = new Animation(FRAME_DUR, bobRight);	
		medusaLeftTexture = new Texture(Gdx.files.internal("images/medusaLeft.png"));
		medusaRightTexture = new Texture(Gdx.files.internal("images/medusaRight.png"));

		bulletTexture = new Texture(Gdx.files.internal("images/bullet.png"));
		
	}
	
	public void render() {
		spriteBatch.begin();
			drawBlocks();
			if(this.bob.isAlive()){
				drawBob();
			}
			if(this.medusa.isAlive()){
				drawMedusa();
			}
			if(this.bullet.isFired()){
				drawBullet();
			}
			drawBullet();
		spriteBatch.end();
		
		this.cam.position.set(CAMERA_WIDTH/2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
	}

	private void drawBlocks() {
		for (Block block : world.getDrawBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
			spriteBatch.draw(blockTexture, blockX(block) * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
		}
	}


	private void drawBob() {
		Bob bob = world.getBob();
		bobCurFrame = bob.getFacingLeft() ? bobLeft : bobRight;
		if(bob.getState().equals(State.WALKING)){
			bobCurFrame = bob.getFacingLeft() ? walkLeft.getKeyFrame(bob.getStateTime(), true) : walkRight.getKeyFrame(bob.getStateTime(), true);
		}
		spriteBatch.draw(bobCurFrame, bobX() * ppuX, bob.getPosition().y * ppuY, Bob.SIZE * ppuX, Bob.SIZE * ppuY);
	}
	private void drawBullet() {
		spriteBatch.draw(bulletTexture,bulletX() * ppuX, bullet.getPosition().y * ppuY, bullet.getBounds().width * ppuX, bullet.getBounds().height * ppuY);
	}
	private void drawMedusa() {
		if(medusa.isFacingLeft()){
			spriteBatch.draw(medusaLeftTexture, medusaX() * ppuX, medusa.getPosition().y * ppuY, Medusa.SIZE * ppuX, Medusa.SIZE * ppuY);
		}else{
			spriteBatch.draw(medusaRightTexture, medusaX() * ppuX, medusa.getPosition().y * ppuY, Medusa.SIZE * ppuX, Medusa.SIZE * ppuY);
		}
	}
	private float blockX(Block block){
		float x = 0;
		if(world.getBob().getPosition().x > CAMERA_WIDTH/2f){
			x = block.getPosition().x - getCamX() + CAMERA_WIDTH/2f;
		}
		else{
			x = block.getPosition().x;
		}
		return x;
	}
	private float bobX(){
		float x = 0;
		if(world.getBob().getPosition().x >= CAMERA_WIDTH / 2f)
			x = CAMERA_WIDTH / 2f;
		else{
			x = world.getBob().getPosition().x;
		}
		return x;
	}
	public float medusaX(){
		float x = 0;
		if(world.getBob().getPosition().x > CAMERA_WIDTH/2f){
			x = world.getMedusa().getPosition().x - getCamX() + CAMERA_WIDTH/2f;
		}
		else{
			x = world.getMedusa().getPosition().x;
		}
		return x;
	}
	public float bulletX(){
		float x = 0;
		if(world.getBob().getPosition().x > CAMERA_WIDTH/2f){
			x = world.getBullet().getPosition().x - getCamX() + CAMERA_WIDTH/2f;
		}
		else{
			x = world.getBullet().getPosition().x;
		}
		return x;
	}
	public static void TextureSetup(){
		TexturePacker2.process("assets/images/", "assets/images/texures", "textures.pack");
	}
	}

