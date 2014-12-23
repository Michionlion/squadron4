/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package engine.test;

import engine.render.DisplayManager;
import engine.render.Loader;
import engine.render.RawModel;
import engine.render.Renderer;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Saejin
 */
public class test {
    
    
    public static void main(String[] args) {
        
        DisplayManager.createDisplay();
        Loader loader= new Loader();
        Renderer renderer = new Renderer();
        
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
        
        RawModel m = loader.loadToVAO(verts, indices);
        
        while(!Display.isCloseRequested()) {
            renderer.prepare();
            
            renderer.render(m);
            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
    
}
