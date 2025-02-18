package blaybus.mvp.back.controller;

import blaybus.mvp.back.dto.request.DesignerRequestDto;
import blaybus.mvp.back.dto.response.DesignerResponseDto;
import blaybus.mvp.back.dto.response.ResponseDto;
import blaybus.mvp.back.service.DesignerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/designer")
public class MainPageController {
    private final DesignerService designerService;

    @PostMapping("/readDesignerList")
    public ResponseDto<List<DesignerResponseDto>> designerList(@RequestBody DesignerRequestDto designerRequestDto) {
        return new ResponseDto<>(designerService.showDesignerList(designerRequestDto));
    }

    @GetMapping("/readDesignerDetail/{designerId}")
    public ResponseDto<DesignerResponseDto> designerDetail(@PathVariable Long designerId) {
        return new ResponseDto<>(designerService.getDesigner(designerId));
    }
}