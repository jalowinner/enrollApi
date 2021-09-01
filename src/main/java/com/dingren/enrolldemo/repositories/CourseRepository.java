package com.dingren.enrolldemo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dingren.enrolldemo.domains.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
	@Query("select e from #{#entityName} e where e.isActive=true and e.coursename=?1")
	Course findByCoursenameSoft(String coursename);
	
	@Transactional
	@Query("update #{#entityName} e set e.isActive=false where e.coursename=?1")
	@Modifying
	Integer deleteByCoursenameSoft(String coursename);
	@Query("select e from #{#entityName} e where e.isActive=true")
	List<Course> findAllSoft();
}
