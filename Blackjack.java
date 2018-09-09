// Blackjack Card Game
// Alexis Dionne
 
import java.util.*;
import java.util.Stack;
import java.util.Scanner;
import java.util.List;
 
public class Blackjack {
  
  boolean tryParseInt(String value) {
    boolean isInt;
    try {
      Integer.parseInt(value);
      isInt = true;
    } catch (NumberFormatException e) {
      isInt = false;
    }
    return isInt;
  }
  
  boolean tryParseResponse (String value) {
    boolean isValid;
    if (value.contains("y") || value.contains("n")) 
      isValid = true;
    else
      isValid = false; 
    return isValid;
  }
  
  int placeBets (int currentWinnings) {
    Scanner reader = new Scanner(System.in);  // Reading from System.in
    boolean badBet = true;
    System.out.println("You have $" + currentWinnings + " to bet.");
    System.out.print("How much would you like to bet?  ");
    String value = reader.next();
    
    while (!tryParseInt(value)) {
      System.out.print("You've entered an invalid number. How much would you like to bet?  ");
      value = reader.next();
    }
    int betAmount = Integer.parseInt(value);
  
    while (badBet) {
      if (betAmount > currentWinnings) {
        System.out.print("You don't have enough to make that bet. Please enter a new bet amount:  ");
        betAmount = reader.nextInt();
      }
      else if (betAmount <= 0) {
        System.out.print("$" + betAmount + " is not a valid bet. Please enter a new bet amount:  ");
        betAmount = reader.nextInt();
      }
      else {
        badBet = false;
      }
    }
    
    return betAmount;
  }
  
  int calculateTotal (ArrayList<Integer> hand) {
    boolean containsAce = false;
    int total = 0;
    for(int i = 0; i < hand.size(); i++) {
      if (hand.get(i) == 11) {
        containsAce = true;
      }
      total += hand.get(i);
    }
    
    if (total > 21 && containsAce) {
      total -= 10;
    }
    return total;
  }
  
  void runGame () {
    // runs a game of blackjack
    Deck deck = new Deck();
    int currentWinnings = 200; // initial amount the user can bet with
    boolean keepPlaying = true;
    Scanner reader = new Scanner(System.in);
    
    while (keepPlaying && (currentWinnings > 0)) {
      int betAmount = 0;
      int choice = 0;
      ArrayList<Integer> playerHand = new ArrayList<Integer>();
      int playerHandValue = 0;
      ArrayList<Integer> dealerHand = new ArrayList<Integer>();
      int dealerHandValue = 0;
      
      System.out.println();
      betAmount = placeBets(currentWinnings);
      
      // initial deal and calculations
      playerHand.add(deck.dealCard());
      playerHand.add(deck.dealCard());
      dealerHand.add(deck.dealCard());
      dealerHand.add(deck.dealCard());
      
      playerHandValue = calculateTotal(playerHand);
      System.out.print("\n\nYour hand value is " + playerHandValue);
      
      dealerHandValue = calculateTotal(dealerHand);
      System.out.println("\nThe dealer's first card is worth " + dealerHand.get(0)+ "\n");
      
      while (playerHandValue < 21) { // player's turn
        boolean invalidChoice = true;
        System.out.print("\nDo you want to:\n1. Stand\n2. Hit\n3. Double Down\nChoice: ");
        String value = reader.next();
        while (invalidChoice) {
          while (!tryParseInt(value)) {
            System.out.print("You've entered an invalid number. What would you like to do?  ");
            value = reader.next();
          }
          choice = Integer.parseInt(value);
          if (choice == 1 || choice == 2 || choice == 3) {
            invalidChoice = false;
          }
          else if (choice == 3 && betAmount*2 <= currentWinnings) {
            invalidChoice = true;
            System.out.print("You've entered an invalid number. What would you like to do?  ");
            value = reader.next();
          }
          else {
            System.out.print("You've entered an invalid number. What would you like to do?  ");
            value = reader.next();
          }
        }
        
        if (choice == 1) { // player stands and nothing happens
          break;
        }
        else if (choice == 2) { // hit
          playerHand.add(deck.dealCard());
          playerHandValue = calculateTotal(playerHand);
          System.out.println("Your hand is now worth:  " + playerHandValue);
        }
        else if (choice == 3) { // double down
          playerHand.add(deck.dealCard());
          playerHandValue = calculateTotal(playerHand);
          System.out.println("Your hand is now worth:  " + playerHandValue);
          betAmount *= 2;
          break;
        }
      }
      
      // dealer's turn
      System.out.println("\nThe dealer has " + dealerHandValue + ".");
      while (dealerHandValue <= 16) {
        dealerHand.add(deck.dealCard());
        dealerHandValue = calculateTotal(dealerHand);
        if (dealerHandValue > 21) {
          System.out.println("The dealer hit and busted.");
        }
        else { 
          System.out.println("The dealer hit and now has " + dealerHandValue + ".");
        }
      }
      System.out.println();
      
      // who wins
      if (playerHandValue > 21) { // player busted
        currentWinnings -= betAmount;
        if (currentWinnings > 0) {
          System.out.print("You lost! You now have $" + currentWinnings + "\nPlay again(y or n)?  ");
        }
        else {
          System.out.print("You have no more money :(");
        }
      }
      else if (playerHandValue > dealerHandValue || (playerHandValue <= 21 && dealerHandValue > 21)) {
        currentWinnings += betAmount;
        System.out.print("You win! You now have $" + currentWinnings + "\nPlay again(y or n)?  ");
      }
      else {
        if (dealerHandValue == 21 && playerHandValue == 21) { 
          System.out.print("You tied! You still have $" + currentWinnings + "\nPlay again(y or n)?  ");
        }
        else if (dealerHandValue <= 21 && playerHandValue < dealerHandValue) {
          currentWinnings -= betAmount;
          if (currentWinnings > 0) {
            System.out.print("You lost! You now have $" + currentWinnings + "\nPlay again(y or n)?  ");
          }
          else {
            System.out.print("You have no more money :(");
            System.exit(0);
          }
        }
      }
      
      String value = reader.next();
      while (!tryParseResponse(value)) {
        System.out.print("That's not a valid response. \nPlay again(y or n)?  ");
        value = reader.next();
      }
      if (value.contains("n")) {
        System.out.println("Thank you for playing!");
        System.exit(0);
      }
      else if (value.contains("y")) {
        System.out.println();
      }
    }
  }

  public static void main (String[] args) {
    Blackjack game = new Blackjack();
    game.runGame();
  }
}
 
class Deck {
  // an instance of a deck object
  Stack<Integer> cards;
  
  Deck () {
    // constructor
    cards = new Stack<Integer>();
    int cardValue = 1;
    for (int i = 0; i < 52; i++) {
      if (i%4 == 0) {
        cardValue++;
        if (cardValue > 10 && i >= 40)
          cardValue = 10; // face cards are all worth 10
      }
      cards.push(cardValue);
    }
    
    shuffleDeck();
    // System.out.print("\nShuffled!!  ");
    // showDeck();
  }
  
  void shuffleDeck () {
    // shuffles the cards in the deck randomly
    int index, temp;
    Random random = new Random();
    int[] shuffledDeck = new int[52];
    int i = 0;
    // move cards into an array
    while (!cards.empty()){
      shuffledDeck[i++] = cards.pop();
    }
    // shuffle the array
    for (i = 52 - 1; i > 0; i--) {
      index = random.nextInt(i+1);
      temp = shuffledDeck[index];
      shuffledDeck[index] = shuffledDeck[i];
      shuffledDeck[i] = temp;
    }
    i = 0;
    while (cards.size() < 52) {
      cards.push(shuffledDeck[i++]);
    }
  }
  
  int dealCard () {
    return cards.pop();
  }
  
  void showDeck () {
    int[] shuffledDeck = new int[52];
    int i = 0;
    while (!cards.empty()){
      shuffledDeck[i++] = cards.pop();
    }
    i = 0;
    while (cards.size() < 52) {
      System.out.print(shuffledDeck[i] + ", ");
      cards.push(shuffledDeck[i++]);
    }
  }
}
 