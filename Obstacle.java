import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entity{

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Obstacle(String id, Point position,
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

    public Point getPosition(){
        return this.position;
    }

    public void setPosition(Point position){
        this.position = position;
    }

    public int getActionPeriod(){
        return this.actionPeriod;
    }

    public int getAnimationPeriod(){
        return this.animationPeriod;
    }

    public int getImageIndex(){
        return this.imageIndex;
    }

    public List<PImage> getImages(){
        return this.images;
    }


    // Obstacle objects don't use this method but, as of right now, I don't have a better solution. Need to use
    // inheritance.

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){

    }


    public Action createAnimationAction(int repeatCount) {
        return null;
    }


    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
        return null;
    }

    public void nextImage(){

    }











}
