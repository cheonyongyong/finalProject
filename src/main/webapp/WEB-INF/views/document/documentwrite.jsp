<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<div class="row">
	<div class="fluid">
		<div class="card">
			<div class="card-body">
				<div class="card p-5">
					<div class="col-md-6">
						<h4 class="card-title-20">${documentVO.docClfName }</h4>
					</div>
					<form class="needs-validation row gx-3 gy-2" method="post" action="/document/register" id="registerForm">
						<input type="hidden" id="docClfCode" name="docClfCode" value="${documentVO.docClfCode }">
						<c:if test="${status eq 'u' }">
							<input type="hidden" id="docNo" name="docNo" value="${documentVO.docNo }">
						</c:if>
						<div class="row gx-3 gy-2">
							<div class="col-md-1"></div>
							<div class="col-md-4 align-items-top">
								<div class="card">
									<div class="card-body">
										<div class="row">
											<label for="example-text-input"
												class="col-md-4 text-center col-form-label">제목</label>
											<div class="col-md-8">
												<input class="form-control" type="text" name="docTitle" id="docTitle" value="${documentVO.docTitle }">
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-4 ms-auto">
								<div class="card">
									<div class="card-header">
										<div class="row">
											<div class="col-md-6">
												<h4 class="card-title-15">결재자</h4>
											</div>
											<div class="shrink justify-content-md-end col-md-3 ms-auto">
												<button type="button" class="btn btn-outline-primary w-xs" data-bs-toggle="modal"
																		data-bs-target="#insertApr">추가</button>
											</div>
											<div class="shrink justify-content-md-end col-md-3 ms-auto">
												<button type="button" class="btn btn-outline-danger w-xs" id="delAprBtn">삭제</button>
											</div>
										</div>
									</div>
									<div class="card" id="apr"></div>
								</div>
							</div>
							<div class="col-md-1"></div>
							<div class="row"></div>
						</div>
						<div class="row justify-content-md-center">
							<div class="col-md-10">
								<c:choose>
									<c:when test="${status eq 'u'}">
										<textarea class="form-control" cols="2000" id="docClfFile" name="docClfFile" rows="2000">${documentVO.docCont }</textarea>
									</c:when>
									<c:otherwise>
										<textarea class="form-control" cols="2000" id="docClfFile" name="docClfFile" rows="2000">${documentVO.docClfFile }</textarea>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="d-grid gap-2 d-md-flex justify-content-md-end">
							<button class="btn btn-secondary" type="button" id="cancelBtn">취소</button>
							<button class="btn btn-primary" type="button" id="registerBtn">등록하기</button>
							<button class="btn btn-success" type="button" id="storageBtn">임시저장</button>
						</div>
						<sec:csrfInput />
					</form>
					<!-- end form -->
				</div>
				<div class="modal fade bs-example-modal-center" tabindex="-1" id="insertApr"
					aria-labelledby="mySmallModalLabel" aria-modal="true" role="dialog"
					style="display: none;">
					<div class="modal-dialog modal-dialog-centered">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">결재자 추가</h5>
								<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
							</div>
							<div class="modal-body">
								<div class="card">
									<div class="card-body">
										<ul class="nav nav-pills">
											<li class="nav-item">
												<select class="form-select" name="searchType" id="aprSelect">
													<c:forEach items="${deptList }" var="dept">
														<option value="${dept.empNo }">${dept.empName }/${dept.deptName }</option>
													</c:forEach>
												</select>
											</li>
										</ul>
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
								<button type="button" class="btn btn-success" data-bs-dismiss="modal" id="addAprBtn">확인</button>
							</div>
						</div>
						<!-- /.modal-content -->
					</div>
					<!-- /.modal-dialog -->
				</div>
			</div>
		</div>
	</div>
</div>



<script type="text/javascript">
$(function() {
	CKEDITOR.replace("docClfFile", {
		filebrowserUploadUrl : "",
		height : 800
	});

	var count = 1;
	var addAprBtn = $("#addAprBtn");
	var storageBtn = $("#storageBtn");
	var delAprBtn = $("#delAprBtn");
	var cancelBtn = $("#cancelBtn");
	
	var registerBtn = $("#registerBtn");
	var registerForm = $("#registerForm");

	var apr = $("#apr");

	addAprBtn.on("click",function() {
		var aprSelect = $("#aprSelect");
		var name = $("#aprSelect option:selected").text().split("/")[0];
		var empno = $("#aprSelect option:selected").val();
		console.log($("#aprSelect option:selected").text());
		console.log($("#aprSelect option:selected").val());
		
		var html = "";
		html += "<div class='card-body'>";
		html += "<div class='row align-items-center'>";
		html += "<label for='example-text-input' class='col-md-3 col-form-label'>결재자"
				+ count + "순위</label>";
		html += "<div class='col-md-5'>";
		html += "<input class='form-control' type='hidden' name='empNo' id='empNo' value='"+empno+"'>";
		html += "<input class='form-control' type='text' value='"+name+"'>";
		html += "</div>";
		html += "<div class='col-md-2 ms-auto'>";
		html += "<div class='form-check' style='margin: auto; position: relative;'>";
		html += "<input class='form-check-input' type='checkbox' value='Y' name='aprJob' id='apr"+count+"'>";
		html += "<label class='form-check-label text-primary' for='apr+"+count+"+'>전결자</label>";
		html += "</div>";
		html += "</div>";
		html += "</div>";
		html += "</div>";
		count += 1;
		apr.append(html);
	});

	storageBtn.on("click", function() {
		console.log(CKEDITOR.instances.docClfFile.getData());
		
		
		if (confirm("현재 내용을 저장하시겠습니까?")) {
			registerForm.attr("action","/document/docsave");
			registerForm.submit();
		}

	});

	registerBtn.on("click", function() {
		
		var parser = new DOMParser();
		var html = parser.parseFromString(CKEDITOR.insetances.docClfFile.getData(), 'text/html');
		var str = html.all[2].childNodes[2].innerHTML;
		var html2 = parser.parseFromString(str, 'text/html');
		var input = html2.all[2].childNodes[11].defaultValue;
		var select1 = html2.all[2].childNodes[13].value;
		var select2 = html2.all[2].childNodes[15].value;
		var select3 = html2.all[2].childNodes[17].value;
		var select4 = html2.all[2].childNodes[19].value;
		
		console.log(input + " " + select1 + ":" + select2 + "~" + select3 + ":" + select4);
		
// 		if (confirm("현재 내용을 제출하시겠습니까?")) {
// 			registerForm.submit();
// 		}
	});

	delAprBtn.on("click", function() {
		// 		console.log(arp.find('.card-body').last());
		if(count == 1){
			return;
		}else{
			
		count -= 1;
		apr.find('.card-body').last().remove();
		}
	});

	// 	function checkArp(){
	// 		arp.find('.card-body').last().find('#arp'+count+').checked();
	// 	}
	
	cancelBtn.on("click", function(){
		history.back();
	});
});
</script>











