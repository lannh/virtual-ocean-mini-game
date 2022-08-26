public class Animation implements Action
{
    private AnimationEntity entity;
    private int repeatCount;

    public Animation(AnimationEntity entity, int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    @Override
    public void executeAction(EventScheduler scheduler)
    {
        this.executeAnimationAction(scheduler);
    }

    public void executeAnimationAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity,
                    new Animation (this.entity, Math.max(this.repeatCount - 1, 0)),
                    this.entity.getAnimationPeriod());
        }
    }
}
