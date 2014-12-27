package engine;

import engine.render.DisplayManager;
import engine.render.Renderer;
import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Globals {

    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;

    public static TickThread logicThread = new TickThread(30);
    
    
    public static CopyOnWriteArrayList<GameObject> gameObjects;

    /**
     * Get the time in milliseconds
     *
     * @return The system time in milliseconds
     */
    public static double getTime() {
        return (Sys.getTime() * 1000d) / Sys.getTimerResolution();
    }

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        DisplayManager.createDisplay();
        try {
            Keyboard.create();
            Mouse.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
            DisplayManager.closeDisplay();
            Sys.alert("ERROR", "Could not create Input Devices!");
        }

        Renderer renderer = new Renderer();

        Thread renderThread = new Thread(renderer, "renderThread");

        renderThread.start();
    }
}
