package assets.game.objects;

import assets.Loader;
import assets.ShipManager;
import assets.game.objects.Projectile.ProjectileType;
import engine.Globals;
import engine.util.Util;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

public final class PlayerShip extends Ship {
    
    public static int TURN_MBUTTON = 1;
    public static int ACCEL_MBUTTON = 0;

    private float THRUST = 0.18f;
    private float TURN_RATE = 2.425f;

    public PlayerShip(float x, float y, float rot) {
        super(new Vector2f(x, y), rot, new Vector2f(0, 0), Globals.userName());
        delta = new Vector2f(0, 0);
        ammoType = ProjectileType.LASER;
    }

    @Override
    public void tick() {

        if (energy <= ENERGY_AMOUNT) {
            energy += ENERGY_RECHARGE;
        } else if (energy >= ENERGY_AMOUNT) {
            energy = ENERGY_AMOUNT;
        }

        double desiredRot = Math.toDegrees(Math.atan2(Mouse.getX() - (pos.x - Globals.viewArea.getX()), (Globals.HEIGHT-Mouse.getY()) - (pos.y - Globals.viewArea.getY())));
        if (Math.abs(desiredRot - rotation) > TURN_RATE / 2f) {
            
            double currentRot = rotation;
            
            if (desiredRot < 0) {
                desiredRot += 360;
            }

            if (Mouse.isButtonDown(TURN_MBUTTON)) {
                boolean clockwise = true;
                if (currentRot < desiredRot && desiredRot - currentRot > 180) {
                    clockwise = false;
                }
                if (currentRot > desiredRot && currentRot - desiredRot <= 180) {
                    clockwise = false;
                }

                if (clockwise) {
                    rotate(TURN_RATE);
                } else {
                    rotate(-TURN_RATE);
                }

            }
        }
        if (Mouse.isButtonDown(ACCEL_MBUTTON) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
            if (useEnergy(0.23f)) {
                setAccelerating(true);
            }
        } else {
            setAccelerating(false);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && isAccelerating()) {
            if (useEnergy(0.13f)) {
                THRUST *= THRUST_BOOST_MOD;
            } else {
                //play sound - energy fail
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && isAccelerating()) {
            if (useEnergy(0.14f)) {
                THRUST *= THRUST_SLOW_MOD;
            } else {
                //play sound - energy fail
            }
        }

        calculateMovement();

        calculateShieldRecharge();

        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            energy += 25;
            missileMag = 50;
        }

        //weapons
        if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
            ammoType = ProjectileType.LASER;
            turbo = false;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
            ammoType = ProjectileType.MISSILE;
            turbo = false;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
            //turbo lasers or missiles
            turbo = true;
            laserCount = TURBO_LASER_DELAY;
            missileCount = TURBO_MISSILE_DELAY;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            firing = true;
            if (ammoType == ProjectileType.LASER && laserCount <= 0 && turbo) {
                if (useEnergy(9f)) {
                    laserCount = TURBO_LASER_DELAY;
                    fire(ProjectileType.LASER);
                } else {
                    //play sound - energy fail
                }
            } else if (ammoType == ProjectileType.LASER && laserCount <= 0) {
                if (useEnergy(6.5f)) {
                    laserCount = LASER_DELAY;
                    fire(ProjectileType.LASER);
                } else {
                    //play sound - energy fail
                }
            } else if (ammoType == ProjectileType.MISSILE && missileCount <= 0 && missileMag > 0) {
                if(turbo) {
                    if(useEnergy(20f)) {
                        missileCount = TURBO_MISSILE_DELAY;
                        fire(ProjectileType.MISSILE);
                    }
                } else if (useEnergy(0.16f)) {
                    missileCount = MISSILE_DELAY;
                    fire(ProjectileType.MISSILE);
                } else {
                    //play sound - energy fail
                }
            }

        } else {
            firing = false;
        }

        if (armor <= 0 && !dead) {
            die();
        }

        laserCount--;
        missileCount--;
        shieldCount--;
        frames++;

        System.out.println("POSITION: " + pos + ", ROTATION: " + rotation);

//        System.out.println(Game.isFastTicking(this));
//        if(isAccelerating) Game.add(new EngineParticle(x - Math.cos(Math.toRadians(rotation)) * 6.5, y - Math.sin(Math.toRadians(rotation)) * 6.5, -Math.cos(Math.toRadians(rotation))*1.5, -Math.sin(Math.toRadians(rotation))*1.5, new Color(200, 200, 200, 145)));
//        else Game.add(new EngineParticle(x - Math.cos(Math.toRadians(rotation)) * 12, y - Math.sin(Math.toRadians(rotation)) * 12, Color.DARK_GRAY));
        if (frames % 3 == 0) {
            if (Globals.isMulti()) {
                Globals.CLIENT.sendAccel(isAccelerating());
            }
        }
    }

    private void calculateWarpMovement() {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            if (useEnergy(0.30f)) {
                speedLimit = WARP_SPEED_LIMIT;
                slowDown = false;
            } else if (speedLimit == WARP_SPEED_LIMIT) {
                speedLimit = SPEED_LIMIT;
                slowDown = true;
            }
        } else if (speedLimit == WARP_SPEED_LIMIT) {
            slowDown = true;
            speedLimit = SPEED_LIMIT;
        }
    }

    private void calculateMovement() {

        float deltaLength = (float) Math.sqrt(delta.lengthSquared());

        if (isAccelerating()) {
            double r = Math.toRadians(rotation);
            Vector2f accel;
            accel = new Vector2f((float) (Math.sin(r) * THRUST), (float) (Math.cos(r) * THRUST));
            Vector2f.add(delta, accel, delta);
        }
        //calculate if warping or slowing down
        calculateWarpMovement();

        if (slowDown && deltaLength >= speedLimit) {
//            Util.log("slowing speed!");
            delta.scale(0.965f);
        } else {
            slowDown = false;
        }

        //max speed clipping
        if (SPEED_LIMIT_ON && deltaLength > speedLimit && !slowDown) {
//            System.out.println("cutting speed!");
            Util.setMagnitudeOfVector2f(delta, speedLimit);
        }

        if (movementInhibitor && !isAccelerating() && !slowDown && useEnergy(0.08f) && !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && deltaLength >= 0.016f) {
            if (deltaLength >= 1) {
                delta.scale(0.965f);
            } else if (deltaLength <= 0.2) {
                delta.scale(0.785f);
            } else {
                delta.scale(0.835f);
            }
//            Util.log("slowing");
            if (deltaLength < 0.016d) {
                delta.scale(0);
            }
        }

        //actually do movement and -center-
        Vector2f.add(pos, delta, pos);
        if (Globals.isMulti()) {
            Globals.CLIENT.sendPos(getX(), getY(), getRotation());
        }
    }

    @Override
    protected void die() {
        dead = true;
        System.out.println("dead");

        if (Globals.isMulti()) {
            Globals.CLIENT.sendMSG("/dead", "");
        }
        ShipManager.respawnPlayerShip();
    }

    @Override
    public boolean interpolate() {
        return true;
    }
}
