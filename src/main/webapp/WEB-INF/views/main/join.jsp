<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%
	// 로그인 되었을 때 해당 유저의 id가 저장될 변수
	String user_id= null;
	//url을 입력해서 페이지로 넘어올수 있어 세션처리하여 보안을 높임
	// 세션에 "user_id" 이름을 가지는 value가 존재하면 
	// user_id 변수에 해당 value를 저장 
	// 로그인 상태 확인 -> !=null 이면 로그인 되어있는 상태임
	if(session.getAttribute("user_id") != null) {
		// PrintWriter : 페이지에 스크립트문 작성할 때 사용
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('이미 로그인 되있습니다.');");
		script.println("location.href = 'main.jsp';");
		script.println("</script>");
	}
%>
<!DOCTYPE html>
<html>
<head>
<title>JSP 게시판</title>
<meta http-equiv="Conetent-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width" initial-scale="1">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/custom.css">
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

<script src="//ajax.googleapis.com/ajax/libs/jquery.min.js" charset="UTF-8"></script>
<script src="<%= request.getContextPath() %>/js/address.js" charset="UTF-8"></script>

<script>
// document (HTML 문서)가 로드가 완료되었을 때 실행
$(document).ready(function () {
	
	var id_check_flag = false;
	var pw_check_flag = false;
	var em_check_flag = false;
	
	// user_pw2 id를 가지는 HTML 태그가 변경되었을 때 실행
	$('#user_pw2').change(function() {
		// 각각의 id를 가지는 HTML 태그에서 value 값을 가져옴
		var user_pw  = $('#user_pw').val();
		var user_pw2 = $('#user_pw2').val();
		if (user_pw != user_pw2) {
			// 해당 id를 가지는 HTML 태그를 보여줌
			$('#diff_pw').show();
			// 해당 id를 가지는 HTML 태그를 숨김
			$('#equal_pw').hide();
			pw_check_flag = false;
		}
		else {
			$('#equal_pw').show();
			$('#diff_pw').hide();
			pw_check_flag = true;
		}
	});
	
	// id_check id를 가지는 HTML 태그가 클릭되었을 때 실행
	$('#id_check').click(function() {
		var user_id = $('#user_id').val();
		
		if(user_id == '') {
			alert('아이디를 입력하세요.');
			return;
		}
		
		// 비동기통신 AJAX 진행
		$.ajax({
			// 전송부
			type : 'GET',		// 전송방식
			url  : './idCheck',	// 전송할 url
			data : {			// 전송하는 데이터
				// parameter name : parameter value
				user_id : user_id
			},
			// 송신부
			dataType : 'text',	// 송신받는 데이터의 타입
			success : function(data) {	// 통신에 성공했을 때
				// 사용가능 가능 아이디
				if(data == 0) {
					alert('사용가능한 아이디입니다.');
					$('#user_id').attr('readonly', true);
					id_check_flag = true;
				}
				// 사용중인 아이디
				else if(data == 1) {
					alert('사용중인 아이디입니다.');
					$('#user_id').attr('value', '');
				}
				// 데이터베이스 오류
				else {
					alert('데이터베이스 오류가 발생했습니다.');
				}
			},
			error : function() {	// 통신에 실패했을 때
				alert('중복체크 실패');
			}
		});
	});
	
	$('#getAddrBtn').click(function(){
		var postCode  = $('#entry_postcode5').val();
		var doroAddr  = $('#entry_address').val();
		var jibunAddr = $('#entry_extra_info').val();
		
		$('#addr1').val(postCode);
		$('#addr2').val(doroAddr + ' ' + jibunAddr);
	});
	
	// sendAuthBtn를 가지는 HTML 태그가 클릭되었을 때 실행
	$('#sendAuthBtn').click(function() {
		var user_mail = $('#user_mail_modal').val();
		
		if(user_mail == '') {
			alert('이메일을 입력하세요.');
			return;
		}
		
		// 비동기통신 AJAX 진행
		$.ajax({
			// 전송부
			type : 'GET',				// 전송방식
			url  : './sendAuthMail',	// 전송할 url
			data : {					// 전송하는 데이터
				// parameter name : parameter value
				user_mail : user_mail
			},
			// 송신부
			dataType : 'text',	// 송신받는 데이터의 타입
			success : function(data) {	// 통신에 성공했을 때
				// 메일 전송 성공
				if(data == 0) {
					alert('인증 메일을 전송했습니다.');
					$('#authDiv').show();
					$('#user_mail_modal').attr('readonly', 'true');
					$('#sendAuthBtn').attr('class', 'btn btn-warning btn-block');
					$('#sendAuthBtn').text('인증번호 재전송');
				}
				// 메일 전송 실패
				else if(data == -1) {
					alert('다시 메일주소를 입력하세요.');
					$('#user_mail_modal').val('');
				}
			}
		});
	});
	
	// authMailBtn를 가지는 HTML 태그가 클릭되었을 때 실행
	$('#authMailBtn').click(function(){
		var user_mail = $('#user_mail_modal').val();
		var auth_num  = $('#auth_num').val();
		
		$.ajax({
			type : 'POST',
			url  : './mailAuth',
			data : {
				user_mail : user_mail,
				auth_num  : auth_num
			},
			dataType : 'text',
			success : function(data) {
				if(data == 0) {
					alert('인증되었습니다.');
					$('#user_email').val(user_mail);
					$('#emailAuthModal').modal('hide');
					em_check_flag = true;
				}
				else if(data == 1) {
					alert('인증번호가 다릅니다.');
					$('#auth_num').val('');
				}
				else if(data == -1) {
					alert('데이터베이스 오류가 발생했습니다.');
					$('#auth_num').val('');
				}
			}
		});
		
	});
	
	// form 태그가 submit 이벤트를 발생시켰을 때
	$('form').submit(function(event) {
		var user_id 	= $('#user_id').val();
		var user_pw 	= $('#user_pw').val();
		var user_pw2 	= $('#user_pw2').val();
		var user_name 	= $('#user_name').val();
		var user_gender = $('#user_gender').val();
		var user_email 	= $('#user_email').val();
		var user_addr1 	= $('#user_addr1').val();
		var user_addr2 	= $('#user_addr2').val();
		var user_addr3 	= $('#user_addr3').val();
		
		if(user_id == "" || user_pw == "" || user_pw2 == "" ||
				user_name == "" || user_gender == "" || user_email == "" ||
				user_addr1 == "" || user_addr2 == "" || user_addr3 == "") {
			alert('모든 값을 입력하세요.');
			event.preventDefault();
			return;
		}
		
		if(!id_check_flag) { 
			alert('아이디 중복체크를 해주세요.');
			event.preventDefault();
			return;
		}
		if(!pw_check_flag) { 
			alert('비밀번호를 확인 해주세요.');
			event.preventDefault();
			return;
		}
		if(!em_check_flag) { 
			alert('이메일 인증을 해주세요.');
			event.preventDefault();
			return;
		}
		
	});
	
	
// 마지막 괄호단//
});
</script>
</head>

<body>

<!-- 메인 네비게이션 -->
<!-- nav : 네비게이션 리스트 만들어주는 HTML 태그 -->
<!-- default 철자 확인잘하기 -->
<nav class="navbar navbar-default">

	<!-- 네비게이션 헤더 -->
	<!-- div : 웹 페이지에서 공간을 지정하는 HTML 태그 (가로 전체) -->
	<div class="navbar-header">
		<!-- button : 버튼을 만들어주는 HTML 태그 -->
		<button type="button" class="navbar-toggle collapsed" 
			data-toggle="collapse" data-target="#bs-navbar-collapse"
			aria-expanded="false">
			<!-- span : 웹 페이지에서 공간을 지정하는 HTML 태그 (컨텐츠 사이즈 가로) -->
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
		<!-- a : url을 이동시켜주는 HTML 태그 href="" 속성에 경로 지정 -->
		<a class="navbar-brand" href="<%= request.getContextPath() %>/home">JSP 게시판</a>
	</div>
	<!-- 네비게이션 헤더 종료 -->
	
	<!-- 네비게이션 메뉴 -->
	<div class="collapse navbar-collapse" id="bs-navbar-collapse">
		
		<!-- 메인 메뉴 -->
		<!-- ul : 순서가 없는 리스트를 만들어주는 HTML 태그 -->
		<ul class="nav navbar-nav">
			<!-- li : 리스트 하나의 요소를 만들어주는 HTML 태그 -->
			<li class="active"><a href="<%= request.getContextPath() %>/home">메인</a></li>
			<li><a href="<%= request.getContextPath() %>/bbs">게시판</a></li>
		</ul>
		<!-- 메인 메뉴 종료 -->
		
		<!-- 마이페이지 메뉴 -->
		<!-- 로그인이 되있지 않을 때 표시 -->
		<%
			if(user_id == null) {
		%>
		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown">
				<a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button"
					aria-haspopup="true" aria-expanded="false">
					마이페이지<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li><a href="<%= request.getContextPath() %>/home/login">로그인</a></li>
					<li><a href="<%= request.getContextPath() %>/home/join">회원가입</a></li>
				</ul>
			</li>
		</ul>
		<%
			} else {
		%>
		<!-- 로그인이 되었을 때 표시 -->
		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown">
				<a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button"
					aria-haspopup="true" aria-expanded="false">
					마이페이지<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li><a href="<%= request.getContextPath() %>/home/logout">로그아웃</a></li>
				</ul>
			</li>
		</ul>
		<%
			}
		%>
		<!-- 마이페이지 메뉴 종료 -->
		
	</div>
	<!-- 네비게이션 메뉴 종료 -->
	
</nav>
<!-- 메인 네비게이션 종료 -->

<!-- 메인 컨텐츠 -->
<div class="container">
	<div class="col-lg-3"></div>
	
	<div class="col-lg-6">
		<div class="jumbotron">
			<form method="POST" action="./joinAction">
				<h3 style="text-align: center;">회원가입</h3>
				
				<!-- 아이디 입력에 대한 공간 할당 -->
				<!-- bs : row - 공간을 12개로 분할 -->
				<div class="row">
				
					<!-- 아이디 입력 -->
					<div class="form-group col-sm-9 col-md-9 col-lg-9">
						<input type="text" class="form-control" id="user_id" autocomplete="off" name="user_id" placeholder="아이디">
					</div>
						
					<div class="col-sm-3 col-md-3 col-lg-3" style="margin-bottom: 15px;">
						<button type="button" class="btn btn-success btn-block" id="id_check">중복체크</button>
					</div>
				</div>
				
				<!-- 비밀번호 입력에 대한 공간 할당 -->
				<div class="row">
					<!-- 비밀번호 입력 -->
					<div class="form-group col-sm-12 col-md-12 col-lg-12">
						<input type="password" class="form-control" id="user_pw" name="user_pw" placeholder="비밀번호">
					</div>
				</div>
				
				<!-- 비밀번호 확인에 대한 공간 할당 -->
				<div class="row">
					<!-- 비밀번호 입력 -->
					<div class="form-group col-sm-12 col-md-12 col-lg-12">
						<input type="password" class="form-control" id="user_pw2" name="user_pw2" placeholder="비밀번호 확인">
					</div>
				</div>
				
				<!-- 비밀번호 체크에 대한 공간 할당 -->
				<div class="row">
					<div class="col-sm-12 col-md-12 col-lg-12">
						<h6 id="diff_pw" class="text-danger" style="display: none;">비밀번호가 서로 다릅니다.</h6>
						<h6 id="equal_pw" class="text-success" style="display: none;">확인되었습니다.</h6>
					</div>
				</div>
				
				<!-- 이름 입력에 대한 공간 할당 -->
				<div class="row">
					<!-- 이름 입력 -->
					<div class="form-group col-sm-12 col-md-12 col-lg-12">
						<input type="text" class="form-control" name="user_name" placeholder="이름">
					</div>
				</div>
				
				<!-- 성별 선택에 대한 공간 할당 -->
				<div class="row">
					<!-- 성별 선택 -->
					<div class="form-group" style="text-align: center;">
						<div class="btn-group" data-toggle="buttons">
							<!-- label : UI 요소에 라벨을 정의하는 HTML 태그 -->
							<label class="btn btn-primary active">
								<!--  autocomplete 속성 : 과거 기록을 dropdown 형식으로 보여줄지를 선택 - true : 보여줌 , false : 보여주지않음 -->
								<input type="radio" name="user_gender" autocomplete="off" value="m" checked>남자
							</label>
							<label class="btn btn-primary">
								<input type="radio" name="user_gender" autocomplete="off" value="w">여자
							</label>
						</div>
					</div>
				</div>
				
				<!-- 이메일 입력에 대한 공간 할당 -->
				<div class="row">
					<!-- 이메일 입력 -->
					<div class="form-group col-sm-9 col-md-9 col-lg-9">
						<input type="email" class="form-control" id="user_email" name="user_email" placeholder="이메일" readonly>
					</div>
					<!-- 인증 -->	
					<div class="col-sm-3 col-md-3 col-lg-3" style="margin-bottom: 15px;">
						<button type="button" class="btn btn-success btn-block" 
							data-toggle="modal" data-target="#emailAuthModal">인증</button>
					</div>
				</div>
				
				<!-- 주소 입력에대한 공간 할당 -->
				<div class="row">
					<!-- 우편번호 입력 -->
					<div class="form-group col-sm-3 col-md-3 col-lg-3">
						<input type="text" class="form-control" id="addr1" name="addr1" placeholder="우편번호" readonly>
					</div>
					<!-- 주소검색 -->	
					<div class="col-sm-3 col-md-3 col-lg-3" style="margin-bottom: 15px;">
						<button type="button" class="btn btn-success btn-block"
							data-toggle="modal" data-target="#addrModal">주소검색</button>
					</div>
				</div>
				<div class="row">
					<!-- 주소 입력 -->
					<div class="form-group col-sm-12 col-md-12 col-lg-12">
						<input type="text" class="form-control" id="addr2" name="addr2" placeholder="주소" readonly>
					</div>
				</div>
				<!-- 상세주소 입력 -->
				<div class="row">
					<div class="form-group col-sm-12 col-md-12 col-lg-12">
						<input type="text" class="form-control" name="addr3" placeholder="상세주소">
					</div>
				</div>
				
				<!-- 제출 버튼 -->
				<div class="form-group">
					<input type="submit" class="btn btn-primary form-control" value="회원가입">
				</div>
				
			</form>
		</div>
	</div>
	
	<div class="col-lg-3"></div>
</div>
<!-- 메인 컨텐츠 종료 -->

<!-- 이메일 인증 모달 -->
<div id="emailAuthModal" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">이메일 인증</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-lg-8">
						<input type="email" class="form-control" id="user_mail_modal" placeholder="이메일">
					</div>
					<div class="col-lg-4">
						<button type="button" class="btn btn-success btn-block" id="sendAuthBtn">인증번호 전송</button>
					</div>
				</div>
				<div class="row" id="authDiv" style="margin-top: 15px; display: none;">
					<div class="col-lg-4">
						<input type="text" class="form-control" id="auth_num" placeholder="인증번호">
					</div>
					<div class="col-lg-4">
						<button type="button" class="btn btn-primary btn-block" id="authMailBtn">인증</button>
					</div>
					<div class="col-lg-4"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 이메일 인증 모달 종료 -->

<!-- 주소 검색 모달 -->
<div id="addrModal" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">주소 검색</h4>
			</div>
			<div class="modal-body">
				<div class="row" id="postcodify"></div>
				
				<script>
					$('#postcodify').postcodify({
						insertPostcode5  : '#entry_postcode5',
						insertAddress    : '#entry_address',
						insertExtraInfo : '#entry_extra_info',
						userFullJibeon   : true,
						mapLinkProvider  : 'google',
						ready : function() {
							$('#postcodify div.postcode_search_status.empty').hide();
						},
						beforeSearch : function() {
							$('#entry_box').hide();
						},
						afterSearch : function(selectedEntry) {
							$('#postcodify div.postcode_search_result').remove();
							$('#postcodify div.postcode_search_status.summary').hide();
							$('#entry_box').show();
							$('#entry_details').focus();
						}
					});
				</script>
				
				<div>
					<p>
						<label for="entry_postcode5">우편번호</label>
						<input type="text" class="form-control" id="entry_postcode5" readonly>
					</p>
					<p>
						<label for="entry_address">도로명주소</label>
						<input type="text" class="form-control" id="entry_address" readonly>
					</p>
					<p>
						<label for="entry_extra_info">지명주소</label>
						<input type="text" class="form-control" id="entry_extra_info" readonly>
					</p>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id="getAddrBtn" class="btn btn-primary" data-dismiss="modal">확인</button>
			</div>
		</div>
	</div>
</div>
<!-- 주소 검색 모달 종료 -->

<script src="<%= request.getContextPath() %>/js/bootstrap.js"></script>
</body>

</html>






