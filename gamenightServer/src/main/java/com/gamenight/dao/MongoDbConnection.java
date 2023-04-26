package com.gamenight.dao;

import org.bson.Document;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.stereotype.Component;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import jakarta.annotation.PostConstruct;

@Component
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class MongoDbConnection {

	@PostConstruct
	public void connect()
	{
		String connectionString = "mongodb+srv://appUser:6xwFr8W4NzKEk9i@gamenightdb.ibyuynv.mongodb.net/?retryWrites=true&w=majority";
	    
		ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();

	    MongoClientSettings settings = MongoClientSettings.builder()
	            .applyConnectionString(new ConnectionString(connectionString))
	            .serverApi(serverApi)
	            .build();
	    
	    // Create a new client and connect to the server
		MongoClient mongoClient = MongoClients.create(settings);
			
	    try {
	    	
	        // Send a ping to confirm a successful connection
	        MongoDatabase database = mongoClient.getDatabase("admin");
	        database.runCommand(new Document("ping", 1));
	        System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
	        
	    } catch (MongoException e) {
	        e.printStackTrace();
	    }
	}
}
