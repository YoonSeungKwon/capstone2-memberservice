package yoon.docker.memberService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.docker.memberService.entity.Members;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

    Members findMembersByMemberIdx(long idx);

    Members findMembersByEmail(String email);

    boolean existsMembersByEmail(String email);

    void deleteByMemberIdx(long idx);

    Members findMembersByRefresh(String token);

}
