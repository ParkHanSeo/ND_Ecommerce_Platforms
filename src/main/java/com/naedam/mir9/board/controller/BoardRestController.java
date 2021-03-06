package com.naedam.mir9.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.naedam.mir9.board.model.service.BoardService;
import com.naedam.mir9.board.model.vo.Board;
import com.naedam.mir9.board.model.vo.BoardComment;
import com.naedam.mir9.board.model.vo.BoardFile;
import com.naedam.mir9.board.model.vo.Post;
import com.naedam.mir9.member.model.vo.Member;

@RestController
@RequestMapping("/board/*")
public class BoardRestController {
	
	@Autowired
	private BoardService boardService;
	
	@PostMapping(value="json/addComment")
	public void addComment(@RequestBody BoardComment boardComment)throws Exception{
		System.out.println("/json/addComment 시작");
	
		Member member = boardService.getMemberData(boardComment.getCommentMemberNo());
		boardComment.setCommentWriter(member.getLastName()+member.getFirstName());
		
		boardService.addComment(boardComment);
	}
	
	@GetMapping(value="json/listBoard")
	public List<Board> listBoard() throws Exception{		
		return boardService.getBoardTitle();
	}
	
	@GetMapping(value="json/getBoardAllData/{boardNo}")
	public Board getBoardAllData(@PathVariable("boardNo") int boardNo, Model model) throws Exception{
		
		System.out.println("getBoardAllData 시작");
		
		System.out.println("데이터 확인 ::: "+boardService.getBoardAllData(boardNo));
		
		return boardService.getBoardAllData(boardNo);
	}
	
	@GetMapping(value="json/getMemberData")
	public Member getMemberData() throws Exception{
		
		System.out.println("getMemberData 시작");
		
		
		//아이디 값이 없기 때문에 일단 memberNo 2번으로
		return boardService.getMemberData(2);
	}
	
	@GetMapping(value="json/getPostData/{postNo}")
	public Post getPostData(@PathVariable("postNo") int postNo)throws Exception{
		
		System.out.println("getPostData 시작");
		
		return boardService.getPostData(postNo);
	}
	
	@GetMapping(value="json/getMemberData2/{memberNo}")
	public Member getMemberData2(@PathVariable("memberNo") int memberNo) throws Exception{
		
		System.out.println("getMemberData2 시작");
		
		return boardService.getMemberData(memberNo);
	}
	
	@GetMapping(value="json/getCommentList/{postNo}")
	public List<BoardComment> getCommentList(@PathVariable("postNo") int postNo) throws Exception{
		
		System.out.println("json/getCommentList 시작");
		
		List<BoardComment> boardComment = boardService.getCommentList(postNo);
		
		return boardComment;
	}
	
	@GetMapping(value="json/postViewCount/{postNo}")
	public void postViewCount(@PathVariable("postNo") int postNo) throws Exception{
		
		System.out.println("json/postViewCount 시작");
		Post post = boardService.getPostData(postNo);
		System.out.println("viewCOunt 확인 ::: "+post.getPostViewCount()+1);
		post.setPostViewCount(post.getPostViewCount()+1);
		boardService.postViewCount(post);
	}
	
	@GetMapping(value="json/postFileCount/{postNo}")
	public void postFileCount(@PathVariable("postNo") int postNo) throws Exception{
		
		System.out.println("json/postFileCount 시작");
		Post post = boardService.getPostData(postNo);
		System.out.println("downloadCount 확인 ::: "+post.getPostDownloadCount()+1);
		post.setPostDownloadCount(post.getPostDownloadCount()+1);
		boardService.postFileCount(post);
	}
	
	@GetMapping(value="json/getPostFile/{postNo}")
	public List<BoardFile> getPostFile(@PathVariable("postNo") int postNo) throws Exception{
		
		System.out.println("json/getPostFile 시작");
		List<BoardFile> boardFile =  boardService.getPostFile(postNo);
		
		return boardFile;
	}
	
	@GetMapping(value="json/deleteFile/{fileNo}")
	public void deleteFile(@PathVariable("fileNo") int fileNo) throws Exception{
		System.out.println("json/deleteFile 시작");
		boardService.deleteFile(fileNo);
	}
	
	@GetMapping(value="json/deleteComment/{commentNo}")
	public void deleteComment(@PathVariable("commentNo") int commentNo) throws Exception{
		System.out.println("json/deleteComment 시작");
		boardService.deleteComment(commentNo);
	}
	
	
	@GetMapping(value="json/downloadImg/{fileNo}")
	public void download(HttpServletResponse response, @PathVariable("fileNo") int fileNo) throws Exception {
		System.out.println("json/downloadImg 시작");
		BoardFile boardFile = boardService.getFileData(fileNo);
		
        // 직접 파일 정보를 변수에 저장해 놨지만, 이 부분이 db에서 읽어왔다고 가정한다.
		String fileName = boardFile.getFileName();
		String saveFileName = "C:\\workspace\\NdMir9\\src\\main\\webapp\\resources\\imgs\\imageBoard\\board"+fileName;
		String contentType = "image/jpg";
        File file = new File(saveFileName);
        long fileLength = file.length();
        
        System.out.println("확인 ==== --- :: "+fileName);
        System.out.println("확인 ==== --- :: "+saveFileName);
        //파일의 크기와 같지 않을 경우 프로그램이 멈추지 않고 계속 실행되거나, 잘못된 정보가 다운로드 될 수 있다.

        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Length", "" + fileLength);
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");

        try(
                FileInputStream fis = new FileInputStream(saveFileName);
                OutputStream out = response.getOutputStream();
        ){
                int readCount = 0;
                byte[] buffer = new byte[1024];
                while((readCount = fis.read(buffer)) != -1){
                    out.write(buffer,0,readCount);
                }
        }catch(Exception ex){
            throw new RuntimeException("file Save Error");
        }
	}
	
	
}







