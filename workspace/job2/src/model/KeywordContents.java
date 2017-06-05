package model;

public class KeywordContents {
	private String keyword_id;
	private String collecting_date;
	private String beginDate_s;
	private String endDate_s;
	private String collector_name;
	private String collector_ip;
	
	
	
	public String getCollector_ip() {
		return collector_ip;
	}
	public void setCollector_ip(String collector_ip) {
		this.collector_ip = collector_ip;
	}
	public String getCollector_name() {
		return collector_name;
	}
	public void setCollector_name(String collector_name) {
		this.collector_name = collector_name;
	}
	public String getBeginDate_s() {
		return beginDate_s;
	}
	public void setBeginDate_s(String beginDate_s) {
		this.beginDate_s = beginDate_s;
	}
	public String getEndDate_s() {
		return endDate_s;
	}
	public void setEndDate_s(String endDate_s) {
		this.endDate_s = endDate_s;
	}
	public String getKeyword_id() {
		return keyword_id;
	}
	public void setKeyword_id(String keyword_id) {
		this.keyword_id = keyword_id;
	}
	public String getCollecting_date() {
		return collecting_date;
	}
	public void setCollecting_date(String collecting_date) {
		this.collecting_date = collecting_date;
	}
}
