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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private  String password;
    private  String username;

    @Singular
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "User_Authority", joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
    inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID",referencedColumnName = "ID")})
    private  Set<Authority> authorities;
    @Builder.Default
    private  boolean accountNonExpired =true;
    @Builder.Default
    private  boolean accountNonLocked =true;
    @Builder.Default
    private  boolean credentialsNonExpired =true;
    @Builder.Default
    private  boolean enabled =true;
}
