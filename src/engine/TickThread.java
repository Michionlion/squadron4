package engine;

import engine.interfaces.Tickable;
import java.awt.event.KeyEvent;
import java.util.concurrent.CopyOnWriteArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class TickThread extends Thread {

    public float MILLIS_PER_FRAME;
    public double lastTickTime = Globals.getTime();
    private double lastFPS = Globals.getTime();
    private boolean running;
    private int tps;
    private int ticks = 0;
    private long totalTicks = 0;

    private volatile CopyOnWriteArrayList<Tickable> entities = new CopyOnWriteArrayList<>();

    
    public TickThread(int tps) {
        MILLIS_PER_FRAME = 1_000 / tps;
        this.tps = tps;
    }

    @Override
    public void run() {
        running = true;
        lastTickTime = Globals.getTime();
        lastFPS = Globals.getTime();
        while (running) {
            
            logic();
            lastTickTime = Globals.getTime();
            
            
            if (Globals.getTime() - lastTickTime > 1000) {
                Display.setTitle("Squadron 4   -   " + ticks);
                tps = ticks;
                ticks = 0;
                lastTickTime += 1000;
            }
            ticks++;
            
        }
    }

    public void logic() {
        long time = System.currentTimeMillis();
        if (!entities.isEmpty()) {
            for (Tickable e : entities) {
                e.tick();
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            esced();
        }
        totalTicks++;

    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public static void sleep(int millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
        }
    }

    public void end() {
        running = false;
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

    public int getFPS() {
        return tps;
    }

    private synchronized void esced() {
        notifyAll();
    }
}
