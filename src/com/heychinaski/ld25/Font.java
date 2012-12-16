package com.heychinaski.ld25;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Font {

  Image image;
  
  String row1 = "ABCDEFGHIJKL";
  String row2 = "MNOPQRSTUVWX";
  String row3 = "YZ.,!";

  public Font(Image image) {
    super();
    this.image = image;
  }
  
  public void renderString(Graphics g, String s, int x, int y) {
    g.setColor(Color.white);
    for(int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      int xIndex = -1;
      int yIndex = -1;
      if(row1.indexOf(c) >= 0) {
        xIndex = row1.indexOf(c) * 8;
        yIndex = 0;
      } else if(row2.indexOf(c) >= 0) {
        xIndex = row2.indexOf(c) * 8;
        yIndex = 8;
      } else {
        xIndex = row3.indexOf(c) * 8;
        yIndex = 16;
      }

      g.fillRect(x - 1, y - 1, 10, 10);
      g.drawImage(image, x, y, x + 8, y + 8, xIndex, yIndex, xIndex + 8, yIndex + 8, null);
      
      x += 10;
    }
  }
}
