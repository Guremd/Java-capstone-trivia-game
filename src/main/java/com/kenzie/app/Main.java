package com.kenzie.app;

// import necessary libraries


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.app.dto.CluesDTO;

import java.util.Random;
import java.util.Scanner;

public class Main {
    /* Java Fundamentals Capstone project:
       - Define as many variables, properties, and methods as you decide are necessary to
       solve the program requirements.
       - You are not limited to only the class files included here
       - You must write the HTTP GET call inside the CustomHttpClient.sendGET(String URL) method
         definition provided
       - Your program execution must run from the main() method in Main.java
       - The rest is up to you. Good luck and happy coding!

     */

    //TODO:try-catch for error input; custom exception when caught(?)
    //TODO: create separate method/class for generating random trivia(?)
    //TODO: search a possible method to compare a part of a string for code to recognize the correct answer
    //TODO: separate method for checking answer
    //TODO: generate trivia based on a certain category only

    public static void welcomeMenu(){
        System.out.println("Random Trivia Game\n" +
                "You will be given 10 trivia questions\n" +
                "Good Luck!\n" +
                "----------\n"
        );
    }

    public static CluesDTO getTrivia(String httpURL) throws JsonProcessingException {

        String httpResponse;
        CluesDTO clueObject;
        httpResponse = CustomHttpClient.sendGET(httpURL);
        ObjectMapper objectMapper = new ObjectMapper();
        clueObject = objectMapper.readValue(httpResponse, CluesDTO.class);

        return clueObject;
    }

    public static String getTriviaCategory(String URL, int ID) throws JsonProcessingException {
        return getTrivia(URL).getClues().get(ID).getCategory().getTitle();
    }

    public static String getTriviaQuestion(String URL, int ID) throws JsonProcessingException {
        return getTrivia(URL).getClues().get(ID).getQuestion();
    }

    public static String getTriviaAnswer(String URL, int ID) throws JsonProcessingException {
        return getTrivia(URL).getClues().get(ID).getAnswer();
    }



    public static void main(String[] args)  {
        //Write main execution code here

        welcomeMenu();

        try {
            final String TRIVIA_URL = "https://jservice.kenzie.academy/api/clues";

            //Scanner properties
            Scanner scanner = new Scanner(System.in);
            String userInput = "";

            //Score keeping variables
            int currentScore = 0;
            int totalScore = 0;

            //Random class -- generate random trivia based on ID number
            int triviaID;
            Random random = new Random();

            for (int i = 0; i < 10; i++) {
                //random trivia query from API
                triviaID = random.nextInt(100);
                System.out.println("Category: " + getTriviaCategory(TRIVIA_URL,triviaID));
                System.out.println("Question: " + getTriviaQuestion(TRIVIA_URL,triviaID));
                System.out.println("Please type your answer below:");
                userInput = scanner.nextLine();


                //compare user input to answer stored in the JSON; get points if correct, none if wrong
                if (userInput.equalsIgnoreCase(String.valueOf(getTriviaAnswer(TRIVIA_URL,triviaID)))) {
                    totalScore = totalScore + 1;
                    System.out.println("DING DING DING! Correct!");
                    System.out.println("Your current score is " + totalScore + "\n");
                } else {
                    System.out.println("Bzzt. Wrong answer.");
                    System.out.println("The correct answer was " + getTriviaAnswer(TRIVIA_URL,triviaID));
                    System.out.println("Your current score is " + totalScore + "\n");
                }
            }

            System.out.println("------------------------");
            System.out.println("Your final score is: " + totalScore + " out of 10");


        }
        catch (Exception e) {
            System.out.println("Unexpected error: " + e);
        }


    }
}

