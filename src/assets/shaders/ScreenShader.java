/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package assets.shaders;

import engine.interfaces.SpriteShader;
import engine.util.Util;
import org.lwjgl.util.vector.Matrix4f;

/**
 *
 * @author Saejin
 */
public class ScreenShader extends ShaderProgram implements SpriteShader {

    private static final String VERTEX_FILE = "src/assets/shaders/screen_shader.vert";
    private static final String FRAGMENT_FILE = "src/assets/shaders/screen_shader.frag";
    
    private int location_transformationMatrix;
    
    public ScreenShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        start();
        loadMatrix(super.getUniformLocation("projectionMatrix"), Util.createOrthoMatrix());
        stop();
    }
    
    
    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    public void loadTransformationMatrix(Matrix4f tMatrix) {
        super.loadMatrix(location_transformationMatrix, tMatrix);
    }
    
}
