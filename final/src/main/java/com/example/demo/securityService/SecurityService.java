package com.example.demo.securityService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.repository.CommentRepository;
import com.example.demo.service.ArticleService;

public class SecurityService {

	private CommentRepository commentRepository;
	private ArticleService articleService;

	@Transactional
	@DeleteMapping("/article/delete/{id}")
	// board를 return할 필요 없을 때 <?>
	public void deleteArticle(@PathVariable long id) {
		System.out.println("delete 호출됨");
		commentRepository.deleteByArticleId((long) 61);
		articleService.deleteArticleById((long) 61);
	}
}
