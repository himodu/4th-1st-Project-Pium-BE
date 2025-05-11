package gdg.pium.domain.user.entity;

import gdg.pium.global.entity.BaseEntity;
import gdg.pium.global.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
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
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(nullable = false)
    private Provider provider;

    @Builder
    public User(String email, String password, String nickname, UserRole role, String profileImageUrl, Provider provider) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }
}
