package com.seccert.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seccert.server.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}