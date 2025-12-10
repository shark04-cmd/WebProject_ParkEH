package utils;

import java.util.Map;

public class BoardPage {
	public static String pagingStr(int totalCount, int pageSize, int blockPage, int pageNum, String reqUrl,
			Map<String, Object> map) {
		String pagingStr = "";

		int totalPages = (int) (Math.ceil((double) totalCount / pageSize));

		int pageTemp = (((pageNum - 1) / blockPage) * blockPage) + 1;

		String queryStr = "";
		if (map.get("searchWord") != null) {
			queryStr += "&searchField=" + map.get("searchField");
			queryStr += "&searchWord=" + map.get("searchWord");
		}

		if (pageTemp != 1) {
			pagingStr += "<a href='" + reqUrl + "?pageNum=1" + queryStr + "'>[첫 페이지]</a>";
			pagingStr += "&nbsp;";
			pagingStr += "<a href='" + reqUrl + "?pageNum=" + (pageTemp - 1) + queryStr + "'>[이전 블록]</a>";
			pagingStr += "&nbsp;";
		}

		int blockCount = 1;
		while (blockCount <= blockPage && pageTemp <= totalPages) {
			if (pageTemp == pageNum) {
				pagingStr += "&nbsp;<span class='current'>" + pageTemp + "</span>&nbsp;";
			} else {
				pagingStr += "&nbsp;<a href='" + reqUrl + "?pageNum=" + pageTemp + queryStr + "'>" + pageTemp
						+ "</a>&nbsp;";
			}
			pageTemp++;
			blockCount++;
		}

		if (pageTemp <= totalPages) {
			pagingStr += "<a href='" + reqUrl + "?pageNum=" + pageTemp + queryStr + "'>[다음 블록]</a>";
			pagingStr += "&nbsp;";
			pagingStr += "<a href='" + reqUrl + "?pageNum=" + totalPages + queryStr + "'>[마지막 페이지]</a>";
		}

		return pagingStr;
	}
}