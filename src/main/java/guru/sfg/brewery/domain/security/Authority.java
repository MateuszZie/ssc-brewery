package guru.sfg.brewery.domain.security;

import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String permission;

//    @Singular
//    @ManyToMany(mappedBy = "authorities")
//    private Set<User> users;
    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;
}
