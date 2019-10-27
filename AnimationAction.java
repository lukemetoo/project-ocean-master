public class AnimationAction implements Action {

    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public AnimationAction(Entity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler){
        ((ActiveEntity)(entity)).nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity,
                    ((ActiveEntity)(this.entity)).createAnimationAction(Math.max(this.repeatCount - 1, 0)),
                ((ActiveEntity)(this.entity)).getAnimationPeriod());
        }
    }
}
