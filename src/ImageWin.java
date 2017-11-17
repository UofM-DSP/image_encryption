import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.BufferedReader;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


class ImageWin {

	/** Image Display panel */
	//private ImageRead panel;
      private  BufferedImage image, enc_image ;

	/** Image encrypter class */
	private ImageEncrypt encrypter;
	
	/** Internal filename */
	//private String fileName="C:\\Users\\wasif.DANTA-02\\Documents\\NetBeansProjects\\Java_image_encryption_test\\images\\sample\\flower3.jpg";
       public File fileName1;
       private String fileName;
       

	/** Constructor */
	public ImageWin(){
            try {
                //fileName="";
                String file_name="E:\\project_v6\\matconvnet-1.0-beta16\\image_dataset\\mnist_png\\training\\0_labels_training.txt";
                File file = new File(file_name);
                BufferedReader reader = null;
                reader = new BufferedReader(new FileReader(file));
                String tempString;
                int i=0;
            //  while ((tempString = reader.readLine()) != null) {
             for(i=1;i<=25000;i++)
              {
                     fileName="";
                   // myVector.add(tempString);
                  
               // if(i==5)break;
                    
                  //  System.out.println(tempString);
                 tempString="im"+i+".jpg";
                    fileName="E:\\project_v6\\matconvnet-1.0-beta16\\image_dataset\\flickr_25k_bgr\\"+tempString;
                   System.out.println( "file name:"+fileName);
                   image=imageFromFile(fileName);
              encrypter = new ImageEncrypt();
                  try {
                    enc_image= encrypter.map(image,true,false);
                  String directory="E:\\img\\flickr_25k_backgournd_enc\\"+tempString;
                   ImageIO.write(enc_image, "jpg", new File(directory));
                   // i++;
                    
                    } 
                catch (Exception ex) 
                    {
                    Logger.getLogger(ImageWin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
              
             
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ImageWin.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (IOException ex) {
                Logger.getLogger(ImageWin.class.getName()).log(Level.SEVERE, null, ex);
            }         

	}

	
	private BufferedImage imageFromFile(String file){
    
               
		
		//System.out.println("Loading File ... " + file);

		BufferedImage img = null;
		try{
                     img = ImageIO.read(new File(file));
			//img = ImageIO.read(file);
		}catch(Exception e){
			System.out.println("Error:" + e);
		}
		
		return img;
	}
			

	
	/** Main function **/
	public static void main(String args[]) 
	{		
		ImageWin win = new ImageWin();
         // read_directory();
                
		
	}
        
        //read directory files
        public static void read_directory()
        {
             File folder = new File("E:\\project_v6\\matconvnet-1.0-beta16\\image_dataset\\flickr_25k");
           File[] listOfFiles = folder.listFiles();
          String out_put_file_1="E:\\project_v6\\matconvnet-1.0-beta16\\image_dataset\\flickr_25k_labels"+".txt";
            
            //File file = new File("k_nearest_neighbour_anonymized.txt");
             File file1 = new File(out_put_file_1);
           // file1.createNewFile();
           
            try {
                // creates a FileWriter Object
                FileWriter writer1 = new FileWriter(file1);
                 for (int i = 0; i < listOfFiles.length; i++)
        {
          if (listOfFiles[i].isFile())
          {
            System.out.println( listOfFiles[i].getName());
            String s=listOfFiles[i].getName();
            writer1.write(s+""+"\n");
          } 
        }
            } catch (IOException ex) {
                Logger.getLogger(ImageWin.class.getName()).log(Level.SEVERE, null, ex);
            }

       
            
        }
}

