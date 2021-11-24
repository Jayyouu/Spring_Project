<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// 로그인 되었을 때 해당 유저의 id가 저장될 변수
	String user_id = null;

	// 세션에 "user_id" 이름을 가지는 value가 존재하면 
	// user_id 변수에 해당 value를 저장 
	// 로그인 상태 확인
	if(session.getAttribute("user_id") != null) {
		user_id = (String) session.getAttribute("user_id");
	}
%>
<!DOCTYPE html>
<html>
<head>

<title>JSP 게시판</title>
<meta http-equiv="Conetent-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width" initial-scale="1">
<link rel="stylesheet" href="${path}/resources/css/bootstrap.css">
<link rel="stylesheet" href="${path}/resources/css/custom.css">
</head>

<body>

<!-- 메인 네비게이션 -->
<!-- nav : 네비게이션 리스트를 만들어주는 HTML 태그 -->
<!-- default 철자 확인잘하기 -->
<nav class="navbar navbar-default">
	<!-- 네비게이션 헤더 -->
	<!-- div : 웹 페이지에서 공간을 지정하는 HTML 태그 (가로전체)-->
	<div class="navbar-header">
		<!-- botton : 버튼을 만들어주는 HTML 태그 -->
		<button type="button" class="navbar-toggle collapsed" 
			data-toggle="collapse" data-target="#bs-navbar-collapse"
			aria-expanded="false">
			<!-- span : 웹 페이지 공간을 지정하는 HTML 태그 (컨텐츠 사이즈만큼)-->
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
		<!-- a : url을 이동시켜주는 태그 href="" 속성에 경로 지정-->
		<a class="navbar-brand" href="${path}/home">JSP 게시판</a>
	</div>
	<!-- 네비게이션 헤더 종료 -->
	
	<!-- 네비게이션 메뉴 -->
	<div class="collapse navbar-collapse" id="bs-navbar-collapse">
		<!-- 메인 메뉴 -->
		<!-- ul : 순서가 없는 리스트를 만들어주는 HTML태그 -->
		<ul class="nav navbar-nav">
			<!-- li : 리스트 하나의 요소를 만들어주는 HTML태그 -->
			<li class="active"><a href="${path}/home">메인</a></li>
			<li><a href="${path}/bbs">게시판</a></li>
		</ul>
		<!-- 메인 메뉴 종료 -->
		
		<!-- 마이페이지 메뉴 -->
		<!-- 로그인이 되어있지 않을 때 표시 -->
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
					<li><a href="${path}/home/login">로그인</a></li>
					<li><a href="${path}/home/join">회원가입</a></li>
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
					<li><a href="${path}/home/logout">로그아웃</a></li>
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

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="${path}/resources/js/bootstrap.js"></script>
</body>

</html>