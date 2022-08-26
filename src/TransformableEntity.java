import processing.core.PImage;

import java.util.List;
import java.util.Optional;

//Entity that can transform
public interface TransformableEntity
{
    boolean transform(WorldModel world, EventScheduler scheduler,
                      ImageStore imageStore);
}
