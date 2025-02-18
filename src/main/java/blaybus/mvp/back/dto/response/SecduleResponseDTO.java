package blaybus.mvp.back.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class SecduleResponseDTO {

    private List<Map.Entry<LocalTime, Boolean>> availabilityMap;

}
