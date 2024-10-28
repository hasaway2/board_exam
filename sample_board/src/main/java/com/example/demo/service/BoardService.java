package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.*;

@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;
	@Autowired
	private MemberReadBoardDao memberReadBoardDao;
	@Autowired
	private MemberBoardDao memberBoardDao;
	
	/*
	 * boardDao에서 sequence로 일련번호를 생성해서 글번호로 사용할 것이다
	 *      insert into board(bno......) values(board_seq.nextval....)
	 *      위처럼 사용하면 boardDao에서 시퀀스를 생성해서 혼자 사용하고 끝
	 * 그런데 boardDao에서 생성한 시퀀스를 서비스에서 사용하고 싶다면?
	 * 		마이바티스의 경우 selectKey를 사용한다
	 * 		JPA의 경우 save() 함수는 엔티티를 리턴 -> 엔티티에서 꺼내면 된다
	 */
	public Long write(BoardDto.Create dto, String loginId) {
		Board board = dto.toEntity(loginId);
		boardDao.save(board);
		return board.getBno();
	}

	public BoardDto.Read read(Long bno, String loginId) {
		BoardDto.Read dto = boardDao.findById(bno).orElseThrow(()->new JobFailException("글을 찾을 수 없습니다"));
		// 3. 삭제된 글이라면 본문 내용을 삭제된 글입니다로 변경
		if(dto.getIsDeleted()==true)
			return dto.글본문삭제();
				
		// 1. 비로그인이거나 내가 작성한 글이면 조회수 변경 없음
		if(loginId==null || dto.getWriter().equals(loginId))
			return dto;
	
		// 2. 로그인했고 남이 작성한 글인 경우 조회수 증가 작업 시작
		//    (이미 읽은 글이 아닌 경우에만)
		boolean 이미읽었는가 = memberReadBoardDao.existsByBnoAndUsername(bno, loginId);
		if(이미읽었는가==false) {
			memberReadBoardDao.save(bno, loginId);
			boardDao.increaseReadCnt(bno);
			dto.조회수증가();
		}
		
		return dto;
	}
	
	@Value("10")
	private int PAGE_SIZE;
	@Value("5")
	private int BLOCK_SIZE;
	
	public BoardDto.Page findAll(Integer pageno) {
		int countOfBoard = boardDao.count();
		
		int numberOfPage = (countOfBoard-1)/10 + 1;
		
		int prev = (pageno-1)/BLOCK_SIZE * BLOCK_SIZE;
		int start = prev+1;
		int end = prev + BLOCK_SIZE;
		int next = end + 1;
		
		if(end>=numberOfPage) {
			end = numberOfPage;
			next = 0;
		}
		
		// 1-1, 2 11 3 21
		int startRowNum = (pageno-1)*PAGE_SIZE + 1;
		int endRowNum = startRowNum + PAGE_SIZE - 1;
		List<BoardDto.BoardList> boards = boardDao.findAll(startRowNum, endRowNum);
		
		return new BoardDto.Page(prev, start, end, next, pageno, boards);
	}
	
	public void update(BoardDto.Update dto, String loginId) {
		BoardDto.Read board = boardDao.findById(dto.getBno()).orElseThrow(()->new JobFailException("글을 찾을 수 없습니다"));
		if (!board.getWriter().equals(loginId))
			throw new JobFailException("잘못된 작업입니다");
		boardDao.update(dto.toEntity());
	}

	public void delete(long bno, String loginId) {
		BoardDto.Read board = boardDao.findById(bno).orElseThrow(()->new JobFailException("글을 찾을 수 없습니다"));
		if (!board.getWriter().equals(loginId))
			throw new JobFailException("잘못된 작업입니다");;
		boardDao.deleteByBno(bno);
	}
	
	public int 추천(Long bno, String loginId) {
		BoardDto.Read board = boardDao.findById(bno).orElseThrow(() -> new JobFailException("글을 찾을 수 없습니다"));
		if (board.getWriter().equals(loginId))
			return board.getGoodCnt();
		boolean result = memberBoardDao.existsByUsernameAndBno(loginId, bno); 	
		if (result==true)
			return board.getGoodCnt();
		memberBoardDao.save(loginId, bno);
		boardDao.update(Board.builder().bno(bno).goodCnt(1).build());
		return board.getGoodCnt() + 1;
	}

	public int 비추(Long bno, String loginId) {
		BoardDto.Read board = boardDao.findById(bno).orElseThrow(() -> new JobFailException("글을 찾을 수 없습니다"));
		if (board.getWriter().equals(loginId))
			return board.getBadCnt();
		boolean result = memberBoardDao.existsByUsernameAndBno(loginId, bno); 	
		if (result==true)
			return board.getBadCnt();
		memberBoardDao.save(loginId, bno);
		boardDao.update(Board.builder().bno(bno).badCnt(1).build());
		return board.getBadCnt() + 1;
	}
}








