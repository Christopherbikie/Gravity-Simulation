#version 330

in vec3 exPosition;

out vec4 fragColor;

uniform vec2 star;
uniform float scale;

void main()
{
	fragColor = vec4(0.1 * scale, 0.1 * scale, 0.1 * scale, 1.0);
	fragColor /= (length(star - vec2(exPosition.x, exPosition.y * 9 / 16))) + 0.07 * scale;
}
