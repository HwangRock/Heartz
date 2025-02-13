package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.Designer;
import blaybus.mvp.back.dto.request.DesignerAddRequestDto;
import blaybus.mvp.back.exception.ApiException;
import blaybus.mvp.back.exception.ErrorDefine;
import blaybus.mvp.back.repository.DesignerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DesignerService {
    private final DesignerRepository designerRepository;

    public Boolean createDesigner(DesignerAddRequestDto requestDto) {
        if (designerRepository.existsById(requestDto.designer_id())) {
            throw new ApiException(ErrorDefine.DESIGNER_ID_EXIST);
        }

        Designer designerSaved = requestDto.toEntity();
        designerRepository.save(designerSaved);

        return true;
    }
}
