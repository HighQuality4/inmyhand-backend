package com.inmyhand.refrigerator.member.repository;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//       💗/( ･ω･`)ﾉ💗
//        　💖/(･ω･`)ﾉ💖
//        　　💖(ω･`ﾉ💛
//        　　　(･`💛 )
//        　　 (💛　　)ﾉ💚
//        　 💚/(　　 )ﾉ💚
//        　　💙(　 ´)ﾉ💙
//        　　　(　´ﾉ💙
//        　　　( ﾉ💜 )
//        　　　💜´･ω)
//        　 💜/( ･ω･)❤
//        　❤/( ･ω･`)ﾉ❤
//        💗/( ･ω･`)ﾉ💗
//        　💖/(･ω･`)ﾉ💖
//        　　💖(ω･`ﾉ💛
//        　　　(･`💛 )
//        　　 (💛　　)ﾉ💚
//        　 💚/(　　 )ﾉ💚
//        　　💙(　 ´)ﾉ💙
//        　　　(　´ﾉ💙
//        　　　( ﾉ💜 )
//        　　　💜´･ω)
//        　 💜/( ･ω･)❤
//        　❤/( ･ω･`)ﾉ❤
//        💗/( ･ω･`)ﾉ💗
//        　💖/(･ω･`)ﾉ💖
//        　　💖(ω･`ﾉ💛
//        　　　(･`💛 )
//        　　 (💛　　)

@Repository
public interface LoginRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);
}
