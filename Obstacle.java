import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entity{

    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;

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

    public int getActionPeriod(){ return this.actionPeriod; }
    public int getAnimationPeriod(){
        return this.animationPeriod;
    }



    public Point getPosition(){
        return this.position;
    }
    public void setPosition(Point position){
        this.position = position;
    }
    public int getImageIndex(){
        return this.imageIndex;
    }
    public List<PImage> getImages(){
        return this.images;
    }


    public static String getObstacleKey(){return OBSTACLE_KEY;}
    public static int getObstacleNumProperties(){return OBSTACLE_NUM_PROPERTIES;}
    public static int getObstacleId(){return OBSTACLE_ID;}
    public static int getObstacleCol(){return OBSTACLE_COL;}
    public static int getObstacleRow(){return OBSTACLE_ROW;}


}
