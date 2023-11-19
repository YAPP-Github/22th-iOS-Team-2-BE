package com.pyonsnalcolor.member.controller;

import com.pyonsnalcolor.member.AuthMemberId;
import com.pyonsnalcolor.member.dto.MemberInfoResponseDto;
import com.pyonsnalcolor.member.dto.NicknameRequestDto;
import com.pyonsnalcolor.member.dto.TokenDto;
import com.pyonsnalcolor.member.service.AuthService;
import com.pyonsnalcolor.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Tag(name = "(인증 제외)사용자 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;

    @Operation(summary = "로그아웃", description = "사용자의 JWT 토큰을 무효화합니다.")
    @Parameter(name = "tokenDto", description = "JWT 로그인 토큰")
    @DeleteMapping("/logout")
    public ResponseEntity logout(@RequestBody TokenDto tokenDto) {
        authService.logout(tokenDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "사용자 정보 조회", description = "사용자의 정보를 조회합니다.")
    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponseDto> getMemberInfo(
            @Parameter(hidden = true) @AuthMemberId Long memberId
    ) {
        MemberInfoResponseDto memberInfoResponseDto = memberService.getMemberInfo(memberId);
        return new ResponseEntity(memberInfoResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "이전) 닉네임 변경", description = "사용자의 닉네임을 변경합니다.")
    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(
            @RequestBody @Valid NicknameRequestDto nicknameRequestDto,
            @Parameter(hidden = true) @AuthMemberId Long memberId
    ) {
        memberService.updateProfile(memberId, null, nicknameRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "프로필 변경", description = "사용자의 닉네임, 프로필 사진을 변경합니다.")
    @PatchMapping(value = "/profile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Void> updateProfile(
            @RequestPart(value = "nicknameRequestDto", required = false) @Valid NicknameRequestDto nicknameRequestDto,
            @RequestPart(value="imageFile", required = false) MultipartFile imageFile,
            @Parameter(hidden = true) @AuthMemberId Long memberId
    ) {
        memberService.updateProfile(memberId, imageFile, nicknameRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "닉네임 중복 검사", description = "닉네임 중복 여부를 검사합니다.")
    @PostMapping("/nickname")
    public ResponseEntity<Void> validateNickname(
            @RequestBody @Valid NicknameRequestDto nicknameRequestDto
    ) {
        memberService.checkIfNicknameIsDuplicate(nicknameRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "회원 탈퇴", description = "사용자 정보를 삭제합니다.")
    @DeleteMapping("/withdraw")
    public ResponseEntity<TokenDto> withdraw(
            @Parameter(hidden = true) @AuthMemberId Long memberId
    ) {
        authService.withdraw(memberId);
        return new ResponseEntity(HttpStatus.OK);
    }
}