import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OctopusNotFull extends Octopus
{
    public OctopusNotFull(String id, Point position, List<PImage> images,
                          int resourceLimit, int resourceCount,
                          int actionPeriod, int animationPeriod)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    @Override
    protected boolean executePredatorActivity(WorldModel world,
              EventScheduler scheduler, ImageStore imageStore, Entity target)
    {
        this.setResourceCount(this.getResourceCount() + 1);
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);

        return (this.transform(world, scheduler, imageStore));
    }

    @Override
    protected Octopus newForm()
    {
        return new OctopusFull (this.getId(), this.getPosition(),
                this.getImages(), this.getResourceLimit(), this.getResourceCount(),
                this.getActionPeriod(), this.getAnimationPeriod());
    }

    @Override
    protected boolean isTarget(Entity entity) { return (entity instanceof Fish); }

}
