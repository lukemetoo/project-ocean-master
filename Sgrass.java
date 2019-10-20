import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Sgrass implements Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Sgrass(String id, Point position,
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
