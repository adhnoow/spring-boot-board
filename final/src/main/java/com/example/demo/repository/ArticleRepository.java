package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	// Article의 boardNum 기준으로 해당 게시판에만 게시물 출력하는 메소드
	List<Article> findByBoardName(int boardName);

	// home에서 게시판을 출력하기 위해 게시판 리스트를 찾는 메소드
	// nativeQuery = true → SQL
	@Query(value = "SELECT DISTINCT BOARD_NAME FROM TABLE_ARTICLE", nativeQuery = true)
	List<String> findAllBoard();

	// 게시글 검색 기능
//	@Query(value = "SELECT * FROM TABLE_ARTICLE WEHRE TITLE LIKE", nativeQuery = true)
	Page<Article> findAllByTitleContainsIgnoreCaseOrContentContainsIgnoreCaseAndBoardName(String keyword1,
			String keyword2, String boardName, Pageable pageble);

	Page<Article> findByBoardName(String boardName, Pageable pageble);

}