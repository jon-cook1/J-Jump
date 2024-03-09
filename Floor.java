public class Floor extends Block{
    private static final int DEF_WIDTH = 900;
    private static final int DEF_HEIGHT = 243;
    private static final int DEF_X = 900;
    private static final int DEF_Y = 530;

    private static final String PLAYER_IMAGE_FILE = "assets/floor.png";

    Floor(int x, int y, int width, int height){
      super(x,y,width,height,PLAYER_IMAGE_FILE);
    }
    Floor(){
      super(DEF_X,DEF_Y,DEF_WIDTH,DEF_HEIGHT,PLAYER_IMAGE_FILE);
    }
    Floor(int x, int y, int width, int height,String filename){
      super(x,y,width,height,filename);
    }
}
