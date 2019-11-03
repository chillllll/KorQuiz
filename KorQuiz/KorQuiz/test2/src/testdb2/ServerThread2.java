package testdb2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;

import javax.xml.bind.ParseConversionEvent;

public class ServerThread2 extends Thread {
	static Connection con = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	static String result = "";

	private static List<ServerThread2> threads = new ArrayList<ServerThread2>();
	private Socket socket;
	OutputStream os;
	
	static ObjectOutputStream oos;
	String url = "jdbc:mysql://127.0.0.1:3306/test2";
	String id = "root";
	String pw = "123d";

	public ServerThread2(Socket socket) {
		super();
		this.socket = socket;
		threads.add(this);

	}
	//PrintWriter out = null;
	PrintWriter out=null;
	PrintWriter out2 = null;
	PrintWriter out3 = null;
	PrintWriter out4 = null;
	BufferedReader in = null;
	
	public void run() {

		
		String inType = "";
		String inKeyA = "";
		String inKeyB = "";
		String inKeyC = "";
		String inlevel = "";
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
					System.out.println(inputLine);

					StringTokenizer intk = new StringTokenizer(inputLine, "/");
					inType = intk.nextToken();
					if (inType.equals("sol")) {
						inSol = intk.nextToken();
						System.out.println("inSol");
						System.out.println(inSol);
					} else {
						inKeyA = intk.nextToken();
						inKeyB = intk.nextToken();
						inKeyC = intk.nextToken();
						inlevel = intk.nextToken();
					}

					if (inType.equals("Atype")) {
						if (inKeyB.equals("culture")) {
							String adata = selectA1(inKeyA, inKeyB, inKeyC, inlevel);
							String bdata = selectB1(adata, inKeyA, inKeyB, inlevel);
							String cdata = selectC1(adata, inKeyA, inKeyC, inlevel);
							byte[] cimg = selectImg(bdata, inKeyB, inlevel);
							String servermsg = cdata + "!" + bdata;
							
							out.println(servermsg);
							
							
							os = socket.getOutputStream();
							oos = new ObjectOutputStream(os);
							oos.writeObject(cimg);
							

							System.out.println(servermsg);
							System.out.println(cimg);
							System.out.println(socket.getSendBufferSize());
							
							/*
							System.out.println("oos플러시");
							
							System.out.println("os플러시");
							out.flush();
							System.out.println("out플러시");
							 */
			                
			                System.out.println("이미지소켓닫음");
	
							
						} else {
							String adata = selectA1(inKeyA, inKeyB, inKeyC, inlevel);
							String bdata = selectB1(adata, inKeyA, inKeyB, inlevel);
							String cdata = selectC1(adata, inKeyA, inKeyC, inlevel);
							String servermsg = cdata + "!" + bdata;
							out.println(servermsg);
							System.out.println(servermsg);
						}

					} else if (inType.equals("Btype")) {
						String adata = selectA2(inKeyA, inKeyB, inKeyC, inlevel);
						String bdata = selectB2(adata, inKeyA, inKeyB, inlevel);
						String cdata = selectC2(adata, inKeyA, inKeyC, inlevel);
						String servermsg = cdata + "!" + bdata;
						out.println(servermsg);
						System.out.println(servermsg);

					} else if (inType.equals("Ctype")) {

						String adata = selectA3(inKeyA, inKeyB, inlevel);
						out.println(adata);
						System.out.println(adata);

					} else if (inType.equals("sol")) {
						String sols = resultp(inSol, inlevel);
						System.out.println(sols);
						out.println(sols);

					}

				} catch (Exception e) {
					
					if(inType.equals("Atype")&&inKeyB.equals("culture"))
					{
						oos.flush();
						oos.close();
						os.flush();
						os.close();
						System.out.println("스트림닫음");
					}
					socket.close();
					System.out.println("소켓닫음");
					threads.remove(this);
					return;
				}
			}

		} catch (Exception e) {
			System.err.println(e);
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	//// A유형

	static String selectA1(String keyA, String keyB, String keyC, String level) {
		String returnA1Data = "";
		String dataA1Name = "";
		int dataA1Start = 0;
		int dataA1End = 0;
		String dataA2Name = "";

		if (level.equals("1")) {
			if (keyB.equals("culture")) {
				keyB = "culture1";
			} else if (keyB.equals("event")) {
				keyB = "event1";
			}
			if (keyC.equals("culture")) {
				keyC = "culture1";
			} else if (keyC.equals("event")) {
				keyC = "event1";
			}
		} else if (level.equals("2")) {
			if (keyB.equals("culture")) {
				keyB = "culture2";
			} else if (keyB.equals("event")) {
				keyB = "event2";
			}
			if (keyC.equals("culture")) {
				keyC = "culture2";
			} else if (keyC.equals("event")) {
				keyC = "event2";
			}
		} else if (level.equals("3")) {
			if (keyB.equals("culture")) {
				keyB = "culture3";
			} else if (keyB.equals("event")) {
				keyB = "event3";
			}
			if (keyC.equals("culture")) {
				keyC = "culture3";
			} else if (keyC.equals("event")) {
				keyC = "event3";
			}
		}

		ArrayList<String> dataA2List = new ArrayList<>();

		int randomA1 = 0;
		int randomA2 = 0;

		int maxA = 0;
		int cBreakFlag = 0;

		try {
			stmt = null;
			rs = null;
			result = "";
			String countASQL = "select count(*) from " + keyA + "";

			stmt = con.createStatement();
			rs = stmt.executeQuery(countASQL);

			while (rs.next()) {
				result = rs.getString(1);
			}
			maxA = Integer.parseInt(result);

			for (;;) { // A1 선택
				randomA1 = 1 + (int) (Math.random() * maxA);
				stmt = null;
				rs = null;
				result = "";

				String selectA1SQL = "select * from king where kingid=" + randomA1 + "";
				stmt = con.createStatement();
				rs = stmt.executeQuery(selectA1SQL);

				while (rs.next()) {
					result = rs.getString("kingname") + "/" + rs.getString("kingstart") + "/" + rs.getString("kingend");
					StringTokenizer stkA1 = new StringTokenizer(result, "/");
					dataA1Name = stkA1.nextToken();
					dataA1Start = Integer.parseInt(stkA1.nextToken());
					dataA1End = Integer.parseInt(stkA1.nextToken());
				}

				// 선택된 A1이 3개 이상의 C데이터와 1개 이상의 B데이터를 가지고 있는지 확인
				stmt = null;
				rs = null;
				result = "";

				System.out.println("-----------키B와 C--------------");
				System.out.println(keyB);
				System.out.println(keyC);

				String countA1BSQL = "select count(*) from " + keyB + " where " + keyB + "" + keyA + "='" + dataA1Name
						+ "'";

				stmt = con.createStatement();

				rs = stmt.executeQuery(countA1BSQL); ///////////////////////////////////////// 문제줄
				System.out.println("a1중간");
				while (rs.next()) {
					result = rs.getString(1);
				}

				int countA1B = Integer.parseInt(result);
				if (countA1B > 0) { // A1의 B 데이터가 1개 이상일때, C 데이터를 3개 이상 가지는지 확인
					stmt = null;
					rs = null;
					result = "";

					String countA1CSQL = "select count(*) from " + keyC + " where " + keyC + "" + keyA + "='"
							+ dataA1Name + "'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(countA1CSQL);
					System.out.println("a1중간2");
					while (rs.next()) {
						result = rs.getString(1);
					}

					int countA1C = Integer.parseInt(result);
					if (countA1C >= 4) {
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

			String selectA2SQL = "select * from king where ((kingstart>=(" + dataA1Start + "-100)) && (kingend<=("
					+ dataA1End + "+100))) && " + keyA + "name not like '" + dataA1Name + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectA2SQL);
			while (rs.next()) {
				result = rs.getString("kingname");
				dataA2List.add(result);
			}

			int A2count = 0;
			String A2c = "";
			for (;;) {
				randomA2 = (int) (Math.random() * (dataA2List.size() - 1));
				dataA2Name = dataA2List.get(randomA2);
				A2count = A2count + 1;
				System.out.println("A2count");
				System.out.println(A2count);

				stmt = null;
				rs = null;
				result = "";

				System.out.println("a2에서의 키 b와 c");
				System.out.println(keyB);
				System.out.println(keyC);

				String countA2SQL = "select count(*) from " + keyC + " where " + keyC + "king = '" + dataA2Name + "'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(countA2SQL);
				System.out.println("aa1");
				while (rs.next()) {
					result = rs.getString(1);
					System.out.println("aa2");
				}
				System.out.println("aa3");
				int countA2C = Integer.parseInt(result);
				System.out.println("counta2c");
				System.out.println(countA2C);

				if (countA2C > 0)
					break;
				if (A2count >= 20) {
					returnA1Data = "fail";
					System.out.println("fail");
					break;
				}
			} // A2 시기 선택 종료
			System.out.println("fail2");

		} catch (SQLException e) {
		}

		System.out.println("a데이터");

		returnA1Data = dataA1Name + "/" + dataA2Name;

		return returnA1Data;

	} // A1 A2 반복문 종료

	/////////////////////////////////////////////////////////
	// 보기 데이터 선택

	static String selectB1(String dataA, String keyA, String keyB, String level) {
		String returnB1Data = "";
		String dataA1 = "";
		String bCname = "";
		String bEname = "";
		String outType = "";

		double randomSil = Math.random();
		int intSil = (int) (randomSil * 2) + 1;

		if (intSil == 1)
			outType = "title";

		else if (intSil == 2)
			outType = "content";

		stmt = null;
		rs = null;
		result = "";

		StringTokenizer stDataBA = new StringTokenizer(dataA, "/");
		dataA1 = stDataBA.nextToken();

		System.out.println("dataA:");
		System.out.println(dataA);
		System.out.println("dataA1:");
		System.out.println(dataA1);

		try {
			stmt = null;
			rs = null;
			result = "";

			if (level.equals("1")) {
				if (keyB.equals("culture")) {
					keyB = "culture1";
				} else if (keyB.equals("event")) {
					keyB = "event1";
				}
			} else if (level.equals("2")) {
				if (keyB.equals("culture")) {
					keyB = "culture2";
				} else if (keyB.equals("event")) {
					keyB = "event2";
				}
			} else if (level.equals("3")) {
				if (keyB.equals("culture")) {
					keyB = "culture3";
				} else if (keyB.equals("event")) {
					keyB = "event3";
				}
			}

			System.out.println("-----------키B--------------");
			System.out.println(keyB);

			String selectABSQL = "select * from " + keyB + " where " + keyB + "" + keyA + " = '" + dataA1 + "'";

			stmt = con.createStatement();

			rs = stmt.executeQuery(selectABSQL); ///////// 문제되는 줄

			System.out.println("됨");

			if (keyB.equals("culture1") || keyB.equals("culture2") || keyB.equals("culture3")) {
				ArrayList<String> bCnameList = new ArrayList<>();
				while (rs.next()) {
					result = rs.getString("culturename") + "$" + rs.getInt("cultureid") + "$"
							+ rs.getString("culturelv");
					bCnameList.add(result);
					System.out.println("keyB가 culture일때:");
					System.out.println(result);
				}
				int bCrand = (int) (Math.random() * (bCnameList.size() - 1));
				bCname = bCnameList.get(bCrand);
				returnB1Data = bCname;
				System.out.println("bc1");
			} else if (keyB.equals("event1") || keyB.equals("event2") || keyB.equals("event3")) {
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
				String bElv = "";

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
				ArrayList<String> bElvList = new ArrayList<>();

				while (rs.next()) {
					result = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getInt(3) + "/" + rs.getString(4) + "/"
							+ rs.getString(6) + "/" + rs.getString(7) + "/" + rs.getString(5) + "/" + rs.getString(9)
							+ "/" + rs.getString(10) + "/" + rs.getString(8) + "/" + rs.getInt("eventid") + "/"
							+ rs.getString(12);
					;
					System.out.println("keyB가 event일때");
					System.out.println(result);

					stB = new StringTokenizer(result, "/");
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
					bElv = stB.nextToken();

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
					bElvList.add(bElv);
				}
				int bErand = (int) (Math.random() * (bEnameList.size() - 1));
				System.out.println("outtype");
				System.out.println(outType);
				if (outType.equals("title")) {
					bEname = bEnameList.get(bErand) + "$" + bEidList.get(bErand) + "$" + bElvList.get(bErand);

					System.out.println("b2");
				} else if (outType.equals("content")) {
					String tempName5 = "";
					StringTokenizer nameTK = new StringTokenizer(bEpeopleList.get(bErand), "#");
					tempName5 = nameTK.nextToken();

					if (bEkingList.get(bErand).equals(bEpeopleList.get(bErand))) {
						bEname = nullFilter("year#" + bEyearList.get(bErand))
								+ nullFilter("place#" + bEplaceList.get(bErand))
								+ nullFilter("reason1#" + bEreason1List.get(bErand) + "!" + bEreason2List.get(bErand)
										+ "!" + bEreason3List.get(bErand))
								+ nullFilter("what#" + bEwhatList.get(bErand))
								+ nullFilter("how#" + bEhowList.get(bErand)) + "$" + bEidList.get(bErand);
						System.out.println("b4");
					} else {
						bEname = nullFilter("name#" + bEpeopleList.get(bErand))
								+ nullFilter("year#" + bEyearList.get(bErand))
								+ nullFilter("place#" + bEplaceList.get(bErand))
								+ nullFilter("reason1#" + bEreason1List.get(bErand) + "!" + bEreason2List.get(bErand)
										+ "!" + bEreason3List.get(bErand))
								+ nullFilter("what#" + bEwhatList.get(bErand))
								+ nullFilter("how#" + bEhowList.get(bErand)) + "$" + bEidList.get(bErand);
						System.out.println("b5");
					}
				}

				returnB1Data = bEname;
			}
		} catch (SQLException e) {
		}
		System.out.println("선택지");
		System.out.println(returnB1Data);
		return returnB1Data;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 선택지 데이터 선택
	static String selectC1(String dataA, String keyA, String keyC, String level) {
		String returnC1Data = "";
		String dataA1 = "";
		String dataA2 = "";
		String a1c1 = "";
		String a1c2 = "";
		String a1c3 = "";
		String a2c1 = "";
		String a1c1lv = "";
		String a1c2lv = "";
		String a1c3lv = "";
		String a2c1lv = "";
		double aver = 0;
		String aver2;
		double a1c1i;
		double a1c2i;
		double a1c3i;
		double a2c1i;
		StringTokenizer stDataA = new StringTokenizer(dataA, "/");

		dataA1 = stDataA.nextToken();
		dataA2 = stDataA.nextToken();
		String outType = "";
		double randomSil = Math.random();
		int intSil = (int) (randomSil * 2) + 1;

		if (intSil == 1)
			outType = "title";

		else if (intSil == 2)
			outType = "content";

		try {
			// dataA1,2를 가지는 keyB 데이터 요청
			stmt = null;
			rs = null;
			result = "";
			String lv = "";

			if (level.equals("1")) {

				if (keyC.equals("culture")) {
					keyC = "culture1";
				} else if (keyC.equals("event")) {
					keyC = "event1";
				}
			} else if (level.equals("2")) {

				if (keyC.equals("culture")) {
					keyC = "culture2";
				} else if (keyC.equals("event")) {
					keyC = "event2";
				}
			} else if (level.equals("3")) {
				if (keyC.equals("culture")) {
					keyC = "culture3";
				} else if (keyC.equals("event")) {
					keyC = "event3";
				}
			}

			System.out.println("-----------키C--------------");
			System.out.println(keyC);

			String selectA1CSQL = "select * from " + keyC + " where " + keyC + "" + keyA + " = '" + dataA1 + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectA1CSQL);
			System.out.println("c1");
			////////////////////////////////////////////////////////////
			if (keyC.equals("culture1") || keyC.equals("culture2") || keyC.equals("culture3")) {

				ArrayList<String> a1cnameList = new ArrayList<>();
				ArrayList<String> a2cnameList = new ArrayList<>();
				ArrayList<String> a1lvList = new ArrayList<>();
				ArrayList<String> a2lvList = new ArrayList<>();
				System.out.println("cc1");
				while (rs.next()) {
					result = rs.getString("culturename") + "$" + rs.getInt("cultureid") + "$"
							+ rs.getString("culturelv");
					System.out.println("cc2");
					a1cnameList.add(result);
					lv = rs.getString("culturelv");
					a1lvList.add(lv);
					System.out.println("keyC가 culture일때");
					System.out.println(result);
					System.out.println("레벨은");
					System.out.println(lv);
					System.out.println("cc3");
				}
				int[] randoms = new int[10];
				int z = 2;

				for (;;) {
					randoms[z] = (int) (Math.random() * a1cnameList.size());
					if (randoms[z] != randoms[z + 1] && randoms[z] != randoms[z + 2]) {
						z = z - 1;
					}
					if (z < 0)
						break;
				}
				a1c1 = a1cnameList.get(randoms[0]);
				a1c2 = a1cnameList.get(randoms[1]);
				a1c3 = a1cnameList.get(randoms[2]);
				a1c1lv = a1lvList.get(randoms[0]);
				a1c2lv = a1lvList.get(randoms[1]);
				a1c3lv = a1lvList.get(randoms[2]);

				System.out.println("cc4");

				stmt = null;
				rs = null;
				result = "";
				String lv2 = "";
				String selectA2CSQL = "select * from " + keyC + " where " + keyC + "" + keyA + " = '" + dataA2 + "'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(selectA2CSQL);
				System.out.println("cc5");
				while (rs.next()) {
					System.out.println("cc6");
					result = rs.getString("culturename") + "$" + rs.getInt("cultureid") + "$"
							+ rs.getString("culturelv");
					a2cnameList.add(result);
					lv2 = rs.getString("culturelv");
					a2lvList.add(lv2);
					System.out.println("cc7");
				}
				int a2crandom = (int) (Math.random() * (a2cnameList.size() - 1));
				a2c1 = a2cnameList.get(a2crandom);
				a2c1lv = a2lvList.get(a2crandom);

				a1c1i = Integer.parseInt(a1c1lv);
				a1c2i = Integer.parseInt(a1c2lv);
				a1c3i = Integer.parseInt(a1c3lv);
				a2c1i = Integer.parseInt(a2c1lv);
				aver = (a1c1i + a1c2i + a1c3i + a2c1i) / 4;
				System.out.println("aver");
				System.out.println(aver);

				System.out.println("a1c1");
				System.out.println(a1c1);
				System.out.println(a1c1lv);
				System.out.println("a1c2");
				System.out.println(a1c2);
				System.out.println(a1c2lv);
				System.out.println("a1c3");
				System.out.println(a1c3);
				System.out.println(a1c3lv);
				System.out.println("a2c1");
				System.out.println(a2c1);
				System.out.println(a2c1lv);
				System.out.println("aver");
				System.out.println(aver);

				///////////////////////////////////////////////////////
			}

			if (keyC.equals("event1") || keyC.equals("event2") || keyC.equals("event3")) {
				System.out.println("outType");
				System.out.println(outType);
				if (outType.equals("title")) {
					ArrayList<String> a1enameList = new ArrayList<>();
					ArrayList<String> a2enameList = new ArrayList<>();
					ArrayList<String> a1lvList = new ArrayList<>();
					ArrayList<String> a2lvList = new ArrayList<>();

					while (rs.next()) {
						result = rs.getString("eventname") + "$" + rs.getInt("eventid");
						a1enameList.add(result);
						lv = rs.getString("eventlv");
						a1lvList.add(lv);
						System.out.println("keyC가 event일때");
						System.out.println(result);
						System.out.println("레벨은");
						System.out.println(lv);
						System.out.println("c2");
					}
					int[] randoms = new int[8];
					int z = 2;
					for (;;) {
						randoms[z] = (int) (Math.random() * a1enameList.size());
						if (randoms[z] != randoms[z + 1] && randoms[z] != randoms[z + 2]) {
							z = z - 1;
						}
						if (z < 0)
							break;
					}
					a1c1 = a1enameList.get(randoms[0]);
					a1c2 = a1enameList.get(randoms[1]);
					a1c3 = a1enameList.get(randoms[2]);
					a1c1lv = a1lvList.get(randoms[0]);
					a1c2lv = a1lvList.get(randoms[1]);
					a1c3lv = a1lvList.get(randoms[2]);
					System.out.println("c3");

					stmt = null;
					rs = null;
					String lv2;
					result = "";
					String selectA2CSQL = "select * from " + keyC + " where " + keyC + "" + keyA + " = '" + dataA2
							+ "'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(selectA2CSQL);
					System.out.println("c4");

					while (rs.next()) {
						result = rs.getString("eventname") + "$" + rs.getInt("eventid") + "$" + rs.getString("eventlv");
						a2enameList.add(result);
						lv2 = rs.getString("eventlv");
						a2lvList.add(lv2);
						System.out.println("c5");
					}
					int a2rand = (int) (Math.random() * (a2enameList.size() - 1));
					a2c1 = a2enameList.get(a2rand);
					a2c1lv = a2lvList.get(a2rand);

					a1c1i = Integer.parseInt(a1c1lv);
					a1c2i = Integer.parseInt(a1c2lv);
					a1c3i = Integer.parseInt(a1c3lv);
					a2c1i = Integer.parseInt(a2c1lv);
					aver = (a1c1i + a1c2i + a1c3i + a2c1i) / 4;
					System.out.println("aver");
					System.out.println(aver);

					System.out.println(a1c1);
					System.out.println(a1c1lv);
					System.out.println("a1c2");
					System.out.println(a1c2);
					System.out.println(a1c2lv);
					System.out.println("a1c3");
					System.out.println(a1c3);
					System.out.println(a1c3lv);
					System.out.println("a2c1");
					System.out.println(a2c1);
					System.out.println(a2c1lv);
					System.out.println("aver");
					System.out.println(aver);

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
					String elv = "";
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
					ArrayList<String> elvList = new ArrayList<>();

					System.out.println("c6");
					while (rs.next()) {
						result = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getInt(3) + "/" + rs.getString(4)
								+ "/" + rs.getString(6) + "/" + rs.getString(7) + "/" + rs.getString(5) + "/"
								+ rs.getString(9) + "/" + rs.getString(10) + "/" + rs.getString(8) + "/"
								+ rs.getInt("eventid") + "/" + rs.getString(12);
						stC1 = new StringTokenizer(result, "/");
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
						elv = stC1.nextToken();

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
						elvList.add(elv);

					}

					int[] randoms = new int[8];
					int z = 3;
					for (;;) {
						randoms[z] = (int) (Math.random() * enameList.size());
						if (randoms[z] != randoms[z + 1] && randoms[z] != randoms[z + 2]) {
							z = z - 1;
						}
						if (z < 0)
							break;
					}

					ArrayList<String> tempName = new ArrayList<>();
					for (int t = 0; t < 4; t++) {
						String temps = "";
						StringTokenizer nameTK = new StringTokenizer(epeopleList.get(randoms[t]), "#");
						temps = nameTK.nextToken();
						tempName.add(temps);
					}
					System.out.println("c8");
					if (ekingList.get(randoms[0]).equals(tempName.get(0))) {
						a1c1 = nullFilter("place#" + eplaceList.get(randoms[0]))
								+ nullFilter("reason#" + ereason1List.get(randoms[0]) + "!"
										+ ereason2List.get(randoms[0]) + "!" + ereason3List.get(randoms[0]))
								+ nullFilter("what#" + ewhatList.get(randoms[0]))
								+ nullFilter("how#" + ehowList.get(randoms[0])) + "$" + eidList.get(randoms[0]) + "$"
								+ elvList.get(randoms[0]);
					} else if (ekingList.get(randoms[0]) != tempName.get(0)) {
						a1c1 = nullFilter("name#" + epeopleList.get(randoms[0]))
								+ nullFilter("place#" + eplaceList.get(randoms[0]))
								+ nullFilter("reason#" + ereason1List.get(randoms[0]) + "!"
										+ ereason2List.get(randoms[0]) + "!" + ereason3List.get(randoms[0]))
								+ nullFilter("what#" + ewhatList.get(randoms[0]))
								+ nullFilter("how#" + ehowList.get(randoms[0])) + "$" + eidList.get(randoms[0]) + "$"
								+ elvList.get(randoms[0]);
					}
					if (ekingList.get(randoms[1]).equals(tempName.get(1))) {
						a1c2 = nullFilter("place#" + eplaceList.get(randoms[1]))
								+ nullFilter("reason#" + ereason1List.get(randoms[1]) + "!"
										+ ereason2List.get(randoms[1]) + "!" + ereason3List.get(randoms[1]))
								+ nullFilter("what#" + ewhatList.get(randoms[1]))
								+ nullFilter("how#" + ehowList.get(randoms[1])) + "$" + eidList.get(randoms[1]) + "$"
								+ elvList.get(randoms[1]);
					} else if (ekingList.get(randoms[1]) != tempName.get(1)) {
						a1c2 = nullFilter("name#" + epeopleList.get(randoms[1]))
								+ nullFilter("place#" + eplaceList.get(randoms[1]))
								+ nullFilter("reason#" + ereason1List.get(randoms[1]) + "!"
										+ ereason2List.get(randoms[1]) + "!" + ereason3List.get(randoms[1]))
								+ nullFilter("what#" + ewhatList.get(randoms[1]))
								+ nullFilter("how#" + ehowList.get(randoms[1])) + "$" + eidList.get(randoms[1]) + "$"
								+ elvList.get(randoms[1]);
					}
					if (ekingList.get(randoms[2]).equals(tempName.get(2))) {
						a1c3 = nullFilter("place#" + eplaceList.get(randoms[2]))
								+ nullFilter("reason#" + ereason1List.get(randoms[2]) + "!"
										+ ereason2List.get(randoms[2]) + "!" + ereason3List.get(randoms[2]))
								+ nullFilter("what#" + ewhatList.get(randoms[2]))
								+ nullFilter("how#" + ehowList.get(randoms[2])) + "$" + eidList.get(randoms[2]) + "$"
								+ elvList.get(randoms[2]);
					} else if (ekingList.get(randoms[2]) != tempName.get(2)) {
						a1c3 = nullFilter("name#" + epeopleList.get(randoms[2]))
								+ nullFilter("place#" + eplaceList.get(randoms[2]))
								+ nullFilter("reason#" + ereason1List.get(randoms[2]) + "!"
										+ ereason2List.get(randoms[2]) + "!" + ereason3List.get(randoms[2]))
								+ nullFilter("what#" + ewhatList.get(randoms[2]))
								+ nullFilter("how#" + ehowList.get(randoms[2])) + "$" + eidList.get(randoms[2]) + "$"
								+ elvList.get(randoms[2]);
					}
					System.out.println("c9");
					a1c1lv = elvList.get(randoms[0]);
					a1c2lv = elvList.get(randoms[1]);
					a1c3lv = elvList.get(randoms[2]);

					stmt = null;
					rs = null;
					result = "";
					String selectA2BSQL = "select * from " + keyC + " where " + keyC + "" + keyA + " = '" + dataA2
							+ "'";
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
					String e2lv = "";

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
					ArrayList<String> e2lvList = new ArrayList<>();

					while (rs.next()) { // 2 = people, 8 = king
						result = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getInt(3) + "/" + rs.getString(4)
								+ "/" + rs.getString(6) + "/" + rs.getString(7) + "/" + rs.getString(5) + "/"
								+ rs.getString(9) + "/" + rs.getString(10) + "/" + rs.getString(8) + "/"
								+ rs.getInt("eventid") + "/" + rs.getString(12);
						stC2 = new StringTokenizer(result, "/");
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
						e2lv = stC2.nextToken();

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
						e2lvList.add(e2lv);

					}

					int randoms2 = (int) (Math.random() * (e2nameList.size() - 1));

					String tempName4 = "";
					StringTokenizer nameTK = new StringTokenizer(e2peopleList.get(randoms2), "#");
					tempName4 = nameTK.nextToken();

					if (e2kingList.get(randoms2).equals(tempName4)) {
						a2c1 = nullFilter("place#" + e2placeList.get(randoms2))
								+ nullFilter("reason#" + e2reason1List.get(randoms2) + "!" + e2reason2List.get(randoms2)
										+ "!" + e2reason3List.get(randoms2))
								+ nullFilter("what#" + e2whatList.get(randoms2))
								+ nullFilter("how#" + e2howList.get(randoms2)) + "$" + e2idList.get(randoms2);
					} else if (e2kingList.get(randoms2) != tempName4) {
						a2c1 = nullFilter("name#" + e2peopleList.get(randoms2))
								+ nullFilter("place#" + e2placeList.get(randoms2))
								+ nullFilter("reason#" + e2reason1List.get(randoms2) + "!" + e2reason2List.get(randoms2)
										+ "!" + e2reason3List.get(randoms2))
								+ nullFilter("what#" + e2whatList.get(randoms2))
								+ nullFilter("how#" + e2howList.get(randoms2)) + "$" + e2idList.get(randoms2);
					}
					System.out.println("c12");
					a2c1lv = e2lvList.get(randoms2);

					a1c1i = Integer.parseInt(a1c1lv);
					a1c2i = Integer.parseInt(a1c2lv);
					a1c3i = Integer.parseInt(a1c3lv);
					a2c1i = Integer.parseInt(a2c1lv);
					aver = (a1c1i + a1c2i + a1c3i + a2c1i) / 4;
					System.out.println("aver");
					System.out.println(aver);

					System.out.println(a1c1);
					System.out.println(a1c1lv);
					System.out.println("a1c2");
					System.out.println(a1c2);
					System.out.println(a1c2lv);
					System.out.println("a1c3");
					System.out.println(a1c3);
					System.out.println(a1c3lv);
					System.out.println("a2c1");
					System.out.println(a2c1);
					System.out.println(a2c1lv);
					System.out.println("aver");
					System.out.println(aver);
				}
			}
		} catch (SQLException e) {
		}

		aver2 = String.valueOf(aver);
		returnC1Data = a1c1 + "/" + a1c2 + "/" + a1c3 + "!" + a2c1 + "!" + aver2;

		return returnC1Data;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//// B유형

	static String selectA2(String keyA, String keyB, String keyC, String level) {
		String returnA1Data = "";
		String dataA1Name = "";
		int dataA1Id = 0;
		String dataA2Name = "";

		if (level.equals("1")) {
			if (keyB.equals("culture")) {
				keyB = "culture1";
			} else if (keyB.equals("event")) {
				keyB = "event1";
			}
			if (keyC.equals("culture")) {
				keyC = "culture1";
			} else if (keyC.equals("event")) {
				keyC = "event1";
			}
		} else if (level.equals("2")) {
			if (keyB.equals("culture")) {
				keyB = "culture2";
			} else if (keyB.equals("event")) {
				keyB = "event2";
			}
			if (keyC.equals("culture")) {
				keyC = "culture2";
			} else if (keyC.equals("event")) {
				keyC = "event2";
			}
		} else if (level.equals("3")) {
			if (keyB.equals("culture")) {
				keyB = "culture3";
			} else if (keyB.equals("event")) {
				keyB = "event3";
			}
			if (keyC.equals("culture")) {
				keyC = "culture3";
			} else if (keyC.equals("event")) {
				keyC = "event3";
			}
		}

		ArrayList<String> dataA2List = new ArrayList<>();

		int randomA1 = 0;
		int randomA2 = 0;

		int maxA = 0;
		int a = 0;
		int cBreakFlag = 0;

		try {
			stmt = null;
			rs = null;
			result = "";
			String countASQL = "select count(*) from " + keyA + "";
			stmt = con.createStatement();
			rs = stmt.executeQuery(countASQL);

			while (rs.next()) {
				result = rs.getString(1);
			}
			maxA = Integer.parseInt(result);

			// Data A1, A2를 뽑기위한 반복문
			while (a <= maxA) {
				a += 1;
				// A1을 뽑기 위한 반복문
				for (;;) {
					randomA1 = 1 + (int) (Math.random() * maxA);
					stmt = null;
					rs = null;
					result = "";

					System.out.println("-----------키B와 C--------------");
					System.out.println(keyB);
					System.out.println(keyC);

					if (keyA.equals("people")) {
						String selectA1SQL = "select * from people where peopleid=" + randomA1 + "";
						stmt = con.createStatement();
						rs = stmt.executeQuery(selectA1SQL);

						while (rs.next()) {
							result = rs.getString("peoplename") + "/" + rs.getString("peopleid");
							StringTokenizer stkA1 = new StringTokenizer(result, "/");
							dataA1Name = stkA1.nextToken();
							dataA1Id = Integer.parseInt(stkA1.nextToken());
						}
					}
					// 선택된 A1이 3개 이상의 C데이터와 1개 이상의 B데이터를 가지고 있는지 확인
					stmt = null;
					rs = null;
					result = "";

					String countA1BSQL = "select count(*) from " + keyB + " where " + keyB + "" + keyA + "='"
							+ dataA1Name + "'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(countA1BSQL);

					while (rs.next()) {
						result = rs.getString(1);
					}
					int countA1B = Integer.parseInt(result);
					if (countA1B > 0) { // A1의 B 데이터가 1개 이상일때, C 데이터를 3개 이상 가지는지
										// 확인
						stmt = null;
						rs = null;
						result = "";

						String countA1CSQL = "select count(*) from " + keyC + " where " + keyC + "" + keyA + "='"
								+ dataA1Name + "'";
						stmt = con.createStatement();
						rs = stmt.executeQuery(countA1CSQL);
						System.out.println("a1중간");
						while (rs.next()) {
							result = rs.getString(1);
						}
						int countA1C = Integer.parseInt(result);
						if (countA1C >= 3) {
							cBreakFlag = 1;
						}
						if (cBreakFlag == 1) {
							cBreakFlag = 0;
							break;
						}
					}
				} // A1 선택 반복문 종료

				stmt = null;
				rs = null;
				result = "";
				String selectA2SQL = "select * from people where ((peopleid<=(" + dataA1Id + "+4)) " + "&& (peopleid>=("
						+ dataA1Id + "-4))) && " + keyA + "name not like '" + dataA1Name + "'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(selectA2SQL);
				System.out.println("문제다1");
				while (rs.next()) {
					result = rs.getString("peoplename");
					dataA2List.add(result);
				}

				for (;;) {
					randomA2 = (int) (Math.random() * (dataA2List.size() - 1));
					dataA2Name = dataA2List.get(randomA2);
					stmt = null;
					rs = null;
					result = "";

					System.out.println("a2에서의 키 b와 c");
					System.out.println(keyB);
					System.out.println(keyC);

					String countA2SQL = "select count(*) from " + keyC + " where " + keyC + "people = '" + dataA2Name
							+ "'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(countA2SQL);
					System.out.println("a2성공1");
					while (rs.next()) {
						result = rs.getString(1);
					}
					System.out.println("a2성공2");
					int countA2C = Integer.parseInt(result);
					if (countA2C > 0)
						break;
				} // A2 시기 선택 종료
			}
		}

		catch (SQLException e) {
		}

		returnA1Data = dataA1Name + "/" + dataA2Name;
		return returnA1Data;
	} // A1 A2 반복문 종료

	//////////////////////////////////////////////////////////////
	static String selectB2(String dataA, String keyA, String keyB, String level) {
		String returnB1Data = "";
		String dataA1 = "";
		String bEname = "";
		String outType = "";
		System.out.println("b진입");
		double randomSil = Math.random();
		int intSil = (int) (randomSil * 2) + 1;

		if (intSil == 1)
			outType = "title";

		else if (intSil == 2)
			outType = "content";

		stmt = null;
		rs = null;
		result = "";

		StringTokenizer stDataBA = new StringTokenizer(dataA, "/");
		dataA1 = stDataBA.nextToken();

		System.out.println("dataA:");
		System.out.println(dataA);
		System.out.println("dataA1:");
		System.out.println(dataA1);

		try {
			stmt = null;
			rs = null;
			result = "";

			if (level.equals("1")) {
				if (keyB.equals("culture")) {
					keyB = "culture1";
				} else if (keyB.equals("event")) {
					keyB = "event1";
				}
			} else if (level.equals("2")) {
				if (keyB.equals("culture")) {
					keyB = "culture2";
				} else if (keyB.equals("event")) {
					keyB = "event2";
				}
			} else if (level.equals("3")) {
				if (keyB.equals("culture")) {
					keyB = "culture3";
				} else if (keyB.equals("event")) {
					keyB = "event3";
				}
			}

			System.out.println("-----------키B--------------");
			System.out.println(keyB);

			// dataA1,2를 가지는 keyB 데이터 요청
			String selectA1BSQL = "select * from " + keyB + " where " + keyB + "" + keyA + " = '" + dataA1 + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectA1BSQL);

			System.out.println("됨");

			if (keyB.equals("event1") || keyB.equals("event2") || keyB.equals("event3")) {
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
				String bElv = "";

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
				ArrayList<String> bElvList = new ArrayList<>();

				while (rs.next()) {
					result = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getInt(3) + "/" + rs.getString(4) + "/"
							+ rs.getString(6) + "/" + rs.getString(7) + "/" + rs.getString(5) + "/" + rs.getString(9)
							+ "/" + rs.getString(10) + "/" + rs.getString(8) + "/" + rs.getInt("eventid") + "/"
							+ rs.getString(12);
					;
					System.out.println("keyB가 event일때");
					System.out.println(result);

					stB = new StringTokenizer(result, "/");
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
					bElv = stB.nextToken();

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
					bElvList.add(bElv);
				}
				int bErand = (int) (Math.random() * (bEnameList.size() - 1));

				if (outType.equals("title")) {
					bEname = bEnameList.get(bErand) + "$" + bEidList.get(bErand) + "$" + bElvList.get(bErand);
				} else if (outType.equals("content")) {
					String tempName5 = "";
					StringTokenizer nameTK = new StringTokenizer(bEpeopleList.get(bErand), "#");
					tempName5 = nameTK.nextToken();

					if (bEkingList.get(bErand).equals(bEpeopleList.get(bErand))) {
						bEname = nullFilter("year#" + bEyearList.get(bErand))
								+ nullFilter("place#" + bEplaceList.get(bErand))
								+ nullFilter("reason1#" + bEreason1List.get(bErand) + "!" + bEreason2List.get(bErand)
										+ "!" + bEreason3List.get(bErand))
								+ nullFilter("what#" + bEwhatList.get(bErand))
								+ nullFilter("how#" + bEhowList.get(bErand)) + "$" + bEidList.get(bErand);
					} else {
						bEname = nullFilter("name#" + bEpeopleList.get(bErand))
								+ nullFilter("year#" + bEyearList.get(bErand))
								+ nullFilter("place#" + bEplaceList.get(bErand))
								+ nullFilter("reason1#" + bEreason1List.get(bErand) + "!" + bEreason2List.get(bErand)
										+ "!" + bEreason3List.get(bErand))
								+ nullFilter("what#" + bEwhatList.get(bErand))
								+ nullFilter("how#" + bEhowList.get(bErand)) + "$" + bEidList.get(bErand);
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
	static String selectC2(String dataA, String keyA, String keyC, String level) {
		String returnC1Data = "";
		String dataA1 = "";
		String dataA2 = "";

		String a1c1 = "";
		String a1c2 = "";
		String a1c3 = "";
		String a2c1 = "";
		String a1c1lv = "";
		String a1c2lv = "";
		String a1c3lv = "";
		String a2c1lv = "";
		double aver = 0;
		double a1c1i;
		double a1c2i;
		double a1c3i;
		double a2c1i;

		String outType = "";
		String aver2 = "";

		double randomSil = Math.random();
		int intSil = (int) (randomSil * 2) + 1;

		if (intSil == 1)
			outType = "title";

		else if (intSil == 2)
			outType = "content";

		StringTokenizer stDataA = new StringTokenizer(dataA, "/");
		dataA1 = stDataA.nextToken();
		dataA2 = stDataA.nextToken();

		try {
			stmt = null;
			rs = null;
			result = "";
			String lv = "";

			if (level.equals("1")) {

				if (keyC.equals("culture")) {
					keyC = "culture1";
				} else if (keyC.equals("event")) {
					keyC = "event1";
				}
			} else if (level.equals("2")) {

				if (keyC.equals("culture")) {
					keyC = "culture2";
				} else if (keyC.equals("event")) {
					keyC = "event2";
				}
			} else if (level.equals("3")) {
				if (keyC.equals("culture")) {
					keyC = "culture3";
				} else if (keyC.equals("event")) {
					keyC = "event3";
				}
			}

			System.out.println("-----------키C--------------");
			System.out.println(keyC);

			// dataA1,2를 가지는 keyB 데이터 요청
			String selectA1CSQL = "select * from " + keyC + " where " + keyC + "" + keyA + " = '" + dataA1 + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectA1CSQL);

			if (keyC.equals("event1") || keyC.equals("event2") || keyC.equals("event3")) {
				if (outType.equals("title")) {
					ArrayList<String> a1enameList = new ArrayList<>();
					ArrayList<String> a2enameList = new ArrayList<>();
					ArrayList<String> a1lvList = new ArrayList<>();
					ArrayList<String> a2lvList = new ArrayList<>();

					while (rs.next()) {
						result = rs.getString("eventname") + "$" + rs.getInt("eventid");
						a1enameList.add(result);
						lv = rs.getString("eventlv");
						a1lvList.add(lv);

					}
					int[] randoms = new int[8];
					int z = 2;
					for (;;) {
						randoms[z] = (int) (Math.random() * a1enameList.size());
						if (randoms[z] != randoms[z + 1] && randoms[z] != randoms[z + 2]) {
							z = z - 1;
						}
						if (z < 0)
							break;
					}
					a1c1 = a1enameList.get(randoms[0]);
					a1c2 = a1enameList.get(randoms[1]);
					a1c3 = a1enameList.get(randoms[2]);
					a1c1lv = a1lvList.get(randoms[0]);
					a1c2lv = a1lvList.get(randoms[1]);
					a1c3lv = a1lvList.get(randoms[2]);

					stmt = null;
					rs = null;
					String lv2;
					result = "";
					String selectA2CSQL = "select * from " + keyC + " where " + keyC + "" + keyA + " = '" + dataA2
							+ "'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(selectA2CSQL);

					while (rs.next()) {
						result = rs.getString("eventname") + "$" + rs.getInt("eventid") + "$" + rs.getString("eventlv");
						a2enameList.add(result);
						lv2 = rs.getString("eventlv");
						a2lvList.add(lv2);
					}
					int a2rand = (int) (Math.random() * (a2enameList.size() - 1));
					a2c1 = a2enameList.get(a2rand);
					a2c1lv = a2lvList.get(a2rand);

					a1c1i = Integer.parseInt(a1c1lv);
					a1c2i = Integer.parseInt(a1c2lv);
					a1c3i = Integer.parseInt(a1c3lv);
					a2c1i = Integer.parseInt(a2c1lv);
					aver = (a1c1i + a1c2i + a1c3i + a2c1i) / 4;
					System.out.println("aver");
					System.out.println(aver);

					System.out.println(a1c1);
					System.out.println(a1c1lv);
					System.out.println("a1c2");
					System.out.println(a1c2);
					System.out.println(a1c2lv);
					System.out.println("a1c3");
					System.out.println(a1c3);
					System.out.println(a1c3lv);
					System.out.println("a2c1");
					System.out.println(a2c1);
					System.out.println(a2c1lv);
					System.out.println("aver");
					System.out.println(aver);

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
					String elv = "";
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
					ArrayList<String> elvList = new ArrayList<>();

					while (rs.next()) {
						result = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getInt(3) + "/" + rs.getString(4)
								+ "/" + rs.getString(6) + "/" + rs.getString(7) + "/" + rs.getString(5) + "/"
								+ rs.getString(9) + "/" + rs.getString(10) + "/" + rs.getString(8) + "/"
								+ rs.getInt("eventid") + "/" + rs.getString(12);
						stC1 = new StringTokenizer(result, "/");
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
						elv = stC1.nextToken();

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
						elvList.add(elv);
					}

					int[] randoms = new int[8];
					int z = 3;
					for (;;) {
						randoms[z] = (int) (Math.random() * enameList.size());
						if (randoms[z] != randoms[z + 1] && randoms[z] != randoms[z + 2]) {
							z = z - 1;
						}
						if (z < 0)
							break;
					}

					ArrayList<String> tempName = new ArrayList<>();
					for (int t = 0; t < 4; t++) {
						String temps = "";
						StringTokenizer nameTK = new StringTokenizer(epeopleList.get(randoms[t]), "#");
						temps = nameTK.nextToken();
						tempName.add(temps);
					}

					if (ekingList.get(randoms[0]).equals(tempName.get(0))) {
						a1c1 = nullFilter("place#" + eplaceList.get(randoms[0]))
								+ nullFilter("reason#" + ereason1List.get(randoms[0]) + "!"
										+ ereason2List.get(randoms[0]) + "!" + ereason3List.get(randoms[0]))
								+ nullFilter("what#" + ewhatList.get(randoms[0]))
								+ nullFilter("how#" + ehowList.get(randoms[0])) + "$" + eidList.get(randoms[0]) + "$"
								+ elvList.get(randoms[0]);
					} else if (ekingList.get(randoms[0]) != tempName.get(0)) {
						a1c1 = nullFilter("name#" + epeopleList.get(randoms[0]))
								+ nullFilter("place#" + eplaceList.get(randoms[0]))
								+ nullFilter("reason#" + ereason1List.get(randoms[0]) + "!"
										+ ereason2List.get(randoms[0]) + "!" + ereason3List.get(randoms[0]))
								+ nullFilter("what#" + ewhatList.get(randoms[0]))
								+ nullFilter("how#" + ehowList.get(randoms[0])) + "$" + eidList.get(randoms[0]) + "$"
								+ elvList.get(randoms[0]);
					}
					if (ekingList.get(randoms[1]).equals(tempName.get(1))) {
						a1c2 = nullFilter("place#" + eplaceList.get(randoms[1]))
								+ nullFilter("reason#" + ereason1List.get(randoms[1]) + "!"
										+ ereason2List.get(randoms[1]) + "!" + ereason3List.get(randoms[1]))
								+ nullFilter("what#" + ewhatList.get(randoms[1]))
								+ nullFilter("how#" + ehowList.get(randoms[1])) + "$" + eidList.get(randoms[1]) + "$"
								+ elvList.get(randoms[1]);
					} else if (ekingList.get(randoms[1]) != tempName.get(1)) {
						a1c2 = nullFilter("name#" + epeopleList.get(randoms[1]))
								+ nullFilter("place#" + eplaceList.get(randoms[1]))
								+ nullFilter("reason#" + ereason1List.get(randoms[1]) + "!"
										+ ereason2List.get(randoms[1]) + "!" + ereason3List.get(randoms[1]))
								+ nullFilter("what#" + ewhatList.get(randoms[1]))
								+ nullFilter("how#" + ehowList.get(randoms[1])) + "$" + eidList.get(randoms[1]) + "$"
								+ elvList.get(randoms[1]);
					}
					if (ekingList.get(randoms[2]).equals(tempName.get(2))) {
						a1c3 = nullFilter("place#" + eplaceList.get(randoms[2]))
								+ nullFilter("reason#" + ereason1List.get(randoms[2]) + "!"
										+ ereason2List.get(randoms[2]) + "!" + ereason3List.get(randoms[2]))
								+ nullFilter("what#" + ewhatList.get(randoms[2]))
								+ nullFilter("how#" + ehowList.get(randoms[2])) + "$" + eidList.get(randoms[2]) + "$"
								+ elvList.get(randoms[2]);
					} else if (ekingList.get(randoms[2]) != tempName.get(2)) {
						a1c3 = nullFilter("name#" + epeopleList.get(randoms[2]))
								+ nullFilter("place#" + eplaceList.get(randoms[2]))
								+ nullFilter("reason#" + ereason1List.get(randoms[2]) + "!"
										+ ereason2List.get(randoms[2]) + "!" + ereason3List.get(randoms[2]))
								+ nullFilter("what#" + ewhatList.get(randoms[2]))
								+ nullFilter("how#" + ehowList.get(randoms[2])) + "$" + eidList.get(randoms[2]) + "$"
								+ elvList.get(randoms[2]);
					}
					a1c1lv = elvList.get(randoms[0]);
					a1c2lv = elvList.get(randoms[1]);
					a1c3lv = elvList.get(randoms[2]);

					stmt = null;
					rs = null;
					result = "";
					String selectA2BSQL = "select * from " + keyC + " where " + keyC + "" + keyA + " = '" + dataA2
							+ "'";
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
					String e2lv = "";

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
					ArrayList<String> e2lvList = new ArrayList<>();

					while (rs.next()) { // 2 = people, 8 = king
						result = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getInt(3) + "/" + rs.getString(4)
								+ "/" + rs.getString(6) + "/" + rs.getString(7) + "/" + rs.getString(5) + "/"
								+ rs.getString(9) + "/" + rs.getString(10) + "/" + rs.getString(8) + "/"
								+ rs.getInt("eventid") + "/" + rs.getString(12);
						stC2 = new StringTokenizer(result, "/");
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
						e2lv = stC2.nextToken();

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
						e2lvList.add(e2lv);
					}

					int randoms2 = (int) (Math.random() * (e2nameList.size() - 1));

					String tempName4 = "";
					StringTokenizer nameTK = new StringTokenizer(e2peopleList.get(randoms2), "#");
					tempName4 = nameTK.nextToken();

					if (e2kingList.get(randoms2).equals(tempName4)) {
						a2c1 = nullFilter("place#" + e2placeList.get(randoms2))
								+ nullFilter("reason#" + e2reason1List.get(randoms2) + "!" + e2reason2List.get(randoms2)
										+ "!" + e2reason3List.get(randoms2))
								+ nullFilter("what#" + e2whatList.get(randoms2))
								+ nullFilter("how#" + e2howList.get(randoms2)) + "$" + e2idList.get(randoms2);
					} else if (e2kingList.get(randoms2) != tempName4) {
						a2c1 = nullFilter("name#" + e2peopleList.get(randoms2))
								+ nullFilter("place#" + e2placeList.get(randoms2))
								+ nullFilter("reason#" + e2reason1List.get(randoms2) + "!" + e2reason2List.get(randoms2)
										+ "!" + e2reason3List.get(randoms2))
								+ nullFilter("what#" + e2whatList.get(randoms2))
								+ nullFilter("how#" + e2howList.get(randoms2)) + "$" + e2idList.get(randoms2);
					}
					a2c1lv = e2lvList.get(randoms2);

					a1c1i = Integer.parseInt(a1c1lv);
					a1c2i = Integer.parseInt(a1c2lv);
					a1c3i = Integer.parseInt(a1c3lv);
					a2c1i = Integer.parseInt(a2c1lv);
					aver = (a1c1i + a1c2i + a1c3i + a2c1i) / 4;
					System.out.println("aver");
					System.out.println(aver);

					System.out.println(a1c1);
					System.out.println(a1c1lv);
					System.out.println("a1c2");
					System.out.println(a1c2);
					System.out.println(a1c2lv);
					System.out.println("a1c3");
					System.out.println(a1c3);
					System.out.println(a1c3lv);
					System.out.println("a2c1");
					System.out.println(a2c1);
					System.out.println(a2c1lv);
					System.out.println("aver");
					System.out.println(aver);
				}
			}
		} catch (SQLException e) {
		}

		aver2 = String.valueOf(aver);
		returnC1Data = a1c1 + "/" + a1c2 + "/" + a1c3 + "!" + a2c1 + "!" + aver2;

		return returnC1Data;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// C유형
	static String selectA3(String KeyA, String KeyB, String level) {
		String orderResult = "";
		String order1 = "";
		String order2 = "";
		String order3 = "";
		String order4 = "";
		String order5 = "";
		String order6 = "";
		String order1lv = "";
		String order2lv = "";
		String order3lv = "";
		String order4lv = "";
		String order5lv = "";
		String order6lv = "";
		double order1i = 0;
		double order2i = 0;
		double order3i = 0;
		double order4i = 0;
		double order5i = 0;
		double order6i = 0;
		String outType = "";
		String lv = "";
		double aver = 0;

		double randomSil = Math.random();
		int intSil = (int) (randomSil * 2) + 1;

		if (intSil == 1)
			outType = "title";

		else if (intSil == 2)
			outType = "content";

		if (level.equals("1")) {
			if (KeyB.equals("culture")) {
				KeyB = "culture1";
			} else if (KeyB.equals("event")) {
				KeyB = "event1";
			}

		} else if (level.equals("2")) {
			if (KeyB.equals("culture")) {
				KeyB = "culture2";
			} else if (KeyB.equals("event")) {
				KeyB = "event2";
			}

		} else if (level.equals("3")) {
			if (KeyB.equals("culture")) {
				KeyB = "culture3";
			} else if (KeyB.equals("event")) {
				KeyB = "event3";
			}

		}

		System.out.println("-----------키B--------------");
		System.out.println(KeyB);

		ArrayList<String> cnameList = new ArrayList<>();
		ArrayList<String> clvList = new ArrayList<>();

		ArrayList<String> enameList = new ArrayList<>();
		ArrayList<String> elvList = new ArrayList<>();

		ArrayList<String> oEnameList = new ArrayList<>();
		ArrayList<String> oElvList = new ArrayList<>();

		try {

			stmt = null;
			rs = null;
			result = "";

			String orderSQL = "select * from " + KeyB + " where " + KeyB + "year not like '0' order by " + KeyB
					+ "year asc";
			stmt = con.createStatement();
			rs = stmt.executeQuery(orderSQL);

			if (KeyA.equals("sort") || KeyA.equals("after") || KeyA.equals("between")) {

				if (KeyB.equals("culture1") || KeyB.equals("culture2") || KeyB.equals("culture3")) {

					while (rs.next()) {
						result = rs.getString("culturename") + "$" + rs.getInt("cultureid") + "$"
								+ rs.getString("culturelv");
						lv = rs.getString("culturelv");
						cnameList.add(result);
						clvList.add(lv);

					}

					int crand1 = (int) (Math.random() * ((cnameList.size() - 3) - 4 + 1)) + 4;
					if (crand1 < 0) {
						crand1 *= -1;
					}
					int crand2 = 1 + (int) (Math.random() * 1);

					order1 = cnameList.get(crand1);
					order2 = cnameList.get(crand1 + crand2);
					order3 = cnameList.get((crand1 + crand2) + 1);
					order4 = cnameList.get((crand1 + crand2) + 2);
					order5 = cnameList.get((crand1 + crand2) + 3);
					order6 = cnameList.get((crand1 + crand2) + 4);

					order1lv = clvList.get(crand1);
					order2lv = clvList.get(crand1 + crand2);
					order3lv = clvList.get((crand1 + crand2) + 1);
					order4lv = clvList.get((crand1 + crand2) + 2);
					order5lv = clvList.get((crand1 + crand2) + 3);
					order6lv = clvList.get((crand1 + crand2) + 4);

					order1i = Integer.parseInt(order1lv);
					order2i = Integer.parseInt(order2lv);
					order3i = Integer.parseInt(order3lv);
					order4i = Integer.parseInt(order4lv);
					order5i = Integer.parseInt(order5lv);
					order6i = Integer.parseInt(order6lv);

					aver = (order1i + order2i + order3i) / 3;
					System.out.println("3");
					System.out.println("order1");
					System.out.println(order1);
					System.out.println("order2");
					System.out.println(order2);
					System.out.println("order3");
					System.out.println(order3);
					System.out.println("order4");
					System.out.println(order4);
					System.out.println("order5");
					System.out.println(order5);
					System.out.println("order6");
					System.out.println(order6);

				} else if (KeyB.equals("event1") || KeyB.equals("event2") || KeyB.equals("event3")) {
					System.out.println(outType);

					if (outType.equals("title")) {
						System.out.println("aa");
						while (rs.next()) {

							result = rs.getString("eventname") + "$" + rs.getInt("eventid") + "$"
									+ rs.getString("eventlv");
							enameList.add(result);
							lv = rs.getString("eventlv");
							elvList.add(lv);
						}
						int erand1 = (int) (Math.random() * ((enameList.size() - 3) - 4 + 1)) + 4;
						int erand2 = 1 + (int) (Math.random() * 1);
						if (erand1 < 0) {
							erand1 *= -1;
						}

						order1 = enameList.get(erand1);
						order2 = enameList.get(erand1 + erand2);
						order3 = enameList.get((erand1 + erand2) + 1);
						order4 = enameList.get((erand1 + erand2) + 2);
						order5 = enameList.get((erand1 + erand2) + 3);
						order6 = enameList.get((erand1 + erand2) + 4);

						order1lv = elvList.get(erand1);
						order2lv = elvList.get(erand1 + erand2);
						order3lv = elvList.get((erand1 + erand2) + 1);
						order4lv = elvList.get((erand1 + erand2) + 2);
						order5lv = elvList.get((erand1 + erand2) + 3);
						order6lv = elvList.get((erand1 + erand2) + 4);

						order1i = Integer.parseInt(order1lv);
						order2i = Integer.parseInt(order2lv);
						order3i = Integer.parseInt(order3lv);
						order4i = Integer.parseInt(order4lv);
						order5i = Integer.parseInt(order5lv);
						order6i = Integer.parseInt(order6lv);

						System.out.println("aa5");

						System.out.println("order1");
						System.out.println(order1);
						System.out.println("order2");
						System.out.println(order2);
						System.out.println("order3");
						System.out.println(order3);
						System.out.println("order4");
						System.out.println(order4);
						System.out.println("order5");
						System.out.println(order5);
						System.out.println("order6");
						System.out.println(order6);

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
						String oElv = "";

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

						System.out.println("ss");
						while (rs.next()) {

							result = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getInt(3) + "/"
									+ rs.getString(4) + "/" + rs.getString(6) + "/" + rs.getString(7) + "/"
									+ rs.getString(5) + "/" + rs.getString(9) + "/" + rs.getString(10) + "/"
									+ rs.getInt("eventid") + "/" + rs.getString(12);

							stO = new StringTokenizer(result, "/");
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
							oElv = stO.nextToken();

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
							oElvList.add(oElv);

						}
						int oErand1 = (int) (Math.random() * ((oEnameList.size() - 3) - 4 + 1)) + 4;
						int oErand2 = 1 + (int) (Math.random() * 1);
						if (oErand1 < 0) {
							oErand1 *= -1;
						}

						order1 = nullFilter("name#" + oEpeopleList.get(oErand1))
								+ nullFilter("place#" + oEplaceList.get(oErand1))
								+ nullFilter("reason#" + oEreason1List.get(oErand1) + "!" + oEreason2List.get(oErand1)
										+ "!" + oEreason3List.get(oErand1))
								+ nullFilter("what#" + oEwhatList.get(oErand1))
								+ nullFilter("how#" + oEhowList.get(oErand1)) + "$" + oEidList.get(oErand1) + "$"
								+ oElvList.get(oErand1);
						order2 = nullFilter("name#" + oEpeopleList.get(oErand1 + oErand2))
								+ nullFilter("place#" + oEplaceList.get(oErand1 + oErand2))
								+ nullFilter("reason#" + oEreason1List.get(oErand1 + oErand2) + "!"
										+ oEreason2List.get(oErand1 + oErand2) + "!"
										+ oEreason3List.get(oErand1 + oErand2))
								+ nullFilter("what#" + oEwhatList.get(oErand1 + oErand2))
								+ nullFilter("how#" + oEhowList.get(oErand1 + oErand2)) + "$"
								+ oEidList.get(oErand1 + oErand2) + "$" + oElvList.get(oErand1 + oErand2);
						order3 = nullFilter("name#" + oEpeopleList.get((oErand1 + oErand2) + 1))
								+ nullFilter("place#" + oEplaceList.get((oErand1 + oErand2) + 1))
								+ nullFilter("reason#" + oEreason1List.get((oErand1 + oErand2) + 1) + "!"
										+ oEreason2List.get((oErand1 + oErand2) + 1) + "!"
										+ oEreason3List.get((oErand1 + oErand2) + 1))
								+ nullFilter("what#" + oEwhatList.get((oErand1 + oErand2) + 1))
								+ nullFilter("how#" + oEhowList.get((oErand1 + oErand2) + 1)) + "$"
								+ oEidList.get((oErand1 + oErand2) + 1) + "$" + oElvList.get((oErand1 + oErand2) + 1);
						order4 = nullFilter("name#" + oEpeopleList.get((oErand1 + oErand2) + 2))
								+ nullFilter("place#" + oEplaceList.get((oErand1 + oErand2) + 2))
								+ nullFilter("reason#" + oEreason1List.get((oErand1 + oErand2) + 2) + "!"
										+ oEreason2List.get((oErand1 + oErand2) + 2) + "!"
										+ oEreason3List.get((oErand1 + oErand2) + 2))
								+ nullFilter("what#" + oEwhatList.get((oErand1 + oErand2) + 2))
								+ nullFilter("how#" + oEhowList.get((oErand1 + oErand2) + 2)) + "$"
								+ oEidList.get((oErand1 + oErand2) + 2) + "$" + oElvList.get((oErand1 + oErand2) + 2);
						order5 = nullFilter("name#" + oEpeopleList.get((oErand1 + oErand2) + 3))
								+ nullFilter("place#" + oEplaceList.get((oErand1 + oErand2) + 3))
								+ nullFilter("reason#" + oEreason1List.get((oErand1 + oErand2) + 3) + "!"
										+ oEreason2List.get((oErand1 + oErand2) + 3) + "!"
										+ oEreason3List.get((oErand1 + oErand2) + 3))
								+ nullFilter("what#" + oEwhatList.get((oErand1 + oErand2) + 3))
								+ nullFilter("how#" + oEhowList.get((oErand1 + oErand2) + 3)) + "$"
								+ oEidList.get((oErand1 + oErand2) + 3) + "$" + oElvList.get((oErand1 + oErand2) + 3);
						order6 = nullFilter("name#" + oEpeopleList.get((oErand1 + oErand2) + 4))
								+ nullFilter("place#" + oEplaceList.get((oErand1 + oErand2) + 4))
								+ nullFilter("reason#" + oEreason1List.get((oErand1 + oErand2) + 4) + "!"
										+ oEreason2List.get((oErand1 + oErand2) + 4) + "!"
										+ oEreason3List.get((oErand1 + oErand2) + 4))
								+ nullFilter("what#" + oEwhatList.get((oErand1 + oErand2) + 4))
								+ nullFilter("how#" + oEhowList.get((oErand1 + oErand2) + 4)) + "$"
								+ oEidList.get((oErand1 + oErand2) + 4) + "$" + oElvList.get((oErand1 + oErand2) + 4);

						System.out.println("ss23");

						order1lv = oElvList.get(oErand1);
						order2lv = oElvList.get(oErand1 + oErand2);
						order3lv = oElvList.get((oErand1 + oErand2) + 1);
						order4lv = oElvList.get((oErand1 + oErand2) + 2);
						order5lv = oElvList.get((oErand1 + oErand2) + 3);
						order6lv = oElvList.get((oErand1 + oErand2) + 4);

						System.out.println("ss24");

						order1i = Integer.parseInt(order1lv);
						order2i = Integer.parseInt(order2lv);
						order3i = Integer.parseInt(order3lv);
						order4i = Integer.parseInt(order4lv);
						order5i = Integer.parseInt(order5lv);
						order6i = Integer.parseInt(order6lv);

						System.out.println("ss3");

						System.out.println("order1");
						System.out.println(order1);
						System.out.println("order2");
						System.out.println(order2);
						System.out.println("order3");
						System.out.println(order3);
						System.out.println("order4");
						System.out.println(order4);
						System.out.println("order5");
						System.out.println(order5);
						System.out.println("order6");
						System.out.println(order6);
					}
					System.out.println("ss4");
				}
				System.out.println("ss5");

				if (KeyA.equals("sort")) {
					System.out.println("sort완료");
					aver = (order1i + order2i + order3i) / 3;
					orderResult = order1 + "!" + order2 + "!" + order3 + "!" + aver;
					System.out.println(orderResult);
					return orderResult;
				} else if (KeyA.equals("after")) {
					aver = (order1i + order2i + order3i + order4i + order5i) / 5;
					orderResult = order1 + "!" + order2 + "!" + order3 + "!" + order4 + "!" + order5 + "!" + aver;
					System.out.println(orderResult);
					return orderResult;
				} else if (KeyA.equals("between")) {
					aver = (order1i + order2i + order3i + order4i + order5i + order6i) / 6;
					orderResult = order1 + "!" + order2 + "!" + order3 + "!" + order4 + "!" + order5 + "!" + order6
							+ "!" + aver;
					System.out.println(orderResult);
				}

				/////

			}

		} catch (SQLException e) {
		}

		return orderResult;

	}

	//////////////////////////////////////////////////////////////////////////////////////

	static String resultp(String resultContent, String level) {
		String resultp = "";
		String mType1 = "";
		String mType2 = "";
		String qid1R = "", qid2R = "", qid3R = "", qid4R = "", qid5R = "", qid6R = "";
		int qid1 = 0, qid2 = 0, qid3 = 0, qid4 = 0, qid5 = 0, qid6 = 0;

		// ABC1E1A : mType1 = keyB, mType2 = keyC, qid1 = keyC, qid2~5 = keyB
		// qzcontent.add(mType1 + "/" + mType2 + "/" + qid1 + "/" + qid2 + "/" +
		// qid3 + "/" + qid4 + "/" + qid5 + "/" + qid6);
		System.out.println("resultcontent");
		System.out.println(resultContent);

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

			if (level.equals("1")) {
				if (mType1.equals("culture")) {
					mType1 = "culture1";
				} else if (mType1.equals("event")) {
					mType1 = "event1";
				}
				if (mType2.equals("culture")) {
					mType2 = "culture1";
				} else if (mType2.equals("event")) {
					mType2 = "event1";
				}
			} else if (level.equals("2")) {
				if (mType1.equals("culture")) {
					mType1 = "culture2";
				} else if (mType1.equals("event")) {
					mType1 = "event2";
				}
				if (mType2.equals("culture")) {
					mType2 = "culture2";
				} else if (mType2.equals("event")) {
					mType2 = "event2";
				}
			} else if (level.equals("3")) {
				if (mType1.equals("culture")) {
					mType1 = "culture3";
				} else if (mType1.equals("event")) {
					mType1 = "event3";
				}
				if (mType2.equals("culture")) {
					mType2 = "culture3";
				} else if (mType2.equals("event")) {
					mType2 = "event3";
				}
			}
			// ABC1E1A : mType1 = keyB, mType2 = keyC, qid1 = keyC, qid2~5 =
			// keyB
			if (mType2.equals("null")) { // ABC를 제외한 나머지 유형
				if (qid1 != 0) {
					if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
						resultSQL = "select * from " + mType1 + " where cultureid = " + qid1 + "";
					} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
						resultSQL = "select * from " + mType1 + " where eventid = " + qid1 + "";
					}
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);

					while (rs.next()) {
						if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString("cultureking") + ", " + "" + rs.getString("cultureyear") + "!"
									+ rs.getInt("cultureid");
							qid1R = result;
						} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid1R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid1R = "null#null";
				}

				if (qid2 != 0) {
					if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
						resultSQL = "select * from " + mType1 + " where cultureid = " + qid2 + "";
					} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
						resultSQL = "select * from " + mType1 + " where eventid = " + qid2 + "";
					}
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);
					while (rs.next()) {
						if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString("cultureking") + ", " + rs.getString("cultureyear") + "!"
									+ rs.getInt("cultureid");
							qid2R = result;
						} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid2R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid2R = "null#null";
				}

				if (qid3 != 0) {
					if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
						resultSQL = "select * from " + mType1 + " where cultureid = " + qid3 + "";
					} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
						resultSQL = "select * from " + mType1 + " where eventid = " + qid3 + "";
					}
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);
					while (rs.next()) {
						if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString(mType1 + "king") + ", " + rs.getString(mType1 + "year") + "!"
									+ rs.getInt("cultureid");
							qid3R = result;
						} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid3R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid3R = "null#null";
				}

				if (qid4 != 0) {
					if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
						resultSQL = "select * from " + mType1 + " where cultureid = " + qid4 + "";
					} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
						resultSQL = "select * from " + mType1 + " where eventid = " + qid4 + "";
					}
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);
					while (rs.next()) {
						if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString(mType1 + "king") + ", " + rs.getString(mType1 + "year") + "!"
									+ rs.getInt("cultureid");
							qid4R = result;
						} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid4R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid4R = "null#null";
				}

				if (qid5 != 0) {
					if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
						resultSQL = "select * from " + mType1 + " where cultureid = " + qid5 + "";
					} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
						resultSQL = "select * from " + mType1 + " where eventid = " + qid5 + "";
					}
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);
					while (rs.next()) {
						if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString(mType1 + "king") + ", " + rs.getString(mType1 + "year") + "!"
									+ rs.getInt("cultureid");
							qid5R = result;
						} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid5R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid5R = "null#null";
				}

				if (qid6 != 0) {
					if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
						resultSQL = "select * from " + mType1 + " where cultureid = " + qid6 + "";
					} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
						resultSQL = "select * from " + mType1 + " where eventid = " + qid6 + "";
					}
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);
					while (rs.next()) {
						if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString(mType1 + "king") + ", " + rs.getString(mType1 + "year") + "!"
									+ rs.getInt("cultureid");
							qid6R = result;
						} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid6R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid6R = "null#null";
				}

				resultp = qid1R + "/" + qid2R + "/" + qid3R + "/" + qid4R + "/" + qid5R + "/" + qid6R;

			} else { // ABC1E1A : mType1 = keyB, mType2 = keyC, qid1 = keyC,
						// qid2~5 = keyB

				if (qid1 != 0) {
					if (mType2.equals("culture1") || mType2.equals("culture2") || mType2.equals("culture3")) {
						resultSQL = "select * from " + mType2 + " where cultureid = " + qid1 + "";
					} else if (mType2.equals("event1") || mType2.equals("event2") || mType2.equals("event3")) {
						resultSQL = "select * from " + mType2 + " where eventid = " + qid1 + "";
					}
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);
					while (rs.next()) {
						if (mType2.equals("culture1") || mType2.equals("culture2") || mType2.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString(mType2 + "king") + ", " + rs.getString(mType2 + "year") + "!"
									+ rs.getInt("cultureid");
							qid1R = result;
						} else if (mType2.equals("event1") || mType2.equals("event2") || mType2.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid1R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid1R = "null#null";
				}

				if (qid2 != 0) {
					if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
						resultSQL = "select * from " + mType1 + " where cultureid = " + qid2 + "";
					} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
						resultSQL = "select * from " + mType1 + " where eventid = " + qid2 + "";
					}
					;
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);
					while (rs.next()) {
						if (mType2.equals("culture1") || mType2.equals("culture2") || mType2.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString(mType2 + "king") + ", " + rs.getString(mType2 + "year") + "!"
									+ rs.getInt("cultureid");
							qid2R = result;
						} else if (mType2.equals("event1") || mType2.equals("event2") || mType2.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid2R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid2R = "null#null";
				}

				if (qid3 != 0) {
					if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
						resultSQL = "select * from " + mType1 + " where cultureid = " + qid3 + "";
					} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
						resultSQL = "select * from " + mType1 + " where eventid = " + qid3 + "";
					}
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);
					while (rs.next()) {
						if (mType2.equals("culture1") || mType2.equals("culture2") || mType2.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString(mType2 + "king") + ", " + rs.getString(mType2 + "year") + "!"
									+ rs.getInt("cultureid");
							qid3R = result;
						} else if (mType2.equals("event1") || mType2.equals("event2") || mType2.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid3R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid3R = "null#null";
				}

				if (qid4 != 0) {
					if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
						resultSQL = "select * from " + mType1 + " where cultureid = " + qid4 + "";
					} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
						resultSQL = "select * from " + mType1 + " where eventid = " + qid4 + "";
					}
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);
					while (rs.next()) {
						if (mType2.equals("culture1") || mType2.equals("culture2") || mType2.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString(mType2 + "king") + ", " + rs.getString(mType2 + "year") + "!"
									+ rs.getInt("cultureid");
							qid4R = result;
						} else if (mType2.equals("event1") || mType2.equals("event2") || mType2.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid4R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid4R = "null#null";
				}

				if (qid5 != 0) {
					if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
						resultSQL = "select * from " + mType1 + " where cultureid = " + qid5 + "";
					} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
						resultSQL = "select * from " + mType1 + " where eventid = " + qid5 + "";
					}
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);
					while (rs.next()) {
						if (mType2.equals("culture1") || mType2.equals("culture2") || mType2.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString(mType2 + "king") + ", " + rs.getString(mType2 + "year") + "!"
									+ rs.getInt("cultureid");
							qid5R = result;
						} else if (mType2.equals("event1") || mType2.equals("event2") || mType2.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid5R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid5R = "null#null";
				}

				if (qid6 != 0) {
					if (mType1.equals("culture1") || mType1.equals("culture2") || mType1.equals("culture3")) {
						resultSQL = "select * from " + mType1 + " where cultureid = " + qid6 + "";
					} else if (mType1.equals("event1") || mType1.equals("event2") || mType1.equals("event3")) {
						resultSQL = "select * from " + mType1 + " where eventid = " + qid6 + "";
					}
					stmt = con.createStatement();
					rs = stmt.executeQuery(resultSQL);
					while (rs.next()) {
						if (mType2.equals("culture1") || mType2.equals("culture2") || mType2.equals("culture3")) {
							result = "culture#" + rs.getString("culturename") + "!" + rs.getString("cetc") + "!"
									+ rs.getString(mType2 + "king") + ", " + rs.getString(mType2 + "year") + "!"
									+ rs.getInt("cultureid");
							qid6R = result;
						} else if (mType2.equals("event1") || mType2.equals("event2") || mType2.equals("event3")) {
							result = "event#" + rs.getString(1) + ": " + nullFilter("name#" + rs.getString(2)) + ""
									+ nullFilter("year#" + rs.getInt(3)) + "" + nullFilter("place#" + rs.getString(4))
									+ ""
									+ nullFilter("reason#" + rs.getString(5) + "!" + rs.getString(9) + "!"
											+ rs.getString(10))
									+ "" + nullFilter("what#" + rs.getString(6)) + ""
									+ nullFilter("how#" + rs.getString(7));
							qid6R = result;
						}
					}
					stmt = null;
					rs = null;
				} else {
					qid6R = "null#null";
				}
				resultp = qid1R + "/" + qid2R + "/" + qid3R + "/" + qid4R + "/" + qid5R + "/" + qid6R;
			}

		} catch (SQLException e) {
		}

		return resultp;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	static String nullFilter(String text) {
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
			if (reason1.equals("NULL")) {
				outText = "";
			} else if (reason1 != "NULL" && reason2.equals("NULL")) {
				outText = reason1 + " ";
			} else if (reason2 != "NULL" && reason3.equals("NULL")) {
				outText = reason1 + " " + reason2 + " ";
			} else if (reason3 != "NULL") {
				outText = reason1 + " " + reason2 + " " + reason3 + " ";
			}
		}
		if (sID.equals("name")) {
			if (inText.equals("null")) {
				outText = "";
			} else {
				if (state.equals("o")) {
					outText = inText + "은 ";
				} else if (state.equals("x")) {
					outText = inText + "는 ";
				}
			}
		}
		if (sID.equals("year")) {
			if (inText.equals("0")) {
				outText = "";
			} else {
				outText = inText + "년 ";
			}
		}
		if (sID.equals("place")) {
			if (inText.equals("NULL")) {
				outText = "";
			} else {
				outText = inText + "에서 ";
			}
		}
		if (sID.equals("what")) {
			if (inText.equals("null")) {
				outText = "";
			} else {
				if (state.equals("o")) {
					outText = inText + "을 ";
				} else if (state.equals("x")) {
					outText = inText + "를 ";
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

	////////////////////////////////////////////////////////////////////////////////////// 이미지스트링으로변환
	static byte[] selectImg(String bdata, String keyB, String level) throws IOException {
		String query = null;
		String culimg = null;
		byte[] buf = null;

		String sel1 = "";
		String sel2 = ""; // 보기의 id

		StringTokenizer atk = new StringTokenizer(bdata, "$");
		sel1 = atk.nextToken();
		sel2 = atk.nextToken();

		if (level.equals("1")) {
			if (keyB.equals("culture")) {
				keyB = "culture1";
			}
		} else if (level.equals("2")) {
			if (keyB.equals("culture")) {
				keyB = "culture2";
			}
		} else if (level.equals("3")) {
			if (keyB.equals("culture")) {
				keyB = "culture3";
			}
		}

		try {
			stmt = null;
			rs = null;
			result = null;
			query = "select cultureimg from " + keyB + " where cultureid=" + sel2 + "";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			System.out.println(query);

			if (rs.next()) {
				Blob blob = rs.getBlob(1);

				BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
				int nFileSize = (int) blob.length();
				buf = new byte[nFileSize];
				int nReadSize = in.read(buf, 0, nFileSize);// 이미지파일을 문자열로 저장
				in.close();

				// culimg = new String(buf);

				// System.out.println(culimg);
			}

		} catch (SQLException e) {

		}

		return buf;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	static String selectImgSort(String bdata, String keyB, String level) throws IOException {
		String query = null;
		String query2 = null;
		String query3 = null;
		String culimg = null;
		String culimg2 = null;
		String culimg3 = null;

		String rst1 = "";
		String rst2 = "";
		String rst3 = "";

		String sel1 = "";
		String sel2 = ""; // 보기의 id
		String sel3 = "";
		String sel4 = "";
		String sel5 = "";
		String sel6 = "";

		StringTokenizer stk = new StringTokenizer(bdata, "!"); // 문제 데이터
		rst1 = stk.nextToken();
		rst2 = stk.nextToken();
		rst3 = stk.nextToken();

		StringTokenizer atk1 = new StringTokenizer(rst1, "$"); // 데이터1 id추출
		sel1 = atk1.nextToken();
		sel2 = atk1.nextToken();

		StringTokenizer atk2 = new StringTokenizer(rst2, "$"); // 데이터1 id추출
		sel3 = atk2.nextToken();
		sel4 = atk2.nextToken();

		StringTokenizer atk3 = new StringTokenizer(rst3, "$"); // 데이터1 id추출
		sel5 = atk3.nextToken();
		sel6 = atk3.nextToken();

		if (level.equals("1")) {
			if (keyB.equals("culture")) {
				keyB = "culture1";
			}
		} else if (level.equals("2")) {
			if (keyB.equals("culture")) {
				keyB = "culture2";
			}
		} else if (level.equals("3")) {
			if (keyB.equals("culture")) {
				keyB = "culture3";
			}
		}

		try {
			stmt = null;
			rs = null;
			result = null;
			query = "select cultureimg from " + keyB + " where cultureid=" + sel2 + "";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			if (rs.next()) {
				Blob blob = rs.getBlob(1);

				BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
				int nFileSize = (int) blob.length();
				byte[] buf = new byte[nFileSize]; // 이미지파일을 문자열로 저장
				int nReadSize = in.read(buf, 0, nFileSize);
				in.close();

				culimg = new String(buf);

				// System.out.println(culimg);
			}

		} catch (SQLException e) {

		}
		try {
			stmt = null;
			rs = null;
			result = null;
			query2 = "select cultureimg from " + keyB + " where cultureid=" + sel4 + "";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query2);

			if (rs.next()) {
				Blob blob = rs.getBlob(1);

				BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
				int nFileSize = (int) blob.length();
				byte[] buf = new byte[nFileSize]; // 이미지파일을 문자열로 저장
				int nReadSize = in.read(buf, 0, nFileSize);
				in.close();

				culimg2 = new String(buf);

				// System.out.println(culimg);
			}

		} catch (SQLException e) {

		}
		try {
			stmt = null;
			rs = null;
			result = null;
			query3 = "select cultureimg from " + keyB + " where cultureid=" + sel6 + "";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query3);

			if (rs.next()) {
				Blob blob = rs.getBlob(1);

				BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
				int nFileSize = (int) blob.length();
				byte[] buf = new byte[nFileSize]; // 이미지파일을 바이트배열로 저장
				int nReadSize = in.read(buf, 0, nFileSize);
				in.close();

				culimg3 = new String(buf);

				// System.out.println(culimg);
			}

		} catch (SQLException e) {

		}

		String img = culimg + "sort" + culimg2 + "sort" + culimg3;

		return img;
	}

}
