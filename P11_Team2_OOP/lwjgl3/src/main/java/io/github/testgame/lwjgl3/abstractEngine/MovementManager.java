package io.github.testgame.lwjgl3.abstractEngine;

import io.github.testgame.lwjgl3.entity.moveableObject.iMoveable;

import java.util.Map;

public abstract class MovementManager {
    private IOManager ioManager;
    private Map<String, Integer> keyMapping; // Maps action names to keys
    public MovementManager(){}
    public MovementManager(IOManager ioManager, Map<String, Integer> keyMapping) {
        this.ioManager = ioManager;
        this.keyMapping = keyMapping;
    }
    public abstract void manualMovement(iMoveable entity);
    public abstract void autoMovement(iMoveable entity, iMoveable target);
}
