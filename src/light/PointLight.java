package light;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class PointLight extends Light {
    private Vector3f color;
    private Vector3f position;
    private float intensity;
    private Attenuation attenuation;
    public static final float SPECULAR_POWER = 1.5f;


    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
        this.color = color;
        this.position = position;
        this.intensity = intensity;
        this.attenuation = attenuation;
    }

    public Vector3f getColor() {
        return color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getIntensity() {
        return intensity;
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }
}
