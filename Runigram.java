import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);

		image = flippedVertically(tinypic);
		System.out.println();
		print(image);

		image = grayScaled(tinypic);
		System.out.println();
		print(image);

		image = scaled(tinypic, 3, 5);
		System.out.println();
		print(image);
		
		//// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int red = in.readInt();
                int green = in.readInt();
                int blue = in.readInt();
                image[row][col] = new Color(red, green, blue);
            }
        }
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		int numCols = image[0].length;
		int numRows = image.length;
		for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
              print(image[row][col]);
			  
            }
			System.out.println();
        }
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		int numCols = image[0].length;
		int numRows = image.length;
		Color[][] newimg = new Color[numRows][numCols];
		for (int row = 0; row < (numRows); row++) {
            for (int col = 0; col < numCols; col++) {
            newimg[row][col] = image[row][numCols-1-col];		  
            }
		}
		return newimg;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		int numCols = image[0].length;
		int numRows = image.length;
		Color[][] newimg = new Color[numRows][numCols];
		for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
             newimg[row][col] = image[numRows-1-row][col];		  
            }
		}
		return newimg;
	}
	
	private static Color luminance(Color pixel) {
		int lum = (int)((0.299 * pixel.getRed()) + (0.587 * pixel.getGreen()) + (0.114 * pixel.getBlue()));
		Color newcolor = new Color(lum, lum, lum);
		return newcolor;
	}
	
	
	public static Color[][] grayScaled(Color[][] image) {
		int numCols = image[0].length;
		int numRows = image.length;
		Color[][] newimg = new Color[numRows][numCols];
		for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
             newimg[row][col] = luminance(image[row][col]);		  
            }
		}
		return newimg;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		int numRows = image.length;   
		int numCols = image[0].length; 
		Color[][] newimg = new Color[height][width];
		double rowScale = (double) numRows / height;
		double colScale = (double) numCols / width;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int origRow = (int) (i * rowScale);
				int origCol = (int) (j * colScale);
				newimg[i][j] = image[origRow][origCol];
			}
		}
	
		return newimg;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int v1 = (int) (alpha * c1.getRed() + (1-alpha) * c2.getRed());
		int v2 = (int) (alpha * c1.getGreen() + (1-alpha) * c2.getGreen());
		int v3 = (int) (alpha * c1.getBlue() + (1-alpha) * c2.getBlue());
		Color newc = new Color(v1, v2, v3);
		return newc;
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		Color[][] newimg = new Color[image1.length][image1[0].length];
		for (int i = 0; i < image1.length; i++) {
			for (int j = 0; j < image1[0].length; j++) {
				newimg[i][j] = blend(image1[i][j], image2[i][j], alpha);
			}
		}
		return newimg;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		target = scaled(target, source.length, source[0].length);
		Double alpha = 0.0;
		for (int i=0; i<n; i++) {
			alpha = (double) (n-i) / n;
			source = blend(source, target, alpha);
			setCanvas(source);
			StdDraw.pause(1000);
		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

