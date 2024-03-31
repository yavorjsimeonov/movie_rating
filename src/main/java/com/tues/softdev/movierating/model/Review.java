package com.tues.softdev.movierating.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  public Long id;

  public String title;

  public String opinion;

  public int rating;

  @Column(name = "created_at")
  public Date createdAt;

  @Column(name = "review_author_name")
  public String reviewAuthorName;

  @ManyToOne
  @JoinColumn(name = "movie_id", referencedColumnName = "id")
  public Movie movie;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  public User user;


}
