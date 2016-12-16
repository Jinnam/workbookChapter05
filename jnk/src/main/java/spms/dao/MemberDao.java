package spms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import spms.vo.Member;

public class MemberDao {
	Connection connection;
	public void setConnection(Connection connection){
		this.connection = connection;
	}
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	
	public int insert(Member member) throws Exception{
		int rowCount = 0;
		try{
			pstmt = connection.prepareStatement(
					"INSERT INTO MEMBERS(EMAIL,PWD,MNAME,CRE_DATE,MOD_DATE)"
					+ " VALUES (?,?,?,NOW(),NOW())");
			pstmt.setString(1, member.getEmail());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getName());
			rowCount = pstmt.executeUpdate();
		}catch(Exception e){
			throw e;
		}finally{
			try {if (pstmt != null) pstmt.close();} catch(Exception e) {}
		}
		
		return rowCount;
	}
	
	
	
	public int delete(int no) throws Exception{
		int rowCount=0;
		try{
			stmt = connection.createStatement();
			rowCount=stmt.executeUpdate(
					"DELETE FROM MEMBERS WHERE MNO=" +no);
		}catch(Exception e){
			throw e;
		}finally{
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
		}
		
		return rowCount;
	}
	
	
	public Member selectOne(int no) throws Exception{
		Member member = new Member();
		try{
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
				"SELECT MNO,EMAIL,MNAME,CRE_DATE FROM MEMBERS" + 
				" WHERE MNO=" +no);	
			if (rs.next()) {
				member.setNo(rs.getInt("MNO"))
						.setEmail(rs.getString("EMAIL"))
						.setName(rs.getString("MNAME"))
						.setCreatedDate(rs.getDate("CRE_DATE"));
			} else {
				throw new Exception("해당 번호의 회원을 찾을 수 없습니다.");
			}
		}catch(Exception e){
				throw e;
		}finally{
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
		}
		
		return member;
	}

	
	public int update(Member member) throws Exception{
		int rowCount = 0;
		try{
			pstmt = connection.prepareStatement(
					"UPDATE MEMBERS SET EMAIL=?,MNAME=?,MOD_DATE=now()"
							+ " WHERE MNO=?");
					pstmt.setString(1, member.getEmail());
					pstmt.setString(2, member.getName());
					pstmt.setInt(3, member.getNo());
					rowCount=pstmt.executeUpdate();
		}catch(Exception e){
			throw e;
		}finally{
			try {if (pstmt != null) stmt.close();} catch(Exception e) {}
		}return rowCount;
	}
	
	
	public Member exist(String email, String password) throws Exception{
		Member member = new Member();
		try{
			pstmt = connection.prepareStatement(
					"SELECT MNAME,EMAIL FROM MEMBERS"
					+ " WHERE EMAIL=? AND PWD=?");
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				member	.setEmail(rs.getString("EMAIL"))
						.setName(rs.getString("MNAME"));
		
			} else {
				member = null;
			}

		}catch(Exception e){
			throw e;
		}finally{
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (pstmt != null) pstmt.close();} catch(Exception e) {}
		}
		return member;
	}
	
	
	public List<Member> selectList() throws Exception{
		ArrayList<Member> members = new ArrayList<Member>();
		try{
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT MNO,MNAME,EMAIL,CRE_DATE" + 
					" FROM MEMBERS" +
					" ORDER BY MNO ASC");
			
			// 데이터베이스에서 회원 정보를 가져와 Member에 담는다.
			// 그리고 Member객체를 ArrayList에 추가한다.
			while(rs.next()) {
				members.add(new Member()
							.setNo(rs.getInt("MNO"))
							.setName(rs.getString("MNAME"))
							.setEmail(rs.getString("EMAIL"))
							.setCreatedDate(rs.getDate("CRE_DATE"))	);
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
		}
		return members;
	}
}
