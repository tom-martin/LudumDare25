package com.heychinaski.ld25;

import java.awt.Graphics2D;
import java.awt.Image;

public class BackgroundTile {
  Image image;
  private int size;
  private Image image2;
  private Image darkImage;
  private Image darkImage2;

  public BackgroundTile(Image image, Image image2, Image darkImage, Image darkImage2) {
    this.image = image;
    this.image2 = image2;
    this.darkImage = darkImage;
    this.darkImage2 = darkImage2; 
    size = image.getWidth(null);
  }
  
  public void render(int x, int y, int endX, int endY, Graphics2D g, boolean dark) {
    Image i = dark ? darkImage : image;
    Image i2 = dark ? darkImage2 : image2;
    
    int normX = ((x / size) * size) - size;
    int normY = ((y / size) * size) - size;
    
    int normEndX = ((endX / size) * size) + size;
    int normEndY = ((endY / size) * size) + size;
    
    for(int cy = normY - size;cy < normEndY;cy+=size) {
      for(int cx = normX - size;cx < normEndX;cx+=size) {
        Image ri = cx != 0 && cy != 0 && (((cx * cy) % 2) + ((cx + cy) % 5)) == 0 ? i2 : i; 
        g.drawImage(ri, cx, cy, null);
      }
    }
  }
}
