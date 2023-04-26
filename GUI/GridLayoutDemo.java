package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GridLayoutDemo extends JFrame {

    GridLayout experimentLayout;
    String[][] surface;
    int[][] indices;
    int[][] path_indices;
    ArrayList<Label> labels = new ArrayList<>();
    public GridLayoutDemo(String name, String[][] surface, int[][] indices, int[][] path_indices) {
        super(name);
        setResizable(false);
        this.indices = indices;
        this.surface = surface;
        this.path_indices = path_indices;
    }

    public void addComponentsToPane(final Container pane) {
        final JPanel compsToExperiment = new JPanel();
        experimentLayout = new GridLayout(surface.length,surface[0].length);
        compsToExperiment.setLayout(experimentLayout);
        JPanel controls = new JPanel();
        controls.setLayout(new GridLayout(surface.length,surface[0].length));

        //Set up components preferred size
//        JButton b = new JButton("Just fake button");
//        Dimension buttonSize = b.getPreferredSize();
//        compsToExperiment.setPreferredSize(new Dimension((int)(buttonSize.getWidth() * 2.5)+maxGap,
//                (int)(buttonSize.getHeight() * 3.5)+maxGap * 2));

        //Add buttons to experiment with Grid Layout
        for (int i = 0; i < surface.length; i++) {
            for (int j = 0; j < surface[0].length; j++) {
                Label label = new Label(surface[i][j]);
                compsToExperiment.add(label);
                labels.add(label);
                label.setForeground(Color.BLUE);
            }
        }
        pane.add(compsToExperiment, BorderLayout.NORTH);
        pane.add(new JSeparator(), BorderLayout.CENTER);
        pane.add(controls, BorderLayout.SOUTH);
    }

    private void createAnimation() throws InterruptedException {
        int len = surface[0].length, i,j;
        int last_lst_i = -1;
        for(int[] pair : indices)
        {
            i=pair[0];j=pair[1];
            int lst_i = i * len + j;
            Thread.sleep(1000);
            if(last_lst_i != -1)
                labels.get(last_lst_i).setForeground(Color.GREEN);
            labels.get(lst_i).setForeground(Color.RED);
            last_lst_i = lst_i;
        }
        last_lst_i = -1;
        for(int[] pair : path_indices)
        {
            i=pair[0];j=pair[1];
            int lst_i = i * len + j;
            Thread.sleep(500);
            if(last_lst_i != -1)
                labels.get(last_lst_i).setForeground(Color.BLACK);
            labels.get(lst_i).setForeground(Color.RED);
            last_lst_i = lst_i;
        }
    }
    /**
     * Create the GUI and show it.  For thread safety,
     * this method is invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI(String[][] surface, int[][] all_indices, int[][] path_indices) throws InterruptedException {
        //Create and set up the window.
        GridLayoutDemo frame = new GridLayoutDemo("GridLayoutDemo", surface, all_indices, path_indices);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.createAnimation();
    }

    public static void main(String[] args) throws IOException {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        String indicesFilepath = args[0];
        String surfaceFilepath = args[1];
        ArrayList<String> surface = (ArrayList<String>) Files.readAllLines(Paths.get(surfaceFilepath));
        ArrayList<String> indices = (ArrayList<String>) Files.readAllLines(Paths.get(indicesFilepath));
        AtomicBoolean found = new AtomicBoolean(false);
        ArrayList<String> indices_allnodes = new ArrayList<>();
        ArrayList<String> indices_path = new ArrayList<>();
        for (String index : indices) {
            if (index.equals("-")) {
                found.set(true);
                continue;
            }
            if (found.get())
                indices_path.add(index);
            else
                indices_allnodes.add(index);
        }
        // parse surface
        String[][] parsed_sur = parseSurface(surface);
        int[][] parsed_all_indices = parseIndices(indices_allnodes);
        int[][] parsed_path_indices = parseIndices(indices_path);
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI(parsed_sur, parsed_all_indices, parsed_path_indices);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private static String[][] parseSurface(ArrayList<String> lines)
    {
        int num_rows = 0;
        String[][] surface = new String[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                surface[i][j] = "" + lines.get(i).charAt(j);
            }
        }
        return surface;
    }
    private static int[][] parseIndices(ArrayList<String> lines)
    {
        int[][] int_indices = new int[lines.size()][2];
        for (int i = 0; i < lines.size(); i++) {
            String[] ind = lines.get(i).split(",");
            int_indices[i][0] = Integer.parseInt(ind[0]);
            int_indices[i][1] = Integer.parseInt(ind[1]);
        }
        return int_indices;
    }

}
