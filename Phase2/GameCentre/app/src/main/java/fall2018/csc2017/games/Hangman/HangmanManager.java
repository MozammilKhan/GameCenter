package fall2018.csc2017.games.Hangman;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fall2018.csc2017.games.Game;

/**
 * Manages a Hangman object.
 */
class HangmanManager implements Game, Serializable {
    /**
     * The current score.
     */
    private int score;
    /**
     * The Hangman being managed.
     */
    private Hangman hangman;
    /**
     * The number of undos; default set to 3.
     */
    private int numUndos = 3;
    /**
     * The difficulty of the word; default set to medium.
     */
    private String difficulty = "medium";
    /**
     * The map containing list of all possible words, sorted by difficulty keys.
     */
    private final static Map<String, ArrayList<String>> words = new HashMap<String, ArrayList<String>>() {{
        put("easy", new ArrayList<String>() {{
            add("CHARGER");
            add("TABLET");
            add("SYSTEM");
            add("INTERNET");
        }});
        put("medium", new ArrayList<String>() {{
            add("AGGRESSIVE");
            add("FUTURISTIC");
            add("ANACONDA");
            add("EINSTEIN");
        }});
        put("hard", new ArrayList<String>() {{
            add("CROQUET");
            add("BAGPIPES");
            add("BANJO");
            add("GAZEBO");
        }});
    }};

    /**
     * Creates a new manager for a specific word.
     */
    HangmanManager(int mode, String word) {
        hangman = new Hangman(word);
    }

    /**
     * Manage a new shuffled hangman game.
     *
     * @param difficulty the difficulty
     */
    HangmanManager(String difficulty) {
        hangman = new Hangman(getNewWord());
        this.difficulty = difficulty;
        setNumUndos(3);
    }

    @Override
    public String getDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        hangman = new Hangman(getNewWord());
    }

    @Override
    public String getGameId() {
        return "hangman";
    }

    @Override
    public boolean highTopScore() {
        return false;
    }

    /**
     * Returns the number of undos
     *
     * @return the number of undos
     */
    public int getNumUndos() {
        return numUndos;
    }

    /**
     * Sets the number of undos to a specified one.
     *
     * @param numUndos number of undos
     */
    public void setNumUndos(int numUndos) {
        this.numUndos = numUndos;
    }

    @Override
    public boolean undo() {
        return false;
    }

    /**
     * Returns the hangman for this manager
     *
     * @return the hangman for this game
     */
    Hangman getHangman() {
        return hangman;
    }

    /**
     * Generates a new word based on difficulty.
     *
     * @return a word for the user to guess
     */
    String getNewWord() {
        Random rand = new Random();
        ArrayList<String> chosenWords;
        switch (difficulty) {
            case "easy":
                chosenWords = words.get("easy");
                break;
            case "hard":
                chosenWords = words.get("hard");
                break;
            default:
                chosenWords = words.get("medium");
                break;
        }
        String newWord = chosenWords.get(rand.nextInt(chosenWords.size()));

        while (hangman != null && newWord.equals(hangman.currWord)) {
            newWord = chosenWords.get(rand.nextInt(chosenWords.size()));
        }

        return newWord;

    }

    /**
     * Returns if the puzzle was solved.
     *
     * @return if the game is solved
     */
    public boolean puzzleSolved() {
        return (new String(hangman.getRevealedWord())).equals(hangman.currWord);
    }

    /**
     * Returns if the game has been lost.
     *
     * @return if the game has been lost.
     */
    public boolean puzzleLost() {
        return hangman.getCurrentGuesses() == hangman.TOTAL_GUESSES;
    }

    /**
     * Returns if the guess was valid.
     *
     * @param guess, the users guess
     * @return if guess is a valid guess, single char from english alphabet
     */
    boolean isValid(String guess) {
        return guess.matches("[a-zA-Z]");
    }

    /**
     * Returns the current score for this hangman game.
     *
     * @return the current score for this hangman game.
     */
    int getScore() {
        int modifier;
        switch (getDifficulty()) {
            case "easy":
                modifier = 3;
                break;
            case "medium":
                modifier = 6;
                break;
            case "hard":
                modifier = 9;
                break;
            default:
                modifier = 3;
                break;
        }
        return modifier * (6 - hangman.getCurrentGuesses());
    }
}
