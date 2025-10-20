package com.mycompany.mini2.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.mycompany.mini2.board.Board;


public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findByBoard(Board board);

}
