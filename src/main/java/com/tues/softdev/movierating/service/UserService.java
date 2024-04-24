package com.tues.softdev.movierating.service;

import com.tues.softdev.movierating.model.User;
import com.tues.softdev.movierating.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  public User findByName(String name) {
    return userRepository.findByName(name);
  }
}
