package blaybus.mvp.back.controller;


import blaybus.mvp.back.dto.request.SecduleReadRequestDTO;
import blaybus.mvp.back.dto.response.SecduleResponseDTO;
import blaybus.mvp.back.service.SecduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/secdule")
public class SecduleController {

    private final SecduleService secduleService;


    @GetMapping("/readDesignerTimeSchedule")
    public SecduleResponseDTO readDesignerTimeSecdule(@RequestBody SecduleReadRequestDTO secduleReadRequestDTO){
        return secduleService.readDesignerTimeSecdule(secduleReadRequestDTO);
    }

}
