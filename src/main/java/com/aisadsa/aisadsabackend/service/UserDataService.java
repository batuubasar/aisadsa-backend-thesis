package com.aisadsa.aisadsabackend.service;



import com.aisadsa.aisadsabackend.core.dto.request.CreateUserDataRequest;
import com.aisadsa.aisadsabackend.core.dto.response.UserDataResponse;
import com.aisadsa.aisadsabackend.repository.UserDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserDataService {
    private final UserDataRepository userDataRepository;
    private final UserService userService;



/*

    public List<UserDataResponse> getAllDataOfUser(String userEmail) {
        UUID userId = userService.getUserId(userEmail);
        return userDataRepository.findByUserId(userId);
    }

    public void save(CreateUserDataRequest createUserDataRequest) {
        // TODO: 11.02.2025 jwt authentication sağlandıktan sonra UserData oluşturabilmek için
        // TODO: authenticationdan currentUser çekilecek onla userId fieldı doldurulacak
        // TODO: frontend'den questionId gelecek.
    }
*/

}


