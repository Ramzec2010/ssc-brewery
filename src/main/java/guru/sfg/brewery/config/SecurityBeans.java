package guru.sfg.brewery.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.ICredentialRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration
public class SecurityBeans {

    @Bean
    public GoogleAuthenticator googleAuthenticator(ICredentialRepository credentialRepository) {
        GoogleAuthenticatorConfig authenticatorConfig = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder().setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30))
                .setWindowSize(10)
                .setNumberOfScratchCodes(0)
                .build();

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator(authenticatorConfig);
        googleAuthenticator.setCredentialRepository(credentialRepository);

        return googleAuthenticator;
    }

    @Bean
    public AuthenticationEventPublisher applicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);

        return tokenRepository;
    }

}