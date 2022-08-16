package com.example.car_rental.auth;

import com.example.car_rental.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    //private final JwtRequestFilter jwtRequestFilter;
    private final AuthenticationConfiguration authenticationConfiguration;


    public SecurityConfig(DataSource dataSource, AuthenticationConfiguration authenticationConfiguration) {
        this.dataSource = dataSource;
        //this.jwtRequestFilter = jwtRequestFilter;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PATCH", "DELETE")
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowCredentials(true);
            }
        };
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .withUser("Adam")
//                .password("{bcrypt}" + new BCryptPasswordEncoder().encode("haslo123"))
//                .roles("Admin");
//        //auth.userDetailsService(email -> (UserDetails) userService.readUserByEmail(email));
//    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        MyUserDetails user = (MyUserDetails) User.builder()
//                .username("adkowal777@gmail.com")
//                .password(new BCryptPasswordEncoder().encode("haslo123"))
//                .roles("Admin")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    @Override
    //@Bean
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .authorizeRequests()
                //users
                .antMatchers(HttpMethod.POST, "/users/register").permitAll()
                .antMatchers(HttpMethod.GET, "/users/{user_id}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/users/{user_id}").hasRole("Admin")
                .antMatchers(HttpMethod.PATCH, "/users/{user_id}").hasRole("Admin")
                .antMatchers(HttpMethod.POST, "/users/login").permitAll()
                .antMatchers(HttpMethod.GET, "/users").permitAll()
                //cars
                .antMatchers(HttpMethod.GET, "/cars").permitAll()
                .antMatchers(HttpMethod.POST, "/cars").hasRole("Admin")
                .antMatchers(HttpMethod.GET, "/cars/{car_id}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/cars/{car_id}").hasRole("Admin")
                .antMatchers(HttpMethod.PATCH, "/cars/{car_id}").hasRole("Admin")
                //rentals
                .antMatchers(HttpMethod.GET, "/rentals/car/{car_id}/active").permitAll()
                .antMatchers(HttpMethod.GET, "/rentals/car/{car_id}").hasRole("Admin")
                .antMatchers(HttpMethod.GET, "/rentals/user/{user_id}/active").hasRole("Admin")
                .antMatchers(HttpMethod.GET, "/rentals/user/{user_id}/unpaid").hasRole("Admin")
                .antMatchers(HttpMethod.GET, "/rentals/user/{user_id}").hasRole("Admin")
                .antMatchers(HttpMethod.POST, "/rentals/user/{user_id}").hasRole("Admin")
                .antMatchers(HttpMethod.DELETE, "/rentals/{rental_id}").permitAll()
                .antMatchers(HttpMethod.PATCH, "/rentals/{rental_id}").hasRole("Admin")
                .antMatchers(HttpMethod.GET, "/rentals").permitAll()
                .antMatchers(HttpMethod.POST, "/rentals").permitAll()
                .antMatchers(HttpMethod.GET, "/rentals/unpaid").permitAll()
                .antMatchers(HttpMethod.GET, "/rentals/unreturned").permitAll()
                .antMatchers(HttpMethod.GET, "/rentals/pay/{rental_id}").permitAll()
                .antMatchers(HttpMethod.GET, "/rentals/{rental_id}/return").hasRole("Admin")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //http.addFilterBefore(jwtRequestFilter, JwtRequestFilter.class);
    }
}
