import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab implements ActiveEntity {

    private static final String CRAB_KEY = "crab";
    private static final String CRAB_ID_SUFFIX = " -- crab";
    private static final int CRAB_PERIOD_SCALE = 4;
    private static final int CRAB_ANIMATION_MIN = 50;
    private static final int CRAB_ANIMATION_MAX = 150;

    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;


    public Crab(Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }


    public void executeCrabActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler) {

        EntityCreator entityCreator = new EntityCreator(); // made this

        Optional<Entity> crabTarget = world.findNearest(this.position, Sgrass.class);
        long nextPeriod = this.actionPeriod;

        if (crabTarget.isPresent()) {
            Point tgtPos = crabTarget.get().getPosition();

            if (this.moveToCrab(world, crabTarget.get(), scheduler)) {
                Entity quake = entityCreator.createQuake(tgtPos, // changed this
                        imageStore.getImageList(Quake.getQuakeKey()));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                ((ActiveEntity)(quake)).scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                nextPeriod);
    }

    public boolean moveToCrab(WorldModel world,
                              Entity target, EventScheduler scheduler)
    {
        if (world.adjacent(this.position, target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionCrab(world, target.getPosition());

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

    public Point nextPositionCrab(WorldModel world,
                                  Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz,
                this.position.getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Fish)))
        {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Fish)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }


    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore){

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0), this.getAnimationPeriod());
    }

    public int getAnimationPeriod(){
        return this.animationPeriod;
    }
    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }
    public Action createAnimationAction(int repeatCount)
    {
        return new AnimationAction(this, null, null, repeatCount);
    }
    public Action createActivityAction(WorldModel world,
                                       ImageStore imageStore) { return new ActivityAction(this, world, imageStore, 0); }
    public Point getPosition(){
        return position;
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



    public static String getCrabKey(){return CRAB_KEY;}
    public static String getCrabIdSuffix(){return CRAB_ID_SUFFIX;}
    public static int getCrabPeriodScale(){return CRAB_PERIOD_SCALE;}
    public static int getCrabAnimationMin(){return CRAB_ANIMATION_MIN;}
    public static int getCrabAnimationMax(){return CRAB_ANIMATION_MAX;}

}
