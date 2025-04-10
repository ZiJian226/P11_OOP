package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.InputAdapter;

import java.util.HashSet;
import java.util.Set;

public class IOManager extends InputAdapter {
    private final Set<Integer> keysPressed = new HashSet<>();

    @Override
    public boolean keyDown(int keycode) {
        keysPressed.add(keycode);
        return true;
    }
    @Override
    public boolean keyUp(int keycode) {
        keysPressed.remove(keycode);
        return true;
    }
    public Set<Integer> getKeysPressed() {
        return keysPressed;
    }
    public void clearKeysPressed() {
        keysPressed.clear();
    }
}
