package com.pyonsnalcolor.member.dto;

import com.pyonsnalcolor.member.entity.Member;
import com.pyonsnalcolor.member.enumtype.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Schema(description = "사용자 정보 Response DTO")
@Getter
public class MemberInfoResponseDto {

    @Schema(description = "OAuth ID", example = "apple-user@gmail.com")
    @NotBlank
    private String oauthId;

    @Schema(description = "OAuth 타입", example = "APPLE")
    @NotBlank
    private String oauthType;

    @NotBlank
    private String profileImage;

    @NotBlank
    private Long memberId;

    @NotBlank
    private String nickname;

    @NotBlank
    private String email;

    @Schema(description = "게스트 유저인지 구분용", required = true)
    @NotBlank
    private Boolean isGuest;

    public MemberInfoResponseDto(Member member) {
        this.memberId  = member.getId();
        this.oauthId = member.getOAuthId();
        this.oauthType = member.getOAuthType().toString();
        this.nickname  = member.getNickname();
        this.profileImage = member.getProfileImage();
        this.email  = member.getEmail();
        this.isGuest = member.getRole().equals(Role.ROLE_GUEST); // 게스트 구분용 필드 추가
    }
}