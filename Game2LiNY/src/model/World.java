package model;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World{
	Integer width;
	Integer height;

	private Block[][] blocks;
	Array<Rectangle> collisionRects = new Array<Rectangle>();
	public World(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	Bob bob;
	Bullet bullet;
	Medusa medusa;

	public Block[][] getBlocks() {
		return blocks;
	}
	public Array<Rectangle> getCollisionRects() {
		return collisionRects;
	}
	
	public Block get(int x, int y){
		return blocks[x][y];
	}
	
	public int getHeight(){
		return height;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setWidth(int width	){
		this.width = width;
	}
	public Medusa getMedusa(){
		return medusa;
	}
	public Bullet getBullet(){
		return bullet;
	}
	public Bob getBob() {
		return bob;
	}

	public World() {
		createDemoWorld();
	}
	
	private void createDemoWorld() {
		  bob = new Bob(new Vector2(3, 5));
		  bullet = new Bullet(new Vector2(-1,-1));
		  medusa  = new Medusa(new Vector2(49, 1));
		  width = 70;
		  height = 9;
		  blocks = new Block[width][height];
			for (int col = 0; col < width; col++) {
				for (int row = 0; row < height; row++) {
					blocks[col][row] = null;
				}
			}

			for (int i = 0; i < 70; i++) {
				if(((i<16)&&(i>=0))||i>33){
					blocks[i][0]= new Block(new Vector2(i, 0));   
				}else if((i>16)&&(i<34)&&(i%2==0)){
					blocks[i][0]= new Block(new Vector2(i, 0));   
				}
				
				for(int j = 0; j <9; j++){
					if((i>50)&&(i<70)){
						blocks[i][j] = new Block(new Vector2(i, j));
					}
				}
				blocks[34][1] = new Block(new Vector2(34, 1));
				if((i >35)&&(i<40)){
					blocks[i][1] = new Block(new Vector2(i, 1));
					blocks[i][2] = new Block(new Vector2(i, 2));

				}
			     blocks[i][7] = new Block(new Vector2(i, 7));   
			     blocks[i][8] = new Block(new Vector2(i, 8));        
			     if (i < 13){
			      blocks[i][1] = new Block(new Vector2(i, 1));
			     }
			     if(i  < 9){
			      blocks[i][2] = new Block(new Vector2(i, 2));
			      blocks[i][3] = new Block(new Vector2(i, 3));
			     }
			     blocks[0][4] = new Block(new Vector2(0, 4));
			     blocks[0][5] = new Block(new Vector2(0, 5));
			     blocks[0][6] = new Block(new Vector2(0, 6));
			}
			blocks[3][1] = new Block(new Vector2(3,1));
	}
	public List<Block> getDrawBlocks(int width, int height) {
		int x = (int)bob.getPosition().x - width;
		int y = (int)bob.getPosition().y - height;
		if(x<0){
			x=0;
		}
		if(y<0){
			y=0;
		}
		int xt = x + 2 * width;
		int yt = y + 2 * height;
		if (xt > getWidth()) {
			xt = getWidth() - 1;
		}
		if(yt > getHeight()){
			yt = getHeight() - 1;
		}
		
		List<Block> blocks = new ArrayList<Block>();
		
		Block block;
		for (int col = x; col <= xt; col++) {
			for (int row = y; row <= yt; row++) {
				block = getBlocks()[col][row];
				if(block != null){
					blocks.add(block);
				}
			}
		}
		return blocks;
	}
}
