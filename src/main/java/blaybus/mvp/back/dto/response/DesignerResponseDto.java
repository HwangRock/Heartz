package blaybus.mvp.back.dto.response;

import blaybus.mvp.back.domain.Designer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DesignerResponseDto {
    Long designerId;
    String profilePhoto; // 프로필 url
    String name;
    String field; // 전문 분야
    String location; // 지역구
    Integer offPrice; // 대면 가격
    Integer onPrice; // 비대면 가격
    Boolean isOnline; // 비대면 여부 1 => 비대면 가능
    Boolean isOffline; // 대면 여부 1 => 대면 가능
    Integer rating;
    String text;


    public static DesignerResponseDto of(Designer designer) {
        return DesignerResponseDto.builder()
                .designerId(designer.getDesignerId())
                .profilePhoto(designer.getProfilePhoto())
                .name(designer.getName())
                .field(designer.getField())
                .isOnline(designer.getIsOnline())
                .isOffline(designer.getIsOffline())
                .onPrice(designer.getOnPrice())
                .offPrice(designer.getOffPrice())
                .rating(designer.getRating())
                .text(designer.getText())
                .location(designer.getLocation())
                .build();
    }
}