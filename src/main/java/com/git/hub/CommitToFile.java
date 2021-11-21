package com.git.hub;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Date;

public class CommitToFile {

    public static void updateCommit(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        CommitHistory shaid = new CommitHistory("1234", new Date(), "shaid");
        File file = new File("person.json");
        try {

            mapper.writeValue(file, shaid);
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(shaid);
            //System.out.println(jsonString);

            CommitHistory car = mapper.readValue(file, CommitHistory.class);
            System.out.println(car);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}