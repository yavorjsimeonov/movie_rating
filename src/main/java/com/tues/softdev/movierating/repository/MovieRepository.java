package com.tues.softdev.movierating.repository;

import com.tues.softdev.movierating.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

  Movie findByTitle(String title);
  List<Movie> findAllByDirector(String director);

  List<Movie> findAllByYear(int year);

}
