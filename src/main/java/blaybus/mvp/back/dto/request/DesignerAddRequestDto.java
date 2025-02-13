package blaybus.mvp.back.dto.request;

import blaybus.mvp.back.domain.Designer;
import lombok.Builder;

// TODO: S3 추가 후 profilePhoto 관련 작업 필요
@Builder
public record DesignerAddRequestDto(
        Long designer_id,
        String name,
        String location,
        String address,
        String field,
        Integer offPrice,
        Integer onPrice,
        String rating
        ) {

    public Designer toEntity() {
        return Designer.builder()
                .designerId(this.designer_id)
                .name(this.name)
                .location(this.location)
                .address(this.address)
                .field(this.field)
                .offPrice(this.offPrice)
                .onPrice(this.onPrice)
                .rating(this.rating)
                .build();
    }
}
