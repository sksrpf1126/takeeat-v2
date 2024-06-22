package com.back.takeeat.domain.user;

import com.back.takeeat.common.domain.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String name;

    private String email;

    private String pwd;

    private String phone;

    @Enumerated(EnumType.STRING)
    private MemberRoleType role;

    private String profile;

    private String deleteYn;

}
