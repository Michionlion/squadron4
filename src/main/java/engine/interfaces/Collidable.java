package engine.interfaces;

import java.awt.Rectangle;
import java.awt.geom.Area;

public interface Collidable {

    public Area getCollision();

    public Rectangle getBoxCollision();

    public boolean isColliding(Collidable c);

    public void addListener(InputListener i);

    public void removeListener(InputListener i);
}
