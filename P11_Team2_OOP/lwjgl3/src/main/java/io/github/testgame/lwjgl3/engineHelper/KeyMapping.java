package io.github.testgame.lwjgl3.engineHelper;

import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.Map;

public class KeyMapping {
    private Map<String, Integer> keyMap;

    public KeyMapping() {
        keyMap = new HashMap<>();
        setDefaultKeys();
    }

    private void setDefaultKeys() {
        keyMap.put("MOVE_UP", Input.Keys.W);
        keyMap.put("MOVE_DOWN", Input.Keys.S);
        keyMap.put("MOVE_LEFT", Input.Keys.A);
        keyMap.put("MOVE_RIGHT", Input.Keys.D);
        keyMap.put("JUMP", Input.Keys.SPACE);
    }

    public void setKey(String action, int key) {
        keyMap.put(action, key);
    }

    public int getKey(String action) {
        return keyMap.getOrDefault(action, -1);
    }

    public Map<String, Integer> getKeyMap() {
        return keyMap;
    }
}
