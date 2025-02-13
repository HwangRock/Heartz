package blaybus.mvp.back.controller;

import blaybus.mvp.back.dto.request.DesignerAddRequestDto;
import blaybus.mvp.back.dto.response.ResponseDto;
import blaybus.mvp.back.service.DesignerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final DesignerService designerService;

    @PostMapping("/addDesigner")
    public ResponseDto<Boolean> addDesigner(@RequestBody DesignerAddRequestDto designerAddRequestDto) {
        return new ResponseDto<>(designerService.createDesigner(designerAddRequestDto));
    }
}
