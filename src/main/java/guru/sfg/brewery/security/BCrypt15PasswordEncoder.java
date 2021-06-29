package guru.sfg.brewery.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BCrypt15PasswordEncoder implements PasswordEncoder {
    private Pattern BCRYPT_PATTERN;
    private final Log logger;
    private final int strength;
    private final BCrypt15PasswordEncoder.BCryptVersion version;
    private final SecureRandom random;

    public BCrypt15PasswordEncoder() {
        this(-1);
    }

    public BCrypt15PasswordEncoder(int strength) {
        this(strength, (SecureRandom)null);
    }

    public BCrypt15PasswordEncoder(BCrypt15PasswordEncoder.BCryptVersion version) {
        this(version, (SecureRandom)null);
    }

    public BCrypt15PasswordEncoder(BCrypt15PasswordEncoder.BCryptVersion version, SecureRandom random) {
        this(version, -1, random);
    }

    public BCrypt15PasswordEncoder(int strength, SecureRandom random) {
        this(BCrypt15PasswordEncoder.BCryptVersion.$2A, strength, random);
    }

    public BCrypt15PasswordEncoder(BCrypt15PasswordEncoder.BCryptVersion version, int strength) {
        this(version, strength, (SecureRandom)null);
    }

    public BCrypt15PasswordEncoder(BCrypt15PasswordEncoder.BCryptVersion version, int strength, SecureRandom random) {
        this.BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");
        this.logger = LogFactory.getLog(this.getClass());
        if (strength == -1 || strength >= 4 && strength <= 31) {
            this.version = version;
            this.strength = strength == -1 ? 15 : strength;
            this.random = random;
        } else {
            throw new IllegalArgumentException("Bad strength");
        }
    }

    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        } else {
            String salt;
            if (this.random != null) {
                salt = BCrypt.gensalt(this.version.getVersion(), this.strength, this.random);
            } else {
                salt = BCrypt.gensalt(this.version.getVersion(), this.strength);
            }

            return BCrypt.hashpw(rawPassword.toString(), salt);
        }
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        } else if (encodedPassword != null && encodedPassword.length() != 0) {
            if (!this.BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
                this.logger.warn("Encoded password does not look like BCrypt");
                return false;
            } else {
                return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
            }
        } else {
            this.logger.warn("Empty encoded password");
            return false;
        }
    }

    public boolean upgradeEncoding(String encodedPassword) {
        if (encodedPassword != null && encodedPassword.length() != 0) {
            Matcher matcher = this.BCRYPT_PATTERN.matcher(encodedPassword);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Encoded password does not look like BCrypt: " + encodedPassword);
            } else {
                int strength = Integer.parseInt(matcher.group(2));
                return strength < this.strength;
            }
        } else {
            this.logger.warn("Empty encoded password");
            return false;
        }
    }

    public static enum BCryptVersion {
        $2A("$2a"),
        $2Y("$2y"),
        $2B("$2b");

        private final String version;

        private BCryptVersion(String version) {
            this.version = version;
        }

        public String getVersion() {
            return this.version;
        }
    }
}

