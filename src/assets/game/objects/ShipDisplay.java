package assets.game.objects;

import engine.interfaces.Interpolatable;
import org.lwjgl.util.vector.Vector2f;

public class ShipDisplay extends Ship implements Interpolatable {

    public ShipDisplay(Vector2f pos, float rotation, Vector2f delta, String name) {
        super(pos, rotation, delta, name);

    }

    @Override
    public boolean interpolate() {
        return true;
    }


}
