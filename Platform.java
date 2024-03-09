public class Platform extends Block implements Moving{

  private static final String PLATFORM_IMAGE = "assets/platform.jpg";
  private static final String WALL_IMAGE = "assets/wall.png";
  private static final int DEF_WIDTH = 150;
  private static final int DEF_HEIGHT = 25;
  private static final int DEF_X = 900;
  private static final int DEF_Y = 0;
  private static final int MOVEMENT_SPEED = 2;

  private static final boolean MOVEY = false;
  private static final int DEF_RANGE = 25;

  private boolean movey;
  private int miny;
  private int maxy;
  private boolean incy;


  Platform(){
    this(DEF_X,DEF_Y,DEF_WIDTH,DEF_HEIGHT,PLATFORM_IMAGE,MOVEY,DEF_RANGE);
  }

  Platform(int y){
    this(DEF_X,y,DEF_WIDTH,DEF_HEIGHT,PLATFORM_IMAGE,MOVEY,DEF_RANGE);
  }

  Platform(int y,boolean movey){
    this(DEF_X,y,DEF_WIDTH,DEF_HEIGHT,PLATFORM_IMAGE,movey,DEF_RANGE);
  }
  Platform(int y,boolean movey,int range){
    this(DEF_X,y,DEF_WIDTH,DEF_HEIGHT,PLATFORM_IMAGE,movey,range);
  }
  Platform(int x,int y,boolean movey,int range){
    this(x,y,DEF_WIDTH,DEF_HEIGHT,PLATFORM_IMAGE,movey,range);
  }
  Platform(int x,int y,boolean movey){
    this(x,y,DEF_WIDTH,DEF_HEIGHT,PLATFORM_IMAGE,movey,DEF_RANGE);
  }
  Platform(int x,int y){
    this(x,y,DEF_WIDTH,DEF_HEIGHT,PLATFORM_IMAGE,MOVEY,DEF_RANGE);
  }
  Platform(int x,int y,String w){
    this(x,y,DEF_HEIGHT,DEF_WIDTH,WALL_IMAGE,MOVEY,DEF_RANGE);
  }

  Platform(int x, int y, int width, int height, String image){
    this(x,y,width,height,image,MOVEY,DEF_RANGE);
  }
  Platform(int x, int y, int width, int height){
    this(x,y,width,height,PLATFORM_IMAGE,MOVEY,DEF_RANGE);
  }
  Platform(int x, int y, int width, int height,boolean movey, int range){
    this(x,y,width,height,PLATFORM_IMAGE,movey,range);
  }
  Platform(int x, int y, int width, int height, String image, boolean movey, int range){
    super(x,y,width,height,image);
    this.movey = movey;
    this.miny = y-range;
    this.maxy = y+range;
    incy = true;
  }

  public void scrollMinMax(int amount){
  }

  public void move(){

    if (movey){
      if (incy)
        this.moveY(MOVEMENT_SPEED);
      else
        this.moveY(-1*MOVEMENT_SPEED);

      if (this.getY()>=maxy){
        this.setY(maxy-1);
        incy = false;
      }
      else if (this.getY()<=miny){
        this.setY(miny+1);
        incy = true;
      }
    }
  }
}
