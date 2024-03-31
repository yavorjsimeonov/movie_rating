package com.tues.softdev.movierating.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movie")
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  public Long id;

  public String title;

  public int year;

  public String genre;

  public String director;

  @Column(name = "average_rating")
  public int averageRating;

  @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Review> reviews = new HashSet<>();
}
