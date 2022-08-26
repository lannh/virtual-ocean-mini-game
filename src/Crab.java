import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Crab extends PredatorEntity implements TransformableEntity
{
    private static final String GENGAR_ID = "gengar";
    private static final int GENGAR_ACTION_PERIOD = 3000;
    private static final int GENGAR_ANIMATION_PERIOD = 0;

    public Crab(String id, Point position, List<PImage> images,
                int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    protected boolean executePredatorActivity(WorldModel world,
              EventScheduler scheduler,ImageStore imageStore,
              Entity target)
    {
        long nextPeriod = this.getActionPeriod();
        Point tgtPos = target.getPosition();

        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);

        Quake.createQuake(world, scheduler, imageStore, tgtPos);

        nextPeriod += this.getActionPeriod();

        scheduler.scheduleEvent(this,
                new Activity (this, world, imageStore),
                nextPeriod);

        return true;
    }

    @Override
    public boolean executeActivityAtBlackHole(WorldModel world, EventScheduler scheduler,
                                            ImageStore imageStore)
    {
        return transform(world, scheduler, imageStore);
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler,
                             ImageStore imageStore)
    {
        ActivityEntity gengar = new Gengar(GENGAR_ID, this.getPosition(),
                imageStore.getImageList(GENGAR_ID),
                GENGAR_ACTION_PERIOD, GENGAR_ANIMATION_PERIOD);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(gengar);
        gengar.scheduleActions(world, scheduler, imageStore);

        return false;
    }

    @Override
    protected boolean canNotMoveTo(WorldModel world, Point newPos)
    {
        Optional<Entity> occupant = world.getOccupant(newPos);
        return (occupant.isPresent() && !(occupant.get() instanceof Fish));
    }

    @Override
    protected boolean isTarget(Entity entity)
    {
        return (entity instanceof SGrass);
    }

}
