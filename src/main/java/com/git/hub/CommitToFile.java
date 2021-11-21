package com.git.hub;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class CommitToFile {


    public void updateCommit(CommitHistory shaid) {
        ObjectMapper mapper = new ObjectMapper();

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

    public CommitHistory getSavedCommit() {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File("person.json");
        try {
            CommitHistory car = mapper.readValue(file, CommitHistory.class);
            return car;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}