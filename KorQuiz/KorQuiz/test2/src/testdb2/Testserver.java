package testdb2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;

import javax.xml.bind.ParseConversionEvent;


public class Testserver extends Thread {
	static Connection con = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	static String result = "";
	
	
	private static List<Testserver> threads = new ArrayList<Testserver>();
	private Socket socket;

	String url = "jdbc:mysql://127.0.0.1:3306/cultuer";
	String id = "root";
	String pw = "123d";
	
	
	public Testserver(Socket socket) {
		super();
		this.socket = socket;
		threads.add(this);
	}
	
	public void run() {
		PrintWriter out = null;
		BufferedReader in = null;
		String inType = "";
		String inKeyA = "";
		String inKeyB = "";
		String inKeyC = "";
		String inOtype = "";
		String inSol = "";
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(url, id, pw);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			while (true) {
				try {
					out = new PrintWriter(socket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String inputLine = null;
					inputLine = in.readLine();
						StringTokenizer intk = new StringTokenizer(inputLine, "/");
						inType = intk.nextToken();
						if (inType.equals("sol")) {
							inSol = intk.nextToken();
						} else {
							inKeyA = intk.nextToken();
							inKeyB = intk.nextToken();
							inKeyC = intk.nextToken();
							inOtype = intk.nextToken();
						}
						
						if (inType.equals("Atype")) {
							String adata = selectA1(inKeyA, inKeyB, inKeyC);
							String bdata = selectB1(adata, inKeyA, inKeyB);
							String cdata = selectC1(adata, inKeyA, inKeyC);	
							String servermsg = bdata+"!"+cdata;
							out.println(servermsg);
						} else if (inType.equals("Btype")) {
							String adata = selectA2(inKeyA, inKeyB, inKeyC);
							String bdata = selectB2(adata, inKeyA, inKeyB, inOtype);
							String cdata = selectC2(adata, inKeyA, inKeyC, inOtype);
							String servermsg = bdata+"!"+cdata;
							out.println(servermsg);	
						} else if (inType.equals("Ctype")) {
							String adata = selectA3(inKeyA, inKeyB,inKeyC);
							out.println(adata);
						} else if (inType.equals("sol")) {
							String sols = resultp(inSol);
							out.println(sols);
						}
				} catch (Exception e) {
					socket.close();
					threads.remove(this);
					return;
				}
			}
			
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	//// A유형
	
	static String selectA1 (String keyA, String keyB, String keyC) {
		String returnA1Data = "";
		String dataA1Name = "";
		int dataA1Start = 0;
		int dataA1End = 0;
		String dataA2Name = "";
		
		ArrayList<String> dataA2List = new ArrayList<>();
		
		int randomA1 = 0;
		int randomA2 = 0;
		
		int maxA = 0;
		int cBreakFlag = 0;		
	
		try {
			stmt = null;
			rs = null;
			result = "";
			String countASQL = "select count(*) from "+keyA+"";
			stmt = con.createStatement();
			rs = stmt.executeQuery(countASQL);
			while (rs.next()) {
				result = rs.getString(1);
			}
			maxA = Integer.parseInt(result);
			
			for (;;) { // A1 선택
				randomA1 = 1+(int)(Math.random() * maxA);
				stmt = null;
				rs = null;
				result = "";
				
				String selectA1SQL = "select * from king where kingid="+randomA1+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(selectA1SQL);
				while (rs.next()) {
					result = rs.getString("kingname")+"/"+rs.getString("kingstart")
					+"/"+rs.getString("kingend");
					StringTokenizer stkA1 = new StringTokenizer(result, "/");
					dataA1Name = stkA1.nextToken();
					dataA1Start = Integer.parseInt(stkA1.nextToken());
					dataA1End = Integer.parseInt(stkA1.nextToken());
				}
				
				// 선택된 A1이 3개 이상의 C데이터와 1개 이상의 B데이터를 가지고 있는지 확인
				stmt = null;
				rs = null;
				result = "";
				String countA1BSQL = "select count(*) from "+keyB+" where "+keyB+""+keyA+"='"+dataA1Name+"'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(countA1BSQL);
				while (rs.next()) {
					result = rs.getString(1);
				}
				int countA1B = Integer.parseInt(result);
				if (countA1B > 0) { // A1의 B 데이터가 1개 이상일때, C 데이터를 3개 이상 가지는지 확인
					stmt = null;
					rs = null;
					result = "";
					String countA1CSQL = "select count(*) from "+keyC+" where "+keyC+""+keyA+"='"+dataA1Name+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(countA1CSQL);
					while (rs.next()) {
						result = rs.getString(1);
					}
					int countA1C = Integer.parseInt(result);
					if (countA1C >= 3){
						cBreakFlag = 1;
					}
					if (cBreakFlag == 1) {
						cBreakFlag = 0;
						break;
					}
				}				
			} // A1 선택 반복문 종료			
			 
				
				// A2를 뽑기 위한 반복문
			stmt = null;
			rs = null;
			result = "";
			String selectA2SQL = "select * from king where ((kingstart>=("+dataA1Start+"-100)) && (kingend<=("+dataA1End+"+100))) && "+keyA+"name not like '"+dataA1Name+"'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectA2SQL);			
			while (rs.next()) {
				result = rs.getString("kingname");
				dataA2List.add(result);
			}
			for (;;) {
				randomA2 = (int)(Math.random() * (dataA2List.size()-1));
				dataA2Name = dataA2List.get(randomA2);
				stmt = null;
				rs = null;
				result = "";
				String countA2SQL = "select count(*) from "+keyC+" where "+keyC+"king = '"+dataA2Name+"'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(countA2SQL);
				while (rs.next()) {
					result = rs.getString(1);
				}
				int countA2C = Integer.parseInt(result);
				if (countA2C > 0)
					break;
			} // A2 시기 선택 종료

		} catch (SQLException e) {}
		
		returnA1Data = dataA1Name+"/"+dataA2Name;
		return returnA1Data;
	} // A1 A2 반복문 종료
		
				/////////////////////////////////////////////////////////
		// 보기 데이터 선택
	
	static String selectB1 (String dataA, String keyA, String keyB) {
		String returnB1Data = "";
		String dataA1 = "";
		String bCname = "";
		String bEname = "";
		String outType="";
		double randomSil = Math.random();
		int intSil=(int)(randomSil*2)+1;
		
		if (intSil == 1)
			outType="title";
		
		else if(intSil == 2)
			outType="content";
		
		
		stmt = null;
		rs = null;
		result = "";
		
		StringTokenizer stDataA = new StringTokenizer(dataA, "/");
		dataA1 = stDataA.nextToken();
						

		try {
			stmt = null;
			rs = null;
			result = "";
			
			
			String selectABSQL = "select * from "+keyB+" where "+keyB+""+keyA+" = '"+dataA1+"'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectABSQL);
			
			if (keyB.equals("culture")) {
				ArrayList<String> bCnameList = new ArrayList<>();
				while (rs.next()) {
					result = rs.getString("culturename")+"$"+rs.getInt("cultureid");
					bCnameList.add(result);
				}				
				int bCrand = (int)(Math.random() * (bCnameList.size()-1));
				bCname = bCnameList.get(bCrand);
				returnB1Data = bCname;
			} else if (keyB.equals("event")) {
				StringTokenizer stB;
				String bEname2 = "";
				String bEpeople = "";
				String bEyear = "";
				String bEplace = "";
				String bEwhat = "";
				String bEhow = "";
				String bEreason1 = "";
				String bEreason2 = "";
				String bEreason3 = "";
				String bEking = "";
				int bEid = 0;
				
				ArrayList<String> bEnameList = new ArrayList<>();
				ArrayList<String> bEpeopleList = new ArrayList<>();
				ArrayList<String> bEyearList = new ArrayList<>();
				ArrayList<String> bEplaceList = new ArrayList<>();
				ArrayList<String> bEwhatList = new ArrayList<>();
				ArrayList<String> bEhowList = new ArrayList<>();
				ArrayList<String> bEreason1List = new ArrayList<>();
				ArrayList<String> bEreason2List = new ArrayList<>();
				ArrayList<String> bEreason3List = new ArrayList<>();
				ArrayList<String> bEkingList = new ArrayList<>();
				ArrayList<Integer> bEidList = new ArrayList<>();
				
				while (rs.next()) {
					result = rs.getString(1)+"/"+rs.getString(2)+"/"+rs.getInt(3)
					+"/"+rs.getString(4)+"/"+rs.getString(6)+"/"+rs.getString(7)
					+"/"+rs.getString(5)+"/"+rs.getString(9)+"/"+rs.getString(10)+"/"+rs.getString(8)+"/"+rs.getInt("eventid");
					
					stB = new StringTokenizer (result,"/");
					bEname2 = stB.nextToken();
					bEpeople = stB.nextToken();
					bEyear = stB.nextToken();
					bEplace = stB.nextToken();
					bEwhat = stB.nextToken();
					bEhow = stB.nextToken();
					bEreason1 = stB.nextToken();
					bEreason2 = stB.nextToken();
					bEreason3 = stB.nextToken();
					bEking = stB.nextToken();
					bEid = Integer.parseInt(stB.nextToken());
					
					bEnameList.add(bEname2);
					bEpeopleList.add(bEpeople);
					bEyearList.add(bEyear);
					bEplaceList.add(bEplace);
					bEwhatList.add(bEwhat);
					bEhowList.add(bEhow);
					bEreason1List.add(bEreason1);
					bEreason2List.add(bEreason2);
					bEreason3List.add(bEreason3);
					bEkingList.add(bEking);
					bEidList.add(bEid);
				}
				int bErand = (int)(Math.random() * (bEnameList.size()-1));
				if (outType.equals("title")) {
					bEname = bEnameList.get(bErand)+"$"+bEidList.get(bErand);
				} else if (outType.equals("content")){
					
					if (bEkingList.get(bErand).equals(bEpeopleList.get(bErand))) {
						bEname = nullFilter("year#"+bEyearList.get(bErand))+nullFilter("place#"+bEplaceList.get(bErand))+nullFilter("reason1#"+bEreason1List.get(bErand)+"!"+bEreason2List.get(bErand)+"!"+bEreason3List.get(bErand))
							+nullFilter("what#"+bEwhatList.get(bErand))+nullFilter("how#"+bEhowList.get(bErand))+"$"+bEidList.get(bErand);
					} else {
						bEname = nullFilter("name#"+bEpeopleList.get(bErand))+nullFilter("year#"+bEyearList.get(bErand))+nullFilter("place#"+bEplaceList.get(bErand))+nullFilter("reason1#"+bEreason1List.get(bErand)+"!"+bEreason2List.get(bErand)+"!"+bEreason3List.get(bErand))
						+nullFilter("what#"+bEwhatList.get(bErand))+nullFilter("how#"+bEhowList.get(bErand))+"$"+bEidList.get(bErand);
					}
				}
				returnB1Data = bEname;
			}
			} catch (SQLException e) {
		}				
		return returnB1Data;
	}
				
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 선택지 데이터 선택
	static String selectC1 (String dataA, String keyA, String keyC) {
		String returnC1Data = "";
		String dataA1 = "";
		String dataA2 = "";
		String a1c1 = "";
		String a1c2 = "";
		String a1c3 = "";
		String a2c1 = "";
		StringTokenizer stDataA = new StringTokenizer(dataA, "/");
		
		dataA1 = stDataA.nextToken();
		dataA2 = stDataA.nextToken();
		String outType="";
		double randomSil = Math.random();
		int intSil=(int)(randomSil*2)+1;
		
		if (intSil == 1)
			outType="title";
		
		else if(intSil == 2)
			outType="content";
						
		try {
			// dataA1,2를 가지는 keyB 데이터 요청
			stmt = null;
			rs = null;
			result = "";
			String selectA1CSQL = "select * from "+keyC+" where "+keyC+""+keyA+" = '"+dataA1+"'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectA1CSQL);
			if(keyC.equals("culture")) {
				
				ArrayList<String> a1cnameList = new ArrayList<>();				
				ArrayList<String> a2cnameList = new ArrayList<>();
				
				while (rs.next()) {
					result = rs.getString("culturename")+"$"+rs.getInt("cultureid");
					a1cnameList.add(result);	
				}
				int[] randoms = new int[10];
				int z=2;

				for (;;) {
					randoms[z] = (int)(Math.random() * a1cnameList.size());
					if (randoms[z]!=randoms[z+1] && randoms[z]!=randoms[z+2]) {
						z = z - 1;
					}
					if (z<0)
						break;
				}
				a1c1 = a1cnameList.get(randoms[0]);
				a1c2 = a1cnameList.get(randoms[1]);
				a1c3 = a1cnameList.get(randoms[2]);
							

				stmt = null;
				rs = null;
				result = "";
				String selectA2CSQL = "select * from culture where culture"+keyA+" = '"+dataA2+"'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(selectA2CSQL);
				while (rs.next()) {
					result = rs.getString("culturename")+"$"+rs.getInt("cultureid");					
					a2cnameList.add(result);
				}
				int a2crandom = (int)(Math.random() * (a2cnameList.size()-1));				
				a2c1 = a2cnameList.get(a2crandom);				
					
			} else if (keyC.equals("event")) {
				if (outType.equals("title")) {
					ArrayList<String> a1enameList = new ArrayList<>();
					ArrayList<String> a2enameList = new ArrayList<>();					
					while (rs.next()) {
						result = rs.getString("eventname")+"$"+rs.getInt("eventid");
						a1enameList.add(result);
					}
					int[] randoms = new int[8];
					int z=2;
					for (;;) {
						randoms[z] = (int)(Math.random() * a1enameList.size());
						if (randoms[z]!=randoms[z+1] && randoms[z]!=randoms[z+2]) {
							z = z - 1;
						}
						if (z<0)
							break;
					}
					a1c1 = a1enameList.get(randoms[0]);
					a1c2 = a1enameList.get(randoms[1]);
					a1c3 = a1enameList.get(randoms[2]);
					
					
					stmt = null;
					rs = null;
					result = "";
					String selectA2CSQL = "select * from event where event"+keyA+" = '"+dataA2+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(selectA2CSQL);
					while (rs.next()) {
						result = rs.getString("eventname")+"$"+rs.getInt("eventid");
						a2enameList.add(result);
					}
					int a2rand = (int)(Math.random() * (a2enameList.size()-1));				
					a2c1 = a2enameList.get(a2rand);
				} else if (outType.equals("content")) {
					StringTokenizer stC1;
					String ename = "";
					String epeople = "";
					String eyear = "";
					String eplace = "";
					String ewhat = "";
					String ehow = "";
					String ereason1 = "";
					String ereason2 = "";
					String ereason3 = "";
					String eking = "";
					int eid = 0;
					
					ArrayList<String> enameList = new ArrayList<>();
					ArrayList<String> epeopleList = new ArrayList<>();
					ArrayList<String> eyearList = new ArrayList<>();
					ArrayList<String> eplaceList = new ArrayList<>();
					ArrayList<String> ewhatList = new ArrayList<>();
					ArrayList<String> ehowList = new ArrayList<>();
					ArrayList<String> ereason1List = new ArrayList<>();
					ArrayList<String> ereason2List = new ArrayList<>();
					ArrayList<String> ereason3List = new ArrayList<>();
					ArrayList<String> ekingList = new ArrayList<>();
					ArrayList<Integer> eidList = new ArrayList<>();
					
					while (rs.next()) { // 2 = people, 8 = king
						result = rs.getString(1)+"/"+rs.getString(2)+"/"+rs.getInt(3)
						+"/"+rs.getString(4)+"/"+rs.getString(6)+"/"+rs.getString(7)
						+"/"+rs.getString(5)+"/"+rs.getString(9)+"/"+rs.getString(10)+"/"+rs.getString(8)+"/"+rs.getInt("eventid");
						stC1 = new StringTokenizer (result,"/");
						ename = stC1.nextToken();
						epeople = stC1.nextToken();
						eyear = stC1.nextToken();
						eplace = stC1.nextToken();
						ewhat = stC1.nextToken();
						ehow = stC1.nextToken();
						ereason1 = stC1.nextToken();
						ereason2 = stC1.nextToken();
						ereason3 = stC1.nextToken();
						eking = stC1.nextToken();
						eid = Integer.parseInt(stC1.nextToken());
						
						enameList.add(ename);
						epeopleList.add(epeople);
						eyearList.add(eyear);
						eplaceList.add(eplace);
						ewhatList.add(ewhat);
						ehowList.add(ehow);
						ereason1List.add(ereason1);
						ereason2List.add(ereason2);
						ereason3List.add(ereason3);
						ekingList.add(eking);
						eidList.add(eid);
					}

					int[] randoms = new int[8];
					int z=3;
					for (;;) {
						randoms[z] = (int)(Math.random() * enameList.size());
						if (randoms[z]!=randoms[z+1] && randoms[z]!=randoms[z+2] ) {
							z = z - 1;
						}
						if (z<0)
							break;
					}
					
					ArrayList<String> tempName = new ArrayList<>();
					for (int t=0; t<4; t++) {
						String temps = "";
						StringTokenizer nameTK = new StringTokenizer(epeopleList.get(randoms[t]), "#");
						temps = nameTK.nextToken();
						tempName.add(temps);
					}	
					
					if (ekingList.get(randoms[0]).equals(tempName.get(0)) || keyA.equals("people")) {
						a1c1 = nullFilter("place#"+eplaceList.get(randoms[0]))+nullFilter("reason#"+ereason1List.get(randoms[0])+"!"+ereason2List.get(randoms[0])+"!"+ereason3List.get(randoms[0]))
							+nullFilter("what#"+ewhatList.get(randoms[0]))+nullFilter("how#"+ehowList.get(randoms[0]))+"$"+eidList.get(randoms[0]);
					} else if (ekingList.get(randoms[0]) != tempName.get(0)) {
						a1c1 = nullFilter("name#"+epeopleList.get(randoms[0]))+nullFilter("place#"+eplaceList.get(randoms[0]))+nullFilter("reason#"+ereason1List.get(randoms[0])+"!"+ereason2List.get(randoms[0])+"!"+ereason3List.get(randoms[0]))
						+nullFilter("what#"+ewhatList.get(randoms[0]))+nullFilter("how#"+ehowList.get(randoms[0]))+"$"+eidList.get(randoms[0]);
					}
					if (ekingList.get(randoms[1]).equals(tempName.get(1)) || keyA.equals("people")) {
						a1c2 = nullFilter("place#"+eplaceList.get(randoms[1]))+nullFilter("reason#"+ereason1List.get(randoms[1])+"!"+ereason2List.get(randoms[1])+"!"+ereason3List.get(randoms[1]))
							+nullFilter("what#"+ewhatList.get(randoms[1]))+nullFilter("how#"+ehowList.get(randoms[1]))+"$"+eidList.get(randoms[1]);
					} else if (ekingList.get(randoms[1]) != tempName.get(1)) {
						a1c2 = nullFilter("name#"+epeopleList.get(randoms[1]))+nullFilter("place#"+eplaceList.get(randoms[1]))+nullFilter("reason#"+ereason1List.get(randoms[1])+"!"+ereason2List.get(randoms[1])+"!"+ereason3List.get(randoms[1]))
						+nullFilter("what#"+ewhatList.get(randoms[1]))+nullFilter("how#"+ehowList.get(randoms[1]))+"$"+eidList.get(randoms[1]);
					}
					if (ekingList.get(randoms[2]).equals(tempName.get(1)) || keyA.equals("people")) {
						a1c3 = nullFilter("place#"+eplaceList.get(randoms[2]))+nullFilter("reason#"+ereason1List.get(randoms[2])+"!"+ereason2List.get(randoms[2])+"!"+ereason3List.get(randoms[2]))
							+nullFilter("what#"+ewhatList.get(randoms[2]))+nullFilter("how#"+ehowList.get(randoms[2]))+"$"+eidList.get(randoms[2]);
					} else if (ekingList.get(randoms[2]) != tempName.get(1)) {
						a1c3 = nullFilter("name#"+epeopleList.get(randoms[2]))+nullFilter("place#"+eplaceList.get(randoms[2]))+nullFilter("reason#"+ereason1List.get(randoms[2])+"!"+ereason2List.get(randoms[2])+"!"+ereason3List.get(randoms[2]))
						+nullFilter("what#"+ewhatList.get(randoms[2]))+nullFilter("how#"+ehowList.get(randoms[2]))+"$"+eidList.get(randoms[2]);
					}
					
					
					stmt = null;
					rs = null;
					result = "";
					String selectA2BSQL = "select * from event where event"+keyA+" = '"+dataA2+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(selectA2BSQL);
					
					StringTokenizer stC2;
					String e2name = "";
					String e2people = "";
					String e2year = "";
					String e2place = "";
					String e2what = "";
					String e2how = "";
					String e2reason1 = "";
					String e2reason2 = "";
					String e2reason3 = "";
					String e2king = "";
					int e2id = 0;
					
					ArrayList<String> e2nameList = new ArrayList<>();
					ArrayList<String> e2peopleList = new ArrayList<>();
					ArrayList<String> e2yearList = new ArrayList<>();
					ArrayList<String> e2placeList = new ArrayList<>();
					ArrayList<String> e2whatList = new ArrayList<>();
					ArrayList<String> e2howList = new ArrayList<>();
					ArrayList<String> e2reason1List = new ArrayList<>();
					ArrayList<String> e2reason2List = new ArrayList<>();
					ArrayList<String> e2reason3List = new ArrayList<>();
					ArrayList<String> e2kingList = new ArrayList<>();
					ArrayList<Integer> e2idList = new ArrayList<>();
					
					while (rs.next()) { // 2 = people, 8 = king
						result = rs.getString(1)+"/"+rs.getString(2)+"/"+rs.getInt(3)
						+"/"+rs.getString(4)+"/"+rs.getString(6)+"/"+rs.getString(7)
						+"/"+rs.getString(5)+"/"+rs.getString(9)+"/"+rs.getString(10)+"/"+rs.getString(8)+"/"+rs.getInt("eventid");
						stC2 = new StringTokenizer (result,"/");
						e2name = stC2.nextToken();
						e2people = stC2.nextToken();
						e2year = stC2.nextToken();
						e2place = stC2.nextToken();
						e2what = stC2.nextToken();
						e2how = stC2.nextToken();
						e2reason1 = stC2.nextToken();
						e2reason2 = stC2.nextToken();
						e2reason3 = stC2.nextToken();
						e2king = stC2.nextToken();
						e2id = Integer.parseInt(stC2.nextToken());
						
						e2nameList.add(e2name);
						e2peopleList.add(e2people);
						e2yearList.add(e2year);
						e2placeList.add(e2place);
						e2whatList.add(e2what);
						e2howList.add(e2how);
						e2reason1List.add(e2reason1);
						e2reason2List.add(e2reason2);
						e2reason3List.add(e2reason3);
						e2kingList.add(e2king);
						e2idList.add(e2id);
					}
					
					int randoms2 = (int)(Math.random() * (e2nameList.size()-1));
					
					String tempName2 = "";
					StringTokenizer nameTK = new StringTokenizer(e2peopleList.get(randoms2), "#");
					tempName2 = nameTK.nextToken();
					
					if (e2kingList.get(randoms2).equals(tempName2) || keyA.equals("people")) {
						a2c1 = nullFilter("place#"+e2placeList.get(randoms2))+nullFilter("reason#"+e2reason1List.get(randoms2)+"!"+e2reason2List.get(randoms2)+"!"+e2reason3List.get(randoms2))
							+nullFilter("what#"+e2whatList.get(randoms2))+nullFilter("how#"+e2howList.get(randoms2))+"$"+e2idList.get(randoms2);
					} else if (e2kingList.get(randoms2) != tempName2) {
						a2c1 = nullFilter("name#"+e2peopleList.get(randoms2))+nullFilter("place#"+e2placeList.get(randoms2))+nullFilter("reason#"+e2reason1List.get(randoms2)+"!"+e2reason2List.get(randoms2)+"!"+e2reason3List.get(randoms2))
						+nullFilter("what#"+e2whatList.get(randoms2))+nullFilter("how#"+e2howList.get(randoms2))+"$"+e2idList.get(randoms2);
					}
				}
			}			
		} catch (SQLException e) {
		}

		returnC1Data = a1c1+"/"+a1c2+"/"+a1c3+"!"+a2c1;
	
		return returnC1Data;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//// B유형
	
	static String selectA2 (String keyA,String keyB, String keyC) {
		String returnA1Data = "";
		String dataA1Name = "";
		int dataA1Id = 0;
		String dataA2Name = "";
		
		int randomA1 = 0;
		int randomA2 = 0;
		
		int maxA = 0;
		int a = 0;
		
		int bfA2 = 0; // A2 반복문 탈출 Flag		
	
		try {
			stmt = null;
			rs = null;
			result = "";
			String countASQL = "select count(*) from "+keyA+"";
			stmt = con.createStatement();
			rs = stmt.executeQuery(countASQL);
			while (rs.next()) {
				result =rs.getString(1);
			}
			maxA = Integer.parseInt(result);
			
			//Data A1, A2를 뽑기위한 반복문
			while (a <= maxA) {
				a += 1;
				// A1을 뽑기 위한 반복문
				for (;;) {
					randomA1 = 1+(int)(Math.random() * maxA);
					stmt = null;
					rs = null;
					result = "";
					if (keyA.equals("people")) {
						String selectA1SQL = "select * from people where peopleid="+randomA1+"";
						stmt = con.createStatement();
						rs = stmt.executeQuery(selectA1SQL);
						while (rs.next()) {
							result = rs.getString("peoplename")+"/"+rs.getString("peopleid");
							StringTokenizer stkA1 = new StringTokenizer(result, "/");
							dataA1Name = stkA1.nextToken();
							dataA1Id = Integer.parseInt(stkA1.nextToken());
						}
					}
					stmt = null;
					rs = null;
					result = "";
					String countA1BSQL = "select count(*) from "+keyB+" where "+keyB+""+keyA+"='"+dataA1Name+"'";
					String countA1CSQL = "select count(*) from "+keyC+" where "+keyC+""+keyA+"='"+dataA1Name+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(countA1BSQL);
					while (rs.next()) {
						result = rs.getString(1);
					}		
					
					int countA1B = Integer.parseInt(result);	
					rs = stmt.executeQuery(countA1CSQL);
					while (rs.next()) {
						result = rs.getString(1);
					}
					int countA1C = Integer.parseInt(result);
					if (countA1B >= 1 && countA1C >= 3)
						break;
				} // A1 반복문 종료
				
				for (;;) {
					if (keyA.equals("people")) {
						stmt = null;
						rs = null;
						result = "";
						String selectA2SQL = "select * from people where ((peopleid<=("+dataA1Id+"+2)) && (peopleid>=("+dataA1Id+"-2))) && "+keyA+"name not like '"+dataA1Name+"'";
						stmt = con.createStatement();
						rs = stmt.executeQuery(selectA2SQL);
						ArrayList<String> dataA2List = new ArrayList<>();
						while (rs.next()) {
							result = rs.getString("peoplename");
							dataA2List.add(result);
						}
						int loopCount = 0;
						for (;;) {
							loopCount += 1;
							randomA2 = (int)(Math.random() * (dataA2List.size()-1));
							dataA2Name = dataA2List.get(randomA2);
							stmt = null;
							rs = null;
							result = "";
							String countA2SQL1 = "select count(*) from "+keyC+" where "+keyC+"peoplename = '"+dataA2Name+"'";
							stmt = con.createStatement();
							rs = stmt.executeQuery(countA2SQL1);
							while (rs.next()) {
								result = rs.getString(1);
							}
							int countA2B = Integer.parseInt(result);
							if (countA2B > 0) {
								bfA2 = 1;
								break;
							}
							if (loopCount == 30) {
								bfA2 = 2;
								break;
							}
						}// keyA가 king일때 A2 루프 끝
					} // A2 반복문 종료
				
				if (bfA2 == 1) {
					bfA2 = 0;
					break;
				}
			} // A1 A2 반복문 종료
			
		}
			
		}		
		catch (SQLException e) {}
		
		returnA1Data = dataA1Name+"/"+dataA2Name;
		return returnA1Data;
	}
	
	
	static String selectB2 (String dataA, String keyA, String keyB, String outType) {
		String returnBData = "";
		String dataA1 = "";
		
		String a1b1 = "";
		
		
		stmt = null;
		rs = null;
		result = "";
		
		StringTokenizer stDataA = new StringTokenizer(dataA, "/");
		dataA1 = stDataA.nextToken();
						
		try {
			// dataA1,2를 가지는 keyB 데이터 요청
			String selectA1BSQL = "select * from "+keyB+" where "+keyB+""+keyA+" = '"+dataA1+"'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectA1BSQL);
			if(keyB.equals("event")) {
				
				ArrayList<String> a1cnameList = new ArrayList<>();				
				
				while (rs.next()) {
					result = rs.getString("eventname")+"$"+rs.getInt("eventid");
					a1cnameList.add(result);	
				}
				
				int a1crandom = (int)(Math.random() * (a1cnameList.size()-1));				
				a1b1 = a1cnameList.get(a1crandom);
				
				
				
			} else if (outType.equals("content")) {
					StringTokenizer stB1;
					String ename = "";
					String epeople = "";
					String eyear = "";
					String eplace = "";
					String ewhat = "";
					String ehow = "";
					String ereason1 = "";
					String ereason2 = "";
					String ereason3 = "";
					String eking = "";
					int eid = 0;
					
					ArrayList<String> enameList = new ArrayList<>();
					ArrayList<String> epeopleList = new ArrayList<>();
					ArrayList<String> eyearList = new ArrayList<>();
					ArrayList<String> eplaceList = new ArrayList<>();
					ArrayList<String> ewhatList = new ArrayList<>();
					ArrayList<String> ehowList = new ArrayList<>();
					ArrayList<String> ereason1List = new ArrayList<>();
					ArrayList<String> ereason2List = new ArrayList<>();
					ArrayList<String> ereason3List = new ArrayList<>();
					ArrayList<String> ekingList = new ArrayList<>();
					ArrayList<Integer> eidList = new ArrayList<>();
					
					while (rs.next()) { // 2 = people, 8 = king
						result = rs.getString(1)+"/"+rs.getString(2)+"/"+rs.getInt(3)
						+"/"+rs.getString(4)+"/"+rs.getString(6)+"/"+rs.getString(7)
						+"/"+rs.getString(5)+"/"+rs.getString(9)+"/"+rs.getString(10)+"/"+rs.getString(8)+"/"+rs.getInt("eventid");
						stB1 = new StringTokenizer (result,"/");
						ename = stB1.nextToken();
						epeople = stB1.nextToken();
						eyear = stB1.nextToken();
						eplace = stB1.nextToken();
						ewhat = stB1.nextToken();
						ehow = stB1.nextToken();
						ereason1 = stB1.nextToken();
						ereason2 = stB1.nextToken();
						ereason3 = stB1.nextToken();
						eking = stB1.nextToken();
						eid = Integer.parseInt(stB1.nextToken());
						
						enameList.add(ename);
						epeopleList.add(epeople);
						eyearList.add(eyear);
						eplaceList.add(eplace);
						ewhatList.add(ewhat);
						ehowList.add(ehow);
						ereason1List.add(ereason1);
						ereason2List.add(ereason2);
						ereason3List.add(ereason3);
						ekingList.add(eking);
						eidList.add(eid);
					}
					int randoms = (int)(Math.random() * (enameList.size()-1));
					
					String tempName = "";
					StringTokenizer nameTK = new StringTokenizer(epeopleList.get(randoms), "#");
					tempName = nameTK.nextToken();
					
					if (ekingList.get(randoms).equals(tempName) || keyA.equals("people")) {
						a1b1 = nullFilter("place#"+eplaceList.get(randoms))+nullFilter("reason#"+ereason1List.get(randoms)+"!"+ereason2List.get(randoms)+"!"+ereason3List.get(randoms))
							+nullFilter("what#"+ewhatList.get(randoms))+nullFilter("how#"+ehowList.get(randoms))+"$"+eidList.get(randoms);
					} else if (ekingList.get(randoms) != tempName) {
						a1b1 = nullFilter("name#"+epeopleList.get(randoms))+nullFilter("place#"+eplaceList.get(randoms))+nullFilter("reason#"+ereason1List.get(randoms)+"!"+ereason2List.get(randoms)+"!"+ereason3List.get(randoms))
						+nullFilter("what#"+ewhatList.get(randoms))+nullFilter("how#"+ehowList.get(randoms))+"$"+eidList.get(randoms);
					}
				
			}			
		} catch (SQLException e) {
		}

		returnBData = a1b1;
	
		return returnBData;
	}
				
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 선택지 데이터 선택
	static String selectC2 (String dataA, String keyA, String keyC, String outType) {
		String returnCData = "";
		String dataA1 = "";
		String dataA2 = "";
		
		String a1c1 = "";
		String a1c2 = "";
		String a1c3 = "";
		String a2c1 = "";
		
		stmt = null;
		rs = null;
		result = "";
		
		StringTokenizer stDataA = new StringTokenizer(dataA, "/");
		dataA1 = stDataA.nextToken();
		dataA2 = stDataA.nextToken();					
		
		try {
			// dataA1,2를 가지는 keyB 데이터 요청
			String selectA1CSQL = "select * from "+keyC+" where "+keyC+""+keyA+" = '"+dataA1+"'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectA1CSQL);
			
			if (keyC.equals("event")) {
				if (outType.equals("title")) {
					ArrayList<String> a1enameList = new ArrayList<>();
					ArrayList<String> a2enameList = new ArrayList<>();					
					while (rs.next()) {
						result = rs.getString("eventname")+"$"+rs.getInt("eventid");
						a1enameList.add(result);
					}
					int[] randoms = new int[8];
					int z=2;
					for (;;) {
						randoms[z] = (int)(Math.random() * a1enameList.size());
						if (randoms[z]!=randoms[z+1] && randoms[z]!=randoms[z+2]) {
							z = z - 1;
						}
						if (z<0)
							break;
					}
					a1c1 = a1enameList.get(randoms[0]);
					a1c2 = a1enameList.get(randoms[1]);
					a1c3 = a1enameList.get(randoms[2]);
					
					
					stmt = null;
					rs = null;
					result = "";
					String selectA2CSQL = "select * from event where event"+keyA+" = '"+dataA2+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(selectA2CSQL);
					while (rs.next()) {
						result = rs.getString("eventname")+"$"+rs.getInt("eventid");
						a2enameList.add(result);
					}
					int a2rand = (int)(Math.random() * (a2enameList.size()-1));				
					a2c1 = a2enameList.get(a2rand);
				} else if (outType.equals("content")) {
					StringTokenizer stB1;
					String ename = "";
					String epeople = "";
					String eyear = "";
					String eplace = "";
					String ewhat = "";
					String ehow = "";
					String ereason1 = "";
					String ereason2 = "";
					String ereason3 = "";
					String eking = "";
					int eid = 0;
					
					ArrayList<String> enameList = new ArrayList<>();
					ArrayList<String> epeopleList = new ArrayList<>();
					ArrayList<String> eyearList = new ArrayList<>();
					ArrayList<String> eplaceList = new ArrayList<>();
					ArrayList<String> ewhatList = new ArrayList<>();
					ArrayList<String> ehowList = new ArrayList<>();
					ArrayList<String> ereason1List = new ArrayList<>();
					ArrayList<String> ereason2List = new ArrayList<>();
					ArrayList<String> ereason3List = new ArrayList<>();
					ArrayList<String> ekingList = new ArrayList<>();
					ArrayList<Integer> eidList = new ArrayList<>();
					
					while (rs.next()) { // 2 = people, 8 = king
						result = rs.getString(1)+"/"+rs.getString(2)+"/"+rs.getInt(3)
						+"/"+rs.getString(4)+"/"+rs.getString(6)+"/"+rs.getString(7)
						+"/"+rs.getString(5)+"/"+rs.getString(9)+"/"+rs.getString(10)+"/"+rs.getString(8)+"/"+rs.getInt("eventid");
						stB1 = new StringTokenizer (result,"/");
						ename = stB1.nextToken();
						epeople = stB1.nextToken();
						eyear = stB1.nextToken();
						eplace = stB1.nextToken();
						ewhat = stB1.nextToken();
						ehow = stB1.nextToken();
						ereason1 = stB1.nextToken();
						ereason2 = stB1.nextToken();
						ereason3 = stB1.nextToken();
						eking = stB1.nextToken();
						eid = Integer.parseInt(stB1.nextToken());
						
						enameList.add(ename);
						epeopleList.add(epeople);
						eyearList.add(eyear);
						eplaceList.add(eplace);
						ewhatList.add(ewhat);
						ehowList.add(ehow);
						ereason1List.add(ereason1);
						ereason2List.add(ereason2);
						ereason3List.add(ereason3);
						ekingList.add(eking);
						eidList.add(eid);
					}

					int[] randoms = new int[8];
					int z=3;
					for (;;) {
						randoms[z] = (int)(Math.random() * enameList.size());
						if (randoms[z]!=randoms[z+1] && randoms[z]!=randoms[z+2] ) {
							z = z - 1;
						}
						if (z<0)
							break;
					}
					
					ArrayList<String> tempName = new ArrayList<>();
					for (int t=0; t<4; t++) {
						String temps = "";
						StringTokenizer nameTK = new StringTokenizer(epeopleList.get(randoms[t]), "#");
						temps = nameTK.nextToken();
						tempName.add(temps);
					}	
					
					if (ekingList.get(randoms[0]).equals(tempName.get(0)) || keyA.equals("people")) {
						a1c1 = nullFilter("place#"+eplaceList.get(randoms[0]))+nullFilter("reason#"+ereason1List.get(randoms[0])+"!"+ereason2List.get(randoms[0])+"!"+ereason3List.get(randoms[0]))
							+nullFilter("what#"+ewhatList.get(randoms[0]))+nullFilter("how#"+ehowList.get(randoms[0]))+"$"+eidList.get(randoms[0]);
					} else if (ekingList.get(randoms[0]) != tempName.get(0)) {
						a1c1 = nullFilter("name#"+epeopleList.get(randoms[0]))+nullFilter("place#"+eplaceList.get(randoms[0]))+nullFilter("reason#"+ereason1List.get(randoms[0])+"!"+ereason2List.get(randoms[0])+"!"+ereason3List.get(randoms[0]))
						+nullFilter("what#"+ewhatList.get(randoms[0]))+nullFilter("how#"+ehowList.get(randoms[0]))+"$"+eidList.get(randoms[0]);
					}
					if (ekingList.get(randoms[1]).equals(tempName.get(1)) || keyA.equals("people")) {
						a1c2 = nullFilter("place#"+eplaceList.get(randoms[1]))+nullFilter("reason#"+ereason1List.get(randoms[1])+"!"+ereason2List.get(randoms[1])+"!"+ereason3List.get(randoms[1]))
							+nullFilter("what#"+ewhatList.get(randoms[1]))+nullFilter("how#"+ehowList.get(randoms[1]))+"$"+eidList.get(randoms[1]);
					} else if (ekingList.get(randoms[1]) != tempName.get(1)) {
						a1c2 = nullFilter("name#"+epeopleList.get(randoms[1]))+nullFilter("place#"+eplaceList.get(randoms[1]))+nullFilter("reason#"+ereason1List.get(randoms[1])+"!"+ereason2List.get(randoms[1])+"!"+ereason3List.get(randoms[1]))
						+nullFilter("what#"+ewhatList.get(randoms[1]))+nullFilter("how#"+ehowList.get(randoms[1]))+"$"+eidList.get(randoms[1]);
					}
					if (ekingList.get(randoms[2]).equals(tempName.get(1)) || keyA.equals("people")) {
						a1c3 = nullFilter("place#"+eplaceList.get(randoms[2]))+nullFilter("reason#"+ereason1List.get(randoms[2])+"!"+ereason2List.get(randoms[2])+"!"+ereason3List.get(randoms[2]))
							+nullFilter("what#"+ewhatList.get(randoms[2]))+nullFilter("how#"+ehowList.get(randoms[2]))+"$"+eidList.get(randoms[2]);
					} else if (ekingList.get(randoms[2]) != tempName.get(1)) {
						a1c3 = nullFilter("name#"+epeopleList.get(randoms[2]))+nullFilter("place#"+eplaceList.get(randoms[2]))+nullFilter("reason#"+ereason1List.get(randoms[2])+"!"+ereason2List.get(randoms[2])+"!"+ereason3List.get(randoms[2]))
						+nullFilter("what#"+ewhatList.get(randoms[2]))+nullFilter("how#"+ehowList.get(randoms[2]))+"$"+eidList.get(randoms[2]);
					}
					
					
					stmt = null;
					rs = null;
					result = "";
					String selectA2BSQL = "select * from event where event"+keyA+" = '"+dataA2+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(selectA2BSQL);
					
					StringTokenizer stB2;
					String e2name = "";
					String e2people = "";
					String e2year = "";
					String e2place = "";
					String e2what = "";
					String e2how = "";
					String e2reason1 = "";
					String e2reason2 = "";
					String e2reason3 = "";
					String e2king = "";
					int e2id = 0;
					
					ArrayList<String> e2nameList = new ArrayList<>();
					ArrayList<String> e2peopleList = new ArrayList<>();
					ArrayList<String> e2yearList = new ArrayList<>();
					ArrayList<String> e2placeList = new ArrayList<>();
					ArrayList<String> e2whatList = new ArrayList<>();
					ArrayList<String> e2howList = new ArrayList<>();
					ArrayList<String> e2reason1List = new ArrayList<>();
					ArrayList<String> e2reason2List = new ArrayList<>();
					ArrayList<String> e2reason3List = new ArrayList<>();
					ArrayList<String> e2kingList = new ArrayList<>();
					ArrayList<Integer> e2idList = new ArrayList<>();
					
					while (rs.next()) { // 2 = people, 8 = king
						result = rs.getString(1)+"/"+rs.getString(2)+"/"+rs.getInt(3)
						+"/"+rs.getString(4)+"/"+rs.getString(6)+"/"+rs.getString(7)
						+"/"+rs.getString(5)+"/"+rs.getString(9)+"/"+rs.getString(10)+"/"+rs.getString(8)+"/"+rs.getInt("eventid");
						stB2 = new StringTokenizer (result,"/");
						e2name = stB2.nextToken();
						e2people = stB2.nextToken();
						e2year = stB2.nextToken();
						e2place = stB2.nextToken();
						e2what = stB2.nextToken();
						e2how = stB2.nextToken();
						e2reason1 = stB2.nextToken();
						e2reason2 = stB2.nextToken();
						e2reason3 = stB2.nextToken();
						e2king = stB2.nextToken();
						e2id = Integer.parseInt(stB2.nextToken());
						
						e2nameList.add(e2name);
						e2peopleList.add(e2people);
						e2yearList.add(e2year);
						e2placeList.add(e2place);
						e2whatList.add(e2what);
						e2howList.add(e2how);
						e2reason1List.add(e2reason1);
						e2reason2List.add(e2reason2);
						e2reason3List.add(e2reason3);
						e2kingList.add(e2king);
						e2idList.add(e2id);
					}
					
					int randoms2 = (int)(Math.random() * (e2nameList.size()-1));
					
					String tempName2 = "";
					StringTokenizer nameTK = new StringTokenizer(e2peopleList.get(randoms2), "#");
					tempName2 = nameTK.nextToken();
					
					if (e2kingList.get(randoms2).equals(tempName2) || keyA.equals("people")) {
						a2c1 = nullFilter("place#"+e2placeList.get(randoms2))+nullFilter("reason#"+e2reason1List.get(randoms2)+"!"+e2reason2List.get(randoms2)+"!"+e2reason3List.get(randoms2))
							+nullFilter("what#"+e2whatList.get(randoms2))+nullFilter("how#"+e2howList.get(randoms2))+"$"+e2idList.get(randoms2);
					} else if (e2kingList.get(randoms2) != tempName2) {
						a2c1 = nullFilter("name#"+e2peopleList.get(randoms2))+nullFilter("place#"+e2placeList.get(randoms2))+nullFilter("reason#"+e2reason1List.get(randoms2)+"!"+e2reason2List.get(randoms2)+"!"+e2reason3List.get(randoms2))
						+nullFilter("what#"+e2whatList.get(randoms2))+nullFilter("how#"+e2howList.get(randoms2))+"$"+e2idList.get(randoms2);
					}
				}
			}			
		} catch (SQLException e) {
		}

		returnCData = a1c1+"/"+a1c2+"/"+a1c3+"!"+a2c1;
	
		return returnCData;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// C유형
	static String selectA3 (String KeyA ,String KeyB, String keyC) {
		String orderResult = "";
		String order1 = "";
		String order2 = "";
		String order3 = "";
		String order4 = "";
		String order5 = "";
		String order6 = "";
		String outType="";
		
		double randomSil = Math.random();
		int intSil=(int)(randomSil*2)+1;
		
		if (intSil == 1)
			outType="title";
		
		else if(intSil == 2)
			outType="content";
		
		
		
		ArrayList<String> cnameList = new ArrayList<>();
		int crand1 = (int)(Math.random() * ((cnameList.size()-3) - 4 + 1))+4;
		int crand2 = 1+(int)(Math.random() * 1);
		
		
		ArrayList<String> enameList = new ArrayList<>();
		int erand1 = (int)(Math.random() * ((enameList.size()-3) -4  + 1))+4;
		int erand2 = 1+(int)(Math.random() * 1);
		
		ArrayList<String> oEnameList = new ArrayList<>();
		int oErand1 = (int)(Math.random() * ((oEnameList.size()-3) -4 + 1)) + 4;
		int oErand2 = 1+(int)(Math.random() * 1);
		
		try {
			
			
			stmt = null;
			rs = null;
			result = "";
			
			String orderSQL = "select * from "+KeyB+" where "+KeyB+"year not like '0' order by "+KeyB+"year asc";
			stmt = con.createStatement();
			rs = stmt.executeQuery(orderSQL);
			
			if(KeyA == "순서정렬"){
			if (KeyB.equals("culture")) {
				
				while (rs.next()) {
					result = rs.getString("culturename")+"$"+rs.getInt("cultureid");					
					cnameList.add(result);
				}
				
				order1 = cnameList.get(crand1);
				order2 = cnameList.get(crand1+crand2);
				order3 = cnameList.get((crand1+crand2)+1);				
			} 
			else if (KeyB.equals("event")) {
				if (outType.equals("title")) {
					
					while (rs.next()) {
						result = rs.getString("eventname")+"$"+rs.getInt("eventid");
						enameList.add(result);
					}
					
					order1 = enameList.get(erand1);
					order2 = enameList.get(erand1+erand2);
					order3 = enameList.get((erand1+erand2)+1);				
				} else if (outType.equals("content")) {
					StringTokenizer stO;
					
					String oEname = "";
					String oEpeople = "";
					String oEyear = "";
					String oEplace = "";
					String oEwhat = "";
					String oEhow = "";
					String oEreason1 = "";
					String oEreason2 = "";
					String oEreason3 = "";
					int oEid = 0;
					
					ArrayList<String> oEpeopleList = new ArrayList<>();
					ArrayList<String> oEyearList = new ArrayList<>();
					ArrayList<String> oEplaceList = new ArrayList<>();
					ArrayList<String> oEwhatList = new ArrayList<>();
					ArrayList<String> oEhowList = new ArrayList<>();
					ArrayList<String> oEreason1List = new ArrayList<>();
					ArrayList<String> oEreason2List = new ArrayList<>();
					ArrayList<String> oEreason3List = new ArrayList<>();
					ArrayList<Integer> oEidList = new ArrayList<>();
					
					while (rs.next()) {
						result = rs.getString(1)+"/"+rs.getString(2)+"/"+rs.getInt(3)
						+"/"+rs.getString(4)+"/"+rs.getString(6)+"/"+rs.getString(7)
						+"/"+rs.getString(5)+"/"+rs.getString(9)+"/"+rs.getString(10)+"/"+rs.getInt("eventid");

						stO = new StringTokenizer (result,"/");
						oEname = stO.nextToken();
						oEpeople = stO.nextToken();
						oEyear = stO.nextToken();
						oEplace = stO.nextToken();
						oEwhat = stO.nextToken();
						oEhow = stO.nextToken();
						oEreason1 = stO.nextToken();
						oEreason2 = stO.nextToken();
						oEreason3 = stO.nextToken();
						oEid = Integer.parseInt(stO.nextToken());
						
						oEnameList.add(oEname);
						oEpeopleList.add(oEpeople);
						oEyearList.add(oEyear);
						oEplaceList.add(oEplace);
						oEwhatList.add(oEwhat);
						oEhowList.add(oEhow);
						oEreason1List.add(oEreason1);
						oEreason2List.add(oEreason2);
						oEreason3List.add(oEreason3);
						oEidList.add(oEid);
					}
					
					
					
					order1 = nullFilter("name#"+oEpeopleList.get(oErand1))+nullFilter("place#"+oEplaceList.get(oErand1))+nullFilter("reason#"+oEreason1List.get(oErand1)+"!"+oEreason2List.get(oErand1)+"!"+oEreason3List.get(oErand1))
						+nullFilter("what#"+oEwhatList.get(oErand1))+nullFilter("how#"+oEhowList.get(oErand1))+"$"+oEidList.get(oErand1);
					order2 = nullFilter("name#"+oEpeopleList.get(oErand1+oErand2))+nullFilter("place#"+oEplaceList.get(oErand1+oErand2))+nullFilter("reason#"+oEreason1List.get(oErand1+oErand2)+"!"+oEreason2List.get(oErand1+oErand2)+"!"+oEreason3List.get(oErand1+oErand2))
						+nullFilter("what#"+oEwhatList.get(oErand1+oErand2))+nullFilter("how#"+oEhowList.get(oErand1+oErand2))+"$"+oEidList.get(oErand1+oErand2);	
					order3 = nullFilter("name#"+oEpeopleList.get((oErand1+oErand2)+1))+nullFilter("place#"+oEplaceList.get((oErand1+oErand2)+1))+nullFilter("reason#"+oEreason1List.get((oErand1+oErand2)+1)+"!"+oEreason2List.get((oErand1+oErand2)+1)+"!"+oEreason3List.get((oErand1+oErand2)+1))
						+nullFilter("what#"+oEwhatList.get((oErand1+oErand2)+1))+nullFilter("how#"+oEhowList.get((oErand1+oErand2)+1))+"$"+oEidList.get((oErand1+oErand2)+1);		
				}
			}
			}
		
		if( KeyA=="이후발"){	
			if (KeyB.equals("culture")) {
				
				
				while (rs.next()) {
					result = rs.getString("culturename")+"$"+rs.getInt("cultureid");					
					cnameList.add(result);
				}
				
				order4 = cnameList.get((crand1+crand2)+2);
				order5 = cnameList.get((crand1+crand2)+3);
							
			} 
			else if (KeyB.equals("event")) {
				if (outType.equals("title")) {
					
					while (rs.next()) {
						result = rs.getString("eventname")+"$"+rs.getInt("eventid");
						enameList.add(result);
					}
					
					order4 = enameList.get((erand1+erand2)+2);
					order5 = enameList.get((erand1+erand2)+3);
									
				} else if (outType.equals("content")) {
					StringTokenizer stO;
					
					String oEname = "";
					String oEpeople = "";
					String oEyear = "";
					String oEplace = "";
					String oEwhat = "";
					String oEhow = "";
					String oEreason1 = "";
					String oEreason2 = "";
					String oEreason3 = "";
					int oEid = 0;
					
					
					ArrayList<String> oEpeopleList = new ArrayList<>();
					ArrayList<String> oEyearList = new ArrayList<>();
					ArrayList<String> oEplaceList = new ArrayList<>();
					ArrayList<String> oEwhatList = new ArrayList<>();
					ArrayList<String> oEhowList = new ArrayList<>();
					ArrayList<String> oEreason1List = new ArrayList<>();
					ArrayList<String> oEreason2List = new ArrayList<>();
					ArrayList<String> oEreason3List = new ArrayList<>();
					ArrayList<Integer> oEidList = new ArrayList<>();
					
					while (rs.next()) {
						result = rs.getString(1)+"/"+rs.getString(2)+"/"+rs.getInt(3)
						+"/"+rs.getString(4)+"/"+rs.getString(6)+"/"+rs.getString(7)
						+"/"+rs.getString(5)+"/"+rs.getString(9)+"/"+rs.getString(10)+"/"+rs.getInt("eventid");

						stO = new StringTokenizer (result,"/");
						oEname = stO.nextToken();
						oEpeople = stO.nextToken();
						oEyear = stO.nextToken();
						oEplace = stO.nextToken();
						oEwhat = stO.nextToken();
						oEhow = stO.nextToken();
						oEreason1 = stO.nextToken();
						oEreason2 = stO.nextToken();
						oEreason3 = stO.nextToken();
						oEid = Integer.parseInt(stO.nextToken());
						
						oEnameList.add(oEname);
						oEpeopleList.add(oEpeople);
						oEyearList.add(oEyear);
						oEplaceList.add(oEplace);
						oEwhatList.add(oEwhat);
						oEhowList.add(oEhow);
						oEreason1List.add(oEreason1);
						oEreason2List.add(oEreason2);
						oEreason3List.add(oEreason3);
						oEidList.add(oEid);
					}
					
					
					
					order4 = nullFilter("name#"+oEpeopleList.get((oErand1+oErand2)+2))+nullFilter("place#"+oEplaceList.get((oErand1+oErand2)+2))+nullFilter("reason#"+oEreason1List.get((oErand1+oErand2)+2)+"!"+oEreason2List.get((oErand1+oErand2)+2)+"!"+oEreason3List.get((oErand1+oErand2)+2))
						+nullFilter("what#"+oEwhatList.get((oErand1+oErand2)+2))+nullFilter("how#"+oEhowList.get((oErand1+oErand2)+2))+"$"+oEidList.get((oErand1+oErand2)+2);
					order5 = nullFilter("name#"+oEpeopleList.get((oErand1+oErand2)+3))+nullFilter("place#"+oEplaceList.get((oErand1+oErand2)+3))+nullFilter("reason#"+oEreason1List.get((oErand1+oErand2)+3)+"!"+oEreason2List.get((oErand1+oErand2)+3)+"!"+oEreason3List.get((oErand1+oErand2)+3))
						+nullFilter("what#"+oEwhatList.get((oErand1+oErand2)+3))+nullFilter("how#"+oEhowList.get((oErand1+oErand2)+3))+"$"+oEidList.get((oErand1+oErand2)+3);	
							
				}
			}
		}	
		
		if(KeyA=="사이발생"){
			
			if (KeyB.equals("culture")) {
				
				while (rs.next()) {
					result = rs.getString("culturename")+"$"+rs.getInt("cultureid");					
					cnameList.add(result);
				}
				
				order6 = cnameList.get((crand1+crand2)+4);
							
			} 
			else if (KeyB.equals("event")) {
				if (outType.equals("title")) {
					
					while (rs.next()) {
						result = rs.getString("eventname")+"$"+rs.getInt("eventid");
						enameList.add(result);
					}
					
					order6 = enameList.get((erand1+erand2)+4);
									
				} else if (outType.equals("content")) {
					StringTokenizer stO;
					
					String oEname = "";
					String oEpeople = "";
					String oEyear = "";
					String oEplace = "";
					String oEwhat = "";
					String oEhow = "";
					String oEreason1 = "";
					String oEreason2 = "";
					String oEreason3 = "";
					int oEid = 0;
					
				
					ArrayList<String> oEpeopleList = new ArrayList<>();
					ArrayList<String> oEyearList = new ArrayList<>();
					ArrayList<String> oEplaceList = new ArrayList<>();
					ArrayList<String> oEwhatList = new ArrayList<>();
					ArrayList<String> oEhowList = new ArrayList<>();
					ArrayList<String> oEreason1List = new ArrayList<>();
					ArrayList<String> oEreason2List = new ArrayList<>();
					ArrayList<String> oEreason3List = new ArrayList<>();
					ArrayList<Integer> oEidList = new ArrayList<>();
					
					while (rs.next()) {
						result = rs.getString(1)+"/"+rs.getString(2)+"/"+rs.getInt(3)
						+"/"+rs.getString(4)+"/"+rs.getString(6)+"/"+rs.getString(7)
						+"/"+rs.getString(5)+"/"+rs.getString(9)+"/"+rs.getString(10)+"/"+rs.getInt("eventid");

						stO = new StringTokenizer (result,"/");
						oEname = stO.nextToken();
						oEpeople = stO.nextToken();
						oEyear = stO.nextToken();
						oEplace = stO.nextToken();
						oEwhat = stO.nextToken();
						oEhow = stO.nextToken();
						oEreason1 = stO.nextToken();
						oEreason2 = stO.nextToken();
						oEreason3 = stO.nextToken();
						oEid = Integer.parseInt(stO.nextToken());
						
						oEnameList.add(oEname);
						oEpeopleList.add(oEpeople);
						oEyearList.add(oEyear);
						oEplaceList.add(oEplace);
						oEwhatList.add(oEwhat);
						oEhowList.add(oEhow);
						oEreason1List.add(oEreason1);
						oEreason2List.add(oEreason2);
						oEreason3List.add(oEreason3);
						oEidList.add(oEid);
					}
					
					order6 = nullFilter("name#"+oEpeopleList.get((oErand1+oErand2)+4))+nullFilter("place#"+oEplaceList.get((oErand1+oErand2)+4))+nullFilter("reason#"+oEreason1List.get((oErand1+oErand2)+4)+"!"+oEreason2List.get((oErand1+oErand2)+4)+"!"+oEreason3List.get((oErand1+oErand2)+4))
						+nullFilter("what#"+oEwhatList.get((oErand1+oErand2)+4))+nullFilter("how#"+oEhowList.get((oErand1+oErand2)+4))+"$"+oEidList.get((oErand1+oErand2)+4);	
							
				}
			}
			}
			
			
		} catch (SQLException e) {			
		}
		
		
		orderResult = order1+"!"+order2+"!"+order3+"!"+order4+"!"+order5+"!"+order6;
		return orderResult;
		
			
		}
			
			
			

		
static String resultp (String resultContent) {
	String resultp = "";
	String mType1 = "";
	String mType2 = "";
	String qid1R = "", qid2R = "", qid3R = "", qid4R = "", qid5R = "", qid6R = "";
	int qid1 = 0, qid2 = 0, qid3 = 0, qid4 = 0, qid5 = 0, qid6 = 0;
	
	//ABC1E1A : mType1 = keyB, mType2 = keyC, qid1 = keyC, qid2~5 = keyB
    //qzcontent.add(mType1 + "/" + mType2 + "/" + qid1 + "/" + qid2 + "/" + qid3 + "/" + qid4 + "/" + qid5 + "/" + qid6);
	
	StringTokenizer retk = new StringTokenizer(resultContent, "$");
	mType1 = retk.nextToken();
	mType2 = retk.nextToken();
	qid1 = Integer.parseInt(retk.nextToken());
	qid2 = Integer.parseInt(retk.nextToken());
	qid3 = Integer.parseInt(retk.nextToken());
	qid4 = Integer.parseInt(retk.nextToken());
	qid5 = Integer.parseInt(retk.nextToken());
	qid6 = Integer.parseInt(retk.nextToken());
	
	try {
		stmt = null;
		rs = null;
		result = "";
		
		String resultSQL = "";
		
		//ABC1E1A : mType1 = keyB, mType2 = keyC, qid1 = keyC, qid2~5 = keyB
		if (mType2.equals("null")) { // ABC를 제외한 나머지 유형
			if (qid1 != 0) {
				resultSQL = "select * from "+mType1+" where "+mType1+"id = "+qid1+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);				
				while (rs.next()) {
					if (mType1.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid1R = result;
					} else if (mType1.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid1R = result;
					}
				}
				stmt = null;
				rs = null;
			} else {
				qid1R = "null#null";
			}

			if (qid2 != 0) {
				resultSQL = "select * from "+mType1+" where "+mType1+"id = "+qid2+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);
				while (rs.next()) {
					if (mType1.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid2R = result;
					} else if (mType1.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid2R = result;
					}
				}
				stmt = null;
				rs = null;
			} else {
				qid2R = "null#null";
			}

			if (qid3 != 0) {
				resultSQL = "select * from "+mType1+" where "+mType1+"id = "+qid3+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);
				while (rs.next()) {
					if (mType1.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid3R = result;
					} else if (mType1.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid3R = result;
					}
				}
				stmt = null;
				rs = null;
			} else {
				qid3R = "null#null";
			}

			if (qid4 != 0) {
				resultSQL = "select * from "+mType1+" where "+mType1+"id = "+qid4+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);
				while (rs.next()) {
					if (mType1.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid4R = result;
					} else if (mType1.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid4R = result;
					}
				}
				stmt = null;
				rs = null;
			} else {
				qid4R = "null#null";
			}

			
			if (qid5 != 0) {
				resultSQL = "select * from "+mType1+" where "+mType1+"id = "+qid5+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);
				while (rs.next()) {
					if (mType1.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid5R = result;
					} else if (mType1.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid5R = result;
					}
				}
				stmt = null;
				rs = null;
			} else {
				qid5R = "null#null";
			}

			if (qid6 != 0) {
				resultSQL = "select * from "+mType1+" where "+mType1+"id = "+qid6+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);
				while (rs.next()) {
					if (mType1.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid6R = result;
					} else if (mType1.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid6R = result;
					}
				}
				stmt = null;
				rs = null;	
			} else {
				qid6R = "null#null";
			}

			
			resultp = qid1R+"/"+qid2R+"/"+qid3R+"/"+qid4R+"/"+qid5R+"/"+qid6R;
			
		} else { //ABC1E1A : mType1 = keyB, mType2 = keyC, qid1 = keyC, qid2~5 = keyB
			
			if (qid1 != 0) {
				resultSQL = "select * from "+mType2+" where "+mType2+"id = "+qid1+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);				
				while (rs.next()) {
					if (mType2.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid1R = result;
					} else if (mType2.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid1R = result;
					}
				}
				stmt = null;
				rs = null;
			} else {
				qid1R = "null#null";
			}

			if (qid2 != 0) {
				resultSQL = "select * from "+mType1+" where "+mType1+"id = "+qid2+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);
				while (rs.next()) {
					if (mType1.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid2R = result;
					} else if (mType1.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid2R = result;
					}
				}
				stmt = null;
				rs = null;
			} else {
				qid2R = "null#null";
			}

			
			if (qid3 != 0) {
				resultSQL = "select * from "+mType1+" where "+mType1+"id = "+qid3+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);
				while (rs.next()) {
					if (mType1.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid3R = result;
					} else if (mType1.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid3R = result;
					}
				}
				stmt = null;
				rs = null;
			} else {
				qid3R = "null#null";
			}

			
			if (qid4 != 0) {
				resultSQL = "select * from "+mType1+" where "+mType1+"id = "+qid4+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);
				while (rs.next()) {
					if (mType1.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid4R = result;
					} else if (mType1.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid4R = result;
					}
				}
				stmt = null;
				rs = null;
			} else {
				qid4R = "null#null";
			}

			if (qid5 != 0) {
				resultSQL = "select * from "+mType1+" where "+mType1+"id = "+qid5+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);
				while (rs.next()) {
					if (mType1.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid5R = result;
					} else if (mType1.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid5R = result;
					}
				}
				stmt = null;
				rs = null;					
			} else {
				qid5R = "null#null";
			}

			if (qid6 != 0) {
				resultSQL = "select * from "+mType1+" where "+mType1+"id = "+qid6+"";
				stmt = con.createStatement();
				rs = stmt.executeQuery(resultSQL);
				while (rs.next()) {
					if (mType1.equals("culture")) {
						result = "culture#"+rs.getString("culturename")+"!"+rs.getString("cetc")+"!"+rs.getString("cultureking")+", "+rs.getString("cultureyear")+"!"+rs.getInt("cultureid");
						qid6R = result;
					} else if (mType1.equals("event")) {
						result = "event#"+rs.getString(1)+": "+nullFilter("name#"+rs.getString(2))+""+nullFilter("year#"+rs.getInt(3))+""+nullFilter("place#"+rs.getString(4))
						+""+nullFilter("reason#"+rs.getString(5)+"!"+rs.getString(9)+"!"+rs.getString(10))+""+nullFilter("what#"+rs.getString(6))+""+nullFilter("how#"+rs.getString(7));
						qid6R = result;
					}
				}
				stmt = null;
				rs = null;					
			} else {
				qid6R = "null#null";
			}				
			resultp = qid1R+"/"+qid2R+"/"+qid3R+"/"+qid4R+"/"+qid5R+"/"+qid6R;
		}
		
		
		
	} catch (SQLException e) {			
	}		
	
	return resultp;
}

static String nullFilter (String text) {
	String sID = "";
	String inText = "";
	String outText = "";
	String reason1 = "";
	String reason2 = "";
	String reason3 = "";
	String state = "";
	
	StringTokenizer tkNull = new StringTokenizer(text, "#");
	sID = tkNull.nextToken();
	inText = tkNull.nextToken();
	if (sID.equals("name") || sID.equals("what"))
		state = tkNull.nextToken();
	
	if (sID.equals("reason")) {
		StringTokenizer tkReason = new StringTokenizer(inText, "!");
		reason1 = tkReason.nextToken();
		reason2 = tkReason.nextToken();
		reason3 = tkReason.nextToken();
		if (reason1.equals("null")) {
			outText = "";
		} else if (reason1 != "null" && reason2.equals("null")) {
			outText = reason1+" ";
		} else if (reason2 != "null" && reason3.equals("null")) {
			outText = reason1+" "+reason2+" ";
		} else if (reason3 != "null") {
			outText = reason1+" "+reason2+" "+reason3+" ";
		}
	}		
	if (sID.equals("name")) {
		if (inText.equals("null")) {
			outText = "";
		} else {
			if (state.equals("o")) {
				outText = inText+"은 ";
			} else if (state.equals("x")) {
				outText = inText+"는 ";
			}
		}
	}		
	if (sID.equals("year")) {
		if (inText.equals("0")) {
			outText = "";
		} else {
			outText = inText+"년 ";
		}
	}		
	if (sID.equals("place")) {
		if (inText.equals("null")) {
			outText = "";
		} else {
			outText = inText+"에서 ";
		}
	}		
	if (sID.equals("what")) {
		if (inText.equals("null")) {
			outText = "";
		} else {
			if (state.equals("o")) {
				outText = inText+"을 ";
			} else if (state.equals("x")) {
				outText = inText+"를 ";
			}
		}
	}		
	if (sID.equals("how")) {
		if (inText.equals("null")) {
			outText = "";
		} else {
			outText = inText;
		}
	}	
	return outText;
}

}

		

	