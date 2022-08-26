import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public abstract class Entity
{
   private String id;
   private Point position;
   private List<PImage> images;
   private int imageIndex;

   public Entity(String id, Point position, List<PImage> images)
   {
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
   }

   public Point getPosition() { return position; }
   public void setPosition(Point position) { this.position = position; }

   public PImage getCurrentImage()
   {
      return this.images.get(this.imageIndex);
   }

   protected String getId() { return id;}

   protected int getImageIndex() { return imageIndex;}

   protected void setImageIndex(int imageIndex)
   {
      this.imageIndex = imageIndex;
   }

   protected List<PImage> getImages() { return images;}
}
