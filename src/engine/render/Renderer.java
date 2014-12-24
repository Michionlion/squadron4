package engine.render;

import assets.models.RawModel;
import assets.models.TexturedModel;
import assets.shaders.ShaderProgram;
import assets.shaders.StaticShader;
import engine.util.Utils;
import entities.Entity;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;


public class Renderer {
    
    private static final float FOV = 85;
    private static final float NEAR = 0.001f;
    private static final float FAR = 6000;
    
    
    
    
    private Matrix4f projectionMatrix;
    
    public Renderer(StaticShader shader) {
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    
    
    public void prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(1,0,0,1);
    }
    
    public void render(Entity entity, StaticShader shader) {
        
        
        TexturedModel tMod = entity.getModel();
        RawModel mod = tMod.getRawModel();
        
        Matrix4f tMatrix = Utils.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.loadTransformationMatrix(tMatrix);
        
        GL30.glBindVertexArray(mod.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tMod.getTexture().getID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, mod.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }
    
    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth()/(float) Display.getHeight();
        float yScale = (float) ((1f/Math.tan(Math.toRadians(FOV/2f)))*aspectRatio);
        float xScale = yScale/aspectRatio;
        float frustumLength = FAR - NEAR;
        
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR+NEAR)/frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2*FAR*NEAR)/frustumLength);
        projectionMatrix.m33 = 0;
    }
}
