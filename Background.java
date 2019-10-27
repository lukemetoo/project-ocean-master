import java.nio.channels.Pipe;
import java.util.List;
import processing.core.PImage;

final class Background
{
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private String id;
   private List<PImage> images;
   private int imageIndex;

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }

   public static int getBgndNumProperties(){return BGND_NUM_PROPERTIES;}
   public static int getBgndId(){return BGND_ID;}
   public static int getBgndCol(){return BGND_COL;}
   public static int getBgndRow(){return BGND_ROW;}
   public List<PImage> getImages(){
      return images;
   }
   public int getImageIndex(){
      return imageIndex;
   }
}
