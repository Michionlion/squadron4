package assets.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class SpriteShader extends ShaderProgram {
    
    private static final String VERTEX_FILE = "src/assets/shaders/sprite.vert";
    private static final String FRAGMENT_FILE = "src/assets/shaders/sprite.frag";
    
    private int location_transformationMatrix;
    private int location_projectionMatrix;

    public SpriteShader(String vertexFile, String fragmentFile) {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }
    
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }
}