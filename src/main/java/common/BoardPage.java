package common;

public class BoardPage {

	// 게시판 페이지 링크를 HTML 문자열로 반환
	public static String pagingStr(int totalCount, int pageSize, int blockPage, int pageNum, String reqUrl) {
		String pagingStr = "";

		// 1. 전체 페이지 수 계산
		int totalPages = (int) (Math.ceil(((double) totalCount / pageSize)));

		// 2. 현재 페이지 블록의 시작 페이지 번호 계산
		int pageTemp = (((pageNum - 1) / blockPage) * blockPage) + 1;

		// 3. '이전 블록' 바로가기 링크 (첫 블록이 아닐 때만 표시)
		if (pageTemp != 1) {
			pagingStr += "<a href='" + reqUrl + "&pageNum=1'>[첫 페이지]</a>";
			pagingStr += "&nbsp;";
			pagingStr += "<a href='" + reqUrl + "&pageNum=" + (pageTemp - 1) + "'>[이전 블록]</a>";
		}

		// 4. 각 페이지 번호 표시
		int blockCount = 1;
		while (blockCount <= blockPage && pageTemp <= totalPages) {
			if (pageTemp == pageNum) {
				// 현재 페이지는 링크를 제거하고 볼드체로 표시
				pagingStr += "&nbsp;<span class='current-page'>" + pageTemp + "</span>&nbsp;";
			} else {
				// 현재 페이지가 아닌 경우 링크를 포함
				pagingStr += "&nbsp;<a href='" + reqUrl + "&pageNum=" + pageTemp + "'>" + pageTemp + "</a>&nbsp;";
			}
			pageTemp++;
			blockCount++;
		}

		// 5. '다음 블록' 바로가기 링크 (마지막 페이지 블록이 아닐 때만 표시)
		if (pageTemp <= totalPages) {
			pagingStr += "<a href='" + reqUrl + "&pageNum=" + pageTemp + "'>[다음 블록]</a>";
			pagingStr += "&nbsp;";
			pagingStr += "<a href='" + reqUrl + "&pageNum=" + totalPages + "'>[마지막 페이지]</a>";
		}

		return pagingStr;
	}
}