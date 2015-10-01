package assets.game.objects;

import assets.Loader;
import assets.game.objects.Projectile.ProjectileType;
import engine.GameObject;
import engine.Globals;
import engine.util.*;
import org.lwjgl.util.vector.Vector2f;

public abstract class Ship extends GameObject {
    
    public static final boolean SPEED_LIMIT_ON = true;
    public static final float SPEED_LIMIT = 3.85f;
    public static final float WARP_SPEED_LIMIT = 132.5f;
    public static final float THRUST_TURN_MOD = 0.95f;
    public static final float THRUST_SLOW_MOD = 0.12f;
    public static final float THRUST_BOOST_MOD = 9;
    public static final int ENERGY_AMOUNT = 256;
    public static final float ENERGY_RECHARGE = ENERGY_AMOUNT / 500f;
    public static final int ARMOR_AMOUNT = 35;
    public static final int SHIELD_AMOUNT = 50;
    public static final int MISSILE_AMOUNT = 8;
    public static final int MISSILE_DELAY = 88;
    public static final int LASER_DELAY = 7;
    public static final int TURBO_LASER_DELAY = 1;
    public static final int SHIELD_RECHARGE_DELAY = 35;
    public static final int SHIELD_OVERCHARGE_DELAY = 80;
    
    
    protected float speedLimit = SPEED_LIMIT;
    protected boolean accelerating = false;
    protected boolean dead = false;
    protected ProjectileType ammoType;
    protected boolean firing;
    protected boolean slowDown = false;
    protected float energy = ENERGY_AMOUNT;
    protected float armor = ARMOR_AMOUNT;
    protected float shields = SHIELD_AMOUNT;
    protected int missileMag = MISSILE_AMOUNT;
    protected int laserCount = LASER_DELAY;
    protected boolean turbo = false;
    protected boolean movementInhibitor = false;
    protected int shieldCount = 0;
    protected int missileCount = 0;
    protected int frames;
    protected String name;

    public Ship(Vector2f pos, float rotation, Vector2f delta, String name) {
        super(Loader.getTexture("spaceship-off2"), pos, rotation, delta, new Vector2f(55,55));
        this.name = name;
    }
    
    @Override
    public void tick() {
        
        move(delta);
        frames++;
    }
    
    public void calculateShieldRecharge() {
        if (shieldCount <= 0) {
                if (shields < SHIELD_AMOUNT) {
                    if (useEnergy(0.08f)) {
                        shields += 0.3;
                    }
                    if (shieldCount < -SHIELD_OVERCHARGE_DELAY) {
                        if (useEnergy(0.06f)) {
                            shields += 0.3;
                        }
                    }
                    if (shieldCount < -SHIELD_OVERCHARGE_DELAY * 2) {
                        if (useEnergy(0.04f)) {
                            shields += 0.3;
                        }
                    }
                    if (shieldCount < -SHIELD_OVERCHARGE_DELAY * 3) {
                        if (useEnergy(0.03f)) {
                            shields += 0.4;
                        }
                    }
                    if (shieldCount < -SHIELD_OVERCHARGE_DELAY * 4) {
                        if (useEnergy(0.02f)) {
                            shields += 0.5;
                        }
                    }
                    if (shieldCount < -SHIELD_OVERCHARGE_DELAY * 5) {
                        if (useEnergy(0.01f)) {
                            shields += 0.6;
                        }
                    }
                } else {
                    shields = SHIELD_AMOUNT;
                }
                if(Globals.isMulti()) Globals.CLIENT.sendVitals(armor, shields);
            }
    }
    
    protected boolean useEnergy(float amount) {
        if (energy >= amount) {
            energy -= amount;
            return true;
        } else {
            return false;
        }
    }
    
    public void fire(ProjectileType type) {
        float spawnX;
        float spawnY;
        float spawnRot;
        float radians = (float) Math.toRadians(rotation);
        if (type == ProjectileType.LASER) {
            //play sound - laser1
            spawnRot = (float) (rotation + (Math.random() * 2 - 1));
            spawnX = (float) (pos.x + Math.sin(radians) * 19);
            spawnY = (float) (pos.y + Math.cos(radians) * 19);
        } else if (type == ProjectileType.MISSILE) {
            //play sound - missile1
            missileMag--;
            spawnRot = rotation;
            spawnX = (float) (pos.x + Math.sin(radians) * 12);
            spawnY = (float) (pos.y + Math.cos(radians) * 12);
        } else {
            spawnRot = rotation;
            spawnX = (float) (pos.x + Math.sin(radians) * 12);
            spawnY = (float) (pos.y + Math.cos(radians) * 12);
        }

        //log("creating projectile at: " + x + ", " + y + "  rotation: " + rotation + ", type: " + type);
        if (Globals.isMulti()) {
            Globals.CLIENT.sendProj(spawnX, spawnY, delta.x, delta.y, spawnRot, type.getID());
        } else {
            Globals.add(new Projectile(new Vector2f(spawnX,spawnY), Util.copy(delta), spawnRot, type));
        }
    }
    
    public void doDamage(float damage) {
        shieldCount = SHIELD_RECHARGE_DELAY;

        if (shields <= 0) {
            // play sound - armorhit1
            armor -= damage;
        } else {
            // play sound - shieldhit0
            shields -= damage;
        }

        if (shields < 0) {
            // play sound - armorhit0
            armor -= Math.abs(shields);
            shields = 0;
        }
        if (armor <= 0) {
            armor = 0;
            if (!dead) {
                die();
            }
        }

        if(Globals.isMulti()) Globals.CLIENT.sendVitals(armor, shields);
    }
    
    public void updateInfo(Vector2f pos, Vector2f delta, float rot) {
        this.pos = pos;
        this.delta = delta;
        setRotation(rot);
    }
    
    public void setStats(float armor, float shields) {
        this.armor = armor;
        this.shields = shields;
    }
    
    public double getArmor() {
        return armor;
    }
    
    public double getShields() {
        return shields;
    }
    
    public void setAccel(boolean accel) {
        accelerating = accel;
    }
    
    public boolean getAccel() {
        return accelerating;
    }
    
    public String getName() {
        return name;
    }
    
    protected void die() {
        dead = true;
        System.out.println("dead: " + name);
    }
}