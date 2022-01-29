package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) {
        File dictionary = new File(args[0]);
        int wordLength = Integer.parseInt(args[1]);
        int maxGuesses = Integer.parseInt(args[2]);
        EvilHangmanGame evil = new EvilHangmanGame();
        evil.setGuessesLeft(maxGuesses);
        try {
            evil.startGame(dictionary, wordLength);
        }
        catch (EmptyDictionaryException empty) {
            System.out.println("Empty Dictionary!");
        }
        catch (IOException io) {
            System.out.println("Can't read dictionary file.");
        }

        //Game loop:

        while (evil.getGuessesLeft() > 0) {
            System.out.println("You have " + evil.getGuessesLeft() + " guesses left");
            System.out.print("Used letters: ");
            boolean isFirst = true;
            for (char letter: evil.getGuessedLetters()) {
                if (!isFirst) {
                    System.out.print(" ");
                }
                System.out.print(letter);
                isFirst = false;
            }
            System.out.println("");
            System.out.println("Word: " + evil.getCurrentSetString());
            //System.out.println("Possible words left: " + evil.getCurrentSubsetSize());
            System.out.print("Enter guess: ");
            Scanner s = new Scanner(System.in);
            char guess = s.next().charAt(0);
            try {
                evil.makeGuess(guess);
            }
            catch (GuessAlreadyMadeException g) {
                System.out.println("Guess already made.");
            }
            if (!evil.checkIfCorrect(guess)) {
                System.out.println("Sorry, there are no " + guess + "'s");
            }
            else {
                System.out.println("Yes, there is " + evil.getLetterQuantity(guess) + " " + guess + "'s");
            }
            if (!evil.getCurrentSetString().contains("-")) {
                System.out.print("You win!");
                System.out.println("The word was: " + evil.getCurrentSetString());
                break;
            }
            System.out.println();
        }
        if (evil.getCurrentSetString().contains("-")){
            String finalWord = evil.getCurrentWordSet().first();
            System.out.println("You lose!");
            System.out.println("The word was: " + finalWord);
        }

        //System.out.println();
        //for (String word: evil.getCurrentWordSet()) {
            //System.out.println(word);
        //}
    }
}
