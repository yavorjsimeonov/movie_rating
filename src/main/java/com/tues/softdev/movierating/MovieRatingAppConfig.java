package com.tues.softdev.movierating;

import com.tues.softdev.movierating.service.MovieService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.tues.softdev.movierating.repository")
@ComponentScan("com.tues.softdev.movierating")
@Import(MovieService.class)
public class MovieRatingAppConfig {

}
