import plane.complex.ComplexNumber;

import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import static java.lang.System.out;

/************************************************************************************************************************
 * <code>JuliaFractal</code> is an executable class which can create Julia set fractals for input constant ComplexNumber.
 * @author Shreyas Kulkarni
 * @version 1.0
 *
 ************************************************************************************************************************
 */

public class GenerateFractal {
    public static void main(String args[]) throws IOException {
        // Taking the Image WIDTH and HEIGHT variables. Increasing or decreasing the value will affect computation time.
        double WIDTH = 1600;
        double HEIGHT = 1200;

        Color RED = new Color(255, 0, 0);
        Color BLUE = new Color(0, 0, 255);
        Color GREEN = new Color(0, 255, 0);

        float[] hsb_red = Color.RGBtoHSB(255, 0, 0, null);
        float[] hsb_blue = Color.RGBtoHSB(0, 0, 255, null);
        float[] hsb_green = Color.RGBtoHSB(0, 255, 0, null);


        // Setting the Brightness and Saturation of every pixel to maximum
        float Saturation = 1f;
        float Brightness = 1f;

        // Creating a new blank RGB Image to draw the fractal on
        BufferedImage img = new BufferedImage((int) WIDTH, (int) HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

        // Setting the maximum iterations to 256. This can be increased if you suspect an escapee set may be found beyond this value.
        // Increasing or decreasing the value will affect computation time.
        int maxIterations = 256;

        // Looping through every pixel of image
        for (int X = 0; X < WIDTH; X++) {
            for (int Y = 0; Y < HEIGHT; Y++) {
                // Creating an empty complex number to hold the value of last z
                ComplexNumber oldz = new ComplexNumber();

                double x = (X - WIDTH / 2) / (WIDTH / 2);
                double y = (Y - HEIGHT / 2) / (HEIGHT / 2);
                ComplexNumber newz = new ComplexNumber(x, y);

                double v = (Math.sqrt(3)) / 2f;

                ComplexNumber a = new ComplexNumber(1, 0);
                ComplexNumber b = new ComplexNumber(2, 0);
                ComplexNumber c = new ComplexNumber(3, 0);
                ComplexNumber root1 = new ComplexNumber(1, 0);
                ComplexNumber root2 = new ComplexNumber(-0.5, -v);
                ComplexNumber root3 = new ComplexNumber(-0.5, v);

                // Iterating till the fixed point iteration either converges or runs out of total iterations
                int i;
                int escapee = -1;

                for (i = 0; i < maxIterations; i++) {
                    // Saving the current z in oldz
                    oldz = newz;

                    newz = ComplexNumber.add(ComplexNumber.divide(ComplexNumber.multiply(b, newz), c), ComplexNumber.divide(a, ComplexNumber.multiply(c, ComplexNumber.pow(newz, 2))));

                    // Checking if the modulus/magnitude of complex number has exceeded the radius of 2
                    // If yes, the pixel is an escapee and we break the loop
                    if ((ComplexNumber.subtract(newz, root1).mod()) < 0.0001 && (ComplexNumber.subtract(newz, root1).mod()) > -0.0001) {
                        escapee = 1;
                        break;
                    }
                    else if ((ComplexNumber.subtract(newz, root2).mod()) < 0.0001 && (ComplexNumber.subtract(newz, root2).mod()) > -0.0001) {
                        escapee = 2;
                        break;
                    }
                    else if ((ComplexNumber.subtract(newz, root3).mod()) < 0.0001 && (ComplexNumber.subtract(newz, root3).mod()) > -0.0001) {
                        escapee = 3;
                        break;
                    }
                }

                //
                float Hue = (i % 76) / (3f * 75.0f) - 1f / 12f;
                Color color;

                switch (escapee) {
                    case 1:
                        color = Color.getHSBColor(hsb_red[0] + Hue, hsb_red[1], hsb_red[2]);
                        break;
                    case 2:
                        color = Color.getHSBColor(hsb_blue[0] + Hue, hsb_blue[1], hsb_blue[2]);
                        break;
                    case 3:
                        color = Color.getHSBColor(hsb_green[0] + Hue, hsb_green[1], hsb_green[2]);
                        break;
                    default:
                        // Setting the brightness to zero since the pixel is a prisoner
                        Brightness = 0f;
                        color = Color.getHSBColor(Hue, Saturation, Brightness);
                        break;
                }

                // Creating the color from HSB values and setting the pixel to the computed color
                img.setRGB(X, Y, color.getRGB());
            }
        }
        // Saving the image with unix Timestamp appended to filename
        long unixTime = System.currentTimeMillis() / 1000L;
        ImageIO.write(img, "PNG", new File("julia" + unixTime + ".png"));
    }
}