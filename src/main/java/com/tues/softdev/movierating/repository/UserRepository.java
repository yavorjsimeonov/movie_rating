package com.tues.softdev.movierating.repository;

import com.tues.softdev.movierating.model.Movie;
import com.tues.softdev.movierating.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByName(String name);

}
