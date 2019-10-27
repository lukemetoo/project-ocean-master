import processing.core.PImage;

import java.util.List;

public interface Entity {

    //int getAnimationPeriod(); // Moveable entity

   // void nextImage(); // Moveable Entity

    //void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore); // MoveableEntity

    //Action createAnimationAction(int repeatCount); // Moveable Entity

    //Action createActivityAction(WorldModel world, ImageStore imageStore); // Moveable Entity

    Point getPosition(); // Entity

    void setPosition(Point position); // Entity

    List<PImage> getImages(); // Entity

    int getImageIndex(); // Entity

    //int getActionPeriod(); // Moveable entity
}
