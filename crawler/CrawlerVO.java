package crawler;

public class CrawlerVO {
	// conid	contitle	conprice	condate	consuggest	conadr	contype
	int conId;
	String conTitle;
	int conPrice;
	String conDate;
	int conSuggest;
	String conAdr;
	String conType;
	
	public CrawlerVO(){}
	
	public CrawlerVO(int conId, String conTitle, int conPrice, String conDate, int conSuggest, String conAdr,
			String conType) {
		this.conId = conId;
		this.conTitle = conTitle;
		this.conPrice = conPrice;
		this.conDate = conDate;
		this.conSuggest = conSuggest;
		this.conAdr = conAdr;
		this.conType = conType;
	}
	 
	public CrawlerVO(int conId, String conTitle, int conPrice, int conSuggest) {
		this.conId = conId;
		this.conTitle = conTitle;
		this.conPrice = conPrice;
		this.conSuggest = conSuggest;
	}
	
	public int getConId() {
		return conId;
	}
	public void setConId(int conId) {
		this.conId = conId;
	}
	public String getConTitle() {
		return conTitle;
	}
	public void setConTitle(String conTitle) {
		this.conTitle = conTitle;
	}
	public int getConPrice() {
		return conPrice;
	}
	public void setConPrice(int conPrice) {
		this.conPrice = conPrice;
	}
	public String getConDate() {
		return conDate;
	}
	public void setConDate(String conDate) {
		this.conDate = conDate;
	}
	public int getConSuggest() {
		return conSuggest;
	}
	public void setConSuggest(int conSuggest) {
		this.conSuggest = conSuggest;
	}
	public String getConAdr() {
		return conAdr;
	}
	public void setConAdr(String conAdr) {
		this.conAdr = conAdr;
	}
	public String getConType() {
		return conType;
	}
	public void setConType(String conType) {
		this.conType = conType;
	}
	
	
}
