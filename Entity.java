import processing.core.PImage;

import java.util.List;

public interface Entity {

    Point getPosition();

    void setPosition(Point position);

    List<PImage> getImages();

    int getImageIndex();
}
