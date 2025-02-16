package blaybus.mvp.back.service;

import blaybus.mvp.back.config.AdminConfig;
import blaybus.mvp.back.domain.Designer;
import blaybus.mvp.back.dto.request.DesignerAddRequestDto;
import blaybus.mvp.back.dto.request.DesignerDeleteRequestDto;
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
    private final AdminConfig adminConfig;


    // 디자이너 추가
    // TODO: admin_pw
    public Boolean createDesigner(DesignerAddRequestDto designerAddRequestDto) {
        // 중복 아이디
        if (designerRepository.existsById(designerAddRequestDto.designer_id())) {
            throw new ApiException(ErrorDefine.DESIGNER_ID_EXIST);
        }

        Designer designerSaved = designerAddRequestDto.toEntity();
        designerRepository.save(designerSaved);

        return true;
    }

    // 디자이너 정보 수정 (key = designer_id)
    // TODO: admin_pw
    public Boolean updateDesigner(DesignerAddRequestDto designerAddRequestDto) {
        Optional<Designer> existingDesigner = designerRepository.findByDesignerId(designerAddRequestDto.designer_id());

        if (existingDesigner.isPresent()) {
            Designer designerUpdate = existingDesigner.get();

            // 기존 객체의 필드만 업데이트
            designerUpdate.setName(designerAddRequestDto.name());
            designerUpdate.setLocation(designerAddRequestDto.location());
            designerUpdate.setAddress(designerAddRequestDto.address());
            designerUpdate.setField(designerAddRequestDto.field());
            designerUpdate.setOffPrice(designerAddRequestDto.offPrice());
            designerUpdate.setOnPrice(designerAddRequestDto.onPrice());
            designerUpdate.setRating(designerAddRequestDto.rating());
            designerUpdate.setText(designerAddRequestDto.text());

            designerRepository.save(designerUpdate);
            return true;
        } else {
            throw new ApiException(ErrorDefine.DESIGNER_ID_NOT_FOUND);
        }
    }

    // 디자이너 정보 삭제
    @Transactional
    public Boolean deleteDesigner(DesignerDeleteRequestDto designerDeleteRequestDto) {
        // 아이디가 없을 떄
        if (!designerRepository.existsByDesignerId((designerDeleteRequestDto.designer_id()))) {
            throw new ApiException(ErrorDefine.DESIGNER_ID_NOT_FOUND);
        }

        // 관리자 권한 없음
        if(!adminConfig.getPassword().equals(designerDeleteRequestDto.admin_pw())) {
            throw new ApiException(ErrorDefine.UNAUTHORIZED_USER);
        }

        // 삭제
        designerRepository.deleteByDesignerId(designerDeleteRequestDto.designer_id());
        return true;
    }
}
