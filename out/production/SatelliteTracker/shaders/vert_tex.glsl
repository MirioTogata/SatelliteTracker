uniform mat4 transform;
uniform mat4 texMatrix;
uniform mat4 modelviewMatrix;
uniform mat3 normalMatrix;

attribute vec4 position;
attribute vec4 color;
attribute vec3 normal;
attribute vec2 texCoord;

varying vec3 vertPos;
varying vec3 vertNormal;
varying vec4 vertColor;
varying vec4 vertTexCoord;

void main() {
  gl_Position = transform * position;

  vertPos = vec3(modelviewMatrix * position);
  vertNormal = normalMatrix * normal;
  vertColor = color;
  vertTexCoord = texMatrix * vec4(texCoord, 1.0, 1.0);

}