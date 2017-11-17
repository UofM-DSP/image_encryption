
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.util.Random;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import java.math.BigInteger;
import javax.imageio.ImageIO;

class ImageEncrypt{

	boolean verbose=false;

	Random generator;

	Cipher cipher;
	SecretKeySpec skeySpec;

	/** Constructor */
	ImageEncrypt() {
	
		try{
			// Used for noise
			generator = new Random();
		   
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
    	 	kgen.init(128); 

       		SecretKey skey = kgen.generateKey();
			byte[] raw = skey.getEncoded();
       		skeySpec = new SecretKeySpec(raw, "AES");

	       	cipher = Cipher.getInstance("AES/ECB/NoPadding");
		
		}catch(Exception e){ System.out.println("ERROR: " + e);}

	}

	/** Creates a new blank image 
	 * @param fill the fill color
	 * @param w the width of the image
	 * @param h the height of the image
	 * @return an image filled with a solid color
	 */
	public BufferedImage blankImage(Color fill, int w, int h){
	
		int color = fill.getRGB();

		BufferedImage image = new BufferedImage(w,h,
			BufferedImage.TYPE_INT_RGB);
		
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){
				image.setRGB(i,j,color);
			}
		}
	
		return image;
	}

	/** Noise an image **/
	public BufferedImage addNoise(BufferedImage image){
		
		BufferedImage encImage = new BufferedImage(
			image.getWidth(),
			image.getHeight(),
			BufferedImage.TYPE_INT_RGB);

		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){
				encImage.setRGB(i,j,noiseRGB(image.getRGB(i,j)));
			}
		}
		return encImage;
	}

	/** add noise to an integer **/
	public int noiseRGB(int val){
		int encVal = val + generator.nextInt(10000);
		return encVal;
	}

	/** Set the key **/
	public void setKey(byte [] key){

		skeySpec = new SecretKeySpec(key,"AES");
	}

	byte [] getKey(){ return skeySpec.getEncoded();}
	
	/** Encrypt an Image **/
	public BufferedImage map(BufferedImage image,boolean encrypt,boolean trick) throws Exception{
		

		// Test if the coordinates are O.K.
		if(image.getWidth() % 2 != 0 || image.getHeight() % 2 != 0){
			throw(new Exception("Image size not multiple of 2 :("));
		}

		BufferedImage encImage = new BufferedImage(image.getWidth(),image.getHeight(),
			BufferedImage.TYPE_INT_RGB);

		if(encrypt){
			//System.out.println("Encrypting Image ... trick=" + trick);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		}else{ 
			System.out.println("Decrypting Image ... trick=" + trick);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		}

		for(int x=0;x<image.getWidth(); x+=2){
			for(int y=0;y<image.getHeight(); y+=2){
				
				if(verbose) System.out.println("Block: (" + x+","+y+") -----");

				int counter =0;
				byte [] pixelBytes = new byte[16];

				// Loop through internal block
				for (int i=0;i<2;i++){ 
     				for (int j=0;j<2;j++){
						int val = image.getRGB(x+i,y+j);
						
						// Where is the java t. op?
						if(trick && encrypt) val +=x*y;

						byte [] sub  = intToByteArray(val);
						
						if(verbose){ 
							System.out.println("Val: " + val + " Bytes: ");
							printByteArray(sub);
						}

						for(int k=0;k<4;k++) pixelBytes[(counter)*4+k] = sub[k];
						counter++;
					} 
				}
				
				// Cipher 
				byte [] enc = cipher.doFinal(pixelBytes);
				
				if(verbose){
					System.out.println("Block to encode:");
					printByteArray(pixelBytes);

					System.out.println("Cipher:");
					printByteArray(enc);
				}

				counter =0;
				
				// Re-encode the new image
				for (int i=0;i<2;i++){ 
     				for (int j=0;j<2;j++){
						byte [] sub = new byte[4];
						for(int k=0;k<4;k++) sub[k] = enc[(counter)*4+k];
						
						int val = byteArrayToInt(sub);
						if(trick && !encrypt) val -=x*y;

						encImage.setRGB(x+i,y+j,val);

						counter++;
					} 
				}
			}
		}
               //  ImageIO.write(bi, "png", new File("./image.png"));
		return encImage;
	}


	
 	public static final byte[] intToByteArray(int value) 
	{
             return new byte[] {
                     (byte)(value >>> 24),
                     (byte)(value >>> 16),
                     (byte)(value >>> 8),
                     (byte)value};
    }

 	public static final int byteArrayToInt(byte [] b) 
	{
    	     return (b[0] << 24)
                     + ((b[1] & 0xFF) << 16)
                     + ((b[2] & 0xFF) << 8)
                     + (b[3] & 0xFF);
    }

	public static void printByteArray(byte [] array)
	{
		System.out.print("{");
		for(int i=0;i<array.length;i++)
			System.out.print(" " + array[i]);
		System.out.println(" }");

	}

}


