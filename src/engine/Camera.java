package engine;

import engine.interfaces.Tickable;
import java.awt.geom.Rectangle2D;
import org.lwjgl.util.vector.Vector2f;

public class Camera extends Rectangle2D.Float implements Tickable {

    private Vector2f target;
    private float moveFactor = 0.95f; // between 0.95 and 0.99
    private Vector2f lastPos; // last frames position
    private Vector2f delta;
    
    public Camera(float xx, float yy, float WIDTH, float HEIGHT) {
        super(xx, yy, WIDTH, HEIGHT);
        target = null;
        lastPos = new Vector2f(xx, yy);
        delta = new Vector2f(0, 0);
    }
    
    @Override
    public void tick(float deltaTime) {
        if(target!=null) {
            double factor = Math.pow(moveFactor, deltaTime);
            centerOn((float) (factor * x + (1 - factor) * target.x), (float) (factor * y + (1 - factor) * target.y));
        }
        System.out.println(delta);
    }
    
    public float distanceSq(float x, float y) { // dis from center of screen
        return ((this.x+width/2) - x) * ((this.x+width/2) - x) + ((this.y+height/2) - y) * ((this.y+height/2) - y);
    }
    
    public void centerOn(float x, float y) {
        setLocation(x-width/2, y-height/2);
    }
    
    public void setLocation(float x, float y) {
        lastPos.set(this.x, this.y);
        delta.set(x-lastPos.x, y-lastPos.y);
        this.x = x;
        this.y = y;
        
    }
    
    public void setTarget(float x, float y) {
        if(target==null) target = new Vector2f();
        target.set(x, y);
    }

        
}
