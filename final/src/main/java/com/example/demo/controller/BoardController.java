package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Article;
import com.example.demo.model.Comment;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ArticleService;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;

@Controller
public class BoardController {

	// Sercurity session Authentication

	/*
	 * 1. 관리자(Admin) 2. 매니저(Manager) 3. 회원(User) 4. 손님(not logined)
	 */

	@Autowired
	private ArticleService articleService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired // Config에서 Bean으로 띄워서 import 가능
	private BCryptPasswordEncoder bCryptPasswordEncoder; // 비밀번호 암호화를 위해

	// 기본 페이지를 login 말고 게시판 리스트 출력
	@RequestMapping({ "", "/" }) // {, }: 다중 Mapping
	public String home(Model model) {
//		Set<String> boardNum = new HashSet<String>(articleService.findAllBoard4());
//		System.out.println("요거요거 ===>  " + boardNum);
//		model.addAttribute("boardNum", boardNum);
		System.out.println("---> " + articleRepository.findAllBoard());
		model.addAttribute("boardList", articleRepository.findAllBoard());
		return "home";
	}

	// security "/login": spring security가 가로챔
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}

	@GetMapping("/joinForm")
	public String join() {
		return "joinForm";
	}

	@PostMapping("/join")
	public ResponseEntity<String> join(@RequestBody User user, Authentication authentication) {
		// System.out.println(user.toString());
		String message = "Welcome, you just joined!";// view 단에서 출력할 메시지 기본값 세팅
		List<User> userList = userService.findAllUserList();

		for (User existUser : userList) { // 향상된 for문 사용
			// 해당하는 값이 이미 존재하면 메시지를 수정함
			if (user.getNickname().equals(existUser.getNickname())) {
				System.out.println("일치하는 닉네임 있음");
				message = "Name already taken please please try another one :D";
				break;
			} else if (user.getEmail().equals(existUser.getEmail())) {
				System.out.println("일치하는 이메일 있음");
				message = "Email address not available please try another one!";
				break;
			} else if (user.getUsername().equals(existUser.getUsername())) {
				System.out.println(user.getUsername() + "    " + existUser.getUsername());
				System.out.println("일치하는 아이디 있음: " + user.toString());
				message = "ID already exists :(";
				break;
			}
		}
		if (message.equals("Welcome, you just joined!")) {
			user.setRole(Role.ROLE_USER);
			// @Autowired 해서 encode: password가 암호화 됨
			String encPassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encPassword);
			userService.saveUser(user);
		}
		System.out.println("최종 결과   " + message);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// 유저 관리(매니저 권한 여부, 회원삭제) 창
	@PreAuthorize("hasRole('ROLE_ADMIN')") // 관리자만 접근 가능한 페이지
	@GetMapping("/manageUser")
	public String manageUser(Model model,
			@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageble) {
		model.addAttribute("userList", userService.findUserList(pageble));
		return "manageUser";
	}

	// 매니저 권한 부여, 박탈 메소드
	@PutMapping("/manageUser/{action}-manager/{id}")
	public ResponseEntity<String> addAndRemoveManager(@PathVariable String action, @PathVariable long id) {
		String message = "";
		System.out.println(action);
		if (action.equals("add")) {
			User user = userService.findUserById(id);
			user.setRole(Role.ROLE_MANAGER);
			userService.saveUser(user);
			message = "Successfully give manager permission to ";
		} else if (action.equals("remove")) {
			User user = userService.findUserById(id);
			user.setRole(Role.ROLE_USER);
			userService.saveUser(user);
			message = "Successfully give user permission to ";
		}
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// 게시판(생성, 접근범위 수정, 삭제) 관리를 위한 메소드
	@PreAuthorize("hasRole('ROLE_ADMIN')") // 관리자만 접근 가능한 페이지
	@GetMapping("/manageBoard")
	public String manageBoard(Model model,
			@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageble) {
//		model.addAttribute("boardList", articleService.findBoardList(pageble));
		return "manageBoard";
	}

	@PostMapping("/change-{action}")
	public ResponseEntity<String> changeUserInfo(@PathVariable String action, Model model) {
		if (action.equals("username")) {
			System.out.println("changeUsername()");
//			userService.findUserById(id)
		} else if (action.equals("id")) {
			System.out.println("changeId()");
		} else if (action.equals("password")) {
			System.out.println("changePwd()");
		} else if (action.equals("email")) {
			System.out.println("email()");
		}
		return new ResponseEntity<>("Hello World!", HttpStatus.OK);
	}

	@GetMapping("/{boardName}/list")
	public String list(@PathVariable String boardName, Model model,
			@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageble) {
		System.out.println("리스트 오냐");
		// 게시판 이름 받아놓기
		model.addAttribute("boardName", boardName);
		// 해당하는 게시판의 게시물 리스트를 뽑아야 함
		model.addAttribute("articleList", articleService.findAllbyBoardName(boardName, pageble));
		System.out.println(articleService.findAllbyBoardName(boardName, pageble));
		return "board/list";
	}

	@PostMapping("/{boardName}/create-post")
	public String form(@PathVariable String boardName, Model model) {
		System.out.println("호출 됨");
		model.addAttribute("boardName", boardName);
		return "board/form";
	}

	@PostMapping("/article/insert")
	public ResponseEntity<Article> insertArticle(@RequestBody Article article, Principal principal) {
		// 게시글을 저장할 때 현재 로그인한 유저의 id(username)값이 들어가야 됨
		// query(save)문 실행시 함께 저장하면 되므로 insert method()의 인자로 찾는 방법을 택함
		// Spring Security의 최상위 인터페이스: Principal
		article.setUser(userRepository.findByUsername(principal.getName()));
		System.out.println("article.toString() =>   " + article.toString());
		articleService.saveArticle(article);
		System.out.println("!!!!!!! " + article.getUser().getUsername());
		System.out.println("article.toString() =>" + article.toString());
		return new ResponseEntity<Article>(article, HttpStatus.CREATED);
	}

	@RequestMapping("/{boardName}/article")
	public String article(@PathVariable String boardName, Model model,
			@RequestParam(value = "no", defaultValue = "0") long id) {
		// System.out.println("임시테스트페이지"+id);

		// 게시글 구현
		Article article = articleService.findArticleById(id);
		System.out.println(article.toString());
		model.addAttribute("article", article);

		// 댓글 구현
		// article 번호에 해당하는 comment를 출력해야 함
		model.addAttribute("commentList", commentRepository.findCommentByArticle(article));
		System.out.println("메시지 리스트 설정 완 : " + (commentRepository.findCommentByArticle(article)));
		return "board/article";
	}

	@RequestMapping("/{boardName}/search")
	public String articleSearch(@PathVariable String boardName, String searchKeyword, Model model,
			@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageble) {
		System.out.println("제발 넘어 와라");
		model.addAttribute("searchKeyword", searchKeyword); // view단에 키워드도 출력함
		model.addAttribute("searchResult",
				articleRepository.findAllByTitleContainsIgnoreCaseOrContentContainsIgnoreCaseAndBoardName(searchKeyword,
						searchKeyword, boardName, pageble));
		System.out.println(articleRepository.findAllByTitleContainsIgnoreCaseOrContentContainsIgnoreCaseAndBoardName(
				searchKeyword, searchKeyword, boardName, pageble));
		return "searchResultForm";
	}

//	@PostMapping("/article/result")
//	public String articleResult(String searchKeyword, Model model) {
//		System.out.println("호출은 됨");
//		model.addAttribute("searchKeyword", searchKeyword); // view단에 키워드도 출력함
//		model.addAttribute("searchResult", articleRepository
//				.findAllByTitleContainsIgnoreCaseOrContentContainsIgnoreCase(searchKeyword, searchKeyword));
//		return "searchResult";
//	}

	// updateForm으로 넘겨주는 method
	// ajax를 사용하기 때문에 중간 매개 메소드가 필요했음
	@PostMapping("/article/update/{id}")
	public String updateArticleBtn(@PathVariable long id, Model model) {
		System.out.println("============> updateArticleBtn() 호출");
		System.out.println("=================> 게시글 번호: " + id);
		Article article = articleService.findArticleById(id);
		System.out.println(article.toString());
		model.addAttribute("article", article);
		return "board/updateArticleForm";
	}

	// 게시글 update query문을 실행하는 method
	@Transactional
	@PutMapping("/article/update/{id}")
	public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody Article article) {
		System.out.println("======> updateArticle 여기까진 옴");
		System.out.println("======>" + article.toString());
		// Article(model)에서 notnull 객체는 게시물 작성시에는 null로 들어갈 수 있으나 list에서 출력할 때 오류가 남
		// 따라서 wdate와 user 값이 ajax에서 null로 넘어왔기 때문에 id값을 통해 setting 해줌
		// null로 넘어온 이유는 date 타입을 parsing하는 데 문제가 생겨 일부러 넣지 않음(article.html)
		Article editArticle = articleService.findArticleById(id);
		editArticle.setTitle(article.getTitle());
		editArticle.setContent(article.getContent());
		System.out.println("======>" + editArticle.toString());
		articleService.saveArticle(editArticle);
		return new ResponseEntity<Article>(editArticle, HttpStatus.OK);
	}

	// 게시글 delete query문을 실행하는 method
	@Transactional
	@DeleteMapping("/article/delete/{id}")
	// board를 return할 필요 없을 때 <?>
	public ResponseEntity<?> deleteArticle(@PathVariable long id) {
		System.out.println("delete 호출됨");
		try {
			// 외래키 설정 때문에 해당 게시글의 댓글을 먼저 삭제하고 게시글을 삭제할 수 있음
			commentRepository.deleteByArticleId(id);
			System.out.println("게시글 번호에 해당하는 댓글만 삭제가 됐는지 확인해라"); // 됨
			articleService.deleteArticleById(id);
		} catch (Exception e) {
			System.out.println("error 발생" + e);
		}
		return new ResponseEntity<>("{}", HttpStatus.OK);
	}

	// 폼에서 받아온 값으로 댓글 등록 query문을 실행하는 method
	@PostMapping("/comment/insert")
	public String insertComment(String reqComment, long article, Principal principal) {
		Comment comment = new Comment();
		comment.setComment(reqComment);

		// 위의 insertArticle()과 같은 방법으로 user 값을 세팅함
		comment.setUser(userRepository.findByUsername(principal.getName()));
		// form에서 가져온 아이디 값을 통해서 article 전체를 찾아 세팅함
		comment.setArticle(articleService.findArticleById(article));
		commentService.saveComment(comment);
		// System.out.println("!!!!!!! 유저 아이디: " + comment.getUser().getUsername());
		// System.out.println("comment.toString() =>" + comment.toString());

		String boardName = comment.getArticle().getBoardName();
		return "redirect:/" + boardName + "/article?no=" + article; // 수정사항: 댓글 단 후에 뒤로가기 누르면 이전 페이지 나옴
	}

	// comment를 남기고 목록보기 버튼 대신 뒤로가기를 눌렀을 때 list로 리턴하도록 하는 메소드
	@GetMapping("/comment/insert")
	public String insertComment() {
		return "board/list"; // 수정사항: 댓글 단 후에 뒤로가기 누르면 이전 페이지 나옴
	}

	// updateForm으로 넘겨주는 method
	// ajax를 사용하기 때문에 중간 매개 메소드가 필요했음
	@PostMapping("/comment/update/{id}")
	public String updateCommentBtn(@PathVariable long id, Model model) {
		System.out.println("============> updateCommentBtn() 호출");
		System.out.println("=================> 게시글 번호: " + id);
		Comment comment = commentService.findCommentById(id);
		System.out.println("=================> toString: " + comment.toString());
		model.addAttribute("comment", comment);
		return "board/updateCommentForm";
	}

	// 댓글 update query문을 실행하는 method
	@Transactional
	@PutMapping("/comment/update/{id}")
	public ResponseEntity<Comment> updateComment(@PathVariable long id, @RequestBody Comment comment) {
		System.out.println("======> updateComment 여기까진 옴");
		System.out.println("======> setting 전: " + comment.toString());
		// 아이디를 통해서 수정 전 댓글을 가져옴
		Comment editComment = commentService.findCommentById(id);
		editComment.setComment(comment.getComment());

		System.out.println("======> setting 후: " + editComment.toString());
		commentService.saveComment(editComment);
		return new ResponseEntity<Comment>(editComment, HttpStatus.OK);
	}

	// 댓글 delete query문을 실행하는 method
	@Transactional
	@DeleteMapping("/comment/delete/{id}")
	// board를 return할 필요 없을 때 <?>
	public ResponseEntity<?> deleteComment(@PathVariable long id) {
		System.out.println("===========> delete 호출됨");
		try {
			commentService.deleteCommentById(id);
		} catch (Exception e) {
			System.out.println("error 발생" + e);
		}
		return new ResponseEntity<>("{}", HttpStatus.OK);
	}

	// BoardSercurityConfig에서 모든 사용자가 접근할 수 있도록 설정
	@RequestMapping("/user")
	public String user() {
		return "user";
	}

	// BoardSercurityConfig에서 매니저 이상만 접근할 수 있도록 설정
	@RequestMapping("/manager")
	public String manager() {
		return "manager";
	}

	// BoardSercurityConfig에서 관리자만 접근할 수 있도록 설정
	@RequestMapping("/admin")
	public String admin() {
		return "admin";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
	@RequestMapping("/data")
	public @ResponseBody String data() {
		return "data infomation";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')") // 해당 role만 들어갈 수 있음
	@RequestMapping("/info")
	public @ResponseBody String info(User user) {
		return "private infomation";
	}
}