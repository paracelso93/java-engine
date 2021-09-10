#version 400 core

in vec4 o_color;
in vec3 mv_normals;
in vec3 mv_pos;
in mat4 mv_view;

struct Attenuation {
    float constant;
    float linear;
    float exponent;
};

struct PointLight {
    vec3 color;
    vec3 position;
    float intensity;
    Attenuation attenuation;
};

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int has_texture;
    float reflectance;
};

out vec4 color;

uniform vec3 ambient_light;
uniform float specular_power;
uniform Material material;
uniform PointLight point_light;
// uniform vec3 cameraPos;

vec4 ambient_color;
vec4 diffuse_color;
vec4 specular_color;

void setup_colors(Material material) {
    ambient_color = material.ambient;
    diffuse_color = material.diffuse;
    specular_color = material.specular;
}

vec4 calculate_point_light(PointLight light, vec3 position, vec3 normal) {
    vec4 diffuse = vec4(0);
    vec4 specular = vec4(0);

    vec3 light_position = (mv_view * vec4(light.position, 1.0)).xyz;
    vec3 light_direction = light_position - position;
    vec3 to_light_source = normalize(light_direction);
    float diffuse_factor = max(dot(normal, to_light_source), 0.0);
    diffuse = diffuse_color * vec4(light.color, 1.0) * light.intensity * diffuse_factor;

    vec3 camera_direction = normalize(-position);
    vec3 from_light_source = -to_light_source;
    vec3 reflected_light = normalize(reflect(from_light_source, normal));
    float specular_factor = max(dot(camera_direction, reflected_light), 0.0);
    specular_factor = pow(specular_factor, specular_power);
    specular = specular_color * specular_factor * material.reflectance * vec4(light.color, 1.0);

    float distance = length(light_direction);
    float attenuation = light.attenuation.constant + light.attenuation.linear * distance + light.attenuation.exponent * distance * distance;
    return (diffuse_color * specular) / attenuation;
}

void main(void) {
    setup_colors(material);

    vec4 point = calculate_point_light(point_light, mv_pos, mv_normals);

    color = ambient_color * vec4(ambient_light, 1) + point;
}