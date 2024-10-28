package com.example.demo.dao;

import org.apache.ibatis.annotations.*;

@Mapper
public interface MemberReadBoardDao {
	@Select("select count(*) from member_read_board where bno=#{bno} and username=#{username} and rownum=1")
	public boolean existsByBnoAndUsername(Long bno, String username);
	
	@Insert("insert into member_read_board values(#{username}, #{bno})")
	public int save(Long bno, String username);
}
