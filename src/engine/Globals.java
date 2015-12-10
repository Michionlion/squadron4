package engine;

import engine.interfaces.RenderObject;
import engine.interfaces.Tickable;
import engine.render.Renderer;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;

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
    
    public static final int FPS_CAP = 240;

    public static Ticker TICKER = new Ticker(30);
    public static Renderer RENDERER = new Renderer();
    public static Client CLIENT;
    
    
    public static CopyOnWriteArrayList<GameObject> gameObjects;
    public static CopyOnWriteArrayList<RenderObject> renderObjects;
    
    public static Camera camera = new Camera(0, 0, WIDTH, HEIGHT);
    
    
    
    
    
    
    
    
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
    
    
    public static String getUserName() {
        return "NO_USERNAME_SUPPORT_YET";
    }
    
    public static boolean isMulti() {
        return false;
    }
    
    /**
     * Get the time in milliseconds
     *
     * @return The system time in milliseconds
     */
    public static double getTime() {
        return System.nanoTime() / 1_000_000d;
    }
    
    protected static void init() {
        gameObjects = new CopyOnWriteArrayList<>();
        renderObjects = new CopyOnWriteArrayList<>();
        add(camera);
    }
    
    protected static void startGame() {
        Thread renderThread = new Thread(RENDERER, "renderThread");
        Thread tickThread = new Thread(TICKER, "tickThread");
        
        init();
        
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
