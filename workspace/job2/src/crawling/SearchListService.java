package crawling;

import java.util.ArrayList;
import java.util.List;

import model.Search;



public class SearchListService {
	
	private String searchUrl;
	private List<Search>[] searchList;
	private List<Search>[] listCount;
	
	public List<Search> crawlingList(String[] params){
		
		List<Search> total = new ArrayList<Search>();//반환값
		
		String[] searchParams = params;
		
		//String word = URLEncoder.encode(searchParams[4], "UTF-8");
		
		String word = searchParams[4];
		
		int page_cnt_max = 1;
		int page_cnt = 1;
		
		if(searchParams[1] != null && !searchParams[1].equals("")){
			page_cnt = Integer.parseInt(searchParams[1]); 
		}else{
			page_cnt = page_cnt_max;
		}
		
		
		if(searchParams[0].equals("CC0005")){ //네이버 
			if(searchParams[3].equals("CC0007")){ //블러그 URL 설정
				this.searchUrl = "http://section.blog.naver.com/sub/SearchBlog.nhn?type=post&option.keyword=" + word+"&term=period&option.startDate="+searchParams[2]+"&option.endDate="+searchParams[2]+"&option.page.currentPage=";
			}else if(searchParams[3].equals("CC0008")){ //카페 URL 설정
				this.searchUrl = "http://section.cafe.naver.com/ArticleSearch.nhn?query="+word+"&period="+searchParams[2]+"&period="+searchParams[2]+"&page=";
			}
		}else if(searchParams[0].equals("CC0006")){ // 다음
			if(searchParams[3].equals("CC0007")){ //블러그 URL 설정
				this.searchUrl = "http://search.daum.net/search?q="+word+"&w=blog&m=board&f=section&SA=daumsec&lpp=10&nil_src=blog&period=u&sd="+searchParams[2]+"000000&ed="+searchParams[2]+"235959&page=";
			}else if(searchParams[3].equals("CC0008")){ //카페 URL 설정
				this.searchUrl = "http://search.daum.net/search?q="+word+"&w=cafe&m=board&f=section&SA=daumsec&lpp=10&nil_src=blog&period=u&sd="+searchParams[2]+"000000&ed="+searchParams[2]+"235959&p=";
			}
		}
		
		this.listCount = new List[1];
		
		//수집기를 1페이지 돌려서 페이지 카운트를 구한다.
		SearchListDao searchDao = new SearchListDao();
		this.listCount[0] = searchDao.getSearchParsingList(searchParams, this.searchUrl+"1", 0);
		String count = this.listCount[0].get(0).getConCount();
		
		if(count.length()>1){
			if(count.substring(count.length()-1, count.length()).equals("0")){
				page_cnt = Integer.parseInt(count.substring(0, count.length()-1));
			}else{
				page_cnt = Integer.parseInt(count.substring(0, count.length()-1))+1;
			}
		}else{
			page_cnt = 1;
		}
		 
		//웹상 오류가 뜨면 페이지 카운트가 1이고 실제 카운트가 0인 경우 30초후 다시 검색
		/*if(page_cnt == 1 && count.equals("0")){
			try {
				System.out.println("page_cnt = 1 , sleep(30000)");
				Thread.sleep(10000);
				this.listCount[0] = searchDao.getSearchParsingList(searchParams, this.searchUrl+"1", 0);
				count = this.listCount[0].get(0).getConCount();
				System.out.println("count2 : "+count);
				if(count.length()>1){
					if(count.substring(count.length()-1, count.length()).equals("0")){
						page_cnt = Integer.parseInt(count.substring(0, count.length()-1));
					}else{
						page_cnt = Integer.parseInt(count.substring(0, count.length()-1))+1;
					}
				}else{
					page_cnt = 1;
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		if(page_cnt == 1){
			this.searchList = new List[page_cnt];
			this.searchList[0] = this.listCount[0];
			for(int j = 0; j < this.searchList[0].size() ; j++){
				Search search_list = this.searchList[0].get(j);
				total.add(search_list);
			}
			return total;
			
		}else{
			this.searchList = new List[page_cnt];
		}
		
		StringBuffer sb = new StringBuffer(); //url 정보를 모아서 중복이 되면 멈춘다.
		
		Boolean go_stop = false;
		
		String tot_cnt = "";
		int new_page_cnt = 0;
		
		// 페이지 카운트 수만큼 getSearchParsingList를 돌려 searchList에 결과를 담는다.
		for(int i = 0; i < page_cnt ; i++){
			int r_cnt = i+1;
			//this.searchUrl = "http://section.blog.naver.com/sub/SearchBlog.nhn?type=post&option.keyword=" + word+"&option.page.currentPage="+r_cnt;
			String url = "";
			if(searchParams[3].equals("CC0007")){
				//url = "http://7proxysites.com/browse.php?u="+URLEncoder.encode(this.searchUrl, "UTF-8")+r_cnt;
				url = this.searchUrl+r_cnt;
			}else if(searchParams[3].equals("CC0008")){
				url = this.searchUrl+r_cnt;
				//url = this.searchUrl;
			}
			
			//천건이상의 경우 1000건 마다 5초씩 쉰다
			if(i != 0 && i%100 == 0){
				try {
					Thread.sleep(5000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			this.searchList[i] = searchDao.getSearchParsingList(searchParams, url, i);
			
			//크롤링 중간에 검색건수가 변경 될때 작은 수로 변경되면 그수로 페이지 카운트를 바꾼다.
			tot_cnt = searchList[0].get(0).getConCount();

			if(tot_cnt.length()>1){
				if(count.substring(tot_cnt.length()-1, tot_cnt.length()).equals("0")){
					new_page_cnt = Integer.parseInt(tot_cnt.substring(0, tot_cnt.length()-1));
				}else{
					new_page_cnt = Integer.parseInt(tot_cnt.substring(0, tot_cnt.length()-1))+1;
				}
			}else{
				new_page_cnt = 1;
			}
			if(page_cnt > new_page_cnt){
				page_cnt = new_page_cnt; //크롤링 중간에 총 카운트 숫자가 변경된경우
			}else{
//				System.out.println(" no change");
			}
			for(int j = 0; j < this.searchList[i].size() ; j++){
				Search search_list = this.searchList[i].get(j);
				search_list.setConCount(tot_cnt);
				if(search_list.getUrl().length() > 20){ // 검색건수가 0인경우
					String url_con = search_list.getUrl().substring(20, search_list.getUrl().length());
					if(sb.indexOf(url_con) == -1){
						sb.append(url_con+"|");
						go_stop = false;//go
					}else{
						go_stop = true; //stop
						break;
					}
				}
				total.add(search_list);
			}
			
			if(go_stop){
				break;
			}

		}
		return total;
	}
}
