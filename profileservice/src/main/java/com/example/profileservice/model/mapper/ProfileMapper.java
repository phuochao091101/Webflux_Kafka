package com.example.profileservice.model.mapper;

import com.example.profileservice.data.Profile;
import com.example.profileservice.model.ProfileDTO;

public class ProfileMapper {
    public static Profile dtoToEntity(ProfileDTO profileDTO){
        Profile profile = new Profile();
        profile.setId(profileDTO.getId());
        profile.setName(profileDTO.getName());
        profile.setRole(profileDTO.getRole());
        profile.setStatus(profileDTO.getStatus());
        profile.setEmail(profileDTO.getEmail());
        return profile;
    }
    public static ProfileDTO entityToDto(Profile profile){
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profile.getId());
        profileDTO.setEmail(profile.getEmail());
        profileDTO.setStatus(profile.getStatus());
        profileDTO.setName(profile.getName());
        profileDTO.setRole(profile.getRole());
        return profileDTO;
    }
}

