/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caesarcipher;

import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Author: Grace Guo 
 * Class: ICS4UE-51
 * Program: Caeser Cipher
 * Summary: The program takes in the user's input of a phrase and can be 
 * encoded or decoded with a desired shift. The phrase can also be manually cracked and provides
 * the decode for each shift of the alphabet as well as the best decode. 
 */
public class CaesarCipher {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        String inputMessage = "";
        int moveLettersBy = 0;//letter shift 
        String changedMessage;
        String programChoice = "";
        boolean validInput = false; //to chack all inputs 
        boolean gamePlay = true; //game is being played 
        String[] possResults;//array for results 
        int maxWords = 0, curWords = 0, bestKey = 0;
        String curSentence, curToken;
        StringTokenizer tokenSent; 
        int exceptionNum = 0; //type of exception 
        boolean programValid = false, phraseValid = false, shiftValid = false, anInt = false;//check for valid inputs

        Scanner keyboard = new Scanner(System.in);

        while (gamePlay) {
            System.out.println("Encode (e); Decode (d); Brute Force (b); Quit (q): "); //menu 
            do { 
                try {
                    
                    while (!programValid) {
                        programChoice = (keyboard.nextLine()).toLowerCase();

                        if (programChoice.equals("q")) {//quitting for inputted q
                            validInput = true;
                            programValid = true; 
                            phraseValid = true; 
                            shiftValid = true; 
                        } else {
                        switch (programChoice) {
                            case "e": //encoding
                                System.out.print("Phrase to encode: ");
                                programValid = true; 
                                break;
                            case "d": //decoding
                                System.out.print("Phrase to decode: ");
                                programValid = true;
                                break;
                            case "b": //brute force 
                                System.out.print("Phrase to brute force: ");
                                programValid = true;
                                break;
                            default: //invalid program choice
                                exceptionNum = 1; 
                                validInput = false;
                                programValid = false; 
                                validate(); //throw exception for other inputs
                            }
                        }
                    }
                    
                    while(!phraseValid){
                        inputMessage = keyboard.nextLine();

                        if (inputMessage.equals("")) {
                            exceptionNum = 2; 
                            validInput = false; 
                            phraseValid = false; 
                            validate();//throw exception for an empty string 
                        }else{
                            switch(programChoice){
                                case "e": //encoding, ask for a right shift 
                                    System.out.print("Shift right by: ");
                                    break; 
                                case "d": //decoding, ask for a left shift 
                                    System.out.print("Shift left by: ");
                                    break; 
                                case "b": //brute force 
                                    shiftValid = true; 
                                    validInput = true; 
                                    break; 
                            }
                            phraseValid = true; 
                        }
                    }
                    while (!shiftValid) {
                        while (!anInt){//checks if the input is an integer
                            try {
                                 moveLettersBy = keyboard.nextInt();
                                 String gunk = keyboard.nextLine(); 
                                 anInt = true; 
                            } catch (Exception inputMisMatch) {//invalid integer
                                String gunk = keyboard.nextLine(); 
                                System.out.print("Please enter an integer: ");
                            }
                        }
                        
                        if(moveLettersBy >= 0 && moveLettersBy <= 25){//checking if leter shift is within 0 to 25 
                            validInput = true;
                            shiftValid = true; 
                        }else{
                            exceptionNum = 3; 
                            shiftValid = false; 
                            validInput = false;
                            validate(); //throw exception for invalid shift 
                        }
                    }
                } catch (Exception e) {
                    
                    switch(exceptionNum){
                        case 1: //exception for invalid program input 
                            System.out.print("Please enter either e,d,b for a program choice or q for quit: ");
                            break; 
                        case 2: //exception for blank phrase input 
                            System.out.print("Please do not leave the phrase blank. Enter a proper phrase: "); 
                            break; 
                        case 3: //exception for inputted letter shift below 0 and above 25
                            System.out.print("Please enter a letter shift between 0 and 25 inclusive: ");
                            anInt = false; 
                            break; 
                    }
                }

            } while (!validInput); //checking for invalid input 

            if (programChoice.equals("q")) {
                gamePlay = false; //quitting game 
            } else {
                switch (programChoice) {
                    case "e"://encode
                        changedMessage = changeIt(inputMessage, moveLettersBy); //calls method for encoding the string 
                        System.out.println("Your encoded message is: " + changedMessage);
                        break;
                    case "d"://decode
                        moveLettersBy = -moveLettersBy; //negative shift for decoding 
                        changedMessage = changeIt(inputMessage, moveLettersBy); //calls method for decoding the string 
                        System.out.println("Your decoded message is: " + changedMessage);
                        break;
                    case "b"://brute force 
                        possResults = breakCode(inputMessage);
                        for (int letter = 0; letter <= 25; letter++) {//shift for each letter of the alphabet 

                            curSentence = possResults[letter]; //calls method for array of decoded senetnce for each letter of alphabet 
                            tokenSent = new StringTokenizer(curSentence, " \n\r"); //set each sentence into individual tokens 
                            System.out.println("For shift of " + letter + ", decoded is: " + curSentence);

                            while (tokenSent.hasMoreElements()) {
                                curToken = (tokenSent.nextToken()).toLowerCase();

                                switch (curToken) {//looks for these common words in each shifted sentence  
                                    case "the":
                                        curWords++;
                                        break;
                                    case "a":
                                        curWords++;
                                        break;
                                    case "of":
                                        curWords++;
                                        break;
                                    case "to":
                                        curWords++;
                                        break;
                                    case "this":
                                        curWords++; 
                                        break; 
                                    case "be":
                                        curWords++; 
                                        break; 
                                    case "have": 
                                        curWords++; 
                                        break; 
                                    case "is": 
                                        curWords++; 
                                        break; 
                                }
                            }
                            if (curWords > maxWords) {//comparing number of common english words found in each shifted sentence 
                                maxWords = curWords;
                                bestKey = letter;//setting the best decode key to decode with most common english words
                            }
                        }
                        System.out.println("The best decode was with key " + bestKey);
                        break;
                }
                //clearing data and restarting game 
                programValid = false; 
                phraseValid = false; 
                shiftValid = false; 
                gamePlay= true; 
                anInt = false; 
                exceptionNum = 0; 
            }
        }
        System.out.println("Thanks for Ciphering.");
    }

    /**
     * @param inputMessage      phrase inputted by user
     * @param moveLettersBy     amount to shift letters by
     * @return                  encoded or decoded message
     */
    
    public static String changeIt(String inputMessage, int moveLettersBy) { //encoding/decoding method 
        String changedString = "";
        char storedLetter;
        int letterIntVal;
        int letterRef;
        int remainLeft; 
        char finalChar; 

        for (int i = 0; i <= inputMessage.length() - 1; i++) {//goes through each character in the user inputted phrase 
            storedLetter = inputMessage.charAt(i);
            letterIntVal = (int) storedLetter; //ASCII value of the character 

            if (letterIntVal >= 65 && letterIntVal <= 90) { 
                letterRef = 65; //determines letter is lowercase
            } else {
                letterRef = 97; //determines letter is uppercase
            }
            
            if((letterIntVal >= 65 && letterIntVal <= 90)||(letterIntVal >= 97 && letterIntVal <= 122)) //shifting for uppercase and lower case letters
            {
                storedLetter = (char)(inputMessage.charAt(i)-letterRef); 
                remainLeft = ((storedLetter + moveLettersBy) + 26) %26; 
                finalChar = (char)(letterRef + remainLeft);
            }else{ //no shift for all other characters 
                finalChar = (char)letterIntVal; 
            }
            changedString += finalChar; //concatenating the encoded/decoded string 
        }
        return changedString; //returns encoded/decoded string 
    }

    /**
     * @param inputMessage      phrase inputted by user
     * @return                  array of shifted phrases for each letter of the alphabet
     */
    
    public static String[] breakCode(String inputMessage) { //method for brute force 
        String[] possibleCodes = new String[26]; //array for all decoded sentenced 
        int decodeLetter; 
        String curGuess = "";

        for (int letter = 0; letter <= 25; letter++) {//for each letter of the alphabet 
            
            decodeLetter = -letter; 
            curGuess = changeIt(inputMessage,decodeLetter); //calls decode method
            possibleCodes[letter] = curGuess; //stores each sentence in array 
            curGuess = "";
        }
        return possibleCodes; 
    }

    static void validate() throws InvalidInput {
        throw new InvalidInput("Empty Input"); //throw an exception 
    }

    public static class InvalidInput extends Exception { //class for exception

        InvalidInput(String message) {
            super(message);
        }
    }
}

