package com.mycompany.mini2.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface MovieCommentRepository extends JpaRepository<MovieComment, Long>{
	
	List<MovieComment> findByMovie(Movie movie);

}
