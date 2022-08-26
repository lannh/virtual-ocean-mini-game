import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OctopusFull extends Octopus
{

    public OctopusFull(String id, Point position, List<PImage> images,
                       int resourceLimit, int resourceCount,
                       int actionPeriod, int animationPeriod)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    @Override
    protected boolean executePredatorActivity(WorldModel world,
              EventScheduler scheduler, ImageStore imageStore, Entity target)
    {
        //at atlantis trigger animation
        ((AnimationEntity)target).scheduleActions(world, scheduler,imageStore);

        //transform to unfull
        return (this.transform(world, scheduler, imageStore));
    }

    @Override
    protected Octopus newForm()
    {
        return new OctopusNotFull (this.getId(), this.getPosition(),
                this.getImages(),
                this.getResourceLimit(), 0,
                this.getActionPeriod(), this.getAnimationPeriod());
    }

    @Override
    protected boolean isTarget(Entity entity) { return (entity instanceof Atlantis); }

}
