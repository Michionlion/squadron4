package engine.test;

import engine.render.DisplayManager;
import engine.render.Loader;
import assets.models.RawModel;
import assets.models.TexturedModel;
import engine.render.Renderer;
import org.lwjgl.opengl.Display;
import assets.shaders.StaticShader;
import assets.textures.ModelTexture;

public class test {
    
    
    public static void main(String[] args) {
        
        DisplayManager.createDisplay();
        Loader loader= new Loader();
        Renderer renderer = new Renderer();
        StaticShader s = new StaticShader();
        
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
        
        RawModel m = loader.loadToVAO(verts, texCoords, indices);
        ModelTexture tex = new ModelTexture(loader.loadTexture("image"));
        TexturedModel model = new TexturedModel(m, tex);
        while(!Display.isCloseRequested()) {
            renderer.prepare();
            s.start();
            renderer.render(model);
            DisplayManager.updateDisplay();
            s.stop();
        }
        
        s.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
    
}
