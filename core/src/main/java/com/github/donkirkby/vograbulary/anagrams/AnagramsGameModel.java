package com.github.donkirkby.vograbulary.anagrams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import com.github.donkirkby.vograbulary.ultraghost.WordList;

/**
 * Manage the state of a game, including the state of the board, current word, 
 * previously played words, and player scores.
 *
 * An AnagramsGameModel provides the data that is rendered on an AnagramsBoard 
 * object, and accepts user input via an AnagramsBoard object.
 *
 * @author Don Kirkby
 */
public class AnagramsGameModel implements Serializable {
    private static final long serialVersionUID = 346139871345262525L;
//  private static final int NUMBER_OF_LETTERS_IN_DECK = 50;
//  private static final int NUMBER_OF_VOWELS_IN_DECK = 10;
//  private static final String CONSONANTS = "BCDFHJKLMNPQRSTVWXYZ";
//  private static final String VOWELS = "AEIOU";

    private WordList wordList;
    private LetterSet letterSet;
    private HashMap<String, AnagramsPlayer> wordOwners;
    private HashSet<String> playedWords;
    private ArrayList<AnagramsPlayer> players;

    /**
     * Set the deck of letters to a given list. Resets the game state.
     * @param deck
     */
    public void setDeck(String deck) {
        letterSet = new LetterSet(deck);
        wordOwners = new HashMap<String, AnagramsPlayer>();
        players = new ArrayList<AnagramsPlayer>();
        playedWords = new HashSet<String>();
    }

    /**
     * Reveal the next letter from the deck.
     * @return the letter
     */
    public char revealLetter() {
        return letterSet.showNextLetter();
    }

    /**
     * Get the list of currently unclaimed letters. Letters are added to this
     * list by calling revealLetter() and removed by calling makeWord()
     * or changeWord().
     * @return
     */
    public String getUnclaimedLetters() {
        return letterSet.getVisibleLetters();
    }

    /**
     * Add a player to the game.
     * @param player The player to add.
     */
    public void addPlayer(AnagramsPlayer player) {
        players.add(player);
    }

    /**
     * Get all the players that were added by addPlayer().
     * @return a list of players.
     */
    public List<AnagramsPlayer> getPlayers() {
        return players;
    }

    /**
     * Get all words claimed by a player.
     * @param player The player who owns the words.
     * @return A list of words.
     */
    public List<String> getWords(AnagramsPlayer player) {
        ArrayList<String> words = new ArrayList<String>();
        for (Entry<String, AnagramsPlayer> entry : wordOwners.entrySet()) {
            if (entry.getValue() == player) {
                words.add(entry.getKey());
            }
        }
        return words;
    }

    /**
     * Make a word from unclaimed letters, and assign it to a player.
     * @param word The word to make.
     * @param player The player claiming the word.
     * @throws InvalidWordException When word is not in the dictionary, or
     * cannot be made from unclaimed letters.
     */
    public void makeWord(String word, AnagramsPlayer player) throws InvalidWordException {
        validateWord(word);
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if ( ! letterSet.reserveLetter(letter)) {
                letterSet.releaseReservedLetters();
                throw new InvalidWordException(
                        "The letter " + letter +
                                " is not available to make " + word + ".");
            }
        }
        letterSet.hideReservedLetters();
        playedWords.add(word);
        player.setScore(player.getScore() + word.length());
        wordOwners.put(word, player);
    }

    private void validateWord(String word) throws InvalidWordException {
        if (word.length() < 4) {
            throw new InvalidWordException(
                    "Words must be at least 4 letters long.");
        }
        if (playedWords.contains(word)) {
            throw new InvalidWordException(word + " has already been played.");
        }
        if ( ! wordList.contains(word)) {
            throw new InvalidWordException(
                    word + " is not in the dictionary.");
        }
    }

    public WordList getWordList() {
        return wordList;
    }

    public void setWordList(WordList mWordFinder) {
        this.wordList = mWordFinder;
    }

    /**
     * Make a new word from a claimed word, and assign it to a player.
     * @param oldWord The existing word.
     * @param newWord The word to make.
     * @param player The player claiming the word.
     * @throws InvalidWordException When oldWord has not been claimed, newWord
     *      is not in the dictionary or newWord is not makeable from all the
     *      letters in oldWord, plus zero or more unclaimed letters.
     */
    public void changeWord(
            String oldWord,
            String newWord,
            AnagramsPlayer player) throws InvalidWordException {
        validateWord(newWord);
        AnagramsPlayer oldPlayer = wordOwners.get(oldWord);
        if (oldPlayer == null) {
            throw new InvalidWordException(
                    oldWord + "is not a claimed word.");
        }

        LetterSet oldSet = new LetterSet(oldWord);
        oldSet.showAllRemaining();
        try
        {
            for (int i = 0;
                 i < newWord.length(); i++) {
                char letter = newWord.charAt(i);
                if ( ! oldSet.reserveLetter(letter) &&
                        ! letterSet.reserveLetter(letter)) {
                    throw new InvalidWordException(
                            letter + " is not available to make " + newWord + ".");
                }
            }
            oldSet.hideReservedLetters();
            if (oldSet.getVisibleLetters().length() > 0) {
                throw new InvalidWordException(
                        "Some letters of " + oldWord + " were not used in " +
                                newWord + ".");
            }
            letterSet.hideReservedLetters();
            playedWords.add(newWord);
            wordOwners.put(newWord, player);
            wordOwners.remove(oldWord);
        }
        finally {
            letterSet.releaseReservedLetters();
        }
        player.setScore(player.getScore() + newWord.length());
        oldPlayer.setScore(oldPlayer.getScore() - oldWord.length());
    }

    public boolean isDeckEmpty() {
        return letterSet.getRemainingCount() <= 0;
    }
}