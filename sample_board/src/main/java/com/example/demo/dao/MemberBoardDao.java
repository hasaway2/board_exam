package com.example.demo.dao;

import org.apache.ibatis.annotations.*;

@Mapper
public interface MemberBoardDao {
	@Insert("insert into member_board values(#{username}, #{bno}) ")
	public int save(String username, Long bno);

	@Select("select count(*) from member_board where username=#{username} and bno=#{bno} and rownum=1")
	public boolean existsByUsernameAndBno(String username, Long bno);
}
