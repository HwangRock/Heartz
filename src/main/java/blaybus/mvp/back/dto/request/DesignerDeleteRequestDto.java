package blaybus.mvp.back.dto.request;


import blaybus.mvp.back.domain.Designer;
import lombok.Builder;

@Builder
public record DesignerDeleteRequestDto(Long designer_id, String admin_pw) {
}
