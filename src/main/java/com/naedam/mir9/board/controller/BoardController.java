package com.naedam.mir9.board.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naedam.mir9.board.model.service.BoardService;
import com.naedam.mir9.board.model.vo.Board;
import com.naedam.mir9.board.model.vo.BoardAuthority;
import com.naedam.mir9.board.model.vo.BoardOption;
import com.naedam.mir9.board.model.vo.Page;
import com.naedam.mir9.board.model.vo.Post;
import com.naedam.mir9.board.model.vo.Search;
import com.naedam.mir9.member.model.vo.Member;

@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	int pageUnit = 5;
	int pageSize = 5;
	
	@PostMapping("addBoard")
	public String addBoard(@ModelAttribute("board") Board board,
						   @ModelAttribute("boardAuthority") BoardAuthority boardAuthority,
						   @ModelAttribute("boardOption") BoardOption boardOption) throws Exception{
		
		System.out.println("board/addBoard 시작");
		boardService.addBoard(board);
		boardAuthority.setAuthorityBoard(board);
		boardService.addAuthority(boardAuthority);
		boardOption.setOptionBoard(board);
		boardService.addOption(boardOption);
		
		
		return "redirect:/board/listBoard";
	}
	
	@PostMapping("addPost")
	public String addPost(@ModelAttribute("post") Post post,
						  @ModelAttribute("board") Board board,
					      @RequestParam("postName") MultipartFile postName,
					      @RequestParam("ThombnailName") MultipartFile ThombnailName) throws Exception {
		
		System.out.println("addPost 시작");

		String filePath = "C:\\Users\\user\\ckImageFile"; 
		
		File file = new File(filePath+postName.getOriginalFilename());
		File file2 = new File(filePath+ThombnailName.getOriginalFilename());
		post.setPostBoard(board);
		post.setPostFile(postName.getOriginalFilename());
		post.setPostThombnail(ThombnailName.getOriginalFilename());
		postName.transferTo(file);
		ThombnailName.transferTo(file2);
		
		boardService.addPost(post);
		
		System.out.println("post 데이터 확인 ::: "+post);
		System.out.println("board 데이터 확인 ::: "+board);
		
		return "mir9/board/postList?boardNo="+board.getBoardNo();
	}
	
	@PostMapping("updateBoard")
	public String updateBoard(@RequestParam("boardNo") int boardNo,
			   @ModelAttribute("board") Board board,
			   @ModelAttribute("boardAuthority") BoardAuthority boardAuthority,
			   @ModelAttribute("boardOption") BoardOption boardOption) throws Exception{
		
		System.out.println("updateBoard 시작"); 
		System.out.println("board 확인 :: "+board);
		System.out.println("boardAuthority 확인 :: "+boardAuthority);
		System.out.println("boardOption 확인 :: "+boardOption);		
		
		boardService.updateBoard(board);
		boardAuthority.setAuthorityBoard(board);
		boardService.updateAuthority(boardAuthority);
		boardOption.setOptionBoard(board);
		boardService.updateOption(boardOption);
		
		
		return "redirect:/board/listBoard";
	}
	
	
	
	
	@GetMapping("listBoard")
	public String listBoard(@ModelAttribute("search") Search search, Board board, Model model) throws Exception {
		
		System.out.println("listBoard 시작");
		
		//페이지 처리
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		Map<String, Object> map = boardService.getBoardList(search);
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "board/boardList";
	}
	
	@GetMapping("postList")
	public String listPost(Board board, Model model, @ModelAttribute("search") Search search) throws Exception {
		
		System.out.println("/listPost 시작");
		
		Post post = new Post();
		Member member = new Member();
		
		Board board2 = boardService.getBoardData(board.getBoardNo());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("board", board);
		
		Map<String, Object> resultMap = boardService.getPostList(map);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)resultMap.get("totalCount")).intValue(), pageUnit, pageSize);
		
		member = boardService.getMemberData(board2.getBoardMemberNo().getMemberNo());
		resultMap.put("member", member);
		
		model.addAttribute("list", resultMap.get("list")); 
		model.addAttribute("board", board);
		model.addAttribute("board2", board2);
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("member", member);
		
		return "board/postList";
	}
	
	@PostMapping("deleteChoiceBoard")
	public void deleteChoiceBoard(@RequestParam(value = "boardArr[]") List<String> boardArr, 
								  Board board) throws Exception{
		
		System.out.println("deleteChoiceBoard 시작");
		
		int result = 0;
		int boardNo = 0;
		
		for(String i : boardArr) {
			boardNo = Integer.parseInt(i);
			board.setBoardNo(boardNo);
			boardService.deleteChoiceBoard(board.getBoardNo());
		}
		result = 1;
		
	}
	
	@PostMapping("deleteChoicePost")
	public void deleteChoicePost(@RequestParam(value = "postArr[]") List<String> postArr, 
								  Post post) throws Exception{
		
		System.out.println("deleteChoicePost 시작");
		
		int result = 0;
		int postNo = 0;
	
		for(String i : postArr) {
			postNo = Integer.parseInt(i);
			post.setPostNo(postNo);
			boardService.deleteChoicePost(post.getPostNo());
		}
		result = 1;
		
	}
	
	@PostMapping("addPostCopy")
	public void addPostCopy(@RequestParam(value = "postArr[]") List<String> postArr,
							@RequestParam("boardNo")int boardNo) throws Exception{
		
		System.out.println("addPostCopy 시작");
		
		Board board = new Board();
		
		
		int result = 0;
		int postNo = 0;
		System.out.println("boardNo 확인 ::: "+boardNo);
		for(String i : postArr) {
			postNo = Integer.parseInt(i);
			Post post = boardService.getPostData(postNo);
			post.getPostBoard().setBoardNo(boardNo);
			boardService.addPost(post);
		}
		result = 1;
		
	}
	
	@PostMapping("addPostChange")
	public void addPostChange(@RequestParam(value = "postArr[]") List<String> postArr,
							@RequestParam("boardNo")int boardNo) throws Exception{
		
		System.out.println("addPostChange 시작");
		
		Board board = new Board();
		
		
		int result = 0;
		int postNo = 0;
		
		for(String i : postArr) {
			postNo = Integer.parseInt(i);
			Post post = boardService.getPostData(postNo);
			post.getPostBoard().setBoardNo(boardNo);
			boardService.addPost(post);
			boardService.deleteChoicePost(postNo);
		}
		result = 1;
		
	}	
	
	@GetMapping("test")
	public String test() {
		return "board/test";
	}
	
	@PostMapping("imageUpload")
	public void imageUpload(HttpServletRequest request, HttpServletResponse response,
							MultipartHttpServletRequest multiFile,
							@RequestParam MultipartFile upload) throws Exception{
		
		System.out.println("진입 확인");
		UUID uid = UUID.randomUUID();
		
		OutputStream out = null;
		PrintWriter printWriter = null;
		
		
		
		try{ 
			//파일 이름 가져오기 
			String fileName = upload.getOriginalFilename(); 
			byte[] bytes = upload.getBytes(); 
			//이미지 경로 생성 
			String path = "C:\\Users\\user\\ckImageFile";// fileDir는 전역 변수라 그냥 이미지 경로 설정해주면 된다. 
			String ckUploadPath = path + uid + "_" + fileName; 
			File folder = new File(path); 
			//해당 디렉토리 확인 
			if(!folder.exists()){ 
				try{ folder.mkdirs(); // 폴더 생성 
				}catch(Exception e){ 
					e.getStackTrace(); 
				} 
			} 
			out = new FileOutputStream(new File(ckUploadPath)); 
			out.write(bytes); 
			out.flush(); // outputStram에 저장된 데이터를 전송하고 초기화 
			String callback = request.getParameter("CKEditorFuncNum"); 
			printWriter = response.getWriter(); 
			String fileUrl = "/mir9/board/ckImgSubmit?uid=" + uid + "&fileName=" + fileName; // 작성화면 
			// 업로드시 메시지 출력 
			printWriter.println("{\"filename\" : \""+fileName+"\", \"uploaded\" : 1, \"url\":\""+fileUrl+"\"}"); 
			printWriter.flush(); 
			}catch(IOException e){ 
				e.printStackTrace(); 
			} finally { 
				try { 
					if(out != null) { 
						out.close(); 
					} 
					if(printWriter != null) { 
						printWriter.close(); 
					} 
				} catch(IOException e) { 
					e.printStackTrace(); 
				} 
			} return;
					
	}
	
	@RequestMapping(value="ckImgSubmit") 
	public void ckSubmit(@RequestParam(value="uid") String uid , 
						 @RequestParam(value="fileName") String fileName , 
						 HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{ 
		//서버에 저장된 이미지 경로 
		String path = "C:\\Users\\user\\ckImageFile"; 
		
		String sDirPath = path + uid + "_" + fileName; 
		
		File imgFile = new File(sDirPath); 
		//사진 이미지 찾지 못하는 경우 예외처리로 빈 이미지 파일을 설정한다. 
		
		if(imgFile.isFile()){ 
			byte[] buf = new byte[1024]; 
			int readByte = 0; 
			int length = 0; 
			byte[] imgBuf = null; 
			FileInputStream fileInputStream = null; 
			ByteArrayOutputStream outputStream = null; 
			ServletOutputStream out = null; 
			
			try{ 
				fileInputStream = new FileInputStream(imgFile); 
				outputStream = new ByteArrayOutputStream(); 
				out = response.getOutputStream(); 
				
				while((readByte = fileInputStream.read(buf)) != -1){ 
					outputStream.write(buf, 0, readByte); 
				} 
				
				imgBuf = outputStream.toByteArray(); 
				length = imgBuf.length; 
				out.write(imgBuf, 0, length); 
				out.flush(); 
			}catch(IOException e){ 
				 
			}finally { 
				outputStream.close(); 
				fileInputStream.close(); 
				out.close(); 
			} 
		} 
	}
	

		
	
}

























