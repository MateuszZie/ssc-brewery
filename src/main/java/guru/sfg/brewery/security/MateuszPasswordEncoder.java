package guru.sfg.brewery.security;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;


import java.util.HashMap;
import java.util.Map;

public class MateuszPasswordEncoder {

    public static PasswordEncoder createDelegatingPasswordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("bcrypt15", new BCrypt15PasswordEncoder());
        encoders.put("ldap", new LdapShaPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("sha256", new StandardPasswordEncoder());
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    private MateuszPasswordEncoder() {
    }
}
