package shaders;


import java.util.*;

public class ShaderList {
    private static final ArrayList<Program> shaders;
    static {
        shaders = new ArrayList<>();
    }

    public static <T extends Program> T getShader(Class<T> type) {
        for (Program program : shaders) {
            if (type.equals(program.getClass())) {
                return type.cast(program);
            }
        }

        return null;
    }

    public static void addShader(Program program) {
        shaders.add(program);
    }

    public static <T extends Program> void deleteShader(Class<T> type) {

        shaders.removeIf(shader -> shader.getClass().equals(type));
    }

    public static void destroy() {
        for (Program shader : shaders) {
            shader.destroy();
        }
    }
}
