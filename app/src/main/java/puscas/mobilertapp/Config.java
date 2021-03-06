package puscas.mobilertapp;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * The configurator for the Ray Tracer engine.
 */
public final class Config {

    /**
     * @see Config#getScene()
     */
    private final int scene;

    /**
     * @see Config#getShader()
     */
    private final int shader;

    /**
     * @see Config#getAccelerator()
     */
    private final int accelerator;

    /**
     * @see Config#getWidth()
     */
    private final int width;

    /**
     * @see Config#getHeight()
     */
    private final int height;

    /**
     * @see Config#getSamplesPixel()
     */
    private final int samplesPixel;

    /**
     * @see Config#getSamplesLight()
     */
    private final int samplesLight;

    /**
     * @see Config#getObjFilePath()
     */
    private final String objFilePath;

    /**
     * @see Config#getMatFilePath()
     */
    private final String matFilePath;

    /**
     * @see Config#getCamFilePath()
     */
    private final String camFilePath;

    /**
     * A private constructor to force the usage of the {@link Config.Builder}.
     *
     * @param builder The {@link Config.Builder} for this class.
     */
    private Config(final @NotNull Config.Builder builder) {
        this.scene = builder.getScene();
        this.shader = builder.getShader();
        this.accelerator = builder.getAccelerator();
        this.width = builder.getWidth();
        this.height = builder.getHeight();
        this.samplesPixel = builder.getSamplesPixel();
        this.samplesLight = builder.getSamplesLight();
        this.objFilePath = builder.getObjFilePath();
        this.matFilePath = builder.getMatFilePath();
        this.camFilePath = builder.getCamFilePath();
    }


    /**
     * Gets the index of the scene.
     */
    @Contract(pure = true)
    public int getScene() {
        return this.scene;
    }

    /**
     * Gets the index of the shader.
     */
    @Contract(pure = true)
    public int getShader() {
        return this.shader;
    }

    /**
     * Gets the index of the acceleration structure.
     */
    @Contract(pure = true)
    public int getAccelerator() {
        return this.accelerator;
    }

    /**
     * Gets the width of the image.
     */
    @Contract(pure = true)
    public int getWidth() {
        return this.width;
    }

    /**
     * Gets the height of the image.
     */
    @Contract(pure = true)
    public int getHeight() {
        return this.height;
    }

    /**
     * Gets the number of samples per pixel.
     */
    @Contract(pure = true)
    public int getSamplesPixel() {
        return this.samplesPixel;
    }

    /**
     * Gets the number of samples per light.
     */
    @Contract(pure = true)
    public int getSamplesLight() {
        return this.samplesLight;
    }


    /**
     * Gets the path to the OBJ file containing the geometry of the scene.
     */
    @Contract(pure = true)
    public String getObjFilePath() {
        return this.objFilePath;
    }

    /**
     * Gets the path to the MTL file containing the materials of the scene.
     */
    @Contract(pure = true)
    public String getMatFilePath() {
        return this.matFilePath;
    }

    /**
     * Gets the path to the CAM file containing the camera in the scene.
     */
    @Contract(pure = true)
    public String getCamFilePath() {
        return this.camFilePath;
    }

    /**
     * The builder for this class.
     */
    static final class Builder {

        /**
         * The {@link Logger} for this class.
         */
        private static final Logger LOGGER_BUILDER = Logger.getLogger(Config.Builder.class.getName());

        /**
         * @see Config.Builder#withScene(int)
         */
        private int scene = 0;

        /**
         * @see Config.Builder#withShader(int)
         */
        private int shader = 0;

        /**
         * @see Config.Builder#withAccelerator(int)
         */
        private int accelerator = 0;

        /**
         * @see Config.Builder#withWidth(int)
         */
        private int width = 0;

        /**
         * @see Config.Builder#withHeight(int)
         */
        private int height = 0;

        /**
         * @see Config.Builder#withSamplesPixel(int)
         */
        private int samplesPixel = 0;

        /**
         * @see Config.Builder#withSamplesLight(int)
         */
        private int samplesLight = 0;

        /**
         * The path to the OBJ file.
         */
        private String objFilePath = "";

        /**
         * The path to the MTL file.
         */
        private String matFilePath = "";

        /**
         * The path to the CAM file.
         */
        private String camFilePath = "";

        /**
         * Sets the scene of {@link Config}.
         *
         * @param scene The new value for the {@link Config#scene} field.
         * @return The builder with {@link Config.Builder#scene} already set.
         */
        @Contract("_ -> this")
        @NonNull
        final Config.Builder withScene(final int scene) {
            LOGGER_BUILDER.info("withScene");

            this.scene = scene;
            return this;
        }

        /**
         * Sets the shader of {@link Config}.
         *
         * @param shader The new value for the {@link Config#shader} field.
         * @return The builder with {@link Config.Builder#height} already set.
         */
        @Contract("_ -> this")
        @NonNull
        final Config.Builder withShader(final int shader) {
            LOGGER_BUILDER.info("withShader");

            this.shader = shader;
            return this;
        }

        /**
         * Sets the {@link Config#accelerator}.
         *
         * @param accelerator The new value for the {@link Config#accelerator} field.
         * @return The builder with {@link Config.Builder#samplesPixel} already set.
         */
        @Contract("_ -> this")
        @NonNull
        final Config.Builder withAccelerator(final int accelerator) {
            LOGGER_BUILDER.info("withAccelerator");

            this.accelerator = accelerator;
            return this;
        }

        /**
         * Sets the width of {@link Config}.
         *
         * @param width The new value for the {@link Config#width} field.
         * @return The builder with {@link Config.Builder#width} already set.
         */
        @Contract("_ -> this")
        @NonNull
        final Config.Builder withWidth(final int width) {
            LOGGER_BUILDER.info("withWidth");

            this.width = width;
            return this;
        }

        /**
         * Sets the height of {@link Config}.
         *
         * @param height The new value for the {@link Config#height} field.
         * @return The builder with {@link Config.Builder#height} already set.
         */
        @Contract("_ -> this")
        @NonNull
        final Config.Builder withHeight(final int height) {
            LOGGER_BUILDER.info("withHeight");

            this.height = height;
            return this;
        }

        /**
         * Sets the samples per pixel of {@link Config}.
         *
         * @param samplesPixel The new value for the {@link Config#samplesPixel} field.
         * @return The builder with {@link Config.Builder#samplesPixel} already set.
         */
        @Contract("_ -> this")
        @NonNull
        final Config.Builder withSamplesPixel(final int samplesPixel) {
            LOGGER_BUILDER.info("withSamplesPixel");

            this.samplesPixel = samplesPixel;
            return this;
        }

        /**
         * Sets the samples per light of {@link Config}.
         *
         * @param samplesLight The new value for the {@link Config#samplesLight} field.
         * @return The builder with {@link Config.Builder#samplesLight} already set.
         */
        @Contract("_ -> this")
        @NonNull
        final Config.Builder withSamplesLight(final int samplesLight) {
            LOGGER_BUILDER.info("withSamplesLight");

            this.samplesLight = samplesLight;
            return this;
        }

        /**
         * Sets the path to the OBJ file of {@link Config}.
         *
         * @param objFilePath The new value for the {@link Config#objFilePath} field.
         * @return The builder with {@link Config.Builder#objFilePath} already set.
         */
        @Contract("_ -> this")
        @NonNull
        final Config.Builder withOBJ(final String objFilePath) {
            LOGGER_BUILDER.info("withOBJ");

            this.objFilePath = objFilePath;
            return this;
        }

        /**
         * Sets the path to the MTL file of {@link Config}.
         *
         * @param matFilePath The new value for the {@link Config#matFilePath} field.
         * @return The builder with {@link Config.Builder#matFilePath} already set.
         */
        @Contract("_ -> this")
        @NonNull
        final Config.Builder withMAT(final String matFilePath) {
            LOGGER_BUILDER.info("withMAT");

            this.matFilePath = matFilePath;
            return this;
        }

        /**
         * Sets the path to the CAM file of {@link Config}.
         *
         * @param camFilePath The new value for the {@link Config#camFilePath} field.
         * @return The builder with {@link Config.Builder#camFilePath} already set.
         */
        @Contract("_ -> this")
        @NonNull
        final Config.Builder withCAM(final String camFilePath) {
            LOGGER_BUILDER.info("withCAM");

            this.camFilePath = camFilePath;
            return this;
        }

        /**
         * Builds a new instance of {@link Config}.
         *
         * @return A new instance of {@link Config}.
         */
        @Contract(" -> new")
        @NonNull
        final Config build() {
            LOGGER_BUILDER.info("build");

            return new Config(this);
        }



        /**
         * @see Config.Builder#withScene(int)
         */
        @Contract(pure = true)
        public int getScene() {
            return this.scene;
        }

        /**
         * @see Config.Builder#withShader(int)
         */
        @Contract(pure = true)
        public int getShader() {
            return this.shader;
        }

        /**
         * @see Config.Builder#withAccelerator(int)
         */
        @Contract(pure = true)
        public int getAccelerator() {
            return this.accelerator;
        }

        /**
         * @see Config.Builder#withWidth(int)
         */
        @Contract(pure = true)
        public int getWidth() {
            return this.width;
        }

        /**
         * @see Config.Builder#withHeight(int)
         */
        @Contract(pure = true)
        public int getHeight() {
            return this.height;
        }

        /**
         * @see Config.Builder#withSamplesPixel(int)
         */
        @Contract(pure = true)
        public int getSamplesPixel() {
            return this.samplesPixel;
        }

        /**
         * @see Config.Builder#withSamplesLight(int)
         */
        @Contract(pure = true)
        public int getSamplesLight() {
            return this.samplesLight;
        }


        /**
         * @see Config.Builder#withOBJ(String)
         */
        @Contract(pure = true)
        public String getObjFilePath() {
            return this.objFilePath;
        }

        /**
         * @see Config.Builder#withMAT(String)
         */
        @Contract(pure = true)
        public String getMatFilePath() {
            return this.matFilePath;
        }

        /**
         * @see Config.Builder#withCAM(String)
         */
        @Contract(pure = true)
        public String getCamFilePath() {
            return this.camFilePath;
        }
    }
}
