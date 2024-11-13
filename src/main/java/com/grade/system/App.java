package com.grade.system;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * App entry point
 *
 */
@SuppressWarnings("all")
public class App {

    public static void main(String[] args) {

        String uri = "mongodb+srv://sb:sbgravity100@main.pa1uh.mongodb.net/?retryWrites=true&w=majority&appName=main";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .serverApi(serverApi)
                .build();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("main");
            MongoCollection<Document> users = database.getCollection("oop_users");
            MongoCollection<Document> students = database.getCollection("students");
            try {
                // students.createIndex(Indexes.text("lastName"));
                Menu m = new Menu(users, students);
                m.Start();
            } catch (MongoException me) {
                System.err.println(me);
            }
        }
    }

}
