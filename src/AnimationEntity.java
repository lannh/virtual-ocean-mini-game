import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends ActivityEntity
{
    private int animationPeriod;

    public AnimationEntity(String id, Point position, List<PImage> images,
                           int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod() { return animationPeriod; }

    public void nextImage()
    {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    }

}
