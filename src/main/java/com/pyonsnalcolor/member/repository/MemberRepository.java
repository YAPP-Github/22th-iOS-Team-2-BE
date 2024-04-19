package com.pyonsnalcolor.member.repository;

import com.pyonsnalcolor.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByoAuthId(String oAuthId);

    @Query("select m.refreshToken from Member m where m.oAuthId= :oAuthId")
    Optional<String> findRefreshTokenByoAuthId(@Param("oAuthId") String oAuthId);

    Optional<Member> findByRefreshToken(String refreshToken);

    List<Member> findByNickname(String nickname);
}
