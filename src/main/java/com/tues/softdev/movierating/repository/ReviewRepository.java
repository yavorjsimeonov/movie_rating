package com.tues.softdev.movierating.repository;

import com.tues.softdev.movierating.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  List<Review> findAllByRating(double rating);

  List<Review> findAllByReviewAuthorName(String reviewAuthorName);

}