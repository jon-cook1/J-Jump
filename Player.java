//The entity that the human player controls in the game window
//The player moves in reaction to player input
public class Player extends Entity {

  //Location of image file to be drawn for a Player
  private static final String PLAYER_IMAGE_FILE = "assets/guy.gif";
  private static final String PLAYER_IMAGE_FLIPPED = "assets/guyflipped.gif";
  private static final String STANDING = "assets/guystill.png";
  private static final String STANDING_FLIPPED = "assets/guystillflipped.png";

  private static final String INVIS_IMG = "assets/blank.png";
  //Dimensions of the Player
  private static final int PLAYER_WIDTH = 75;
  private static final int PLAYER_HEIGHT = 75;
  //Default speed that the Player moves (in pixels) each time the user moves it
  private static final int DEFAULT_MOVEMENT_SPEED = 5;
  //Starting hit points
  private static final int STARTING_LIVES = 3;
  private static final int STARTING_HP = 100;

  //Current movement speed
  private int movementSpeed;
  //Remaining Hit Points (HP) -- indicates the number of "hits" (ie collisions
  //with Avoids) that the player can take before the game is over
  private int hp;
  private int lives;

  private boolean jump;
  private int jump_count;
  private boolean jump_allowed;
  private boolean immune;
  private int immune_timer;
  private boolean respawn;
  private int spawn_point;

  private boolean flipped;

  public Player(){
    this(0, 0);
  }

  public Player(int x, int y){
    super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_IMAGE_FILE);
    this.hp = STARTING_HP;
    this.movementSpeed = DEFAULT_MOVEMENT_SPEED;

    this.jump = false;
    this.jump_count = 0;
    this.jump_allowed = true;
    immune = false;
    immune_timer = 0;
    lives = STARTING_LIVES;
    respawn = false;
    hp = STARTING_HP;
    spawn_point = 1;
    flipped = false;
  }
  public void immuneOff(){
    immune_timer = 0;
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
        if (this.getImageName() != INVIS_IMG)
          this.setImageName(INVIS_IMG);
        else
          updateImage(false);
    }
    if (immune_timer == 0){
      immune = false;
      updateImage(false);
      return true;
    }
    }
    return false;
  }

  public void tryJump(){
    if (jump_allowed)
      jump = true;
  }

  public void allowJump(){
    if (!jump_allowed){
      jump_allowed = true;
      jump_count = 0;
    }
  }

  public boolean jump(){
    if (jump){
      if (jump_count > 0)
        this.moveY(-20);
      jump_count++;
      if (jump_count == 17){
        jump = false;
        jump_allowed = false;
      }
    return true;
    }
  return false;
  }


  //Retrieve and set the Player's current movement speed
  public int getMovementSpeed(){
    return this.movementSpeed;
  }

  public void setMovementSpeed(int newSpeed){
    this.movementSpeed = newSpeed;
  }


  //Retrieve the Player's current HP
  public int getHP(){
    return hp;
  }


  //Set the player's HP to a specific value.
  //Returns an boolean indicating if Player still has HP remaining
  public boolean setHP(int newHP){
    this.hp = newHP;
    return (this.hp > 0);
  }

  //Set the player's HP to a specific value.
  public void modifyHP(int delta){

    if (immune == false){
      this.hp += delta;
      if (this.getHP()<=0){
        this.lives --;
        this.setHP(STARTING_HP);
        respawn = true;
      }
    }
  }
  public void flipGuy(){
    if (isFlipped()){
      this.setImageName(PLAYER_IMAGE_FILE);
      flipped = false;
    }
    else{
      this.setImageName(PLAYER_IMAGE_FLIPPED);
      flipped = true;
    }
  }
  public boolean isFlipped(){
    return flipped;
  }
  public void updateImage(boolean walking){
    if (walking){
      if (flipped)
        this.setImageName(PLAYER_IMAGE_FLIPPED);
      else
        this.setImageName(PLAYER_IMAGE_FILE);
    }
    else{
      if (flipped)
        this.setImageName(STANDING_FLIPPED);
      else
        this.setImageName(STANDING);
    }
  }

  public void forceRespawn(){
    respawn = true;
  }
  public void op(){
    lives = 20;
    hp=2;
    immune_timer = 999999999;
    immune = true;
  }
  public int getLives(){
    return lives;
  }
  public void endRespawn(){
    this.setImageName(STANDING);
    respawn = false;
  }
  public int getStartingHP(){
    return STARTING_HP;
  }
  public boolean tryRespawn(){
    return respawn;
  }
  public void updateSpawn(int spawn){
    spawn_point = spawn;
  }
  public int getSpawn(){
    return spawn_point;
  }
  public void walk(){
    setImageName(PLAYER_IMAGE_FILE);
  }
}
