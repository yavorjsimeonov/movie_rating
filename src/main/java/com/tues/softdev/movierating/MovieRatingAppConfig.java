package com.tues.softdev.movierating;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.tues.softdev.movierating.repository")
@ComponentScan("com.tues.softdev.movierating")
public class MovieRatingAppConfig {

}
