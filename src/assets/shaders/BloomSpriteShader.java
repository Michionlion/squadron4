package assets.shaders;

import engine.Globals;
import engine.interfaces.Shader;
import engine.interfaces.SpriteShader;
import engine.util.Util;
import org.lwjgl.util.vector.Matrix4f;

public class BloomSpriteShader extends ShaderProgram implements SpriteShader {
    
    private static final String VERTEX_FILE = "src/assets/shaders/sprite.vert";
    private static final String FRAGMENT_FILE = "src/assets/shaders/sprite_blurback.frag";
    
    private int location_transformationMatrix;
    private int location_windowWidth;
    private int location_windowHeight;

    public BloomSpriteShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        start();
        loadInt(location_windowWidth, Globals.WIDTH);
        loadInt(location_windowHeight, Globals.HEIGHT);
        loadMatrix(super.getUniformLocation("projectionMatrix"), Util.createOrthoMatrix());
        stop();
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_windowWidth = super.getUniformLocation("windowWidth");
        location_windowHeight = super.getUniformLocation("windowHeight");
    }
    
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }
}