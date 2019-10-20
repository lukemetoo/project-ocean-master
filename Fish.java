import processing.core.PImage;

import java.util.List;

public class Fish implements Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Fish(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod(){
        return this.animationPeriod;
    }

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
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

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore){

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
    }

    public Action createAnimationAction(int repeatCount)
    {
        return new AnimationAction(this, null, null, repeatCount);
    }

    public Action createActivityAction(WorldModel world,
                                       ImageStore imageStore)
    {
        return new ActivityAction(this, world, imageStore, 0);
    }

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


}
