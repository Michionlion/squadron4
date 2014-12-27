package assets.sprites;

import assets.textures.Texture2D;
import engine.interfaces.Interpolatable;
import engine.interfaces.Tickable;
import org.lwjgl.util.vector.Vector2f;

public class MovingSprite extends Sprite implements Interpolatable, Tickable {

    Vector2f delta;
    
    public MovingSprite(Texture2D tex, Vector2f pos, float rotation, Vector2f delta, Vector2f size, float priority) {
        super(tex, pos,size.x, size.y, rotation, priority);
        this.delta = delta;
    }

    @Override
    public void tick() {
        
        super.translate(delta.x, delta.y);
    }
    
    @Override
    public float getDeltaX() {
        return delta.x;
    }

    @Override
    public float getDeltaY() {
        return delta.y;
    }

    
}
