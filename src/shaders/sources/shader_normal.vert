#version 400 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec4 i_color;
layout (location = 2) in vec2 uv_coord;
layout (location = 3) in vec3 normals;

out vec4 o_color;
out vec2 uv;
out vec3 mv_normals;
out vec3 mv_pos;
out mat4 mv_view;

uniform mat4 transform;
uniform mat4 mvp;
uniform mat4 view;

void main(void) {
    o_color = i_color;
    uv = uv_coord;
    mv_view = view;
    vec4 pos_moved = view * transform * vec4(pos, 1.0);
    gl_Position = mvp * pos_moved;
    mv_normals = (normalize(view * transform * vec4(normals, 1.0))).xyz;
    mv_pos = pos_moved.xyz;
}