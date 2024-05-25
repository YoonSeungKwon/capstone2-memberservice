package yoon.docker.memberService.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import yoon.docker.memberService.entity.Members;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

    Members findMembersByMemberIdx(long idx);

    Members findMembersByEmail(String email);

    Members findMembersByRefresh(String token);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsMembersByEmail(String email);

    void deleteByMemberIdx(long idx);

    List<Members> findMembersByMemberIdxIn(List<Long> list);

}
