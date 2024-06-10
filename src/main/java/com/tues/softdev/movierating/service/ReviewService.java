package com.tues.softdev.movierating.service;

import com.tues.softdev.movierating.model.Review;
import com.tues.softdev.movierating.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

  @Autowired
  ReviewRepository reviewRepository;

  public Review save(Review review) {
    return reviewRepository.save(review);
  }

  public List<Review> findAllByRating(double rating) {
    return reviewRepository.findAllByRating(rating);
  }

  public List<Review> findAllByReviewAuthorName(String reviewAuthorName) {
    return reviewRepository.findAllByReviewAuthorName(reviewAuthorName);
  }

  public List<Review> findAll() {
    return reviewRepository.findAll();
  }
}
