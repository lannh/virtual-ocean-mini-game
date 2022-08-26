import processing.core.PImage;

import java.util.*;

//Entity that spread out resources at random pos
public abstract class ResourceEntity extends TravelingEntity
{
    private static final int LIMIT_TIME_PURSUING_A_TARGET = 30;

    public ResourceEntity(String id, Point position, List<PImage> images,
                           int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod, LIMIT_TIME_PURSUING_A_TARGET);
    }

    @Override
    protected boolean canNotPursueCurrentTarget(WorldModel world,
                                             EventScheduler scheduler,ImageStore imageStore)
    {
        return getPathToTarget().isEmpty() || canNotMoveTo(world, getCurTargetPos());
    }

    @Override
    protected void findNewPath(WorldModel world)
    {
        setCurWaitingTime(0);
        clearPathToTarget();

        Random rand = new Random();
        while(getPathToTarget().isEmpty())
        {
            setCurTargetPos( new Point(rand.nextInt(world.getNumCols()), rand.nextInt(world.getNumRows())));
            if(!canNotMoveTo(world, getCurTargetPos()))
                this.computePath(world);
        }
    }

    @Override
    protected boolean continueOnCurrentPath(WorldModel world,
                                            EventScheduler scheduler, ImageStore imageStore)
    {
        return  !this.moveTo(world, scheduler) ||
                !addingResourceActivity(world, imageStore);
    }

    @Override
    protected void giveUPOnTarget()
    {
        clearPathToTarget();
    }

    protected abstract boolean addingResourceActivity(WorldModel world,ImageStore imageStore);
}
