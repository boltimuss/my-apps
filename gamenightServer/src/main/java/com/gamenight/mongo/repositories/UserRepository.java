package com.gamenight.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gamenight.dao.models.User;

public interface UserRepository extends MongoRepository<User, String> {

}
