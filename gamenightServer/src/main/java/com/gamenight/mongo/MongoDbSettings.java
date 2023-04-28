//package com.gamenight.mongo;
//
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
//import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.ServerApi;
//import com.mongodb.ServerApiVersion;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//
//@Configuration
//@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
//public class MongoDbSettings {
//	
//	@Bean
//	public MongoClient createClient()
//	{
//		String connectionString = "mongodb+srv://appUser:6xwFr8W4NzKEk9i@gamenightdb.ibyuynv.mongodb.net/?retryWrites=true&w=majority";
//	    
//		ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
//
//	    MongoClientSettings settings = MongoClientSettings.builder()
//	            .applyConnectionString(new ConnectionString(connectionString))
//	            .serverApi(serverApi)
//	            .build();
//	    
//		return MongoClients.create(settings);
//	}
//	
////	    try {
////	    	
////	        // Send a ping to confirm a successful connection
////	    	MongoDatabase database = mongoClient.getDatabase("gamenightDB");
////	    	Iterator<?> it = database.getCollection("users").find().iterator();
////	  
////	    	while (it.hasNext())
////	    		System.out.println(it.next());
////	        
////	    } catch (MongoException e) {
////	        e.printStackTrace();
////	    }
//}
