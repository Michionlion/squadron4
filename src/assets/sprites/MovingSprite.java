package assets.sprites;

import engine.interfaces.Tickable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class MovingSprite extends Sprite implements Tickable {

    protected Vector2f delta;

    public MovingSprite(Texture tex, Vector2f pos, float rotation, Vector2f delta, Vector2f size, float priority) {
        super(tex, pos, size.x, size.y, rotation, priority);
        this.delta = delta;
    }

    @Override
    public void tick() {

//        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
//            delta.x -= 1f;
//            log("d");
//        } else if (delta.x + 0.00001f >= 0) {
//            delta.x = 0;
//            log("d1");
//        } else {
//            delta.x += 0.00001f;
//            log("d2");
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
//            delta.x += 1f;
//            log("a");
//        } else if (delta.x - 0.00001f <= 0) {
//            delta.x = 0;
//            log("a1");
//        } else {
//            delta.x -= 0.00001f;
//            log("a2");
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
//            delta.y += 1f;
//            log("w");
//        } else if (delta.y - 0.00001f <= 0) {
//            delta.y = 0;
//            log("w1");
//        } else {
//            delta.y -= 0.00001f;
//            log("w2");
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
//            log("s");
//            delta.y -= 1f;
//        } else if (delta.y + 0.00001f >= 0) {
//            delta.y = 0;
//            log("s1");
//        } else {
//            delta.y += 0.00001f;
//            log("s2");
//        }
//
//        System.out.println("translating: " + delta);
//        Vector2f dVec = new Vector2f((float)(Math.cos(Math.toRadians(rotation)) * 0.4f), (float)(Math.sin(Math.toRadians(rotation)) * 0.4f));
//        super.translate(dVec);
        super.translate(delta.x, delta.y);
    }
    
    private void log(Object s) {
        System.out.println(s);
    }

    public float getDeltaX() {
        return delta.x;
    }

    public float getDeltaY() {
        return delta.y;
    }

    public void setDelta(Vector2f delta) {
        this.delta = delta;
    }

    public void setDeltaX(float dx) {
        delta.x = dx;
    }

    public void setDeltaY(float dy) {
        delta.y = dy;
    }
}
