import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Scanner;
import java.io.*;
import java.util.concurrent.TimeUnit;
//meet 11:20 friday

//current record: 72.846

public class CookGame extends AbstractGame {

//------------------------------------------
//bad practice, but easier for map writing
Entity get_vals1 = new Mini();
Entity get_vals2 = new Brute();
Entity get_vals3 = new Floor();

private int bh = get_vals2.getHeight();
private int mh = get_vals1.getHeight();
private int sw = get_vals1.getWidth();
private int h0 = getWindowHeight()/4;
private int h1 = h0*2;
private int h2 = h0*3-5;
//ground height
private int h3 = get_vals3.getY();

private int w0 = getWindowWidth();
private int w1 = w0/4+w0;
private int w2 = w0/2+w0;
private int w3 = (w0/4)*3+w0;
private boolean f=false; private boolean t=true;
private boolean spawn = true;

  //-----------------------------------------

    //Dimensions of game window
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 600;

    //Maximum that the game speed can be increased to
    //(a percentage, ex: a value of 300 = 300% speed, or 3x regular speed)
    private static final int MAX_GAME_SPEED = 300;
    //Interval that the speed changes when pressing speed up/down keys
    private static final int SPEED_CHANGE = 20;
    private static final String INTRO_SPLASH_FILE = "assets/newsplash.gif";
    private static final String DEATH_IMAGE = "assets/deathimage.png";
    private static final String BACKGROUND = "assets/skybackground.png";
    private static final String CHECKPOINT = "assets/checkpoint.png";
    //Key pressed to advance past the splash screen
    public static final int ADVANCE_SPLASH_KEY = KeyEvent.VK_ENTER;
    public static final int BACKDOOR_KEY = KeyEvent.VK_1;
    public static final int MAX_SCROLLS = 2917;

    public static final int HEALTH_BAR = 36;
    private static final int MAP_INTERVAL = 180;
    private static final String FIREWORKS = "assets/fireworks.gif";
    private static final String BALLOON = "assets/balloon.png";
    private static final String IS_THAT = "assets/isthat.png";
    private static final String THE_END = "assets/theend.png";
    private static final String BALLOON_GUY = "assets/balloonguy.png";

    private static final String WIN_IMAGE = "assets/youwin.gif";
    private static final String LOSE_IMAGE = "assets/youlose.gif";
    private static final String RECORDS = "records.txt";

    //A Random object for all your random number generation needs!
    public static final Random rand = new Random();
    private static ArrayList<Double> current_times = new ArrayList<Double>(10);

    //Player's current score
    private int score;
    private boolean animation_active = false;
    //Stores a reference to game's Player object for quick reference
    //(This Player will also be in the displayList)
    private Player player = new Player(0,-75);
    private boolean already_spawned = false;
    private boolean player_walking = false;
    private boolean backdoor = false;
    private int level_percent;
    private Entity balloon;
    private boolean game_end = false;
    private boolean loop_stop = false;
    private long start_time;
    private long last_time;

    public CookGame(){
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public CookGame(int gameWidth, int gameHeight){
        super(gameWidth, gameHeight);
    }

    //Performs all of the initialization operations that need to be done before the game starts
    protected void preGame(){
        setBackgroundImage(BACKGROUND);
        this.setSplashImage(INTRO_SPLASH_FILE);
        setTitleText("Jon Jump");
        this.setDebugMode(false);
        score = 0;
        super.refresh();
    }

    protected void startAnimation(){
      if (!isDebugEnabled()){
        setGameSpeed(200);
        animation_active = true;
        Floor nameblock = new Floor((DEFAULT_WIDTH-150)/2,150,150,150,"assets/logo.png");
        displayList.add(nameblock);
        while (super.startanim){
          super.refresh();
          super.handleKeyPresses();
        }
        Floor startfloor = new Floor((DEFAULT_WIDTH-150)/2,150,150,300,"assets/floor.png");
        displayList.add(0,startfloor);
        for (int i=0;i<190;i++){
          super.refresh();
          startfloor.moveY(2);
          if (i%10==0)
            startfloor.moveHeight(-3);
          startfloor.moveWidth(4);
          startfloor.moveX(-2);
        }

        startfloor.setX(0);
        startfloor.setWidth(DEFAULT_WIDTH);
        System.out.println(startfloor.getHeight());

        while (nameblock.getY()+150>0){
          nameblock.moveY(-7);
          super.refresh();
        }
        displayList.remove(displayList.indexOf(nameblock));
      }
      else
        displayList.add(0,new Floor(0,530,900,243));
      displayList.add(player);
      if (!isDebugEnabled()){scrollAnimation();}
      super.refresh();
      int fall = 0;
      while(player.getY()<450){
        fall++;
        player.moveY(fall);
        super.refresh();
      }
      animation_active = false;
      start_time = System.currentTimeMillis();
      last_time = start_time;



    }
    private void scrollAnimation(){
      setGameSpeed(1000);
      while (SCROLLS < MAX_SCROLLS){
        refresh();
        buildmap();
        moveEntities();
        //bruteLogic();
        player.moveX(7);
        if (player.getX()>359){
          scrollEntities(-1);
          SCROLLS ++;
          player.setX(359);
          already_spawned = false;
        }
      }
      try{TimeUnit.SECONDS.sleep(1);}
      catch(InterruptedException e){}
      while (SCROLLS>0){
        scrollEntities(2);
        SCROLLS = SCROLLS-2;
        moveEntities();
        refresh();
      }
      setGameSpeed(100);
      SCROLLS = 0;
      player.setX(0);
      player.setY(0-player.getHeight()-1);
      displayList.clear();
      displayList.add(player);
      player.updateSpawn(1);
      displayList.add(0,new Floor(0,530,900,243));
    }




    //Called on each game tick
    protected void updateGame(){

      buildmap();
      if(!loop_stop){
        playerMove();
        moveEntities();
        bruteLogic();
        healthLogic();
      }
      else
        gg(true);
    }





    private void healthLogic(){
      if (getSplashImage() == null){
        if (player.getLives()<=0){
          gg(false);
          loop_stop = true;
        }
        else{
          String hearts = "";
          String hp = "";
          for (int i=0;i<player.getLives();i++)
            hearts = hearts + "*";
          if (player.getLives()==0)
            hearts = "0";
          //level_percent = (int)((float)SCROLLS/MAX_SCROLLS*100);
          respawn(hearts);

          float hp_percent = (float)player.getHP()/player.getStartingHP();
          for (int j=0;j<(HEALTH_BAR*hp_percent);j++)
            hp = hp + "-";


            setTitleText("Remaining Lives: "+hearts+"\t\tHP: "+hp+"\t\tLevel Progress: "+(int)((float)SCROLLS/MAX_SCROLLS*100)+"%");
        }
      }
      if (backdoor)
        backDoor();
    }

    private void respawn(String hearts){
     try{
      if (player.tryRespawn()){
        already_spawned = false;
        player.immuneOff();
        player.updateImmune();
        setTitleText("Remaining Lives: "+hearts+"\t\tHP: 0"+"\t\tLevel Progress: "+(int)((float)SCROLLS/MAX_SCROLLS*100)+"%");
        refresh();
        TimeUnit.SECONDS.sleep(1);

        displayList.clear();
        Platform deathscreen = new Platform(0,0,getWindowWidth(),getWindowHeight(),DEATH_IMAGE);
        displayList.add(deathscreen);
        //TimeUnit.SECONDS.sleep(2);
        refresh();
        for (int i=6;i>0;i--){
          setTitleText("Respawning in "+i+" seconds");
          TimeUnit.SECONDS.sleep(1);
          refresh();
        }
        displayList.remove(deathscreen);
        player.setX(0);
        player.setY(-1-player.getHeight());
        displayList.add(player);

      if (player.getSpawn()==1)
        SCROLLS = 0;
      else if (player.getSpawn()==2)
        SCROLLS = 1260;

        displayList.add(0,new Floor(0,530,900,243));
        player.endRespawn();
        refresh();
        player.immune();
      }
    }
    catch (InterruptedException e){}
    }


    private void playerMove(){
      player.moveY(10);
      if (player.getY()>DEFAULT_HEIGHT)
        player.modifyHP(-1*player.getStartingHP());
      if (player.jump())
        handleCollisions("Ceiling");
      handleCollisions("Floor");
      if (player.updateImmune())
        handleAllCollisions("Floor");

      if (!player.isimmume()){
        if (player_walking)
          player.updateImage(true);
        else
          player.updateImage(false);
        player_walking = false;
      }
    }


    private void moveEntities(){
      for (int i=0;i<displayList.size();i++){
        boolean projectile_deleted = false;
        if ((displayList.get(i) instanceof Moving)&&(displayList.get(i) instanceof Collidable)){
          ((Moving)displayList.get(i)).move();

          if (displayList.get(i) instanceof Platform){
            handleCollisions("Ceiling");
            handleCollisions("Floor");
          }


          if (displayList.get(i) instanceof Projectile){
            for (int j=0;j<displayList.size();j++){
              if (!(displayList.get(j) instanceof Player || displayList.get(j) instanceof Enemy)){
                if (((Collidable)displayList.get(i)).checkRightCollisions(displayList.get(j))||(((Collidable)displayList.get(i)).checkLeftCollisions(displayList.get(j)))){
                  displayList.remove(i);
                  //System.out.println("Projectile Deleted");
                  projectile_deleted = true;
                  break;
                }
              }
            }
          }
          if (!projectile_deleted){
          if ((((Collidable)displayList.get(i)).checkRightCollisions(player)||(((Collidable)displayList.get(i)).checkLeftCollisions(player)))&&(!(player.isimmume()))){
            player.modifyHP(((Enemy)displayList.get(i)).getDamage());
            player.immune();

            if (displayList.get(i) instanceof Projectile){
              displayList.remove(i);
              projectile_deleted = true;
            }
          }
        //make game more efficient by deleting off map entities
          if (!projectile_deleted&&!animation_active){
            if (displayList.get(i).getX()+displayList.get(i).getWidth()<0)
              displayList.remove(i);
          }
          }
        }
      }
    }

    private void scrollEntities(int vector){
      for (int i=0;i<displayList.size();i++){
        if (displayList.get(i) instanceof Scrollable)
          ((Scrollable)displayList.get(i)).scroll(vector);
      }
    }

    private void bruteLogic(){
      for (int i=0;i<displayList.size();i++){
        boolean direction = false;
        if (displayList.get(i) instanceof Brute){
          if (player.getX()>displayList.get(i).getX()+displayList.get(i).getWidth()){
            ((Brute)displayList.get(i)).flip("right");
            direction = true;
          }
          else if (player.getX()+player.getWidth()<displayList.get(i).getX()){
            ((Brute)displayList.get(i)).flip("left");
            direction = false;
          }

          if (((Brute)displayList.get(i)).project()){
            displayList.add(new Projectile(displayList.get(i).getX(),displayList.get(i).getY()+displayList.get(i).getHeight()/3,direction));
          }
        }
      }
    }


    private void handleAllCollisions(String dontcollide){
      String[] collision_types = {"Floor","Ceiling","Right","Left"};
      for (int i=0;i<collision_types.length;i++)
        if (!collision_types[i].equals(dontcollide))
          handleCollisions(collision_types[i]);
    }

    protected void handleCollisions(String type){
      for (int i=0;i<displayList.size();i++){
        if (displayList.get(i) instanceof Collidable){

          if (displayList.get(i) instanceof Block){
            if (type.equals("Floor")&&((Collidable)displayList.get(i)).checkFloorCollisions(player)){
              player.allowJump();
              player.setY(displayList.get(i).getY()-player.getHeight()-1);
              break;
            }
            else if (type.equals("Ceiling")&&((Collidable)displayList.get(i)).checkCeilingCollisions(player)){
              player.setY(displayList.get(i).getY()+displayList.get(i).getHeight()+1);
            }
            else if (type.equals("Right")&&((Collidable)displayList.get(i)).checkRightCollisions(player)){
              player.setX(displayList.get(i).getX()-player.getWidth()-1);
            }
            else if (type.equals("Left")&&((Collidable)displayList.get(i)).checkLeftCollisions(player)){
              player.setX(displayList.get(i).getX()+displayList.get(i).getWidth()+1);
            }
          }


          if ((displayList.get(i) instanceof Enemy)){
            ((Enemy)displayList.get(i)).updateImmune();

            if ((!player.isimmume())&&(!(((Enemy)displayList.get(i)).isimmume()))){
              if (type.equals("Floor")&&((Collidable)displayList.get(i)).checkFloorCollisions(player)){
                if (!(displayList.get(i) instanceof Projectile)){

                  if (((Enemy)displayList.get(i)).updateHealth())
                    displayList.remove(i);
                  else
                    ((Enemy)displayList.get(i)).immune();
                  player.allowJump();
                  player.tryJump();
                }
                  else{
                    player.modifyHP(((Enemy)displayList.get(i)).getDamage());
                    player.immune();
                    displayList.remove(i);
                  }
                }
                else if ((type.equals("Ceiling")&&((Collidable)displayList.get(i)).checkCeilingCollisions(player))||(type.equals("Right")&&((Collidable)displayList.get(i)).checkRightCollisions(player))||(type.equals("Left")&&((Collidable)displayList.get(i)).checkLeftCollisions(player))){
                  if (!(displayList.get(i) instanceof Projectile)){
                    player.modifyHP(((Enemy)displayList.get(i)).getDamage());
                    player.immune();
                  }
                  else{
                    player.modifyHP(((Enemy)displayList.get(i)).getDamage());
                    player.immune();
                    displayList.remove(i);
                  }
                }
              }
            }
          }
        }
      }

    //Called once the game is over, performs any end-of-game operations
    protected boolean postGame(){
      long timer = 999999999;
      while (timer>0){
        timer --;
        refresh();
        ArrayList<Integer> keysPressed = window.getKeysPressed();
        for(Integer key : keysPressed){
          if (key == KEY_QUIT_GAME)
            System.exit(0);
          if (key == KeyEvent.VK_ENTER)
            return true;
        }
      }
      return false;
    }

    //Determines if the game is over or not
    //Game can be over due to either a win or lose state
    private void gg(boolean w_or_l){
      game_end = true;
      displayList.clear();

      setTitleText("GAME OVER - Press Escape to Quit or Enter to Play Again");

      if (w_or_l)
        setSplashImage(WIN_IMAGE);

      else
        setSplashImage(LOSE_IMAGE);
      refresh();
    }


    protected boolean isGameOver(){
      return game_end;
    }

    private void endAnimation(){
      healthLogic();
      player.immuneOff();
      player.updateImmune();
      player.walk();
      for (int i=0;i<52;i++){
        scrollEntities(-1);
        refresh();
      }
      balloon.setImageName(BALLOON_GUY);

      double time = (System.currentTimeMillis() - start_time) / 1000.0;
      current_times.add(time);
      System.out.println(time);

      logIntervals();

      displayList.remove(player);
      refresh();
      try{TimeUnit.SECONDS.sleep(2);}
      catch (InterruptedException e){}

      for (int j=0;j<500;j++){
        balloon.moveX(2);
        if (j%2==0)
          balloon.moveY(-2);
        if (j==80){
          displayList.add(0,new Platform(90,h1-200,400,400,FIREWORKS));
          displayList.add(0,new Platform(290,h1-200,400,400,FIREWORKS));
          displayList.add(0,new Platform(490,h1-200,400,400,FIREWORKS));
        }
        refresh();
      }
    }


    //Reacts to a single key press on the keyboard
    //Override's AbstractGame's handleKey
    protected void handleKey(int key){
        //first, call AbstractGame's handleKey to deal with any of the
        //fundamental key press operations
        super.handleKey(key);
        setDebugText("Key Pressed!: " + KeyEvent.getKeyText(key));
        //if a splash screen is up, only react to the advance splash key
        if (getSplashImage() != null){
            if (key == ADVANCE_SPLASH_KEY)
                super.setSplashImage(null);
            return;
        }
        else{
            if (key == BACKDOOR_KEY&&super.isPaused&&super.isDebugEnabled()){
              backdoor = true;
              super.isPaused = false;
            }
            if (key == super.KEY_PAUSE_GAME){
              if (super.isPaused == false)
                super.isPaused = true;
              else
                super.isPaused = false;
            }
            if (!isPaused){
              /*if (key == super.SPEED_UP_KEY){
              super.setGameSpeed(super.getGameSpeed()+SPEED_CHANGE);
              if (super.getGameSpeed() > MAX_GAME_SPEED)
                super.setGameSpeed(MAX_GAME_SPEED);
            }
              else if (key == super.SPEED_DOWN_KEY){
                super.setGameSpeed(super.getGameSpeed()-SPEED_CHANGE);
                if (super.getGameSpeed() < 1)
                  super.setGameSpeed(1);
            }*/

            playerMove(key);
          }
        }
      }


    private void playerMove(int key){
        if (key == super.UP_KEY){
          player.tryJump();
        }
        else if (key == super.DOWN_KEY){
        }
        else if (key == super.LEFT_KEY){
          player_walking = true;
          if (player.getX()-player.getMovementSpeed() < 0)
            player.setX(0);
          else
            player.setX(player.getX()-player.getMovementSpeed());

          handleCollisions("Left");
          if (!player.isFlipped())
            player.flipGuy();
        }
        else if (key == super.RIGHT_KEY) {
          player_walking = true;
          if (player.getX()>359){
            scrollEntities(-1);
            SCROLLS ++;
            already_spawned = false;
          }
          else{
          if (player.getX()+player.getMovementSpeed() > super.getWindowWidth()-75)
            player.setX(super.getWindowWidth()-75);
          else if (player.getX()+player.getMovementSpeed() > 360)
            player.setX(360);
          else
            player.setX(player.getX()+player.getMovementSpeed());
          }
          handleCollisions("Right");

          if (player.isFlipped())
            player.flipGuy();
      }
    }


    private void backDoor(){
      player.forceRespawn();
      refresh();
      respawn("A lot");
      SCROLLS = 2700;
      backdoor = false;
      player.op();
    }



//public Mini(int x,int y,boolean moving)
    //doen't need moving
//public Brute(int x,int y)
//Platform(int x,int y,boolean movey,int range){
    //doesn't need range

  //Platform(int x, int y, int width, int height, String image){
//--------------------Map Starts Here--------------------------

    private void buildmap(){
      boolean floor = true;
      //simply for faster typing
      int mi = MAP_INTERVAL;
      if (!already_spawned){
    //if (spawn){SCROLLS=mi*15;spawn=false;player.op();}

        int s = SCROLLS;
        if (s==0){
          a(new Mini(w0,h3-mh));a(new Mini(w3,h3-mh,t));
        }
        else if (s==mi*1){
          a(new Platform(w1,h2));a(new Platform(w2,h1));a(new Platform(w3,h2));
          a(new Platform(w3,h0));a(new Mini(w3,h0-mh));
          a(new Mini(w2,h3-mh,t));a(new Mini(w3,h3-mh,t));
          storeInterval();
        }
        else if (s==mi*2){
          a(new Platform(w1,h1-10));a(new Mini(w1+10,h1-10-mh));
          a(new Mini(w1+20,h3-mh));a(new Platform(w2,h2));
          a(new Platform(w2+30,h0));a(new Platform(w3,h1+30));
          a(new Mini(w3+10,h1+30-mh));a(new Mini(w2+20,h3-mh));
          a(new Mini(w2+25+sw,h3-mh));a(new Platform(w3+80,h3-150,"w"));
          storeInterval();
        }
        else if (s==mi*3){
          floor = f;
          a(new Platform(w0+20,h3-10));a(new Platform(w1+50,h1+30,t,125));
          a(new Platform(w2+50,h0));a(new Platform(w2,h3-10));
          a(new Mini(w2+40,h3-mh-10));
          storeInterval();
        }
        else if (s==mi*4){
          floor = f;
          a(new Platform(w0+140,h3-50));a(new Platform(w2+45,h2,300,25));
          a(new Mini(w2+75,h2-mh,t));
          storeInterval();
        }
        else if (s==mi*5){
          floor = f;
          a(new Platform(w0,h2+10,75,25));a(new Platform(w1,h3-115,75,25,t,200));
          a(new Platform(w2,h2+10,75,25));a(new Platform(w3,h2+10,75,25));
          a(new Mini(w0+5+10,h2+10-mh));a(new Mini(w2+10,h2+10-mh));
          a(new Mini(w3+25,h2+10-mh));
          storeInterval();
        }
        else if (s==mi*6){
          a(new Platform(w0+80,h3-100,"w"));a(new Brute(w3,h3-165));
          a(new Platform(w3,h3-15,90,15));a(new Platform(w0+80,h3-102,25,2));
          a(new Mini(w3-100,h3-mh-15));a(new Mini(w2-100,h3-mh,t));
          a(new Platform(w3-100,h3-15,100,15));
          storeInterval();
        }
        else if (s==mi*7){
          //checkpoint, blank area
          storeInterval();
        }
        else if (s==mi*7+(mi/2)){
          player.updateSpawn(2);
          a(new Platform(w2-w0/2-200,h0-200,400,400,FIREWORKS));
          a(new Platform(w2-w0/2-200,h0,400,200,CHECKPOINT));
          storeInterval();
        }
        else if (s==mi*8){
          a(new Platform(w0+80,h3-100,"w"));a(new Brute(w2));
          a(new Platform(w2,h3-164,90,15));a(new Platform(w2-75,h3-70,50,20));
          storeInterval();
        }
        else if (s==mi*9){
          floor = f;
          a(new Platform(w0,h3-100,300,25));a(new Platform(w2+90,h2,15,10,t,150));
          a(new Platform(w3+50,h1,15,10,t,250));a(new Mini(w0+30,h3-mh-100,t));
          storeInterval();
        }
        else if (s==mi*10){
          floor = f;
          a(new Platform(w0+90,h2,15,10,t,150));a(new Platform(w1+50,h1,15,10,t,250));
          a(new Platform(w3,h3,15,10));
          storeInterval();
        }
        else if (s==mi*11){
          a(new Platform(w0+80,h3-155-player.getHeight(),"w"));
          int spacing = 105;
          for (int i=0;i<3;i++){
            a(new Platform(w0+spacing,h3-30-player.getHeight()));
            spacing = spacing + 150;
          }
          a(new Platform(w3+80,h3-150,"w"));a(new Platform(w3+80,h3-300,"w"));
          a(new Mini(w3-100,h3-mh,t));a(new Mini(w0+395,h3-30-player.getHeight()-mh,t,55));
          a(new Platform(w0+400,h1-110,t,110));a(new Platform(w3+80,h3-350,"w"));
          storeInterval();
        }
        else if (s==mi*12){
          floor = f;
          a(new Platform(w3,h3-50,90,20));a(new Platform(w3,h0+130,90,20));
          a(new Brute(w3,h3-200));a(new Brute(w3,h0-20));
          a(new Platform(w0,h1,50,20,t,225));
          a(new Platform(w1,h2,50,20,t,150));
          a(new Platform(w2,h2-100,50,20,t,200));
          storeInterval();
        }
        else if (s==mi*13){
          floor = f;
          a(new Platform(w0+50,h3-80,10,5));a(new Platform(w1,h1,300,25,t,250));
          storeInterval();
        }
        else if (s==mi*14){
          a(new Mini(w0+50,h3-mh));
          storeInterval();
        }
        else if (s==mi*15){
          //System.out.println("end");
          a(new Platform(getWindowWidth()/2-150,getWindowHeight()/3-150,300,200,IS_THAT));
          balloon = new Platform(getWindowWidth()*2-280,h3-500,400,500,BALLOON);
          displayList.add(0,balloon);
          storeInterval();
        }
        else if (s==mi*15+mi/2){
          a(new Platform(getWindowWidth()/2-150,getWindowHeight()/3-150,300,200,THE_END));
        }
        else if (s==MAX_SCROLLS){
          endAnimation();
          loop_stop = true;
        }

        if (s>mi*15)
          floor = f;

        if ((s % mi == 0)&&floor)
          displayList.add(new Floor());
        already_spawned = true;
      }
    }
    //simply for faster typing
    private void a(Entity e){
      displayList.add(e);
    }


    private void storeInterval(){
      long temp_time = System.currentTimeMillis();
      double time = (temp_time - last_time) / 1000.0;
      last_time = temp_time;
      current_times.add(time);
      System.out.println(time);
    }

    private void logIntervals(){
      ArrayList<Double> existing = getExistingTimes();
      intervalPrint(existing);
      try{
        FileWriter record_file = new FileWriter(RECORDS);
        for (int i=0;i<existing.size();i++){
          if (existing.get(i) < current_times.get(i))
            record_file.write(existing.get(i)+"");
          else
            record_file.write(current_times.get(i)+"");
          record_file.write("\n");
        }
        record_file.close();
      }
      catch (IOException e) {}
    }


    private ArrayList<Double> getExistingTimes(){
      ArrayList<Double> existing = new ArrayList<Double>(10);

      try{
        File file = new File(RECORDS);
        Scanner scan = new Scanner(file);
        for (int i=0;i<current_times.size();i++)
          existing.add(Double.parseDouble(scan.nextLine()));
        //add final var for number of stages to prevent line not found exception
      }
      catch(FileNotFoundException e){}
      return existing;
    }


    private void intervalPrint(ArrayList<Double> existing){
      String spaces;
      String diff;
      System.out.println("Stage\tRecord\tYour Time\tDifference");
      for (int i=0;i<current_times.size();i++){
        if (i>9)
          spaces = "   \t";
        else
          spaces = "    \t";

          double dif = current_times.get(i) - existing.get(i);
          if (dif>0)
             diff = "+"+dif;
          else
             diff = dif+"";
          System.out.println((i+1)+spaces+"\t"+existing.get(i)+"s\t"+current_times.get(i)+"s  \t"+diff);
      }
    }
}
