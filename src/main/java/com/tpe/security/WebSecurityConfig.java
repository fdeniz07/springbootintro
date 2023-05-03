package com.tpe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration  //Security katmanina bu class'in konfigürasyon class'i oldugunu söylüyoruz
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //Metot seviyede yetkilendirme yapacagimizi söylüyoruz / Eger endpoint seviyesinde yetkilendirmes yapilacaksa burasi yazilmaz
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //!!! Bu class'daki 1. amacimiz : AuthenticationManager
    //!!! Bu class'daki 2. amacimiz : AuthenticationProvider
    //!!! Bu class'daki 3. amacimiz : PasswordEncoder'larimizi olusturup birbirleriyle tanistirmak


    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable(). //csrf korumasini kapatiyoruz. Browserin farkli tablari arasinda gecis ile araya girme saldirilarina csrf saldirilari denir.
                authorizeHttpRequests().
                antMatchers("/",
                                        "index.html",
                                        "/register",
                                        "/css/*",
                                        "/js/*").permitAll(). //bu endpoint'leri yetkili mi diye kontrol etme (anonim olarak git yükle)
               //and().
               // authorizeRequests().antMatchers("/students/**").hasRole("ADMIN"). //Endpoint(Class) bazinda seviye yetkilendirmesi bu sekilde
                anyRequest(). //muaf tutulan endpointler disinda gelen herhangi bir requesti
                authenticated().//yetkili mi diye kontrol et
                and().
                httpBasic(); //bunu yaparkende Basic Auth. kullanilacagini belirtiyoruz

    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder()); //encoder ile tanistiriyoruz
        authProvider.setUserDetailsService(userDetailsService); //userDetailService ile tanistiriyoruz

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(authProvider());

    }
}
