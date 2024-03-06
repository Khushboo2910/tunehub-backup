package com.kodnest.tunehub.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kodnest.tunehub.entity.User;
//@org.springframework.stereotype.Repository
public interface UserRepository extends JpaRepository<User, String> {
	public User findByEmail(String email);
	
}