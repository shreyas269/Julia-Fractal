import plane.complex.ComplexNumber;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
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

public class MultiJulia
{
    public static void main(String args[])throws IOException
    {
        // Taking the Image WIDTH and HEIGHT variables. Increasing or decreasing the value will affect computation time.
        double WIDTH = 1600;
        double HEIGHT = 1200;

        // Setting the Saturation of every pixel to maximum
        // This can be played with to get different results
        float Saturation = 1f;

        // Creating a new blank RGB Image to draw the fractal on
        BufferedImage img = new BufferedImage((int)WIDTH, (int)HEIGHT,BufferedImage.TYPE_3BYTE_BGR);
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Getting the constant ComplexNumber as input from the user for use in the function f(z) = z + c
        out.print("Re(c): ");
        //double cReal = Double.parseDouble(reader.readLine());
        double cReal = -0.6;
        //double cReal = -1;
        out.print("Im(c): ");
        //double cImag = Double.parseDouble(reader.readLine());
        double cImag = 0.4;
        //double cImag = 0;

        // Creating the constant complex number from input real and imaginary values
        ComplexNumber constant = new ComplexNumber(cReal,cImag);

        // Setting the maximum iterations to 256. This can be increased if you suspect an escapee set may be found beyond this value.
        // Increasing or decreasing the value will affect computation time.

        int max_iter = 256;

        // Looping through every pixel of image
        for(int X=0; X<WIDTH; X++)
        {
            for(int Y=0; Y<HEIGHT; Y++)
            {
                // Creating an empty complex number to hold the value of last z
                ComplexNumber oldz = new ComplexNumber();

                // Setting the value of z0 for every pixel
                // z0 is a function of (x,y) i.e. the pixel co-ordinates.
                // The function can be anything, but should depend on (x,y) in some way and should lie between [-1,1]
                // I use the function,
                // Re(z) = 2*(X-WIDTH/2)/(WIDTH/2)
                // Im(z) = 1.33*(Y-HEIGHT/2)/(HEIGHT/2)
                // This gives a good centered fractal.You can play around with the function to get better results.
                double x = (X-WIDTH/2)/(WIDTH/2);
                double y = (Y-HEIGHT/2)/(HEIGHT/2);
                ComplexNumber newz = new ComplexNumber(x, y );
                //ComplexNumber newz = new ComplexNumber(x - (x*x*x - 3*x*y*y) / (3*(x*x - y*y)), y - (3*x*x*y - y*y*y) / (3*(x*x - y*y)) );

                // Iterating till the orbit of z0 escapes the radius 2 or till maximum iterations are completed
                int i;
                for(i=0;i<max_iter; i++)
                {
                    // Saving the current z in oldz
                    oldz = newz;

                    // Applying the function newz = newz^2 + c, where c is the constant ComplexNumber user input
//                    newz = newz.square();
//                    newz.add(constant);

                    newz = newz.square();
                    newz.add(constant);

                    // Checking if the modulus/magnitude of complex number has exceeded the radius of 2
                    // If yes, the pixel is an escapee and we break the loop
                    if(newz.mod() > 2)
                        break;
                }

                // Checking if the pixel is an escapee
                // If yes, setting the brightness to the maximum
                // If no, setting the brightness to zero since the pixel is a prisoner
                float Brightness = i < max_iter ? 1f : 0;

                // Setting Hue to a function of number of iterations (i) taken to escape the radius 2
                // Hue = (i%256)/255.0f;
                // i%256 to bring i in range [0,255]
                // Then dividing by 255.0f to bring it in range [0,1] so that we can pass it to Color.getHSBColor(H,S,B) function
                float Hue = (i%256)/255.0f;

                // Creating the color from HSB values and setting the pixel to the computed color
                Color color = Color.getHSBColor(Hue, Saturation, Brightness);
                img.setRGB(X,Y,color.getRGB());
            }
        }
        // Saving the image
        ImageIO.write(img,"PNG", new File("julia.png"));
    }
}