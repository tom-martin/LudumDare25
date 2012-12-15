package com.heychinaski.ld25;

import java.awt.geom.Rectangle2D;

public class CollisionManager {
  
  private Game game;
  
  Rectangle2D.Float boundsA = new Rectangle2D.Float();
  Rectangle2D.Float nextBoundsA = new Rectangle2D.Float();
  Rectangle2D.Float boundsB = new Rectangle2D.Float();
  Rectangle2D.Float intersection = new Rectangle2D.Float();
  
  public CollisionManager(Game game) {
    super();
    this.game = game;
  }

  public void update(float tick) {
    for(int i = 0; i < game.entities.size(); i++) {
      Entity a = game.entities.get(i);
      
      boundsA.x = a.x - (a.w / 2);
      boundsA.y = a.y - (a.h / 2);
      boundsA.width = a.w;
      boundsA.height = a.h;
      
      nextBoundsA.x = a.nextX - (a.w / 2);
      nextBoundsA.y = a.nextY - (a.h / 2);
      nextBoundsA.width = a.w;
      nextBoundsA.height = a.h;
      
      for(int j = 0; j < game.entities.size(); j++) {
        Entity b = game.entities.get(j);
        
        if(a != b) {
          boundsB.x = b.x - (b.w / 2);
          boundsB.y = b.y - (b.h / 2);
          boundsB.width = b.w;
          boundsB.height = b.h;
          
          if(nextBoundsA.intersects(boundsB)) {
            a.collided(b, tick, game, boundsA, nextBoundsA, boundsB);
          }
        }
      }
    }
  }
}
