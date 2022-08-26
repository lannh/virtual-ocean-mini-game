import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SGrass extends NonAnimationEntity
{
    private static final String FISH_KEY = "fish";
    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;
    private static final int FISH_REACH = 1;

    public SGrass(String id, Point position,
                  List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        Optional<Point> openPt = this.findOpenAround(world);

        if (openPt.isPresent())
        {
            Random rand = new Random();
            ActivityEntity fish = new Fish (FISH_ID_PREFIX + this.getId(),
                    openPt.get(), imageStore.getImageList(FISH_KEY),
                    FISH_CORRUPT_MIN + rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN));

            world.addEntity(fish);
            fish.scheduleActions(world, scheduler,imageStore);
        }

        scheduler.scheduleEvent(this,
                new Activity (this, world, imageStore),
                this.getActionPeriod());
    }

    private Optional<Point> findOpenAround(WorldModel world)
    {
        for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
        {
            for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
            {
                Point newPt = new Point(this.getPosition().x + dx, this.getPosition().y + dy);
                if (world.withinBounds(newPt) &&
                        !world.isOccupied(newPt))
                {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

}
