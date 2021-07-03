import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.io.*;
import javax.swing.filechooser.FileFilter;
/**
* Homework 2
* @author Yi Yang
* @version rev. 1 23/6/2021
 */

public class hw2
{
  /**
    * Main method that starts the gol gui application thread 
    * @param args - command line arguments
    */
    public static void main(String[] args)throws FileNotFoundException, IOException
    {
      EventQueue.invokeLater(() ->
         {
          var frame = new Gol_gui();
          frame.Gol_gui();
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);
        });
    }
}