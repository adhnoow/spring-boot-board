package com.example.demo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@Table(name = "TABLE_ARTICLE")
public class Article {
	@Id
	@Column(name = "ARTICLE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String boardName; // 해당하는 게시판 번호

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private Date wdate; // 글 작성일

	@Column(nullable = false)
	@PrePersist // 글이 save(insert)되는 시점의 시간
	public void beforeCreate() {
		wdate = new Date();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@NonNull
	@JsonIgnore // updateDone() 메소드
	private User user; // 외래키: 글 쓴 사용자 식별

	public Article(Long id, String boardName, String title, String content, Date wdate, @NonNull User user) {
		super();
		this.id = id;
		this.boardName = boardName;
		this.title = title;
		this.content = content;
		this.wdate = wdate;
		this.user = user;
	}

}
