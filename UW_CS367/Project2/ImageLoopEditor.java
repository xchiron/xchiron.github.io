import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/********************************
 This class implements a GUI-based editor for an Image Loop editor.
 A LinkedLoop<Image> named loop is declared.
 You must complete the code in the methods named pushXXX
 to implement the individual editor commands.
 ********************************/
public class ImageLoopEditor {

	protected static LoopADT<Image> loop = new LinkedLoop<Image>();
	private static String EMPTYLOOPSTRING = "no images";


   // Methods that implement the GUI buttons' actions
    static String pushFind(String title){
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	
    	Image curImage;
    	
    	for (int k = 0; k < loop.size(); k++)
	    {
    		try
    		{
    			curImage = loop.getCurrent();
    		}
    		catch (EmptyLoopException ex1)
    		{
    			return EMPTYLOOPSTRING;
    		}
    		
    		if (curImage.getTitle().equals(title))
    		{
    			String text = displayContext();
    	    	return text;
    		}
    		else
    		{
    			loop.next();
    		}
	    }
    	
    	return "not found";
    }

    static String pushSave(String filename){
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	
    	File saveFile = new File(filename);
    	if (saveFile.exists()) 
    	{
    		return "Warning: file already exists, will be overwritten\n";
    	}
    	
    	PrintStream ps;
    	
    	try
    	{
    		 ps = new PrintStream(saveFile);
    	}
    	catch (FileNotFoundException ex)
    	{
    		return "unable to save";
    	}
    	
    	String text = null;
    	String title = null;
    	String imgFilename = null;
    	String duration = null;
    	Image curImage;
	    for (int k = 0; k < loop.size(); k++)
	    {
	    	try
	    	{
	    		curImage = loop.getCurrent();
	    		title = curImage.getTitle();
	    		imgFilename = curImage.getFile();
	    		duration = Integer.toString(curImage.getDuration());
	    		ps.println( imgFilename + " " + duration + " \"" + title + "\"");
	    	}
	    	catch (EmptyLoopException ex)
			{
				return EMPTYLOOPSTRING;
			}
	    	
	    	if (k % 100 == 0)
	    	{
	    		ps.flush();
	    	}
	    	loop.previous();
	    	
	    }
	    
	    ps.close();
        
    	return "Write Complete";
    }

    static String pushLoad(String filename){
    	File srcFile = new File(filename);
    	if (!srcFile.exists()) {
    		return "unable to load";
    	}
    	
    	try {
        	Scanner fileIn = new Scanner(srcFile);
        	while (fileIn.hasNext()) {
        		String line = fileIn.nextLine();
        		String cleanLine;
        		
        		if(line.contains("\"")) {
        			cleanLine = line.replace("\"", "");
        		}
        		else
        		{
        			cleanLine=line;
        		}
        		
        		String[] Ary = cleanLine.split(" ");
        		
        		String imgFilename = Ary[0];
        		String duration = Ary[1];
        		String title;
        		
        		if (Ary.length == 3)
        		{
        			title = Ary[2];
        		}
        		else if (Ary.length > 3)
        		{
        			title = Ary[2];
        			for (int i = 3; i < Ary.length; i++)
        			{
        				title = title + " " + Ary[i];
        			}
        		}
        		else
        		{
        			title = "";
        		}
        		
        		File imgSrcFile = new File("images/" + imgFilename);
            	if (!imgSrcFile.exists()) {
            		return "Warning: " + imgFilename + " is not in images folder";
            	}
        		
        		Image newImage = new Image(imgFilename);
        		newImage.setTitle(title);
        		try
        		{
        			newImage.setDuration(Integer.parseInt(duration));
        		}
        		catch (NumberFormatException ex2)
            	{
            		return "duration for an image is not numeric";
            	}
            	loop.add(newImage);
        		}
        	fileIn.close();
        		
        	}
    	catch (FileNotFoundException fnf) {
    		return "unable to load";
    	}    	
    	//resets to the first node loaded
    	loop.previous();
    	return "";
    }

	  // add new image AFTER current image
    static String pushAddImage(String filename){
    	File srcFile = new File("images/"+filename);
    	if (!srcFile.exists()) {
    		return "Warning: " + filename + " is not in images folder";
    	}
    	
    	Image newImage = new Image(filename);
    	
    	if (loop.isEmpty())
    	{
    		loop.add(newImage);
        	String text = displayContext();
        	return text;
    	}
    	else
    	{
        	loop.add(newImage);
        	String text = displayContext();
        	return text;
    	}
    }

	  // insert new image BEFORE current image
    static String pushInsertImage(String filename){
    	File srcFile = new File("images/"+filename);
    	if (!srcFile.exists()) {
    		return "Warning: " + filename + " is not in images folder";
    	}
    	
    	Image newImage = new Image(filename);
    	loop.next();
    	loop.add(newImage);
    	String text = displayContext();
    	return text;
    }

    static String pushJump(String count){
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	
    	int intCount;
    	String text;
    	
    	try
    	{
    		intCount = Integer.parseInt(count);
    	}
    	catch (NumberFormatException ex)
    	{
    		return "invalid jump count \n";
    	}
    	
    	if (intCount == 0)
    	{
    		text = displayContext();
        	return text;
    	}
    	else if (intCount < 0)
    	{
    		for (int k = 0; k > intCount; k--)
    	    {
    			loop.next();
    	    }
    		text = displayContext();
        	return text;
    	}
    	else
    	{
    		for (int k = 0; k < intCount; k++)
    	    {
    			loop.previous();
    	    }
    		text = displayContext();
        	return text;
    	}
    }

    static String pushUpdate(String time){
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	try
    	{
    		int timeInt = Integer.parseInt(time);
    		if (timeInt >= 0)
    		{
    			loop.getCurrent().setDuration(timeInt);
    		}
    		else
    		{
    			return "please enter a positive time";
    		}
    	}
    	catch (EmptyLoopException ex1)
		{
			return EMPTYLOOPSTRING;
		}
    	catch (NumberFormatException ex2)
    	{
    		return "please enter a numeric time";
    	}
    	String text = displayContext();
    	return text;
    }

    static String pushEdit(String title){
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	try
    	{
    		loop.getCurrent().setTitle(title);
    	}
    	catch (EmptyLoopException ex)
		{
			return EMPTYLOOPSTRING;
		}
    	String text = displayContext();
    	return text;
    }

    static String pushDisplay(){
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	
    	String text = null;
    	String title = null;
    	String filename = null;
    	String duration = null;
    	Image curImage;
	    for (int k = 0; k < loop.size(); k++)
	    {
	    	try
	    	{
	    		curImage = loop.getCurrent();
	    		title = curImage.getTitle();
	    		filename = curImage.getFile();
	    		duration = Integer.toString(curImage.getDuration());
	    		if (k==0)
	    		{
	    			text = title + "[" + duration + ", " + filename + "]" + "\n";
	    		}
	    		else 
	    		{
	    			text = text  + title + "[" + duration + ", " + filename + "]" + "\n";
	    		}
	    	}
	    	catch (EmptyLoopException ex)
			{
				return EMPTYLOOPSTRING;
			}
	    	loop.previous();
	    }
    	return text;
    }

    static String pushShow(){
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	try
    	{
    	loop.getCurrent().displayImage();
    	}
    	catch (EmptyLoopException ex)
		{
			return EMPTYLOOPSTRING;
		}
    	String text = displayContext();
    	return text;
    }


    static String pushTest(){
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	
    	// need to put all images in a single List and have that display here.
    	ArrayList<Image> ImageAryList = new ArrayList<Image>();  
    	for (int k = 0; k < loop.size(); k++)
	    {
    		try
    		{
    			ImageAryList.add(loop.getCurrent());
    		}
    		catch (EmptyLoopException ex)
    		{
    			return EMPTYLOOPSTRING;
    		}
    		loop.next();
	    }
    	Image.displayImageList(ImageAryList);
    	return "Running Test";
    }

    static String pushRemove(){
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	
    	try 
    	{
    		Image removedImage = loop.removeCurrent();
    	}
    	catch (EmptyLoopException ex)
		{
			return EMPTYLOOPSTRING;
		}
    	
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	
    	String text = displayContext();
    	return text;
    	
    }


    static String pushForward(){
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	loop.previous();
    	String text = displayContext();
    	return text;
    }


    static String pushBack(){
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	loop.next();
    	String text = displayContext();
    	return text;
    }

    private static String displayContext() 
    {
    	String context = null;
    	
    	if (loop.isEmpty())
    	{
    		return EMPTYLOOPSTRING;
    	}
    	else if (loop.size() == 1) 
    	{
    		try 
    		{
    			context = "--> " + loop.getCurrent().toString() + " <--\n";
    		}
    		catch (EmptyLoopException ex)
    		{
    			return EMPTYLOOPSTRING;
    		}
    		
    	}
    	else if (loop.size() == 2) 
    	{
    		try
    		{
    			context = "--> " + loop.getCurrent().toString() + " <--\n";
    			loop.previous();
    			context = context + "    " + loop.getCurrent().toString() + "    \n";
    			loop.next();
    		}
    		catch (EmptyLoopException ex) 
    		{
    			return EMPTYLOOPSTRING;
    		}
    	}
    	else
    	{
    		try
    		{
    			loop.next();
    			context = "    " + loop.getCurrent().toString() + "    \n";
    			loop.previous();
    			context = context + "--> " + loop.getCurrent().toString() + " <--\n";
    			loop.previous();
    			context = context + "    " + loop.getCurrent().toString() + "    \n";
    			loop.next();
    		}
    		catch (EmptyLoopException ex)
    		{
    			return EMPTYLOOPSTRING;
    		}
    	}
    	return context;
    }

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

  static String pushQuit(){
		// You may use this method as implemented here
        System.exit(0);
	      return "";
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
