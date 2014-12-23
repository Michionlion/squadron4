package engine.render;

import engine.Globals;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
    
    private static final int WIDTH=1280,HEIGHT=720;
    private static final int FPS_CAP = 120;
    

    public static void createDisplay() {
        
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true));
            Display.setTitle("Squadron 4");
        } catch (LWJGLException ex) {
            Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
        
    }

    public static void closeDisplay() {
        
        Display.destroy();
    }

}
