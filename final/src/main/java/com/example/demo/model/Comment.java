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
@Table(name = "TABLE_COMMENT")
public class Comment {
	@Id
	@Column(name = "COMMENT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String comment;

	@Column(nullable = false)
	private Date wdate; // 댓글 작성일

	@Column(nullable = false)
	@PrePersist // 댓글이 save(insert)되는 시점의 시간
	public void beforeCreate() {
		wdate = new Date();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@JsonIgnore
	@NonNull
	private User user; // 외래키: 댓글 쓴 사용자 식별

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTICLE_ID")
	@JsonIgnore
	@NonNull
	private Article article; // 외래키: 댓글 쓴 게시물 번호 식별

	public Comment(Long id, String comment, Date wdate, @NonNull User user) {
		super();
		this.id = id;
		this.comment = comment;
		this.wdate = wdate;
		this.user = user;
	}

}
