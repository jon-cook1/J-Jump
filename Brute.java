public class Brute extends Enemy /*implements Moving*/{
  private static final String DEF_IMAGE = "assets/Brute.png";
  private static final String FLIPPED_IMAGE = "assets/BruteFlipped.png";

  private static final int DEF_WIDTH = 90;
  private static final int DEF_HEIGHT = 150;
  private static final int DEF_HP = 3;
  private static final int DEF_X = 900;
  private static final int DEF_Y = 380;
  private static final int DAMAGE = -50;
  private static final int DEF_RANGE = 100;
  private static int MOVEMENT = 3;

  private int min;
  private int max;
  private boolean increasing;
  private int projectile_timer;


  public Brute(int x, int y, int width, int height, String imageName,int hp,int range){
    super(x,y,width,height,imageName,hp);
    min = x-range;
    max = x+range;
    increasing = false;
    projectile_timer = 45;
  }
  public Brute(int x){
    this(x,DEF_Y,DEF_WIDTH,DEF_HEIGHT,DEF_IMAGE,DEF_HP,DEF_RANGE);
  }
  public Brute(int x,int y){
    this(x,y,DEF_WIDTH,DEF_HEIGHT,DEF_IMAGE,DEF_HP,DEF_RANGE);
  }
  public Brute(){
    this(DEF_X,DEF_Y,DEF_WIDTH,DEF_HEIGHT,DEF_IMAGE,DEF_HP,DEF_RANGE);
  }

  public void scrollMinMax(int amount){
    min = min+amount;
    max = max+amount;
  }

  public void move(){
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

  public boolean updateHealth(){
    this.hp = this.hp-1;
    if (this.hp == 0)
      return true;

    return false;
  }
  public int getDamage(){
    return DAMAGE;
  }
  public boolean project(){
    if (projectile_timer == 0){
      projectile_timer = 75;
      return true;
    }
    else
      projectile_timer --;
    return false;
  }
  public String getDefImage(){
    return DEF_IMAGE;
  }
  public void flip(String direction){
    if (direction == "left")
      this.setImageName(DEF_IMAGE);
    else if (direction == "right")
      this.setImageName(FLIPPED_IMAGE);
  }


}
