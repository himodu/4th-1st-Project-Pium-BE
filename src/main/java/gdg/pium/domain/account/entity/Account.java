package gdg.pium.domain.account.entity;

import gdg.pium.global.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "accounts")
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Size(min = 2, max = 10)
    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // 기본 : DONATOR(인증 완료 시 수정)
    private UserRole role;

    @Column(nullable = false)
    private String profileImageUrl;

    @Builder
    public Account(String email, String password, String nickname, UserRole role, String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
    }
}
