package pl.kurs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("adam")
                .password(encoder.encode("adam"))
                .roles("ADMIN")
                .build();

        UserDetails manager1 = User.withUsername("marek")
                .password(encoder.encode("marek"))
                .roles("MANAGER")
                .build();

        UserDetails manager2 = User.withUsername("mariusz")
                .password(encoder.encode("mariusz"))
                .roles("MANAGER")
                .build();

        UserDetails user1 = User.withUsername("pawel")
                .password(encoder.encode("pawel"))
                .roles("USER")
                .build();

        UserDetails user2 = User.withUsername("tomek")
                .password(encoder.encode("tomek"))
                .roles("USER")
                .build();

        UserDetails user3 = User.withUsername("darek")
                .password(encoder.encode("darek"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, manager1, manager2, user1, user2, user3);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.GET, "/cars/**")
                                .hasAnyRole("ADMIN", "MANAGER", "USER")
                                .requestMatchers(HttpMethod.POST, "/cars")
                                .hasAnyRole("ADMIN", "MANAGER", "USER")
                                .requestMatchers(HttpMethod.PUT, "/cars")
                                .hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.DELETE, "/cars/**")
                                .hasRole("ADMIN")
                                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }

}
