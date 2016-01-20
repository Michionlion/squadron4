package assets.game.particlesystem;

import assets.Loader;
import java.io.File;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class SmokeParticle extends Particle {
    public static Texture[] SMOKE = new Texture[10];
    public int animIndex = -1;
    
    public static void init() {
        // go from 0 to 9
        for(int i=0; i < SMOKE.length; i++) {
            SMOKE[i] = Loader.getTexture("smoke" + File.separator + (i+1)); // + i when smoke texs implemented
        }
    }
    

    public SmokeParticle(Vector2f pos, float rotation, Vector2f delta, ParticleSystem parent) {
        super(SMOKE[0], pos, rotation, delta, new Vector2f(8,8), parent);
        super.lifetime = 300;
    }
    
    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
        if(born == 6) System.out.println(animIndex);
        if(parent.frames-born % 10==0) animIndex++;
    }
}
