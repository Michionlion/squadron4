package engine;

import assets.sprites.WorldObject;
import engine.interfaces.RenderObject;
import engine.interfaces.Tickable;
import engine.render.Renderer;
import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;

public class Globals {
    
    public static String OS;
    
    static { // get OS before anything else
        System.out.print("Running on " + System.getProperty("os.name"));
        if(System.getProperty("os.name").toLowerCase().contains("windows")) OS = "WINDOWS";
        else if(System.getProperty("os.name").toLowerCase().contains("nux")) OS = "LINUX";
        else if(System.getProperty("os.name").toLowerCase().contains("mac")) OS = "MAC";
        System.out.println(", setting OS to " + OS);
    }
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    
    public static final int FPS_CAP = 60;

    public static Ticker TICKER = new Ticker(30);
    public static Renderer RENDERER = new Renderer(); //force AA by putting true in as arg
    public static Client CLIENT;
    
    
    public static CopyOnWriteArrayList<WorldObject> worldObjects;
    public static CopyOnWriteArrayList<RenderObject> renderObjects;
    
    public static Rectangle viewArea = new Rectangle(0, 0, WIDTH, HEIGHT);
    
    
    
    
    
    
    
    
    public static void add(Object o) {
        if(o instanceof Tickable) TICKER.add((Tickable) o);
        if(o instanceof WorldObject) worldObjects.add((WorldObject) o);
        if(o instanceof RenderObject) renderObjects.add((RenderObject) o);
    }
    
    public static void remove(Object o) {
        if(o instanceof Tickable) if(TICKER.isTicking((Tickable) o)) TICKER.remove((Tickable) o);
        if(o instanceof GameObject) if(worldObjects.contains((GameObject)o)) worldObjects.remove((GameObject) o);
        if(o instanceof RenderObject) if(renderObjects.contains((RenderObject)o)) renderObjects.remove((RenderObject) o);
    }
    
    
    public static String userName() {
        return "NO_USERNAME_SUPPORT_YET";
    }
    
    public static Vector2f mousePos() {
        return new Vector2f(Mouse.getX(), HEIGHT-Mouse.getY());
    }
    
    public static boolean isMulti() {
        return false;
    }
    
    public void log(Object s) {
        System.out.println(s);
    }
    
    /**
     * Get the time in milliseconds
     *
     * @return The system time in milliseconds
     */
    public static double getTime() {
        return System.nanoTime() / 1_000_000d;
    }
    
    protected static void initArrays() {
        worldObjects = new CopyOnWriteArrayList<>();
        renderObjects = new CopyOnWriteArrayList<>();
    }
    
    protected static void startGame() {
        Thread renderThread = new Thread(RENDERER, "renderThread");
        Thread tickThread = new Thread(TICKER, "tickThread");
        
        initArrays();
        
        
        renderThread.start();
        while(!Display.isCreated()) {
            try {
            Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tickThread.start();
    }

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
        
                
//        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        
        
        startGame();
    }
}
