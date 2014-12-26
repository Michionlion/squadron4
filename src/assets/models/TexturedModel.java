package assets.models;

import assets.textures.Texture2D;

public class TexturedModel {
    
    private RawModel rawModel;
    private Texture2D texture;
    
    public TexturedModel(RawModel model, Texture2D tex) {
        this.rawModel = model;
        this.texture = tex;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Texture2D getTexture() {
        return texture;
    }
    
    
    
}
