package Assignment1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Question1 {

    public static class Convolution {
        private static int height = 0;
        private static int width = 0;

        public static int[][] getConvolution(int[][] img, int[][] kernel, int numberThreads) {
            int a, b;
            
            int[][] output = new int[a = img.length - kernel.length + 1][b = img[0].length - kernel[0].length + 1];
            height = img.length;
            width = img[0].length;
            int n = height * width;
            int blocksPerThread = n / numberThreads;
            int restBlocks = n % numberThreads;

            List<Thread> threadList = new ArrayList<>();
    
            for (int threadIndex = 0; threadIndex < numberThreads; threadIndex++) {
                int to = threadIndex * blocksPerThread;
                int from = to + blocksPerThread;
                //System.out.println("from: " + from + " to: " + to);
                Thread thread = null;
                if (restBlocks > 0) {
                    restBlocks--;
                    thread = new Thread(() -> {
                        for (int index = to; index < from; index++) {
                            for (int y = 0; y < a; y = y + 1) {
                                for (int x = 0; x < b; x = x + 1) {
 
                                    int sum = 0;
                                    for (int yy = 0; yy < kernel.length; yy++) {
                                        for (int xx = 0; xx < kernel[0].length; xx++) {
                                            sum += img[y + yy][x + xx] * kernel[yy][xx];
                                        }
                                    }
                                    output[y][x] = sum;
                                }
                            }
                        }
                    });
                } else {
                    thread = new Thread(() -> {
                        for (int index = to; index < from; index++) {
                            for (int y = 0; y < a; y = y + 1) {
                                for (int x = 0; x < b; x = x + 1) {

                                    int sum = 0;
                                    for (int yy = 0; yy < kernel.length; yy++) {
                                        for (int xx = 0; xx < kernel[0].length; xx++) {
                                            sum += img[y + yy][x + xx] * kernel[yy][xx];
                                        }
                                    }
                                    output[y][x] = sum;
                                }
                            }
                        }
                    });
                }
                threadList.add(thread);
                thread.start();
            }


            threadList.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.out.println("error in thread join " + e.getMessage());
                }
            });
        
            return output;
        }
            
    }



    public static class ConvolutionUtils {


        public static int[][] getMatrixFromImage(BufferedImage img) throws IOException {

            // get image's width and height
            int width = img.getWidth();
            int height = img.getHeight();
            int[][] matrix = new int[height][width];

            // convert to greyscale
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Here (x,y)denotes the coordinate of image
                    // for modifying the pixel value.
                    int avg = img.getRGB(x, y);

                    matrix[y][x] = avg;
                }
            }
            return matrix;
        }

        public static BufferedImage getMatrixFromData(int[][] img) throws IOException {

            // get image's width and height
            int width = img.length;
            int height = img[0].length;
            int arrOriginal[][]=new int[height][width];
            BufferedImage writeBackImage = new BufferedImage(img[0].length, img.length, BufferedImage.TYPE_BYTE_GRAY);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    writeBackImage.setRGB(j,i,arrOriginal[i][j]);
                }
            }
            return writeBackImage;
        }
        public static void writeOutputImage(String filename, BufferedImage img, int[][] newImg) throws IOException {

            int width = newImg.length;
            int height = newImg[0].length;
            int arrOriginal[][]=new int[height][width];
            BufferedImage writeBackImage = new BufferedImage(newImg[0].length, newImg.length, BufferedImage.TYPE_BYTE_GRAY);
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    int p = img.getRGB(x, y);

                    int avg = newImg[y][x];

                    p = avg;
                    writeBackImage.setRGB(x, y, p);
                }
            }

            // write image
            try {
                File f = new File(filename);
                ImageIO.write(writeBackImage, "jpg", f);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private static final String IMAGE_PATH = "D:/Studies/Lectures/Sem 9/Concurrent Programming/Assignment/" +
            "Assignment1/Lena.jpg";
    private static int[][] imageArray;
    private static int[][] imageArray2;
    private static final int[][] kernelIdentity = {{1, 0, 1}, {0, 1, 0}, {1, 0, 1}};

    public static void main(String[] args) {
        int convolutionNumber = 0;
        int numberOfThreads = 0;
        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter the number of convolutions u require");
        if(myObj.hasNextInt()) {
            convolutionNumber = myObj.nextInt();
            myObj.close();
        }
        else {
            System.out.println("The required convolution must be an integer number. Exiting program.");
            System.exit(0);
        }
        BufferedImage img = null;
        BufferedImage img2 = null;
        BufferedImage newBufferedImage = null;
        File f = null;
        File f2 = null;
        // read image
        try {
            f = new File(IMAGE_PATH);
            img = ImageIO.read(f);
            System.out.println(img.getWidth() + "  " + img.getHeight());
        } catch (IOException e) {
            System.out.println(e);
        }
        try
        {
            int a = 0;
            int[][] convolutedImg = null;
            BufferedImage img1 = null;
            final String RESULT_IMAGE_PATH = "D:/Studies/Lectures/Sem 9/Concurrent Programming/Assignment/" +
                    "Assignment1/convolutionImage2.jpg";
                    if(img.getWidth() >=1024){
                        numberOfThreads = 8;
                    }
                    if(img.getWidth() >=512 && img.getWidth() <=1024 ){
                        numberOfThreads = 6;
                    }
                    if(img.getWidth() >=216 && img.getWidth() <=512  ){
                        numberOfThreads = 4;
                    }
                    if(img.getWidth() >=0 && img.getWidth() <=216 ){
                        numberOfThreads = 4;
                     }

                
            imageArray = Main.ConvolutionUtils.getMatrixFromImage(img);
            while (a  < convolutionNumber) {
                if(a == 0){
                convolutedImg = Main.Convolution.getConvolution(imageArray, kernelIdentity, numberOfThreads );
                newBufferedImage = Main.ConvolutionUtils.getMatrixFromData(convolutedImg);
                Main.ConvolutionUtils.writeOutputImage(RESULT_IMAGE_PATH, newBufferedImage, convolutedImg);
                System.out.println(img.getWidth() + "  " + img.getHeight());
                a++;
            }else{
                f2 = new File(RESULT_IMAGE_PATH);
                img2 = ImageIO.read(f2);
                imageArray2 =Main.ConvolutionUtils.getMatrixFromImage(img2); 
                convolutedImg = Main.Convolution.getConvolution(imageArray2, kernelIdentity, numberOfThreads );
                newBufferedImage = Main.ConvolutionUtils.getMatrixFromData(convolutedImg);
                Main.ConvolutionUtils.writeOutputImage(RESULT_IMAGE_PATH, newBufferedImage, convolutedImg);
                System.out.println(img2.getWidth() + "  " + img2.getHeight());
              }
            }
            

        }
        catch(IOException e)
        {
            System.out.println(e);
        }

    }

}
