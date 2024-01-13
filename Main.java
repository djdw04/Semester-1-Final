import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static String[] words;
    static int maxGuesses = 7;

    private static String[] readString(String inputFilename) throws FileNotFoundException { /* Scans the information on the file */
        File file = new File(inputFilename);
        Scanner scanner = new Scanner(file);
        int numberOfLinesInFile = countLinesInFile(inputFilename);
        String[] data = new String[numberOfLinesInFile];
        int index = 0;
        while (scanner.hasNextLine()) {
            data[index++] = scanner.nextLine();
        }
        scanner.close();
        return data;
    }

    private static int countLinesInFile(String inputFilename) throws FileNotFoundException { /* Scans the information on the file */
        File file = new File(inputFilename);
        Scanner scanner = new Scanner(file);
        int lineCount = 0;
        while (scanner.hasNextLine()) {
            lineCount++;
            scanner.nextLine();
        }
        scanner.close();
        return lineCount;
    }


    static String getRandomWord() { /* Randomizes a word for the game */
        int randomIndex = (int) (Math.random());
        return words[randomIndex];
    }


    public static boolean checkSingleLetter(String guess) { /* checks if the user enters a guess that is a single character, and that the character is from a-z or A-Z */
        return guess.length() == 1 && guess.matches("[a-zA-Z]");
    }

    static String userGuess(String[] guessedLetters, StringBuilder incorrectGuessesBuilder) { /* Says user input is false */
        Scanner scanner = new Scanner(System.in);
        String guess;
        boolean validGuess = false;
        

        while (!validGuess) { /* Asks the user to enter and scans the input, making it upper case */
            System.out.print("\nWhat is your guess: ");
            guess = scanner.nextLine().toUpperCase();

            if (!checkSingleLetter(guess)) { /* Check if the guess is a single letter */
                System.out.println("Please enter a single letter from A to Z.");
                continue;
            }

            if (Arrays.asList(guessedLetters).contains(guess)) { /* Asks the user to enter a different letter if a duplicate is entered (For correct guess) */
                System.out.println("You have already guessed that letter. Please enter a different letter.");
                continue;
            }

            if (incorrectGuessesBuilder.toString().contains(guess)) { /* Asks the user to enter a different letter if a duplicate is entered (For incorrect guess) */
                System.out.println("You have already guessed that letter. Please enter a different letter.");
                continue;
            }

            validGuess = true; /* Says user input is true, after successfully going through all these requirements */
            return guess;
        }

        return null;
    }

    public static void main(String[] args) {
        try {
            words = readString("HangmanWordsList.txt"); /* Uses the file */
        } catch (FileNotFoundException e) {
            return;
        }

        Scanner scanner = new Scanner(System.in); /* Scans the user input if they want to play again */
        boolean playAgain = true;

        System.out.println("Welcome to Hangman!"); /* Prints a welcome message to user */

        while (playAgain) { /* Resets the game when the user wants to play again */
            String word = getRandomWord();
            int arrayLength = word.length();
            int incorrectGuesses = 0;
            String[] guessedLetters = new String[arrayLength];
            StringBuilder incorrectGuessesBuilder = new StringBuilder();

            while (incorrectGuesses < maxGuesses) {
                String guess = userGuess(guessedLetters, incorrectGuessesBuilder);

                boolean correctGuess = false;

                for (int i = 0; i < arrayLength; i++) {
                    if (word.charAt(i) == guess.charAt(0)) {
                        guessedLetters[i] = guess;
                        correctGuess = true;
                    }
                }

                if (correctGuess) {
                    System.out.println("Correct guess!"); /* Prints correct guess */
                } else {
                    System.out.println("Incorrect guess!"); /* Prints Incorrect guess */
                    incorrectGuesses++;
                    System.out.println("Incorrect guesses remaining: " + (maxGuesses - incorrectGuesses)); /* Prints guesses remanining by subtracting the incorrect guesses from the maximum guess */
                    incorrectGuessesBuilder.append(guess).append(" ");
                }

                System.out.print("Current progress: "); /* Prints out the current progress of the user by replacing the dash with the correct letter, if the user guesses the letter correctly */
                for (String letter : guessedLetters) {
                    if (letter != null) {
                        System.out.print(letter);
                    } else {
                        System.out.print("_");
                    }
                }

                System.out.println();
                System.out.println("Incorrect guesses: " + incorrectGuessesBuilder.toString()); /* Prints letters that are marked as incorrect guesses */
                System.out.println("Number of incorrect guesses: " + incorrectGuesses); /* Prints number incorrect guesses left */

                if (String.join("", guessedLetters).equals(word)) {
                    System.out.println("\nCongratulations! You guessed the word: " + word); /* Prints win banner */
                    break;
                }
            }

            if (incorrectGuesses == maxGuesses) {
                System.out.println("\nSorry, you ran out of guesses. The word was: " + word); /* Prints losing banner */
            }

            while (true) {
                System.out.print("\nDo you want to play again? Enter Yes or No: "); /* Asks user if they want to play again and takes the user input */
                String playAgainInput = scanner.nextLine().toUpperCase();

                if (playAgainInput.equals("YES")) { /* Repeats game if user wants to play again */
                    playAgain = true;
                    break;
                } else if (playAgainInput.equals("NO")) { /* Ends game if user does not want to play again and prints message */
                    playAgain = false;
                    System.out.println("Thanks for Playing!");
                    break;
                } else {
                    System.out.println("Invalid Input"); /* Prints invalid input if the user does not enter yes or no */
                }
            }
        }
    }
}