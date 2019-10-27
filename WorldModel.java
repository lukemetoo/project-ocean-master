import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{
   public final Random rand = new Random();

   // Still need to sort out all of these static constants
   // Want to have them all in WorldModel class.


   /*private static final String CRAB_KEY = "crab";
   private static final String CRAB_ID_SUFFIX = " -- crab";
   private static final int CRAB_PERIOD_SCALE = 4;
   private static final int CRAB_ANIMATION_MIN = 50;
   private static final int CRAB_ANIMATION_MAX = 150;*/

   private static final String BGND_KEY = "background";
   /*private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;*/

   private static final String OCTO_KEY = "octo";
   /*private static final int OCTO_NUM_PROPERTIES = 7;
   private static final int OCTO_ID = 1;
   private static final int OCTO_COL = 2;
   private static final int OCTO_ROW = 3;
   private static final int OCTO_LIMIT = 4;
   private static final int OCTO_ACTION_PERIOD = 5;
   private static final int OCTO_ANIMATION_PERIOD = 6;*/

   private static final String OBSTACLE_KEY = "obstacle";
   /*private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;*/

   private static final String FISH_KEY = "fish";
   //private static final int FISH_NUM_PROPERTIES = 5;
   //private static final int FISH_ID = 1;
   //private static final int FISH_COL = 2;
   //private static final int FISH_ROW = 3;
   //private static final int FISH_ACTION_PERIOD = 4;
   private static final int FISH_REACH = 1;
   //private static final String FISH_ID_PREFIX = "fish -- ";
   //private static final int FISH_CORRUPT_MIN = 20000;
   //private static final int FISH_CORRUPT_MAX = 30000;*/

   private static final String ATLANTIS_KEY = "atlantis";
   /*private static final int ATLANTIS_NUM_PROPERTIES = 4;
   private static final int ATLANTIS_ID = 1;
   private static final int ATLANTIS_COL = 2;
   private static final int ATLANTIS_ROW = 3;
   private static final int ATLANTIS_ANIMATION_PERIOD = 70;
   private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;*/

   private static final String SGRASS_KEY = "seaGrass";
   /*private static final int SGRASS_NUM_PROPERTIES = 5;
   private static final int SGRASS_ID = 1;
   private static final int SGRASS_COL = 2;
   private static final int SGRASS_ROW = 3;
   private static final int SGRASS_ACTION_PERIOD = 4;*/

   private static final String QUAKE_KEY = "quake";
   /*private static final int QUAKE_ACTION_PERIOD = 1100;
   private static final int QUAKE_ANIMATION_PERIOD = 100;
   private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;*/

   private static final int PROPERTY_KEY = 0;


   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
      {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (this.withinBounds(newPt) &&
                    !this.isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public void tryAddEntity(Entity entity)
   {
      if (this.isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }

   public boolean withinBounds(Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < this.numRows &&
              pos.getX() >= 0 && pos.getX() < this.numCols;
   }

   public boolean isOccupied(Point pos)
   {
      return this.withinBounds(pos) &&
              this.getOccupancyCell(pos) != null;
   }

   public Optional<Entity> findNearest(Point pos,
                                              Class neighbor)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities)
      {
         if (neighbor.isInstance(entity))
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }

   /*
      Assumes that there is no entity currently occupying the
      intended destination cell.
   */
   public void addEntity(Entity entity)
   {
      if (this.withinBounds(entity.getPosition()))
      {
         this.setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void removeEntity(Entity entity)
   {
      removeEntityAt(entity.getPosition());
   }

   private void removeEntityAt(Point pos)
   {
      if (withinBounds(pos)
              && getOccupancyCell(pos) != null)
      {
         Entity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         this.entities.remove(entity);
         setOccupancyCell(pos, null);
      }
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(getCurrentImage(getBackgroundCell(pos)));
      }
      else
      {
         return Optional.empty();
      }
   }

   public void setBackground(Point pos,
                                    Background background)
   {
      if (withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }

   public Optional<Entity> getOccupant(Point pos)
   {
      if (isOccupied(pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public Entity getOccupancyCell(Point pos)
   {
      return this.occupancy[pos.getY()][pos.getX()];
   }

   public void setOccupancyCell(Point pos,
                                       Entity entity)
   {
      this.occupancy[pos.getY()][pos.getX()] = entity;
   }

   public Background getBackgroundCell(Point pos)
   {
      return this.background[pos.getY()][pos.getX()];
   }

   public void setBackgroundCell(Point pos,
                                        Background background)
   {
      this.background[pos.getY()][pos.getX()] = background;
   }

   public PImage getCurrentImage(Object entity)
   {
      if (entity instanceof Background)
      {
         return ((Background)entity).getImages()
                 .get(((Background)entity).getImageIndex());
      }
      else if (entity instanceof Entity)
      {
         return ((Entity) entity).getImages().get(((Entity) entity).getImageIndex());
      }
      else
      {
         throw new UnsupportedOperationException(
                 String.format("getCurrentImage not supported for %s",
                         entity));
      }
   }

   public Optional<Entity> nearestEntity(List<Entity> entities,
                                                Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = distanceSquared(nearest.getPosition(), pos);

         for (Entity other : entities)
         {
            int otherDistance = distanceSquared(other.getPosition(), pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   private int distanceSquared(Point p1, Point p2)
   {
      int deltaX = p1.getX() - p2.getX();
      int deltaY = p1.getY() - p2.getY();

      return deltaX * deltaX + deltaY * deltaY;
   }

   public boolean adjacent(Point p1, Point p2) {
      return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) ||
              (p1.getY() == p2.getY() && Math.abs(p1.getX() - p2.getX()) == 1);
   }



   public boolean processLine(String line, WorldModel world,
                                     ImageStore imageStore, EntityParser parser)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {

         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return parser.parseBackground(properties, world, imageStore);
            case OCTO_KEY:
               return parser.parseOcto(properties, world, imageStore);
            case OBSTACLE_KEY:
               return parser.parseObstacle(properties, world, imageStore);
            case FISH_KEY:
               return parser.parseFish(properties, world, imageStore);
            case ATLANTIS_KEY:
               return parser.parseAtlantis(properties, world, imageStore);
            case SGRASS_KEY:
               return parser.parseSgrass(properties, world, imageStore);
         }
      }

      return false;
   }

   //public int getBgndNumProperties(){ return BGND_NUM_PROPERTIES; }
   //public int getBgndCol(){ return BGND_COL; }
   //public int getBgndRow(){ return BGND_ROW; }
   //public int getBgndId(){return BGND_ID;}

   /*public int getOctoNumProperties(){return OCTO_NUM_PROPERTIES;}
   public int getOctoCol(){return OCTO_COL;}
   public int getOctoRow(){return OCTO_ROW;}
   public int getOctoId(){return OCTO_ID;}
   public int getOctoLimit(){return OCTO_LIMIT;}
   public int getOctoActionPeriod(){return OCTO_ACTION_PERIOD;}
   public int getOctoAnimationPeriod(){return OCTO_ANIMATION_PERIOD;}
   public String getOctoKey(){return OCTO_KEY;}*/

   //public int getObstacleNumProperties(){return OBSTACLE_NUM_PROPERTIES;}
   //public int getObstacleId(){return OBSTACLE_ID;}
   //public int getObstacleCol(){return OBSTACLE_COL;}
   //public int getObstacleRow(){return OBSTACLE_ROW;}
   //public String getObstacleKey(){return OBSTACLE_KEY;}

   /*public int getFishNumProperties(){return FISH_NUM_PROPERTIES;}
   public int getFishId(){return FISH_ID;}
   public int getFishCol(){return FISH_COL;}
   public int getFishRow(){return FISH_ROW;}
   public int getFishActionPeriod(){return FISH_ACTION_PERIOD;}
   public String getFishKey(){return FISH_KEY;}
   public String getFishIdPrefix(){ return FISH_ID_PREFIX;}
   public int getFishCorruptMin(){ return FISH_CORRUPT_MIN;}
   public int getFishCorruptMax(){ return FISH_CORRUPT_MAX; }*/

   //public int getAtlantisAnimationRepeatCount(){ return ATLANTIS_ANIMATION_REPEAT_COUNT; }

   //public String getQuakeKey(){ return QUAKE_KEY; }
   //public static int getQuakeAnimationPeriod(){return QUAKE_ANIMATION_PERIOD;}
   //public static int getQuakeActionPeriod(){return QUAKE_ACTION_PERIOD;}
   //public int getQuakeAnimationRepeatCount(){return QUAKE_ANIMATION_REPEAT_COUNT;}

   //public String getCrabKey(){return CRAB_KEY;}
   //public String getCrabIdSuffix(){ return CRAB_ID_SUFFIX;}
   //public int getCrabPeriodScale(){ return CRAB_PERIOD_SCALE; }
   //public int getCrabAnimationMin(){ return CRAB_ANIMATION_MIN; }
   //public int getCrabAnimationMax(){ return CRAB_ANIMATION_MAX; }

   //public int getAtlantisNumProperties(){return ATLANTIS_NUM_PROPERTIES;}
   //public int getAtlantisId(){return ATLANTIS_ID;}
   //public int getAtlantisCol(){return ATLANTIS_COL;}
   //public int getAtlantisRow(){return ATLANTIS_ROW;}
   //public String getAtlantisKey(){return ATLANTIS_KEY;}

   /*public int getSgrassNumProperties(){return SGRASS_NUM_PROPERTIES;}
   public int getSgrassId(){return SGRASS_ID;}
   public int getSgrassCol(){return SGRASS_COL;}
   public int getSgrassRow(){return SGRASS_ROW;}
   public int getSgrassActionPeriod(){return SGRASS_ACTION_PERIOD;}
   public String getSgrassKey(){return SGRASS_KEY;}*/


   public Set<Entity> getEntities(){ return entities; }
   public int getNumCols(){ return numCols; }
   public int getNumRows(){ return numRows; }


}
