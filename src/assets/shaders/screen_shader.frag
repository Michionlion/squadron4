#version 400 core

in vec2 pass_textureCoords;

out vec4 outColor;


uniform sampler2D textureSampler;

vec4 color = texture(textureSampler, pass_textureCoords);
bool debug = false;
vec2 size = 1f/textureSize(textureSampler, 0);


bool isEdge();
vec4 blur();

void main() {

    if(color.a < 0.2f) outColor = vec4(1,0,0,1);
    else outColor = color;
    
    //outColor = blur();
    
}

bool isEdge() {
//  a b
//   x
//  c d
    
    float threshold = 0.7f;
    //coords
    vec2 ac,bc,cc,dc;
    //color of coord
    vec4 a, b, c, d;
    //is coord clear?
    bool i, ia,ib,ic,id;
    ac = vec2(pass_textureCoords.x - size.x,  pass_textureCoords.y + size.y);
    bc = vec2(pass_textureCoords.x + size.x,  pass_textureCoords.y + size.y);
    cc = vec2(pass_textureCoords.x - size.x,  pass_textureCoords.y - size.y);
    dc = vec2(pass_textureCoords.x + size.x,  pass_textureCoords.y - size.y);

    



    //float one = 1f;
    //float zero = 0f;
    //if(pass_textureCoords.x > one || pass_textureCoords.x < zero || pass_textureCoords.y > one || pass_textureCoords.y < zero) return true;
    //if(ac.x > one || ac.x < zero || ac.y > one || ac.y < zero) return false;
    
    //if(bc.x > one || bc.x < zero || bc.y > one || bc.y < zero) return false;
    
    //if(cc.x > one || cc.x < zero || cc.y > one || cc.y < zero) return false;
    
    //if(dc.x > one || dc.x < zero || dc.y > one || dc.y < zero) return false;
    
    
    a = texture(textureSampler, ac);
    b = texture(textureSampler, bc);
    c = texture(textureSampler, cc);
    d = texture(textureSampler, dc);
    
    if(color.a < threshold) i = true;
    if(a.a < threshold) ia = true;
    if(b.a < threshold) ib = true;
    if(c.a < threshold) ic = true;
    if(d.a < threshold) id = true;
    
    bool anyNotClear = !ia || !ib || !ic || !id;
    bool anyClear = ia || ib || ic || id;


    if(i && anyNotClear) return true;
    if(i) return true;
    
    
    
    return false;
}

vec4 blur() {
    
    bool blurred = false;
    
    float blurAmt = 0f;

    if(isEdge()) {
        blurred = true;
        debug = true;
        blurAmt = size*2;
    }
    if(blurred) {
        int i,j;
        int samples;
        vec4 sum = vec4(0);

        for(i=-2; i<=2; i++) {
            for(j=-2; j<=2; j++) {
                vec2 offset = vec2(i, j)*blurAmt;
                vec4 toAdd = texture(textureSampler, pass_textureCoords + offset);
                if(toAdd.a > 0.6) sum += toAdd;
                samples++;
            }
        }
        if(debug) return vec4(1,0,0,1);
        return (sum/samples) + color/4.7f;
    }
    
    if(debug) return vec4(1,0,0,1);
    return color;
    
    
    
}