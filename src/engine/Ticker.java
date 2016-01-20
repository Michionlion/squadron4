package engine;

import engine.interfaces.Tickable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.lwjgl.input.Keyboard;

public class Ticker implements Runnable {

//    public float MILLIS_PER_FRAME;
    
    ScheduledExecutorService scheduler;
    
    public double lastTickTime = Globals.getTime();
    private double lastFPS = Globals.getTime();
    private boolean running;
    private int tps;
    public final int targetTPS;
    private int ticks = 0;
    private long totalTicks = 0;

    private volatile CopyOnWriteArrayList<Tickable> entities = new CopyOnWriteArrayList<>();

    
    public Ticker(int tps) {
//        MILLIS_PER_FRAME = 1_000 / tps;
        targetTPS = tps;
        
        this.tps = -1;
        
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void run() {
        running = true;
        lastTickTime = Globals.getTime();
        lastFPS = Globals.getTime();
        //TimeUnit.SECONDS.toNanos(1)/60, TimeUnit.NANOSECONDS
        scheduler.scheduleAtFixedRate(new Tick(), TimeUnit.MILLISECONDS.toNanos(30), TimeUnit.SECONDS.toNanos(1)/targetTPS, TimeUnit.NANOSECONDS);
    }

    public void logic() {
        double time = Globals.getTime();
//        System.out.println("MOUSE: Vec2[" + Mouse.getX() + ", " + (Globals.HEIGHT-Mouse.getY()) + "]");
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            Globals.camera.x += 5f;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            Globals.camera.x -= 5f;
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            Globals.camera.y -= 5f;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            Globals.camera.y += 5f;
        }
        
//        System.out.println(Globals.camera.x + ", " + Globals.camera.y);
        System.out.println("MOUSE: " + Globals.mousePos());
        if (!entities.isEmpty()) {
            for (Tickable e : entities) {
                e.tick((float) (time-lastTickTime)); // pass in deltaTime
            }
        }
        totalTicks++;
    }

    public void end() {
        running = false;
        scheduler.shutdown();
    }

    public void add(Tickable e) {
        entities.add(e);
    }

    public void remove(Tickable e) {
        if (entities.contains(e)) {
            entities.remove(e);
        }

    }

    public boolean isTicking(Tickable e) {
        return entities.contains(e);
    }

    public int getTPS() {
        return tps;
    }
    
    
    private class Tick implements Runnable {

        @Override
        public void run() {
            if(!running) return;
            logic();
            lastTickTime = Globals.getTime();
            if (Globals.getTime() - lastFPS > 1000) {
                tps = ticks;
                ticks = 0;
                lastFPS += 1000;
            }
            ticks++;
        }
        
    }
}
