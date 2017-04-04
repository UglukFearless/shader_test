
// Fragment Shader

//"in" attributes from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;


//our different texture units
uniform sampler2D u_texture; //default GL_TEXTURE0, expected by SpriteBatch
uniform sampler2D u_texture1; 
uniform sampler2D u_mask;
uniform vec4 ourColor;

void main(void) {
	//sample the colour from the first texture
	vec4 texColor0 = texture2D(u_texture, vTexCoord);

	//sample the colour from the second texture
//	vec4 texColor1 = texture2D(u_texture1, vTexCoord);
//	vec4 texColor1 = vec4(0.3f, 0.9f, 0.0f, 1.0f);
    vec4 texColor1 = ourColor;

	//get the mask; we will only use the alpha channel
	float mask = texture2D(u_mask, vTexCoord).a;

	//interpolate the colours based on the mask
	gl_FragColor = vColor * mix(texColor0, texColor1, mask);
}