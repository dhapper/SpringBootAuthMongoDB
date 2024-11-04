package com.authenticator.user_authenticator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

@Service
public class UserService {

    private final MongoClient mongoClient;
    private final MongoDatabase database;

    @Autowired
    public UserService() {
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        this.database = mongoClient.getDatabase("user_credentials");
    }

    public void addNewUser(String username, String password, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        // Check if the user already exists
        Document existingUser = collection.find(Filters.eq("username", username)).first();
        if (existingUser != null) {
            System.out.println("User already exists. Choose a different username.");
            return; // Exit the method if the user exists
        }

        // Create a document for the new user
        Document document = new Document("username", username).append("password", password);

        // Insert the document
        collection.insertOne(document);
        System.out.println("Document inserted successfully!");
    }

    public boolean authenticateUser(String username, String password, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        // Find user document by username
        Document userDoc = collection.find(Filters.eq("username", username)).first();

        if (userDoc != null) {
            String storedPassword = userDoc.getString("password");
            System.out.println("Stored Password: " + storedPassword); // Debug print
            return password.equals(storedPassword);
        } else {
            System.out.println("User document not found."); // Debug print
            return false;
        }
    }
}
