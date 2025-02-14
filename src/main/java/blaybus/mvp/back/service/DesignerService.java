package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.Designer;
import blaybus.mvp.back.dto.request.DesignerAddRequestDto;
import blaybus.mvp.back.exception.ApiException;
import blaybus.mvp.back.exception.ErrorDefine;
import blaybus.mvp.back.repository.DesignerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DesignerService {
    private final DesignerRepository designerRepository;

    public Boolean createDesigner(DesignerAddRequestDto designerAddRequestDto) {
        if (designerRepository.existsById(designerAddRequestDto.designer_id())) {
            throw new ApiException(ErrorDefine.DESIGNER_ID_EXIST);
        }

        Designer designerSaved = designerAddRequestDto.toEntity();
        designerRepository.save(designerSaved);

        return true;
    }

    // TODO: designer_id로 designer 찾은 후 정보 수정
    public Boolean updateDesigner(DesignerAddRequestDto designerAddRequestDto) {

        Optional<Designer> existingDesigner = designerRepository.findByDesignerId(designerAddRequestDto.designer_id());
        if (existingDesigner.isPresent()) {
            Designer designerUpdate = Designer.builder()
                    .designerId(existingDesigner.get().getDesignerId())
                    .name(designerAddRequestDto.name())
                    .location(designerAddRequestDto.location())
                    .address(designerAddRequestDto.address())
                    .field(designerAddRequestDto.field())
                    .offPrice(designerAddRequestDto.offPrice())
                    .onPrice(designerAddRequestDto.onPrice())
                    .rating(designerAddRequestDto.rating())
                    .text(designerAddRequestDto.text())
                    .build();
            designerRepository.save(designerUpdate);

            return true;
        }
        else {
            throw new ApiException(ErrorDefine.DESIGNER_ID_NOT_FOUND);
        }
    }
}
