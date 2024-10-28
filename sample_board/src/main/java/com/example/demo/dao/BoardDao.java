package com.example.demo.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.dto.*;
import com.example.demo.entity.*;

@Mapper
public interface BoardDao {
	public int save(Board board);

	public Optional<BoardDto.Read> findById(Long bno);

	@Update("update board set read_cnt=read_cnt+1 where bno=#{bno} and rownum=1")
	public int increaseReadCnt(Long bno);

	public List<BoardDto.BoardList> findAll(Integer startRowNum, Integer endRowNum);
	
	@Select("select count(*) from board")
	public int count();

	public void update(Board entity);

	public void deleteByBno(long bno);
}
