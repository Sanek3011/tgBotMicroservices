package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
//@ToString(exclude = "reports")
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name")
    String name;
    @Column(name = "tgid")
    Long telegramId;
    @Enumerated(EnumType.STRING)
    Role role;
//    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
//    List<Report> reports;
    @Column(name = "score")
    Integer score;
//    @Enumerated(EnumType.STRING)
//    State state;
    @Column(name = "token")
    String token;
    @Column(name = "expires_at")
    LocalDateTime expiresAt;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }



    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
