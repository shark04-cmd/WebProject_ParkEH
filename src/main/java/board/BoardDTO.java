package board;

import java.sql.Timestamp;

public class BoardDTO {
    
    private int num;        // 글번호 (PK)
    private String writer;  // 작성자 (member.id와 연결)
    private String subject; // 제목
    private String content; // 내용
    private int readcount;  // 조회수
    private Timestamp regdate; // 작성일
    private String type;    // 게시판 타입 (free, qna, data 등)
    
    // Getter 및 Setter 메서드
    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }
    public String getWriter() { return writer; }
    public void setWriter(String writer) { this.writer = writer; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getReadcount() { return readcount; }
    public void setReadcount(int readcount) { this.readcount = readcount; }
    public Timestamp getRegdate() { return regdate; }
    public void setRegdate(Timestamp regdate) { this.regdate = regdate; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}