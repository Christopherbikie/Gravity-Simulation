#version 330

in vec2 corner;
in vec2 position;
out vec2 tex_coord;
uniform vec2 zoom;

void main()
{
	gl_Position = vec4(position * zoom, 0, 1);
	tex_coord = corner;
}
