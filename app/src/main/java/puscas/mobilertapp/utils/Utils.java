package puscas.mobilertapp.utils;

import android.widget.NumberPicker;
import androidx.annotation.NonNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java8.util.Objects;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Contract;
import puscas.mobilertapp.exceptions.FailureException;

/**
 * Utility class with some helper methods.
 */
public final class Utils {

    /**
     * The {@link Logger} for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    /**
     * A private constructor in order to prevent instantiating this helper class.
     */
    private Utils() {
    }

    /**
     * Helper method that waits for an {@link ExecutorService} to finish all the
     * tasks.
     *
     * @param executorService The {@link ExecutorService}.
     */
    public static void waitExecutorToFinish(@Nonnull final ExecutorService executorService) {
        LOGGER.info("waitExecutorToFinish");

        boolean running = true;
        do {
            try {
                running = !executorService.awaitTermination(1L, TimeUnit.DAYS);
            } catch (final InterruptedException ex) {
                Thread.currentThread().interrupt();
            } finally {
                Utils.handleInterruption("Utils#waitExecutorToFinish");
            }
        }
        while (running);

        final String message = "waitExecutorToFinish" + ConstantsMethods.FINISHED;
        LOGGER.info(message);
    }

    /**
     * Helper method that handles an {@link InterruptedException}.
     *
     * @param methodName The name of the method to appear in the logs.
     * @implNote It resets the interrupted flag because when instrumented tests
     *     fail by timeout, this interrupt makes the {@link android.app.Activity}
     *     not finish properly.
     */
    public static void handleInterruption(@Nonnull final String methodName) {
        final boolean interrupted = Thread.interrupted();
        final String message = String.format("%s exception: %s", methodName, interrupted);
        LOGGER.severe(message);
    }

    /**
     * Helper method which reads a {@link String} from an {@link InputStreamReader}.
     *
     * @param inputStream The {@link InputStream} to read from.
     * @return A {@link String} containing the contents of the {@link InputStream}.
     */
    @Nonnull
    public static String readTextFromInputStream(@Nonnull final InputStream inputStream) {
        LOGGER.info("readTextFromInputStream");
        try (InputStreamReader isReader = new InputStreamReader(
            inputStream, Charset.defaultCharset());
             BufferedReader reader = new BufferedReader(isReader)) {

            final StringBuilder stringBuilder = new StringBuilder(1);
            String str = reader.readLine();
            while (Objects.nonNull(str)) {
                stringBuilder.append(str).append(ConstantsUI.LINE_SEPARATOR);
                str = reader.readLine();
            }
            return stringBuilder.toString();
        } catch (final OutOfMemoryError ex1) {
            throw new FailureException(ex1);
        } catch (final IOException ex2) {
            throw new FailureException(ex2);
        } finally {
            final String message = "readTextFromInputStream" + ConstantsMethods.FINISHED;
            LOGGER.info(message);
        }
    }

    /**
     * Calculates the size, in MegaBytes of the scene with a certain number of
     * primitives.
     * Note that this method tries to predict the size of the scene in the
     * Ray Tracer engine context, and not in the OpenGL context.
     *
     * @param numPrimitives The number of primitives in the scene.
     * @return The size, in MegaBytes, of the scene.
     */
    @Contract(pure = true)
    public static int calculateSceneSize(final int numPrimitives) {
        LOGGER.info("calculateSceneSize");
        final int triangleVerticesSize = 3 * Constants.BYTES_IN_FLOAT * 3;
        final int triangleNormalsSize = 3 * Constants.BYTES_IN_FLOAT * 3;
        final int triangleTextureCoordinatesSize = 2 * Constants.BYTES_IN_FLOAT * 3;
        final int triangleMaterialIndexSize = Constants.BYTES_IN_INTEGER;

        final int triangleMembersSize = triangleVerticesSize
            + triangleNormalsSize
            + triangleTextureCoordinatesSize
            + triangleMaterialIndexSize;

        final int triangleMethodsSize = Constants.BYTES_IN_POINTER * 21;
        final int triangleSize = triangleMembersSize + triangleMethodsSize;
        return 1 + ((numPrimitives * triangleSize) / Constants.BYTES_IN_MEGABYTE);
    }

    /**
     * Helper method that parses the displayed value from a {@link NumberPicker}
     * to an actual {@link Integer}.
     *
     * @param picker The {@link NumberPicker} to parse the displayed value.
     * @return The current displayed value in the {@link NumberPicker}.
     */
    public static int getValueFromPicker(@NonNull final NumberPicker picker) {
        LOGGER.info("getValueFromPicker");

        try {
            return Integer.parseInt(picker.getDisplayedValues()
                [picker.getValue() - 1]);
        } catch (final NumberFormatException ex) {
            throw new FailureException(ex);
        }
    }

    /**
     * Helper method that parses the displayed value from the resolution
     * {@link NumberPicker} to a {@link Pair} with the actual {@link Integer}s
     * (width, height).
     *
     * @param picker The {@link NumberPicker} to parse the displayed resolution.
     * @return The current displayed resolution in the {@link NumberPicker}.
     */
    @NonNull
    public static Pair<Integer, Integer> getResolutionFromPicker(
        @NonNull final NumberPicker picker) {
        LOGGER.info("getResolutionFromPicker");

        final String strResolution = picker.getDisplayedValues()[picker.getValue() - 1];

        try {
            final int width = Integer.parseInt(strResolution.substring(0, strResolution.indexOf('x')));
            final int height = Integer.parseInt(
                strResolution.substring(strResolution.indexOf('x') + 1));
            return Pair.of(width, height);
        } catch (final NumberFormatException ex) {
            throw new FailureException(ex);
        }
    }

    /**
     * Helper method that will execute a {@link Runnable} and will ignore any
     * {@link Exception} that might be thrown.
     * @implNote If an {@link Exception} is thrown, then it will just log
     * the message.
     *
     * @param method The {@link Runnable} to call.
     */
    public static void executeWithCatching(@Nonnull final Runnable method) {
        LOGGER.info(ConstantsMethods.RUN);
        try {
            method.run();
        } catch (final RuntimeException ex) {
            LOGGER.warning(ex.getMessage());
        }
    }

}
