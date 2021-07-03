import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.Box;
import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.io.*;
import javax.swing.filechooser.FileFilter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Gol_gui extends JFrame
{

  // The board of the game of life 
  int[][] board;

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

  // Icon Image 
  // Image icon_img = new ImageIcon(src_dir+sp+"icon.png").getImage();
  // BufferedImage icon_img;
  ImageIcon icon_img;

  // Graphing object 
  private DisplayGraphics graphPane;

  // Initializes the swing file chooser dialog
  JFileChooser fileChooser=new JFileChooser(new File(seed_dir));
  JFileChooser dirChooser=new JFileChooser(new File(src_dir_sub));

  // Screen default size
  private int screenHeight = 1080;
  private int screenWidth = 1920;

  // The simulation object
  private GolAPI gol_obj; 


  /**
  * Initializes 
  */
  private void initComponents() 
  {
    //-----------------------------MENU---------------------------------
    menuBar = new JMenuBar();
    fileMenu = new JMenu("File");
    helpMenu = new JMenu("Help");
    openMenuItem = new JMenuItem();
    quitMenuItem = new JMenuItem("Quit");
    saveAsMenuItem = new JMenuItem("Output Directory");
    aboutMenuItem = new JMenuItem("About");
    outputFilePatternMenuItem = new JMenuItem();
    quickStartMenuItem = new JMenuItem("Quick Start");
    SpinnerModel sm = new SpinnerNumberModel(10, 0, 496000, 1); //default value,lower bound,upper bound,increment by
    iterationSpinner = new JSpinner(sm);
    try{
      icon_img = new Gol_gui().getImage("icon.png");
    }
    catch(Exception e){
      e.printStackTrace();
    }

    firsPane = new JPanel();
    scndPane = new DisplayGraphics();
    // scndPane = new JPanel();
    graphPane = (DisplayGraphics) scndPane;
    firsPane.setLayout(new BoxLayout(firsPane, BoxLayout.Y_AXIS));

    iteratLabel = new JLabel("\'th iteration");
    setIterationLabel = new JLabel("Set Total Iteration");
    statLabel = new JLabel("0");
    Font font = new Font("Courier", Font.BOLD,24);
    statLabel.setFont(font);

    dirChooser.setDialogTitle("Output Directory");
    dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    dirChooser.setCurrentDirectory(new File(".."));

    ChangeListener spinnerListener = new SpinnerChanged();

    openMenuItem.setText("Open");
    openMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(openMenuItem);
    toggleOutMenu = new JMenu("Toggle Output");
    enableOutMenuItem = new JMenuItem("Enable Output to Disk");
    disableOutMenuItem = new JMenuItem("Disable Output to Disk");

    outputFilePatternMenuItem.setText("Outout File Pattern");
    outputFilePatternMenuItem.addActionListener(new ActionListener() {
         @Override
     public void actionPerformed(ActionEvent evt) {
      if(gol_obj==null){
        JOptionPane.showMessageDialog(firsPane, "Choose a seed first!");
        return;
      }
        String fopta = (String)JOptionPane.showInputDialog(
           firsPane,
           "Enter output file prefix\n(example: \"out\",\"sim\")", 
           "Outfile Pattern",            
           JOptionPane.PLAIN_MESSAGE,
           null,            
           null, 
           ""
        );

        String foptb = (String)JOptionPane.showInputDialog(
           firsPane,
           "Enter output file suffix\n(example: \".txt\",\".out\")", 
           "Outfile Pattern",            
           JOptionPane.PLAIN_MESSAGE,
           null,            
           null, 
           ""
        );
        gol_obj.of_patn(fopta,foptb);
      }
      });

    quickStartMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        quickStartMenuItemActionPerformed(evt);
      }
    });

    aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        aboutMenuItemActionPerformed(evt);
      }
    });

    quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        quitMenuItemActionPerformed(evt);
      }
    });

    enableOutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        enableOutMenuItemActionPerformed(evt);
      }
    });

    disableOutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        disableOutMenuItemActionPerformed(evt);
      }
    });

    saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAsMenuItemActionPerformed(evt);
      }
    });

    displayMenu = new JMenu("Display");

    viewColorMenuItem=new JMenuItem("View Color");
    cellColorMenuItem=new JMenuItem("Cell Color");
    antialiasMenuItem=new JMenuItem("Antialiasing Toggle");
    viewColorMenuItem.addActionListener(new ImmediateListener1());
    cellColorMenuItem.addActionListener(new ImmediateListener());
    antialiasMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        antialiasMenuItemActionPerformed(evt);
      }
    });
    displayMenu.add(cellColorMenuItem);
    displayMenu.add(viewColorMenuItem);
    displayMenu.addSeparator();
    displayMenu.add(antialiasMenuItem);
    fileMenu.add(saveAsMenuItem);
    toggleOutMenu.add(enableOutMenuItem);
    toggleOutMenu.add(disableOutMenuItem);
    fileMenu.add(toggleOutMenu);
    fileMenu.add(outputFilePatternMenuItem);
    fileMenu.addSeparator();
    fileMenu.add(quitMenuItem);
    helpMenu.add(quickStartMenuItem);
    helpMenu.addSeparator();
    helpMenu.add(aboutMenuItem);
    menuBar.add(fileMenu);
    menuBar.add(displayMenu);
    menuBar.add(helpMenu);

    setJMenuBar(menuBar);

//-----------------------------LHS PANEL---------------------------------
    // iterationSpinner.setBounds(10,4294999,0,1);
    iterationSpinner.addChangeListener(spinnerListener);

    firsPane.add(Box.createRigidArea(new Dimension(200, 10)));
    statLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(statLabel);
    iteratLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(iteratLabel);
    
    add_button("Start",new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        startButtonActionPerformed(evt);
      }
    }, firsPane);
    add_button("Stop",new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        stopButtonActionPerformed(evt);
      }
    }, firsPane);

    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    dirLabel1=new JLabel("Seed File:");
    dirLabel2=new JLabel("(No seed file selected)");
    dirLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
    dirLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(dirLabel1);
    firsPane.add(dirLabel2);
    add_button("Reload",new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        reloadButtonActionPerformed(evt);
      }
    }, firsPane);

    toggleOutButton = new JButton("File Output: off");
    toggleOutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    toggleOutButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        toggleOutbuttonActionPerformed(evt,toggleOutButton);
      }
    });
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    firsPane.add(new JLabel("-"));
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    firsPane.add(toggleOutButton);
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    firsPane.add(new JLabel("-"));
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    iteratLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    setIterationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(setIterationLabel);
    iterationSpinner.setMaximumSize(new Dimension(100,30)); 
    iterationSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(iterationSpinner);

    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    speedBoxLabel = new JLabel("Change fps:");
    speedBoxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(speedBoxLabel);
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    speedBox = new JComboBox();
    speedBox.setMaximumSize(new Dimension(100,30)); 
    speedBox.setEditable(true);
    speedBox.addItem(1);
    speedBox.addItem(2);
    speedBox.addItem(5);
    speedBox.addItem(10);
    speedBox.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent evt)
            {
               speedBoxctionPerformed(evt);
            }
    });
    firsPane.add(speedBox);
    // History portion
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    firsPane.add(new JLabel("-"));
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));

    histPrevButton = new JButton("<");
    histNextButton = new JButton(">");
    histPrevButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
    histPrevButton.setPreferredSize(new Dimension(20, 30));
    histNextButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    histNextButton.setPreferredSize(new Dimension(20, 30));
    histPrevButton.addActionListener(new ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        histPrevButtonActionPerformed(evt);
      }
    });
    histNextButton.addActionListener(new ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        histNextButtonActionPerformed(evt);
      }
    });
    histToggleButton = new JButton("Toggle History");
    histToggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    histToggleButton.addActionListener(new ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        histToggleButtonActionPerformed(evt);
      }
    });



    histBoxSizer = Box.createHorizontalBox();
    histBoxSizer.add(histPrevButton);
    histBoxSizer.add(histNextButton);

    firsPane.add(histToggleButton);
    firsPane.add(histBoxSizer);

    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    firsPane.add(new JLabel("-"));
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));

    sizeLabel = new JLabel("Change view size");
    sizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(sizeLabel);
    add_button("+",new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        incSizeButtonActionPerformed(evt);
      }
    }, firsPane);
    add_button("-",new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        decSizeButtonActionPerformed(evt);
      }
    }, firsPane);


//-----------------------------MAIN PANEL---------------------------------
    mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,firsPane, scndPane);
    mainPane.setDividerLocation( 150 );
    mainPane.setForeground( new Color( 0,0,0 ) );
    getContentPane().add(mainPane);  

  } // END of initComponents()
  
  /**
   * Adds a button to the panel, pnl. 
   * It would be Green if label is "Start"
   * It would be Red if label is "Stop"
   * It would be Yellow if label is "Reload"
   * @param lable - (misspelled) should be "label", but it doesn't matter
   * @param listener - the listener that needs to be called when the event triggers 
   * @param pnl - the panel that the button should be attached to
   * 
   * */
  public void add_button(String lable, ActionListener listener, JPanel pnl){
    JButton button = new JButton(lable);
    if (lable=="Start") button.setBackground(Color.GREEN);
    else if (lable=="Stop") button.setBackground(Color.RED);
    else if (lable=="Reload") button.setBackground(Color.YELLOW);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.addActionListener(listener);
    pnl.add(button);

  }

  /**
   * (Not used in final product)
   * Adds a button to the Menu, pnl. 
   * It would be Green if label is "Start"
   * It would be Red if label is "Stop"
   * It would be Yellow if label is "Reload"
   * @param lable - (misspelled) should be "label", but it doesn't matter
   * @param listener - the listener that needs to be called when the event triggers 
   * @param pnl - the Menu that the button should be attached to
   * 
   * */
  public void add_button_menu(String lable, ActionListener listener, JMenu pnl){
    JButton button = new JButton(lable);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.addActionListener(listener);
    pnl.add(button);

  }

  /**
  * Starts the gui application
  */
  public void Gol_gui()
  {
    
    
    setTitle("Game of Life");
    System.out.printf("in frame: %s;%s",curDir,src_dir);

    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();
    screenHeight = screenSize.height;
    screenWidth = screenSize.width;

    setSize(screenWidth / 2, screenWidth / 3);
    setLocationByPlatform(true);
    String currentDir = System.getProperty("user.dir");
    
    initComponents();
    setIconImage(icon_img.getImage()); 
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
    } catch (Exception e) {
      System.out.println("No good window start! theme ");
    }
  }

  
  /**
  * Open file chooser.
  * @param evt - The event object that doesn't matter for us
  * 
  */
  public void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) 
  {
    if (fileChooser.showOpenDialog(null) != fileChooser.APPROVE_OPTION) {
        return;
    }
    if (action ) {
      JOptionPane.showMessageDialog(this, "Simulation is running, please stop the simulation first!");
        return;
    }

    gol_obj=new GolAPI();
    toggleOutButton.setText("File Output: off");
    statLabel.setText("0");
    try{
      seed_dir = fileChooser.getSelectedFile().getName();
      gol_obj.GolAPI(seed_dir);
      graphPane.set_board_size(gol_obj.getx(),gol_obj.gety());
      graphPane.set_board(gol_obj.get_board());
    }
    catch (Exception e){
      JOptionPane.showMessageDialog(this, "File not found or simulation format false!");
    }
    gol_obj.set_it_count((int)iterationSpinner.getValue());
    graphPane.repaint();
    dirLabel2.setText("..."+sp+seed_dir );

  }



  /**
  * Reloads the simulation
  * @param evt - The event object that doesn't matter for us
  */
  private void reloadButtonActionPerformed(ActionEvent evt){
    if (seed_dir == null ){
      JOptionPane.showMessageDialog(this, "Choose a seed first!");
    }
    // jLabel1.setText(fileChooser.getSelectedFile().getName());
    // gol_obj=new GolAPI();
    try{
      gol_obj.GolAPI(seed_dir);
      graphPane.set_board_size(gol_obj.getx(),gol_obj.gety());
      graphPane.set_board(gol_obj.get_board());
    }
    catch (Exception e){
      JOptionPane.showMessageDialog(this, "File not found or simulation format false!");
      gol_obj = new GolAPI();
      graphPane.repaint();  

    }
    gol_obj.set_it_count((int)iterationSpinner.getValue());
    statLabel.setText(gol_obj.get_iteration()+"");
    graphPane.repaint();

  }


  /**
  * Changes the fps of the simulation 
  * @param evt - The event object that doesn't matter for us
  */
  private void speedBoxctionPerformed(java.awt.event.ActionEvent evt) {
    int spd =Integer.parseInt(speedBox.getSelectedItem().toString());
    System.out.println(spd);
    fps = (1000/spd) ;
    System.out.println(fps);
  } 

  /**
  * Go to the prevous board from the current history board
  * @param evt - The event object that doesn't matter for us
  * 
  */
  private void histPrevButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if(gol_obj==null)return;
    hist_prev();
  } 

 
  
  /**
  * Go to the next history board view
  * @param evt - The event object that doesn't matter for us
  */
  private void histNextButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if(gol_obj==null)return;
    hist_next();
  } 
  
  /**
  * Enables outputs to file, also updates the toggleOutButton if successful
  * @param evt - The event object that doesn't matter for us
  * 
  */
  private void enableOutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    
    if(gol_obj==null || !gol_obj.has_out_dir()) {
      JOptionPane.showMessageDialog(this, "To enable output to disk, \n please choose a output directory first.");
      return;
    }
    System.out.println(gol_obj.get_out_dir()+"");
    gol_obj.enable_of();
    toggleOutButton.setText("File Output: on");
  } 
  
  /**
  * Enables outputs to file, also updates the toggleOutButton if successful
  * @param evt - The event object that doesn't matter for us
  * 
  */
  private void disableOutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    if(gol_obj==null)return;
    gol_obj.disable_of();
    toggleOutButton.setText("File Output: off");
  } 

  /**
  * Toggles the history view on/off
  * @param evt - The event object that doesn't matter for us
  */
  private void histToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if(gol_obj==null)return;
    hist_ind=gol_obj.fpt;
    graphPane.toggle_hist();
    graphPane.repaint();
  } 

  /**
  * Toggle file output to disk on/off
  * @param evt - The event object that doesn't matter for us
  */
  private void toggleOutbuttonActionPerformed(java.awt.event.ActionEvent evt,JButton b) {
    if(gol_obj==null){
      JOptionPane.showMessageDialog(this, "Choose a seed first!");
      return;
    }
    if(!gol_obj.has_out_dir()){
        saveAsMenuItemActionPerformed(evt);
      }
    if(gol_obj.has_out_dir()){
      if(gol_obj.get_of_()){
        b.setText("File Output: off");
        disableOutMenuItemActionPerformed(evt);
      }
      else {
        b.setText("File Output: on");
        enableOutMenuItemActionPerformed(evt);

      }
    }
  } 
  

  /**
  * Toggles Antialiasing for the display on/off
  * @param evt - The event object that doesn't matter for us
  */
  private void antialiasMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    graphPane.toggle_antialias();
    graphPane.repaint();
  } 
  /**
  * Shows a quick start guide (more information please read the manual)
  * @param evt - The event object that doesn't matter for us
  */
  private void quickStartMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    JOptionPane.showMessageDialog(this, "Open a seed file from the menu. \nThen hit start.");
  }
  /**
  * Decreases the size of the board
  * @param evt - The event object that doesn't matter for us
  */
  private void decSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if (seed_dir == null ){
      JOptionPane.showMessageDialog(this, "Choose a seed first!");
    }
    graphPane.dec_size();
  }
  /**
  * Increases the size of the board
  * @param evt - The event object that doesn't matter for us
  */
  private void incSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if (seed_dir == null ){
      JOptionPane.showMessageDialog(this, "Choose a seed first!");
    }
    graphPane.inc_size();
  }

  /**
  * Start button that starts 
  * @param evt - The event object that doesn't matter for us
  */
  private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if (action) return;
    action = true;
    if (gol_obj==null) 
    {
      JOptionPane.showMessageDialog(this, "Please load a seed file first!");
      return;
    }
    

    /**
     * Because the drawing methods is using the same thread, if we just keep the simulation running, the UI would freez.
     * Sets a seperate thread for the display to happen.
     * 
     * */
    SwingWorker<Boolean, int[][]> display_thread = new SwingWorker<Boolean, int[][]>() {
      @Override
       protected Boolean doInBackground() throws Exception {
        while ( action && gol_obj.has_next()){
          publish(gol_obj.game());
          try{
            Thread.sleep((int)fps);
          }
          catch(InterruptedException e){
            System.out.println("Tick sleep interapted.");
          }
        }
        if ( !action || !gol_obj.has_next()) action = false;
        return true;
       }

       protected void done() {
        boolean status;
        try {
         status = get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } 
       }

       @Override
       protected void process(List<int[][]> chunks) {
        int[][] mostRecentValue = chunks.get(chunks.size()-1);
        // try{
          graphPane.display_board(mostRecentValue);
        // }
        
        statLabel.setText(gol_obj.get_iteration()+"");
       }
      
    };
    display_thread.execute();
  }

  /**
  * Stop button action
  * @param evt - The event object that doesn't matter for us
  */
  private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
    action = false;
  }

  /**
  * Open the about page
  * @param evt - The event object that doesn't matter for us
  */
  private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    if (dialog == null) 
    dialog = new AboutDialog(this);
    dialog.setVisible(true);
  }

  /**
  * Open the output directory file dialog
  * @param evt - The event object that doesn't matter for us
  */
  private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    if(gol_obj!=null && gol_obj.has_out_dir()) dirChooser.setCurrentDirectory(new File(gol_obj.get_out_dir()));
    if (dirChooser.showOpenDialog(null) != dirChooser.APPROVE_OPTION) {
        return;
    }
    if (action ) {
      JOptionPane.showMessageDialog(this, "Simulation is running, please stop the simulation first!");
        return;
    }
    if (seed_dir == null ){
      JOptionPane.showMessageDialog(this, "Choose a seed first!");
    }
    // gol_obj=new GolAPI();
    
    try{

      gol_obj.set_out_dir(""+dirChooser.getSelectedFile());

    }
    catch (Exception e){
      JOptionPane.showMessageDialog(this, "File not found or simulation format false!");
    }


    graphPane.repaint();


  }

  /**
  * Quits the program 
  * @param evt - The event object that doesn't matter for us
  */
  private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    System.exit(0);
  }

  /**
  * The iteration changer that sets the iteration count of the game of life API.
  * *IMPORTANT: this will reset the history view log
  * @param evt - The event object that doesn't matter for us
  */
  private void iterationSpinnerStateChanged(ChangeEvent e){
    if (gol_obj==null) {
        JOptionPane.showMessageDialog(this, "Please load a seed file first!");
        return ;
      }
        gol_obj.set_it_count((int)iterationSpinner.getValue());
      }
  

  /**
   * behavior of the spinner
   * @param evt - The event object that doesn't matter for us
   * 
   * */
  private class SpinnerChanged implements ChangeListener
    {
      public void stateChanged(ChangeEvent e){
      iterationSpinnerStateChanged(e);
    }};

  /**
   * Delegates the game of life API and the graphics engine to display the previous history board 
   * 
   * */
  public void hist_prev(){
    if (hist_ind<=0 || !gol_obj.has_prev(hist_ind)) return;
    hist_ind--;
    graphPane.set_hist_it(hist_ind);
    graphPane.set_hist_board(gol_obj.hist[hist_ind]);
    graphPane.repaint();
  }

  /**
   * Delegates the game of life API and the graphics engine to display the next history board 
   * 
   * */
  public void hist_next(){

    if(!gol_obj.has_next(hist_ind)) return;
    hist_ind++;
    graphPane.set_hist_it(hist_ind);
    graphPane.set_hist_board(gol_obj.hist[hist_ind]);
    graphPane.repaint();
  }


   /**
    * Credit: The example given on lecture 8
    * This listener pops up a modeless color chooser. The panel color is changed immediately when
    * the user picks a new color.
    */
   private class ImmediateListener implements ActionListener
   {
      public ImmediateListener()
      {
         chooser = new JColorChooser();
         chooser.getSelectionModel().addChangeListener(new ChangeListener()
            {
               public void stateChanged(ChangeEvent event)
               {
                  graphPane.set_color3(chooser.getColor());
                  graphPane.repaint();
               }
            });

         dialog = new JDialog((Frame) null, false /* not modal */);
         dialog.add(chooser);
         dialog.pack();
      }

      public void actionPerformed(ActionEvent event)
      {
         chooser.setColor(getBackground());
         dialog.setVisible(true);
      }

      private JDialog dialog;
      private JColorChooser chooser;
   }

   /**
    * Credit: The example given on lecture 8
    * This listener pops up a modeless color chooser. The panel color is changed immediately when
    * the user picks a new color.
    */
   private class ImmediateListener1 implements ActionListener
   {
      public ImmediateListener1()
      {
         chooser = new JColorChooser();
         chooser.getSelectionModel().addChangeListener(new ChangeListener()
            {
               public void stateChanged(ChangeEvent event)
               {
                  graphPane.set_color2(chooser.getColor());
                  graphPane.repaint();
               }
            });

         dialog = new JDialog((Frame) null, false /* not modal */);
         dialog.add(chooser);
         dialog.pack();
      }

      public void actionPerformed(ActionEvent event)
      {
         chooser.setColor(getBackground());
         dialog.setVisible(true);
      }

      private JDialog dialog;
      private JColorChooser chooser;
   }

   /**
    * Credit: https://coderanch.com/t/569491/java/images-jar-file
    * Takes a relative path to get a ImageIcon
    * @param path - file path of the system to a image
    * 
    * */
  private  ImageIcon getImage(String path){
 
  // System.out.println(path);
  URL  imgURL = getClass().getResource(path);
  System.out.println(imgURL);
      if (imgURL != null) {
          return new ImageIcon(imgURL);
      } 
      else {
          System.err.println("Couldn't find file: " + path+"\n");
          return null;
      }
  }

  /**
   * The current history board index 
   * */
  private int hist_ind; 
  /**
   * The fps of the orunning simulation 
   * */
  private double fps=1000; 
  /**
   * Whether the simulation is running
   * */
  private boolean action=false; 

  /**
   * Iteration label
   * */
  public JLabel iteratLabel; 
  /**
   * Set iteration label
   * */
  public JLabel setIterationLabel; 
  /**
   * The current iteration of the simulation 
   * */
  public JLabel statLabel; 
  /**
   * The fps label (not used)
   * */
  public JLabel speedLabel; 
  /**
   * the fps changer combox box 
   * */
  private JLabel speedBoxLabel; 
  /**
   * The first directory label
   * */
  public JLabel dirLabel1;  
  /**
   * the second directory label
   * */
  public JLabel dirLabel2; 
  /**
   * the History label
   * */
  public JLabel histLabel; 
  /**
   * The history iteration label (may not be used)
   * */
  public JLabel histLabelIt; 

  /**
   * the Size label
   * */
  public JLabel sizeLabel; 
  /**
   * The menu bar
   * */
  private JMenuBar menuBar; 
  /**
   * the file menu
   * */
  private JMenu fileMenu; 
  /**
   * the help menu
   * */
  private JMenu helpMenu; 
  /**
   * the toggle file output sub-menu
   * */
  private JMenu toggleOutMenu; 
  /**
   * The open menu item that opens the file chooser
   * */
  private JMenuItem openMenuItem; 
  /**
   * the display menu
   * */
  private JMenu displayMenu; 
  /**
   * the view color changer menu item 
   * */
  private JMenuItem viewColorMenuItem; 
  /**
   * the antialiasing toggle menu item 
   * */
  private JMenuItem antialiasMenuItem; 
  /**
   * the cell color changer menu item 
   * */
  private JMenuItem cellColorMenuItem; 
  /**
   * the quit menu item
   * */
  private JMenuItem quitMenuItem; 
  /**
   * the saveAs menu item  (name changed to "Output Directory")
   * */
  private JMenuItem saveAsMenuItem;    
  /**
   * the enable output menu item
   * */
  private JMenuItem enableOutMenuItem;  
  /**
   * the disable output menu item 
   * */
  private JMenuItem disableOutMenuItem;   
  /**
   * the about menu item 
   * */
  private JMenuItem aboutMenuItem; 
  /**
   * the output files pattern changer menu item that opens dialogs to change the output file pattern
   * */
  private JMenuItem outputFilePatternMenuItem; 
  /**
   * the quick start menu item 
   * */
  private JMenuItem quickStartMenuItem; 
  /**
   * the iteration changer spinner 
   * */
  private JSpinner iterationSpinner; 
  /**
   * the left hand side panel (quick access menu)
   * */
  private JPanel firsPane; 
  /**
   * the right hand side panel (display)
   * */
  private JPanel scndPane; 
  /**
   * The start button
   * */
  private JButton startButton; 
  /**
   * the toggle output to disk button 
   * */
  private JButton toggleOutButton; 
  /**
   * the stop button
   * */
  private JButton stopButton; 
  /**
   * the history toggle for toggling the history view
   * */
  private JButton histToggleButton; 
  /**
   * the "<" button goes to the previous button
   * */
  private JButton histPrevButton; 
  /**
   * the ">" button that goes the the next history board 
   * */
  private JButton histNextButton; 
  /**
   * the "+" button that increases teh size of the display
   * */
  private JButton incSizeButton; 
  /**
   * the "-" button that decrease the size of the display
   * */
  private JButton decSizeButton; 
  /**
   * the fps changer combo box 
   * */
  private JComboBox speedBox; 
  /**
   * the history view changer combo box (redected)
   * */
  private JComboBox histBox; 
  /**
   * the main panel
   * */
  private JSplitPane mainPane; 
  /**
   * the history buttons' container box
   * */
  private Box histBoxSizer;  
  /**
   * the color changer #1
   * */
  private ImmediateListener colorChooser1; 
  /**
   * the color  chanager #2
   * */
  private ImmediateListener1 colorChooser2; 
  /**
   * the about dialog 
   * */
  private AboutDialog dialog; 

}

/**
 * Credit: Used the example template from lecture 8
 * 
 * A dialog that displays about.
 */
class AboutDialog extends JDialog
{
   public AboutDialog(JFrame owner)
   {
      super(owner, "About DialogTest", true);

      // add HTML label to center

      add(
            new JLabel(
                  "<html><h1><i>Game of Life Simulation</i></h1><hr>By Mike Yang</html>"),
            BorderLayout.CENTER);

      
      JButton ok = new JButton("Ok");
      ok.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               setVisible(false);
            }
         });

      // add Ok button to southern border

      JPanel panel = new JPanel();
      panel.add(ok);
      add(panel, BorderLayout.SOUTH);

      Toolkit kit = Toolkit.getDefaultToolkit();
      Dimension screenSize = kit.getScreenSize();
      screenHeight = screenSize.height;
      screenWidth = screenSize.width;

      setSize(screenWidth / 6, screenHeight / 6);
      setLocationByPlatform(true);
   }

  private int screenHeight = 500;
  private int screenWidth = 400;
}




