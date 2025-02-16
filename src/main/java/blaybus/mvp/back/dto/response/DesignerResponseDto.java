package blaybus.mvp.back.dto.response;

import blaybus.mvp.back.domain.Designer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DesignerResponseDto {
    String profilePhoto;
    String field;
    Integer offPrice;
    Integer onPrice;
    Boolean isOnLine;
    Boolean isOffLine;
    Integer rating;


    public static DesignerResponseDto of(Designer designer) {
        return DesignerResponseDto.builder()
                .profilePhoto(designer.getProfilePhoto())
                .field(designer.getField())
                .isOnLine(designer.getIsOnline())
                .onPrice(designer.getOnPrice())
                .offPrice(designer.getOffPrice())
                .rating(designer.getRating())
                .build();
    }
}