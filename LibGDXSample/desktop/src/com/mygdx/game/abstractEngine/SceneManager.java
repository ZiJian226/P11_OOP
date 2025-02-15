package com.mygdx.game.abstractEngine;

public class SceneManager {
	private static SceneManager instance;
	private String currentScene;
	
	private SceneManager() {
		this.currentScene = "MainMenu";
	}
	
	public static SceneManager getInstance() {
		if (instance == null) {
			instance = new SceneManager();
		}
		return instance;
	}
	
	public void switchScene(String newScene) {
		this.currentScene = newScene;
	}
	
	public String getCurrentScene() {
		return currentScene;
	}
}
