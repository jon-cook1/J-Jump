public class Mini extends Enemy implements Moving{
  private static final String DEF_IMAGE = "assets/snake.png";
  private static final String FLIPPED_IMAGE = "assets/snakeflipped.png";

  private static final int DEF_WIDTH = 100;
  private static final int DEF_HEIGHT = 50;
  private static final int DEF_HP = 1;
  private static final int DEF_X = 900;
  private static final int DEF_Y = 480;
  private static final int DAMAGE = -25;
  private static final int DEF_RANGE = 100;
  private static final int MOVEMENT = 1;
  private static final boolean DEF_MOVING = false;

  private int min;
  private int max;
  private boolean increasing;
  private boolean moving;

  public Mini(int x, int y, int width, int height, String imageName,int hp,int range,boolean moving){
    super(x,y,width,height,imageName,hp);
    min = x-range;
    max = x+range;
    increasing = false;
    this.moving=moving;
  }
  public Mini(int y){
    this(DEF_X,y,DEF_WIDTH,DEF_HEIGHT,DEF_IMAGE,DEF_HP,DEF_RANGE,DEF_MOVING);
  }
  public Mini(){
    this(DEF_X,DEF_Y,DEF_WIDTH,DEF_HEIGHT,DEF_IMAGE,DEF_HP,DEF_RANGE,DEF_MOVING);
  }
  public Mini(int x,int y,boolean moving){
    this(x,y,DEF_WIDTH,DEF_HEIGHT,DEF_IMAGE,DEF_HP,DEF_RANGE,moving);
  }
  public Mini(int x,int y){
    this(x,y,DEF_WIDTH,DEF_HEIGHT,DEF_IMAGE,DEF_HP,DEF_RANGE,DEF_MOVING);
  }
  public Mini(int x,int y,boolean moving,int range){
    this(x,y,DEF_WIDTH,DEF_HEIGHT,DEF_IMAGE,DEF_HP,range,moving);
  }

  public void scrollMinMax(int amount){
    min = min+amount;
    max = max+amount;
  }

  public void move(){
    if (moving){
      if (this.getX()<=min){
        increasing = true;
        this.setImageName(FLIPPED_IMAGE);
      }
      else if (this.getX()>=max){
        increasing = false;
        this.setImageName(DEF_IMAGE);
      }
      if (increasing)
        this.moveX(MOVEMENT);
      else
        this.moveX(-1*MOVEMENT);
    }
  }

  public boolean updateHealth(){
    hp = hp-1;
    if (hp == 0)
      return true;
    return false;
  }
  public int getDamage(){
    return DAMAGE;
  }
  public String getDefImage(){
    return DEF_IMAGE;
  }


}
