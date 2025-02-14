package blaybus.mvp.back.service;

import blaybus.mvp.back.repository.DesignerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final DesignerRepository designerRepository;

}