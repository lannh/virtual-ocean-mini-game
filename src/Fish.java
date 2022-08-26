import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Fish extends NonAnimationEntity
{
    private static final String CRAB_KEY = "crab";
    private static final String CRAB_ID_SUFFIX = " -- crab";
    private static final int CRAB_PERIOD_SCALE = 4;
    private static final int CRAB_ANIMATION_MIN = 50;
    private static final int CRAB_ANIMATION_MAX = 150;

    public Fish(String id, Point position,
                List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Random rand = new Random();

        PredatorEntity crab = new Crab (this.getId() + CRAB_ID_SUFFIX,
                pos, imageStore.getImageList(CRAB_KEY),
                this.getActionPeriod() / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN + rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN));

        world.addEntity(crab);
        crab.scheduleActions(world, scheduler,imageStore);
    }

}
