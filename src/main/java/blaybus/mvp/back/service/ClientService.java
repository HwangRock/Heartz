package blaybus.mvp.back.service;

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
        return clientRepository.findIdByEmail(email);
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ((CustomUserDetails) authentication.getPrincipal()).getEmail();
        }
        return null; // 또는 적절한 기본값
    }

}
