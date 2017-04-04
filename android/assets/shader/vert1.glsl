
// Vertex Shader


//"in" attributes from our SpriteBatch
attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;

//combined projection and view matrix
uniform mat4 u_projTrans;

//"out" varyings to our fragment shader
varying vec4 vColor;
varying vec2 vTexCoord;
 
void main() {
	vColor = a_color;
//    vColor = vec4(0.9f, 0.0f, 1.0f, 1.0f);
	vTexCoord = a_texCoord0;
	gl_Position = u_projTrans * a_position;
}