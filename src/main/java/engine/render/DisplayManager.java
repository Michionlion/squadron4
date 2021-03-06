package engine.render;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import engine.Globals;

public class DisplayManager {

    public static void createDisplay() {

        try {
            Display.setDisplayMode(new DisplayMode(Globals.WIDTH, Globals.HEIGHT));
            Display.create(new PixelFormat(), new ContextAttribs());
            Display.setTitle("Squadron 4");
        } catch (LWJGLException ex) {
            Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
        }

        //        GL11.glViewport(0, 0, Globals.WIDTH, Globals.HEIGHT);
    }

    public static void updateDisplay() {
        Display.update();
        Display.sync(Globals.FPS_CAP);
    }

    public static void closeDisplay() {

        Display.destroy();
    }
}
