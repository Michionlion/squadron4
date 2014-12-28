package engine;

import engine.interfaces.RenderObject;
import engine.interfaces.Tickable;
import engine.render.Renderer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class Globals {

    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    
    public static final int FPS_CAP = 60;

    public static Ticker TICKER = new Ticker(30);
    public static Renderer RENDERER = new Renderer();
    
    public static CopyOnWriteArrayList<GameObject> gameObjects;
    public static CopyOnWriteArrayList<RenderObject> renderObjects;
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void add(Object o) {
        if(o instanceof Tickable) TICKER.add((Tickable) o);
        if(o instanceof GameObject) gameObjects.add((GameObject) o);
        if(o instanceof RenderObject) renderObjects.add((RenderObject) o);
    }
    
    public static void remove(Object o) {
        if(o instanceof Tickable) if(TICKER.isTicking((Tickable) o)) TICKER.remove((Tickable) o);
        if(o instanceof GameObject) if(gameObjects.contains((GameObject)o)) gameObjects.remove((GameObject) o);
        if(o instanceof RenderObject) if(renderObjects.contains((RenderObject)o)) renderObjects.remove((RenderObject) o);
    }
    
    
    /**
     * Get the time in milliseconds
     *
     * @return The system time in milliseconds
     */
    public static double getTime() {
        return (System.nanoTime()* 1000d) / 1_000_000_000d;
    }

    public static void main(String[] args) {
//        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
//        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        
        Thread renderThread = new Thread(RENDERER, "renderThread");
        Thread tickThread = new Thread(TICKER, "tickThread");
        renderThread.start();
        while(!Display.isCreated()) {
            try {
            Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tickThread.start();
        
        
        
    }
}
