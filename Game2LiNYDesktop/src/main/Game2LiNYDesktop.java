package main;

import main.Game2LiNY;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Game2LiNYDesktop {
	public static void main(String[] args) {
		new LwjglApplication(new Game2LiNY(), "Game 2: Lost in New York", 960, 540, true);
	}
}