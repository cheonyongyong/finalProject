<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div class="card-body">
                <div class="table-responsive">
               		<div class="d-flex">
		                <div class="p-2 flex-grow-1"></div>
	                	<div class="p-2">
	                		<button type="button" class="btn btn-light" id="download">
		                		<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-download" viewBox="0 0 16 16">
								  <path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5z"></path>
								  <path d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708l3 3z"></path>
								</svg>
					           	내역 다운로드
					        </button>
	                	</div>
	                	<div class="p-2">
	                		<button type="button" class="btn btn-success" id="annUpdate">
	                			<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
								  <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>
								  <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5v11z"/>
								</svg>
	                			연차 일괄 조정
	                		</button>
	                	</div>
				    </div>
				    <div class="card">
					    <div class="card-body">
					    	<form method="post" id="searchForm">
					    		<input type="hidden" name="page" id="page"/>
								<div class="d-flex mb-3">
									<div class="p-2">
										<select id="yearSelect" name="yearSelect" class="form-select" aria-label="Default select example" style="width:200px; margin-top: 2px;">
											<option <c:if test="${pagingVO.yearSelect eq '2013'}">selected="selected"</c:if> value="2013">2013</option>
											<option <c:if test="${pagingVO.yearSelect eq '2014'}">selected="selected"</c:if> value="2014">2014</option>
											<option <c:if test="${pagingVO.yearSelect eq '2015'}">selected="selected"</c:if> value="2015">2015</option>
											<option <c:if test="${pagingVO.yearSelect eq '2016'}">selected="selected"</c:if> value="2016">2016</option>
											<option <c:if test="${pagingVO.yearSelect eq '2017'}">selected="selected"</c:if> value="2017">2017</option>
											<option <c:if test="${pagingVO.yearSelect eq '2018'}">selected="selected"</c:if> value="2018">2018</option>
											<option <c:if test="${pagingVO.yearSelect eq '2019'}">selected="selected"</c:if> value="2019">2019</option>
											<option <c:if test="${pagingVO.yearSelect eq '2020'}">selected="selected"</c:if> value="2020">2020</option>
											<option <c:if test="${pagingVO.yearSelect eq '2021'}">selected="selected"</c:if> value="2021">2021</option>
											<option <c:if test="${pagingVO.yearSelect eq '2022'}">selected="selected"</c:if> value="2022">2022</option>
											<option <c:if test="${pagingVO.yearSelect eq '2023'}">selected="selected"</c:if> value="2023">2023</option>
										</select>
									</div>
				                	<div class="p-2">
										<div class="gridjs-search">
											<input type="text" id="searchWord" name="searchWord" value="${searchWord }" placeholder="사원명 검색" aria-label="Type a keyword..." class="gridjs-input gridjs-search-input">
											<button type="submit" style="margin-bottom: 4px;" class="btn btn-primary" id="searchBtn">검색</button>
										</div>
									</div>
				                	<div class="ms-auto p-2">
				                		<select class="form-select" id="selectDept" name="selectDept" aria-label="Default select example">
						                	<c:forEach items="${comList}" var="comList">
							                	<c:if test="${fn:startsWith(comList.comCode, 'a') }">
													<option <c:if test="${pagingVO.selectDept eq comList.comCode }">selected="selected"</c:if> value="${comList.comCode}">${comList.comName}</option>
							                	</c:if>
										    </c:forEach>
										</select>
				                	</div>
							    </div>
							    <sec:csrfInput/>
						    </form>
		                    <table class="table table-bordered align-middle table-nowrap table-check">
		                        <thead class="table-light">
		                            <tr>
		                                <th scope="col">이름</th>
		                                <th scope="col">사번</th>
		                                <th scope="col">부서</th>
		                                <th scope="col">잔여 연차</th>
		                                <th scope="col">1월</th>
		                                <th scope="col">2월</th>
		                                <th scope="col">3월</th>
		                                <th scope="col">4월</th>
		                                <th scope="col">5월</th>
		                                <th scope="col">6월</th>
		                                <th scope="col">7월</th>
		                                <th scope="col">8월</th>
		                                <th scope="col">9월</th>
		                                <th scope="col">10월</th>
		                                <th scope="col">11월</th>
		                                <th scope="col">12월</th>
		                            </tr>
		                        </thead>
		                        <tbody id="tbody">
		                        	<c:set value="${pagingVO.dataList }" var="annList"/>
		                        	<c:choose>
		                        		<c:when test="${empty annList }">
		                        			<tr>
		                        				<td colspan="16" align="center">검색 내역이 존재하지 않습니다.</td>
		                        			</tr>
		                        		</c:when>
		                        		<c:otherwise>
		                        			<c:forEach items="${annList }" var="annList">
				                            <tr>
				                                <td>${annList.empName }</td>
				                                <td>${annList.empNo }</td>
				                                <td>${annList.comName }</td>
				                                <td>
										            <c:set var="curVacation" 
												            value="${annList.jan + annList.feb + annList.mar + annList.apr 
												            		+ annList.may + annList.jun + annList.jul + annList.aug 
												            		+ annList.sep + annList.oct + annList.nov + annList.dec}" />
										            ${curVacation}
				                                </td>
				                                <td>${annList.jan }</td>
				                                <td>${annList.feb }</td>
				                                <td>${annList.mar }</td>
				                                <td>${annList.apr }</td>
				                                <td>${annList.may }</td>
				                                <td>${annList.jun }</td>
				                                <td>${annList.jul }</td>
				                                <td>${annList.aug }</td>
				                                <td>${annList.sep }</td>
				                                <td>${annList.oct }</td>
				                                <td>${annList.nov }</td>
				                                <td>${annList.dec }</td>
				                            </tr>
											</c:forEach>
		                        		</c:otherwise>
		                        	</c:choose>
		                        </tbody>
		                    </table>
					    </div>
				    </div>
				    <div class="card-footer clearfix" id="pagingArea">
						${pagingVO.pagingHTML }
					</div>	
                </div>
            </div>
        </div>
    </div>
</div>
<form action="/excel/annualmanagelist" method="post" id="vacForm">
	<input type="hidden" name="selYear" id="selYear">
	<sec:csrfInput/>
</form>
<script type="text/javascript">
$(function() {
	var download = $("#download"); // 엑셀 다운로드 버튼
	var annUpdate = $("#annUpdate"); // 연차 일괄 조정 버튼
	var searchForm = $("#searchForm"); // 검색 폼
	var searchWord = $("#searchWord"); // 사원명 검색
	var selectDept = $("#selectDept"); // 부서 검색
	var yearSelect = $("#yearSelect"); // 연도 검색
	var pagingArea = $("#pagingArea"); // 페이징 구역
    
	pagingArea.on("click", "a", function(event){
		event.preventDefault();
		var pageNo = $(this).data("page");
		searchForm.find("#page").val(pageNo);
		searchForm.submit();
	});
	    
	selectDept.on("change", function(){
		searchForm.submit();
	});

	yearSelect.on("change", function(){
		searchForm.submit();
	});
	
    download.on("click", function() {
    	$("#selYear").val($("#yearSelect").val());
    	vacForm.submit();
	});
    
});
</script>