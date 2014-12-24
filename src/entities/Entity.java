package entities;

import assets.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
    
    private TexturedModel model;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void move(Vector3f toMove) {
        Vector3f.add(position, toMove, position);
    }
    
    public void rotate(Vector3f toRot) {
        Vector3f.add(rotation, toRot, rotation);
    }
    
    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
    
    
    
    
    
    
}
