import processing.core.PImage;

import java.util.*;

//Entity that can travel around the world
public abstract class TravelingEntity extends AnimationEntity
{
    private Point curTargetPos = null;
    private Queue<Point> pathToTarget = new ArrayDeque<>();
    //private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();
    private final int LIMIT_TIME_WAITING = 3;
    private int curWaitingTime = 0;
    private int curPursuingTime = 0;
    private final int limitTimePursuingATarget;

    public TravelingEntity(String id, Point position, List<PImage> images,
                           int actionPeriod, int animationPeriod, int limitTimePursuingATarget)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.limitTimePursuingATarget = limitTimePursuingATarget;
    }
    @Override
    public void scheduleActions(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new Activity (this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                new Animation (this, 0),
                this.getAnimationPeriod());

    }

    protected Point getCurTargetPos() { return curTargetPos; }
    protected void setCurTargetPos(Point newPos) { curTargetPos = newPos;}
    protected void setCurWaitingTime(int newTime) {curWaitingTime = newTime;}
    protected Queue<Point> getPathToTarget() {return pathToTarget;}
    protected void clearPathToTarget() {pathToTarget.clear();}

    public boolean isNearBlackHole(WorldModel world)
    {
        List<Point> neighbors = PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS.apply(this.getPosition())
                .filter(p -> PathingStrategy.withinBounds(p, world.getNumRows(), world.getNumCols())).toList();

        for(Point pt: neighbors)
        {
            Optional<Entity> entity = world.getOccupant(pt);
            if(entity.isPresent() && entity.get() instanceof BlackHole)
                return true;
        }
        return false;
    }
    public boolean executeActivityAtBlackHole(WorldModel world, EventScheduler scheduler,
                                           ImageStore imageStore)
    {
        Random rand = new Random();
        Point newPos = new Point(rand.nextInt(world.getNumCols()), rand.nextInt(world.getNumRows()));

        //keep finding until found a valid and unoccupied pos in world
        while(!PathingStrategy.withinBounds(newPos, world.getNumRows(), world.getNumCols()) ||
                world.getOccupant(newPos).isPresent())
            newPos = new Point(rand.nextInt(world.getNumCols()), rand.nextInt(world.getNumRows()));

        world.moveEntity(this, newPos);

        return true;
    }

    @Override
    public void executeActivity(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        //System.out.println("****woring on " + this);
        //System.out.println(world.getEntities().size());

        if(isNearBlackHole(world))
        {
            giveUPOnTarget();
            if(!executeActivityAtBlackHole(world, scheduler, imageStore))
                return;
        }

        ++curPursuingTime;

        if(curPursuingTime > limitTimePursuingATarget ||
           canNotPursueCurrentTarget(world, scheduler,imageStore))
            findNewPath(world);

        if (continueOnCurrentPath(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    new Activity (this, world, imageStore),
                    this.getActionPeriod());
        }

    }

    public boolean moveTo(WorldModel world, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(curTargetPos))
        {
            pathToTarget.clear();
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world);

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                    scheduler.unscheduleAllEvents(occupant.get());

                world.moveEntity(this, nextPos);
            }
            return false;

        }
    }

    public Point nextPosition(WorldModel world)
    {
        Point newPos = pathToTarget.peek();

        if(canNotMoveTo(world, newPos))
        {
            newPos = this.getPosition();

            if (curWaitingTime > LIMIT_TIME_WAITING)
                giveUPOnTarget();
            else ++curWaitingTime;

        }
        else pathToTarget.poll();

        return newPos;
    }

    public boolean computePath(WorldModel world)
    {
        List<Point> points;
        Point pos = this.getPosition();
        Point goal = curTargetPos;

        int numOfRowsWorld = world.getNumRows();
        int numOfColsWorld = world.getNumCols();

        //Using A* Pathing Strategy
        points = strategy.computePath(pos, goal,
                p -> PathingStrategy.withinBounds(p, numOfRowsWorld, numOfColsWorld) &&  !canNotMoveTo(world, p),
                (p1, p2) -> PathingStrategy.neighbors(p1,p2),
                //PathingStrategy.CARDINAL_NEIGHBORS);
                //PathingStrategy.DIAGONAL_NEIGHBORS);
                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);

        //convert List points to Queue points
        int numsOfPoints = points.size();
        for(int i = numsOfPoints-1; i>=0; --i)
            this.pathToTarget.add(points.get(i));

        return this.pathToTarget.size()!=0 ? true : false;
    }

    protected boolean canNotMoveTo(WorldModel world, Point newPos)
    {
        return (world.isOccupied(newPos));
    }

    protected abstract boolean canNotPursueCurrentTarget(WorldModel world,
                                                      EventScheduler scheduler,ImageStore imageStore);

    protected abstract void findNewPath(WorldModel world);

    protected abstract boolean continueOnCurrentPath(WorldModel world,
                                                     EventScheduler scheduler,ImageStore imageStore);

    protected abstract void giveUPOnTarget();
}
