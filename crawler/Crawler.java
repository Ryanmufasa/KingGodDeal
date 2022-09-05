package crawler;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Crawler {
	static ArrayList<CrawlerVO> rawList = new ArrayList<>();
	
	public static void main(String[] args) {
		int pageCnt = 1;
		int period = 60 * 1000;
		
		if(args.length>0) {
			period = (Integer.parseInt(args[0])) * 1000;
		}
		
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			for(int i = 1; i<=pageCnt; i++) {
				System.out.println("idx : "+i);
				CrawlerDAO.doCrawling(i);
			} 
		}
	};
	
	ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	service.scheduleAtFixedRate(runnable, 0, period, TimeUnit.MILLISECONDS);
		
	}
	
	
	
	
}