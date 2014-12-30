package engine.render;

import assets.Loader;
import assets.models.RawModel;
import assets.shaders.ShaderProgram;
import assets.shaders.SpriteShader;
import assets.sprites.MovingSprite;
import assets.sprites.Sprite;
import engine.Globals;
import engine.interfaces.Interpolatable;
import engine.interfaces.RenderObject;
import engine.interfaces.Shader;
import engine.util.Util;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class Renderer implements Runnable {

    public float interpolation;

    public static RawModel QUAD;

    public RawModel boundModel;
    public Texture boundTexture;

    private double now = System.nanoTime();
    Area aaa = null;
    private long renders = 0;
    private boolean isInterpolating = false;

    @Override
    public void run() {
        if (Globals.isMulti()) {
            isInterpolating = true;
        }
        //init  - can't do it in constructor!
        try {
            DisplayManager.createDisplay();
            Display.makeCurrent();
        } catch (LWJGLException ex) {
            System.err.println("Unable to change GLCONTEXT threads using Display.makeCurrent()!");
            Sys.alert("ERROR 2", "Unable to change GLCONTEXT threads using Display.makeCurrent()!");
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Keyboard.create();
            Mouse.create();
        } catch (LWJGLException ex) {
            System.err.println("Unable to create Input Devices!");
            Sys.alert("ERROR 3", "Unable to create Input Devices!");
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

//        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        float[] verts
                = {0.5f, 0.5f, 0f, //v1
                    0.5f, -0.5f, 0f, //v2
                    -0.5f, -0.5f, 0f, //v3
                    -0.5f, 0.5f, 0f};  //v4
        float[] texs = {0f, 0f, 0f, 1f, 1f, 1f, 1f, 0f};
        int[] indices = {0, 1, 3, 3, 1, 2};

        QUAD = Loader.loadToVAO(verts, texs, indices);

        //render loop
        SpriteShader s = new SpriteShader();
        MovingSprite sprite2 = new MovingSprite(Loader.getTexture("debug"), new Vector2f(0, 0), 0, new Vector2f(0, 0), new Vector2f(512,512), 1);
        Globals.add(sprite2);
        while (!Display.isCloseRequested()) {
            now = Globals.getTime();
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                break;
            }
            if (isInterpolating) {
                interpolation = Math.min(1.0f, (float) ((now - Globals.TICKER.lastTickTime) / (TimeUnit.SECONDS.toMillis(1) / Globals.TICKER.targetTPS)));
            } else {
                interpolation = 1;
            }
            prepare();

            GL30.glBindVertexArray(QUAD.getVaoID());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, Loader.getTextureID("spaceship-off"));

            for (RenderObject toRender : Globals.renderObjects) {
                if (toRender.isVisible()) {
                    if (toRender instanceof Sprite) {
                        render((Sprite) toRender, s);
                    }
                }
            }

            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL30.glBindVertexArray(0);

            if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
                isInterpolating = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
                isInterpolating = false;
            }
            GL11.glFinish();
            renders++;
            double renderTime = Globals.getTime() - now;
            System.out.println("render " + renders + " done in " + renderTime + "ms.");
            Display.setTitle("Squadron 4  -  TPS: " + Globals.TICKER.getTPS() + "  -  Sprites: " + Globals.renderObjects.size());
            DisplayManager.updateDisplay();
        }

        Globals.TICKER.end();

        s.cleanUp();

        Loader.cleanUp();

        DisplayManager.closeDisplay();
    }

    public void prepare() {
        GL11.glClearColor(0, 0, 1, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void render(Sprite toRender, Shader shader) {
        shader.start();
        Matrix4f tMatrix;
        if (toRender instanceof Interpolatable) {
            Interpolatable i = (Interpolatable) toRender;
//            System.out.println("Interpolating: " + i);
            tMatrix = Util.createSpriteTransformationMatrix(toRender.getX() + (i.getDeltaX() * interpolation), toRender.getY() + (i.getDeltaY() * interpolation), toRender.getRotation(), toRender.getWidth(), toRender.getHeight(), toRender.getPriority());
        } else {
            tMatrix = Util.createSpriteTransformationMatrix(toRender.getX(), toRender.getY(), toRender.getRotation(), toRender.getWidth(), toRender.getHeight(), toRender.getPriority());
        }
        shader.loadTransformationMatrix(tMatrix);

//        GL30.glBindVertexArray(QUAD.getVaoID());
//        GL20.glEnableVertexAttribArray(0);
//        GL20.glEnableVertexAttribArray(1);
//        GL13.glActiveTexture(GL13.GL_TEXTURE0);
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, toRender.getTex().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, QUAD.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
//        GL20.glDisableVertexAttribArray(0);
//        GL20.glDisableVertexAttribArray(1);
//        GL30.glBindVertexArray(0);

        shader.stop();
    }

    public Area createAreaFromImage(String fileName, byte cutoff) {

        Texture tex = Loader.getTexture(fileName);

        byte[] data = tex.getTextureData();

        GeneralPath gp = new GeneralPath();

        int i = tex.getTextureHeight();
        boolean cont = false;
        for (int x = 0; x < i; x++) {
            for (int y = 0; y < i; y++) {
                byte alpha = data[4 * (y * i + x) + 3];

                if (alpha == -1 || alpha >= cutoff) {
                    if (cont) {
                        gp.lineTo(x, y);
                        gp.lineTo(x, y + 1);
                        gp.lineTo(x + 1, y + 1);
                        gp.lineTo(x + 1, y);
                        gp.lineTo(x, y);
                    } else {
                        gp.moveTo(x, y);
                    }
                    cont = true;
                } else {
                    cont = false;
                }
            }
            cont = false;
//            System.out.println(100*((float)x/(float)tex.getTextureWidth()) + "%");
        }

        return new Area(gp);
    }

    public Area createDetailedAreaFromImage(String fileName, byte cutoff) {

        Texture tex = Loader.getTexture(fileName);

        byte[] data = tex.getTextureData();

        Area area = new Area();
        Rectangle rectangle = new Rectangle();
        for (int x = 0; x < tex.getTextureWidth(); x++) {
            for (int y = 0; y < tex.getTextureWidth(); y++) {
                byte alpha = data[4 * (y * tex.getTextureWidth() + x) + 3];
//                System.out.println("alpha at " + x + ", " + y + " is " + alpha);
                if (alpha == -1 || alpha >= cutoff) {
                    rectangle.setBounds(x, y, 1, 1);
                    area.add(new Area(rectangle));
                }
            }
        }
        return area;
    }
}
