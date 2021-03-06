package com.naedam.mir9.board.model.vo;

import lombok.Data;

@Data
public class Search {
	
	///Field
	private int currentPage;
	private String searchCondition;
	private String searchKeyword;
	private String searchType;
	private String searchFirst;
	private String searchLast;
	private int studyInterest;
	private int studyEndFlag;
	private int searchSort;
	private int pageSize;
	private int endRowNum;
	private int startRowNum;
	private int searchBoardNo;
	
	
	public int getEndRowNum() {
		return getCurrentPage()*getPageSize();
	}
	public int getStartRowNum() {
		return (getCurrentPage()-1)*getPageSize()+1;
	}

}


