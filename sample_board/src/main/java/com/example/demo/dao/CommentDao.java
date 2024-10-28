package com.example.demo.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.dto.*;

@Mapper
public interface CommentDao {
	@Insert("insert into comments(cno, content, writer, write_time, bno) values(comments_seq.nextval,#{content},#{writer},sysdate,#{bno})")
	public Integer save(Long bno, String content, String writer);

	@Select("select cno, content, writer, to_char(write_time, 'YYYY-MM-DD HH24:MI:SS') as writeTime from comments where bno=#{bno} order by cno desc")
	public List<CommentDto.Read> findByBno(Long bno);
	
	@Delete("delete from comments where cno=#{cno} and writer=#{writer} and rownum=1")
	public void deleteByCnoAndWriter(Long cno, String writer);
}
