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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "닉네임 변경", description = "사용자의 닉네임을 변경합니다.")
    @Parameter(name = "nickname", description = "변경할 닉네임, 15자 이내 특수문자 허용안됨")
    @PatchMapping("/nickname")
    public ResponseEntity<TokenDto> updateNickname(
            @RequestBody @Valid NicknameRequestDto nicknameRequestDto,
            @Parameter(hidden = true) @AuthMemberId Long memberId
    ) {
        memberService.updateNickname(memberId, nicknameRequestDto);
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