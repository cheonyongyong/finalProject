package kr.or.ddit.controller.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.ServiceResult;
import kr.or.ddit.mapper.document.DocMapper;
import kr.or.ddit.service.document.IDocService;
import kr.or.ddit.vo.CustomUser;
import kr.or.ddit.vo.DocVO;
import kr.or.ddit.vo.DocumentVO;
import kr.or.ddit.vo.EmpVO;
import kr.or.ddit.vo.InfoVO;
import kr.or.ddit.vo.MenuVO;
import kr.or.ddit.vo.PaginationInfoVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/document")
public class DocumentController {
	
	@Inject
	private IDocService docService;
	
	@Inject
	private DocMapper docMapper;
	
	
	// 일반사원 문서종류 화면
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@RequestMapping(value="/gdoctype")
	public String gdocType(
			@RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "docClfName") String searchType,
			@RequestParam(required = false) String searchWord,
			Model model, HttpServletRequest request
			) {
		PaginationInfoVO<DocumentVO> pagingVO = new PaginationInfoVO<DocumentVO>();
		
		if(StringUtils.isNotBlank(searchWord)) {
			pagingVO.setSearchType(searchType);
			pagingVO.setSearchWord(searchWord);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchWord", searchWord);
		}
		pagingVO.setCurrentPage(currentPage);
		
		int count = docService.countList(pagingVO);
		List<DocumentVO> docTypeList = docService.docTypeList(pagingVO);
		
		pagingVO.setTotalRecord(count);
		pagingVO.setDataList(docTypeList);
		
		MenuVO menu1 = new MenuVO("기안작성", "/document/gdoctype");
		List<MenuVO> menuList = Arrays.asList(menu1);
		
		model.addAttribute("menuList", menuList);
		model.addAttribute("docTypeList", pagingVO);
		return "document/gdoctype";
	}
	
	
	

	// 일반사원 문서작성내역 화면
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@RequestMapping(value="/gmydoclist")
	public String myDocList(
			@RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "2") String searchType,
			@RequestParam(required = false) String searchWord,
			Model model, HttpServletRequest request) {
		
//		List<PaginationInfoVO<Object>> pageList = new ArrayList<PaginationInfoVO<Object>>();
		
		PaginationInfoVO<DocVO> docPagingVO = new PaginationInfoVO<DocVO>();
		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = user.getEmp();
		
		if(StringUtils.isNotBlank(searchWord)) {
			docPagingVO.setSearchType(searchType);
			docPagingVO.setSearchWord(searchWord);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchWord", searchWord);
		}
		
		docPagingVO.setCurrentPage(currentPage);
		
		docPagingVO.setBoardCate(empVO.getEmpNo());
		List<DocVO> mydocList = docService.docList(docPagingVO);
		int myDocList = docService.myDocList(docPagingVO);
		
		docPagingVO.setDataList(mydocList);
		docPagingVO.setTotalRecord(myDocList);
		
		
		MenuVO menu1 = new MenuVO("내 신청내역", "/document/gmydoclist");
		List<MenuVO> menuList = Arrays.asList(menu1);
		
		
		model.addAttribute("menuList", menuList);
		model.addAttribute("docPagingVO",docPagingVO);
		return "document/gmydoc";
	}

	// 일반사원 임시문서보관함 화면
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@RequestMapping(value="/gmydocstorage", method = RequestMethod.GET)
	public String gmydocStorage(
			@RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "2") String searchType,
			@RequestParam(required = false) String searchWord,
			Model model, HttpServletRequest request) {
		
		PaginationInfoVO<DocVO> pagingVO = new PaginationInfoVO<DocVO>();
		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = user.getEmp();

		if(StringUtils.isNotBlank(searchWord)) {
			pagingVO.setSearchType(searchType);
			pagingVO.setSearchWord(searchWord);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchWord", searchWord);
		}
		
		pagingVO.setCurrentPage(currentPage);
		pagingVO.setBoardCate(empVO.getEmpNo());
		
		List<DocVO> docList = docService.selectDocList(pagingVO);
		int cnt = docMapper.selectDocCnt(pagingVO);
		pagingVO.setTotalRecord(cnt);
		pagingVO.setDataList(docList);
		
		MenuVO menu1 = new MenuVO("임시보관함", "/document/gmydocstorage");
		List<MenuVO> menuList = Arrays.asList(menu1);
		
		model.addAttribute("docPagingVO",pagingVO);
		model.addAttribute("menuList", menuList);
		return "document/gmydocstorage";
	}
	
	// 관리자 문서분류 화면
	@PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
	@RequestMapping(value="/mdoctype")
	public String mdocType(
			@RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "docClfName") String searchType,
			@RequestParam(required = false) String searchWord,
			Model model, HttpServletRequest request
			) {
		PaginationInfoVO<DocumentVO> pagingVO = new PaginationInfoVO<DocumentVO>();
		
		if(StringUtils.isNotBlank(searchWord)) {
			pagingVO.setSearchType(searchType);
			pagingVO.setSearchWord(searchWord);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchWord", searchWord);
		}
		pagingVO.setCurrentPage(currentPage);
		
		int count = docService.countList(pagingVO);
		List<DocumentVO> docTypeList = docService.docTypeList(pagingVO);
		
		pagingVO.setTotalRecord(count);
		pagingVO.setDataList(docTypeList);
		
		MenuVO menu1 = new MenuVO("기안작성", "/document/mdoctype");
		
		List<MenuVO> menuList = Arrays.asList(menu1);
		
		model.addAttribute("menuList", menuList);
		model.addAttribute("docTypeList", pagingVO);
		return "document/mdoctype";
	}
	
	// 관리자 전체 문서내역 화면
	@PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
	@RequestMapping(value="/alldoclist")
	public String allDocList(
			@RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "title") String searchType,
			@RequestParam(required = false) String searchWord,
			Model model, HttpServletRequest request) {
		
		PaginationInfoVO<DocVO> pagingVO = new PaginationInfoVO<DocVO>();
		
		

		if(StringUtils.isNotBlank(searchWord)) {
			pagingVO.setSearchType(searchType);
			pagingVO.setSearchWord(searchWord);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchWord", searchWord);
		}
		pagingVO.setCurrentPage(currentPage);
		
		MenuVO menu1 = new MenuVO("전체문서목록","/document/alldoclist");

		List<MenuVO> menuList = Arrays.asList(menu1);
		
		List<DocVO> docListAll = docService.docListAll(pagingVO);
		
		int docListCnt = docService.docListCnt();
		pagingVO.setTotalRecord(docListCnt);
		pagingVO.setDataList(docListAll);
		
		model.addAttribute("menuList",menuList);
		model.addAttribute("pagingVO", pagingVO);
		return "document/alldoclist";
	}
	
	// 관리자 문서작성내역 화면
	@PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
	@RequestMapping(value="/mmydoclist")
	public String mMyDocList(
			@RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "2") String searchType,
			@RequestParam(required = false) String searchWord,
			Model model, HttpServletRequest request) {
		
		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = user.getEmp();
		
		PaginationInfoVO<DocVO> docPagingVO = new PaginationInfoVO<DocVO>();
		
		if(StringUtils.isNotBlank(searchWord)) {
			docPagingVO.setSearchType(searchType);
			docPagingVO.setSearchWord(searchWord);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchWord", searchWord);
		}
		docPagingVO.setCurrentPage(currentPage);
		
		docPagingVO.setBoardCate(empVO.getEmpNo());
		
		List<DocVO> mydocList = docService.docList(docPagingVO);
		int mydocCnt = docMapper.myDocList(docPagingVO);
		
		docPagingVO.setDataList(mydocList);
		docPagingVO.setTotalRecord(mydocCnt);
		
		MenuVO menu1 = new MenuVO("내 신청내역", "/document/mmydoclist");
		List<MenuVO> menuList = Arrays.asList(menu1);
		
//		List<DocumentVO> docTypeList = docService.docType();
//		PaginationInfoVO<WorkAplyVO> workPagingVO = new PaginationInfoVO<WorkAplyVO>();
		
//		List<WorkAplyVO> workList = docService.workList(empVO.getEmpNo());
//		workPagingVO.setCurrentPage(currentPage);
//		workPagingVO.setDataList(workList);
//		workPagingVO.setTotalRecord(workList.size());
		
		docPagingVO.setScreenSize(3);
		
		model.addAttribute("menuList", menuList);
		model.addAttribute("docPagingVO",docPagingVO);
//		model.addAttribute("docType", docTypeList);
//		model.addAttribute("workList",workList);
		return "document/mmydoc";
	}
	
	
	// 관리자 받은문서 화면
	@PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
	@RequestMapping(value="/updatedoc")
	public String updateDoc(
			@RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false,  defaultValue = "2") String searchType,
			@RequestParam(required = false) String searchWord,
			Model model, HttpServletRequest request) {
		
		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = user.getEmp();
		
		PaginationInfoVO<DocVO> docPagingVO = new PaginationInfoVO<DocVO>();
		
		if(StringUtils.isNotBlank(searchWord)) {
			docPagingVO.setSearchType(searchType);
			docPagingVO.setSearchWord(searchWord);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchWord", searchWord);
		}
		
		docPagingVO.setCurrentPage(currentPage);
		docPagingVO.setBoardCate(empVO.getEmpNo());
		
		List<DocVO> aprDocList = docService.aprDocList(docPagingVO);
		int aprDocCnt = docMapper.aprDocCnt(docPagingVO);
		
		docPagingVO.setDataList(aprDocList);
		docPagingVO.setTotalRecord(aprDocCnt);
		
		MenuVO menu1 = new MenuVO("결재기안", "/document/updatedoc");
		List<MenuVO> menuList = Arrays.asList(menu1);
		
		model.addAttribute("menuList", menuList);
		model.addAttribute("docPagingVO", docPagingVO);
		
		return "document/apprdoc";
	}
	
	
	// 관리자 임시보관함 화면
	@PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
	@RequestMapping(value="/mmydocstorage")
	public String mmydocStorage(
			@RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false) String searchType,
			@RequestParam(required = false) String searchWord,
			Model model, HttpServletRequest request) {
		
		PaginationInfoVO<DocVO> pagingVO = new PaginationInfoVO<DocVO>();
		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = user.getEmp();
		
		if(StringUtils.isNotBlank(searchWord)) {
			pagingVO.setSearchType(searchType);
			pagingVO.setSearchWord(searchWord);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchWord", searchWord);
		}
		
		pagingVO.setCurrentPage(currentPage);
		pagingVO.setBoardCate(empVO.getEmpNo());
		
		List<DocVO> docList = docService.selectDocList(pagingVO);
		int cnt = docMapper.selectDocCnt(pagingVO);
		
		pagingVO.setTotalRecord(cnt);
		pagingVO.setDataList(docList);

		MenuVO menu1 = new MenuVO("임시저장함", "/document/mmydocstorage");
		List<MenuVO> menuList = Arrays.asList(menu1);
		
		model.addAttribute("docPagingVO",pagingVO);
		model.addAttribute("menuList", menuList);
		return "document/mmydocstorage";
	}
	
	// 문서작성 화면 (공통)
	@PreAuthorize("hasAnyRole('ROLE_MEMBER','ROLE_MANAGER','ROLE_ADMIN')")
	@RequestMapping(value="/documentwrite", method = RequestMethod.GET)
	public String docRegisterForm(Model model, String docClfCode) {
		log.info("docClfFile : " + docClfCode);
		
		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = user.getEmp();
		
		DocumentVO documentVO = docService.detail(docClfCode);
		
		List<InfoVO> emp = docMapper.deptList(empVO.getEmpNo());
		
		MenuVO menu1 = new MenuVO("문서작성", "");
		List<MenuVO> menuList = Arrays.asList(menu1);
		
		model.addAttribute("documentVO", documentVO);
		model.addAttribute("menuList", menuList);
		model.addAttribute("deptList", emp);
		return "document/documentwrite";
	}
	
	// 새로운 문서 작성
//	@PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String docRegister(DocVO docVO, HttpServletRequest req, RedirectAttributes ra) {
		log.info("docVO : " + docVO);
		
		String goPage = "";
		
		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = user.getEmp();
		
		// 작성한 문서가 근무일 때 
		if(docVO.getDocClfCode().equals("2")) {
			ServiceResult result = docService.docWorkRegister(docVO);
			if(result.equals(ServiceResult.OK)) {
				String auth = docMapper.selectAuth(empVO);
				
				if(auth.equals("ROLE_MEMBER")) {
					goPage = "redirect:/document/gmydoclist";
					ra.addFlashAttribute("result", result);
				}else {
					goPage = "redirect:/document/mmydoclist";
					ra.addFlashAttribute("result", result);
				}
			}
			return goPage;
		}
		// 작성한 문서가 휴가일 때
		else {
			ServiceResult result = docService.docVacRegister(docVO);
			if(result.equals(ServiceResult.OK)) {
				String auth = docMapper.selectAuth(empVO);
			if(auth.equals("ROLE_MEMBER")) {
				goPage = "redirect:/document/gmydoclist";
				ra.addFlashAttribute("result", result);
			}else {
				goPage = "redirect:/document/mmydoclist";
				ra.addFlashAttribute("result", result);
			}
			}
			return goPage;
		}
		
	}
	
	@ResponseBody 
	@RequestMapping(value="/aprupdate", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ServiceResult> aprUpdate(@RequestBody DocVO docVO){
		log.info("문서 번호 : " + docVO);
		log.info("문서번호 : " + docVO.getDocNo());
		log.info("반려사유 : " + docVO.getAprRejRsn());
		
		ServiceResult result = null;
		
		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = user.getEmp();
		
		docVO.setEmpNo(empVO.getEmpNo());
		
		if(docVO.getAprRejRsn() == null) {
			result = docService.aprUpdate(docVO);
		}else {
			result = docService.aprReject(docVO);
		}
		
		return new ResponseEntity<ServiceResult>(result, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value="/selectDoc", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<List<DocVO>> selectMyDocList(@RequestBody String docClfCode){
		log.info("docClfCode : " + docClfCode);
		
		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = user.getEmp();
		DocVO docVO = new DocVO();
		docVO.setEmpNo(empVO.getEmpNo());
		docVO.setDocClfCode(docClfCode);
		List<DocVO> docList = docService.selectClfCode(docVO);
		
		return new ResponseEntity<List<DocVO>>(docList, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value="/deletemydoc", method = RequestMethod.GET)
	public String deleteMyDoc(@RequestParam String docNo){
		log.info("삭제할 문서번호 : " + docNo);
		String goPage = "";
		
		ServiceResult result = docService.deleteDoc(docNo);
		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = user.getEmp();
		if(result.equals(ServiceResult.OK)) {
			String auth = docMapper.selectAuth(empVO);
			
			if(auth.equals("ROLE_MEMBER")) {
				goPage = "redirect:/document/gmydocstorage";
			}else {
				goPage = "redirect:/document/mmydocstorage";
			}
		}
		return goPage;
	}
	
	
	@RequestMapping(value="/docfix", method = RequestMethod.GET)
	public String docFix(@RequestParam String docNo, Model model) {
		
		log.info("수정할 문서 번호 : " + docNo);
		
		DocVO docVO = docMapper.selectDoc(docNo);
		
		model.addAttribute("documentVO", docVO);
		model.addAttribute("status", "u");
		
		return "document/documentwrite";
	}
	
	@RequestMapping(value="/docsave", method = RequestMethod.POST)
	public String gdocSave(DocVO docVO) {
		String goPage ="";
		log.info("수정완료한 문서 : " + docVO);
		
		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = user.getEmp();
		
		ServiceResult result = docService.docSave(docVO);
		
		if(result.equals(ServiceResult.OK)) {
			String auth = docMapper.selectAuth(empVO);
			
			if(auth.equals("ROLE_MEMBER")) {
				goPage = "redirect:/document/gmydocstorage";
			}else {
				goPage = "redirect:/document/mmydocstorage";
			}
		}
		return goPage;
	}
	
	
//	@PreAuthorize("hasRole('ROLE_MEMBER')")
//	@RequestMapping(value="/gdocument", method = RequestMethod.GET)
//	public String DocTypeList(
//			@RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
//			@RequestParam(required = false, defaultValue = "empDept1") String searchType,
//			@RequestParam(required = false) String searchWord,
//			Model model, HttpServletRequest request) {
//		log.info("문서분류리스트 페이지");
//		PaginationInfoVO<DocumentVO> pagingVO = new PaginationInfoVO<DocumentVO>();
//		
//		DocumentVO documentVO = new DocumentVO();
//		int docCount= docService.countList(documentVO);
//		List<DocumentVO> docTypeList = docService.docTypeList(pa);
//		
//		if(StringUtils.isNotBlank(searchWord)) {
//			pagingVO.setSearchType(searchType);
//			pagingVO.setSearchWord(searchWord);
//			model.addAttribute("searchType", searchType);
//			model.addAttribute("searchWord", searchWord);
//		}
//		
//		pagingVO.setCurrentPage(currentPage);
//		pagingVO.setTotalRecord(docCount);
//		
//		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		EmpVO empVO = user.getEmp();
//		List<DocVO> mydocList = docService.docList(empVO.getEmpNo());
//		
//		MenuVO menu1 = new MenuVO("문서함", "/document/gdocument");
//		List<MenuVO> menuList = Arrays.asList(menu1);
//		
//		MenuVO menu2 = new MenuVO("문서", "#home1", "tab");
//		MenuVO menu3 = new MenuVO("신청내역", "#profile1", "tab");
//		MenuVO menu4 = new MenuVO("임시저장", "#messages1", "tab");
//		
//		String currentURL = "#home1";
//		List<MenuVO> submenuList = Arrays.asList(menu2, menu3, menu4);
//		
//		pagingVO.setDataList(docTypeList);
//		
//		model.addAttribute("menuList", menuList);
//		model.addAttribute("submenuList", submenuList);
//		model.addAttribute("docTypeList", docTypeList);
//		model.addAttribute("mydocList", mydocList);
//		model.addAttribute("docCount", docCount);
//		request.setAttribute("currentURL", currentURL);
//		return "document/gdocument";
//	}

	
	
//	@PreAuthorize("hasAnyRole('ROLE_MEMBER','ROLE_MANAGER','ROLE_ADMIN')")
//	@RequestMapping(value="/mdocument", method = RequestMethod.GET)
//	public String mdocument(Model model, HttpServletRequest request) {
//		
//		PaginationInfoVO<DocumentVO> pagingVO = new PaginationInfoVO<DocumentVO>();
//		
//		DocumentVO documentVO = new DocumentVO();
//		int docCount= docService.countList(pagingVO);
//		List<DocumentVO> docTypeList = docService.docTypeList(pagingVO);
//		
//		CustomUser user = (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		EmpVO empVO = user.getEmp();
//		List<DocVO> mydocList = docService.docList(empVO.getEmpNo());
//		List<DocVO> docListAll = docService.docListAll();
//
//		DocVO docVO = new DocVO();
//		int allCount = docService.allCount(docVO);
//		
//		MenuVO menu1 = new MenuVO("문서함", "/document/gdocument");
//		List<MenuVO> menuList = Arrays.asList(menu1);
//		
//		MenuVO menu2 = new MenuVO("문서", "#home1", "tab");
//		MenuVO menu3 = new MenuVO("전체조회", "#home2", "tab");
//		MenuVO menu4 = new MenuVO("신청내역", "#profile1", "tab");
//		MenuVO menu5 = new MenuVO("받은문서", "#profile2", "tab");
//		MenuVO menu6 = new MenuVO("임시저장", "#messages1", "tab");
//		
//		String currentURL = "#home1";
//		
//		List<MenuVO> submenuList = Arrays.asList(menu2, menu3, menu4, menu5, menu6);
//		
//		model.addAttribute("menuList", menuList);
//		model.addAttribute("submenuList", submenuList);
//		model.addAttribute("docTypeList", docTypeList);
//		model.addAttribute("mydocList", mydocList);
//		model.addAttribute("docListAll", docListAll);
//		model.addAttribute("allCount", allCount);
//		model.addAttribute("docCount", docCount);
//		request.setAttribute("currentURL", currentURL);
//		
//		return "document/mdocument";
//	}
	
//	@ResponseBody
//	@RequestMapping(value="/search", method = RequestMethod.POST, consumes = "application/json")
//	public ResponseEntity<List<DocumentVO>> searchDocument(@RequestBody Map<String, String> map){
//		log.info("searchDocument 실행");
//		log.info("searchWord : " + map.get("searchWord").toString());
//		String searchWord = map.get("searchWord").toString();
//		DocumentVO documentVO = new DocumentVO();
//		documentVO.setDocClfName(searchWord);
//		int docCount= docService.countList(documentVO);
//		documentVO.setCount(docCount);
//		List<DocumentVO> docList = new ArrayList<DocumentVO>(); 
//		if(docService.search(documentVO).size() == 0) {
//			documentVO.setCount(docCount);
//			docList.add(documentVO);
//		}else {
//			docList = docService.search(documentVO);
//			docList.get(0).setCount(docCount);
//		}
//				
//		
////		String docName = docList.get(0).getDocClfName();
////		log.info("docCount : " + docCount);
////		docList.get(0).setCount(docCount);
////		log.info("docName : " + docName);
//		
//		return new ResponseEntity<List<DocumentVO>>(docList, HttpStatus.OK);
//	}
	
	@PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
	@ResponseBody
	@RequestMapping(value="/searchall", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<List<DocVO>> searchDocumentAll(@RequestBody Map<String, String> map){
		log.info("searchAll 실행");
		log.info("searchWord : " + map.get("searchWord").toString());
		String searchWord = map.get("searchWord").toString();
		DocVO docVO = new DocVO();
		List<DocVO> docList = new ArrayList<DocVO>();
		docVO.setEmpName(searchWord);
		int allCount = docService.allCount(docVO);
//		docVO.setCount(allCount);
		log.info("이미 오류");
		if(docService.seachDoc(docVO).size() == 0){
			log.info("결과 오류");
			docVO.setCount(allCount);
			docList.add(docVO);
		}else {
			docList = docService.seachDoc(docVO);
			docList.get(0).setCount(allCount);
			
		}
		
		
		return new ResponseEntity<List<DocVO>>(docList, HttpStatus.OK);
	}
	
	
}



















