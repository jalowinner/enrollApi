package com.dingren.enrolldemo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dingren.enrolldemo.JwtConfig.JwtTokenConfigurer;
import com.dingren.enrolldemo.JwtConfig.JwtTokenProvider;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private UserDetailsService userDetailsService;
	/*@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}*/
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }*/
	
	
	/*@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*http.authorizeRequests()
			.antMatchers("/login","/signup")
				.permitAll()
			.antMatchers("/**")
				.authenticated()
			.and()
            	.formLogin()
            	.successHandler(successHandler())
            	.failureHandler(failureHandler())
	            .permitAll()
            .and()
	            .logout()
	            .deleteCookies("JSESSIONID")
	            .invalidateHttpSession(true)
	            .permitAll()
	            .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
	            	MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
                    String username = userDetails.getUsername();
                    httpServletResponse.getWriter().append("The user " + username + " has logged out.");
	                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
	            })
	        .and()
	            .exceptionHandling()
	            .accessDeniedHandler(accessDeniedHandler())
	            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
	         .and()
	        	.cors()
	        .and()
	        	.csrf().disable();*/
			http.cors();
			http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
					.authorizeRequests().antMatchers("/login","/signup").permitAll().anyRequest().authenticated();
			http.apply(new JwtTokenConfigurer(jwtTokenProvider));

	        
		
	/*.configurationSource(request -> {
	        		  CorsConfiguration cors = new CorsConfiguration();
	        	      cors.setAllowedOrigins(Arrays.asList("localhost:3000"));
	        	      cors.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
	        	      cors.setAllowedHeaders(Arrays.asList("*"));
	        	      return cors;
	        	    })*/	
        
	}
	
	private AuthenticationSuccessHandler successHandler() {
	    return new AuthenticationSuccessHandler() {
	      @Override
	      public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
    	    MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            Map<String, String> map= new HashMap<String, String>();
    		map.put("username", username);
            String json = new Gson().toJson(map);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().write(json);
	        httpServletResponse.setStatus(200);
	      }
	    };
	  }

	  private AuthenticationFailureHandler failureHandler() {
	    return new AuthenticationFailureHandler() {
	      @Override
	      public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
	        httpServletResponse.getWriter().append("Authentication failure");
	        httpServletResponse.setStatus(401);
	      }
	    };
	  }

	  private AccessDeniedHandler accessDeniedHandler() {
	    return new AccessDeniedHandler() {
	      @Override
	      public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
	        httpServletResponse.getWriter().append("Access denied");
	        httpServletResponse.setStatus(403);
	      }
	    };
	  }
	
}
