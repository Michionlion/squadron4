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
    public static RawModel QUAD;
    
    
    public static Input input;
    public static MouseInput mouseInput;
    
    
    public static CopyOnWriteArrayList<GameObject> gameObjects;
    
    
    public static void main(String[] args) {
        
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        SpriteShader s = new SpriteShader();
        
        float[] verts = 
        {0f,0f,0f,
         0f,1f,0f,
         1f,0f,0f,
         1f,1f,0f};
        
        float[] texs = {0f,0f,1f,1f};
        
        int[] indices = {1,2,3, 2,4,3};
        
        QUAD = loader.loadToVAO(verts, texs, indices);
        
        Renderer renderer = new Renderer();
        Sprite sprite = new Sprite(loader.getTexture("close"), 0, 0, 0, 1);
        
        while(!Display.isCloseRequested()) {
            renderer.prepare();
            s.start();
            
            renderer.render(sprite, s);
            
            
            
            DisplayManager.updateDisplay();
            s.stop();
        }
        
        s.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
