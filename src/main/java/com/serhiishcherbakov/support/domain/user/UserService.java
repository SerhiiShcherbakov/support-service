package com.serhiishcherbakov.support.domain.user;

import com.serhiishcherbakov.support.security.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User syncAndGetUser(UserDetailsDto userDetails) {
        return userRepository.findById(userDetails.id())
                .map(user -> {
                    if (userDetails.updatedAt().isAfter(user.getUpdatedAt())) {
                        user.setName(userDetails.name());
                        user.setPicture(userDetails.picture());
                        user.setRole(userDetails.role());
                        user.setUpdatedAt(userDetails.updatedAt());
                        return userRepository.save(user);
                    }
                    return user;
                })
                .orElseGet(() -> {
                    var user = new User(
                            userDetails.id(),
                            userDetails.name(),
                            userDetails.picture(),
                            userDetails.role(),
                            userDetails.updatedAt()
                    );
                    return userRepository.save(user);
                });
    }

    public void syncUser(UserDetailsDto userDetails) {
        userRepository.findById(userDetails.id())
                .map(user -> {
                    if (userDetails.updatedAt().isAfter(user.getUpdatedAt())) {
                        user.setName(userDetails.name());
                        user.setPicture(userDetails.picture());
                        user.setRole(userDetails.role());
                        user.setUpdatedAt(userDetails.updatedAt());
                        return userRepository.save(user);
                    }
                    return user;
                })
                .orElseGet(() -> {
                    var user = new User(
                            userDetails.id(),
                            userDetails.name(),
                            userDetails.picture(),
                            userDetails.role(),
                            userDetails.updatedAt()
                    );
                    return userRepository.save(user);
                });
    }
}
