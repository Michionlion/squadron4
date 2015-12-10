package assets.game.particlesystem;

import assets.sprites.WorldObject;
import engine.Globals;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class Particle extends WorldObject {
    
    ParticleSystem parent;
    int born;
    int lifetime = 300; //Integer.MAX_VALUE if no limit

    public Particle(Texture tex, Vector2f pos, float rotation, Vector2f delta, Vector2f size, ParticleSystem parent) {
        super(tex, pos, rotation, delta, size, 1);
        this.parent = parent;
        born = parent.frames;
    }

    @Override
    public boolean interpolate() {
        return false;
    }
    
    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
        if(parent.frames-born > lifetime || Globals.camera.distanceSq(getX(), getY()) > 5_000_000) { // 5_000_000 = about 2236*2236
            Globals.remove(this);
        }
    }
}
