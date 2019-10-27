import processing.core.PImage;

import java.util.List;

public class Fish implements ActiveEntity {

    private static final String FISH_KEY = "fish";
    private static final int FISH_NUM_PROPERTIES = 5;
    private static final int FISH_ID = 1;
    private static final int FISH_COL = 2;
    private static final int FISH_ROW = 3;
    private static final int FISH_ACTION_PERIOD = 4;
    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;


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

    public void executeFishActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position;  // store current position before removing

        EntityCreator entityCreator = new EntityCreator(); //made this

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity crab = entityCreator.createCrab(this.id + Crab.getCrabIdSuffix(), // changed this
                pos, this.actionPeriod / Crab. getCrabPeriodScale(),
                Crab.getCrabAnimationMin() +
                        world.rand.nextInt(Crab.getCrabAnimationMax() - Crab.getCrabAnimationMin()),
                imageStore.getImageList(Crab.getCrabKey()));

        world.addEntity(crab);
        ((ActiveEntity)(crab)).scheduleActions(scheduler, world, imageStore);
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
    public int getAnimationPeriod(){
        return this.animationPeriod;
    }
    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }


    public static String getFishIdPrefix(){return FISH_ID_PREFIX;}
    public static String getFishKey(){return FISH_KEY;}
    public static int getFishNumProperties(){return FISH_NUM_PROPERTIES;}
    public static int getFishId(){return FISH_ID;}
    public static int getFishCol(){return FISH_COL;}
    public static int getFishRow(){return FISH_ROW;}
    public static int getFishActionPeriod(){return FISH_ACTION_PERIOD;}
    public static int getFishCorruptMin(){return FISH_CORRUPT_MIN;}
    public static int getFishCorruptMax(){return FISH_CORRUPT_MAX;}


}
