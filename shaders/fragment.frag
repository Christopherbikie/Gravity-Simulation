#version 330

in vec2 tex_coord;
out vec3 out_color;

void main()
{
    if (length(tex_coord - vec2(0.5, 0.5)) > 0.5) discard;
    out_color = vec3(0.7, tex_coord);
}
