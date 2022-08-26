public class Activity implements Action
{
    private ActivityEntity entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(ActivityEntity entity, WorldModel world,
                     ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    @Override
    public void executeAction(EventScheduler scheduler)
    {
        this.executeActivityAction(scheduler);
    }

    public void executeActivityAction(EventScheduler scheduler)
    {
        this.entity.executeActivity(this.world, scheduler, this.imageStore);
    }
}
