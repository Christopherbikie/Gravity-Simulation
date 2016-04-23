#version 330

in vec3 exColour;
in vec2 texCoord;

out vec4 fragColor;

void main()
{
    if (length(texCoord) > 0.5) discard;
    fragColor = vec4(exColour, 1.0);
}