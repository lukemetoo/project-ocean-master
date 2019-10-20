import processing.core.PImage;

import java.util.List;

public interface Entity {

    int getAnimationPeriod();

    void nextImage();

    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    Action createAnimationAction(int repeatCount);

    Action createActivityAction(WorldModel world, ImageStore imageStore);

    Point getPosition();

    void setPosition(Point position);

    List<PImage> getImages();

    int getImageIndex();

    int getActionPeriod();
}
