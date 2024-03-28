package yoon.docker.memberService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.docker.memberService.entity.Members;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

    Members findMembersByEmail(String email);

    boolean existsMembersByEmail(String email);

    Members findMembersByRefresh(String token);

}
