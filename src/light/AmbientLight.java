package light;

import org.lwjgl.util.vector.Vector3f;

public class AmbientLight extends Light {

    private Vector3f color;

    public AmbientLight(Vector3f color) {
        this.color = color;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
