package com.tues.softdev.movierating;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tues.softdev.movierating.model.Movie;
import com.tues.softdev.movierating.model.Review;
import com.tues.softdev.movierating.repository.MovieRepository;
import com.tues.softdev.movierating.repository.ReviewRepository;
import com.tues.softdev.movierating.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.GetAccountSettingsResponse;
import software.amazon.awssdk.services.lambda.model.LambdaException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;

@Service
public class MovieRatingHandler extends AbstractHandler<MovieRatingAppConfig> implements RequestHandler<Map<String,String>, String> {

  private static final Logger logger = Logger.getLogger(MovieRatingHandler.class.getName());
  private static final LambdaClient lambdaClient = LambdaClient.builder().build();

  @Autowired
  public MovieRepository movieRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ReviewRepository reviewRepository;

  @Override
  public String handleRequest(Map<String,String> event, Context context) {

    LambdaLogger logger = context.getLogger();
    logger.log("Handler invoked");

    GetAccountSettingsResponse response = null;
    try {
      response = lambdaClient.getAccountSettings();
    } catch(LambdaException e) {
      logger.log(e.getMessage());
    }
    return response != null ? "Total code size for your account is " + response.accountLimit().totalCodeSize() + " bytes" : "Error";
  }

  /**
   *
   * @param event contains movie data, e.g. title, year, etc.
   * @param context
   * @return
   */
  public String handleAddMovieRequest(Map<String,String> event, Context context) {
    String msg = "addMovie invoked with: " + event;
    LambdaLogger logger = context.getLogger();
    logger.log(msg);


    Movie movie = new Movie();
    movie.title = event.get("title");
    movie.year  = Integer.parseInt(event.get("year"));
    movie.genre = event.get("genre");
    movie.director = event.get("director");
    movie.averageRating = 0;

    getMovieRepository().save(movie);
    logger.log("Added movie: " + event);

    return msg;
  }

  public String handleAddMovieRatingRequest(Map<String,String> event, Context context) {
    String msg = "addMovieRating with: " + event;
    LambdaLogger logger = context.getLogger();
    logger.log(msg);


    Review review = new Review();
    review.title = event.get("title");
    review.opinion = event.get("opinion");
    review.rating  = Integer.parseInt(event.get("rating"));
    review.createdAt = new Date();
    review.movie = getMovieRepository().findByTitle(review.title);
    review.user = getUserRepository().findByName(event.get("author"));;

    getReviewRepository().save(review);

    logger.log("Added movieRating: " + event);

    return msg;
  }

  public String handleCalculateAverageMovieRatingsRequest(Map<String,String> event, Context context) {
    String msg = "calculateAverageRatings invoked";
    LambdaLogger logger = context.getLogger();
    logger.log(msg);

    List<Review> allReviews = getReviewRepository().findAll();
    Map<Long, List<Review>> ratingsByMovie = allReviews.stream().collect(groupingBy( it -> it.id));

    for (Map.Entry<Long, List<Review>> entry : ratingsByMovie.entrySet()) {
      Movie movie = getMovieRepository().findById(entry.getKey()).orElse(null);
      if (movie == null) {
        logger.log("Movie not found: " + entry.getKey());
        continue;
      }

      movie.averageRating = entry.getValue().stream().collect(averagingInt(it -> it.rating)).intValue();

      getMovieRepository().save(movie);

      logger.log("Average rating calculated and stored for movie: " + event);

    }

    return msg;
  }

  public List<Movie> handleSearchMoviesRequest(Map<String,String> event, Context context) {
    String msg = "searchMovies invoked with: " + event;
    LambdaLogger logger = context.getLogger();
    logger.log(msg);

    String searchTitle = event.get("title");
    List<Movie> movieList = searchTitle != null ?
        List.of(getMovieRepository().findByTitle(searchTitle)) :
        getMovieRepository().findAll();

    logger.log("Returning ratings calculated: " + movieList);

    return movieList;
  }

  private MovieRepository getMovieRepository() {
    if (this.movieRepository == null) {
      this.movieRepository = getApplicationContext().getBean(MovieRepository.class);
    }

    return this.movieRepository;
  }

  private ReviewRepository getReviewRepository() {
    if (this.reviewRepository == null) {
      this.reviewRepository = getApplicationContext().getBean(ReviewRepository.class);
    }

    return this.reviewRepository;
  }

  private UserRepository getUserRepository() {
    if (this.userRepository == null) {
      this.userRepository = getApplicationContext().getBean(UserRepository.class);
    }

    return this.userRepository;
  }

}