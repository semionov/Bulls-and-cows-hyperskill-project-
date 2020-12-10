package bullscows;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean error = false;
        boolean success = false;

        int cows;
        int bulls;
        int counter = 1;
        int lengthOfNumber = 0;
        int numberOfSymbols = 0;

        String number;
        String introducedNumber;
        String asterisk = "*";
        String characters;
        String allSymbols = ("0123456789abcdefghijklmnopqrstuvwxyz");
        String asteriskRepeated;

        do {
            try {
                System.out.println("Input the length of the secret code:");
                lengthOfNumber = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Error: " + lengthOfNumber + " isn't a valid number.");
                System.exit(1);
            }
            try {
                System.out.println("Input the number of possible symbols in the code:");
                numberOfSymbols = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Error: " +  numberOfSymbols + " isn't a valid number.");
                System.exit(1);
            }

            if (lengthOfNumber > numberOfSymbols) {
                System.out.println("Error: it's not possible to generate a code with a length of " + lengthOfNumber +
                    " with " + numberOfSymbols + " unique symbols.");
                System.exit(1);
            } else if (numberOfSymbols > 36) {
                System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                System.exit(1);
            } else if (lengthOfNumber == 0 || numberOfSymbols == 0) {
                System.out.println("Error: " + lengthOfNumber + " isn't a valid number.");
                System.exit(1);
            }

            //generate number
            number = generateRandomNumber(lengthOfNumber, numberOfSymbols);

            //reporting secret's code data
            asteriskRepeated = IntStream.range(0, lengthOfNumber).mapToObj(i -> asterisk).collect(Collectors.joining(""));
            if (lengthOfNumber > 10) {
                characters = " (0-9, a-" + allSymbols.charAt(numberOfSymbols - 1) + ").";
            } else {
                characters = " (0-" + allSymbols.charAt(numberOfSymbols - 1) + ").";
            }
            System.out.println("The secret is prepared: " + asteriskRepeated + characters);

            //warning about code length
            if (lengthOfNumber > 36) {
                System.out.println("Error: can't generate a secret number with a length of " + number +
                    " because there aren't enough unique digits.");
                error = true;
            } else {
                error = false;
            }
        } while (error);

        System.out.println("Okay, let's start a game!");

        do {
            System.out.println("Turn " + counter + ":");

            introducedNumber = scanner.next();
            char[] numberArray = makeArray(number);
            char[] introducedNumberArray = makeArray(introducedNumber);

            //counting cows and bulls
            cows = findCows(numberArray, introducedNumberArray);
            bulls = findBulls(numberArray, introducedNumberArray);

            if (bulls == lengthOfNumber) {
                System.out.println("Congratulations! You guessed the secret code.");
                success = true;
            } else {
                System.out.printf("Grade: %d bull(s) and %d cow(s)", bulls, cows);
            }

            counter++;
        } while (!success);

        //printing result
        if (cows == 0 && bulls == 0) {
            System.out.println("None. The secret code is " + number);
        } else if (cows > 0 && bulls == 0) {
            System.out.printf("Grade: %d cow(s). The secret code is %s", cows, number);
        } else if (cows == 0 && bulls > 0) {
            System.out.printf("Grade: %d bull(s). The secret code is %s", bulls, number);
        } else {
            System.out.printf("Grade: %d cow(s) and %d bull(s). The secret code is %s", cows, bulls, number);
        }
    }

    public static int findCows(char[] numberArray, char[] introducedNumberArray) {

        int counterCows = 0;

        for (int k : numberArray) {
            for (int i : introducedNumberArray) {
                if (k == i) {
                    counterCows++;
                    break;
                }
            }
        }
        counterCows -= findBulls(numberArray, introducedNumberArray);
        return counterCows;
    }

    public static int findBulls(char[] numberArray, char[] introducedNumberArray) {

        int counterBulls = 0;

        for (int i = 0; i < numberArray.length; i++) {
            if (numberArray[i] == introducedNumberArray[i]) {
                counterBulls++;
            }
        }
        return counterBulls;
    }

    public static char[] makeArray(String number) {

        //String temp = Integer.toString(integerNumber);
        char[] array = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            array[i] = number.charAt(i);
        }

        return array;
    }

    public static String generateRandomNumber(int lengthOfNumber, int numberOfSymbols) {

        boolean stop;
        StringBuilder chars = new StringBuilder("0123456789abcdefghijklmnopqrstuvwxyz");
        chars.setLength(chars.length() - (36 - numberOfSymbols));

        // char[] allSymbols = chars.toCharArray();

        char[] uniqueNumber = new char[lengthOfNumber];
        SecureRandom random = new SecureRandom();

        do {
            stop = false;
            /* uniqueNumber = String.valueOf(lengthOfNumber < 1 ? 0 : new Random()
             * .nextInt((9 * (int) Math.pow(10, lengthOfNumber - 1)) - 1)
             * + (int) Math.pow(10, lengthOfNumber - 1)).toCharArray();
             */
            for (int i = 0; i < lengthOfNumber; i++) {
                int randomIndex = random.nextInt(chars.length());
                uniqueNumber[i] = chars.charAt(randomIndex);
            }

            for (int i = 0; i < uniqueNumber.length; i++) {
                for (int k = 0; k < uniqueNumber.length; k++) {
                    if (k != i && uniqueNumber[i] == uniqueNumber[k]) {
                        stop = true;
                        break;
                    }
                }
            }
        } while (stop);

        return String.copyValueOf(uniqueNumber);
    }

    public static String generateRandomPassword(int len) {
        // ASCII range - alphanumeric (0-9, a-z)
        final String chars = "0123456789abcdefghijklmnopqrstuvwxyz";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of loop choose a character randomly from the given ASCII range
        // and append it to StringBuilder instance

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static void gameResult(int cows, int bulls, int lengthOfNumber) {
        if (bulls == lengthOfNumber) {
            System.out.println("Congratulations! You guessed the secret code.");
        } else {
            System.out.printf("Grade: %d bull(s) and %d cow(s)", bulls, cows);
        }
    }
}
