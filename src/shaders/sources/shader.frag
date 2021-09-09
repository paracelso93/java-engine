#version 400 core

in vec4 o_color;
in vec2 uv;

out vec4 color;
uniform sampler2D samp;

void main(void) {
    color = texture(samp, vec2(uv.x, 1.0f - uv.y));
}