package engine.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Utils {
    
    // TRANSFORMATION MATRIX CREATION
    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1,0,0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0,1,0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0,0,1), matrix, matrix);
        Matrix4f.scale(scale, matrix, matrix);
        
        return matrix;
    }
    
    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation) {
        return createTransformationMatrix(translation, rotation, new Vector3f(1,1,1));
    }
    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
        return createTransformationMatrix(translation, rotation, new Vector3f(scale,scale,scale));
    }
    public static Matrix4f createTransformationMatrix(float x, float y, float z, float rx, float ry, float rz, float sx, float sy, float sz) {
        return createTransformationMatrix(new Vector3f(x,y,z), new Vector3f(rx,ry,rz), new Vector3f(sx,sy,sz));
    }
    public static Matrix4f createTransformationMatrix(float x, float y, float z, float rx, float ry, float rz, float scale) {
        return createTransformationMatrix(new Vector3f(x,y,z), new Vector3f(rx,ry,rz), new Vector3f(scale,scale,scale));
    }
    public static Matrix4f createTransformationMatrix(float x, float y, float z, float rx, float ry, float rz) {
        return createTransformationMatrix(new Vector3f(x,y,z), new Vector3f(rx,ry,rz), new Vector3f(1,1,1));
    }
    
    public static Matrix4f createSpriteTransformationMatrix(float x, float y, float rotation, float priority) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(new Vector3f(x,y,-2 + (1f/priority)), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,0,1), matrix, matrix);
        
        return matrix;
    }
    
    //OTHER STUFF
    
    
}
