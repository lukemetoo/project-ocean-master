import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Sgrass implements ActiveEntity {

    private static final String SGRASS_KEY = "seaGrass";
    private static final int SGRASS_NUM_PROPERTIES = 5;
    private static final int SGRASS_ID = 1;
    private static final int SGRASS_COL = 2;
    private static final int SGRASS_ROW = 3;
    private static final int SGRASS_ACTION_PERIOD = 4;

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

    public void executeSgrassActivity(WorldModel world,
                                      ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent())
        {
            EntityCreator entityCreator = new EntityCreator();

            Entity fish = entityCreator.createFish(Fish.getFishIdPrefix() + this.id,
                    openPt.get(), Fish.getFishCorruptMin() +
                            world.rand.nextInt(Fish.getFishCorruptMax() - Fish.getFishCorruptMin()),
                    imageStore.getImageList(Fish.getFishKey()));
            world.addEntity(fish);
            ((ActiveEntity)(fish)).scheduleActions(scheduler, world, imageStore);
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


    public static String getSgrassKey(){return SGRASS_KEY;}
    public static int getSgrassNumProperties(){return SGRASS_NUM_PROPERTIES;}
    public static int getSgrassId(){return SGRASS_ID;}
    public static int getSgrassCol(){return SGRASS_COL;}
    public static int getSgrassRow(){return SGRASS_ROW;}
    public static int getSgrassActionPeriod(){return SGRASS_ACTION_PERIOD;}

}
