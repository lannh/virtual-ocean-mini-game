import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Gengar extends PredatorEntity implements TransformableEntity
{
    private static final int GENGAR_BALLS_LIMIT = 5;
    private static final String SEAGRASS_ID = "seaGrass";

    private int curNumOfBalls = 0;

    public Gengar (String id, Point position, List<PImage> images,
                    int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    protected boolean executePredatorActivity(WorldModel world,
                                              EventScheduler scheduler,ImageStore imageStore,
                                              Entity target)
    {
        ++curNumOfBalls;

        long nextPeriod = this.getActionPeriod();
        Point tgtPos = target.getPosition();

        world.removeEntity(target);

        Quake.createQuake(world, scheduler, imageStore, tgtPos);

        if(curNumOfBalls == GENGAR_BALLS_LIMIT)
            return transform(world, scheduler, imageStore);

        nextPeriod += this.getActionPeriod();

        scheduler.scheduleEvent(this,
                new Activity (this, world, imageStore),
                nextPeriod);
        return true;
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler,
                             ImageStore imageStore)
    {
        Point pos = this.getPosition();  // store current position before removing
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        //System.out.println("creating seagrass");
        ActivityEntity seaGrass = new SGrass(SEAGRASS_ID, pos,
                imageStore.getImageList(SEAGRASS_ID),
                new Random().nextInt(2000)+9000);

        world.addEntity(seaGrass);
        seaGrass.scheduleActions(world, scheduler, imageStore);
        return true;
    }

    @Override
    protected boolean isTarget(Entity entity)
    {
        return (entity instanceof Ultraball);
    }

}
