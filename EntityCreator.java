import processing.core.PImage;

import java.util.List;

public class EntityCreator {

    public Entity createAtlantis(String id, Point position,
                                 List<PImage> images)
    {
        return new Atlantis(position, images,0, 0);
    }

    public Entity createOctoFull(String id, int resourceLimit,
                                 Point position, int actionPeriod, int animationPeriod,
                                 List<PImage> images)
    {
        return new OctoFull(id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    public Entity createOctoNotFull(String id, int resourceLimit,
                                    Point position, int actionPeriod, int animationPeriod,
                                    List<PImage> images)
    {
        return new OctoNotFull(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public Entity createObstacle(String id, Point position,
                                 List<PImage> images)
    {
        return new Obstacle(id, position, images,
                0, 0, 0, 0);
    }

    public Entity createFish(String id, Point position, int actionPeriod,
                             List<PImage> images)
    {
        return new Fish(id, position, images, 0, 0,
                actionPeriod, 0);
    }

    public Entity createCrab(String id, Point position,
                             int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Crab(position, images, actionPeriod, animationPeriod);
    }

    public Entity createQuake(Point position, List<PImage> images)
    {
        return new Quake(position, images);
    }

    public Entity createSgrass(String id, Point position, int actionPeriod,
                               List<PImage> images)
    {
        return new Sgrass(id, position, images, 0, 0,
                actionPeriod, 0);
    }
}
