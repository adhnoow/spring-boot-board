package com.example.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	private CommentRepository commentRepository;

	public CommentServiceImpl(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	@Override
	public void saveComment(Comment comment) {
		commentRepository.save(comment);
	}

	@Override
	public Comment findCommentById(Long id) {
		return commentRepository.findById(id).orElse(new Comment());
	}

	@Override
	public Page<Comment> findCommentList(Pageable pageble) {
		pageble = PageRequest.of(pageble.getPageNumber() <= 0 ? 0 : pageble.getPageNumber() - 1, pageble.getPageSize(),
				pageble.getSort());
		return commentRepository.findAll(pageble);
	}

	@Override
	public void deleteCommentById(Long id) {
		commentRepository.deleteById(id);
	}

//	@Override
//	public List<Comment> findCommentByArticle(Article article) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	/*
	 * @Override public List<Comment> findCommentList(List<Comment> comments) {
	 * pageble = PageRequest.of(pageble.getPageNumber() <= 0 ? 0 :
	 * pageble.getPageNumber() - 1, pageble.getPageSize(), pageble.getSort());
	 * return commentRepository.findAll(pageble); }
	 */
}
