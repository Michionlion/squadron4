package assets.shaders;

public class StaticShader extends ShaderProgram {
    
    private static final String VERTEX_FILE = "src/assets/shaders/vertexShader.vert";
    private static final String FRAGMENT_FILE = "src/assets/shaders/fragmentShader.frag";
    
    private int location_transformationMatrix;
    
    public StaticShader() {
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
    
    
}
