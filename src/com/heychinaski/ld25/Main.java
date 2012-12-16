package com.heychinaski.ld25;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    JFrame mainWindow = new JFrame("Tourist Trap");
    JPanel panel = (JPanel) mainWindow.getContentPane();
    
    panel.setPreferredSize(new Dimension(1024, 768));
    panel.setLayout(new BorderLayout());
    
    final Game game = new Game();
    panel.add(game);
    
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.pack();
    
    mainWindow.setCursor(mainWindow.getToolkit().createCustomCursor(
        new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
        "null"));
    
    new Thread() {
      public void run() {
        game.start();
      }
    }.start();
    
    game.requestFocus();
    mainWindow.setVisible(true);
  }

}
