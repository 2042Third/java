import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
   * Drawing class
   * 
   * */
public class DisplayGraphics extends JPanel {


    // Contains the separator of the clients' specific OS
    private static String sp = File.separator;

    // Contains the absolute working directory
    private static String curDir = System.getProperty("user.dir")+sp+"..";

    // Contains the input file path
    private static String seed_dir_sub = "src"+sp+"main"+sp+"resources";

    // Contains the input file path
    private static String src_dir_sub = "src"+sp+"main"+sp+"java";

    // Contains the input file path
    private static String src_dir = curDir+sp+src_dir_sub;

    // Contains the input file path
    private static String seed_dir = curDir+sp+seed_dir_sub;

     /**
    * width
    * */
    protected int w = 40; 
    /**
    * height
    * */
    protected int h = 40; 
    /**
    * static width
    * */
    protected static int dw = 50; 
    /**
    * static height
    * */
    protected static int dh = 50; 

    /**
    * board absolute x size
    * */
    protected int X;  
    /**
    * board absolute y size
    * */
    protected int Y; 

    /**
    * default graphics display size for x
    * */
    protected int resx=1280; 
    /**
    * default graphics display size for y
    * */
    protected int resy=720; 

    /**
    * changing width of the screen
    * */
    protected int width = this.getWidth(); 
    /**
    * changing height of the screen
    * */
    protected int height = this.getHeight(); 

    /**
    * whether history view is open
    * */
    protected boolean hist_view = true; 
    /**
    * whether antialiasing is on
    * */
    protected boolean antialias_view = true; 
    /**
    * stores history boards, resets after changing the iteration size
    * */
    protected int[][] hist_board ; 
    /**
    * tells where the history view is pointing to 
    * */
    protected int hist_it = 0; 

    /**
    * string for history view
    * */
    protected String hist_string1="History View"; 
    /**
    * string for current view
    * */
    protected String hist_string2="Current View"; 
    /**
    * string for formating in graphics display
    * */
    protected AttributedString hist1; 
    /**
    * string for formating in graphics display
    * */
    protected AttributedString hist2; 
    /**
    * font of one of the display letters
    * */
    protected Font font1 = new Font("Serif", Font.PLAIN, 24); 

    /**
    * color palette one
    * */
    protected Color color1 = new Color(0,150,0); 
    /**
    * color pallete two
    * */
    protected Color color2 = new Color(255, 146, 0); 
    /**
    * coolor pallete three
    * */
    protected Color color3 = new Color(21, 61, 249); 


    /**
    * board copy of the display engine
    * */
    private int[][] board; 

    // Image im1 = new ImageIcon(src_dir+sp+"submitty_logo-min.jpg").getImage();
    URL  imgURL = getClass().getResource("rpi_logo.jpg"); // RPI logo for start-up sequence
    Image im2 = new ImageIcon(imgURL).getImage(); // RPI logo for start-up sequence

    /**
     * Truncates/increases the size of a to fit the display of the simulation 
     * @param a - input size 
     * @return - a size that fit the current display 
     * 
     * */
    private int fit(int a){
      return (a+w+h/2)/(2);
    }

    /**
     * Sets the history iteration
     * @param a - history iteration
     * 
     * */
    public void set_hist_it(int a){
      hist_it = a;
    } 

    /**
     * Sets the board to be painted in the next repaint()/paintComponent()
     * @param board - the board to be painted;
     * */
    public void set_board(int[][] b){
      board = atomic_copy(b);
    }

    /**
     * Sets the board to be painted in the next repaint()/paintComponent()
     * @param board - the board to be painted;
     * */
    public void set_hist_board(int[][] b){
      hist_board = atomic_copy(b);
    }

    /**
     * Toggles history view boolean "hist_view" on/off
     * 
     * */
    public void toggle_hist(){

      hist_view=hist_view?false:true;
    }


    /**
     * Toggles Antialiasing for the display engine, on/off
     * 
     * 
     * */
    public void toggle_antialias(){

      antialias_view=antialias_view?false:true;
    }

    /**
     * Sets the board to be painted in the next repaint()/paintComponent()
     * @param board - the board to be painted;
     * */
    public void set_board_size( int intx, int inty){
      resx=intx*(dw+15);
      resy=inty*(dh+15);
      X=intx;
      Y=inty;
      w=resx/2/X;
      h=resy/2/Y;
    }

    /**
     * Increases the size of the current display  
     * 
     * */
    public void inc_size(){
      if (X==0) return;
      resx+=20;
      resy+=20;
      w=resx/2/X;
      h=resx/2/X;
      repaint();
    }

    /**
     * Decrease the size of the current display
     * 
     * */
    public void dec_size(){
      if (X==0) return;
      resx-=20;
      resy-=20;
      w=resx/2/X;
      h=resx/2/X;
      repaint();
    }


    /**
     * 
     * Sets the color of the color pallete #2; if you are looking to change the color pallete #1 (green), it cannot be changed.
     * 
     * */
    public void set_color2(Color c){
      color2 = c;
    }

    /**
     * 
     * Sets the color of the color pallete #3; if you are looking to change the color pallete #1 (green), it cannot be changed.
     * 
     * */
    public void set_color3(Color c){
      color3 = c;
    }

    /**
     * Draw a dot on the display area at position (x,y) 
     * @param x - x axis position 
     * @param y - y axis position
     * @param g - Graphics instance, also extends Graphics2D 
     * 
     * */
    private void draw_dot(int x, int y, Graphics g){
      g.fillOval(fit(w)*x,fit(h)*y, w, h);
    }

    /**
     * Draw a dot on the display area at position (x,y) from offset ox and oy
     * @param x - x axis position 
     * @param y - y axis position
     * @param g - Graphics instance, also extends Graphics2D 
     * @param ox - offset of the x axis position
     * @param oy - offset of the y axis position
     * 
     * */
    private void draw_dot(int x, int y, Graphics g, int ox,int oy){
      g.fillOval(fit(w)*x+ox,fit(h)*y+oy, w, h);
    }


    @Override
    /**
     * Calculates and displays rasterized (pixiel by pixiel) image of every frame in the simulation
     * @param g - graphics instance for display
     * */
    public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D)g;
      hist1=new AttributedString(hist_string1);
      hist2=new AttributedString(hist_string2);
      width = this.getWidth();
      height = this.getHeight();
                        // The simulation cells look bad without antialiasing on
      if(antialias_view) 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); // The simulation cells look bad without antialiasing on
      hist1.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0,hist_string1.length());
      hist2.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0,hist_string2.length());
      hist1.addAttribute(TextAttribute.FONT,font1);
      hist2.addAttribute(TextAttribute.FONT,font1);

      g2.setColor(new Color(255, 255,255));
      g2.fillRect(0, 0, width,height);
      g2.setColor(color2);
      // g.drawRect(0, 0, resx, resy);
      g2.drawRect(0,0,fit(w)*X,fit(h)*Y);

      Font font = new Font("Serif", Font.PLAIN, 24);
      g2.setFont(font);
      // g2.setColor(new Color(0,150,0));
      if(board!=null){
        if(hist_view) g2.drawString(hist2.getIterator(),fit(w)*X+1,30);
      }
      else{
        // g2.drawImage(im1, 0,0,300,300, this); 
        g2.drawImage(im2,800,0,200,200, this);  

        g2.drawString("Start Simulation with a seed file. ",fit(w)*X+1,30);
        g2.setColor(color3);
        g2.drawString("Goto File menu, and click open!",fit(w)*X,60);
        font = new Font("Serif", Font.BOLD|Font.ITALIC, 150);
        g2.setFont(font);

        g2.setColor(color1);
        g2.drawString("Game of Life",fit(w)*X+50,300);
        font = new Font("Serif", Font.PLAIN, 20);
        g2.setFont(font);
        g2.setColor(new Color(0,0,0));
        g2.drawString("by Mike Yang",fit(w)*X+50,400);
      }
      g2.setColor(color3);
      //filled Rectangle with rounded corners. 
      for (int i=0;i<X;i++){
        for(int z=0;z<Y;z++){
          if (board[i][z]==1) draw_dot(i,z,g);
        }
      }
    if(hist_view){
        g2.setColor(color2);
        g2.drawRect(0,fit(h)*Y+30,fit(w)*X,fit(h)*Y);
        font = new Font("Serif", Font.PLAIN, 24);
        g2.setFont(font);
        if(board!=null)
          g2.drawString(hist1.getIterator(),fit(w)*X+1,fit(h)*Y+60);
        g2.setColor(new Color(0,0,0));
        g2.drawString(hist_it+"",fit(w)*X+20,fit(h)*Y*2-20);
        g2.drawString("\'th iteration",fit(w)*X+20,fit(h)*Y*2);
        g2.setColor(color3);
        int offset = fit(h)*Y+30;
        //filled Rectangle with rounded corners. 
        if (hist_board!=null){
          for (int i=0;i<X;i++){
            for(int z=0;z<Y;z++){
              if (hist_board[i][z]==1) draw_dot(i,z,g2,0,offset);
            }
          }
        }
      }
    }

  /**
     * Sets the board, and displays the board.
     * @param board - the board to be painted;
     * */
    public void display_board( int[][] bd){
      set_board(bd);
      repaint();
    }

  /**
  *  Creates a copy of a board
  *  @param board - the target board to be copied 
  *  @return a copy of the board
  * 
  */
  protected int[][] atomic_copy(int[][] board){
    int[][] copy_board = new int[X][Y];
    for (int i=0;i<X;i++){
      for(int z=0;z<Y;z++){
        copy_board[i][z] = (board[i][z]);
      }
    }
    return copy_board;
  }
}