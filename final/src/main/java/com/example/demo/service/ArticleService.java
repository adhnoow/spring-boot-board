package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.Article;

public interface ArticleService { // ServiceImpl class가 상속받기 위한 interface
	void saveArticle(Article article);

	Article findArticleById(Long id);

	Page<Article> findArticleList(Pageable pageble);

	Page<Article> findAllbyBoardName(String boardName, Pageable pageble);

	void deleteArticleById(Long id);
	
	Page<Article> findAllbyTitleOrContentAndBoardName(String keyword1, String keyword2, String boardName, Pageable pageble);

	// 관리자가 게시판을 관리할 때 필요함
	Page<Article> findBoardList(Pageable pageble);
}