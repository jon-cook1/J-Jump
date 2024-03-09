//Contains main... run me to launch the game!
public class launch{
    private static boolean again = true;
    //Initializes and launches the game
    public static void main(String[] args){
      while (again){
          AbstractGame game = new CookGame();
          game.setDebugMode(true);
          if (!game.play())
            again = false;
      }
    }

}
