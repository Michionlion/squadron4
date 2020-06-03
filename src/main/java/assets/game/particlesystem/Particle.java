package assets.game.particlesystem;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import engine.Globals;

import assets.sprites.WorldObject;

public class Particle extends WorldObject {

    ParticleSystem parent;
    int born;
    int lifetime =
            300; // Integer.MAX_VALUE if no limit except visibility in frames (see note below as
    // well

    public Particle(
            Texture tex,
            Vector2f pos,
            float rotation,
            Vector2f delta,
            Vector2f size,
            ParticleSystem parent) {
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
        if (parent.frames - born
                > lifetime) { // add || !isVisible() if wish to remove if outside screen, but won't
            // persist until lifetime if you go back to same area
            Globals.remove(this);
        }
    }
}
