public class EntityParser {

    EntityCreator entityCreator = new EntityCreator();


    public boolean parseBackground(String [] properties,
                                   WorldModel world, ImageStore imageStore)
    {
        if (properties.length == Background.getBgndNumProperties())
        {
            Point pt = new Point(Integer.parseInt(properties[Background.getBgndCol()]),
                    Integer.parseInt(properties[Background.getBgndRow()]));
            String id = properties[Background.getBgndId()];
            world.setBackground(pt,
                    new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == Background.getBgndNumProperties();
    }

    public boolean parseOcto(String [] properties, WorldModel world,
                             ImageStore imageStore)
    {
        if (properties.length == OctoNotFull.getOctoNumProperties())
        {
            Point pt = new Point(Integer.parseInt(properties[OctoNotFull.getOctoCol()]),
                    Integer.parseInt(properties[OctoNotFull.getOctoRow()]));
            Entity entity = entityCreator.createOctoNotFull(properties[OctoNotFull.getOctoId()],
                    Integer.parseInt(properties[OctoNotFull.getOctoLimit()]),
                    pt,
                    Integer.parseInt(properties[OctoNotFull.getOctoActionPeriod()]),
                    Integer.parseInt(properties[OctoNotFull.getOctoAnimationPeriod()]),
                    imageStore.getImageList(OctoNotFull.getOctoKey()));
            world.tryAddEntity(entity);
        }

        return properties.length == OctoNotFull.getOctoNumProperties();
    }

    public boolean parseObstacle(String [] properties, WorldModel world,
                                 ImageStore imageStore)
    {
        if (properties.length == Obstacle.getObstacleNumProperties())
        {
            Point pt = new Point(
                    Integer.parseInt(properties[Obstacle.getObstacleCol()]),
                    Integer.parseInt(properties[Obstacle.getObstacleRow()]));
            Entity entity = entityCreator.createObstacle(properties[Obstacle.getObstacleId()],
                    pt, imageStore.getImageList(Obstacle.getObstacleKey()));
            world.tryAddEntity(entity);
        }

        return properties.length == Obstacle.getObstacleNumProperties();
    }

    public boolean parseFish(String [] properties, WorldModel world,
                             ImageStore imageStore)
    {
        if (properties.length == Fish.getFishNumProperties())
        {
            Point pt = new Point(Integer.parseInt(properties[Fish.getFishCol()]),
                    Integer.parseInt(properties[Fish.getFishRow()]));
            Entity entity = entityCreator.createFish(properties[Fish.getFishId()],
                    pt, Integer.parseInt(properties[Fish.getFishActionPeriod()]),
                    imageStore.getImageList(Fish.getFishKey()));
            world.tryAddEntity(entity);
        }

        return properties.length == Fish.getFishNumProperties();
    }

    public boolean parseAtlantis(String [] properties, WorldModel world,
                                 ImageStore imageStore)
    {
        if (properties.length == Atlantis.getAtlantisNumProperties())
        {
            Point pt = new Point(Integer.parseInt(properties[Atlantis.getAtlantisCol()]),
                    Integer.parseInt(properties[Atlantis.getAtlantisRow()]));
            Entity entity = entityCreator.createAtlantis(properties[Atlantis.getAtlantisId()],
                    pt, imageStore.getImageList(Atlantis.getAtlantisKey()));
            world.tryAddEntity(entity);
        }

        return properties.length == Atlantis.getAtlantisNumProperties();
    }

    public boolean parseSgrass(String [] properties, WorldModel world,
                               ImageStore imageStore)
    {
        if (properties.length == Sgrass.getSgrassNumProperties())
        {
            Point pt = new Point(Integer.parseInt(properties[Sgrass.getSgrassCol()]),
                    Integer.parseInt(properties[Sgrass.getSgrassRow()]));
            Entity entity = entityCreator.createSgrass(properties[Sgrass.getSgrassId()],
                    pt,
                    Integer.parseInt(properties[Sgrass.getSgrassActionPeriod()]),
                    imageStore.getImageList(Sgrass.getSgrassKey()));
            world.tryAddEntity(entity);
        }

        return properties.length == Sgrass.getSgrassNumProperties();
    }

}
