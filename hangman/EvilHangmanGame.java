package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    public EvilHangmanGame() {}
    /**
     * Starts a new game of evil hangman using words from <code>dictionary</code>
     * with length <code>wordLength</code>.
     *	<p>
     *	This method should set up everything required to play the game,
     *	but should not actually play the game. (ie. There should not be
     *	a loop to prompt for input from the user.)
     *
     * @param dictionary Dictionary of words to use for the game
     * @param wordLength Number of characters in the word to guess
     * @throws IOException if the dictionary does not exist or an error occurs when reading it.
     * @throws EmptyDictionaryException if the dictionary does not contain any words.
     */

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner s = new Scanner(dictionary);
        while (s.hasNextLine()) {
            String str = s.nextLine();
            String[] words = str.split(" ");
            currentWordSet.addAll(Arrays.asList(words));
        }
        TreeSet<String> copySet = new TreeSet<>(currentWordSet);
        for (String word: copySet) {
            if (word.length() != wordLength) {
                currentWordSet.remove(word);
            }
        }
        if (currentWordSet.isEmpty()) {
            throw new EmptyDictionaryException();
        }
        if (wordLength > 0) {
            currentSetString.append("-".repeat(Math.max(0, wordLength)));
        }
        else {
            throw new EmptyDictionaryException();
        }
    }
    /**
     * Make a guess in the current game.
     *
     * @param guess The character being guessed, case insensitive
     *
     * @return The set of strings that satisfy all the guesses made so far
     * in the game, including the guess made in this call. The game could claim
     * that any of these words had been the secret word for the whole game.
     *
     * @throws GuessAlreadyMadeException if the character <code>guess</code>
     * has already been guessed in this game.
     */
    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        if (!Character.isAlphabetic(guess)) {
            System.out.println("Guess is not a letter!");
            return currentWordSet;
        }
        char currentGuess = Character.toLowerCase(guess);
        if (!guessesMade.contains(currentGuess)) {
            guessesMade.add(currentGuess);
        }
        else {
            throw new GuessAlreadyMadeException();
        }

        Map<String, TreeSet<String>> subsets = new HashMap<>();

        for (String word: currentWordSet) {
            String subsetName = getSubsetName(word, currentGuess);
            if (!subsets.containsKey(subsetName)) {
                TreeSet<String> subset = new TreeSet<>();
                subset.add(word);
                subsets.put(subsetName, subset);
            }
            else {
                subsets.get(subsetName).add(word);
            }
        }

        String largestSubsetName = "";
        int maxSize = 0;
        TreeSet<String> largestSubset = null;
        for (Map.Entry<String, TreeSet<String>> entry : subsets.entrySet()) {
            TreeSet<String> currentSubset = entry.getValue();
            String currentSubsetName = entry.getKey();
            if (currentSubset.size() > maxSize) {
                largestSubsetName = currentSubsetName;
                largestSubset = currentSubset;
                maxSize = currentSubset.size();
            }
            else if (currentSubset.size() == maxSize) {
                largestSubsetName = breakTie(largestSubsetName, currentSubsetName, currentGuess);
                largestSubset = subsets.get(largestSubsetName);
            }
        }
        setCurrentWordSet(largestSubset);
        setCurrentSetString(largestSubsetName);
        if (!checkIfCorrect(currentGuess)) {
            guessesLeft--;
        }
        return currentWordSet;
    }

    public String getSubsetName(String word, char guess) {
        StringBuilder subsetName = new StringBuilder(currentSetString);
        for (int i = 0; i < word.length(); ++i) {
            if (word.charAt(i) == guess) {
                subsetName.setCharAt(i, guess);
            }
        }
        return subsetName.toString();
    }

    public String breakTie(String s1, String s2, char guess) {
        int s1GuessCount = 0;
        int s2GuessCount = 0;
        ArrayList<Boolean> s1EarliestGuessIndexes = new ArrayList<>();
        ArrayList<Boolean> s2EarliestGuessIndexes = new ArrayList<>();

        for (int i = 0; i < s1.length(); ++i) {
            if (s1.charAt(i) == guess) {
                s1GuessCount++;
                s1EarliestGuessIndexes.add(true);
            }
            else {
                s1EarliestGuessIndexes.add(false);
            }
            if (s2.charAt(i) == guess) {
                s2GuessCount++;
                s2EarliestGuessIndexes.add(true);
            }
            else {
                s2EarliestGuessIndexes.add(false);
            }
        }
        if (s1GuessCount > s2GuessCount) {
            return s2;
        }
        else if (s1GuessCount < s2GuessCount) {
            return s1;
        }
        else { // they're equal
            int currentIndex = s1EarliestGuessIndexes.size() - 1;
            while (s1EarliestGuessIndexes.get(currentIndex) == s2EarliestGuessIndexes.get(currentIndex)) {
                currentIndex--;
            }
            if (s1EarliestGuessIndexes.get(currentIndex)) {
                return s1;
            }
            else {
                return s2;
            }
        }
    }

    public boolean checkIfCorrect(char guess) {
        for (int i = 0; i < currentSetString.length(); ++i) {
            if (currentSetString.charAt(i) == guess) {
                return true;
            }
        }
        return false;
    }

    public int getLetterQuantity(char guess) {
        int letterQuantity = 0;
        for (int i = 0; i < currentSetString.length(); ++i) {
            if (currentSetString.charAt(i) == guess) {
                letterQuantity++;
            }
        }
        return letterQuantity;
    }

    public void setCurrentWordSet(TreeSet<String> currentWordSet) {
        this.currentWordSet = currentWordSet;
    }

    public void setCurrentSetString(String subsetString) {
        this.currentSetString = new StringBuilder(subsetString);
    }

    public void setGuessesLeft(int guessesLeft) {
        this.guessesLeft = guessesLeft;
    }

    public int getGuessesLeft() {
        return guessesLeft;
    }

    public TreeSet<String> getCurrentWordSet() {
        return currentWordSet;
    }

    public int getCurrentSubsetSize() {
        return currentWordSet.size();
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessesMade;
    }
    public String getCurrentSetString() {
        return currentSetString.toString();
    }

    private TreeSet<String> currentWordSet = new TreeSet<String>();
    private StringBuilder currentSetString = new StringBuilder();
    private TreeSet<Character> guessesMade = new TreeSet<Character>();
    private int guessesLeft = -1;
}
