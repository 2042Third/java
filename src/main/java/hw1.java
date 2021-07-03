// package src.main.java;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.nio.file.*;
// import src.main.java.*;

/**
* Homework 1
* @author Yi Yang
* @version rev. 0
*/


public class hw1 
{
  /**
   * Contains the line seperator in the outputs
   */
  private static String LINESEP = "=====================";

  /**
   * Contains the separator of the clients' specific OS
   */
  private static String sp = File.separator;

  /**
   * Contains the input file path
   */
  private static String seed_dir = ".."+sp+"resources";

  /**
   * main() method
   * @param args - command line arguments
   * @exception FileNotFoundException If file is not found
   * @exception IOException If some I/O issue occurred
   */
  public static void main(String[] args)throws FileNotFoundException, IOException
  {
    System.out.println("\n\nConway's Game of Life Simulation, hw1");
    System.out.println(LINESEP);
    System.out.printf("File.separator on this system is %s\n", File.separator);
    System.out.printf("File.pathSeparator on this system is %s\n", File.pathSeparator); 
    System.out.println(LINESEP);
    Scanner in = new Scanner(System.in);
    /**
     * Contains the input file seed name
     */
    String infile_name = "seed";
    System.out.printf("Enter seed file name selected from \"%s\" folder (default \"seed\"): ",seed_dir);
    String user_file_name = in.nextLine();
    System.out.println();
    if (user_file_name.length() != 0){
      infile_name = new StringBuffer(user_file_name).toString();
    }
    System.out.printf("The seed is selected from directory \"%s%s%s\"\n",seed_dir,sp,infile_name);
    System.out.println(LINESEP);

    String line;

    // Path infile_path = Paths.get(seed_dir+sp+infile_name);
    File seed_file = new File(seed_dir+sp+infile_name);
    FileReader seed_rd = new FileReader(seed_file);
    BufferedReader seed_buff = new BufferedReader(seed_rd);
    
    // Starts parsing the input seed file
    line = seed_buff.readLine();
    String[] inp = line.split(",");

    int sx = Integer.parseInt(inp[0].strip());
    int sy = Integer.parseInt(inp[1].strip());

    int[][] board;
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

    // Pass the parsed seed and board data to class gol and start simulation
    gol sim = new gol();
    sim.game(board, sx,sy);


    seed_rd.close();
  }

  /**
   * Prints the board to System.out in proper grid positions
   * @param board - the target board to be printed
   * 
  */
  public static void board_print(int[][] board){
    for (int i=0 ; i<board.length;i++){
      for (int z =0;z< board[0].length;z++){
        System.out.printf(" %d ",board[i][z]);
      }
      System.out.println();
    }
  }

  /**
   * Overwrites board print, instead of output the board to System.out, outputs the board to oufile_name
   * @param board - the target board to be printed
   * @throws IOException - throw this exception when then file path does not open for writing correctly
  */

  public static void board_print(int[][] board, String oufile_name)throws IOException
  {
    File outFile = new File(oufile_name);
    var fw = new FileWriter(oufile_name);
    BufferedWriter bf = new BufferedWriter(fw);
    for (int i=0 ; i<board.length;i++){
      for (int z =0;z< board[0].length;z++){
        String tmp_str = String.format(" %d ",board[i][z]);
        bf.write(tmp_str);
      }
      bf.write("\n");
    }
    bf.close();
    fw.close();
  }
}