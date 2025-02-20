package io.github.testgame.lwjgl3.collision;

import java.util.Queue;
import java.util.LinkedList;

public class PostStepActionProcessor {
    private final Queue<Runnable> postStepActions = new LinkedList<>();

    public void addPostStepAction(Runnable action) {
        postStepActions.add(action);
    }
    // Execute all post step actions in the queue
    public void processPostStepActions() {
        while (!postStepActions.isEmpty()) {
            postStepActions.poll().run();
        }
    }
}
