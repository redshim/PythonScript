package crawling;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.Search;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SearchListDao {
	
	private Document doc;
	
	private Document doc_d;
	
	private Document doc_dr;
	
	private static final Logger logger = Logger.getLogger(SearchListDao.class);

	
	public SearchListDao(){
		
		
	}

	public List<Search> getSearchParsingList(String[] params,String url, int page){
		
		List<Search> result = new ArrayList<Search>();
		
		if(params[0] != null && params[0].equals("CC0005")){
			
			try {
				doc = Jsoup.connect(url).timeout(20*5000).userAgent("Chrome").ignoreContentType(true).get();
				
			} catch (IOException e) {
				Search search = new Search(); //메타저장 모델 생성
				search.setSiteE(params[0]); // 검색사이트 영어명
				search.setUrl("null"); // 블러그 URL
				search.setNum("0"); // 블러그 순번
				search.setPage("0"); // 블러그 페이지
				search.setContent("no_save,no_save"); // 블러그 내용(타이틀 포함)
				search.setSite(params[5]); // 사이트 한글명
				search.setConCount("0"); // 사이트 한글명
				result.add(search);
				return result;
			}
			
			String gubun = "";
			if(params[3] != null && params[3].equals("CC0007")){
				
				//검색 카운트를 가져온다 '.several_post' 기준에서 EM 테그 사이에 있는 숫자를 추출
				Elements totalCount = doc.select(".several_post");
				
				String totalCountString = totalCount.toString();
							
				String conCount = "0";
				
				if(totalCountString.indexOf("<em>") == -1){
					
					Search search = new Search(); //메타저장 모델 생성
					search.setSiteE(params[0]); // 검색사이트 영어명
					search.setUrl("null"); // 블러그 URL
					search.setNum("0"); // 블러그 순번
					search.setPage("0"); // 블러그 페이지
					search.setContent("no_save,no_save"); // 블러그 내용(타이틀 포함)
					search.setSite(params[5]); // 사이트 한글명
					search.setConCount(conCount); // 사이트 한글명
					result.add(search);
					
				}else{
					conCount = totalCountString.substring(totalCountString.indexOf("<em>")+4, totalCountString.indexOf("</em>")-1);
					conCount = extract_numeral(conCount);
				}
				
				Elements elList = doc.select(".list_content");
				
				int num = 0;
				for(Element el : elList){
					Elements divs = el.getElementsByTag("div");

					for(Element div : divs){
						String div_clsnm = div.className();
						if(div_clsnm.equals("")){
							
							String detail_url = div.child(0).attributes().get("href");
							
							String con_url = getUrlConverter(detail_url);
							if(!con_url.equals("no_naver")){
								String content = "no_save,no_save";
								if(params[7].equals("Y")){
									content = getSearchParsingString(params, con_url);
								}
								Search search = new Search(); //메타저장 모델 생성
								search.setSiteE(params[0]); // 검색사이트 영어명
								search.setUrl(detail_url); // 블러그 URL
								search.setNum((num+1)+""); // 블러그 순번
								search.setPage((page+1)+""); // 블러그 페이지
								search.setContent(content); // 블러그 내용(타이틀 포함)
								search.setSite(params[5]); // 사이트 한글명
								search.setConCount(conCount); // 검색건수
								result.add(search);
							
							}else{
								Search search = new Search(); //메타저장 모델 생성
								search.setSiteE(params[0]); // 검색사이트 영어명
								search.setUrl(detail_url); // 블러그 URL
								search.setNum((num+1)+""); // 블러그 순번
								search.setPage((page+1)+""); // 블러그 페이지
								search.setContent("no_save,no_save"); // 블러그 내용(타이틀 포함)
								search.setSite(params[5]); // 사이트 한글명
								search.setConCount(conCount); // 사이트 한글명
								result.add(search);
							}
						}
					}
					num++;
				}
				
			}else if(params[0] != null && params[3].equals("CC0008")){
				gubun = ".txt_block";
				Elements elList = doc.select(gubun);
				Elements totalCount = doc.select(".num");
				
				String totalCountString = totalCount.toString();
				String conCount = "0";
				
				if(totalCountString.indexOf("SearchTotalCount")== -1){
					
					Search search = new Search(); //메타저장 모델 생성
					search.setSiteE(params[0]); // 검색사이트 영어명
					search.setUrl("null"); // 블러그 URL
					search.setNum("0"); // 블러그 순번
					search.setPage("0"); // 블러그 페이지
					search.setContent("no_save,no_save"); // 블러그 내용(타이틀 포함)
					search.setSite(params[5]); // 사이트 한글명
					search.setConCount(conCount); // 사이트 한글명
					result.add(search);
					
				}else{
					conCount = totalCount.get(0).getElementById("SearchTotalCount").text();
					conCount = extract_numeral(conCount);
				}
				
				int num = 0;
				for(Element el : elList){
					Elements divs = el.getElementsByTag("dd");
					
					for(Element div : divs){
						String div_clsnm = div.className();
						if(div_clsnm.equals("txt_block")){
							
							String detail_url = div.child(0).attributes().get("href");
							
							String con_url = detail_url;//getUrlConverter(detail_url);
							if(!con_url.equals("no_naver")){
								String content = "no_save,no_save";
								if(params[7].equals("Y")){
									content = getSearchParsingString(params, con_url);
								}
								
								Search search = new Search(); //메타저장 모델 생성
								search.setSiteE(params[0]); // 검색사이트 영어명
								search.setUrl(detail_url); // 블러그 URL
								search.setNum((num+1)+""); // 블러그 순번
								search.setPage((page+1)+""); // 블러그 페이지
								search.setContent(content); // 블러그 내용(타이틀 포함)
								search.setSite(params[5]); // 사이트 한글명
								search.setConCount(conCount); // 검색건수
								result.add(search);
							
							}else{
								Search search = new Search(); //메타저장 모델 생성
								search.setSiteE(params[0]); // 검색사이트 영어명
								search.setUrl(detail_url); // 블러그 URL
								search.setNum((num+1)+""); // 블러그 순번
								search.setPage((page+1)+""); // 블러그 페이지
								search.setContent("no_save,no_save"); // 블러그 내용(타이틀 포함)
								search.setSite(params[5]); // 사이트 한글명
								result.add(search);
							}
							num++;
						}
					}
				}
			}		
		}else if(params[0] != null && params[0].equals("CC0006")){
			
			try {
				doc = getUrl(url);
			} catch (IOException e) {
				Search search = new Search(); //메타저장 모델 생성
				search.setSiteE(params[0]); // 검색사이트 영어명
				search.setUrl("null"); // 블러그 URL
				search.setNum("0"); // 블러그 순번
				search.setPage("0"); // 블러그 페이지
				search.setContent("no_save,no_save"); // 블러그 내용(타이틀 포함)
				search.setSite(params[5]); // 사이트 한글명
				search.setConCount("0"); // 사이트 한글명
				result.add(search);
				return result;
			}
			
			
			String gubun = "";
			if(params[3] != null && params[3].equals("CC0007")){
				
				//검색 카운트를 가져온다 '.several_post' 기준에서 EM 테그 사이에 있는 숫자를 추출
				Elements totalCount = doc.select(".coll_tit");
				Elements totalCount2 = totalCount.select(".mg_expander");
				
				String conCount = "0";
				if( totalCount2.size() != 2){
					Search search = new Search(); //메타저장 모델 생성
					search.setSiteE(params[0]); // 검색사이트 영어명
					search.setUrl("null"); // 블러그 URL
					search.setNum("0"); // 블러그 순번
					search.setPage("0"); // 블러그 페이지
					search.setContent("no_save,no_save"); // 블러그 내용(타이틀 포함)
					search.setSite(params[5]); // 사이트 한글명
					search.setConCount(conCount); // 사이트 한글명
					result.add(search);
				}else{
					String totalCountString = totalCount2.get(1).text();
					
					conCount = totalCountString.substring(totalCountString.indexOf("/")+2, totalCountString.indexOf("건"));
					conCount = extract_numeral(conCount);
				}
				Elements elList = doc.select(".cont_inner");
				
				int num = 0;
				for(Element el : elList){
					Elements divs = el.select(".clear");
					for(Element div : divs){
						
						Elements di = div.select(".f_url");
						for(Element d_url : di){
							String detail_url = d_url.attributes().get("href");
							String content = "no_save,no_save";
							
							if(params[7].equals("Y")){
								content = getSearchParsingDaum(params, detail_url);
							}
							if(!content.equals("no_daum")){
								
								
								Search search = new Search(); //메타저장 모델 생성
								search.setSiteE(params[0]); // 검색사이트 영어명
								search.setUrl(detail_url); // 블러그 URL
								search.setNum((num+1)+""); // 블러그 순번
								search.setPage((page+1)+""); // 블러그 페이지
								search.setContent(content); // 블러그 내용(타이틀 포함)
								search.setSite(params[5]); // 사이트 한글명
								search.setConCount(conCount); // 검색건수
								result.add(search);
							
							}else{
								Search search = new Search(); //메타저장 모델 생성
								search.setSiteE(params[0]); // 검색사이트 영어명
								search.setUrl(detail_url); // 블러그 URL
								search.setNum((num+1)+""); // 블러그 순번
								search.setPage((page+1)+""); // 블러그 페이지
								search.setContent("no_save,no_save"); // 블러그 내용(타이틀 포함)
								search.setSite(params[5]); // 사이트 한글명
								search.setConCount(conCount); // 사이트 한글명
								result.add(search);
							}
						}
						
					}
					num++;
				}
			}else if(params[0] != null && params[3].equals("CC0008")){
				//검색 카운트를 가져온다 '.several_post' 기준에서 EM 테그 사이에 있는 숫자를 추출
				Elements totalCount = doc.select(".coll_tit");
				Elements totalCount2 = totalCount.select(".mg_expander");
				
				String conCount = "0";
				
				if( totalCount2.size() != 2){
					Search search = new Search(); //메타저장 모델 생성
					search.setSiteE(params[0]); // 검색사이트 영어명
					search.setUrl("null"); // 블러그 URL
					search.setNum("0"); // 블러그 순번
					search.setPage("0"); // 블러그 페이지
					search.setContent("no_save,no_save"); // 블러그 내용(타이틀 포함)
					search.setSite(params[5]); // 사이트 한글명
					search.setConCount(conCount); // 사이트 한글명
					result.add(search);
				}else{
					
					String totalCountString = totalCount2.get(1).text();
					
					conCount = totalCountString.substring(totalCountString.indexOf("/")+1, totalCountString.indexOf("건"));
					conCount = extract_numeral(conCount);
				}
				Elements elList = doc.select(".cont_inner");
				
				int num = 0;
				for(Element el : elList){
					Elements divs = el.select(".f_l");
					for(Element div : divs){
						
						Elements di = div.select(".f_url");
						for(Element d_url : di){
							String detail_url = d_url.attributes().get("href");
							detail_url = detail_url.substring(0, detail_url.indexOf("?q="));
							String content = "no_save,no_save";
							if(params[7].equals("Y")){
								content = getSearchParsingDaumC(params, detail_url);
							}
							if(!content.equals("no_daum")){
								
								Search search = new Search(); //메타저장 모델 생성
								search.setSiteE(params[0]); // 검색사이트 영어명
								search.setUrl(detail_url); // 블러그 URL
								search.setNum((num+1)+""); // 블러그 순번
								search.setPage((page+1)+""); // 블러그 페이지
								search.setContent(content); // 블러그 내용(타이틀 포함)
								search.setSite(params[5]); // 사이트 한글명
								search.setConCount(conCount); // 검색건수
								result.add(search);
							
							}else{
								Search search = new Search(); //메타저장 모델 생성
								search.setSiteE(params[0]); // 검색사이트 영어명
								search.setUrl(detail_url); // 블러그 URL
								search.setNum((num+1)+""); // 블러그 순번
								search.setPage((page+1)+""); // 블러그 페이지
								search.setContent("no_save,no_save"); // 블러그 내용(타이틀 포함)
								search.setSite(params[5]); // 사이트 한글명
								search.setConCount(conCount); // 사이트 한글명
								result.add(search);
							}
	
						}
						
					}
						num++;
				}
			}
		}
		return result;
	}
	
	//숫자 추출함수
	public String extract_numeral(String str)
	{
		String numeral = "", temp = "";
		if( str == null )
		{
			numeral = null;
		}else{
			for( int i = 0; i < str.length(); i++ )
			{
				temp = str.substring(i,i+1);
				if( Character.isDigit(str.charAt(i)) ) //isDigit를 이용 
				{
					numeral += temp;
				}
			}
		}
		return numeral;
	} 
	
	public String getSearchParsingString(String[] params,String c_url){
		String result = "";
		String title = "";
		String content = "";
		
		try {
			//System.out.println("getSearchParsingString c_url : "+c_url);
			doc_d = Jsoup.connect(c_url).timeout(20*5000).userAgent("Chrome").ignoreContentType(true).get();
			
		} catch (IOException e) {
			//e.printStackTrace();
			return "no_save,no_save";
		}
		
		//타이틀 가져오기
		Elements title_b = doc_d.select(".htitle");
		//System.out.println("doc_d : "+doc_d);
		if(title_b.size() > 0){
			Element title_s = title_b.get(0);
			title = title_s.text();
			
			//본문가져오기
			Elements content_b = doc_d.select(".post-view.pcol2");
			
			//System.out.println("txt1 : "+content_b);
			//System.out.println("txt2 : "+content_b.text()); // .text() 함수로 가져오는 경우 (&#x200b;)?(&#xfffd;)?(&#xfeff;) 해당 태그들을 제거 하지 못한다.  
			
			//content = content_b.text();
			String content_c = content_b.toString();
			//content = content_c.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", ""); // 직접 제거 하는경우 빈칸을 제거 해야 한다.
			//content = content.replaceAll("(&nbsp;)?(&#x200b;)?(&#xfffd;)?(&#xfeff;)?(   )?", ""); 
			content = content_b.text();
			content = content.replace(",", " ");
			content = content.replace("     ", " ");
			title = title.replace(",", " ");
			content = content.replace("\n", " ");
			title = title.replace("\n", " ");
			content = content.replace("\r", " ");
			title = title.replace("\r", " ");
			
			//System.out.println("txt3 : "+content);
			result = title +" , "+ content;
			//System.out.println("txt : "+result);
		}else{
			result = "del_blog,del_blog";
		}
		
		return result;
	}
	
	
	public String getSearchParsingDaum(String[] params,String c_url){
		String result = "";
		String title = "";
		String content = "";
		String d_url = "";
			
		d_url = "http://m.blog.daum.net/"+c_url.substring(21, c_url.length());
		
		try {
			//System.out.println("getSearchParsingString c_url : "+c_url);
			doc_d = Jsoup.connect(d_url).timeout(20*5000).userAgent("Chrome").ignoreContentType(true).get();
			
		} catch (IOException e) {
			//e.printStackTrace();
			return "no_save,no_save";
		}
		
		//타이틀 가져오기
		Elements title_b = doc_d.select(".article_info");
		
		if(title_b.size() == 0){
			return "del_blog,del_blog";
		}

		Element title_s = title_b.get(0).child(0);
		title = title_s.text();
		//System.out.println("title : "+title);
		
		//본문가져오기
		Elements content_b = doc_d.select(".small");
		
		content = content_b.text();
		content = content.replace(",", " ");
		content = content.replace("     ", " ");
		content = content.replace("\n", " ");
		title = title.replace(",", " ");
		title = title.replace("\n", " ");
		content = content.replace("\r", " ");
		title = title.replace("\r", " ");
		
		//System.out.println("content : "+content);
		result = title +" , "+ content;
		//System.out.println("txt : "+result);
		
		return result;
	}
	
	//다음 카페
	public String getSearchParsingDaumC(String[] params,String c_url){
		String result = "";
		String title = "";
		String content = "";
		String d_url = "";
		
		d_url = "http://m.cafe.daum.net/"+c_url.substring(21, c_url.length());	
		
		try {
			//System.out.println("getSearchParsingString c_url : "+c_url);
			doc_d = Jsoup.connect(d_url).timeout(20*5000).userAgent("Chrome").ignoreContentType(true).get();
		} catch (IOException e) {
			//e.printStackTrace();
			return "no_save,no_save";
		}
	
		//타이틀 가져오기
		Elements title_b = doc_d.getElementsByTag("h3");
		
		if(title_b.size() == 0){
			return "del_cafe,del_cafe";
		}
		
		title = title_b.get(0).text();
		//Element title_s = title_b.get(0).child(0);
		//title = title_s.text();
		
		//본문가져오기
		Element content_b = doc_d.getElementById("article");
		
		//System.out.println("txt1 : "+content_b);
		//System.out.println("txt2 : "+content_b.text()); // .text() 함수로 가져오는 경우 (&#x200b;)?(&#xfffd;)?(&#xfeff;) 해당 태그들을 제거 하지 못한다.  
		
		//content = content_b.text();
		String content_c = content_b.toString();
		//content = content_c.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", ""); // 직접 제거 하는경우 빈칸을 제거 해야 한다.
		//content = content.replaceAll("(&nbsp;)?(&#x200b;)?(&#xfffd;)?(&#xfeff;)?(   )?", ""); 
		content = content_b.text();
		content = content.replace(",", " ");
		content = content.replace("     ", " ");
		title = title.replace(",", " ");
		content = content.replace("\n", " ");
		title = title.replace("\n", " ");
		content = content.replace("\r", " ");
		title = title.replace("\r", " ");
		
		//System.out.println("content : "+content);
		result = title +" , "+ content;
		//System.out.println("txt : "+result);
		
		return result;
	}
	
	
	
	public String getUrlConverter(String d_url){
		String result = "";
		
		String id = "";
		String page_no = "";
		
		if(d_url != null && d_url.length() >0){
			String naver_cp1 = "blog.naver.com";
			String naver_cp2 = ".blog.me";
			String naver_cp3 = "kr/";
			String naver_cp4 = "com/";
			String naver_cp5 = d_url.substring(7, d_url.length());
			
			String cp1 = d_url.substring(7,21);
			//System.err.println("d_url : "+d_url);
			//System.err.println("cp1 : "+cp1);
			
			Boolean sp3 = d_url.substring(d_url.indexOf(naver_cp3)+6, d_url.length()).length() <= 12;
			Boolean sp4 = d_url.substring(d_url.indexOf(naver_cp4)+4, d_url.length()).length() <= 12;
			Boolean sp5 = naver_cp5.substring(naver_cp5.indexOf("/")+1, naver_cp5.length()).length() <= 12;
			
			if(naver_cp1.equals(cp1)){ //네이버 블러그 형태 1
				//System.err.println("패턴 1");
				int id_end = d_url.indexOf("?");
				id = d_url.substring(22,id_end);
				
				int no_start = d_url.indexOf("&logNo=")+7;
				//int no_end = no_start+12;
				int no_end = d_url.indexOf("&from");
				page_no = d_url.substring(no_start,no_end);
				
				////System.err.println("id : "+id);
				////System.err.println("page_no : "+page_no);
			}else if(d_url.indexOf(naver_cp2) != -1){ //네이버 블러그 형태 2
				//System.err.println("패턴 2");
				
				id = d_url.substring(7,d_url.indexOf(naver_cp2));
				
				int no_start = d_url.indexOf(naver_cp2)+9;
				//int no_end = no_start+12;
				int no_end = d_url.length();
				page_no = d_url.substring(no_start,no_end);
				
				//System.err.println("id : "+id);
				//System.err.println("page_no : "+page_no);
				
			}else if(sp3){ //네이버 블러그 형태 3
				//System.err.println("패턴 3");
				//System.err.println("url : "+d_url.substring(d_url.indexOf(naver_cp3)+3, d_url.length()));
				
				page_no = d_url.substring(d_url.indexOf(naver_cp3)+3, d_url.length());
				
				id = getId(d_url);
				
				//System.err.println("id : "+id);
				//System.err.println("page_no : "+page_no);
				
			}else if(sp4){ //네이버 블러그 형태 4
				//System.err.println("패턴 4");
				//System.err.println("url : "+d_url.substring(d_url.indexOf(naver_cp4)+4, d_url.length()));
				
				page_no = d_url.substring(d_url.indexOf(naver_cp4)+4, d_url.length());
				
				id = getId(d_url);
				
				//System.err.println("id : "+id);
				//System.err.println("page_no : "+page_no);
				
			}else if(sp5){ //네이버 블러그 형태 5
				//System.err.println("패턴 5");
				//System.err.println("url : "+naver_cp5.substring(naver_cp5.indexOf("/")+1, naver_cp5.length()));
				
				page_no = naver_cp5.substring(naver_cp5.indexOf("/")+1, naver_cp5.length());
				
				id = getId(d_url);
				
				//System.err.println("id : "+id);
				//System.err.println("page_no : "+page_no);
				
			}else{//네이버 아님 패턴 추가 예정
				return "no_naver";
			}
			if (id.equals("no_naver")){
				return "no_naver";
			}
			
			result = "http://blog.naver.com/PostView.nhn?blogId="+id+"&logNo="+page_no;
			//result = "http://blog.naver.com/"+id+"/"+page_no;
			//System.err.println("result : "+result);
		}
		
		return result;
	}
	
	public String getId(String d_url){
		String result = "";
		
		
		try {
			
			//System.out.println(d_url);
			doc_dr = Jsoup.connect(d_url).timeout(10*5000).userAgent("Chrome").ignoreContentType(true).get();
			
		} catch (IOException e) {
			//e.printStackTrace();

			return "no_naver";
			
		}
		
		//screenFrame
		Elements test = doc_dr.getElementsByTag("frame");
		
		if(test.size() == 0){
			return "no_naver";
		}
		
		String id = test.get(0).attr("id");
		String result_a = test.get(0).attr("src");
		
		//System.out.println("id : "+id );
		if(id.equals("screenFrame")){
			String result_b = result_a.substring(result_a.indexOf("naver.com/")+10);
			result = result_b.substring(0, result_b.indexOf("/"));
		}else if(id.equals("mainFrame")){
			String result_b = result_a.substring(result_a.indexOf("blogId=")+7);
			result = result_b.substring(0, result_b.indexOf("&logNo"));
		}else{
			result = "no_naver"; 
		}
		
		//System.out.println("result : "+result );
		return result;
	}
	
	//다음에 URL접근을 피하는 방법
	public Document getUrl(String d_url) throws IOException{
		
		URL url_con = new URL(d_url);
		HttpURLConnection huc = (HttpURLConnection)url_con.openConnection();  
        huc.setRequestMethod("GET");  
        // request header set  
        huc.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");  
        huc.setRequestProperty("Accept-Charset", "windows-949,utf-8;q=0.7,*;q=0.3");  
        //huc.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");  
        huc.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");  
        huc.setRequestProperty("Connection", "keep-alive");  
        huc.setRequestProperty("Cookie", "uvkey=UvXxjq97@5EAADrliQsAAAEP; captcha_pass=y; ODT=IIMZ_NKSZ_IVRZ_CCBZ_DICZ_BR1Z_GG2Z_; DDT=SNPZ_NNSZ_BRDA_1DVZ_WSAZ_FGKZ_VO2Z_LB2Z_MS2Z_; DTQUERY=%EB%AF%B8%EB%8B%88%EB%B2%84%EC%8A%A4; TIARA=MHZRg2Qt.BJuY5prIK657ygoyQZ6ukpYLaoq5C-_3ocIpGD3vagvqOM22PRcAmF2LjW94G.z8KqNEP4H44ulLTYIt5MJETlJ; suggest=on");
        huc.setRequestProperty("Host", "search.daum.net");  
        //huc.setRequestProperty("Refer", "http://www.daum.net");
        huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36");
		
		InputStream is = huc.getInputStream();
		StringBuffer sb = new StringBuffer();
	    //byte[] b = new byte[1024];
	    
		BufferedInputStream bis = new BufferedInputStream(is);
	
		BufferedReader br = null;
 
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(bis, "utf-8"));
			//br = new BufferedReader(new InputStreamReader(is, "cp949"));
			//br = new BufferedReader(new InputStreamReader(is, "euc-kr"));
			while ((line = br.readLine()) != null) {
				sb.append(line);
				//System.out.println("line : "+line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
			
			logger.error("e.toString() : "+e.toString());
			logger.error("e.getMessage() : "+e.getMessage());
			logger.error("e.getStackTrace() : "+e.getStackTrace());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e.toString() : "+e.toString());
			logger.error("e.getMessage() : "+e.getMessage());
			logger.error("e.getStackTrace() : "+e.getStackTrace());
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
}
