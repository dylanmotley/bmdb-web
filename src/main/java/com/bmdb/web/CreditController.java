package com.bmdb.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bmdb.business.Credit;
import com.bmdb.db.CreditRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/credits")
public class CreditController {

	@Autowired
	private CreditRepo creditRepo;

	@GetMapping("/")
	public Iterable<Credit> getAll() {
		return creditRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Credit> get(@PathVariable Integer id) {
		return creditRepo.findById(id);
	}

	@PostMapping("/")
	public Credit add(@RequestBody Credit credit) {
		return creditRepo.save(credit);
	}

	@PutMapping("/")
	public Credit update(@RequestBody Credit credit) {
		return creditRepo.save(credit);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		creditRepo.deleteById(id);
	}
	
	// Custom Queries 
	@GetMapping("/movie/{id}")
	public Iterable<Credit> getAllByMovie(@PathVariable int id) {
		// Optional<Movie> movie = movieRepo.findById(id);
		return creditRepo.findAllByMovieId(id);
	}
	
	@GetMapping("/actor/{id}")
	public Iterable<Credit> getAllByActor(@PathVariable int id) {
		// Optional<Movie> movie = movieRepo.findById(id);
		return creditRepo.findAllByActorId(id);
	}

}
