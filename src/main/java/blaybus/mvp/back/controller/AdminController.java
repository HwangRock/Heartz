package blaybus.mvp.back.controller;

import blaybus.mvp.back.dto.request.DesignerAddRequestDto;
import blaybus.mvp.back.dto.request.DesignerDeleteRequestDto;
import blaybus.mvp.back.dto.response.ResponseDto;
import blaybus.mvp.back.service.DesignerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final DesignerService designerService;

    // 디자이너 DB 정보 추가
    @PostMapping("/addDesigner")
    public ResponseDto<Boolean> addDesigner(@RequestBody DesignerAddRequestDto designerAddRequestDto) {
        return new ResponseDto<>(designerService.createDesigner(designerAddRequestDto));
    }

    // 디자이너 정보 수정
    @PutMapping("/updateDesigner")
    public ResponseDto<Boolean> updateDesigner(@RequestBody DesignerAddRequestDto designerAddRequestDto) {
        return new ResponseDto<>(designerService.updateDesigner(designerAddRequestDto));
    }

    @DeleteMapping("/deleteDesigner")
    public ResponseDto<Boolean> deleteDesigner(@RequestBody DesignerDeleteRequestDto designerDeleteRequestDto) {
        return new ResponseDto<>(designerService.deleteDesigner(designerDeleteRequestDto));
    }

}
