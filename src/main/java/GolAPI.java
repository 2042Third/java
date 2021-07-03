import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.File;
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class GolAPI extends gol {
  // Seperator of the current file system
  private static String sp = File.separator;

  // Enable output to file or not
  protected boolean of_ = false;

  // Contains the absolute working directory
  private static String curDir = System.getProperty("user.dir")+sp+"..";

  // Contains the input file path
  private static String seed_dir_sub = "src"+sp+"main"+sp+"resources";

  // Contains the input file path
  private static String src_dir_sub = "src"+sp+"main"+sp+"java";

  // Contains the input file path
  private static String seed_dir = curDir+sp+seed_dir_sub;

  // output file name
  protected String oufile = "";

  // output file name
  protected String oufile_path = "";

  // User file name
  protected  String user_file_name = "";

  // Total iteration/tick count of the simulation
  protected int it_count = 10;

  // Current tick number
  public int fpt = 0;
  
  // History of the previous simulation ticks
  public int[][][] hist=new int[it_count][][];

  // Board
  private int[][] board;  

  /**
   * Gets the absolute X size of the board
   * @return - the size of the x axis of the board
   * 
   * */
  public int getx(){
    return X;
  } 

  /**
   * Gets the absolute Y size of the board
   * @return - the size of the Y axis of the board
   * 
   * */
  public int gety(){
    return Y;
  }

  /**
   * Gets a pointer (non-copy) to the current board
   * @return - the board
   * 
   * */
  public int[][] get_board(){
    return board;
  }


  /**
   * Sets the output file of the simulation, and enables output to disk.
   * @param outfile - output file name
   * 
   * */
  public void set_sim_file(String outfile){
    oufile = outfile;
    of_=true;
  }

  /**
   * Disables output to disk
   * 
   * */
  public void disable_of(){
    of_=false;
  }

  /**
   * Enable output to disk
   * 
   * */
  public void enable_of(){
    of_=true;
  }

  /**
   *  Changes the output file pattern.
   *  @param ofpA - output file pattern A
   *  @param ofpB - output file pattern B
  */
  public void of_patn(String ofpA, String ofpB)
  {
    oufile_patternA = ofpA;
    oufile_patternB = ofpB;
  }

  /**
   *  Changes the number of iterations
   *  @param tick_count - number of total tickes of the simulation
  */
  public void set_it_count(int tick_count)
  {
    it_count = tick_count;
    hist = new int[it_count][][];
  }

  /**
   * Get the current iteration number, 
   * Invariable:
   *  fpt <= it_count
   * */
  public int get_iteration()
  {
    return fpt;
  }
 
  /**
   * Gets whether the program is set to output a file of the simulation
   * @return - boolean of whether the program is set to output to disk
   * 
   * */
  public boolean get_of_()
  {
    return of_;
  }

  /**
   * Checks if the simulation is over
   * 
   * */
  public boolean has_next(){
    if (board == null) return false;
    return ((fpt>=it_count) ? false : true);
  }

  /**
   * Checks if the simulation has more boards at iteration number i
   * @return - true if there is, else false
   * 
   * */
  public boolean has_prev(int i){
    if (board == null) return false;
    return ((i>=fpt) ? false : true);
  }
 
  /**
   * Whether the program has set the output file directory
   * @return - true if the output directory is set
   * 
   * */
  public boolean has_out_dir(){
    return (oufile=="")?false:true;
  }
 
  /**
   * Sets the output directory of the output files
   * @param - the path to the output directory of the outputs files
   * 
   * */
  public void set_out_dir(String path){
    oufile=path;
  }

 
  /**
   * Gets the output directory of the output files
   * @return the output directory
   * 
   * */
  public String get_out_dir(){
    return oufile;
  }

  /**
   * Checks if the simulation is over
   * 
   * */
  public boolean has_next(int a){
    if (board == null) return false;
    return ((a>=fpt-1) ? false : true);
  }

  /**
   *  Overrides gol.game()
   *  Runs a simulation tick.
   *  @throws NumberFormatException - this happens when user inputs something that cannot be interpreted as integer
   *  @throws IOException - throw this exception when then file path does not open for writing correctly
   *  @return The next tick of the simulation, null if the end of the simulation is reached
   * */
  public int[][] game()throws NumberFormatException, IOException
  {
    if (fpt==it_count) return null;
    // hist[fpt] = new int[][];
    hist[fpt] = atomic_copy(board);
    one_iteration(board);
    if (of_ && oufile != null)
      {
        oufile_name = String.format("%s%s%s%s%s",oufile,sp,oufile_patternA,fpt+1,oufile_patternB);
        System.out.println(oufile_name);
        hw1.board_print(board,oufile_name);
        fpt++;
        return board;
      }
    else{
      fpt++;
      return board;
    }
  }


  /**
   * GUI API for Game of Life.
   * Initializes all requirements for gol class.
   * @param usr_file_name - the seed file name 
   * @throws NumberFormatException - this happens when user inputs something that cannot be interpreted as integer
   * @throws IOException - throw this exception when then file path does not open for writing correctly
   * */
  public void GolAPI(String usr_file_name) throws FileNotFoundException, IOException
  {
    String infile_name = "seed";
    user_file_name = usr_file_name;
    System.out.printf("\nGolAPI:  %s,%s\n",usr_file_name,infile_name);
    if (user_file_name.length() != 0){
      infile_name = user_file_name;
    }
    fpt = 0;
    String line;
    File seed_file = new File(seed_dir+sp+infile_name);
    FileReader seed_rd = new FileReader(seed_file);
    BufferedReader seed_buff = new BufferedReader(seed_rd);
    
    // Starts parsing the input seed file
    line = seed_buff.readLine();
    String[] inp = line.split(",");

    int sx = Integer.parseInt(inp[0].strip());
    int sy = Integer.parseInt(inp[1].strip());

    String inp_file = "";
    int di_x=0, di_y=0;
    while ((line = seed_buff.readLine()) != null){
      di_x +=1;
      inp_file = inp_file+"&"+line;
    }

    String[] tmp_ary = inp_file.split("&");
    di_y = tmp_ary[1].split(",").length;
    board = new int[di_x][di_y];
    for (int i=1; i<di_x+1; i++){
      String[] tmp_line = tmp_ary[i].split(",");
      for (int z=0; z<di_y;z++){
        board[i-1][z]=Integer.parseInt(tmp_line[z].strip());
      }
    }
    X = sx;
    Y = sy;
    seed_rd.close();

  }
}