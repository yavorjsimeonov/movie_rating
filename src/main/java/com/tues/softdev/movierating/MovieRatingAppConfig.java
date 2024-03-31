package com.tues.softdev.movierating;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.tues.softdev.movierating", "com.tues.softdev.movierating.repository"})
public class MovieRatingAppConfig {

}
