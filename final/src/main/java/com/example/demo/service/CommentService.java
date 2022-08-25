package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.Comment;

public interface CommentService {
	void saveComment(Comment Comment);

	Comment findCommentById(Long id);

	Page<Comment> findCommentList(Pageable pageble);

	void deleteCommentById(Long id);

//	List<Comment> findCommentByArticle(Article article);

}
