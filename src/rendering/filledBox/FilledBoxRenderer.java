package rendering.filledBox;

import controllers.codeWindow.CodeWindowController;
import controllers.flowchartWindow.FlowchartWindowController;
import controllers.gui.ButtonController;
import dataStructures.RawModel;
import gui.GUIFilledBox;
import gui.Header;
import gui.buttons.Button;
import gui.buttons.TextButton;
import gui.textBoxes.FlowchartTextBox;
import gui.textBoxes.TextBox;
import loaders.Loader;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import utils.Maths;

/**
 * Controls rendering {@link GUIFilledBox filled boxes}, such as those which appear
 * behind {@link TextButton text buttons}, behind text in {@link TextBox text boxes}, or
 * the {@link Header header}. As far as rendering is concerned a filled box consists of a
 * position, a size, and a color. The position and size are two dimensional vectors in the
 * OpenGL coordinate system while the color is a three dimensional rgb color vector.
 */
public class FilledBoxRenderer {

    //The vertices used by a filled box. They go from the bottom left of the screen to the top right
    //in the filled box coordinate system
    private static final float[] VERTICES = {
            0, 0,
            0, 1,
            1, 0,
            1, 1
    };
    //The model used for rendering which uses the vertices
    private RawModel square;
    //The shader program which is used to render a filled box
    private FilledBoxShader shader;

    /**
     * Initializes for {@link GUIFilledBox text box} rendering. Creates the shader program, loads the model to the GPU, and initializes the default transforms.
     */
    public FilledBoxRenderer() {
        shader = new FilledBoxShader();
        square = Loader.loadToVAO(VERTICES, 2);
    }

    /**
     * Renders all {@link GUIFilledBox} filled boxes to the screen which are not part of the GUI. Filled
     * boxes are any rectangular region with a solid background color. The exception is the background
     * of the flowchart which is instead the OpenGL clear color which the screen is set to before
     * rendering each frame.
     * @param flowchartWindowController the controller which contains all flowchart filled boxes
     * @param codeWindowController the controller which contains the filled boxes for the code window
     */
    public void renderToScreen(FlowchartWindowController flowchartWindowController, CodeWindowController codeWindowController) {
        //Perform setup which is performed regardless of which rendering method is used
        prepare();

        //All filled boxes rendered with this method are either part of the code window or flowchart window,
        //which means that they need to be clipped
        shader.doClipping.loadBoolean(true);

        //If the codeWindowController is not null then a code window is open and needs to be rendered
        if (codeWindowController != null) {
            //Load the window position and size from the code window which will be used for clipping the code window text boxes
            shader.windowPosition.loadVec2(codeWindowController.getCodeWindow().getPosition().x, codeWindowController.getCodeWindow().getPosition().y);
            shader.windowSize.loadVec2(codeWindowController.getCodeWindow().getSize());
            //Ensure that the aspect ratio being used for the filled boxes are identities
            shader.aspectRatio.loadMatrix(GeneralSettings.IDENTITY2);
            shader.zoomTranslateMatrix.loadMatrix(GeneralSettings.IDENTITY3);
            //Render the text box behind the line numbers
            renderFilledBox(codeWindowController.getCodeWindow().getTextNumberFilledBox());
            //Render the text box behind the main text
            renderFilledBox(codeWindowController.getCodeWindow().getGuiFilledBox());

            //The flowchart window can only be open if the code window is open
            //Test to see if the flowchart window was also opened
            if (flowchartWindowController != null) {
                //Each text box in the flowchart window controller will use the same translation,
                //clipping window position, clipping window size, and aspect ratio, load the appropriate values
                shader.zoomTranslateMatrix.loadMatrix(flowchartWindowController.getZoomTranslateMatrix());
                shader.windowPosition.loadVec2(flowchartWindowController.getPosition());
                shader.windowSize.loadVec2(flowchartWindowController.getSize());
                shader.aspectRatio.loadMatrix(GeneralSettings.ASPECT_RATIO);

                //For each text box in the flowchart text box controller
                for (TextBox textBox : flowchartWindowController.getFlowchartTextBoxController().getTextBoxes()) {
                    //If the text box is a flowchart text box render it's text boxes
                    if (textBox instanceof FlowchartTextBox) {
                        //Render the text box behind the main text
                        renderFilledBox(textBox.getGuiFilledBox());
                        //Render the text box behind the line numbers
                        renderFilledBox(textBox.getTextNumberFilledBox());
                    } else {
                        //Something is wrong with setup, print an error message
                        System.err.println("Undefined box rendering behavior, non flowchart text box in the flowchart text box controller");
                    }
                }
            }
        }

        //Perform per render call cleanup which is performed regardless of which rendering method is used
        endRendering();
    }

    /**
     * Renders all {@link GUIFilledBox filled boxes} to an image which are part of the flowchart. Filled
     * boxes are any rectangular region with a solid background color. The exception is the background
     * of the flowchart which is instead the OpenGL clear color which the screen is set to before
     * rendering each image.
     * @param flowchartWindowController the controller which contains all flowchart filled boxes
     */
    public void renderToImage(FlowchartWindowController flowchartWindowController) {
        //Perform setup which is performed regardless of which rendering method is used
        prepare();

        //To render an image filled boxes should be arranged according to the scale and position of the image to be created. This is stored in GeneralSettings.IMAGE_TRANSLATION
        shader.zoomTranslateMatrix.loadMatrix(GeneralSettings.IMAGE_TRANSLATION);
        //An image is not affected by the transformations of the screen, load an identity matrix as the aspect ratio transformation
        shader.aspectRatio.loadMatrix(GeneralSettings.IDENTITY2);
        //All filled boxes in an image do not need to be clipped because only the flowchart is rendered
        shader.doClipping.loadBoolean(false);

        //For each text box in the flowchart textbox controller
        for (TextBox textBox : flowchartWindowController.getFlowchartTextBoxController().getTextBoxes()) {
            //If the text box is a flowchart text box render it's text boxes
            if (textBox instanceof FlowchartTextBox) {
                //Render the text box behind the main text
                renderFilledBox(textBox.getGuiFilledBox());
                //Render the text box behind the line numbers
                renderFilledBox(textBox.getTextNumberFilledBox());
            } else {
                //Something is wrong with setup, print an error message
                System.err.println("Undefined box rendering behavior, non flowchart text box in the flowchart text box controller");
            }
        }

        //Perform per render call cleanup which is performed regardless of which rendering method is used
        endRendering();
    }

    /**
     * Renders all filled boxes which are part of the gui. This is a separate call from #renderToImage
     * because gui filled boxes may render over the flowchart such as in an {@link TextButton text button}
     * and this involves rendering over text and flowchart lines this call must happen after those have
     * been rendered.
     * @param header the header currently used which contains a filled box as it's background
     * TODO: Switch out Header for a GUI Controller
     */
    public void renderGuis(Header header) {
        //Perform setup which is performed regardless of which rendering method is used
        prepare();

        //None of these filled boxes will be moved by the aspect ratio,
        //Or the flowchart translation, load identity matrices
        shader.aspectRatio.loadMatrix(GeneralSettings.IDENTITY2);
        shader.zoomTranslateMatrix.loadMatrix(GeneralSettings.IDENTITY3);
        shader.doClipping.loadBoolean(false);

        //Render the headers filled box
        renderFilledBox(header.getGuiFilledBox());

        //For each TextButton there is a filled box behind it
        for (Button button : ButtonController.getButtons()) {
            if (button instanceof TextButton) {
                //Render the buttons filled box
                renderFilledBox(((TextButton) button).getGuiFilledBox());
            }
        }


        //Perform per render call cleanup which is performed regardless of which rendering method is used
        endRendering();
    }

    /**
     * Renders a single filled box. Loads the color, a calculated transformation matrix to put the
     * filled box in the right location with the right scale, and renders the box.
     * @param filledBox the filled box to be rendered. Contains a three dimensional vector representing an
     * RBG color, and two dimensional vectors representing the position and size in the OpenGL coordinate
     * system.
     */
    private void renderFilledBox(GUIFilledBox filledBox) {
        //Filled boxes may have different colors, load the color for each
        shader.color.loadVec3(filledBox.getColor());

        //Create a transformation matrix in the text box coordinate system based on the
        //filled boxes size and position and load it to the shader
        shader.transformation.loadMatrix(Maths.createTransformationMatrix(filledBox.getSize(), filledBox.getPosition()));

        //Render the filled box
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
    }

    /**
     * Cleans up the memory of the {@link FilledBoxShader filled box shader}. The filled box shader has
     * a GPU program used to render saved inside of GRAM. This will not be automatically cleaned up by
     * the Java garbage collector so it must be deleted manually when this renderer will no longer be used.
     * Called automatically when the program is closed.
     */
    public void cleanUp() {
        shader.cleanUp();
    }

    /**
     * Sets the OpenGL state to be appropriate for rendering lines of formatted text and binds
     * the filled box model for rendering.
     */
    private void prepare() {
        //Disable the depth test
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        //Set the shader to be the shader in active use so it does not try to use the previous shader to render
        shader.start();

        //Bind the vertex array which contains the filled boxes information
        GL30.glBindVertexArray(square.getVaoID());
        //This vertex array has positions stored in attribute 0 and does not contain any texture coordinates
        GL20.glEnableVertexAttribArray(0);
    }

    /**
     * Unbinds the filled box model and restores the OpenGL state for another renderer to be called.
     */
    private void endRendering() {
        //Unbind the model
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        //Stop the shader program
        shader.stop();

        //Enable the depth test
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

}