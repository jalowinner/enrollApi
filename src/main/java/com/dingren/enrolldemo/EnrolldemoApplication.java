package com.dingren.enrolldemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dingren.enrolldemo.domains.Course;
import com.dingren.enrolldemo.domains.User;
import com.dingren.enrolldemo.repositories.CourseRepository;
import com.dingren.enrolldemo.repositories.UserRepository;

@SpringBootApplication
public class EnrolldemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnrolldemoApplication.class, args);
	}
	
	//initializing with simple data for testing and demo
	@Bean
    public CommandLineRunner demoData(UserRepository userrepo, CourseRepository courserepo) {
        return args -> { 
        	User u1 = new User();
        	User u2 = new User();
        	User u3 = new User();
        	User u4 = new User();
        	User u5 = new User();
        	User u6 = new User();
        	
        	u1.setUsername("admin1");
        	u2.setUsername("admin2");
        	u3.setUsername("hanjie");
        	u4.setUsername("sun");
        	u5.setUsername("luo");
        	u6.setUsername("liu");
            
        	String password = getEncoder().encode("1111");
        	
        	u1.setPassword(password);
        	u2.setPassword(password);
        	u3.setPassword(password);
        	u4.setPassword(password);
        	u5.setPassword(password);
        	u6.setPassword(password);
        	
        	u1.setRole("admin");
        	u2.setRole("admin");
        	u3.setRole("user");
        	u4.setRole("user");
        	u5.setRole("user");
        	u6.setRole("user");
        	
        	userrepo.save(u1);
        	userrepo.save(u2);
        	userrepo.save(u3);
        	userrepo.save(u4);
        	userrepo.save(u5);
        	userrepo.save(u6);
        	
        	
        	for(Integer i = 1; i < 11; i++) {
        		Course course = new Course();
        		course.setCoursename("ibm"+ i);
        		course.setCourseTitle("new employee security training "+i);
        		course.setInstructor("boss"+i);
        		course.setWorkload(i*10);
        		courserepo.save(course);
        	}
        	
        };
        
    }
	private PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

/*@Bean
public WebMvcConfigurer corsConfigurer() {
	return new WebMvcConfigurer() {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**").allowedOrigins("http://localhost:3000");
		}
	};
}*/
}