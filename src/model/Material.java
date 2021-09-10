package model;

import org.lwjgl.util.vector.Vector4f;

public class Material {
    private final Vector4f ambient;
    private final Vector4f diffuse;
    private final Vector4f specular;
    private final int hasTexture;
    private final float reflectance;

    public Material(Vector4f ambient,
                    Vector4f diffuse,
                    Vector4f specular,
                    int hasTexture,
                    float reflectance) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.hasTexture = hasTexture;
        this.reflectance = reflectance;
    }

    public Vector4f getAmbient() {
        return ambient;
    }

    public Vector4f getDiffuse() {
        return diffuse;
    }

    public Vector4f getSpecular() {
        return specular;
    }

    public int getHasTexture() {
        return hasTexture;
    }

    public float getReflectance() {
        return reflectance;
    }
}
