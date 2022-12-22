package com.mygdx.pong;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.util.Locale;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(Application.APP_FPS);
		config.setTitle(String.format(Locale.ROOT, "%s v%.1f",Application.APP_TITLE, Application.APP_VERSION));
		config.setWindowedMode(Application.APP_DESKTOP_WIDTH, Application.APP_DESKTOP_HEIGHT);
		config.setResizable(true);
		new Lwjgl3Application(new Application(), config);
	}
}
