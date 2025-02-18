package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.Client;
import blaybus.mvp.back.dto.response.CustomUserDetails;
import blaybus.mvp.back.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Long userIdByEmail(String email){
        return clientRepository.findUserIdByEmail(email);
    }

    public String nameByEmail(String email){
        return clientRepository.findNameByEmail(email);
    }

    public Client clientByEmail(String email){
        return clientRepository.findByEmail(email).orElse(null);
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ((CustomUserDetails) authentication.getPrincipal()).getEmail();
        }
        return null; // 또는 적절한 기본값
    }

}
