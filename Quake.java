import processing.core.PImage;

import java.util.List;

public class Quake implements ActiveEntity {

    private static final String QUAKE_KEY = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Quake(Point position,
                  List<PImage> images)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public void executeQuakeActivity(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore){

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                QUAKE_ACTION_PERIOD);
        scheduler.scheduleEvent(this,
                this.createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
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
    public int getActionPeriod(){ return QUAKE_ACTION_PERIOD; }
    public int getAnimationPeriod(){ return QUAKE_ANIMATION_PERIOD;}
    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }


    public static String getQuakeKey(){return QUAKE_KEY;}

}
