package assets.game.objects;

import assets.Loader;
import engine.GameObject;
import engine.Globals;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;



public class Projectile extends GameObject {
    
    public static final float MISSILE_ACCEL = 0.8f; // missile accel in pix per frame

    public static enum ProjectileType {
        
        // projectile defs
        LASER(Loader.getTexture("laser"), 12.54f, 20, 1),
        MISSILE(Loader.getTexture("missile"), 185.67f, 1, 2);
        
        Texture tex;
        float damage;
        float speed;
        int id;
        
        ProjectileType(Texture tex, float damage, float speed, int id) {
            this.tex = tex;
            this.damage = damage;
            this.speed = speed;
            this.id = id;
        }
        
        public Texture getTex() {
            return tex;
        }
        
        public float getDamage() {
            return damage;
        }
        
        public float getSpeed() {
            return speed;
        }
        
        public int getID() {
            return id;
        }
        
    }
    
    public static final ProjectileType convertID(int id) {
        switch(id) {
            case 1:
                return ProjectileType.LASER;
            case 2:
                return ProjectileType.MISSILE;
            
        }
        System.out.println("not 1 or 2, defaulting to laser");
        return ProjectileType.LASER;
    }
    
    
    
    
    //public static final int WIDTH = 20;
    //public static final int HEIGHT = 20;
    
    protected ProjectileType type;
    
    private float speed;
    private int frames;
    
    
    //main constructer
    public Projectile(Vector2f pos, Vector2f delta, float angle, float speed, ProjectileType type) {
        super(type.getTex(), pos, angle, delta, new Vector2f(32,32));
        
        this.type = type;
        
        this.speed = speed;
    }
    
    public Projectile(Vector2f pos, float angle, ProjectileType type) {
        this(pos,new Vector2f(0,0),angle,type.getSpeed(),type);
    }
    
    public Projectile(Vector2f pos, Vector2f delta, float angle, ProjectileType type) {
        this(pos,delta,angle,type.getSpeed(),type);
    }
    
    @Override
    public void tick() {
        Vector2f.add(pos, delta, pos); // <- do residual velocity
        move(rotation, speed); // <-do self movement
        
        if(type==ProjectileType.MISSILE) { // do missile things
            speed += MISSILE_ACCEL;
        }
        
        
        if (frames > 170 && !isVisible()) Globals.remove(this);
        frames++;
    }
    public ProjectileType getType() {
        return type;
    }
    
    public double getDamage() {
        return type.getDamage();
    }
    
    @Override
    public String toString() {
        return "Projectile at: " + pos.x + ", " + pos.y + ", of type: " + type.name() + ", which has lasted for " + frames + " frames!" + "  is it visible (super): " + super.isVisible();
    }
    
    @Override
    public boolean interpolate() {
        return true;
    }
}