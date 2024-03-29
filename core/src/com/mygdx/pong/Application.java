package com.mygdx.pong;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.pong.managers.GameScreenManager;
import com.mygdx.pong.managers.InputManager;

public class Application extends Game {

	// Variables de l’application
	public static String APP_TITLE = "Pong GDX";
	public static double APP_VERSION = 0.1;
	public static int APP_DESKTOP_WIDTH = 1024;
	public static int APP_DESKTOP_HEIGHT = 580;
	public static int APP_FPS = 60;

	// Variables du Jeu
	public static int V_WIDTH = 1024;
	public static int V_HEIGHT = 580;

	// Managers
	public GameScreenManager gsm;
	public AssetManager assets;
	public InputManager input;

	// Batches
	public SpriteBatch batch;
	public ShapeRenderer shapeBatch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeBatch = new ShapeRenderer();

		// Mis en place des managers
		input = InputManager.getInstance(this);
		assets = new AssetManager();
		gsm = GameScreenManager.getInstance(this);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();		// L’ordre est important
		batch.dispose();
		shapeBatch.dispose();
		input.dispose();
		assets.dispose();
		gsm.dispose();
		Gdx.app.exit();
	}
}
