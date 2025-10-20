package com.mycompany.mini2.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface MovieReviewRepository extends JpaRepository<MovieReview, Long>{
	
	List<MovieReview> findByMovie(Movie movie);

}
