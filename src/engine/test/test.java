package engine.test;

import engine.render.DisplayManager;
import engine.render.Loader;
import assets.models.RawModel;
import assets.models.TexturedModel;
import engine.render.Renderer;
import org.lwjgl.opengl.Display;
import assets.shaders.StaticShader;
import assets.textures.ModelTexture;
import entities.Entity;
import org.lwjgl.util.vector.Vector3f;

public class test {
    
    
    public static void main(String[] args) {
        
        DisplayManager.createDisplay();
        Loader loader= new Loader();
        StaticShader s = new StaticShader();
        Renderer renderer = new Renderer(s);
        float[] verts = {
            -0.5f, 0.5f, 0,
            -0.5f, -0.5f, 0,
            0.5f, -0.5f, 0,
            0.5f,0.5f,0
        };
        
        int[] indices = {
            0,1,3,
            3,1,2
        };
        
        float[] texCoords = {
            0,0,
            0,1,
            1,1,
            1,0
            
        };
        TexturedModel model = new TexturedModel(loader.loadToVAO(verts, texCoords, indices), new ModelTexture(loader.loadTexture("image")));
        
        Entity e = new Entity(model, new Vector3f(0,0,-1), new Vector3f(0,0,0), 1);
        
        
        while(!Display.isCloseRequested()) {
            e.move(new Vector3f(0,0,-0.1f));
            renderer.prepare();
            s.start();
            renderer.render(e, s);
            DisplayManager.updateDisplay();
            s.stop();
        }
        
        s.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
    
}
