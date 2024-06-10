package com.tues.softdev.movierating.service;

import com.tues.softdev.movierating.model.Movie;
import com.tues.softdev.movierating.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

  @Autowired
  public MovieRepository movieRepository;

  public Movie save(Movie movie) {
    return movieRepository.save(movie);
  }

  public Optional<Movie> findById(Long id) {
    return movieRepository.findById(id);
  }

  public Movie findByTitle(String title) {
    return movieRepository.findByTitle(title);
  }

  public List<Movie> findAllByDirector(String director) {
    return movieRepository.findAllByDirector(director);
  }

  public List<Movie> findAllByYear(int year) {
    return movieRepository.findAllByYear(year);
  }

  public List<Movie> findAll() {
    return movieRepository.findAll();
  }

}
