package com.authenticator.user_authenticator;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class Authentication {

    private static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private static MongoDatabase database = mongoClient.getDatabase("user_credentials");
    
    public static void main(String[] args) {
    	
//        AddNewUser("Asd", "wasd", "watchlist-app"); // Add the user first
//        
//        boolean login = AuthenticateUser("Asd", "wasd", "watchlist-app");
//        
//        if (login) {
//            System.out.println("Logging in");
//        } else {
//            System.out.println("Invalid Credentials");
//        }

        mongoClient.close();
    }
    
    public static void AddNewUser(String username, String password, String collectionName) {
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

    
    public static boolean AuthenticateUser(String username, String password, String collectionName) {
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
