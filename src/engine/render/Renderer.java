package engine.render;

import assets.models.RawModel;
import assets.models.TexturedModel;
import assets.shaders.ShaderProgram;
import assets.shaders.SpriteShader;
import assets.shaders.StaticShader;
import assets.sprites.Sprite;
import engine.Globals;
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
    
    
    
    public static RawModel QUAD;
    
    public Renderer() {
        float[] verts = 
        {0.5f,0.5f,0f,  //v1
         0.5f,-0.5f,0f,  //v2
         -0.5f,-0.5f,0f,  //v3
         -0.5f,0.5f,0f};  //v4
        
        float[] texs = {0f,0f, 0f,1f, 1f,1f, 1f,0f};
        
        int[] indices = {0,1,3, 3,1,2};
        
        QUAD = Loader.loadToVAO(verts, texs, indices);
        
        
        
        
        
        
        
    }
    
    
    
    
    public void prepare() {
//        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
//        GL11.glClearColor(1,0,0,1);
    }
    
    public void render(Sprite sprite, SpriteShader shader) {
        shader.start();
        Matrix4f tMatrix = Utils.createSpriteTransformationMatrix(sprite.getX(), sprite.getY(), sprite.getRotation(), sprite.getWidth(), sprite.getHeight(), sprite.getPriority());
        shader.loadTransformationMatrix(tMatrix);
        
        
        
        GL30.glBindVertexArray(QUAD.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getTex().getID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, QUAD.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        
        shader.stop();
    }
}
