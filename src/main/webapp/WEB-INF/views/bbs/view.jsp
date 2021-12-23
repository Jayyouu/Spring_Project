<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<title>JSP 게시판</title>
<meta http-equiv="Conetent-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width" initial-scale="1">
<link rel="stylesheet" href="${path}/resources/css/bootstrap.css">
<link rel="stylesheet" href="${path}/resources/css/custom.css">
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="${path}/resources/js/bootstrap.js"></script>

<script>
	$(document).ready(function(){
		
		$('#replyBtn').click(function(){
			var boarder_id = '${map.boarder.boarder_id}';
			var contents = $('#r_contents').val();
			
			$.ajax({
				type: 'GET',
				url: './insertReply',
				data: {boarder_id: boarder_id,
						contents : contents},
				
				//dataType: 'text' -> json을 이용하여 전달받기 때문에 데이터타입 변경,
				// alert(data[i].contents);
				// 컨트롤러에서 Stirng으로 반환하면 undefined 로 알림
				//  -> 반환을 문자열로 하면 안됨, 문자 하나하나를 읽음 -> list 자체를 반환 
				// List<Reply>로 반환타입 -> 500 에러 발생
				// 객체 타입으로 넘기고 싶을때 JSON 형태로 데이터를 전송
				// JSON -> 문자 포맷 : {'key': 'value'} (문자열을 {}형태로 맞춰서 1:1 대응하게 전달)
				// key 값에 프로퍼티 명, value 값에 프로퍼티 벨류값
				// 객체형태를 문자열로 다루기 위해서 이용
				// java 에서 객체럴 javascript 에서 객체로 이용하기위해 가교적 역할을 하는것이 JSON임
				// 객체를 json으로 변환 -> 스프링에서 알아서 전환시키기 위해 pom.xml에 의존성 주입 (jackson, json)
				dataType: 'JSON',
				success: function(data) {
					if(data.length == 0) {
						alert('로그인이 필요합니다.');
						return;
					}
					// tbody 안의 값을 비워주는 역할
					// (글을 입력하고 값을 전달하고 나면 입력할 값들이 비워서 초기화 하기 위해)
					$('#replyBody').empty();
					
					for (var i = 0; i < data.length; i++) {
						var str = '';
						str += '<tr>';
						str += '<td width="50%">' + data[i].contents + '</td>';	// 내용
						str += '<td width="20%">' + data[i].writer + '</td>';	// 작성자
						str += '<td width="20%">' + data[i].reg_date + '</td>';	// 날짜
						str += '<td width="10%"><button class="btn btn-danger btn-block">삭제</button></td>';	//삭제버튼
						str += '</tr>'
						$('#replyBody').append(str);
					}
				}
			//ajax		
			});
			
		// relyBtn.click.function	
		});
		
	// document.ready.function		
	});
</script>
<!-- delete 삭제 script -->
<script>
	function deleteReply(reply_id) {
		var boarder_id = '${map.boarder.boarder_id}';
		
		$.ajax({
			type: 'GET',
			url: './deleteReply',
			data: { reply_id: reply_id,
					boarder_id: boarder_id
				 },
			
			//dataType: 'text' -> json을 이용하여 전달받기 때문에 데이터타입 변경,
			dataType: 'JSON',
			success: function(data) {
				if(data == null) {
					alert('로그인이 필요합니다.');
					return;
				}
				// tbody 안의 값을 비워주는 역할
				// (글을 입력하고 값을 전달하고 나면 입력할 값들이 비워서 초기화 하기 위해)
				$('#replyBody').empty();
				
				for (var i = 0; i<data.length; i++) {
					
					var str = '';
					str += '<tr>';
					str += '<td width="50%">' + data[i].contents + '</td>';	// 내용
					str += '<td width="20%">' + data[i].writer + '</td>';	// 작성자
					str += '<td width="20%">' + data[i].reg_date + '</td>';	// 날짜
					str += '<td width="10%">';
					if('${user_id}' == data[i].writer) {
						str += '<button class="btn btn-danger btn-block" onclick="deleteReply(' + data[i].reply_id + ')">삭제</button>';
					}
					str += '</td>';	
					str += '</tr>'
					$('#replyBody').append(str);
					
					}
			}
		//ajax		
		});
		
	// function	
	}
</script>
</head>
<body>

<!-- 메인 네비게이션 -->
<jsp:include page="../bbsNav.jsp" />

<!-- 게시물 보기 양식 -->
<div class="container">
	<div class="row">
			<table class="table table-striped" style="text-align: center; border: 1px solid #bbbbbb;">
				<thead>
					<tr>
						<th colspan="2"style="background-color : #eeeeee; text-align: center;">게시물</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="width: 20%;">제목</td>
						<!-- ** XSS 방지 ** 매우 중요 -->
						<!-- HTML태그를 문자로 바꿀 때 -->				
						<!-- ' '(공백)-> &nbsp; : 문자열.replaceAll(" ","&nbsp;") -->
						<!-- '<' 	 -> &lt;   : 문자열.replaceAll("<","&lt;") -->
						<!-- '>'	 -> &gt;   : 문자열.replaceAll(" ","&gt;") -->
						<!-- '\n'	 -> '<br>' : 문자열.replaceAll(" ","<br>") -->
						<!--  문자열.replaceAll(" ","&nbsp;") -->
						<td>${map.boarder.title }</td>
					</tr>
					<tr>
						<td>작성자</td>
						<td>${map.boarder.writer }</td>
					</tr>
					<tr>
						<td>작성일</td>
						<td>${map.boarder.reg_date }</td>
					</tr>
					<tr>
						<td>내용</td>
						<td style="min-height: 200px; text-align: left;">${map.boarder.contents }</td>
					</tr>
					<c:if test="${not empty map.uploadFile}">
					<tr>
						<td>첨부파일</td> <!-- 다운로드 기능 a태그 이용하여 설정. -->
						<td><a href="./downloadAction?boarder_id=${map.uploadFile.boarder_id }&file_realName=${map.uploadFile.file_realName }">${map.uploadFile.file_name }</a></td>
					</tr>
					
					</c:if>
				</tbody>
			</table>
			<table class="table table-striped" style="text-align: center; border: 1px solid #bbbbbb;" >
				<thead>
					<tr>
						<td colspan="4">댓글</td>
					</tr>
				</thead>
				<tbody id="replyBody">
					<c:forEach var="reply" items="${map.replyList }">
					<tr>
						<td width="50%"><!-- 내용 -->${reply.contents}</td>
						<td width="20%"><!-- 작성자 -->${reply.writer}</td>
						<td width="20%"><!-- 날짜 -->${reply.reg_date}</td>
						<td width="10%">
						<c:if test="${user_id == reply.writer }">
							<button type="button" class="btn btn-danger btn-block" onclick="deleteReply(${reply.reply_id})">삭제</button>
						</c:if>
						</td>
					</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<!-- 로그인 상태일때만 댓글달기창 표시 -->
					<c:if test="${not empty user_id }">
					<tr>
						<td colspan="3">
							<input type="text"  class="form-control"  id="r_contents" placeholder="댓글 내용">
						</td>
						<td>
							<button type="button" class="btn btn-default btn-block" id="replyBtn">등록</button>
						</td>
					</tr>
					</c:if>
				</tfoot>
			</table>
			
			<a href="../bbs" class="btn btn-default">목록</a>
			<c:if test="${user_id eq map.boarder.writer }">
			<a href="./update?boarder_id=${map.boarder.boarder_id }" class="btn btn-success">수정</a>
			<a onclick="return confirm('정말 삭제하시겠습니까?')" href="./deleteAction?boarder_id=${map.boarder.boarder_id }" class="btn btn-danger">삭제</a>
			</c:if>
	</div>
</div>
<!-- 게시물 보기 양식 종료 -->

<script>
$(document).ready(function(){
	var msg = '${msg}';
	if(msg != null && msg != '') alert(msg);
});
</script>
</body>

</html>







