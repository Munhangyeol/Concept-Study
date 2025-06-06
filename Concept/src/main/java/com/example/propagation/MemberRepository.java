package com.example.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    @Transactional
    public void save(Member member){
        log.info("member 저장");
        em.persist(member);
    }
    public Optional<Member>find(String username){
        return em.createQuery("select m from Member m where username=:username", Member.class)
                .setParameter("username", username)
                .getResultList().stream().findAny();
    }
}
