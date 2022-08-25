package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Article;
import com.example.demo.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	// 게시물 번호 기준으로 해당 게시물에 달린 댓글을 전체 삭제할 수 있도록
	void deleteByArticleId(Long id);

	// 게시물 번호 기준으로 해당 게시물에 달린 댓글만 출력할 수 있도록
	List<Comment> findCommentByArticle(Article article);

}
