package guru.sfg.brewery.domain.security;

import guru.sfg.brewery.domain.Customer;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User implements UserDetails, CredentialsContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private  String password;
    private  String username;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

//    @Singular
//    @ManyToMany(cascade = CascadeType.MERGE)
//    @JoinTable(name = "User_Authority", joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
//    inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID",referencedColumnName = "ID")})
    @Transient
    private  Set<Authority> authorities;

    @Transient
    public Set<GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(Role::getAuthorities)
                .flatMap(Set::stream)
                .map(authority -> {
                    return new SimpleGrantedAuthority(authority.getPermission());
                })
                .collect(Collectors.toSet());
    }

    @Singular
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "User_Role", joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID",referencedColumnName = "ID")})
    private  Set<Role> roles;
    @Builder.Default
    private  boolean accountNonExpired =true;
    @Builder.Default
    private  boolean accountNonLocked =true;
    @Builder.Default
    private  boolean credentialsNonExpired =true;
    @Builder.Default
    private  boolean enabled =true;

    @Builder.Default
    private boolean userGoogle2fa = false;

    private String google2faSecret;

    @Transient
    private boolean google2faRequired = true;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;
}
