import processing.core.PImage;

import java.util.List;

public class Atlantis extends  AnimationEntity
{
    private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

    public Atlantis(String id, Point position,
                    List<PImage> images,
                    int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    @Override
    public void scheduleActions(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new Animation(this, ATLANTIS_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }


}
