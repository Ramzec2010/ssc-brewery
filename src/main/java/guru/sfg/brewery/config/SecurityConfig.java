package guru.sfg.brewery.config;

import guru.sfg.brewery.security.JpaUserDetailsService;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlVariableAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//Enable method security annotation config
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    // needed for use with Spring Data JPA SPeL
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }


    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }


    public RestUrlVariableAuthFilter restUrlVariableAuthFilter(AuthenticationManager authenticationManager) {
        RestUrlVariableAuthFilter filter = new RestUrlVariableAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    /*    @Bean
        PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
            return new LdapShaPasswordEncoder();
            return  new StandardPasswordEncoder();
        }*/
    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
/*        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
        http.addFilterAfter(restUrlVariableAuthFilter(authenticationManager()), RestHeaderAuthFilter.class)
                .csrf().disable();*/

        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll()  //do not use in production
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                    //.antMatchers(HttpMethod.GET, "/api/v1/beer/**")
                    //.hasAnyRole("ADMIN", "USER", "CUSTOMER")
                    //.mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")
                    //.mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries")
                    //.hasAnyRole("ADMIN", "CUSTOMER")
                    //.mvcMatchers(HttpMethod.GET, "/brewery/breweries").hasAnyRole("ADMIN", "CUSTOMER")
                    // .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}")
                    // .hasAnyRole("ADMIN", "USER", "CUSTOMER")
                    //.mvcMatchers("/beers/find", "/beers/{beerId}")
                    //.hasAnyRole("ADMIN", "USER", "CUSTOMER")
                    ;

                })
/*                .authorizeRequests(authorize -> {
                    authorize.antMatchers("/beers/find", "/beers*").permitAll();
                })*/
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin(loginConfigurer -> {
                    loginConfigurer.loginProcessingUrl("/login")
                            .loginPage("/").permitAll()
                            .successForwardUrl("/")
                            .defaultSuccessUrl("/")
                            .failureUrl("/?error");
                })


                .logout(logoutConfigurer -> {
                    logoutConfigurer.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                            .logoutSuccessUrl("/?logout")
                            .permitAll();
                }).httpBasic()
                .and().csrf().ignoringAntMatchers("/h2-console/**", "/api/**")
                .and().rememberMe()
                        .key("sfg-key")
                        .userDetailsService(userDetailsService);

        //h2 console config
        http.headers().frameOptions().sameOrigin();
    }

    @Autowired
    JpaUserDetailsService jpaUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.jpaUserDetailsService).passwordEncoder(passwordEncoder());
/*        auth.inMemoryAuthentication()
                .withUser("spring")
                //{noop} no operation password encoder
                // each password should be stored encoded depending on the algorithm
                .password("{noop}guru")
                .password("{bcrypt}$2a$10$7tYAvVL2/KwcQTcQywHIleKueg4ZK7y7d44hKyngjTwHCDlesxdla")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{sha256}1296cefceb47413d3fb91ac7586a4625c33937b4d3109f5a4dd96c79c46193a029db713b96006ded")
                .roles("USER");

        auth.inMemoryAuthentication().withUser("scott").password("{bcrypt15}$2a$15$on8ctAqzUZD1NhsBwNrbHOjfCu3zcCIty0yoPqCt69MRLDkuBK8O2").roles("CUSTOMER");*/
    }
/*    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("spring")
                .password("guru")
                .roles("ADMIN")
                .build();

        UserDetails user =  User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }*/
}
