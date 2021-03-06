package com.naedam.mir9.schedule.model.service;

import java.util.Map;

import com.naedam.mir9.schedule.model.vo.Schedule;

public interface ScheduleService {
	
	//일정 등록
	public int addSchedule(Schedule schedule) throws Exception;
	
	//일정 리스트
	public Map<String, Object> getScheduleList(Map<String, Object> map) throws Exception;
	
	//일정 데이터
	public int getSchedule(int scheduleNo) throws Exception;
	
	//일정 수정
	public int updateSchedule(Schedule schedule) throws Exception;
	
	//일정 삭제
	public void deleteSchedule(int scheduleNo) throws Exception;
	
}
