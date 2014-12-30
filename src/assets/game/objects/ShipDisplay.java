package assets.game.objects;

import assets.Loader;
import org.lwjgl.util.vector.Vector2f;

public class ShipDisplay extends Ship {

    public ShipDisplay(Vector2f pos, float rotation, Vector2f delta, String name) {
        super(Loader.getTexture("spaceship-off"), pos, rotation, delta, new Vector2f(32f,32f), name);
        
    }
    
    
}
