package engine;

import assets.shaders.StaticShader;
import engine.render.DisplayManager;
import engine.render.Loader;
import engine.render.Renderer;
import java.util.concurrent.CopyOnWriteArrayList;
import engine.util.Input;
import engine.util.MouseInput;
import org.lwjgl.opengl.Display;


public class Globals {
    
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    
    public static Input input;
    public static MouseInput mouseInput;
    
    
    public static CopyOnWriteArrayList<GameObject> gameObjects;
    
    
    public static void main(String[] args) {
        
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        StaticShader s = new StaticShader();
        Renderer renderer = new Renderer(s);
        
        
        while(!Display.isCloseRequested()) {
            renderer.prepare();
            s.start();
            
            //renderer.render();
            
            
            
            DisplayManager.updateDisplay();
            s.stop();
        }
        
        s.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
