package shaders.uniforms;


public class PointLightUniform {
    public int colorLocation, positionLocation, intensityLocation;
    public AttenuationUniform attenuationLocation;

    public PointLightUniform() {
        attenuationLocation = new AttenuationUniform();
    }
}
