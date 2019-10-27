public class ActivityAction implements Action {

    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public ActivityAction(Entity entity, WorldModel world,
                          ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {

        if (this.entity instanceof OctoFull) {
            ((OctoFull) (this.entity)).executeOctoFullActivity(this.world,
                    this.imageStore, scheduler);
        } else if (this.entity instanceof OctoNotFull) {
            ((OctoNotFull) (this.entity)).executeOctoNotFullActivity(this.world,
                    this.imageStore, scheduler);
        } else if (this.entity instanceof Fish) {
            ((Fish) (this.entity)).executeFishActivity(this.world, this.imageStore,
                    scheduler);
        } else if (this.entity instanceof Crab) {
            ((Crab) (this.entity)).executeCrabActivity(this.world,
                    this.imageStore, scheduler);
        } else if (this.entity instanceof Quake) {
            ((Quake) (this.entity)).executeQuakeActivity(this.world, this.imageStore,
                    scheduler);
        } else if (this.entity instanceof Sgrass) {
            ((Sgrass) (this.entity)).executeSgrassActivity(this.world, this.imageStore,
                    scheduler);
        } else if (this.entity instanceof Atlantis) {
            ((Atlantis) (this.entity)).executeAtlantisActivity(this.world, scheduler);
        } else {
            throw new UnsupportedOperationException(
                    String.format("executeActivityAction not supported for %s",
                            this.entity.getClass()));
        }
    }

}


