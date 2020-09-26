#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D texture;
uniform vec4 lightPosition[8];
uniform vec3 lightAmbient[8];
uniform vec3 lightDiffuse[8];
uniform vec3 lightFalloff[8];

varying vec3 vertPos;
varying vec3 vertNormal;
varying vec4 vertColor;
varying vec4 vertTexCoord;

void main() {

  vec4 albedo = vertColor * texture2D(texture, vertTexCoord.st);

  vec3 ambient = albedo.xyz * lightAmbient[0];

  vec3 ldir = (vertPos - lightPosition[1].xyz);
  float dist = length(ldir);
  ldir = normalize(ldir);
  float ldotn = max(0.0f, dot(ldir, normalize(vertNormal)));
  float att = lightFalloff[1].x +
    lightFalloff[1].y * dist +
    lightFalloff[1].z * dist * dist;
  vec3 diffuse = (ldotn * albedo.xyz * lightDiffuse[1]) / att;

  gl_FragColor = vec4(ambient + diffuse, albedo.w);
}