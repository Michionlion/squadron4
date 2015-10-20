package assets.game.particlesystem;

import assets.Loader;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class SmokeParticle extends Particle {
    public static Texture SMOKE_TEX = null;
    
    public static void init() {
        SMOKE_TEX = Loader.getTexture("smoke");
    }
    

    public SmokeParticle(Vector2f pos, float rotation, Vector2f delta, ParticleSystem parent) {
        super(SMOKE_TEX, pos, rotation, delta, new Vector2f(8,8), parent);
    }
}
