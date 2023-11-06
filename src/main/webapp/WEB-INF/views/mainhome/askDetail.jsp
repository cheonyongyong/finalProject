<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="site-blocks-cover overlay" style="background-image: url(${pageContext.request.contextPath }/resources/images/cloud.jpg); height: 30px;" data-aos="fade" id="home-section">
	<div class="container">
		<div class="row align-items-center justify-content-center">
			<div class="col-md-6 mt-lg-5 text-center">
				<h1>문의게시판 상세</h1>
			</div>
		</div>
	</div>
</div>

<div class="container pt-5 pb-5">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="card card-primary">
					<input type="hidden" id="cusEmail" name="cusEmail" value="${inqBoard.customerVO.cusEmail }">
					<div class="card-header" style="background-color: #D0DDF3; color: black;">
						<h5 class="card-title">${inqBoard.inqTitle }</h5>

						<div class="card-tools" align="right">
							<c:if
								test="${SessionInfo.cusRnum eq 'admin' and inqBoard.inqConf eq 'N' }">
								<button type="button" class="btn btn-light" id="replyBtn">답변하기</button>
								<button type="button" class="btn btn-primary" id="adminBtn">답변할 목록</button>
								<br>
							</c:if>
							작성자(사업자등록번호) : ${inqBoard.customerVO.cusName }(${inqBoard.cusRnum })<br>
							회사 이름 : ${inqBoard.customerVO.cusCom }	|	작성일자 :<fmt:formatDate value="${inqBoard.inqDate }" pattern="yyyy-MM-dd HH:mm" />	|	
								<c:if test="${inqBoard.inqConf eq 'N' }">
									<span class="badge badge-danger" style="background-color: #F5899E;">답변대기</span>
								</c:if>
								<c:if test="${inqBoard.inqConf eq 'Y' }">
									<span class="badge badge-primary" style="background-color: #81B5F5;">답변완료</span>
								</c:if>
						</div>
					</div>
					<div class="card-body">${inqBoard.inqCont }</div>
					<div class="card-footer"
						style="background-color: #D0DDF3; color: black;" align="right">
						<button type="button" class="btn btn-light" id="listBtn">목록</button>
						<c:if test="${SessionInfo.cusRnum == inqBoard.cusRnum or SessionInfo.cusRnum eq 'admin' }">
							<button type="button" class="btn btn-primary" id="modiBtn">수정</button>
							<button type="button" class="btn btn-secondary" id="delBtn">삭제</button>
						</c:if>
					</div>
				</div>
			</div>
			<form action="/askdelete" method="post" id="delForm">
				<input type="hidden" name="inqNo" value="${inqBoard.inqNo }" />
				<sec:csrfInput />
			</form>
		</div>
	</div>

	<br>

	<div class="container-fluid" id="replyDiv" style="display: none;">
		<div class="row">
			<div class="col-md-12">
				<div class="card card-primary">
					<div class="card-header"
						style="background-color: #D0DDF3; color: black;">
						<h5 class="card-title">답변하기</h5>
					</div>
					<div class="card-body">
						<form action="/askreply" method="post" id="askReplyForm">
							<input type="hidden" name="inqNo" id="inqNo" value="${inqBoard.inqNo }" />
							<textarea id="inqRepl" name="inqRepl" cols="30" rows="10" maxlength="666" class="form-control"></textarea>
							<sec:csrfInput />
						</form>
					</div>
					<div class="card-footer"
						style="background-color: #D0DDF3; color: black;" align="right">
						<c:if
							test="${SessionInfo.cusRnum eq 'admin' and inqBoard.inqConf eq 'N' }">
							<button type="button" class="btn btn-primary" id="replySubBtn">답변 등록</button>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>

	<c:if test="${inqBoard.inqConf eq 'Y' }">
		<div class="container-fluid" id="replyViewDiv">
			<div class="row">
				<div class="col-md-12">
					<div class="card card-primary">
						<div class="card-header" style="background-color: #D0DDF3; color: black;">
							<h5 class="card-title">${inqBoard.inqTitle }에 대한 답변 내용입니다.</h5>
						</div>
						<div class="card-body">${inqBoard.inqRepl }</div>
						<c:if test="${SessionInfo.cusRnum eq 'admin' and inqBoard.inqConf eq 'Y' }">
							<div class="card-footer" style="background-color: #D0DDF3; color: black;" align="right">
								<button type="button" class="btn btn-primary" id="replyViewBtn">답변 수정하기</button>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</c:if>
	
	<br>
	
	<c:if test="${SessionInfo.cusRnum eq 'admin' and inqBoard.inqConf eq 'Y' }">
	<div class="container-fluid" id="replyModiDiv" style="display: none;">
		<div class="row">
			<div class="col-md-12">
				<div class="card card-primary">
					<div class="card-header" style="background-color: #D0DDF3; color: black;">
						<h5 class="card-title">${inqBoard.inqTitle } 답변 내용 수정하기</h5>
					</div>
					<div class="card-body">
						<form action="/askreply" method="post" id="askReplyModiForm">
							<input type="hidden" name="inqNo" id="inqNo" value="${inqBoard.inqNo }" />
							<textarea id="inqRepl" name="inqRepl" cols="30" rows="10" maxlength="666" class="form-control">${inqBoard.inqRepl }</textarea>
							<sec:csrfInput />
						</form>
					</div>
					<div class="card-footer"
						style="background-color: #D0DDF3; color: black;" align="right">
						<c:if
							test="${SessionInfo.cusRnum eq 'admin' and inqBoard.inqConf eq 'Y' }">
							<button type="button" class="btn btn-primary" id="replyModiBtn">답변 수정</button>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
	</c:if>
	
	
	
	
</div>




<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<script type="text/javascript">
$(function() {
	var listBtn = $("#listBtn");
	var modiBtn = $("#modiBtn");
	var adminBtn = $("#adminBtn");
	var delBtn = $("#delBtn");
	var delForm = $("#delForm");
	var replyBtn = $("#replyBtn");
	var askReplyForm = $("#askReplyForm");
	var replySubBtn = $("#replySubBtn");
	var replyViewBtn = $("#replyViewBtn");
	var replyModiBtn = $("#replyModiBtn");
	var askReplyModiForm = $("#askReplyModiForm");
	
	
	listBtn.on("click", function() {
		location.href = "/askboard";
	});
	
	modiBtn.on("click", function() {
		delForm.attr("method", "get");
		delForm.attr("action", "/askupdate");
		delForm.submit();
	});
	
	adminBtn.on("click", function() {
		location.href = "/mypageadmin/adminboard";
	})
	
	delBtn.on("click", function() {
		if(confirm("정말 삭제하시겠습니까?")) {
			delForm.submit();
		}
	});
	
	replyBtn.on("click", function() {
		$("#replyDiv").toggle();
	});
	
	
	replySubBtn.on("click", function() {
		if(confirm("답변을 등록하시겠습니까?")) {
			askReplyForm.submit();
		}
	});
	
	replyViewBtn.on("click", function() {
		$("#replyModiDiv").toggle();
	});
	
	replyModiBtn.on("click", function() {
		if(confirm("답변을 수정하시겠습니까?")) {
			askReplyModiForm.submit();
		}
	});
	
});
</script>
