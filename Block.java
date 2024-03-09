public abstract class Block extends Entity implements Scrollable,Collidable{
  private static final int SCROLL_SPEED = 5;

  public Block(int x,int y,int width,int height,String image){
    super(x,y,width,height,image);
  }


  public int getScrollSpeed(){
    return SCROLL_SPEED;
  }
  public void scroll(int vector){
    this.moveX(vector*SCROLL_SPEED);
  }


  public boolean checkFloorCollisions(Entity p){
  //add logic for if player left is farther than e left and p right farther than e right
    if (p.getY()+p.getHeight()>=this.getY()&&p.getY()<=this.getY()+this.getHeight()){
      if ((p.getX()>=this.getX()&&p.getX()<=this.getX()+this.getWidth())||(p.getX()+p.getWidth()<=this.getX()+this.getWidth()&&p.getX()+p.getWidth()>=this.getX())||(p.getX()<=this.getX()&&p.getX()+p.getWidth()>=this.getX()+this.getWidth()))
        return true;
    }
    return false;
  }

  public boolean checkCeilingCollisions(Entity p){

    if (p.getY()<=this.getY()+this.getHeight()&&p.getY()>=this.getY()){
      if ((p.getX()>=this.getX()&&p.getX()<=this.getX()+this.getWidth())||(p.getX()+p.getWidth()<=this.getX()+this.getWidth()&&p.getX()+p.getWidth()>=this.getX()))
        return true;
    }
    return false;
  }


  public boolean checkRightCollisions(Entity p){
    if (p.getX()+p.getWidth()>=this.getX()&&p.getX()+p.getWidth()<=this.getX()+this.getWidth()){
      if (!((p.getY()>this.getY()+this.getHeight())||(p.getY()+p.getHeight()<this.getY())))
        return true;
    }
    return false;
  }
  public boolean checkLeftCollisions(Entity p){
    if (p.getX()<=this.getX()+this.getWidth()&&p.getX()>=this.getX()){
      if (!((p.getY()>this.getY()+this.getHeight())||(p.getY()+p.getHeight()<this.getY())))
        return true;
    }
    return false;
  }
}
