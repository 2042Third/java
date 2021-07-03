// package src.main.java;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.nio.file.*;

/**
* Game of Life 
* @author Yi Yang
* @version rev. 0
*/

public class gol{

  protected int X;
  protected int Y;
  protected String oufile="";
  protected String oufile_name;
  protected String oufile_patternA="";
  protected String oufile_patternB=".txt";

  /**
   *  Initializes and runs a simulation for a given user input.
   *  @param sx - size of x axis
   *  @param sy - size of y axis
   *  @param board - a board of size x by y
   *  @throws NumberFormatException - this happens when user inputs something that cannot be interpreted as integer
   *  @throws IOException - throw this exception when then file path does not open for writing correctly
  */
  public void game(int[][] board,int sx,int sy)throws NumberFormatException, IOException
  {
    System.out.println();
    System.out.printf("Read from main function \nseed x: %d\nseed y: %d\n",sx,sy);
    X=sx;
    Y=sy;
    System.out.printf("board:\n");
    hw1.board_print(board);
    System.out.printf("\nEnter a output directory (default System.out): ");
    oufile = new Scanner(System.in).nextLine();
    if (oufile.length()!=0){
      System.out.printf("\nOut file name pattern, comma seperated \n(ex. a \"out,.txt\" pattern yields \"out1.txt\", \"out2.txt\", and so on: ");
      String tmp_input_string = new Scanner(System.in).nextLine();
      String[] inps = tmp_input_string.split(",");
      oufile_patternA=inps[0].strip();
      oufile_patternB=inps[1].strip();
    }

    System.out.printf("\nTo start the simulation please enter iteration number.\nIteration number (default 10):");
    int it_count = 10;
    String user_input_num = new Scanner(System.in).nextLine();
    System.out.println();
    if (user_input_num.length() != 0){
      it_count = Integer.parseInt(user_input_num);
    }
    
    for (int i=0; i<it_count;i++){
      one_iteration(board);
      if (oufile.length() ==0)
        {
          System.out.printf("\nIteration %d: \n",i+1);
          hw1.board_print(board);
        }
      else{
        oufile_name = String.format("%s%s%s%s",oufile,oufile_patternA,i+1,oufile_patternB) ;
        hw1.board_print(board,oufile_name);
      }
    }
    System.out.printf("\nSimulation finished for %d iterations\n", it_count);

  }
  
  /**
  * Modifies the board for one iteration of the simulation.
  *  @param board - the target board
  * 
  */
  protected void one_iteration(int[][] board){
    int[][] new_board = atomic_copy(board);
    for (int i=0;i<X;i++){
      for (int z=0;z<Y;z++){
        board[i][z] = get_status(i,z,new_board);
      }
    }
  }

  /**
  *  Any live cell with fewer than two live neighbors dies, as if caused by underpopulation.
  *  Any live cell with two or three live neighbors lives on to the next generation.
  *  Any live cell with more than three live neighbors dies, as if by overpopulation.
  *  Any dead cell with exactY three live neighbors becomes a live cell, as if by reproduction.
  *  @param x - the target cell's x cordinate
  *  @param y - the target cell's y cordinate
  *  @param board - the target board
  *  @return the status of the target cell; 0 if it is dead, 1 if it is alive
  * 
  */
  protected int get_status(int x, int y, int[][]board){
    int cml = 0;
    // Counts the 8 cells; wraps with modulo operations
    cml += board[(x+1)%X][(y+Y-1)%Y]; // Adding the Y dimension of the board doesn't 
                                      // change the absolute value output, but makes
                                      // sure that the index is positive (java specific)
    cml += board[(x+1)%X][(y)%Y];
    cml += board[(x+1)%X][(y+1)%Y];
    cml += board[x%X][(y+Y-1)%Y];
    cml += board[x%X][(y+1)%Y];
    cml += board[(x+X-1)%X][(y+1)%Y];
    cml += board[(x+X-1)%X][(y)%Y];
    cml += board[(x+X-1)%X][(y+Y-1)%Y];
    switch (cml) {
      case 0:
        return 0;
      case 1:
        return 0;
      case 2:
        return (board[x][y]==1 ? 1:0);
      case 3:
        return 1;
      case 4:
        return 0;
      case 5:
        return 0;
      case 6:
        return 0;
      case 7:
        return 0;
      case 8:
        return 0;
    }
    return board[x][y]; // Should not happen
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
