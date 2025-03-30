package io.github.testgame.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    private static int width = 1800;
    private static int height = 900;

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
//        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
//        config.setForegroundFPS(60);
//        config.setTitle("1009 engine");
//        config.setWindowedMode(width, height);
//        new Lwjgl3Application(new GameMaster(), config);
        return new Lwjgl3Application(new GameMaster(width, height), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Scrub and Shoot");
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// scene tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(true);
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        configuration.setForegroundFPS(60);
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause scene tearing.
        configuration.setWindowedMode(width, height);
        //// You can change these files; they are in lwjgl3/src/main/resources/ .
//        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        configuration.setWindowIcon("texture/player.png");
        return configuration;
    }
}
