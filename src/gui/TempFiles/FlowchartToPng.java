package gui.TempFiles;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;
import ar.com.hjg.pngj.chunks.ChunkLoadBehaviour;



import controllers.ApplicationController;
import gui.OpenFileDialog;
import main.GeneralSettings;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import rendering.renderEngine.MasterRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;


/**
 * This class combines flowchart slices (png images) to be converted into
 * a single large image.
 * */
public class FlowchartToPng {

    private String directoryPath = null;
    private final String FOLDER_NAME = ".img" + File.separator;
    private int imageCount = 0;
    public FileSortedArrayList files = new FileSortedArrayList();
    /**
     * <pre>
     *  This value in openGL coordinates will move a screen down by one screen width
     *                         y|(0,1)
     *              (-1,0) ---- |---- x (1,0)
     *                          | (0,-1)
     *  So to move down one screen size you translate by 2
     * </pre>
     * */
    private final float MOVE_ONE_SCREEN_DOWN = 2.f;



    public FlowchartToPng() {
        //do nothign
    }

    /**
     * Set the directory path
     * Should be something like
     * Should set to GeneralSettings.USERPREF.getUserTempFileDirPath()
     * */
    public FlowchartToPng(String dirPath) {
        initializeDirectory(dirPath);
    }




    /**Starts slicing the flowchart into chunks then saves each chunk
     * */
    public void startImageSlice(int width, int height,ApplicationController controller) {
        //Clear previous files
        clearTempFiles();

        int widthSource = width;
        int heightSource = height;

        int pixelsChop = Math.round(width / 2f);
        pixelsChop = GeneralSettings.DEFAULT_HEIGHT;


        System.out.println("pixelsChop:" + pixelsChop);
        System.out.println("GeneralSettings.IMAGE_SIZE.y: " + GeneralSettings.IMAGE_SIZE.y);
        System.out.println("GeneralSettings.IMAGE_SIZE.x: " + GeneralSettings.IMAGE_SIZE.x);

        //Store the original translation
        float originalTranslationY = GeneralSettings.IMAGE_TRANSLATION.m21;

        //move down for as many openGL spaces it takes to exceed the image size by a minimal amount
        float imageSizeTemp = GeneralSettings.IMAGE_SIZE.y;

        while(imageSizeTemp > -MOVE_ONE_SCREEN_DOWN/2) {
            doRenderCall(widthSource,heightSource,pixelsChop, controller);
            GeneralSettings.IMAGE_TRANSLATION.m21 -= MOVE_ONE_SCREEN_DOWN;
            imageSizeTemp -= MOVE_ONE_SCREEN_DOWN;
        }

        //Reset the originalTranslationY
        GeneralSettings.IMAGE_TRANSLATION.m21 = originalTranslationY;

        //Go to grab files from directory and add them to file sorted array list
        attachFilesFromDir(this.directoryPath);

        //Place file paths into strings... (should really just use the file paths)
        String filesPaths [] = new String[files.size()];
        for(int i = 0; i < files.size(); i++) {
            filesPaths[i] = files.get(i).getAbsolutePath();
        }
        //Recombined files and save to file path
        doTiling(filesPaths,this.directoryPath + "out.png");
    }





    /**does a LWJGL render call for of the flowchart*/
    public void doRenderCall(int widthSource, int heightSource,int height, ApplicationController controller) {
//Create a frame buffer to render the image to
        int renderBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, renderBuffer);

        //Create a texture to load the data into
        int imageIndex = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, imageIndex);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, widthSource, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

        //Configure frame buffer
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, imageIndex, 0);
        GL30.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, renderBuffer);
        GL11.glViewport(0, 0, widthSource, height);

        //Render the flowchart to the image
        MasterRenderer.renderScreenshot(controller.getFlowchartWindowController());




        //Load the data in the frame buffer into a byte buffer which can be saved to an image
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(widthSource * height * bpp);
        //Render the flowchart to the image
        MasterRenderer.renderScreenshot(controller.getFlowchartWindowController());
        //GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL11.glReadPixels(0, 0, widthSource, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        //Find where to save the file
//        OpenFileDialog openFileDialog = new OpenFileDialog();
//        openFileDialog.setFilterList("png,jpg");
//        openFileDialog.saveFileWindow();

        //Ensure a valid file path is entered before saving
       // String path = openFileDialog.getFilePath();
        writePng(this.directoryPath+ File.separator + (imageCount++) + ".png", widthSource,height,bpp, buffer);



        //Delete the frame buffer when done
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, GeneralSettings.DISPLAY_WIDTH, GeneralSettings.DISPLAY_HEIGHT);
        GL11.glDeleteTextures(imageIndex);
        GL30.glDeleteFramebuffers(renderBuffer);
    }


    /**Writes the buffer into a image*/
    public void writePng(String path, int widthSource, int height, int bpp, ByteBuffer buffer) {

        if(path != null) {
            //Create the file
            File file = new File(path);
            String format = "PNG"; // Example: "PNG" or "JPG"
            BufferedImage image = new BufferedImage(widthSource, height, BufferedImage.TYPE_INT_ARGB);

            //Read the data from the byte buffer
            for (int x = 0; x < widthSource; x++) {
                for (int y = 0; y < height; y++) {
                    int i = (x + (widthSource * y)) * bpp;
                    int r = buffer.get(i) & 0xFF;
                    int g = buffer.get(i + 1) & 0xFF;
                    int b = buffer.get(i + 2) & 0xFF;
                    image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
                }
            }
            //Attempt to save the image
            try {
                ImageIO.write(image, format, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Opens the folder and attaches the files in order of
     * creation date to the FileSortedArrayList
     * Should set path to directoryPath
     */
    public void attachFilesFromDir(String path) {
        if (isPathExist(path)) {
            files.clear(); //Lazy I know TO BAD!
            File folder = new File(directoryPath);
            File[] listOfFiles = folder.listFiles();
            for (File in : listOfFiles) {
                files.add(in);
            }
            files.bubbleSort2();
        }
    }

    /**
     * This initializes the temp folder directory
     * Should set to GeneralSettings.USERPREF.getUserTempFileDirPath()
     * */
    public void initializeDirectory(String dirPath) {
        dirPath = dirPath + File.separator + FOLDER_NAME;
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //Should also clean the directory?
        this.directoryPath = dirPath;
    }


    /**
     * Remove all files in the temp directory
     * Warning! This will kill all files form
     * the folder it is called in.
     * You have been warned!!
     * Should only EVER be a valid temp file path
     * set initially.
     * */
    public void clearTempFiles() {
        if(isPathExist(this.directoryPath)) {
            File folder = new File(directoryPath);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    listOfFiles[i].delete();
                }
            }
        }
    }

    /**
     * Returns true or false whether a path exist
     */
    private boolean isPathExist(String path) {
        boolean result = false;
        File dir = new File(path);
        if (dir.exists()) {
            result = true;
        }
        return result;
    }



    /**
     * Do tiling combines tiles (image slices) into a single image.
     * IMAGES ONLY WORK WITH PNG FILES!
     * @param source list of source files to be combined in a single image from first (top) to last (bottom)
     * @param out the out file
     * */
    public void doTiling(String source[], String out){
        ImageInfo imageTemp, imageOut;
        int rows = 0;

        //Build readers & count rows
        PngReader pngReaders[] = new PngReader[source.length];
        for (int i = 0; i < source.length; i++) {
            pngReaders[i] = new PngReader(new File(source[i]));
            pngReaders[i].setChunkLoadBehaviour(ChunkLoadBehaviour.LOAD_CHUNK_NEVER);
            rows += pngReaders[i].imgInfo.rows;
        }

        //The image out is assumed to have the same columns as the
        //first image but the rows must be computed from all images
        imageTemp = pngReaders[0].imgInfo;
        imageOut = new ImageInfo(imageTemp.cols, rows,
                imageTemp.bitDepth, imageTemp.alpha,
                imageTemp.greyscale, imageTemp.indexed);


        //Setup out writer
        PngWriter pngoutWriter = new PngWriter(new File(out), imageOut, true);
        pngoutWriter.copyChunksFrom(pngReaders[0].getChunksList(),
                ChunkCopyBehaviour.COPY_PALETTE | ChunkCopyBehaviour.COPY_TRANSPARENCY);


        //Setup out scanline
        ImageLineInt outScanLine = new ImageLineInt(imageOut);

        //Stack images into one png
        int currentOutRow = 0;
        for (int i = 0; i < source.length; i++) {
            Arrays.fill(outScanLine.getScanline(),0);
            for(int j = 0; j < pngReaders[i].imgInfo.rows; j++, currentOutRow++) {
                ImageLineInt linein = (ImageLineInt) pngReaders[i].readRow(j);
                System.arraycopy(linein.getScanline(), 0, outScanLine.getScanline(),
                        0, linein.getScanline().length);
                pngoutWriter.writeRow(outScanLine, currentOutRow);
            }

        }
        //Close writers / readers
        for(int i = 0; i < pngReaders.length; i++) {
            pngReaders[i].end();
        }
        pngoutWriter.end();
    }







}
