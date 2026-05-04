package server_2026_b.server.security;

import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;
import server_2026_b.server.config.ServerConfigurationException;

import javax.annotation.PostConstruct;

import static server_2026_b.server.utils.Constants.*;
import static server_2026_b.server.utils.ConfigurationErrors.*;

@Component
public class JwtConfig {
    private final Environment env;

    private String secret;
    private long accessExpiration;
    private long refreshExpiration;

    public JwtConfig(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() {
        this.secret = env.getProperty("JWT_SECRET", JWT_SECRET);
        this.accessExpiration = env.getProperty("JWT_ACCESS_EXPIRATION",
                Long.class, JWT_ACCESS_EXPIRATION);
        this.refreshExpiration = env.getProperty("JWT_REFRESH_EXPIRATION",
                Long.class, JWT_REFRESH_EXPIRATION);

        validate();
    }

    private void validate() {
        if (secret == null || secret.isBlank()) {
            throw new ServerConfigurationException(JWT_SECRET_MISSING);
        }
        if (secret.length() < 32) {
            throw new ServerConfigurationException(JWT_SECRET_TOO_SHORT);
        }

        if (accessExpiration <= 0) {
            throw new ServerConfigurationException(JWT_ACCESS_EXPIRATION_MUST_BE_POSITIVE);
        }
        if (refreshExpiration <= 0) {
            throw new ServerConfigurationException(JWT_REFRESH_EXPIRATION_MUST_BE_POSITIVE);
        }
        if (refreshExpiration <= accessExpiration) {
            throw new ServerConfigurationException(JWT_INVALID_EXPIRATION_TIME);
        }
    }

    public String getSecret() {
        return secret;
    }

    public long getAccessExpiration() {
        return accessExpiration;
    }

    public long getRefreshExpiration() {
        return refreshExpiration;
    }
}

