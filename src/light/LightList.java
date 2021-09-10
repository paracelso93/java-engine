package light;

import shaders.Program;

import java.util.ArrayList;
import java.util.HashMap;

public class LightList {
    private static final HashMap<String, Light> lights;
    static {
        lights = new HashMap<>();
    }

    public static <T extends Light> ArrayList<T> getLightsWithType(Class<T> type) {
        ArrayList<T> list = new ArrayList<>();
        for (Light light : lights.values()) {
            if (light.getClass().equals(type)) {
                list.add((T)light);
            }
        }

        return list;
    }

    public static <T extends Light> T getLight(String id) {

        Light light = lights.get(id);
        if (light != null) {
            return (T) light;
        }

        return null;
    }

    public static void addLight(String id, Light program) {
        lights.put(id, program);
    }

    public static void deleteLight(String id) {

        lights.remove(id);
    }
}
