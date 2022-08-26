import processing.core.PImage;

import java.util.*;

public class Gastly extends ResourceEntity
{
    private static final String ULTRABALL_ID = "ultraball";

    public Gastly(String id, Point position, List<PImage> images,
                     int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected boolean addingResourceActivity(WorldModel world,ImageStore imageStore)
    {
        Entity ball = new Ultraball(ULTRABALL_ID, getCurTargetPos(),
                imageStore.getImageList(ULTRABALL_ID));
        world.addEntity(ball);

        return false;
    }
}