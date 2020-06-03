package engine.interfaces;

import java.awt.Rectangle;
import java.awt.geom.Area;

import engine.Collider;

public interface Collidable {

    public Area getCollision();

    public Rectangle getBoxCollision();

    public boolean isColliding(Collider c);

    public void addListener(InputListener i);

    public void removeListener(InputListener i);
}
