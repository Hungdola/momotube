package com.hungtran.youtubeclone.repository;

import com.hungtran.youtubeclone.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findBySub(String sub);

    List<User> findByIdIn(List<String> userIds);
}
