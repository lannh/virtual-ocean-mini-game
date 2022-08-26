import processing.core.PImage;

import java.util.List;
import java.util.Random;

public abstract class ActivityEntity extends Entity
{
    private int actionPeriod;
    public ActivityEntity(String id, Point position, List<PImage> images,
                          int actionPeriod)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() { return actionPeriod; }

    public abstract void executeActivity(WorldModel world, EventScheduler scheduler,
                                         ImageStore imageStore);

    public abstract void scheduleActions(WorldModel world, EventScheduler scheduler,
                                         ImageStore imageStore);
}
