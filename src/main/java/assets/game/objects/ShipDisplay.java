package assets.game.objects;

import org.lwjgl.util.vector.Vector2f;

import engine.interfaces.Interpolatable;

public class ShipDisplay extends Ship implements Interpolatable {

    public ShipDisplay(Vector2f pos, float rotation, Vector2f delta, String name) {
        super(pos, rotation, delta, name);
    }

    @Override
    public boolean interpolate() {
        return true;
    }
}
