package crawler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerDAO {
	static Document doc = null;
	static String url = null;
	static ArrayList<CrawlerVO> newList = new ArrayList<>();
	static ArrayList<CrawlerVO> oldList = new ArrayList<>();

	static final String driver = "org.mariadb.jdbc.Driver";
	static final String DB = "jdbc:mariadb://shared00.iptime.org:3307/hotdeal";
	static Connection con; 
	static PreparedStatement pstmt = null;
	static ResultSet rs = null;
	static int timeoutValue = 20;
	static LocalDate localDate = LocalDate.now();
	static String temp = localDate.toString();
	static String today = temp + " ";
	static String currentYear = temp.substring(0,4) + "-";
	static String dummyTime = " 00:00";
	static int limit = 1000;
	 
	public static void doCrawling(int pageCnt) {
		System.out.println("doCrawling 가동");
		System.out.println(pageCnt);
		
		ArrayList<CrawlerVO> rawList = new ArrayList<>();
		ArrayList<CrawlerVO> compareList = new ArrayList<>();
		
		rawList.addAll(bbombbu(pageCnt));
		rawList.addAll(ruliweb(pageCnt));
		rawList.addAll(quasarzone(pageCnt));
		
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(DB,"hotdeal","Gktelf12!@");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		compareList = getCompareList();
//		for(CrawlerVO idx : rawList) 
//			System.out.println(idx.conId+" / "+idx.conTitle+" / "+idx.conPrice+" / "+idx.conDate);
		
		listDistributor(compareList, rawList);
		insertList();
		updateList();
		deleteOverflow();
		System.out.println("doCrawling 종료");
	}
	
	public static ArrayList<CrawlerVO> bbombbu(int pageIdx) { // 뽐뿌 크롤링 메서드
		ArrayList<CrawlerVO> rawListVo = new ArrayList<>();
		url = "https://www.ppomppu.co.kr/zboard/zboard.php?id=ppomppu&page="+pageIdx+"&divpage=73";
		Pattern filter = Pattern.compile("[/|\\(]?[\\d+,?]+[^+][만|천]?[원|만][^권|^할]\\)?");
		Matcher filterData = null;
		try {
			doc = URLFilter(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements list0 = doc.select(".list0");
		Elements list1 = doc.select(".list1");
		
		Elements list = list0;
		list.addAll(list1);
		
		for(Element idx : list) {
			Elements optionIdx = idx.select(".eng.list_vspace");
			//[pk] conid	contitle	conprice	condate	consuggest	conadr	contype
			
			if(!optionIdx.get(0).text().equals("") && !idx.select(".list_title").text().equals("")) {
			int conId = Integer.parseInt(optionIdx.get(0).text());
			String tempTitle = idx.select(".list_title").text();
			String conTitle = tempTitle.replaceAll(filter.toString(), ""); 
			
			int conPrice = 0;
			filterData = filter.matcher(tempTitle);
			
			if(filterData.find()) {
				String filterStr = filterData.group();
				conPrice = Integer.parseInt(filterStr.replaceAll("\\D",""));
				if(filterStr.indexOf("만") != -1) {
					conPrice = conPrice*10000;
				}else if(filterStr.indexOf("천") != -1){
					conPrice = conPrice*1000;
				}
				if(filterData.find()) {
					String filterStr2 = filterData.group();
					conPrice = Integer.parseInt(filterStr2.replaceAll("\\D",""));
					int conPrice2 = Integer.parseInt(filterStr2.replaceAll("\\D", ""));
					if(filterStr2.indexOf("천")!=-1) {
						conPrice += conPrice2*1000;
					}
				}
			}
			
			String tempDate = optionIdx.get(1).text();
			String conDate = null;
			if(tempDate.indexOf(":") != -1) {
				conDate = today + (tempDate.substring(0,tempDate.length()-3));
			}else {
				conDate = (tempDate.replaceAll("/", "-")) + dummyTime;
			}
			String tempSuggest = optionIdx.get(3).text();
			int cutIdx = tempSuggest.indexOf(" ");
			int conSuggest = 0;
			
			if(cutIdx != -1)
				conSuggest = Integer.parseInt(tempSuggest.substring(0, cutIdx));
			
			String conAdr = "https://www.ppomppu.co.kr/zboard/" + idx.select(".list_title").parents().attr("href");
			String conType = "bbombbu";
			
			CrawlerVO temp = new CrawlerVO(conId,conTitle,conPrice,conDate,conSuggest,conAdr,conType);
			rawListVo.add(temp);
			
			}
		}
		System.out.println("뽐뿌 : "+rawListVo.size());
		return rawListVo;
	}
	
	public static ArrayList<CrawlerVO> ruliweb(int pageIdx) { // 루리웹 크롤링 메서드
		ArrayList<CrawlerVO> rawListVo = new ArrayList<>();
		url = "https://bbs.ruliweb.com/market/board/1020?page="+pageIdx;
		Pattern filter = Pattern.compile("[/|\\(]?[\\d+,?]+[^+][만|천]?[원|만][^권|^할]\\)?");
		Matcher filterData = null;
		try {
			doc = URLFilter(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements list = doc.select(".table_body.blocktarget");
		
		for(Element idx : list) {
			//[pk] conid	contitle	conprice	condate	consuggest	conadr	contype
			if(!idx.select(".id").text().equals("")) {
				int conId = Integer.parseInt(idx.select(".id").text());
				String tempTitle = idx.select(".deco").text();
				String conTitle = tempTitle.replaceAll(filter.toString(), "");
				
				int conPrice = 0;
				filterData = filter.matcher(tempTitle);
				
				if(filterData.find()) {
					String filterStr = filterData.group();
					conPrice = Integer.parseInt(filterStr.replaceAll("\\D",""));
					if(filterStr.indexOf("만") != -1) {
						conPrice = conPrice*10000;
					}else if(filterStr.indexOf("천") != -1){
						conPrice = conPrice*1000;
					}
					if(filterData.find()) {
						String filterStr2 = filterData.group();
						int conPrice2 = Integer.parseInt(filterStr2.replaceAll("\\D", ""));
						if(filterStr2.indexOf("천")!=-1) {
							conPrice += conPrice2*1000;
						}
					}
				}
				
				String tempDate = idx.select(".time").text();
				String conDate = null;
				if(tempDate.indexOf(":") != -1) {
					conDate = today + tempDate;
				}else {
					conDate = (tempDate.replaceAll("[.]", "-")) + dummyTime;
				}
				String tempSuggest = idx.select(".recomd").text();
				int conSuggest = 0;
				if(!tempSuggest.equals("")) 
					conSuggest = Integer.parseInt(tempSuggest);
				String conAdr = idx.select(".deco").attr("href");
				String conType = "ruliweb";
				
				CrawlerVO temp = new CrawlerVO(conId,conTitle,conPrice,conDate,conSuggest,conAdr,conType);
				rawListVo.add(temp);
				
			}
		}
		System.out.println("루리웹 : "+rawListVo.size());
		return rawListVo;
	}
	
	public static ArrayList<CrawlerVO> quasarzone(int pageIdx) { // 퀘이사존 크롤링 메서드
		ArrayList<CrawlerVO> rawListVo = new ArrayList<>();
		url = "https://quasarzone.com/bbs/qb_saleinfo?page="+pageIdx;
		Pattern filter = Pattern.compile("[/|\\(]?[\\d+,?]+[^+][만|천]?[원|만][^권|^할]\\)?");
		
		try {
			doc = URLFilter(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements list = doc.select(".market-type-list.market-info-type-list.relative table tbody tr");
		
		
		for(Element idx : list) {
			//[pk] conid	contitle	conprice	condate	consuggest	conadr	contype
			String conAdr = idx.select(".tit a").attr("href"); 
			int conId = Integer.parseInt(conAdr.substring(conAdr.lastIndexOf("/")+1, conAdr.lastIndexOf("/")+8));
			String tempTitle = idx.select(".ellipsis-with-reply-cnt").text();
			String conTitle = tempTitle.replaceAll(filter.toString(),"");
			String tempPrice = idx.select(".text-orange").text().replaceAll("[^0-9]", "");
			int conPrice = 0;
			if(tempPrice.matches("\\d+")) {
				conPrice = Integer.parseInt(tempPrice);
			}
			String tempDate = idx.select(".date").text();
			String conDate = null;
			if(tempDate.indexOf(":") != -1) {
				conDate = today + tempDate;
			}else {
				conDate = currentYear + tempDate + dummyTime;
			}
			String tempSuggest = idx.select(".num.num").text();
			int conSuggest = Integer.parseInt(tempSuggest);
			String conType = "quasarzone";
			
			CrawlerVO temp = new CrawlerVO(conId,conTitle,conPrice,conDate,conSuggest,conAdr,conType);
			rawListVo.add(temp);
		}
		System.out.println("퀘이사존 : "+rawListVo.size());
		return rawListVo;
	}
	
	public static Document URLFilter(String url) throws IOException {  // 크롤링 URL 정의 메서드
		String value = "Mozilla/5.0 (Linux)";

		Response response = null;
		
			response = Jsoup.connect(url).
								ignoreContentType(true).
								cookie("wcs_bt", "67921ceb44db98:1658939740").
								userAgent(value).
								timeout(1000 * timeoutValue).
								followRedirects(true).
								execute();
		
		return response.parse();
	}
	
	private static ArrayList<CrawlerVO> getCompareList(){
		System.out.println("compare In");
		String sql="SELECT conid , contitle , conprice , consuggest from contents";
		ArrayList<CrawlerVO> listBox = new ArrayList<CrawlerVO>();
		
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				int conId = rs.getInt("conid");
				String conTitle = rs.getString("contitle");
				int conPrice = Integer.parseInt(rs.getString("conprice"));
				int conSuggest = Integer.parseInt(rs.getString("consuggest"));
				
				CrawlerVO tempVo = new CrawlerVO(conId,conTitle,conPrice,conSuggest);
				
				listBox.add(tempVo);
			}
			System.out.println("listBox size : " + listBox.size());
			
		} catch (SQLException e) {
			System.out.println("비교목록 로딩 실패");
			e.printStackTrace();
		}
		
		return listBox;
	}

	private static void listDistributor(ArrayList<CrawlerVO> compareList, ArrayList<CrawlerVO> rawList) {
		int cConId=0;
		int rConId=0;
		String cConTitle=null;
		String rConTitle=null;
		int cConSuggest=0;
		int rConSuggest=0;
		int cConPrice=0;
		int rConPrice=0;
		
		if(compareList.size()!=0) {
			for(int i=0; i<compareList.size() ; i++) {
				for(int j=0;j<rawList.size();j++) {
					cConId = compareList.get(i).getConId();
					rConId = rawList.get(j).getConId();
					
					cConTitle = compareList.get(i).getConTitle();
					rConTitle = rawList.get(j).getConTitle();
					
					cConSuggest = compareList.get(i).getConSuggest();
					rConSuggest = rawList.get(j).getConSuggest();
					
					cConPrice = compareList.get(i).getConPrice();
					rConPrice = rawList.get(j).getConPrice();
					
					if(cConId == rConId) {
						//System.out.println("cId : "+i+"/"+compareList.get(i).getConId()+" / rId : "+j+"/"+rawList.get(j).getConId());
						if(!cConTitle.equals(rConTitle) || cConSuggest != rConSuggest || cConPrice != rConPrice) {
							oldList.add(rawList.get(j));
						}
						rawList.remove(j);
						
						break;
					}
				}
			}
		}
		if(rawList.size()!=0) {
			newList.addAll(rawList);
		}
	}
	
	public static void insertList() {
		String sql = "INSERT INTO contents VALUES(?,?,?,STR_TO_DATE(?,'%Y-%m-%d %H:%i'),?,?,?)";
		System.out.println("newList : "+newList.size());
		try {
			pstmt = con.prepareStatement(sql);
			
		if(newList.size()!=0) {
			for(CrawlerVO idx : newList) {
				pstmt.setInt(1, idx.getConId());
				pstmt.setString(2, idx.getConTitle());
				pstmt.setInt(3, idx.getConPrice());
				pstmt.setString(4, idx.getConDate());
				pstmt.setInt(5, idx.getConSuggest());
				pstmt.setString(6, idx.getConAdr());
				pstmt.setString(7, idx.getConType());
				pstmt.executeUpdate();
			}
		}
		} catch (SQLException e) {
			System.out.println("DB삽입 실행 오류");
			e.printStackTrace();
		}
		
	}
	
	public static void updateList() {
		String sql = "UPDATE contents SET contitle=? , conprice=? , consuggest=? WHERE conid=?";
		System.out.println("oldList : "+oldList.size());
		try {
			pstmt = con.prepareStatement(sql);
		if(oldList.size()!=0) {	
			for(CrawlerVO idx : oldList) {
				pstmt.setString(1, idx.getConTitle());
				pstmt.setInt(2, idx.getConPrice());
				pstmt.setInt(3, idx.getConSuggest());
				pstmt.setInt(4, idx.getConId());
				pstmt.executeUpdate();
			}
		}
		} catch (SQLException e) {
			System.out.println("DB 업데이트 실행 오류");
			e.printStackTrace();
		}
		
	}
		
	public static void deleteOverflow() {
		String sql = "SELECT conid FROM contents ORDER BY condate DESC LIMIT ?,500";
		ArrayList<Integer> overflow = new ArrayList<Integer>();
		
		newList.clear();
		oldList.clear();
		
		try {
				pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, limit);
			rs = pstmt.executeQuery();
		
		while(rs.next()) 
			overflow.add(rs.getInt("conid"));
		
		if(overflow.size()!=0) {
			String sql2 = "DELETE FROM contents WHERE conid=?";
			
			pstmt = con.prepareStatement(sql2);
			for(int i : overflow) {
				pstmt.setInt(1, i);
				pstmt.executeUpdate();
			}
		}
		
		} catch (SQLException e) {
			System.out.println("초과분 삭제 오류");
			e.printStackTrace();
		}
		
	}
	
}


