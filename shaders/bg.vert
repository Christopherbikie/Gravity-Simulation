#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 inColour;

out vec3 exColour;
out vec3 exPosition;

uniform mat4 projectionMatrix;

void main()
{
	gl_Position = projectionMatrix * vec4(position, 1.0);
	exPosition = vec3(projectionMatrix * vec4(position, 1.0));
}
