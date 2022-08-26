import processing.core.PImage;

import java.util.List;

public abstract class NonAnimationEntity extends ActivityEntity
{
    public NonAnimationEntity(String id, Point position,
                              List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    @Override
    public void scheduleActions(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new Activity (this, world, imageStore),
                this.getActionPeriod());
    }
}
