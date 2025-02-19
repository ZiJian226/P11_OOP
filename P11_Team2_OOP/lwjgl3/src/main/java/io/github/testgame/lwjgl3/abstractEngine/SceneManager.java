package io.github.testgame.lwjgl3.abstractEngine;

import io.github.testgame.lwjgl3.scene.SceneType;

public class SceneManager {
	private static SceneManager instance;
	private SceneType currentScene;

	private SceneManager() {
		this.currentScene = SceneType.MAIN_MENU;
	}

	public static SceneManager getInstance() {
		if (instance == null) {
			instance = new SceneManager();
		}
		return instance;
	}

	public void changeScene(SceneType newScene) {
		this.currentScene = newScene;
	}

	public SceneType getCurrentScene() {
		return currentScene;
	}
}
