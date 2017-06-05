package dao;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.PageVO;
import util.CallException;
import util.TextReplace;

public class UrlParserDAO {

	private TextReplace tr = new TextReplace();
	private String totalCount;

	private static final String NAVER = "네이버";
	private static final String DAUM = "다음";

	private static final String BLOG = "블로그";
	private static final String CAFE = "카페";

	private static final Logger logger = Logger.getLogger(UrlParserDAO.class);

	public List<HashMap<String, Object>> getUrlParsing(String[] param)
			throws CallException {

		ArrayList<String> totalListURL = new ArrayList<String>();
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		String siteCode = param[0];
		String isType = param[1];
		String word = param[2];
		String date = param[3];
		String keyWord = param[4];
		String searchUrl = "";

		if (siteCode.equals("CC0005")) { // 네이버
			if (isType.equals("CC0007")) { // 블로그 URL 설정
				// searchUrl = "http://section.blog.naver.com/sub/SearchBlog.nhn?type=post&option.keyword="
				//		+ word + "&term=period&option.startDate=" + date +
				//		"&option.endDate=" + date+ "&option.page.currentPage=";
				searchUrl = "https://search.naver.com/search.naver?where=post&sm=tab_jum&ie=utf8&query="
						+ word
						+ "&ie=utf8&st=sim&sm=tab_opt&date_from="
						+ date
						+ "&date_to="
						+ date
						+ "&date_option=8&srchby=all&dup_remove=1&post_blogurl=&post_blogurl_without=";
			} else if (isType.equals("CC0008")) { // 카페 URL 설정
				// searchUrl =
				// "http://section.cafe.naver.com/ArticleSearch.nhn?query=" +
				// word + "&period=" + date + "&period=" + date + "&page=";
				searchUrl = "https://search.naver.com/search.naver?where=article&sm=tab_jum&ie=utf8&query="
						+ word
						+ "&term=period&option.startDate="
						+ date
						+ "&option.endDate="
						+ date
						+ "&option.page.currentPage=";
			}
		} else if (siteCode.equals("CC0006")) { // 다음
			if (isType.equals("CC0007")) { // 블로그 URL 설정
				searchUrl = "http://search.daum.net/search?q="
						+ word
						+ "&w=blog&m=board&f=section&SA=daumsec&lpp=10&nil_src=blog&period=u&sd="
						+ date + "000000&ed=" + date + "235959&page=";
			} else if (isType.equals("CC0008")) { // 카페 URL 설정
				searchUrl = "http://search.daum.net/search?q="
						+ word
						+ "&w=cafe&m=board&f=section&SA=daumsec&lpp=10&nil_src=blog&period=u&sd="
						+ date + "000000&ed=" + date + "235959&p=";
			}
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("siteCode", siteCode);
		map.put("isType", isType);
		map.put("word", word);
		map.put("keyWord", keyWord);
		map.put("date", date);
		map.put("url", searchUrl);
		map.put("page", 1);

		List<String> startUrlList = getUrlList(map);

		if (startUrlList != null) {
			for (String url : startUrlList) {
				totalListURL.add(url);
			}
		}

		int conCount = 0;

		try {
			if (totalCount != null) {
				conCount = Integer.parseInt(totalCount);
			}
		} catch (NumberFormatException e) {
			String msg = "컨텐츠 갯수 구하기 애러";
			throw new CallException(e, msg);
		}

		PageVO pageVO = new PageVO(10, 1, conCount);
		int totalPage = pageVO.getTotalPage();

		for (int i = 2; i <= totalPage; i++) {
			map.put("page", i);

			List<String> urlList = getUrlList(map);

			if (urlList != null) {
				for (String url : urlList) {
					totalListURL.add(url);
				}
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		logMSG(siteCode, isType, date, keyWord, totalListURL.size());

		if (totalListURL.size() > 0) {
			map = new HashMap<String, Object>();
			map.put("totalListURL", totalListURL);
			map.put("totalCount", conCount);
			result.add(map);
		} else {
			result = null;
		}

		return result;
	}

	// 네이버 블로그 상세내용 파싱
	@SuppressWarnings("static-access")
	public String getSearchParsingString(String url) {
		String result = "";
		String title = "";
		String content = "";
		Document docDetail;

		try {
			docDetail = Jsoup.connect(url)
					.header("Accept-Encoding", "gzip, deflate")
					.timeout(20 * 5000).userAgent("Chrome")
					.ignoreContentType(true).get();
		} catch (IOException e) {
			return "no_save,no_save";
		}

		// 타이틀 가져오기
		Elements title_b = docDetail.select(".htitle");
		// System.out.println("doc_d : "+doc_d);
		if (title_b.size() > 0) {
			Element title_s = title_b.get(0);
			title = title_s.text();

			docDetail.outputSettings().charset().forName("UTF-8");
			// 본문가져오기
			Elements content_b = docDetail.select(".post-view.pcol2");

			content = tr.textReplaceALL(content_b.toString());
			title = tr.textReplaceALL(title);

			result = "\"" + title + content + "\"";
			// System.out.println(result);

		} else {
			title_b = docDetail.select(".se_textarea");

			if (title_b.size() > 0) {
				Element title_s = title_b.get(0);
				title = title_s.text();

				docDetail.outputSettings().charset().forName("UTF-8");

				Elements content_b = docDetail
						.select(".se_component_wrap.sect_dsc.__se_component_area");

				content = tr.textReplaceALL(content_b.toString());
				title = tr.textReplaceALL(title);

				result = "\"" + title + content + "\"";

				// System.out.println(result);
			} else {
				return "\"" + "no-data" + "\"";
			}

		}

		return result;
	}

	/**
	 * URL 리스트 받아오기
	 */
	private List<String> getUrlList(HashMap<String, Object> map) {
		Document doc;
		List<String> result = new ArrayList<String>();

		String siteCode = (String) map.get("siteCode");
		String isType = (String) map.get("isType");
		//String word = (String) map.get("word");
		//String keyWord = (String) map.get("keyWord");
		//String date = (String) map.get("date");
		String url = (String) map.get("url");
		int page = (Integer) map.get("page");

		if (siteCode.equals("CC0005")) {

			try {
				doc = Jsoup.connect(url+page).timeout(20 * 5000).userAgent("Chrome").ignoreContentType(true).get();
			} catch (IOException e) {
				//result.add(new Search(keyWord, "0", "0", "null", "no_save,no_save", "0", siteCode));
				return null;
			}

			// 네이버 블로그
			if (isType.equals("CC0007")) {

			
				//String totalCountString = doc.select(".several_post").toString();
				String totalCountString = doc.select(".title_num").toString();

				totalCount = "0";
				
				if (totalCountString.indexOf("/") == -1) {
					return null;
				} else {
					totalCount = totalCountString.substring(totalCountString.indexOf(" /") + 2, totalCountString.indexOf("건"));
					totalCount = extractNumeral(totalCount);
				}

				//<span class="title_num">1-1 / 1건</span>
				Elements elList = doc.select(".sh_blog_top");
				
				for(Element el : elList) {
					Elements aList  = el.select("dl dt a");
					//System.out.println(el.select("dl dt a"));
					String detailUrl = aList.attr("href");
					result.add(detailUrl);
				}
			

			/*
				String totalCountString = doc.select(".several_post").toString();
				totalCount = "0";
				
				// 포스팅 갯수 구해오기
				if (totalCountString.indexOf("<em>") == -1) {
					return null;
				} else {
					totalCount = totalCountString.substring(totalCountString.indexOf("<em>") + 4, totalCountString.indexOf("</em>") - 1);
					totalCount = extractNumeral(totalCount);
				}
				
				Elements elList = doc.select(".list_content");
				
				for(Element el : elList) {
					
					Elements divList = el.getElementsByTag("div");
					
					for(Element div : divList) {
												
						if(div.className().equals("")){							
							String detailUrl = div.child(0).attributes().get("href");
							result.add(detailUrl);
						}
						
						//						http://blog.naver.com/rmswk1014?Redirect=Log&logNo=220985802080&from=section
						
					}
					
				}
			*/
				
			// 네이버 카페	
			} else if (isType.equals("CC0008")) {
				Elements elList = doc.select(".txt_block");
				Elements num = doc.select(".num");
				
				String totalCountString = num.toString();
				totalCount = "0";
				
				// 포스팅 갯수 구해오기
				if(totalCountString.indexOf("SearchTotalCount")== -1){
					return null;
				}else{
					totalCount = num.get(0).getElementById("SearchTotalCount").text();
					totalCount = extractNumeral(totalCount);
				}
				
				for(Element el : elList){
					
					Elements divs = el.getElementsByTag("dd");
					
					for(Element div : divs){
						
						if(div.className().equals("txt_block")){
							String detailUrl = div.child(0).attributes().get("href");
							result.add(detailUrl);
						}
						
					}
					
				}
			}
		
		// 다음 일 경우	
		} else if(siteCode.equals("CC0006")){
			
			try {
				doc = getUrl(url+page);
			} catch (IOException e) {
				return null;
			}
			
			// 다음 블로그
			if(isType.equals("CC0007")) {
				//검색 카운트를 가져온다 '.several_post' 기준에서 EM 테그 사이에 있는 숫자를 추출
				Elements totalCountSize = doc.select(".coll_tit").select(".mg_expander");
				
				totalCount = "0";
				
				if( totalCountSize.size() != 2){
					return null;
				}else{
					//String totalCountString = totalCountSize.get(1).text();
					//totalCount = totalCountString.substring(totalCountString.indexOf("/")+2, totalCountString.indexOf("건"));
					totalCount = "0";
					totalCount = extractNumeral(totalCount);
				}
				
				Elements elList = doc.select(".cont_inner");
				
				for(Element el : elList) {
					
					Elements divList = el.select(".clear");
					
					for(Element div : divList){
						
						Elements di = div.select(".f_url");
						
						for(Element diUrl : di) {
							String detailUrl = diUrl.attributes().get("href");
							result.add(detailUrl);
						}
						
					}
					
				}
			// 다음 카페	
			} else if (isType.equals("CC0008")) {
				
				Elements totalCountSize = doc.select(".coll_tit").select(".mg_expander");
				
				totalCount = "0";
				
				if( totalCountSize.size() != 2){
					return null;
				} else {			
					//String totalCountString = totalCountSize.get(1).text();
					//totalCount = totalCountString.substring(totalCountString.indexOf("/")+1, totalCountString.indexOf("건"));
					totalCount = "0";
					totalCount = extractNumeral(totalCount);
				}
				
				Elements elList = doc.select(".cont_inner");
				
				for(Element el : elList) {
					
					Elements divList = el.select(".f_l");
					
					for(Element div : divList){
						
						Elements di = div.select(".f_url");
						
						for(Element diUrl : di){					
							String detailUrl = diUrl.attributes().get("href");
							result.add(detailUrl);		
						}
						
					}
				}
			}
			
		}

		return result;
	}

	private String extractNumeral(String totalCount) {
		String numeral = "";
		String temp = "";

		if (totalCount == null) {
			numeral = null;
		} else {
			for (int i = 0; i < totalCount.length(); i++) {
				temp = totalCount.substring(i, i + 1);
				if (Character.isDigit(totalCount.charAt(i))) {
					numeral += temp;
				}
			}
		}
		return numeral;
	}

	public String getUrlConverter(String url) {
		String result = "";

		String id = "";
		String page_no = "";

		if (url != null && url.length() > 0) {
			String naver_cp1 = "blog.naver.com";
			String naver_cp2 = ".blog.me";
			String naver_cp3 = "kr/";
			String naver_cp4 = "com/";
			String naver_cp5 = url.substring(7, url.length());

			String cp1 = url.substring(7, 21);

			Boolean sp3 = url.substring(url.indexOf(naver_cp3) + 6,
					url.length()).length() <= 12;
			Boolean sp4 = url.substring(url.indexOf(naver_cp4) + 4,
					url.length()).length() <= 12;
			Boolean sp5 = naver_cp5.substring(naver_cp5.indexOf("/") + 1,
					naver_cp5.length()).length() <= 12;

			if (naver_cp1.equals(cp1)) { // 네이버 블러그 형태 1
				int id_end = url.indexOf("?");
				id = url.substring(22, id_end);

				int no_start = url.indexOf("&logNo=") + 7;
				int no_end = url.indexOf("&from");
				page_no = url.substring(no_start, no_end);

			} else if (url.indexOf(naver_cp2) != -1) { // 네이버 블러그 형태 2

				id = url.substring(7, url.indexOf(naver_cp2));

				int no_start = url.indexOf(naver_cp2) + 9;
				int no_end = url.length();
				page_no = url.substring(no_start, no_end);

			} else if (sp3) { // 네이버 블러그 형태 3

				page_no = url.substring(url.indexOf(naver_cp3) + 3,
						url.length());

				id = getId(url);

			} else if (sp4) { // 네이버 블러그 형태 4

				page_no = url.substring(url.indexOf(naver_cp4) + 4,
						url.length());

				id = getId(url);

			} else if (sp5) { // 네이버 블러그 형태 5

				page_no = naver_cp5.substring(naver_cp5.indexOf("/") + 1,
						naver_cp5.length());

				id = getId(url);

			} else {// 네이버 아님 패턴 추가 예정
				return "no_naver";
			}
			if (id.equals("no_naver")) {
				return "no_naver";
			}

			result = "http://blog.naver.com/PostView.nhn?blogId=" + id
					+ "&logNo=" + page_no;
			
			
		}

		return result;
	}

	public String getId(String url) {
		String result = "";
		Document docId;

		try {
			docId = Jsoup.connect(url).timeout(10 * 5000).userAgent("Chrome")
					.ignoreContentType(true).get();
		} catch (IOException e) {
			return "no_naver";
		}

		Elements test = docId.getElementsByTag("frame");

		if (test.size() == 0) {
			return "no_naver";
		}

		String id = test.get(0).attr("id");
		String result_a = test.get(0).attr("src");

		if (id.equals("screenFrame")) {
			String result_b = result_a
					.substring(result_a.indexOf("naver.com/") + 10);
			result = result_b.substring(0, result_b.indexOf("/"));
		} else if (id.equals("mainFrame")) {
			String result_b = result_a
					.substring(result_a.indexOf("blogId=") + 7);
			result = result_b.substring(0, result_b.indexOf("&logNo"));
		} else {
			result = "no_naver";
		}

		return result;
	}

	// 다음 블로그 파싱
	public String getSearchParsingDaum(String url) {
		String result = "";
		String title = "";
		String content = "";
		String d_url = "";
		Document doc;

		d_url = "http://m.blog.daum.net/" + url.substring(21, url.length());

		try {
			doc = Jsoup.connect(d_url).timeout(20 * 5000).userAgent("Chrome")
					.ignoreContentType(true).get();

		} catch (IOException e) {
			return "no_save,no_save";
		}

		// 타이틀 가져오기
		Elements title_b = doc.select(".article_info");

		if (title_b.size() == 0) {
			return "del_blog,del_blog";
		}

		title = tr.textReplaceALL(title_b.get(0).child(0).text());
		content = tr.textReplaceALL(doc.select(".small").text());

		result = "\"" + title + content + "\"";

		return result;
	}

	// 다음 카페
	public String getSearchParsingDaumC(String url) {
		String result = "";
		String title = "";
		String content = "";
		String d_url = "";
		Document doc;

		d_url = "http://m.cafe.daum.net/" + url.substring(21, url.length());

		try {
			doc = Jsoup.connect(d_url).timeout(20 * 5000).userAgent("Chrome")
					.ignoreContentType(true).get();
		} catch (IOException e) {
			// e.printStackTrace();
			return "no_save,no_save";
		}

		// 타이틀 가져오기
		Elements title_b = doc.getElementsByTag("h3");

		if (title_b.size() == 0) {
			return "del_cafe,del_cafe";
		}

		title = tr.textReplaceALL(title_b.get(0).text());
		content = tr.textReplaceALL(doc.getElementById("article").text());

		result = "\"" + title + content + "\"";

		return result;
	}

	// 다음에 URL접근을 피하는 방법
	public Document getUrl(String d_url) throws IOException {

		URL url_con = new URL(d_url);
		HttpURLConnection huc = (HttpURLConnection) url_con.openConnection();
		huc.setRequestMethod("GET");
		huc.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		huc.setRequestProperty("Accept-Charset",
				"windows-949,utf-8;q=0.7,*;q=0.3");
		huc.setRequestProperty("Accept-Language",
				"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
		huc.setRequestProperty("Connection", "keep-alive");
		huc.setRequestProperty(
				"Cookie",
				"uvkey=UvXxjq97@5EAADrliQsAAAEP; captcha_pass=y; ODT=IIMZ_NKSZ_IVRZ_CCBZ_DICZ_BR1Z_GG2Z_; DDT=SNPZ_NNSZ_BRDA_1DVZ_WSAZ_FGKZ_VO2Z_LB2Z_MS2Z_; DTQUERY=%EB%AF%B8%EB%8B%88%EB%B2%84%EC%8A%A4; TIARA=MHZRg2Qt.BJuY5prIK657ygoyQZ6ukpYLaoq5C-_3ocIpGD3vagvqOM22PRcAmF2LjW94G.z8KqNEP4H44ulLTYIt5MJETlJ; suggest=on");
		huc.setRequestProperty("Host", "search.daum.net");
		huc.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36");

		InputStream is = huc.getInputStream();
		StringBuffer sb = new StringBuffer();
		// byte[] b = new byte[1024];

		BufferedInputStream bis = new BufferedInputStream(is);

		BufferedReader br = null;

		String line;
		try {
			br = new BufferedReader(new InputStreamReader(bis, "utf-8"));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();

			logger.error("e.toString() : " + e.toString());
			logger.error("e.getMessage() : " + e.getMessage());
			logger.error("e.getStackTrace() : " + e.getStackTrace());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e.toString() : " + e.toString());
			logger.error("e.getMessage() : " + e.getMessage());
			logger.error("e.getStackTrace() : " + e.getStackTrace());
		} finally {
			if (br != null) {
				try {
					br.close();
					is.close();
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return Jsoup.parse(sb.toString());
	}

	// TEST 중
	public JSONObject getNaverCafeUrl(String query, String date, int page)
			throws IOException {
		String url = "http://section.cafe.naver.com/ArticleSearchAjax.nhn";

		query = URLEncoder.encode(query, "UTF-8");

		url = url + "?query=" + query + "&period=" + date + "&period=" + date
				+ "&page=" + page;

		URL url_con = new URL(url);
		HttpURLConnection huc = (HttpURLConnection) url_con.openConnection();
		huc.setRequestMethod("POST");
		huc.setRequestProperty("Accept", "*/*");
		/*
		 * huc.setRequestProperty("Accept-Charset",
		 * "windows-949,utf-8;q=0.7,*;q=0.3");
		 */
		huc.setRequestProperty("Accept-Language",
				"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
		huc.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		huc.setRequestProperty("Connection", "keep-alive");
		huc.setRequestProperty(
				"Cookie",
				"npic=IsCB620sESJjlsLOAqA/ln7qw9q01RwW2T2uF1AlH1vJO8CXNYha3j7eIjxjxD2/CA==; NNB=PDY26INIWVDVO; NaverSuggestUse=use%26unuse; nx_ssl=2; PCID=14644198441478514586552; _ga=GA1.2.1629010211.1464419845; nid_iplevel=1; nid_inf=-705970399; NID_AUT=eM9CV+xImOFswsL3yPZIpl2bu3b5+9oMPUgxkEaxjWxWpO9PzhNMXQHC2La/9Fuq; page_uid=xCf+0loiqxhss7iIBLsssssss4K-500716; NID_SES=AAABWCsK4R5l60rjyTdb+EYzW291rfIYlDdldTU23EAF4cZukk980q2/+EwlkzCz6yaXNfrOtG7d/X7PimjKc3DOw9FMw7Hj/03L0yR0hPwKnpc4WFUASrCmDYTN4BhKY1K1x4v2nHt7APyKkYbhmNltfZ21ySNMtUSy8Yz0u9JMciDE+gTsnI5uXtDI4QUzNdPhuH80+ncY/TH6em7+tth3U+xXpzoPoRFUHyYE8mQi2u1quuOyPhH4GfyM6wEA8st449ZUhIxygonmufA1dOdaxQFep8wRd3eN02Wm1WHJacvFoYm9Ft5txSPgTzsQYJPrgUYmF2NcerWZxx1hVkRy2GfTdbLDxOx31HHVEP3en7oBhe7n3jlTua/q+S+siuhlMIzzHX6yDH8ASpIQwTD3slVuwkKS790ZRyvf8K/Oh8MHHQVkE7kl34c2w5U7eDDA+oTRBeogkbkP+yy283vpIfk=; ncu=92bb46766e3b6cd0dd1b4a207ab2eff964; JSESSIONID=044DA095F381F48ACCB7C886145E1672");
		huc.setRequestProperty("Host", "section.cafe.naver.com");
		huc.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36");
		huc.setDoOutput(true);

		InputStream is = huc.getInputStream();
		StringBuffer sb = new StringBuffer();
		// byte[] b = new byte[1024];

		BufferedInputStream bis = new BufferedInputStream(is);

		BufferedReader br = null;

		String line;
		try {
			br = new BufferedReader(new InputStreamReader(bis, "utf-8"));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();

			logger.error("e.toString() : " + e.toString());
			logger.error("e.getMessage() : " + e.getMessage());
			logger.error("e.getStackTrace() : " + e.getStackTrace());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e.toString() : " + e.toString());
			logger.error("e.getMessage() : " + e.getMessage());
			logger.error("e.getStackTrace() : " + e.getStackTrace());
		} finally {
			if (br != null) {
				try {
					br.close();
					is.close();
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// System.out.println(sb.toString());
		JSONObject json = new JSONObject(sb.toString());

		return json;
	}

	private void logMSG(String siteCode, String isType, String date,
			String keyWord, int count) {
		if (siteCode.equals("CC0005")) {
			siteCode = NAVER;
		} else if (siteCode.equals("CC0006")) {
			siteCode = DAUM;
		}

		if (isType.equals("CC0007")) {
			isType = BLOG;
		} else if (isType.equals("CC0008")) {
			isType = CAFE;
		}

		System.out.println(siteCode + " " + isType + "," + date + "," + keyWord
				+ "," + count + "건");
		logger.debug(siteCode + " " + isType + "," + date + "," + keyWord + ","
				+ count + "건");

		if (count == 0) {
			System.out.println("수집할 데이터가 없습니다.");
			logger.debug("수집할 데이터가 없습니다.");
		}

	}

}
