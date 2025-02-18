package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.Client;
import blaybus.mvp.back.domain.Role;
import blaybus.mvp.back.repository.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
    public Client findOrCreateUser(String userId, String name, String email, String picture) {
        return clientRepository.findByUserId(userId)
                .map(client -> client.update(name, picture))  // 이미 존재하면 정보 업데이트
                .orElseGet(() -> clientRepository.save(Client.builder()
                        .userId(userId)
                        .name(name)
                        .email(email)
                        .role(Role.USER)  // 기본 ROLE 설정
                        .picture(picture)
                        .build()));
    }
}
