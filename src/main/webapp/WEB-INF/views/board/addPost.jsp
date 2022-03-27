<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
	
	function fncAddPost(){		
		$("form[name='addPostForm']").attr("method", "POST").attr("action", "/mir9/board/addPost").submit();		
	}
	
	
	
</script>

<div class="modal fade" id="modalContent" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <form name="addPostForm" method="post" enctype="multipart/form-data">
            
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabelPortfolio">게시물 관리</h4>
            </div>
            <div class="modal-body">

            <h4><p class="text-light-blue"><i class="fa fa-fw fa-info-circle"></i> 글 <span id="board_sub_title">수정</span></p></h4>
            <table class="table table-bordered">
            <tbody><tr>
                <td class="menu">작성자</td>
                <td align="left"><input type="text" name="name" id="name" class="form-control input-sm"></td>
            </tr>
                        <tr>
                <td class="menu">휴대전화</td>
                <td align="left"><input type="text" name="phone" id="phone" class="form-control input-sm" style="width:50%;"></td>
            </tr>
            <tr>
                <td class="menu">이메일</td>
                <td align="left"><input type="text" name="email" id="email" class="form-control input-sm" style="width:50%;"></td>
            </tr>
            <tr>
                <td class="menu">제목</td>
                <td align="left">
                <span style="float:left;width:80%;"><input type="text" name="postTitle" id="postTitle" class="form-control input-sm"></span>
                <span>&nbsp;&nbsp;<input type="checkbox" name="is_notice" value="y">공지사항</span>
                </td>
            </tr>
			<tr>
                 <td class="menu">내용</td>
                 <td align="left">
                 	<textarea name="postContents" id="editor" rows="10" cols="80" style="visibility: hidden; display: none;"></textarea>
                 </td>
            </tr>
            <tr>
                <td class="menu">썸네일 파일</td>
                <td align="left">
                <input type="file" name="ThombnailName" id="ThombnailName" class="form-control input-sm" style="width:80%; display:inline;">
                <span id="display_thumbnail" style="display:none;">
                <button type="button" onclick="winOpen('?tpf=common/image_view&amp;file_name=product/'+$('#code').val()+'_1');" class="btn btn-success btn-xs">보기</button>
                <button type="button" onclick="confirmIframeDelete('?tpf=common/image_delete&amp;file_name=product/'+$('#code').val()+'_1&amp;table=product&amp;code='+$('#code').val());" class="btn btn-danger btn-xs">삭제</button>
                </span>
                </td>
            </tr>
            <tr>
                <td class="menu">파일</td>
                <td align="left">
                <p>
                    <span id="file_list"></span>
                </p>

                <p style="padding-top:10px; float:left; width:100%;">
                    <button type="button" class="btn btn-primary btn-xs" onclick="addFile();"><span class="glyphicon glyphicon-plus"></span> 파일추가</button><br>
                </p>
                    <div id="list_file"><input type="file" name="postName" id="postName" class="form-control input-sm" style="width:100%; display:inline; margin-bottom:10px;"></div>
                </td>
            </tr>
            </tbody></table>

            </div>
            <div class="modal-footer">
            <button type="button" onclick="fncAddPost()" class="btn btn-primary">확인</button>&nbsp;&nbsp;&nbsp;
            <button type="button" onclick="reply()" id="display_reply" style="" class="btn btn-danger">답변 페이지로 전환</button>
            </div>
            <input type="hidden" value="${board.boardNo}" name="boardNo">
        </form></div>
    </div>
</div>