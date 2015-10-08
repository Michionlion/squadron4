package assets.game.particlesystem;

import assets.Loader;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class SmokeParticle extends Particle {
    public static final Texture SMOKE_TEX = Loader.getTexture("missile");

    public SmokeParticle(Vector2f pos, float rotation, Vector2f delta, ParticleSystem parent) {
        super(SMOKE_TEX, pos, rotation, delta, new Vector2f(16,16), parent);
    }
    
}
