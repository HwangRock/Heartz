package blaybus.mvp.back.controller;


import blaybus.mvp.back.domain.Designer;
import blaybus.mvp.back.dto.request.SecduleReadRequestDTO;
import blaybus.mvp.back.dto.response.CustomUserDetails;
import blaybus.mvp.back.dto.response.SecduleResponseDTO;
import blaybus.mvp.back.service.ClientService;
import blaybus.mvp.back.service.DesignerService;
import blaybus.mvp.back.service.SecduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/schedule")
public class SecduleController {

    private final SecduleService secduleService;
    private final ClientService clientService;
    private final DesignerService designerService;

    @GetMapping("/readDesignerTimeSchedule")
    public SecduleResponseDTO readDesignerTimeSecdule(@RequestParam Long designerId, LocalDate date){

        String email = clientService.getCurrentUserEmail();
        Long userId = clientService.userIdByEmail(email);

        //스케줄 조회
        return secduleService.readDesignerTimeSecdule(designerId, date);
    }

    //사용자가 예약하기 버튼 누를 시 스케줄 테이블에 정보 저장. 10분 타이머

    @GetMapping("/readDesignerTimeScheduleBack")
    public SecduleResponseDTO readDesignerTimeScheduleBack(@RequestParam Long designerId, LocalDate date){

        //String email = clientService.getCurrentUserEmail();
        String email = "choeunbin0324@gmail.com";
        Long userId = clientService.userIdByEmail(email);

        Designer designer = designerService.getDesignerById(designerId);

        //저장된 예약 정보 삭제
        secduleService.goBackPage(userId, date, designer);
        //스케줄 조회
        return secduleService.readDesignerTimeSecdule(designerId, date);
    }

}
