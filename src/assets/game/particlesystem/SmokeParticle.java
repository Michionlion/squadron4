package assets.game.particlesystem;

import assets.Loader;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class SmokeParticle extends Particle {
    public static final Texture SMOKE_TEX = Loader.getTexture("smoke");
    
    

    public SmokeParticle(Vector2f pos, Vector2f delta, ParticleSystem parent) {
        super(SMOKE_TEX, pos, (float) Math.random()*360, delta, new Vector2f(32,32), parent);
    }
}
