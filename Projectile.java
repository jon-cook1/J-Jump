public class Projectile extends Enemy implements Moving{
  private static final String DEF_IMAGE = "assets/Projectile.png";

  private static final int DEF_WIDTH = 40;
  private static final int DEF_HEIGHT = 25;
  private static final int DEF_HP = 999;
  private static final int DEF_X = 300;
  private static final int DEF_Y = 400;
  private static final int DAMAGE = -25;
  private static final int SPEED = -13;

  private int speed;
  private int damage;

  //true=right, false=left
  public Projectile(int x,int y,boolean direction){
    super(x,y,DEF_WIDTH,DEF_HEIGHT,DEF_IMAGE,DEF_HP);
    damage = DAMAGE;
    if (!direction)
      speed = SPEED;
    else
      speed = SPEED*-1;
  }

  public void move(){
    this.moveX(speed);
  }

  public void scrollMinMax(int amount){}

  public boolean updateHealth(){return false;}

  public int getDamage(){
    return damage;
  }
  public String getDefImage(){
    return DEF_IMAGE;
  }
}
