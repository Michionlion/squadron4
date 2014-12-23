package engine.test;

import engine.render.DisplayManager;
import engine.render.Loader;
import assets.models.RawModel;
import engine.render.Renderer;
import org.lwjgl.opengl.Display;
import assets.shaders.StaticShader;

public class test {
    
    
    public static void main(String[] args) {
        
        DisplayManager.createDisplay();
        Loader loader= new Loader();
        Renderer renderer = new Renderer();
        StaticShader s = new StaticShader();
        
        float[] verts = {
            -0.8f, 0.8f, 0,
            -0.5f, -0.5f, 0,
            0.1f, -0.1f, 0,
            0.5f,1f,0
        };
        
        int[] indices = {
            0,1,2,
            3,1,2
        };
        
        RawModel m = loader.loadToVAO(verts, indices);
        
        while(!Display.isCloseRequested()) {
            renderer.prepare();
            s.start();
            renderer.render(m);
            DisplayManager.updateDisplay();
            s.stop();
        }
        
        s.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
    
}
