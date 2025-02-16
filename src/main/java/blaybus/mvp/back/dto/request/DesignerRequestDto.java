package blaybus.mvp.back.dto.request;

import lombok.Builder;

@Builder
public record DesignerRequestDto(
        String location, // 지역구
        String field, // 전문 분야
        Boolean isOnline,
        Boolean isOffline,
        Integer minPrice, // 최소 요금
        Integer maxPrice // 최대 요금
        ) {

}
