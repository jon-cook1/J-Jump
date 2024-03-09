public interface Collidable {


  public boolean checkFloorCollisions(Entity p);
  public boolean checkCeilingCollisions(Entity p);
  public boolean checkRightCollisions(Entity p);
  public boolean checkLeftCollisions(Entity p);
}
