package engine.render;

import assets.models.RawModel;
import assets.textures.Texture2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Loader {
    
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private HashMap<String, Integer> textures = new HashMap<>();
    
    public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices, boolean is2D) {
        int posSize = 3;
        if(is2D) posSize = 2;
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, posSize, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }
    
    public Texture2D getTexture(String fileName) {
        return new Texture2D(getTextureID(fileName));
    }
    
    public int getTextureID(String fileName) {
        if(textures.containsKey(fileName)) return textures.get(fileName);
        else return loadTexture(fileName);
    }
    
    private int loadTexture(String fileName) {
        Texture tex = null;
        
        try {
            tex = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(tex == null) {
            System.err.println("unable to load texture: " + fileName);
            return -1;
        }
        int textureID = tex.getTextureID();
        textures.put(fileName, textureID);
        return textureID;
    }
    
    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }
    
    private void storeDataInAttributeList(int attNum, int dataSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attNum, dataSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }
    
    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    
    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    
    public void cleanUp() {
        for(int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        
        for(int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        
        for(int tex : textures) {
            GL11.glDeleteTextures(tex);
        }
    }
}
