package assets.sprites;

import assets.models.RawModel;
import assets.textures.Texture2D;
import engine.Globals;
import engine.render.Loader;
import org.lwjgl.util.vector.Vector2f;

public class Sprite {
    
    private Texture2D tex;
    private Vector2f pos;
    public float rotation,priority;
    private float width,height;
    
    
    public static RawModel quad = null;
    
    
    
    

    public Sprite(Texture2D tex, float x, float y, float rotation, float priority) {
        this.tex = tex;
        pos = new Vector2f(x,y);
        rotate(rotation);
        this.priority = priority;
        
        width = 200;
        height = 200;
        if(quad == null) setUpQuad();
    }
    
    public static void setUpQuad() {
        float[] verts = 
        {0.5f,0.5f,0f,  //v1
         0.5f,-0.5f,0f,  //v2
         -0.5f,-0.5f,0f,  //v3
         -0.5f,0.5f,0f};  //v4
        
        float[] texs = {0f,0f, 0f,1f, 1f,1f, 1f,0f};
        
        int[] indices = {0,1,3, 3,1,2};
        
        quad = Loader.loadToVAO(verts, texs, indices);
    }
    
    
    
    
    public void rotate(float toRot) {
        rotation+=toRot;
        checkRotation();
    }
    
    public void translate(float x, float y) {
        pos.translate(x, y);
    }

    public Texture2D getTex() {
        return tex;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2f getPos() {
        return pos;
    }

    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        checkRotation();
    }
    
    public void checkRotation() {
        if(rotation >=360) {
            rotation-=360;
            checkRotation();
        }
        else if (rotation < 0) {
            rotation+=360;
            checkRotation();
        }
        
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public float getPriority() {
        return priority;
    }

    public void setPriority(float priority) {
        this.priority = priority;
    }
    
    
    
}
