package engine;

import assets.models.RawModel;
import assets.shaders.SpriteShader;
import assets.sprites.Sprite;
import engine.render.DisplayManager;
import engine.render.Loader;
import engine.render.Renderer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;


public class Globals {
    
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    
    
    public static CopyOnWriteArrayList<GameObject> gameObjects;
    
    public static void main(String[] args) {
        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        DisplayManager.createDisplay();
        try {
            Keyboard.create();
            Mouse.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
            cleanUp();
        }
        
        
        
        SpriteShader s = new SpriteShader();
        
        Renderer renderer = new Renderer();
        Sprite sprite = new Sprite(Loader.getTexture("demo"), 0, 0, 0, 1);
        
        while(!Display.isCloseRequested()) {
            renderer.prepare();
            
            
            if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
                sprite.rotate(1);
            } else if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
                sprite.rotate(-1);
            }
            
            if(Keyboard.isKeyDown(Keyboard.KEY_W)) sprite.translate(0, 0.2f);
            else if(Keyboard.isKeyDown(Keyboard.KEY_S)) sprite.translate(0, -0.2f);
            if(Keyboard.isKeyDown(Keyboard.KEY_A)) sprite.translate(0.2f, 0);
            else if(Keyboard.isKeyDown(Keyboard.KEY_D)) sprite.translate(-0.2f, 0);
            
            
            
            renderer.render(sprite, s);
            
            
            
            DisplayManager.updateDisplay();
            
            
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) break;
        }
        
        s.cleanUp();
        cleanUp();
    }
    
    public static void cleanUp() {
        Loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
