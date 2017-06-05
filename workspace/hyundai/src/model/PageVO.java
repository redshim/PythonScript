package model;

public class PageVO {
	//페이징 처리를 위한 VO
	private int recordPerPage; // 페이지에 뿌려줄 레코드 갯수 (listCount)
	private int currentPage; // 사용자가 호출한 페이지 번호 (PageNum)
	
	private int totalCount; //데이터의 갯수 
	private int totalPage; //페이지 번호의 갯수
	private int number; //레코드 번호 먹이기 위한 부분
	private int numberStart; //페이지 번호의 시작
	private int numberEnd; //페이지 번호의 끝(pageCount)
	
	// DB에 보낼 Start 번호
	private int startNo;
	
	// DB에 보낼 End 번호
	private int endNo;
	

	public PageVO(){}
	
	/**
	 * recordPerPage = 페이지에 보여줄 리스트 갯수(ex: 페이지에 게시글 10개씩 보여주기) <br>
	 * currentPage = 사용자가 요청한 페이지 번호 <br>
	 * totalCount = 게시물의 총 갯수 <br>
	 * 
	 * @param recordPerPage
	 * @param currentPage
	 * @param totalCount
	 * 
	 */
	public PageVO(int recordPerPage, int currentPage, int totalCount){
		this.recordPerPage = recordPerPage;
		this.totalCount = totalCount;
		
		this.currentPage = currentPage;
		this.totalPage = (totalCount % recordPerPage == 0) ? totalCount / recordPerPage : totalCount / recordPerPage + 1;
		this.startNo = (currentPage - 1) * recordPerPage + 1;
		this.endNo = currentPage * recordPerPage;
		this.number = totalCount - (currentPage - 1) * recordPerPage + 1;

		this.numberStart = (currentPage - 1) / recordPerPage * recordPerPage + 1;

		this.numberEnd = numberStart + 9;
		if (numberEnd > totalPage) {
			numberEnd = totalPage;
		}
		
	}
	
	


	public PageVO(int recordPerPage, int currentPage, int totalCount, int totalPage, int number, int numberStart,
			int numberEnd, int startNo, int endNo) {
		this.recordPerPage = recordPerPage;
		this.currentPage = currentPage;
		this.totalCount = totalCount;
		this.totalPage = totalPage;
		this.number = number;
		this.numberStart = numberStart;
		this.numberEnd = numberEnd;
		this.startNo = startNo;
		this.endNo = endNo;
	}


	public int getRecordPerPage() {
		return recordPerPage;
	}


	public void setRecordPerPage(int recordPerPage) {
		this.recordPerPage = recordPerPage;
	}


	public int getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}


	public int getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}


	public int getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public int getNumberStart() {
		return numberStart;
	}


	public void setNumberStart(int numberStart) {
		this.numberStart = numberStart;
	}


	public int getNumberEnd() {
		return numberEnd;
	}


	public void setNumberEnd(int numberEnd) {
		this.numberEnd = numberEnd;
	}


	public int getStartNo() {
		return startNo;
	}


	public void setStartNo(int startNo) {
		this.startNo = startNo;
	}


	public int getEndNo() {
		return endNo;
	}


	public void setEndNo(int endNo) {
		this.endNo = endNo;
	}


	@Override
	public String toString() {
		return "PageVO [recordPerPage=" + recordPerPage + ", currentPage=" + currentPage + ", totalCount=" + totalCount
				+ ", totalPage=" + totalPage + ", number=" + number + ", numberStart=" + numberStart + ", numberEnd="
				+ numberEnd + ", startNo=" + startNo + ", endNo=" + endNo + "]";
	}



	
}


