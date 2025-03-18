package io.github.testgame.lwjgl3;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class Camera {
    private OrthographicCamera camera = null;

    private final float SCALE = 1.0f;

    public Camera(float width, float height){
        camera = new OrthographicCamera();
        camera.setToOrtho(false,width/SCALE, height/SCALE);
    }

    public OrthographicCamera getCamera(){
        return camera;
    }

    public void cameraUpdate(float delta, Vector3 position){
        camera.position.set(position);
        camera.update();
    }
}
