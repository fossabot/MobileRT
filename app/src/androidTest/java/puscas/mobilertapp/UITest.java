package puscas.mobilertapp;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.logging.Logger;
import java8.util.stream.IntStreams;
import javax.annotation.Nonnull;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runners.MethodSorters;
import puscas.mobilertapp.utils.Accelerator;
import puscas.mobilertapp.utils.Constants;
import puscas.mobilertapp.utils.ConstantsUI;
import puscas.mobilertapp.utils.Scene;
import puscas.mobilertapp.utils.Shader;
import puscas.mobilertapp.utils.UtilsContext;
import puscas.mobilertapp.utils.UtilsPickerTest;
import puscas.mobilertapp.utils.UtilsTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class UITest extends AbstractTest {

    /**
     * The {@link Logger} for this class.
     */
    @Nonnull
    private static final Logger LOGGER = Logger.getLogger(UITest.class.getName());

    /**
     * The current index of the scene in the {@link NumberPicker}.
     */
    private int counterScene = 0;

    /**
     * The current index of the accelerator in the {@link NumberPicker}.
     */
    private int counterAccelerator = 0;

    /**
     * The current index of the shader in the {@link NumberPicker}.
     */
    private int counterShader = 0;

    /**
     * The current index of the resolution in the {@link NumberPicker}.
     */
    private int counterResolution = 0;

    /**
     * The current index of the number of samples per light in the {@link NumberPicker}.
     */
    private int counterSPL = 0;

    /**
     * The current index of the number of samples per pixel in the {@link NumberPicker}.
     */
    private int counterSPP = 0;

    /**
     * The current index of the number of threads in the {@link NumberPicker}.
     */
    private int counterThreads = 0;

    /**
     * Helper method which tests clicking the preview {@link CheckBox}.
     *
     * @param expectedValue The expected value for the {@link CheckBox}.
     */
    private static void clickPreviewCheckBox(final boolean expectedValue) {
        Espresso.onView(ViewMatchers.withId(R.id.preview))
            .check((view, exception) ->
                assertPreviewCheckBox(view, !expectedValue)
            )
            .perform(ViewActions.click())
            .check((view, exception) ->
                assertPreviewCheckBox(view, expectedValue)
            );
    }

    /**
     * Asserts the {@link CheckBox} expected value.
     *
     * @param view          The {@link View}.
     * @param expectedValue The expected value for the {@link CheckBox}.
     */
    private static void assertPreviewCheckBox(@Nonnull final View view,
                                              final boolean expectedValue) {
        final CheckBox checkbox = view.findViewById(R.id.preview);
        Assertions.assertEquals(Constants.PREVIEW, checkbox.getText().toString(),
            Constants.CHECK_BOX_MESSAGE);
        Assertions.assertEquals(expectedValue, checkbox.isChecked(),
            "Check box has not the expected value");
    }

    /**
     * Helper method which tests the range of the {@link NumberPicker} in the UI.
     *
     * @param numCores The number of CPU cores in the system.
     */
    private static void assertPickerNumbers(final int numCores) {
        IntStreams.rangeClosed(0, 2).forEach(value ->
            UtilsPickerTest
                .changePickerValue(ConstantsUI.PICKER_ACCELERATOR, R.id.pickerAccelerator, value)
        );
        IntStreams.rangeClosed(1, 100).forEach(value ->
            UtilsPickerTest
                .changePickerValue(ConstantsUI.PICKER_SAMPLES_LIGHT, R.id.pickerSamplesLight, value)
        );
        IntStreams.rangeClosed(0, 6).forEach(value ->
            UtilsPickerTest.changePickerValue(ConstantsUI.PICKER_SCENE, R.id.pickerScene, value)
        );
        IntStreams.rangeClosed(0, 4).forEach(value ->
            UtilsPickerTest.changePickerValue(ConstantsUI.PICKER_SHADER, R.id.pickerShader, value)
        );
        IntStreams.rangeClosed(1, numCores).forEach(value ->
            UtilsPickerTest.changePickerValue(ConstantsUI.PICKER_THREADS, R.id.pickerThreads, value)
        );
        IntStreams.rangeClosed(1, 99).forEach(value ->
            UtilsPickerTest
                .changePickerValue(ConstantsUI.PICKER_SAMPLES_PIXEL, R.id.pickerSamplesPixel, value)
        );
        IntStreams.rangeClosed(1, 8).forEach(value ->
            UtilsPickerTest
                .changePickerValue(ConstantsUI.PICKER_SIZE, R.id.pickerSize, value)
        );
    }

    /**
     * Setup method called before each test.
     */
    @Before
    @Override
    public void setUp() {
        super.setUp();

        final String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        LOGGER.info(methodName);
    }

    /**
     * Tear down method called after each test.
     */
    @After
    @Override
    public void tearDown() {
        super.tearDown();

        final String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        LOGGER.info(methodName);
    }

    /**
     * Tests changing all the {@link NumberPicker} and clicking the render
     * {@link Button} few times.
     */
    @Test(timeout = 2L * 60L * 1000L)
    public void testUI() {
        final String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        LOGGER.info(methodName);

        UtilsTest.assertRenderButtonText(Constants.RENDER);

        final int numCores = UtilsContext.getNumOfCores(this.activity);
        assertClickRenderButton(1, numCores);
        assertPickerNumbers(numCores);
        clickPreviewCheckBox(false);
    }

    /**
     * Tests clicking the render {@link Button} many times without preview.
     */
    @Test(timeout = 20L * 60L * 1000L)
    public void testClickRenderButtonManyTimesWithoutPreview() {
        final String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        LOGGER.info(methodName);

        clickPreviewCheckBox(false);

        final int numCores = UtilsContext.getNumOfCores(this.activity);
        assertClickRenderButton(5, numCores);
    }

    /**
     * Tests clicking the render {@link Button} many times with preview.
     */
    @Test(timeout = 30L * 60L * 1000L)
    public void testClickRenderButtonManyTimesWithPreview() {
        final String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        LOGGER.info(methodName);

        clickPreviewCheckBox(false);
        clickPreviewCheckBox(true);

        final int numCores = UtilsContext.getNumOfCores(this.activity);
        assertClickRenderButton(5, numCores);
    }

    /**
     * Helper method which tests clicking the render {@link Button}.
     *
     * @param repetitions The number of repetitions.
     * @param numCores    The number of CPU cores in the system.
     */
    private void assertClickRenderButton(final int repetitions, final int numCores) {
        UtilsPickerTest
            .changePickerValue(ConstantsUI.PICKER_SAMPLES_PIXEL, R.id.pickerSamplesPixel, 99);
        UtilsPickerTest.changePickerValue(ConstantsUI.PICKER_SCENE, R.id.pickerScene, 5);
        UtilsPickerTest.changePickerValue(ConstantsUI.PICKER_THREADS, R.id.pickerThreads, 1);
        UtilsPickerTest.changePickerValue(ConstantsUI.PICKER_SIZE, R.id.pickerSize, 8);
        UtilsPickerTest
            .changePickerValue(ConstantsUI.PICKER_SAMPLES_LIGHT, R.id.pickerSamplesLight, 1);
        UtilsPickerTest
            .changePickerValue(ConstantsUI.PICKER_ACCELERATOR, R.id.pickerAccelerator, 2);
        UtilsPickerTest.changePickerValue(ConstantsUI.PICKER_SHADER, R.id.pickerShader, 2);

        final int numScenes = Scene.values().length;
        final int numAccelerators = Accelerator.values().length;
        final int numShaders = Shader.values().length;
        final int numSPP = 99;
        final int numSPL = 100;
        final int numRes = 9;

        final List<String> buttonTextList =
            ImmutableList.<String>builder().add(Constants.STOP, Constants.RENDER).build();
        IntStreams.range(0, buttonTextList.size() * repetitions).forEach(currentIndex -> {
            final String message = "currentIndex = " + currentIndex;
            LOGGER.info(message);
            final int finalCounterScene = Math.min(this.counterScene % numScenes, 3);
            this.counterScene++;
            final int finalCounterAccelerator =
                Math.max(this.counterAccelerator % numAccelerators, 1);
            this.counterAccelerator++;
            final int finalCounterShader = Math.max(this.counterShader % numShaders, 0);
            this.counterShader++;
            final int finalCounterSPP = Math.max(this.counterSPP % numSPP, 90);
            this.counterSPP++;
            final int finalCounterSPL = Math.max(this.counterSPL % numSPL, 1);
            this.counterSPL++;
            final int finalCounterResolution = Math.max(this.counterResolution % numRes, 7);
            this.counterResolution++;
            final int finalCounterThreads = Math.max(this.counterThreads % numCores, 1);
            this.counterThreads++;
            UtilsPickerTest
                .changePickerValue(ConstantsUI.PICKER_SCENE, R.id.pickerScene, finalCounterScene);
            UtilsPickerTest
                .changePickerValue(ConstantsUI.PICKER_ACCELERATOR, R.id.pickerAccelerator,
                    finalCounterAccelerator);
            UtilsPickerTest.changePickerValue(ConstantsUI.PICKER_SHADER, R.id.pickerShader,
                finalCounterShader);
            UtilsPickerTest
                .changePickerValue(ConstantsUI.PICKER_SAMPLES_PIXEL, R.id.pickerSamplesPixel,
                    finalCounterSPP);
            UtilsPickerTest
                .changePickerValue(ConstantsUI.PICKER_SAMPLES_LIGHT, R.id.pickerSamplesLight,
                    finalCounterSPL);
            UtilsPickerTest.changePickerValue(ConstantsUI.PICKER_THREADS, R.id.pickerThreads,
                finalCounterThreads);
            // TODO: the picker for the resolution always resets its value to the default one
            UtilsPickerTest.changePickerValue(ConstantsUI.PICKER_SIZE, R.id.pickerSize, 4);


            final int expectedIndexOld = currentIndex > 0 ?
                (currentIndex - 1) % buttonTextList.size() : 1;
            final String expectedButtonTextOld = buttonTextList.get(expectedIndexOld);
            final ViewInteraction viewInteraction =
                Espresso.onView(ViewMatchers.withId(R.id.renderButton));
            final int expectedIndex = currentIndex % buttonTextList.size();
            final String expectedButtonText = buttonTextList.get(expectedIndex);

            UtilsTest.assertRenderButtonText(expectedButtonTextOld);

            viewInteraction
                .perform(new ViewActionButton(expectedButtonText))
                .check((view, exception) -> {
                    final Button renderButton = view.findViewById(R.id.renderButton);
                    Assertions.assertEquals(
                        expectedButtonText,
                        renderButton.getText().toString(),
                        "Button message at currentIndex: " + currentIndex
                            + "(" + expectedIndex + ")"
                            + ConstantsUI.LINE_SEPARATOR
                            + "finalCounterScene: " + finalCounterScene
                            + ConstantsUI.LINE_SEPARATOR
                            + "finalCounterAccelerator: " + finalCounterAccelerator
                            + ConstantsUI.LINE_SEPARATOR
                            + "finalCounterShader: " + finalCounterShader
                            + ConstantsUI.LINE_SEPARATOR
                            + "finalCounterSPP: " + finalCounterSPP
                            + ConstantsUI.LINE_SEPARATOR
                            + "finalCounterSPL: " + finalCounterSPL
                            + ConstantsUI.LINE_SEPARATOR
                            + "finalCounterResolution: " + finalCounterResolution
                            + ConstantsUI.LINE_SEPARATOR
                            + "finalCounterThreads: " + finalCounterThreads
                            + ConstantsUI.LINE_SEPARATOR
                    );
                });
        });
    }

}