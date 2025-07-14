package com.kenny.uaa.security.userdetails;

import com.kenny.uaa.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

    private final UserRepo userRepo;

    @Override
    public UserDetails updatePassword(UserDetails userDetail, String newPassword) {
        return userRepo.findOptionalByUsername(userDetail.getUsername())
                .map(user -> {
                    return (UserDetails) userRepo.save(user.withPassword(newPassword));
                })
                .orElse(userDetail);
    }
}
