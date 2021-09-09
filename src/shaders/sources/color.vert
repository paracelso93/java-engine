#version 400 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec4 i_color;

out vec4 o_color;

uniform mat4 transform;
uniform mat4 mvp;
uniform mat4 view;

void main(void) {
    o_color = i_color;
    gl_Position = vec4(pos, 1.0);
    //gl_Position = mvp * view * transform * vec4(pos, 1.0);
}