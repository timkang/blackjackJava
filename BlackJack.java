import java.io.Console;
import java.util.ArrayList;
import java.util.Random;

class BlackJack {

  static class Player {
    private ArrayList<Integer> hand;
    private int chips;
    private Random rng;   
 
    public Player(){
      hand = new ArrayList<Integer>();
      chips = 100;
      rng = new Random();
    }

    public void draw(int number){
      for(int i=0; i<number; i++){
        int tmp = rng.nextInt(13) + 1;
        this.hand.add(tmp);
      } 
    }

    public void discard(){
      this.hand.clear();
    }

    public void addChips(int number){
      this.chips = this.chips + number;
    }

    public void betChips(int number){
      this.chips = this.chips - number;
    }
 
    public int getChipCount(){
      return this.chips;
    } 

    public int countTotal(){
      int total = 0;
      int aceCount = 0;

      for(int i: this.hand){
        if(i < 10){
          total += i;
        } else if(i < 13){
          total += 10;
        } else {
          total += 11;
          aceCount++;
        }
      }

      if (total <= 21) {
        return total;
      } else {
        while(total > 21 && aceCount > 0){
          total -= 10;
          aceCount--;
        }
        return total;
      }
    }

    public void printStatus(){
      System.out.print("Your hand is: ");
  
      for(int i: this.hand){
        if(i < 10){
          System.out.print(i + " ");
        } else if (i == 10) {
          System.out.print("J ");
        } else if (i == 11) {
          System.out.print("Q ");
        } else if (i == 12) {
          System.out.print("K ");
        } else if (i == 13) {
          System.out.print("A ");
        }
      }
      System.out.println();
    }

  }

  public static void main(String args[]){
    //used to get user input
    String input;
    Console console = System.console();

    //create the Player and the Dealer
    Player player = new Player();
    Player dealer = new Player();

    //start the game
    System.out.println("Welcome to BlackJack vs the stupid, evil AI..");
    System.out.println();
    boolean roundStart = true;
    int roundNum = 0;
    int pool = 0;
    int multiplier = 2;

    //main game loop
    while(true){

      //only run once at the beginning of each round
      if (roundStart){
      
        //overall lose condition
        if(player.getChipCount() < 1){
          System.out.println("You're all out of money! Game over after " + roundNum + " rounds!");
          break;
        }
        
        pool = 0;
        player.discard();
        dealer.discard();
        player.draw(2);       
        dealer.draw(2); 
        roundNum++;

        //start the round, place bets
        System.out.println(); 
        System.out.println("Welcome to Round #" + roundNum);
        System.out.println("You currently have " + player.getChipCount() + " chips");
        boolean wagerReceived = false;
        boolean quitMe = false;       
 
        while(!wagerReceived){
          input = console.readLine("Input wager amount or [Q]uit: ");
          if(input.toLowerCase().trim().equals("q")){ 
            System.out.println();
            System.out.println("GG! Game over after " + roundNum + " rounds! ");
            System.out.println("Final chip count is: " + player.getChipCount());
            quitMe = true;
            break; 
          }

          try {
            int tmp = Integer.parseInt(input);
            if(tmp < 1 || tmp > player.getChipCount()){
              System.out.println("Amount too high or low, try again.");
            } else {
              player.betChips(tmp);
              pool += tmp;
              System.out.println("Betting " + tmp + " chips.");
              wagerReceived = true;
            } 
          } catch (NumberFormatException e) {
            System.out.println("Invalid user input, try again.");
          }
        }
        if (quitMe) {
          break;
        }
        System.out.println();
        roundStart = false;
      } else {

      //win/lose conditions
      if(player.countTotal() == 21) {
        player.printStatus();
        System.out.println("21! You win the round! Drawing a new hand...");
        player.addChips(pool * multiplier);
        roundStart = true;
        continue;
      } else if( player.countTotal() >= 22){
        player.printStatus();
        System.out.println("Player loses round, cards over 21.");
        roundStart = true;
        continue;
      } else if( dealer.countTotal() >= 17){

        player.printStatus();
        System.out.println("Total card value: " + player.countTotal());
        System.out.println("Dealer wants to have a showdown with the cards!");
        input = console.readLine("Y/N? ").toLowerCase().trim();

        if(input.equals("y")) {

          if(dealer.countTotal() > player.countTotal() && dealer.countTotal() < 22) {
            System.out.println("Dealer wins round " + dealer.countTotal() + " vs " + player.countTotal());
          } else if (dealer.countTotal() == player.countTotal()){
            System.out.println("Tie! You get your chips back for another go."); 
            player.addChips(pool);
          } else {
            System.out.println("Player wins round " + player.countTotal() + " vs " + dealer.countTotal());
            player.addChips(pool * multiplier);
          }
          roundStart = true;
          continue;
        }
        System.out.println();
      }

      //player choices hit or stand
      System.out.println("You currently have " + player.getChipCount() + " chips");
      player.printStatus();
      System.out.println("Pool is currently: " + pool);
      System.out.println("Total card value: " + player.countTotal());
      System.out.println("[H]it, [S]tand, or [Q]uit?"); 
      input = console.readLine("Type in H/S/Q: ").toLowerCase().trim();

      if(input.equals("h")){
        player.draw(1);
        System.out.println("Hit!");
      } else if (input.equals("s")){
        System.out.println("Stand!");
      } else if(input.equals("q")){ 
        System.out.println();
        System.out.println("GG! Game over after " + roundNum + " rounds! ");
        System.out.println("Final chip count is: " + player.getChipCount());
        break;
      } else {
        System.out.println("Invalid user input, try again.");
      }

      if (dealer.countTotal() < 17){
        dealer.draw(1);
      }
      System.out.println();
    }

    }

  }

}
