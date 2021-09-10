package light;

public class Attenuation {
    public float constant;
    public float linear;
    public float exponent;

    public Attenuation(float constant, float linear, float exponent) {
        this.constant = constant;
        this.linear = linear;
        this.exponent = exponent;
    }
}
