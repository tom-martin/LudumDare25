package com.heychinaski.ld25;

import java.awt.Graphics2D;
import java.awt.Image;

public class BackgroundTile {
  Image image;
  private int size;
  private Image image2;

  public BackgroundTile(Image image, Image image2) {
    this.image = image;
    this.image2 = image2; 
    size = image.getWidth(null);
  }
  
  public void render(int x, int y, int endX, int endY, Graphics2D g) {
    int normX = ((x / size) * size) - size;
    int normY = ((y / size) * size) - size;
    
    int normEndX = ((endX / size) * size) + size;
    int normEndY = ((endY / size) * size) + size;
    
    for(int cy = normY - size;cy < normEndY;cy+=size) {
      for(int cx = normX - size;cx < normEndX;cx+=size) {
        if(cx != 0 && cy != 0 && (((cx * cy) % 2) + ((cx + cy) % 5)) == 0) g.drawImage(image2, cx, cy, null);
        else g.drawImage(image, cx, cy, null);    
      }
    }
  }
}
