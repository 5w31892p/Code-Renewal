package com.example.demo.domain.member.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSeq;

    private String memberKey;

    private String memberName;

    @Column(unique = true, nullable = false)
    private String memberId;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Builder
    public Member(String memberKey,String memberName,  String memberId, String email, String password) {
        this.memberKey = memberKey;
        this.memberName = memberName;
        this.memberId = memberId;
        this.email = email;
        this.password = password;
    }
}
