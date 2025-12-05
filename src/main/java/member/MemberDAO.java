package member; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {

    // JNDI를 이용한 DB 연결 메서드
    public Connection getConnection() throws Exception {
        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        DataSource ds = (DataSource) envCtx.lookup("jdbc/webproject_db"); 
        Connection con = ds.getConnection();
        return con;
    }
    
    // 자원 해제 메서드
    private void closeResources(Connection con, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) try { rs.close(); } catch(Exception e) {}
        if (pstmt != null) try { pstmt.close(); } catch(Exception e) {}
        if (con != null) try { con.close(); } catch(Exception e) {}
    }

    // ID 중복 확인 및 로그인 처리를 위한 회원 정보 조회
    public MemberDTO getMember(String id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MemberDTO member = null;
        // Oracle은 기본적으로 대문자 컬럼명을 사용하므로 대문자로 조회
        String sql = "SELECT * FROM member WHERE id = ?"; 

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new MemberDTO();
                // Oracle DB에 맞게 컬럼명을 대문자로 사용
                member.setId(rs.getString("ID")); 
                member.setPass(rs.getString("PASS"));
                member.setName(rs.getString("NAME"));
                member.setEmail(rs.getString("EMAIL"));
                member.setPhoneNum(rs.getString("PHONENUM"));
                member.setRegidate(rs.getDate("REGIDATE"));
            }
        } catch (Exception e) {
            System.err.println("DB 조회 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(con, pstmt, rs);
        }
        return member;
    }
    
    // 회원가입 처리 (데이터 삽입)
    public int insertMember(MemberDTO member) {
        Connection con = null;
        PreparedStatement pstmt = null;
        
        // Oracle DB의 대문자 컬럼명 사용. REGIDATE는 SYSDATE(자동 삽입) 가정.
        String sql = "INSERT INTO member (ID, PASS, NAME, EMAIL, PHONENUM) VALUES (?, ?, ?, ?, ?)"; 
        int result = 0;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getPass());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getPhoneNum());
            
            result = pstmt.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("DB 삽입 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(con, pstmt, null);
        }
        return result;
    }
}