package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BoardDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	//DB 연결
	public BoardDAO() {

	
		try {
			String url = "jdbc:mysql://localhost:3306/pgh";
			String id = "root";
			String pw = "1234";
			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url,id,pw);
			System.out.println("db 연동 성공");
			
		}catch(Exception e) {
			System.out.println("db 연동 실패");
			e.printStackTrace();
		}
	
	}
	
	
	// 글 등록
	public int write(String title, String userID, String content) {
		
		String sql = "insert into board(bno, title,content,writer) values(?,?,?,?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, next() );
			pstmt.setString(2, title);
			pstmt.setString(3, content);
			pstmt.setString(4, userID);
			System.out.println("글 등록 성공");
			return pstmt.executeUpdate();
		}catch(Exception e) {
			System.out.println("글쓰기 실패");
			e.printStackTrace();
		}finally {
			try {
			if(pstmt !=null) pstmt.close();
			if(conn !=null) conn.close();

			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		return -1; // DB 오류
	}
	
	// 게시글 번호 출력하기 위한 메소드
	public int next() {
		String sql = "select bno from board order by bno desc";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) +1;
			}
			
			return 1; // 게시글이 하나도 없어서 첫번째 게시물인 경우
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return -1; // 데이터베이스 오류
	}
	
	
	// 게시판 글 목록
	public ArrayList<Board> list(int pagenum){
		
		String sql = "select * from board where bno < ? order by bno  desc limit 10 ;";
		ArrayList<Board> boardList = new ArrayList<Board>();		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,next() - (pagenum-1)*10);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Board board = new Board();
				board.setBno(rs.getInt(1));
				board.setTitle(rs.getString(2));
				board.setContent(rs.getString(3));
				board.setWriter(rs.getString(4));
				board.setCreateTime(rs.getTimestamp(5));
				
				boardList.add(board);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
			if(pstmt !=null) pstmt.close();
			if(conn !=null) conn.close();

			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return boardList;
	}
	
	
	
	// 페이지를 게시글 10개 단위로 끊으니깐, 만약에 10개가 안되면 다음 버튼이 없어야함
	public boolean nextPage(int pagenum) {
		String sql = "select * from board where bno < ?";		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,next() - (pagenum-1)*10);
			rs = pstmt.executeQuery();
			System.out.println("확인!!!");
			if(rs.next()) {
				return true; // true니깐  다음 페이지로 넘어갈 수 있음 
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false; 
	}
	
	//글 상세보기 페이지
	public Board detail(int bno) {
		
		String sql = "select * from  board where bno = ?";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Board board = new Board();
				board.setBno(rs.getInt(1));
				board.setTitle(rs.getString(2));
				board.setContent(rs.getString(3));
				board.setWriter(rs.getString(4));
				board.setCreateTime(rs.getTimestamp(5));
				
				return board;
			}
			
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//글 수정하기
	public int update(int bno, String title, String content) {
		String sql = "update board set title = ? , content =?  where bno = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, bno);
			return pstmt.executeUpdate();
		}catch(Exception e) {
			
			e.printStackTrace();
		}finally {
			try {
			if(pstmt !=null) pstmt.close();
			if(conn !=null) conn.close();

			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return -1;
	}
	
	// 게시글 직접 삭제
	public int delete(int bno) {
		String sql =  "delete from board where bno =?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			return pstmt.executeUpdate();
		}catch(Exception e) {
			
			e.printStackTrace();
		}finally {
			try {
			if(pstmt !=null) pstmt.close();
			if(conn !=null) conn.close();

			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return -1;
		
	}
	
	//회원탈퇴시 게시글 삭제
	public int alldelete(String userID) {
		String sql = "delete from board where writer =?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			return pstmt.executeUpdate();
		}catch(Exception e) {
			
			e.printStackTrace();
		}finally {
			try {
			if(pstmt !=null) pstmt.close();
			if(conn !=null) conn.close();

			}catch(Exception e) {
				e.printStackTrace();
			}
	}
		return -1;

}
}
