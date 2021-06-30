package com.bmdb.db;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bmdb.business.Actor;

public interface ActorRepo extends CrudRepository<Actor, Integer> {

	Optional<Actor> findByFirstNameAndLastName(String firstName, String lastName);
}
