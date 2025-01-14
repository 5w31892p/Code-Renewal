package com.example.demo.domain.member.entity;


import com.example.demo.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "tb_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSeq;

    @Column(unique = true, nullable = false)
    private String memberKey;

    private String memberName;

    @Column(unique = true, nullable = false)
    private String memberId;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private MemberPermission permission;

    @Builder
    public Member(String memberKey,String memberName,  String memberId, String email, String password, MemberPermission permission) {
        this.memberKey = memberKey;
        this.memberName = memberName;
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.permission = permission;
    }
}
