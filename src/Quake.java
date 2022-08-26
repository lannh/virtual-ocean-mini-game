import processing.core.PImage;

import java.util.List;

public class Quake extends  AnimationEntity
{
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 0;

    private static final String QUAKE_KEY = "quake";
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public Quake(String id, Point position, List<PImage> images,
                 int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static void createQuake(WorldModel world, EventScheduler scheduler,
                                    ImageStore imageStore, Point pos)
    {

        ActivityEntity quake = new Quake (QUAKE_ID, pos,
                imageStore.getImageList(QUAKE_KEY),
                QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
        world.addEntity(quake);
        quake.scheduleActions(world, scheduler,imageStore);
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
                new Activity (this, world, imageStore),
                this.getActionPeriod());

        scheduler.scheduleEvent(this,
                new Animation (this, QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }
}
