package jjan_back_renewal.user.controller;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import jjan_back_renewal.user.dto.*;
import jjan_back_renewal.config.Response;
import jjan_back_renewal.user.service.UserService;
import jjan_back_renewal.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "이메일 중복 체크", description = "중복 이메일일 시 403 status code 반환합니다.")
    @PostMapping("/unique-email")
    public ResponseEntity<UniqueTestResponseDto> isDuplicatedEmail(@RequestBody String email) {
        if (isEmail(email) && userService.isDuplicatedEmail(email) == UserServiceImpl.NOT_DUPLICATED) {
            return ResponseEntity.ok().body(new UniqueTestResponseDto("email", email));
        } else {
            UniqueTestResponseDto response = new UniqueTestResponseDto("email", email);
            response.response403();
            return ResponseEntity.ok().body(response);
        }
    }

    @Operation(summary = "닉네임 중복 체크", description = "중복 닉네임일 시 403 status code 반환합니다.")
    @PostMapping("/unique-nickname")
    public ResponseEntity<UniqueTestResponseDto> isDuplicatedNickName(@RequestBody String nickName) {

        if (isNickNameLengthOK(nickName) && userService.isDuplicatedNickName(nickName) == UserServiceImpl.NOT_DUPLICATED) {
            return ResponseEntity.ok().body(new UniqueTestResponseDto("nickName", nickName));
        } else {
            UniqueTestResponseDto uniqueTestResponseDto = new UniqueTestResponseDto("nickName", nickName);
            uniqueTestResponseDto.response403();
            return ResponseEntity.ok().body(uniqueTestResponseDto);
        }

    }

    private boolean isNickNameLengthOK(String nickName) {
        if (nickName.length() >= 8 && nickName.length() <= 16) {
            return true;
        } else
            return false;
    }


    private boolean isEmail(String email) {
        boolean validation = false;
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            validation = true;
        }
        return validation;
    }


}
