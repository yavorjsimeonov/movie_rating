package com.tues.softdev.movierating;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tues.softdev.movierating.model.Movie;
import com.tues.softdev.movierating.model.Review;
import com.tues.softdev.movierating.repository.ReviewRepository;
import com.tues.softdev.movierating.repository.UserRepository;
import com.tues.softdev.movierating.service.GoogleImageSearchService;
import com.tues.softdev.movierating.service.MovieService;
import com.tues.softdev.movierating.service.ReviewService;
import com.tues.softdev.movierating.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.GetAccountSettingsResponse;
import software.amazon.awssdk.services.lambda.model.LambdaException;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;

public class MovieRatingHandler /*extends AbstractHandler<MovieRatingAppConfig>*/ implements RequestHandler<Map<String,String>, String> {

  private static final Logger logger = Logger.getLogger(MovieRatingHandler.class.getName());
  private static final LambdaClient lambdaClient = LambdaClient.builder().build();

  private MovieService movieService;
  private UserService userService;
  private ReviewService reviewService;

  private GoogleImageSearchService imageSearchService = new GoogleImageSearchService();

  public MovieRatingHandler() {
    // This will load the Spring application
    ConfigurableApplicationContext ctx = SpringApplication.run(MovieRatingApplication.class, new String[] {});
    System.out.println("============= ctx.appName: " + ctx.getApplicationName() + "; ctx.getBeanDefinitionNames" + Arrays.asList(ctx.getBeanDefinitionNames()));
    // Initialize the dependencies using the spring context
    movieService = ctx.getBean(MovieService.class);
    reviewService = ctx.getBean(ReviewService.class);
    userService = ctx.getBean(UserService.class);
    //movieService = (MovieService) ctx.getAutowireCapableBeanFactory().autowire(MovieService.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
  }

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

    movieService.save(movie);
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
    review.movie = movieService.findByTitle(review.title);
    review.user = userService.findByName(event.get("author"));;

    reviewService.save(review);

    logger.log("Added movieRating: " + event);

    return msg;
  }

  public String handleCalculateAverageMovieRatingsRequest(Map<String,String> event, Context context) {
    String msg = "calculateAverageRatings invoked";
    LambdaLogger logger = context.getLogger();
    logger.log(msg);

    List<Review> allReviews = reviewService.findAll();
    Map<Long, List<Review>> ratingsByMovie = allReviews.stream().collect(groupingBy( it -> it.id));

    for (Map.Entry<Long, List<Review>> entry : ratingsByMovie.entrySet()) {
      Movie movie = movieService.findById(entry.getKey()).orElse(null);
      if (movie == null) {
        logger.log("Movie not found: " + entry.getKey());
        continue;
      }

      movie.averageRating = entry.getValue().stream().collect(averagingInt(it -> it.rating)).intValue();

      movieService.save(movie);

      logger.log("Average rating calculated and stored for movie: " + event);

    }

    return msg;
  }

  public List<Movie> handleSearchMoviesRequest(Map<String,String> event, Context context) {
    String msg = "searchMovies invoked with: " + event;
    LambdaLogger logger = context.getLogger();
    logger.log(msg);

    String searchTitle = event.get("title");
    List<Movie> movieList = new ArrayList<>();
    if (searchTitle != null) {
      Movie movie = movieService.findByTitle(searchTitle);
      movieList = movie != null ? List.of(movie) : new ArrayList<>();
    } else {
      movieList = movieService.findAll();
    }

    movieList = movieList.stream()
        .peek(movie -> {
          String imageUrl = imageSearchService.fetchFirstImageUrl(movie.title);
          movie.imageUrl = imageUrl;
        })
        .collect(Collectors.toList());

    logger.log("Returning ratings calculated: " + movieList);

    return movieList;
  }

}