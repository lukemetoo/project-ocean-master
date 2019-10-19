import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


final class Entity
{
   private EntityKind kind;
   private String id;
   private Point position;
   private List<PImage> images;
   private int imageIndex;
   private int resourceLimit;
   private int resourceCount;
   private int actionPeriod;
   private int animationPeriod;

   public Entity(EntityKind kind, String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }

   public int getAnimationPeriod()
   {
      switch (this.kind)
      {
         case OCTO_FULL:
         case OCTO_NOT_FULL:
         case CRAB:
         case QUAKE:
         case ATLANTIS:
            return this.animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            this.kind));
      }
   }

   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }

   public void executeOctoFullActivity(WorldModel world,
                                              ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> fullTarget = world.findNearest(this.position,
              EntityKind.ATLANTIS);

      if (fullTarget.isPresent() &&
              moveToFull(world, fullTarget.get(), scheduler))
      {
         //at atlantis trigger animation
         fullTarget.get().scheduleActions(scheduler, world, imageStore);

         //transform to unfull
         this.transformFull(world, scheduler, imageStore);
      }
      else
      {
         scheduler.scheduleEvent(this,
                 this.createActivityAction(world, imageStore),
                 this.actionPeriod);
      }
   }

   public void executeOctoNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = world.findNearest(this.position,
              EntityKind.FISH);

      if (!notFullTarget.isPresent() ||
              !moveToNotFull(world, notFullTarget.get(), scheduler) ||
              !transformNotFull(world, scheduler, imageStore))
      {
         scheduler.scheduleEvent(this,
                 createActivityAction(world, imageStore),
                 this.actionPeriod);
      }
   }

   public void executeFishActivity(WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = this.position;  // store current position before removing

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      Entity crab = world.createCrab(this.id + world.getCrabIdSuffix(),
              pos, this.actionPeriod / world. getCrabPeriodScale(),
              world.getCrabAnimationMin() +
                      world.rand.nextInt(world.getCrabAnimationMax() - world.getCrabAnimationMin()),
              imageStore.getImageList(world.getCrabKey()));

      world.addEntity(crab);
      crab.scheduleActions(scheduler, world, imageStore);
   }

   public void executeCrabActivity(WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> crabTarget = world.findNearest(this.position, EntityKind.SGRASS);
      long nextPeriod = this.actionPeriod;

      if (crabTarget.isPresent())
      {
         Point tgtPos = crabTarget.get().position;

         if (this.moveToCrab(world, crabTarget.get(), scheduler))
         {
            Entity quake = world.createQuake(tgtPos,
                    imageStore.getImageList(world.getQuakeKey()));

            world.addEntity(quake);
            nextPeriod += this.actionPeriod;
            quake.scheduleActions(scheduler, world, imageStore);
         }
      }

      scheduler.scheduleEvent(this,
              this.createActivityAction(world, imageStore),
              nextPeriod);
   }

   public void executeQuakeActivity(WorldModel world,
                                           ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

   public void executeAtlantisActivity(WorldModel world,
                                              ImageStore imageStore, EventScheduler scheduler)
   {

      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

   public void executeSgrassActivity(WorldModel world,
                                            ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(this.position);

      if (openPt.isPresent())
      {
         Entity fish = world.createFish(world.getFishIdPrefix() + this.id,
                 openPt.get(), world.getFishCorruptMin() +
                         world.rand.nextInt(world.getFishCorruptMax() - world.getFishCorruptMin()),
                 imageStore.getImageList(world.getFishKey()));
         world.addEntity(fish);
         fish.scheduleActions(scheduler, world, imageStore);
      }

      scheduler.scheduleEvent(this,
              this.createActivityAction(world, imageStore),
              this.actionPeriod);
   }

   public void scheduleActions(EventScheduler scheduler,
                                      WorldModel world, ImageStore imageStore)
   {
      switch (this.kind)
      {
         case OCTO_FULL:
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this, this.createAnimationAction(0),
                    this.getAnimationPeriod());
            break;

         case OCTO_NOT_FULL:
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    this.createAnimationAction(0), this.getAnimationPeriod());
            break;

         case FISH:
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
            break;

         case CRAB:
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    this.createAnimationAction(0), this.getAnimationPeriod());
            break;

         case QUAKE:
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    this.createAnimationAction(world.getQuakeAnimationRepeatCount()),
                    this.getAnimationPeriod());
            break;

         case SGRASS:
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
            break;
         case ATLANTIS:
            scheduler.scheduleEvent(this,
                    this.createAnimationAction(world.getAtlantisAnimationRepeatCount()),
                    this.getAnimationPeriod());
            break;

         default:
      }
   }

   public boolean transformNotFull(WorldModel world,
                                          EventScheduler scheduler, ImageStore imageStore)
   {
      if (this.resourceCount >= this.resourceLimit)
      {
         Entity octo = world.createOctoFull(this.id, this.resourceLimit,
                 this.position, this.actionPeriod, this.animationPeriod,
                 this.images);

         world.removeEntity(this);
         scheduler.unscheduleAllEvents(this);

         world.addEntity(octo);
         octo.scheduleActions(scheduler, world, imageStore);

         return true;
      }

      return false;
   }

   public void transformFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
   {
      Entity octo = world.createOctoNotFull(this.id, this.resourceLimit,
              this.position, this.actionPeriod, this.animationPeriod,
              this.images);

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      world.addEntity(octo);
      octo.scheduleActions(scheduler, world, imageStore);
   }

   public boolean moveToNotFull(WorldModel world,
                                       Entity target, EventScheduler scheduler)
   {
      if (world.adjacent(this.position, target.position))
      {
         this.resourceCount += 1;
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);

         return true;
      }
      else
      {
         Point nextPos = nextPositionOcto(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

   public boolean moveToFull(WorldModel world,
                                    Entity target, EventScheduler scheduler)
   {
      if (world.adjacent(this.position, target.position))
      {
         return true;
      }
      else
      {
         Point nextPos = this.nextPositionOcto(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

   public boolean moveToCrab(WorldModel world,
                                    Entity target, EventScheduler scheduler)
   {
      if (world.adjacent(this.position, target.position))
      {
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);
         return true;
      }
      else
      {
         Point nextPos = this.nextPositionCrab(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

   public Point nextPositionOcto(WorldModel world,
                                        Point destPos)
   {
      int horiz = Integer.signum(destPos.getX() - this.position.getX());
      Point newPos = new Point(this.position.getX() + horiz,
              this.position.getY());

      if (horiz == 0 || world.isOccupied(newPos))
      {
         int vert = Integer.signum(destPos.getY() - this.position.getY());
         newPos = new Point(this.position.getX(),
                 this.position.getY() + vert);

         if (vert == 0 || world.isOccupied(newPos))
         {
            newPos = this.position;
         }
      }

      return newPos;
   }

   public Point nextPositionCrab(WorldModel world,
                                        Point destPos)
   {
      int horiz = Integer.signum(destPos.getX() - this.position.getX());
      Point newPos = new Point(this.position.getX() + horiz,
              this.position.getY());

      Optional<Entity> occupant = world.getOccupant(newPos);

      if (horiz == 0 ||
              (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH)))
      {
         int vert = Integer.signum(destPos.getY() - this.position.getY());
         newPos = new Point(this.position.getX(), this.position.getY() + vert);
         occupant = world.getOccupant(newPos);

         if (vert == 0 ||
                 (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH)))
         {
            newPos = this.position;
         }
      }

      return newPos;
   }

   public Action createAnimationAction(int repeatCount)
   {
      return new Action(ActionKind.ANIMATION, this, null, null, repeatCount);
   }

   public Action createActivityAction(WorldModel world,
                                             ImageStore imageStore)
   {
      return new Action(ActionKind.ACTIVITY, this, world, imageStore, 0);
   }

   public Point getPosition(){
      return position;
   }

   public EntityKind getKind(){
      return kind;
   }

   public void setPosition(Point position){
      this.position = position;
   }

   public List<PImage> getImages(){
      return images;
   }

   public int getImageIndex(){
      return imageIndex;
   }

   public int getActionPeriod(){
      return actionPeriod;
   }


}
