package org.feichtmeier.genericwebapp.service;

import java.util.List;

import org.feichtmeier.genericwebapp.entity.AvatarImage;
import org.feichtmeier.genericwebapp.repository.AvatarImageRepository;
import org.springframework.stereotype.Service;

@Service
public class AvatarImageService implements DataService<AvatarImage> {

    private final AvatarImageRepository avatarImageRepository;

    public AvatarImageService(AvatarImageRepository avatarImageRepository) {
        this.avatarImageRepository = avatarImageRepository;
    }

    @Override
    public List<AvatarImage> findAll() {
        return avatarImageRepository.findAll();
    }

    @Override
    public void save(AvatarImage entity) {
        avatarImageRepository.save(entity);
    }

    @Override
    public void delete(AvatarImage entity) {
        avatarImageRepository.delete(entity);
    }
    
}
