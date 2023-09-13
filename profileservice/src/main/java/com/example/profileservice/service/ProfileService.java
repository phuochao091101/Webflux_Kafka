package com.example.profileservice.service;

import com.example.profileservice.model.ProfileDTO;
import com.example.profileservice.model.mapper.ProfileMapper;
import com.example.profileservice.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;
    public Flux<ProfileDTO> getAllProfile(){
        return profileRepository.findAll()
                .map(ProfileMapper::entityToDto)
                .switchIfEmpty(Mono.error(new Exception("Not Found")));
    }
    public Mono<Boolean> checkDuplicate(String email){
        return profileRepository.findByEmail(email)
                .map(profile -> true)
                .switchIfEmpty(Mono.just(false));
    }
}
