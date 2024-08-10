package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            // 팀 생성
            Team team = new Team();
            team.setName("TeamA");

// 팀 등록
            em.persist(team);

// 멤버 생성
            Member member = new Member();
            member.setUsername("member1");

// 멤버를 팀에 소속시키기 (방법1) - 기본
            member.setTeam(team);
//            team.getMembers().add(member);

            // 멤버 등록
            em.persist(member);

            em.flush();
            em.clear();

            // 단순 팀 조회
            Team findTeam = em.find(Team.class, team.getId());
            System.out.println("findTeam.getName() = " + findTeam.getName());

// 단순 멤버 조회
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember = " + findMember.getUsername());

// 멤버를 통한 팀 조회 (가능)
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());

// 팀을 통한 멤버 조회 (가능)
            System.out.println("findTeamMembers = " + findTeam.getMembers());



            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}





