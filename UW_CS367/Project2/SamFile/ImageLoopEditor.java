///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Assignment 2
// Files:            ImageLoopeEditor.java,EmptyLoopException.java,LinkedLoop.java,LinkedLoopIterator.Java
// Semester:         CS 367-4 SFall 2017
//
// Author:           Sam Adams
// Email:            sajadams@gmail.com
// CS Login:         sjadams3
// Lecturer's Name:  Dr Charles Fischer
// Lab Section:      (Section 4
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
//                   CHECK ASSIGNMENT PAGE TO see IF PAIR-PROGRAMMING IS ALLOWED
//                   If pair programming is allowed:
//                   1. Read PAIR-PROGRAMMING policy (in cs302 policy) 
//                   2. choose a partner wisely
//                   3. REGISTER THE TEAM BEFORE YOU WORK TOGETHER 
//                      a. one partner creates the team
//                      b. the other partner must join the team
//                   4. complete this section for each program file.
//
// Pair Partner:     Mike Huang
// Email:            miked.huang@gmail.com
// CS Login:         zhuang335
// Lecturer's Name:  Dr. Charles Fischer
// Lab Section:      Section 4
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////
//                   must fully acknowledge and credit those sources of help.
//                   Instructors and TAs do not have to be credited here,
//                   but tutors, roommates, relatives, strangers, etc do.
//
// Persons:          Identify persons by name, relationship to you, and email.
//                   Describe in detail the the ideas and help they provided.
//
// Online sources:   avoid web searches to solve your problems, but if you do
//                   search, be sure to include Web URLs and description of 
//                   of any information you find.
//////////////////////////// 80 columns wide //////////////////////////////////


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/********************************
 * 
 This class implements a GUI-based editor for an Image Loop editor.
 A LinkedLoop<Image> named loop is declared.
 You must complete the code in the methods named pushXXX
 to implement the individual editor commands.
 <p> Bugs: No known bugs
 @uathor Sam Adams
 ********************************/
public class ImageLoopEditor {

	protected static LoopADT<Image> loop = new LinkedLoop<Image>();



   // Methods that implement the GUI buttons' actions
	/**
	 * Iterates the loop until an image with a title that contains the specified string is found. 
	 *
	 * @param (title) The string to search for
	 * @return If image is found, returns the current context string of the loop. If empty, returns 'no images'. 
	 * If no image found, returns 'not found'.
	 */
    static String pushFind(String title){
            // Add code here to implement this GUI button
    	String strRet="";
    	try {
			Image tmpImage = loop.getCurrent();
			Iterator<Image> loopIter = loop.iterator();
			strRet="not found\n";	//mzh - add next line
	    	while(loopIter.hasNext()) {
	    		if(tmpImage.getTitle().contains(title)) {
	    			strRet=getCurrentContext();
	    			break;
	    		}
	    		tmpImage=loopIter.next();
	    		loop.next();
	    	}
		} catch (EmptyLoopException e) {
			strRet = "no images\n"; //mzh - add next line
    		return strRet;
		}
    	
    	return strRet;
    }

	/**
	 * Saves the currently loaded images into a text file for later loading. 
	 *
	 * @param (filename) - The filename to save the text file as
	 * @return If unable to save, returns 'unable to save'. If no images exist, returns 'no images to save'. If filename already exists,
	 * returns 'Warning: file already exists, will be overwritten'. Otherwise returns the empty string.
	 */
    static String pushSave(String filename){
            // Add code here to implement this GUI button
    	String strRet="";
    	if (loop.size()==0) {
    		strRet = "no images to save\n"; //mzh - add next line
    		return strRet;
    	}
    	File saveFile = new File(filename);
    	if (saveFile.exists()) {
    		strRet = "Warning: file already exists, will be overwritten\n"; //mzh - add next line
    	}
    	/*if(!saveFile.canWrite()) {
    		strRet = "unable to save - cannot write";
    		return strRet;
    	}
    	*/
    	PrintStream outFile;
		try {
			outFile = new PrintStream(saveFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			strRet = "unable to save\n"; //mzh - add next line
			return strRet;
		}
		
		loop.previous(); // mzh - added this to loop back 1 round so next will print the current file
    	Iterator<Image> loopIter = loop.iterator();
    	while(loopIter.hasNext()) {
    		Image tmpImage = loopIter.next();
    		outFile.println(tmpImage.getFile()+" "+tmpImage.getDuration()+" \""+tmpImage.getTitle()+"\""); 
    	}
    	outFile.close();
    	return strRet;
    }

	/**
	 * Loads the given file into the loop data structure
	 *
	 * @param (filename) The filename to load from
	 * @return If unable to load, returns 'unable to load'. If an image file does not exist, returns a strong warning that file does not exist.
	 * Once complete, returns 'Image load finished'.
	 */
    static String pushLoad(String filename){
            // Add code here to implement this GUI button
    	
    	File srcFile = new File(filename);
    	if (!srcFile.exists()|| !srcFile.canRead()) {
    		return "unable to load\n"; //mzh - add next line
    	}
    	String strRet = "";
    	try {
    		Scanner fileIn = new Scanner(srcFile);
    		while(fileIn.hasNext()) {
    			String line = fileIn.nextLine();
    			String[] tokens = line.split("\"?( |$)(?=(([^\"]*\"){2})*[^\"]*$)\"?"); //split by spaces but only if they aren't in double quotes
    			Integer argCount = tokens.length;
    			String imageFileName = tokens[0];
    			Integer duration = Integer.parseInt(tokens[1]);
    			String title = "";
    			if (argCount>2) {
    				title=tokens[2];
    			}
    			File imageFile = new File("images/"+imageFileName);
    			if (!imageFile.exists()) {
    				strRet = strRet+"Warning: "+imageFileName+" is not in images folder\n";
    				continue;
    			}
    			pushAddImage(imageFileName);
    			loop.getCurrent().setDuration(duration);
    			loop.getCurrent().setTitle(title);
    			//loop.add(tmpImg);
    		}
    		fileIn.close();
    	}
    	catch (FileNotFoundException ex) {
    		return "unable to load\n"; //mzh - add next line
    	} catch (EmptyLoopException e) {
			return "unable to load\n"; //mzh - add next line
		}
    	strRet = strRet+"Image load finished\n";
    	loop.next(); //return loop to first loaded item
    	return strRet;
    	
    }

	  // add new image AFTER current image
	/**
	 * Places an image after the current image and makes this new image the current image.
	 *
	 * @param (filename) The image filename to insert
	 * @return If the image file cannot be found, returns a warning that it could not be loaded. Otherwise, returns the current context of the loop.
	 * If no image found, returns 'not found'.
	 */
    static String pushAddImage(String filename){
            // Add code here to implement this GUI button
    	String strRet="";
    	
    	File imageFile = new File("images/"+filename);
		if (!imageFile.exists()) {
			strRet = "Warning: "+filename+" is not in images folder\n";
			return strRet; // mzh - added return statement
		}
		Image tmpImg = new Image(filename);
		loop.next();
		loop.add(tmpImg);
		strRet = getCurrentContext(); // mzh - set strRet to getCurrentContext
    	return strRet;
    }

	  // insert new image BEFORE current image
	/**
	 * Places an image before the current image and makes this new image the current image.
	 *
	 * @param (filename) The image filename to insert
	 * @return If the image file cannot be found, returns a warning that it could not be loaded. Otherwise, returns the current context of the loop.
	 * If no image found, returns 'not found'.
	 */
    static String pushInsertImage(String filename){
            // Add code here to implement this GUI button
    	String strRet="";
    	
    	File imageFile = new File("images/"+filename);
		if (!imageFile.exists()) {
			strRet = "Warning: "+filename+" is not in images folder\n";
			return strRet; // mzh - added return statement
			
		}
		Image tmpImg = new Image(filename);
		loop.add(tmpImg);
		strRet = getCurrentContext(); // mzh - set strRet to getCurrentContext
    	return strRet;
    }

	/**
	 * Iterators through the loop data structure based on the inputted integer.
	 *
	 * @param (count) - An integer that tells us how many times to iterate through the loop and the direction that we should iterate.
	 * @return If no images exist in the loop, returns 'no images'. If input is not valid, returns 'Invalid jump cojunt'. Otherwise returns the
	 * current loop context after the iteration completes.
	 */
	static String pushJump(String count) {
		// Add code here to implement this GUI button
		String strRet = "";
		if (loop.isEmpty()) {
			strRet = "no images\n"; //mzh - add next line
			return strRet;
		}
		try {
			Integer cnt = Integer.parseInt(count);
			if (cnt > 0) {
				while (cnt > 0) {
					loop.next();
					cnt--;
				}
			} else if (cnt < 0) {
				while (cnt < 0) {
					loop.previous();
					cnt++;
				}
			}
			strRet = getCurrentContext();
		} catch (NumberFormatException ex) {
			strRet = "Invalid jump count\n";  //mzh - add next line
			return strRet;
		}
		return strRet;
	}
	/**
	 * Updates the current image's duration
	 *
	 * @param (title) The new duration for the current image
	 * @return If there's no images in the loop, returns the string 'no images'. If duration is not a valid integer, returns 'Must enter valid integer'. 
	 *  Otherwise, returns the current loop context.
	 */
    static String pushUpdate(String title){
            // Add code here to implement this GUI button
    	String strRet="";
		try {
			Integer time = Integer.parseInt(title);
			loop.getCurrent().setDuration(time);
			getCurrentContext();
		} catch (EmptyLoopException e) {
			strRet = "no images\n"; //mzh - add next line
			return strRet;
		} catch (NumberFormatException ex) {
			strRet = "Must enter valid integer\n"; //mzh - add next line
			return strRet;
		}
		strRet = getCurrentContext();
    	return strRet;
    }
	/**
	 *Updates the title of the current image
	 *
	 * @param (title) The new title of the current image
	 * @return If there's no image in the loop, returns the string 'no images'. Otherwisek returns the current loop context.
	 */
    static String pushEdit(String title){
            // Add code here to implement this GUI button
    	String strRet="";
    	String cleanTitle;
    	
    	// mzh - added "" parsing for title
    	if(title.contains("\"")) {
    		cleanTitle = title.replace("\"", "");
		}
    	else{
    		cleanTitle = title;
    	}
    	
    	try {
			loop.getCurrent().setTitle(cleanTitle); // mzh - change title to cleanTitle
		} catch (EmptyLoopException e) {
			strRet = "no images\n"; //mzh - add next line
			return strRet;
		}
    	strRet = getCurrentContext();
    	return strRet;
    }

	/**
	 * Displays a full list of the loop data structure, starting with the current image.
	 *
	 * @return Returns each image in the form "title [duration, filename]"
	 */
    static String pushDisplay(){
            // Add code here to implement this GUI button
    	String strRet="";
    	Iterator<Image> loopIter = loop.iterator();
    	Image tmpImage;
		try {
			tmpImage = loop.getCurrent();
		} catch (EmptyLoopException e) {
			strRet = "no images";
    		return strRet;
		}
    	while(loopIter.hasNext()) {
    		if (tmpImage.getTitle().length()==0) {
    			strRet = strRet+"["+tmpImage.getDuration()+","+tmpImage.getFile()+"]\n";	
    		}
    		else {
    		strRet = strRet+tmpImage.getTitle()+" ["+tmpImage.getDuration()+","+tmpImage.getFile()+"]\n";
    		}
    		tmpImage = loopIter.next();
    	}
    	return strRet;
    }

	/**
	 * Creates a window for displaying the current image in the loop for the specfiied duration. The title will be included in the window.
	 *
	 * @return Returns ';no images' string if there are no images in the loop. Otherwise returns no string and s=display current image.
	 */
    static String pushShow(){
            // Add code here to implement this GUI button
    	String strRet="";
    		try {
				loop.getCurrent().displayImage();
			} catch (EmptyLoopException e) {
				strRet = "no images";
				return strRet;
			}
    	return strRet;
    }

	/**
	 * Displays all of the images in the loop in a window sequentially based on their durations.
	 *
	 * @return Returns the string 'no images' if the loop is empty. Otherwise, displays each image.
	 */
    static String pushTest(){
            // Add code here to implement this GUI button
    	String strRet="";
    	Iterator<Image> loopIter = loop.iterator();
    	List<Image> listLoop = new LinkedList<Image>();
    	Image tmpImage;
		try {
			tmpImage = loop.getCurrent();
		} catch (EmptyLoopException e) {
    		strRet = "no images\n"; //mzh - add next line
    		return strRet;
		}
    	while(loopIter.hasNext()) {
    		listLoop.add(tmpImage);
    		tmpImage = loopIter.next();
    	}
    	Image.displayImageList(listLoop);
    	return strRet;
    }

	/**
	 * Removes the current image
	 *
	 * @return If the loop has zero or one image, returns the string 'no images'. Otherwise, returns the current loop context after removing the current image.
	 */
    static String pushRemove(){
            // Add code here to implement this GUI button
    	String strRet="";
    	try {
			loop.removeCurrent();
		} catch (EmptyLoopException e) {
    		strRet = "no images\n"; //mzh - add next line
    		return strRet;
		}
    	strRet=getCurrentContext();
    	return strRet;
    }

	/**
	 * Iterates the loop one image forward
	 *
	 * @return The current loop context.
	 */
    static String pushForward(){
            // Add code here to implement this GUI button
    	String strRet="";
    	loop.next();
    	strRet = getCurrentContext();
    	return strRet;
    }

	/**
	 * Iterates the loop one image backward
	 *
	 * @return The current loop context.
	 */
    static String pushBack(){
            // Add code here to implement this GUI button
    	String strRet="";
    	loop.previous();
    	strRet = getCurrentContext();
    	return strRet;
    }

	/**
	 * Displays help information
	 *
	 * @return A string explaining the various commands.
	 */
  static String pushHelp(){
		// You may use this method as implemented here
	  String cmds = "";
	  cmds +="Available commands:\n" +
		"\tSave image loop into filename\n" +
		"\tLoad image loop from filename\n" +
		"\tAdd Image at filename after current image\n" +
		"\tInsert Image at filename before current image\n" +
		"\tFind image matching title\n" +
		"\tUpdate display time of current image\n" +
		"\tEdit title of current image\n" +
		"\tJump count images\n" +
		"\tDisplay loop\n" +
		"\tShow current image in a window\n" +
		"\tTest image loop by showing all images\n" +
		"\tRemove current image from loop\n" +
		"\tMove current image forward one step\n" +
		"\tMove current image back one step\n" +
		"\tHelp on available commands\n" +
		"\tQuit and close this GUI\n" ;
   return cmds;
  }

	/**
	 * Exits the program
	 *
	 * @return Empty string
	 */
  static String pushQuit(){
		// You may use this method as implemented here
        System.exit(0);
	      return "";
  }
  
	/**
	 * Generates a string that summarizes the loop's curent context by showing the current, previous, and next items in the loop.
	 *
	 * @return A string as specified in the assignment specifications. If loop is empty, it returns 'no images'. Otherwise, it returns
	 * the previous, current, and next images if they exist. If only 2 images exist, returns only the current and next image. If only
	 * 1 image exists, it returns only the current image.
	 */
  static String getCurrentContext() { //helper function that returns the current context. reused in a variety of push methods
		String strRet = "";
		try {
			Image tmpImage = loop.getCurrent();
			if (loop.size() == 1) {
				strRet = "-->\t" + tmpImage.toString()+" <--\n";
			} else if (loop.size() == 2) {
				loop.next();
				Image tmpImageNext = loop.getCurrent();
				loop.previous();
				strRet = "-->\t" + tmpImage.toString()+ " <--\n";
				strRet = strRet + "\t"+tmpImageNext.toString() + "\n";
			} else {
				loop.previous();
				Image tmpImagePrev = loop.getCurrent();
				loop.next();
				loop.next();
				Image tmpImageNext = loop.getCurrent();
				loop.previous();
				strRet = "\t"+tmpImagePrev.toString()+ "\n";
				strRet = strRet + "-->\t"+tmpImage.toString() + " <--\n";
				strRet = strRet + "\t"+ tmpImageNext.toString() + "\n";
			}
		} catch (EmptyLoopException e) {
			strRet = "no images";
			return strRet;
		}
		return strRet;
  }


	/********************************
	  The following method actually implements our GUI.
		Major components are buttons and text fields.
		The components are defined and placed (in terms of pixels).
		You should not change any of this unless you really know what you
		are doing.
		Each button has an "action listener."
		When you push a button, the action listener calls a
		pushXXX method that YOU must define.
	*********************************/
public static void runGUI() {

    //f is the JFrame that will be our GUI
    JFrame f=new JFrame("Image Loop");
    // We define the buttons and text areas that will fill the GUI
    // The locations of each component are set, in terms of pixels
    final JTextField tf1=new JTextField("");
    JTextArea ta = new JTextArea();
    ta.setTabSize(4);
    ta.setBounds(50,450,370,300);
    JButton b1=new JButton("Save");
    b1.setBounds(50,25,110,30);
    tf1.setBounds(170,25, 160,30);
    tf1.setText("filename");
    JButton b2=new JButton("Load");
    b2.setBounds(50,60,110,30);
    final JTextField tf2=new JTextField("");
    tf2.setBounds(170,60, 160,30);
    tf2.setText("filename");
    JButton b3=new JButton("Add after");
    b3.setBounds(50,95,110,30);
    final JTextField tf3=new JTextField("");
    tf3.setBounds(170,95, 150,30);
    tf3.setText("filename of image");
    JButton b4=new JButton("Insert before");
    b4.setBounds(50,130,110,30);
    final JTextField tf4=new JTextField("");
    tf4.setBounds(170,130, 150,30);
    tf4.setText("filename of image");
    JButton b5=new JButton("Find");
    b5.setBounds(50,165,110,30);
    final JTextField tf5=new JTextField("");
    tf5.setBounds(170,165, 150,30);
    tf5.setText("title");
    JButton b6=new JButton("Update");
    b6.setBounds(50,200,110,30);
    final JTextField tf6=new JTextField("");
    tf6.setBounds(170,200, 150,30);
    tf6.setText("time");
    JButton b7=new JButton("Edit");
    b7.setBounds(50,235,110,30);
    final JTextField tf7=new JTextField("");
    tf7.setBounds(170,235, 150,30);
    tf7.setText("title");
    JButton b8=new JButton("Jump");
    b8.setBounds(50,270,110,30);
    final JTextField tf8=new JTextField("");
    tf8.setBounds(170,270, 150,30);
    tf8.setText("count");
    JButton b9=new JButton("Display loop");
    b9.setBounds(50,305,110,30);
    JButton b10=new JButton("Show image");
    b10.setBounds(170,305,110,30);
    JButton b11=new JButton("Test loop");
    b11.setBounds(50,340,110,30);
    JButton b12=new JButton("Remove image");
    b12.setBounds(170,340,120,30);
    JButton b13=new JButton("Move forward");
    b13.setBounds(50,375,110,30);
    JButton b14=new JButton("Move backward");
    b14.setBounds(170,375,110,30);
    JButton b15=new JButton("Quit");
    b15.setBounds(50,410,110,30);
    JButton b16=new JButton("Help");
    b16.setBounds(170,410,110,30);


//  The effect of pushing a GUI button is defined in an ActionListener
//  The actionPerformed method is executed when the associated button is pushed

    b1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushSave(tf1.getText()));
        }
    });

    b2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
           ta.setText(pushLoad(tf2.getText()));
        }
    });

    b3.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushAddImage(tf3.getText()));
        }
    });

    b4.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushInsertImage(tf4.getText()));
        }
    });

    b5.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushFind(tf5.getText()));
        }
    });

    b6.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushUpdate(tf6.getText()));
        }
    });

    b7.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushEdit(tf7.getText()));
        }
    });

    b8.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushJump(tf8.getText()));
        }
    });


    b9.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushDisplay());
        }
    });


    b10.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){

     ta.setText(pushShow());
         }
    });


    b11.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushTest());
        }
    });

    b12.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushRemove());
        }
    });


    b13.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushForward());
        }
    });

    b14.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushBack());
        }
    });


    b15.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushQuit());
        }
    });


    b16.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
            ta.setText(pushHelp());
        }
    });

    //Buttons and text frames are added to the JFrame to build the GUI

    f.add(b1);f.add(tf1); f.add(ta);
    f.add(b1);f.add(tf1); f.add(ta);
    f.add(b1);f.add(tf1); f.add(ta);
    f.add(b2);f.add(tf2);
    f.add(b3);f.add(tf3);
    f.add(b4);f.add(tf4);
    f.add(b5);f.add(tf5);
    f.add(b6);f.add(tf6);
    f.add(b7);f.add(tf7);
    f.add(b8);f.add(tf8);
    f.add(b9);//f.add(tf9);
    f.add(b10);//f.add(tf10);
    f.add(b11);//f.add(tf10);
    f.add(b12);//f.add(tf10);
    f.add(b13);//f.add(tf10);
    f.add(b14);//f.add(tf10);
    f.add(b15);//f.add(tf10);
    f.add(b16);//f.add(tf10);
    f.setBounds(50,50,500,800);
    f.setLayout(null);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // The GUI is now up and running!

}


	public static void main(String[] args) {
		runGUI();
  }
}