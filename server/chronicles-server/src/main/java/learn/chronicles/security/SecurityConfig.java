package learn.chronicles.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnWebApplication

public class SecurityConfig {
    // SecurityFilterChain allows configuring
    // web based security for specific http requests.

    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfiguration) throws Exception{
        // we're not using HTML forms in our app
        //so disable CSRF (Cross Site Request Forgery)
        http.csrf().disable();

        // this configures Spring Security to allow
        //CORS related requests (such as preflight checks)
        http.cors();

        // the order of the antMatchers() method calls is important
        // as they're evaluated in the order that they're added
        http.authorizeRequests()
                //add the authentication path and then our requests that we want to authorize
                .antMatchers(HttpMethod.POST,"/authenticate").permitAll()
                //for account creation
                .antMatchers(HttpMethod.POST,"/create_account").permitAll()
                //refresh the tokens
                .antMatchers(HttpMethod.POST,"/refresh_token").authenticated()

                .antMatchers(HttpMethod.GET, "/api/game-instance").authenticated()
                .antMatchers(HttpMethod.GET, "/api/game-instance/user-title/*").authenticated()

                .antMatchers(HttpMethod.POST, "/api/game-instance").authenticated()
                .antMatchers(HttpMethod.POST, "/api/game-instance/*").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/game-instance").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/game-instance/*").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/game-instance").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/game-instance/*").authenticated()

                .antMatchers(HttpMethod.PUT, "/api/user/**").authenticated()

                .antMatchers(HttpMethod.GET, "/api/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority("ADMIN")

                .antMatchers("/room/**").permitAll()

                //if we get to this point, let's deny all requests
                .antMatchers("/**").permitAll()
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(authConfiguration), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    //validate the user's credentials using the authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
