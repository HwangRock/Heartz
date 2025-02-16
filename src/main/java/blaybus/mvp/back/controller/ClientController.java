package blaybus.mvp.back.controller;

import blaybus.mvp.back.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping(value="")
public class ClientController {

    @GetMapping("/login/success")
    public ResponseEntity<Object> loginSuccess(LoginResponse loginResponse) throws URISyntaxException {
        URI redirectUri = new URI("http://localhost:3000/login/redirect");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        httpHeaders.add("Authorization", "Bearer " + loginResponse.getAccessToken());
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

}
