import processing.core.PImage;

import java.util.List;

public abstract class Octopus extends PredatorEntity implements TransformableEntity
{
    private int resourceLimit;
    private int resourceCount;

    public Octopus(String id, Point position, List<PImage> images,
                   int resourceLimit, int resourceCount,
                   int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler,
                             ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit())
        {
            Octopus octo = newForm();

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            octo.scheduleActions(world, scheduler,imageStore);

            return true;
        }

        return false;
    }

    protected int getResourceCount() { return resourceCount; }

    protected void setResourceCount(int resourceCount)
    {
        this.resourceCount = resourceCount;
    }

    protected int getResourceLimit() { return resourceLimit; }

    protected abstract Octopus newForm();
}
