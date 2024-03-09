public abstract class Enemy extends Entity implements Collidable,Scrollable{
  protected int hp;
  private static final int SCROLL_SPEED = 5;
  private static final String IMMUNE_IMG = "assets/blank.png";
  private boolean immune;
  private int immune_timer;



  public Enemy(int x, int y, int width, int height, String imageName,int hp){
    super(x,y,width,height,imageName);
    this.hp = hp;
    immune = false;
    immune_timer = 0;
  }

  public int getScrollSpeed(){
    return SCROLL_SPEED;
  }
  public void scroll(int vector){
    this.moveX(vector*SCROLL_SPEED);
    if (this instanceof Moving){
      ((Moving)this).scrollMinMax(vector*SCROLL_SPEED);
    }

  }

  public boolean checkFloorCollisions(Entity p){
    if (p.getY()+p.getHeight()>=this.getY()&&p.getY()<=this.getY()+this.getHeight()){
      if ((p.getX()>=this.getX()&&p.getX()<=this.getX()+this.getWidth())||(p.getX()+p.getWidth()<=this.getX()+this.getWidth()&&p.getX()+p.getWidth()>=this.getX()))
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

  public void immune(){
    immune = true;
    immune_timer = 100;
  }
  public boolean isimmume(){
    return immune;
  }
  public boolean updateImmune(){
    if (immune){
      if (immune_timer > 0){
        immune_timer --;
        if (this.getImageName() == IMMUNE_IMG)
          this.setImageName(this.getDefImage());
        else
          this.setImageName(IMMUNE_IMG);
    }
    if (immune_timer == 0){
      immune = false;
      this.setImageName(this.getDefImage());
      return true;
    }
    }
    return false;
  }

  public abstract String getDefImage();

  public abstract boolean updateHealth();

  public abstract int getDamage();
}
