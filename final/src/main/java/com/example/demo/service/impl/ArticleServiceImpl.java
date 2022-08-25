package com.example.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.service.ArticleService;

@Service // AtricleService를 상속받은 Impl class가 서비스 클래스
public class ArticleServiceImpl implements ArticleService {
	private ArticleRepository articleRepository;

	public ArticleServiceImpl(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	@Override
	public void saveArticle(Article article) {
		articleRepository.save(article);
	}

	@Override
	public Article findArticleById(Long id) {
		return articleRepository.findById(id).orElse(new Article());
	}

	@Override
	public Page<Article> findArticleList(Pageable pageble) {
		pageble = PageRequest.of(pageble.getPageNumber() <= 0 ? 0 : pageble.getPageNumber() - 1, pageble.getPageSize(),
				pageble.getSort());
		return articleRepository.findAll(pageble);
	}

	@Override
	public void deleteArticleById(Long id) {
		articleRepository.deleteById(id);

	}

	@Override
	public Page<Article> findAllbyBoardName(String boardName, Pageable pageble) {
		pageble = PageRequest.of(pageble.getPageNumber() <= 0 ? 0 : pageble.getPageNumber() - 1, pageble.getPageSize(),
				pageble.getSort());
		return articleRepository.findByBoardName(boardName, pageble);
	}

	@Override
	public Page<Article> findBoardList(Pageable pageble) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Article> findAllbyTitleOrContentAndBoardName(String keyword1, String keyword2, String boardName,
			Pageable pageble) {
		pageble = PageRequest.of(pageble.getPageNumber() <= 0 ? 0 : pageble.getPageNumber() - 1, pageble.getPageSize(),
				pageble.getSort());
		return articleRepository.findAllByTitleContainsIgnoreCaseOrContentContainsIgnoreCaseAndBoardName(keyword1,
				keyword2, boardName, pageble);
	}

}
