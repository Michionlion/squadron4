package engine.render;

import assets.models.RawModel;
import assets.shaders.SpriteShader;
import assets.sprites.Sprite;
import engine.Globals;
import engine.interfaces.Interpolatable;
import engine.util.Utils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

public class Renderer implements Runnable {

    public float interpolation;

    public static RawModel QUAD;
    
    private long now = System.nanoTime();

    public Renderer() {

        float[] verts
                = {0.5f, 0.5f, 0f, //v1
                    0.5f, -0.5f, 0f, //v2
                    -0.5f, -0.5f, 0f, //v3
                    -0.5f, 0.5f, 0f};  //v4
        float[] texs = {0f, 0f, 0f, 1f, 1f, 1f, 1f, 0f};
        int[] indices = {0, 1, 3, 3, 1, 2};

        QUAD = Loader.loadToVAO(verts, texs, indices);
    }

    @Override
    public void run() {

        SpriteShader s = new SpriteShader();
        Sprite sprite = new Sprite(Loader.getTexture("demo"), 0, 0, 0, 1);

        while (!Display.isCloseRequested()) {
            now = System.nanoTime();
            
            if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
                sprite.rotate(1);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
                sprite.rotate(-1);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                sprite.translate(0, 0.2f);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                sprite.translate(0, -0.2f);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                sprite.translate(0.2f, 0);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                sprite.translate(-0.2f, 0);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                break;
            }
            
            
            interpolation = Math.min(1.0f, (float) ((now - Globals.logicThread.lastTickTime) / Globals.logicThread.MILLIS_PER_FRAME) );
            prepare();
            render(sprite, s);

            DisplayManager.updateDisplay();

            
            
            
        }
        
        
        s.cleanUp();
        Loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    public void prepare() {
        GL11.glClearColor(0, 0, 0, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void render(Sprite sprite, SpriteShader shader) {
        shader.start();
        Matrix4f tMatrix;
        if(sprite instanceof Interpolatable) {
            Interpolatable i = (Interpolatable) sprite;
            tMatrix = Utils.createSpriteTransformationMatrix(sprite.getX()+(i.getDeltaX()*interpolation), sprite.getY()+(i.getDeltaY()*interpolation), sprite.getRotation(), sprite.getWidth(), sprite.getHeight(), sprite.getPriority());
        }
        else tMatrix = Utils.createSpriteTransformationMatrix(sprite.getX(), sprite.getY(), sprite.getRotation(), sprite.getWidth(), sprite.getHeight(), sprite.getPriority());
        shader.loadTransformationMatrix(tMatrix);

        GL30.glBindVertexArray(QUAD.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getTex().getID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, QUAD.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);

        shader.stop();
    }
}
