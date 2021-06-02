package org.feichtmeier.genericwebapp.service;

import java.util.ArrayList;
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

    public List<AvatarImage> findUserAvatarImages(String username) {

        List<AvatarImage> userAvatarImages = new ArrayList<>();

        List<AvatarImage> aImages = findAll();
        for (AvatarImage avatarImage : aImages) {
            if (username.equals(avatarImage.getUser().getUsername())) {
                userAvatarImages.add(avatarImage);
            }
        }
        ;
        return userAvatarImages;
    }

    public AvatarImage findMostRecentUserAvatarImage(String username) {
        List<AvatarImage> avatarImages = findUserAvatarImages(username);
        if (avatarImages.isEmpty()) {
            return null;
        }
        AvatarImage mostRecentAvatarImage = avatarImages.get(0);

        for (AvatarImage avatarImage : avatarImages) {
            if (avatarImage.getTimeStamp().isAfter(mostRecentAvatarImage.getTimeStamp())) {
                mostRecentAvatarImage = avatarImage;
            }
        }

        return mostRecentAvatarImage;
    }

    @Override
    public AvatarImage getOne(AvatarImage entity) {
        return avatarImageRepository.getOne(entity.getId());
    }

}
