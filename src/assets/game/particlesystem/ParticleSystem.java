package assets.game.particlesystem;

import assets.Loader;
import engine.GameObject;
import engine.Globals;
import engine.util.Util;
import org.lwjgl.util.vector.Vector2f;

public class ParticleSystem extends GameObject {
    
    //basic system sends out in all directions at set speed
    
    protected float emitSpeed;
    protected int emitDelay,emitAmount;
    
    protected int delay = 0;
    protected int frames = Integer.MAX_VALUE- 60;
    

    public ParticleSystem(float x, float y, float rotation, Vector2f delta, float emitSpeed, int emitDelay, int emitAmount) {
        super(Loader.getTexture("close"), new Vector2f(x,y), rotation, delta, new Vector2f(16,16));
        SmokeParticle.init();
        this.emitSpeed = emitSpeed;
        this.emitDelay = emitDelay;
        this.emitAmount = emitAmount;
    }
    
    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
        
        if(delay <= 0)  {
            emit(emitAmount);
            delay = emitDelay;
        }
        
        
        delay--;
        frames++;
    }
    
    //generic randomized smoke system emit
    public void emit(int amt) {
        float x = pos.x, y=pos.y;
        float rot;
        Vector2f d = new Vector2f(0,0);
        
        for(int i = 0; i < emitAmount; i++) {
            rot = (float) (Math.random()*360);
            d.set((float) Math.cos(Math.toRadians(rot)*emitSpeed), (float) Math.sin(Math.toRadians(rot)*emitSpeed));
            Globals.add(new SmokeParticle(Util.copy(pos), 0, Vector2f.add(delta, d, d), this));
            System.out.println("PARTICLE CREATED: " + pos + ", " + rot + ", " + d);
        }
    }

    @Override
    public boolean interpolate() {
        return false;
    }
    
    
}
