package shaders;

import light.AmbientLight;
import light.PointLight;
import model.Material;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.uniforms.MaterialUniform;
import shaders.uniforms.PointLightUniform;

public class ColorNormalShader extends Program {
    private static final String vertexSource = "src/shaders/sources/color_normal.vert";
    private static final String fragmentSource = "src/shaders/sources/color_normal.frag";

    private int transformLocation, mvpLocation, viewLocation, ambientColorLocation;
    private Matrix4f transform, mvp, view;
    private Vector3f ambientColor;
    private int specularPowerLocation;
    private static final PointLightUniform pointLightUniform = new PointLightUniform();
    private PointLight pointLight;
    private static final MaterialUniform materialUniform = new MaterialUniform();
    private Material material;

    public ColorNormalShader() {
        super(vertexSource, fragmentSource);
    }

    public void addUniforms(Matrix4f transform,
                            Matrix4f mvp,
                            Matrix4f view,
                            AmbientLight ambientLight,
                            PointLight pointLight,
                            Material material) {
        this.transform = transform;
        this.mvp = mvp;
        this.view = view;
        this.ambientColor = ambientLight.getColor();
        this.pointLight = pointLight;
        this.material = material;
    }

    private void addPointLightUniform(String name) {
        pointLightUniform.colorLocation = getUniformLocation(name + ".color");
        pointLightUniform.positionLocation = getUniformLocation(name + ".position");
        pointLightUniform.intensityLocation = getUniformLocation(name + ".intensity");
        pointLightUniform.attenuationLocation.constantLocation = getUniformLocation(name + ".attenuation.constant");
        pointLightUniform.attenuationLocation.linearLocation = getUniformLocation(name + ".attenuation.linear");
        pointLightUniform.attenuationLocation.exponentLocation = getUniformLocation(name + ".attenuation.exponent");
    }

    private void loadPointLightUniform() {
        loadVec3(pointLightUniform.colorLocation, pointLight.getColor());
        loadVec3(pointLightUniform.positionLocation, pointLight.getPosition());
        loadFloat(pointLightUniform.intensityLocation, pointLight.getIntensity());
        loadFloat(pointLightUniform.attenuationLocation.constantLocation, pointLight.getAttenuation().constant);
        loadFloat(pointLightUniform.attenuationLocation.linearLocation, pointLight.getAttenuation().linear);
        loadFloat(pointLightUniform.attenuationLocation.exponentLocation, pointLight.getAttenuation().exponent);
    }

    private void addMaterialUniform(String name) {
        materialUniform.ambientLocation = getUniformLocation(name + ".ambient");
        materialUniform.diffuseLocation = getUniformLocation(name + ".diffuse");
        materialUniform.specularLocation = getUniformLocation(name + ".specular");
        materialUniform.hasTextureLocation = getUniformLocation(name + ".has_texture");
        materialUniform.reflectanceLocation = getUniformLocation(name + ".reflectance");
    }

    private void loadMaterialUniform() {
        loadVec4(materialUniform.ambientLocation, material.getAmbient());
        loadVec4(materialUniform.diffuseLocation, material.getDiffuse());
        loadVec4(materialUniform.specularLocation, material.getSpecular());
        loadInt(materialUniform.hasTextureLocation, material.getHasTexture());
        loadFloat(materialUniform.reflectanceLocation, material.getReflectance());
    }

    @Override
    protected void getAllUniforms() {
        transformLocation = getUniformLocation("transform");
        mvpLocation = getUniformLocation("mvp");
        viewLocation = getUniformLocation("view");
        specularPowerLocation = getUniformLocation("specular_power");
        ambientColorLocation = getUniformLocation("ambient_light");
        addMaterialUniform("material");
        addPointLightUniform("point_light");
    }

    @Override
    public void loadAllUniforms() {
        loadMat4(transformLocation, transform);
        loadMat4(mvpLocation, mvp);
        loadMat4(viewLocation, view);
        loadVec3(ambientColorLocation, ambientColor);
        loadFloat(specularPowerLocation, 2f);
        loadPointLightUniform();
        loadMaterialUniform();
    }
}