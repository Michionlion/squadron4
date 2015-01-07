#version 400 core

in vec2 pass_textureCoords;

out vec4 outColor;


uniform sampler2D textureSampler;

vec4 color = texture(textureSampler, pass_textureCoords);
bool debug = false;



bool isEdge();
bool isWithin(in float within);
vec4 blur();

void main() {

    

    
    outColor = blur();
    
}

bool isWithin(in float within) {
    
    return false;
}

bool isEdge() {
//  a b
//   x
//  c d
    vec2 size = 1/textureSize(textureSampler, 0);
    float threshold = 0.7f;
    vec2 ac,bc,cc,dc;
    vec4 a, b, c, d;
    ac = vec2(pass_textureCoords.x - size.x,  pass_textureCoords.y + size.y);
    bc = vec2(pass_textureCoords.x + size.x,  pass_textureCoords.y + size.y);
    cc = vec2(pass_textureCoords.x - size.x,  pass_textureCoords.y - size.y);
    dc = vec2(pass_textureCoords.x + size.x,  pass_textureCoords.y - size.y);

    float one = 0.995f;
    float zero = 0.005f;

    if(ac.x >= one || ac.x <= zero || ac.y >= one || ac.y <= zero) return false;
    else a = texture(textureSampler, ac);
    if(bc.x >= one || bc.x <= zero || bc.y >= one || bc.y <= zero) return false;
    else b = texture(textureSampler, bc);
    if(cc.x >= one || cc.x <= zero || cc.y >= one || cc.y <= zero) return false;
    else c = texture(textureSampler, cc);
    if(dc.x >= one || dc.x <= zero || dc.y >= one || dc.y <= zero) return false;
    else d = texture(textureSampler, dc);
    


    // COULD DO MORE LOGIC HERE
    if(a.a < threshold) return true;
    if(b.a < threshold) return true;
    if(c.a < threshold) return true;
    if(d.a < threshold) return true;
    
    return false;
}

vec4 blur() {

    
    float blurAmt = 0.000f;

    if(color.r+color.b+color.g+color.a > 3.63) {
        blurAmt = 0.015f;
    }

    if(isEdge()) blurAmt = 0.013f;


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
    
    if(blurAmt > 0.012) return (sum/samples) + color/2.2f;
    else return color;//vec4(0,0,0,1);
}