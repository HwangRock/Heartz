package blaybus.mvp.back.service;

import blaybus.mvp.back.config.AdminConfig;
import blaybus.mvp.back.domain.Designer;
import blaybus.mvp.back.dto.request.DesignerAddRequestDto;
import blaybus.mvp.back.dto.request.DesignerDeleteRequestDto;
import blaybus.mvp.back.dto.request.DesignerRequestDto;
import blaybus.mvp.back.dto.response.DesignerResponseDto;
import blaybus.mvp.back.exception.ApiException;
import blaybus.mvp.back.exception.ErrorDefine;
import blaybus.mvp.back.repository.DesignerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // 필터에 따라서 디자이너 보여줌
    public List<DesignerResponseDto> showDesignerList(DesignerRequestDto designerRequestDto) {
        // 기본값 : 전체 디자이너 조회
        List<Designer> designers = designerRepository.findAll();

        // 필터 적용

        // 1. 전문 분야 필터링
        if (designerRequestDto.field() != null && !designerRequestDto.field().isEmpty()) {
            designers = designers.stream()
                    .filter(d -> d.getField().equals(designerRequestDto.field()))
                    .collect(Collectors.toList());
        }

        // 2. 대면 비대면 필터링

        // 비대면만 선택
        if (designerRequestDto.isOnline() && !designerRequestDto.isOffline()) {
            designers = designers.stream()
                    .filter(Designer::getIsOnline)
                    .collect(Collectors.toList());
        }
        // 대면만 선택
        else if(!designerRequestDto.isOnline() && designerRequestDto.isOffline()) {
            designers = designers.stream()
                    .filter(Designer::getIsOffline)
                    .collect(Collectors.toList());
        }

        // 3. 가격대 필터링
        //  오류 처리
        Integer minPrice = designerRequestDto.minPrice();
        Integer maxPrice = designerRequestDto.maxPrice();
        // 오류 처리 (minPrice가 maxPrice보다 크면 예외 발생)
        if (minPrice != null && maxPrice != null && maxPrice < minPrice) {
            throw new ApiException(ErrorDefine.INVALID_HEADER_ERROR); // TODO: 가격 설정 오류 반환 메시지 추가
        }

        // 가격 필터 적용 (minPrice, maxPrice가 존재하는 경우만)
        if (minPrice != null && maxPrice != null) {
            // 비대면만 있을 때 가격 조회
            if (designerRequestDto.isOnline() && !designerRequestDto.isOffline()) {
                designers = designers.stream()
                        .filter(d -> {
                            Integer onPrice = d.getOnPrice();

                            // 가격이 null이 아닐 경우, minPrice ≤ price ≤ maxPrice 여부 확인

                            return (onPrice != null && onPrice >= minPrice && onPrice <= maxPrice);
                        })
                        .collect(Collectors.toList());
            }
            // 대면 가격 조회
            else if(!designerRequestDto.isOnline() && designerRequestDto.isOffline()) {
                designers = designers.stream()
                        .filter(d -> {
                            Integer offPrice = d.getOffPrice();

                            // 가격이 null이 아닐 경우, minPrice ≤ price ≤ maxPrice 여부 확인

                            return (offPrice != null && offPrice >= minPrice && offPrice <= maxPrice);
                        })
                        .collect(Collectors.toList());
            }
        }


        return designers.stream()
                .map(DesignerResponseDto::of)
                .collect(Collectors.toList());

    }
}
