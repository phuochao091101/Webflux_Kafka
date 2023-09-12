package com.example.profileservice.repository;

import com.example.profileservice.data.Profile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProfileRepository extends ReactiveCrudRepository<Profile,Long> {
}
