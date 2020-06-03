package assets.sprites;

import engine.Globals;
import engine.interfaces.RenderObject;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public abstract class Sprite implements RenderObject {

    private Texture tex;
    protected Vector2f pos;
    public float rotation,priority;
    protected float width,height;

    public Sprite(Texture tex, float x, float y, float rotation, float priority) {
        this(tex, new Vector2f(x,y), 50f, 50f, rotation);
        this.priority = priority;
    }
    public Sprite(Texture tex, Vector2f position, float width, float height, float rotation) {
        this(tex, position, width, height, rotation, 1);
    }
    public Sprite(Texture tex, Vector2f position, float width, float height, float rotation, float priority) {
        this.tex = tex;
        pos = position;
        setRotation(rotation);
        this.priority = priority;

        this.width = width;
        this.height = height;
    }


    public void rotate(float toRot) {
        rotation+=toRot;
        checkRotation();
    }

    public void translate(float x, float y) {
        pos.translate(x, y);
    }

    public void translate(Vector2f t) {
        Vector2f.add(pos, t, pos);
    }

    @Override
    public Texture getTex() {
        return tex;
    }

    protected void setTex(Texture t) {
        if(t.equals(tex)) {
            return; // don't need to change anything, same image
        } else if(t.getImageHeight()==tex.getImageHeight() && t.getImageWidth()==tex.getImageWidth()) {
            tex = t;
        } else {
            throw new IllegalArgumentException("Texture passed is not the same size!");
        }
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

    public final void setPos(Vector2f pos) {
        this.pos = pos;
    }

    public float getRotation() {
        return rotation;
    }

    public final void setRotation(float rotation) {
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

    @Override
    public float getX() {
        return pos.x;
    }

    @Override
    public float getY() {
        return pos.y;
    }

    public float getPriority() {
        return priority;
    }

    public final void setPriority(float priority) {
        this.priority = priority;
    }

    @Override
    public boolean isVisible() {

        boolean i = Globals.camera.contains(pos.x, pos.y);
        boolean f = Globals.camera.contains(pos.x+width, pos.y+height);
        boolean e = Globals.camera.contains(pos.x-width, pos.y-height);
        return i||f||e;
    }

    public float getRenderX() {
        return getX() - Globals.camera.x;
    }

    public float getRenderY() {
        return getY() - Globals.camera.y;
    }
}
