#version 130

in vec2 pass_textureCoords;

out vec4 outColor;


uniform sampler2D textureSampler;
uniform bool antiAliasingOn;

vec4 color = texture(textureSampler, pass_textureCoords);
vec2 size = 1f/textureSize(textureSampler, 0);

bool isEdge();
vec4 blur();

void main() {

    if(!antiAliasingOn) {
        outColor = color;
        return;
    }

    if(isEdge()) outColor = blur(); //this line gets correct edge on linux
    else outColor = color;
    
}

bool isEdge() {
    //how many pixels away samples should reach
    int samples = 1;
    
    float threshold = 0.10f; //clamp(color.a + 0.01f, 0.01f, 1f);
    bool anyClear = false;
    bool anyNotClear = false;

    if(color.a >= threshold) {

        int i,j;
        for(i=-samples; i<=samples; i++) {
            for(j=-samples; j<=samples; j++) {
                vec2 coord = pass_textureCoords + (vec2(i, j)*size);
                float a = texture(textureSampler, coord).a;
                //isClear
                if(a < threshold) {
                    anyClear = true;
                    return true;
                } else {
                    anyNotClear = true;
                    
                }

            }
        }

        return false;
    }

    int i,j;
    for(i=-samples; i<=samples; i++) {
        for(j=-samples; j<=samples; j++) {
            vec2 coord = pass_textureCoords + (vec2(i, j)*size);
            float a = texture(textureSampler, coord).a;
            //isClear
            if(a < threshold) {
                anyClear = true;
                
            } else {
                anyNotClear = true;
                return true;
            }

        }
    }

    return false;
}

vec4 blur() {
    
    int i,j;
    int samples;
    int dis = 1;
    vec4 sum = vec4(0,0,0,0);
    vec4 toAdd;
    for(i=-dis; i<=dis; i++) {
        for(j=-dis; j<=dis; j++) {
            vec2 offset = vec2(i, j)*size/1f;
            toAdd = texture(textureSampler, pass_textureCoords + offset);
            sum += toAdd;
            samples++;
        }
    }

    return (sum/samples) + color/4.2f;
}

/** glsl fxaa shader main code: http://horde3d.org/wiki/index.php5?title=Shading_Technique_-_FXAA
//http://blenderartists.org/forum/archive/index.php/t-303524.html
        //gl_FragColor.xyz = texture2D(buf0,texCoords).xyz;
        //return;

        float FXAA_SPAN_MAX = 8.0;
        float FXAA_REDUCE_MUL = 1.0/8.0;
        float FXAA_REDUCE_MIN = 1.0/128.0;

        vec3 rgbNW=texture2D(buf0,texCoords+(vec2(-1.0,-1.0)/frameBufSize)).xyz;
        vec3 rgbNE=texture2D(buf0,texCoords+(vec2(1.0,-1.0)/frameBufSize)).xyz;
        vec3 rgbSW=texture2D(buf0,texCoords+(vec2(-1.0,1.0)/frameBufSize)).xyz;
        vec3 rgbSE=texture2D(buf0,texCoords+(vec2(1.0,1.0)/frameBufSize)).xyz;
        vec3 rgbM=texture2D(buf0,texCoords).xyz;
        
        vec3 luma=vec3(0.299, 0.587, 0.114);
        float lumaNW = dot(rgbNW, luma);
        float lumaNE = dot(rgbNE, luma);
        float lumaSW = dot(rgbSW, luma);
        float lumaSE = dot(rgbSE, luma);
        float lumaM  = dot(rgbM,  luma);
        
        float lumaMin = min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)));
        float lumaMax = max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE)));
        
        vec2 dir;
        dir.x = -((lumaNW + lumaNE) - (lumaSW + lumaSE));
        dir.y =  ((lumaNW + lumaSW) - (lumaNE + lumaSE));
        
        float dirReduce = max(
                (lumaNW + lumaNE + lumaSW + lumaSE) * (0.25 * FXAA_REDUCE_MUL),
                FXAA_REDUCE_MIN);
          
        float rcpDirMin = 1.0/(min(abs(dir.x), abs(dir.y)) + dirReduce);
        
        dir = min(vec2( FXAA_SPAN_MAX,  FXAA_SPAN_MAX),
                  max(vec2(-FXAA_SPAN_MAX, -FXAA_SPAN_MAX),
                  dir * rcpDirMin)) / frameBufSize;
                
        vec3 rgbA = (1.0/2.0) * (
                texture2D(buf0, texCoords.xy + dir * (1.0/3.0 - 0.5)).xyz +
                texture2D(buf0, texCoords.xy + dir * (2.0/3.0 - 0.5)).xyz);
        vec3 rgbB = rgbA * (1.0/2.0) + (1.0/4.0) * (
                texture2D(buf0, texCoords.xy + dir * (0.0/3.0 - 0.5)).xyz +
                texture2D(buf0, texCoords.xy + dir * (3.0/3.0 - 0.5)).xyz);
        float lumaB = dot(rgbB, luma);

        if((lumaB < lumaMin) || (lumaB > lumaMax)){
                gl_FragColor.xyz=rgbA;
        }else{
                gl_FragColor.xyz=rgbB;
        }
**/