import processing.core.PImage;

import java.util.*;

public class BlackHole extends  AnimationEntity
{
    private static final String BLACK_HOLE_ID = "blackHole";
    private static final int BLACK_HOLE_ANIMATION_REPEAT_COUNT = 0;

    private static final String GASTLY_ID = "gastly";
    private static final int GASTLY_ACTION_PERIOD = 500;
    private static final int GASTLY_ANIMATION_PERIOD = 0;

    private Entity gastly;
    private Background oldBackground;

    public BlackHole(String id, Point position, List<PImage> images,
                     int actionPeriod, int animationPeriod, Background oldBackground)
    {
        super(id, position, images, actionPeriod, animationPeriod);

        this.oldBackground = oldBackground;
        gastly = null;
    }

    public static BlackHole createBlackHole(Point pos, ImageStore imageStore, Background oldBackground)
    {
        Random rand = new Random();

        int blackHoleActionPeriod = 20000 +
                rand.nextInt(30000);

        return new BlackHole(BLACK_HOLE_ID, pos,
                imageStore.getImageList(BLACK_HOLE_ID),
                blackHoleActionPeriod, 0,oldBackground);
    }

    @Override
    public void executeActivity(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        if(this.gastly==null)
        {
            this.createGastly(world, scheduler, imageStore);
            this.scheduleActions(world, scheduler, imageStore);
        }
        else
        {
            Point pos = this.gastly.getPosition();
            world.removeEntity(this.gastly);
            scheduler.unscheduleAllEvents(this.gastly);
            Quake.createQuake(world, scheduler, imageStore, pos);
            //System.out.println("creating quake 1");

            pos = this.getPosition();
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            Quake.createQuake(world, scheduler, imageStore, pos);
            //System.out.println("creating quake 2");

            world.setBackground(pos, oldBackground);
        }
    }

    public boolean createGastly(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        Optional<Point> pos = this.findOpenAround(world);
        if(!pos.isPresent())
            return false;

        ActivityEntity gastly = new Gastly(GASTLY_ID, pos.get(),
                imageStore.getImageList(GASTLY_ID),
                GASTLY_ACTION_PERIOD, GASTLY_ANIMATION_PERIOD);

        this.gastly = gastly;
        world.addEntity(gastly);
        gastly.scheduleActions(world, scheduler, imageStore);
        return true;
    }

    private Optional<Point> findOpenAround(WorldModel world)
    {
        Queue<Point> qu = new ArrayDeque<>();
        qu.add(this.getPosition());

        List<Point> visited = new ArrayList<>();

        while(!qu.isEmpty())
        {
            Point cur = qu.poll();
            visited.add(cur);

            //if found an unoccupied neighbor, return
            if (world.withinBounds(cur) && !world.isOccupied(cur))
                return Optional.of(cur);

            //create list of neighbors that are unoccupied
            Optional<Point> unOccupiedNeighbors = PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS
                    .apply(cur).filter(p -> world.withinBounds(p) && !world.isOccupied(p)).findFirst();

            //if all neighbors are occupied
            if(!unOccupiedNeighbors.isPresent())
            {
                //create list of all valid neighbors
                List<Point> occupiedNeighbors = PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS
                        .apply(cur).filter(p -> world.withinBounds(p)).toList();

                //if that neighbors hasn't been visited, add it to the queue
                for(Point pt: occupiedNeighbors)
                    if(visited.contains(pt) || !qu.contains(pt))
                        qu.add(pt);
            }
            //else if found 1 unoccupied neighbor, add it to the queue
            else qu.add(unOccupiedNeighbors.get());
        }

        return Optional.empty();
    }

    @Override
    public void scheduleActions(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new Animation (this, BLACK_HOLE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());

        scheduler.scheduleEvent(this,
                new Activity (this, world, imageStore),
                this.getActionPeriod());
    }
}