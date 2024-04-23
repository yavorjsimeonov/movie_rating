package com.tues.softdev.movierating;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@ComponentScan("com.tues.softdev.movierating")
public class MovieRatingAppConfig {

}
