package engine;

import assets.models.RawModel;
import assets.shaders.SpriteShader;
import assets.shaders.StaticShader;
import assets.sprites.Sprite;
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
        SpriteShader s = new SpriteShader();
        
        Renderer renderer = new Renderer();
        Sprite sprite = new Sprite(Loader.getTexture("iconify"), 0, 0, 0, 1);
        
        while(!Display.isCloseRequested()) {
            renderer.prepare();
            sprite.rotate(0.1f);
//            sprite.translate(0.001f, 0);
            renderer.render(sprite, s);
            
            
            
            DisplayManager.updateDisplay();
        }
        
        s.cleanUp();
        Loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
