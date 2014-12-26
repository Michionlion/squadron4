package assets.sprites;

import assets.textures.Texture2D;
import org.lwjgl.util.vector.Vector2f;

public class Sprite {
    
    private Texture2D tex;
    private Vector2f pos;
    private float rotation,priority;

    public Sprite(Texture2D tex, float x, float y, float rotation, float priority) {
        this.tex = tex;
        pos = new Vector2f(x,y);
        rotate(rotation);
        this.priority = priority;
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
