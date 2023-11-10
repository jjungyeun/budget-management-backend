package com.wonjung.budget.entity;

import com.wonjung.budget.exception.CustomException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

@Entity
@Getter
@ToString(of = {"id", "account", "nickname", "pushOption"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String account;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String nickname;

    @Column(nullable = false)
    Boolean pushOption;

    @Builder
    public Member(String account, String password, String nickname, Boolean pushOption) {
        Assert.hasText(account, "Member account must not be empty.");
        Assert.hasText(password, "Member password must not be empty.");
        Assert.hasText(nickname, "Member nickname must not be empty.");

        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.pushOption = pushOption != null ? pushOption : false;
    }

    public void edit(String nickname, Boolean pushOption) {
        Assert.hasText(nickname, "Member nickname must not be empty.");

        this.nickname = nickname;
        this.pushOption = pushOption != null ? pushOption : this.pushOption;
    }
}
