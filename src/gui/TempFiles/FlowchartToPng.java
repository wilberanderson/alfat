package gui.TempFiles;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;
import ar.com.hjg.pngj.chunks.ChunkLoadBehaviour;



import controllers.ApplicationController;
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
import java.util.ArrayList;
import java.util.Arrays;


/**
 * This class combines flowchart slices (png images) to be converted into
 * a single large image.
 * */
public class FlowchartToPng {

    private String directoryPath = null;
    private String directoryPath_row = null;
    private String directoryPath_col = null;
    private final String FOLDER_NAME = ".img" + File.separator;
    private final String FOLDER_ROW = ".row" + File.separator;
    private final String FOLDER_COL = ".col" + File.separator;
    private int imageCount = 0; //Numbers the images
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
    private final float MOVE_ONE_SCREEN_RIGHT = 2.f;

    private int numberOfImageColumns = 0;

    public FlowchartToPng() {
        //do nothing
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
        clearAllTempFileFolders();
        System.out.println("Integer.MAX_VALUE "+ Integer.MAX_VALUE);

        int widthSource = width;
        int heightSource = height;

        int pixelsChop = Math.round(width / 2f);
        //pixelsChop = GeneralSettings.DEFAULT_HEIGHT;


        System.out.println("pixelsChop:" + pixelsChop);
        System.out.println("GeneralSettings.IMAGE_SIZE.y: " + GeneralSettings.IMAGE_SIZE.y);
        System.out.println("GeneralSettings.IMAGE_SIZE.x: " + GeneralSettings.IMAGE_SIZE.x);

        //Store the original translation
        float originalTranslationY = GeneralSettings.IMAGE_TRANSLATION.m21;
        float originalTranslationX = GeneralSettings.IMAGE_TRANSLATION.m20;

        //move down for as many openGL spaces it takes to exceed the image size by a minimal amount
        float imageSizeTempY = GeneralSettings.IMAGE_SIZE.y;
        float imageSizeTempX = GeneralSettings.IMAGE_SIZE.x;

        //This slicing a image from the bottom left to the right and moving up then starting at the left and repeating
        //While we can still down the image we want to take a screen shot
        while(imageSizeTempY > -MOVE_ONE_SCREEN_DOWN/2f) {
            //While we can move to the right we want to take a screen shot
            while(imageSizeTempX > 0) {
                doRenderCall(widthSource,heightSource,pixelsChop, controller, this.directoryPath_col); //Takes a screen shot
                GeneralSettings.IMAGE_TRANSLATION.m20 -= MOVE_ONE_SCREEN_RIGHT; //MOVE TO THE RIGHT
                imageSizeTempX -= MOVE_ONE_SCREEN_RIGHT;  //Adjust X size that is available
            }
            GeneralSettings.IMAGE_TRANSLATION.m21 -= MOVE_ONE_SCREEN_DOWN; //MOVE DOWN
            GeneralSettings.IMAGE_TRANSLATION.m20 = originalTranslationX; //Restore the X translation
            imageSizeTempX = GeneralSettings.IMAGE_SIZE.x; //Restore the X size that is available
            imageSizeTempY -= MOVE_ONE_SCREEN_DOWN; //Adjust the Y size that is available
        }

        //Reset the originalTranslationY & originalTranslationX Since we are done
        GeneralSettings.IMAGE_TRANSLATION.m21 = originalTranslationY;
        GeneralSettings.IMAGE_TRANSLATION.m20 = originalTranslationX;

        //Build the final image
        combinedPng("dummy", GeneralSettings.IMAGE_SIZE.x);
    }

    /**Does a LWJGL render call for of the flowchart
     *
     * */
    public void doRenderCall(int widthSource, int heightSource,int height, ApplicationController controller, String pathOut) {
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

        //Write the buffer into a png image
        writePng(pathOut + File.separator + (imageCount++) + ".png", widthSource,height,bpp, buffer);

        //Delete the frame buffer when done
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, GeneralSettings.DISPLAY_WIDTH, GeneralSettings.DISPLAY_HEIGHT);
        GL11.glDeleteTextures(imageIndex);
        GL30.glDeleteFramebuffers(renderBuffer);
    }


    /**Writes the buffer into a .png image*/
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
     * Opens the folder and attaches the files to the files sorted arrayList
     * @param path the folder path
     * @param firstToLast boolean to sort from first created to last
     *                    true = first to last, false = last to first
     */
    public void attachFilesFromDir(String path, boolean firstToLast) {
        if (isPathExist(path)) {
            files.clear(); //Lazy I know TO BAD!
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();
            for (File in : listOfFiles) {
                if(in.isFile()) {
                    files.add(in);
                }
            }
            if(firstToLast == true) {
                files.bubbleSort();
            } else {
                 files.bubbleSort2();
            }
        }
    }

    /**
     * This initializes the temp folder directory
     * Should set to GeneralSettings.USERPREF.getUserTempFileDirPath()
     * */
    public void initializeDirectory(String dirPath) {
        //Make main directory
        dirPath = dirPath + File.separator + FOLDER_NAME;
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        this.directoryPath = dirPath;

        //Make sub directories
        dirPath = dirPath + FOLDER_ROW;
        directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        this.directoryPath_row = dirPath;

        dirPath =  this.directoryPath + FOLDER_COL;
        directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        this.directoryPath_col = dirPath;
    }


    /**
     * Clears the file in .img, .row, and .col
     * */
    private void clearAllTempFileFolders() {
        clearTempFiles(this.directoryPath);
        clearTempFiles(this.directoryPath_col);
        clearTempFiles(this.directoryPath_row);
    }


    /**
     * Remove all files in the temp directory
     * Warning! This will kill all files form
     * the folder it is called in.
     * You have been warned!!
     * Should only EVER be a valid temp file path
     * set initially.
     * */
    private void clearTempFiles(String path) {
        if(isPathExist(path)) {
            File folder = new File(path);
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
     * This opens the .img folder and combined columns into rows
     * then rows into a final out image.
     * */
    public void combinedPng(String outputPath, float imageSizeX) {
        int imageColumns = getNumberOfImageColumns(imageSizeX);

        //Grab all the files from the columns directory and build them into rows
        attachFilesFromDir(this.directoryPath_col, true);
        //Get all the files paths
        String filesPaths [] = files.FilesToStringArray();

        //Recombined files and save to the rows folder
        doTilingColumnManager(filesPaths,imageColumns, this.directoryPath_row);

        //Get all the file paths from the row folder
        files.clear();
        attachFilesFromDir(this.directoryPath_row, false);
        filesPaths = files.FilesToStringArray();
        //Recombined the files rows and save to a out directory
        doTilingRow(filesPaths,this.directoryPath + "out.png");
    }




    /**
     * tile column manager. Feeds in images based on a column index.
     * <pre>
     *     Example: {"file1", "file2", "file3", "file4"}, index = 2
     *    -> 0:{"file1", "file2"} 1:{"file3", "file4"}
     *    -> call doTilingColumn
     * </pre>
     *
     * */
    public static void doTilingColumnManager(String source[],int indexSize, String outPath) {

//        for(String in : source) {
//            System.out.println(" " + in);
//        }


        ArrayList<String[]> sourceIn = new ArrayList<String[]>();
        String [] sourcesInArray = null;
        int indexTemp = 0;
        //Add all the source files into array of strings that are the length of the index size
        for (int i = 0; i < source.length; i++) {
            if (indexTemp == 0) {
                sourcesInArray = new String[indexSize];
            }
            sourcesInArray[indexTemp] = source[i];

            if (indexTemp == indexSize-1) {
                indexTemp = 0;
                sourceIn.add(sourcesInArray);
            } else {
                indexTemp++;
            }
        }
        //Add the leftover which there should not be any...
        if(indexTemp != 0) {
            String [] keep = new String[indexTemp];
            int i = 0;
            for (String isValid : sourcesInArray) {
                if(isValid != null) {
                    keep[i++] = isValid;
                }
            }
            sourceIn.add(keep);
        }

//		System.out.println("indexSize" + indexSize);
//		for(String[] strarr : sourceIn) {
//			for(String str : strarr) {
//				System.out.print(str + "\t");
//			}
//			System.out.println("");
//		}


        //Start converting th columns into rows
        int outIntex = 0;
        for(String[] str : sourceIn) {
            doTilingColumn(str,outPath + File.separator + (outIntex++) + ".png");
        }

    }



    /**
     * Do tiling row combines rows of images into a single image. The sizes of the rows MUST have equal heights and widths.
     * IMAGES ONLY WORK WITH PNG FILES!
     * @param source list of source files to be combined in a single image from first (top) to last (bottom)
     * @param out the out file path
     * */
    public void doTilingRow(String source[], String out){
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


    /**
     * Do tiling column combines columns of images into a single image.
     * The sizes of the heights MUST be equal.
     * @param  source list of source files to be combined in a single image from left to right
     * @param  out the out file path
     * */
    public static void doTilingColumn(String source[], String out){
        ImageInfo imageTemp, imageOut;
        int cols = 0;

        //Build readers & count rows
        PngReader pngReaders[] = new PngReader[source.length];
        for (int i = 0; i < source.length; i++) {
            pngReaders[i] = new PngReader(new File(source[i]));
            pngReaders[i].setChunkLoadBehaviour(ChunkLoadBehaviour.LOAD_CHUNK_NEVER);
            cols += pngReaders[i].imgInfo.cols;
        }

        //The image out is assumed to have the same rows as the
        //first image but the cols must be computed from all images
        imageTemp = pngReaders[0].imgInfo;
        imageOut = new ImageInfo(cols, imageTemp.rows,
                imageTemp.bitDepth, imageTemp.alpha,
                imageTemp.greyscale, imageTemp.indexed);


        //Setup out writer
        PngWriter pngoutWriter = new PngWriter(new File(out), imageOut, true);
        pngoutWriter.copyChunksFrom(pngReaders[0].getChunksList(),
                ChunkCopyBehaviour.COPY_PALETTE | ChunkCopyBehaviour.COPY_TRANSPARENCY);


        //Setup out scanline
        ImageLineInt outScanLine = new ImageLineInt(imageOut);

        //Copy row by row for each image
        for (int i = 0; i < imageOut.rows; i++) {
            Arrays.fill(outScanLine.getScanline(),0);
            ImageLineInt linein;
            for(int j = 0; j < source.length; j++) {
                linein = (ImageLineInt) pngReaders[j].readRow(i);
                System.arraycopy(
                        linein.getScanline(),
                        0,
                        outScanLine.getScanline(),
                        linein.getScanline().length * j,
                        linein.getScanline().length);

            }
            pngoutWriter.writeRow(outScanLine, i);
        }

        //Close writers / readers
        for(int i = 0; i < pngReaders.length; i++) {
            pngReaders[i].end();
        }
        pngoutWriter.end();
    }


    /**
     * This approximates the number of image columns it takes
     * to capture the full images width. This is done by
     * subtracting the imageSizeX by the move_right_amount
     * and the int it returns is the number of images needed
     * to represent a full row.
     * */
    private int getNumberOfImageColumns(float imageSizeX) {
        int result = 0;
        while (imageSizeX > 0) {
            imageSizeX -= MOVE_ONE_SCREEN_RIGHT;
            result++;
        }
        return result;
    }

}