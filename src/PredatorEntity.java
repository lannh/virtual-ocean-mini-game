import processing.core.PImage;

import java.util.*;

//Entity that collect resource
public abstract class PredatorEntity extends TravelingEntity
{
    private Optional<Entity> curTarget = Optional.empty();

    private static final int LIMIT_TIME_PURSUING_A_TARGET = 13;

    public PredatorEntity(String id, Point position, List<PImage> images,
                          int actionPeriod, int animationPeriod)
    {

        super(id, position, images, actionPeriod, animationPeriod, LIMIT_TIME_PURSUING_A_TARGET);
    }

    @Override
    protected boolean canNotPursueCurrentTarget(WorldModel world,
                                             EventScheduler scheduler,ImageStore imageStore)
    {
        return  !curTarget.isPresent() || !world.getOccupant(getCurTargetPos()).equals(curTarget);
    }

    @Override
    protected void findNewPath(WorldModel world)
    {
        curTarget = this.findNearest(world);

        if(curTarget.isPresent())
        {
            setCurTargetPos(curTarget.get().getPosition());
            clearPathToTarget();
            setCurWaitingTime(0);

            if (!this.computePath(world))
            {
                curTarget = Optional.empty();
                setCurTargetPos(null);
            }
        }
        else setCurTargetPos(null);
    }

    @Override
    protected boolean continueOnCurrentPath(WorldModel world,
                                            EventScheduler scheduler, ImageStore imageStore)
    {
        return !curTarget.isPresent() ||
                !this.moveTo(world, scheduler) ||
                !executePredatorActivity(world, scheduler, imageStore, curTarget.get());
    }

    @Override
    protected void giveUPOnTarget()
    {
        clearPathToTarget();
        curTarget = Optional.empty();
        setCurTargetPos(null);
    }

    public Optional<Entity> findNearest(WorldModel world)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.getEntities())
        {
            if (isTarget(entity))
            {
                ofType.add(entity);
            }
        }

        return this.nearestEntity(ofType);
    }

    public Optional<Entity> nearestEntity(List<Entity> entities)
    {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared(this.getPosition());

            for (Entity other : entities)
            {
                int otherDistance = other.getPosition().distanceSquared(this.getPosition());

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    protected abstract boolean isTarget(Entity entity);

    protected abstract boolean executePredatorActivity(WorldModel world,
                                                       EventScheduler scheduler,ImageStore imageStore, Entity target);

}
