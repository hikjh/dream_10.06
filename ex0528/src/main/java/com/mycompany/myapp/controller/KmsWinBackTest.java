package com.mycompany.myapp.controller;

import javax.xml.bind.DatatypeConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class KmsWinBackTest {
	
	// KMS 설정값
	private static String KMS_AUTH_Server = "http://14.32.171.143:18280/";
	private static String KMS_Server = "http://14.32.171.143:18380/";
	private static String AUTH_TOKEN_URI = "authentication/token";
	private static String KMS_PUBLICKEY_URI = "keys/keypair/publickey";
	private static String KMS_PRIVATEKEY_URI = "keys/keypair/privatekey";
	private static String KMS_CERT_URI = "cert";
	private static String KMS_AUTH_ID = "hang_mig_test_user";
	private static String KMS_AUTH_PW = "1q2w3e$R%T^Y";
	private static String KMS_HASH_ALGORITHM = "SHA-256";
	private static String KMS_AUTH_STATE = "10";
	private static int KMS_SERVICE_CODE = 26;
	private static String KMS_KEY_LENGTH = "2048";
	private static String CHARSET = "utf-8";
	private static int SSL_PORT = 443;
	private static Boolean IS_SSL = false;
		
	//KMI설정값
	private static String SERVER_CERT = "/ecdata/deployment/kms/ROOT.war/serverCert/signCert.der";
	private static String SERVER_KEY = "/ecdata/deployment/kms/ROOT.war/serverCert/signPri.key";
	private static String SERVER_PASSWORD = "1q2w3e4r";
	private static Boolean PASSWORD_DEBUG_MODE = true;
	private static String KMI_Server = "http://10.47.12.108:8080/jsp/";
	private static String KMI_RECOVERY_URI = "KeyRecoveryService.jsp";
	private static int KMI_SSL_PORT = 8443;
	
	//KMI DB 설정값
	// local
	private static String url = "jdbc:tibero:thin:@127.0.0.1:8629:tibero";
	private static String id = "US_ECID_KMS";
	private static String pw = "US_ECID_KMS";
	// 라이브러리 추가
	private static String driver = "com.tmax.tibero.jdbc.TbDriver";
		
	
	private static String logFilePath = "/app/jboss/eap/standalone/logs/kmsWinBack.log";
	
	
	public static void main(String[] args) throws Exception {
		
		run();
		
	}
	
	public static void run() throws Exception {
		//0-1. 가져올 키 날짜 입력받기
		Scanner scan = new Scanner(System.in);
		//System.out.println("시작 날짜를 입력");
		String startDate = scan.next();
		//System.out.println("종료 날짜를 입력");
		String endDate = scan.next();
		
		//System.out.println(startDate + " ~ " + endDate);
		
		/*
		//0=2. 로그파일 생성
		SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
		Date date = new Date();
		String currentDate = dateFormat.format(date);
		File file = new File(logFilePath+"."+currentDate);
		PrintStream psFileout = new PrintStream(new FileOutputStream(file,true));
		PrintStream psConsoleout = System.out;
		
		//여기서 부터 파일로 로그 출력.
		System.setOut(psFileout);
		System.setErr(psFileout);
		System.out.println("(인증서 조회) Date :  "+startDate + " ~ " + endDate);
		*/
		//1. (KMI) 인증서와 개인키 개수를 조회한다. -> count 수  where 
		int count = getKmiCount(startDate, endDate);
		System.out.println("(인증서 조회) Count : " + count);
		
		for (int i = 1 ; i < count+1 ; i++ ) {
			//2. (KMI) 사용자 DN을 조회한다.
			String userDn = getUserDn (i, startDate, endDate);
			
			System.out.println("(인덱스) index: "+i+" / (사용자 DN)userDn: "+userDn);
			System.out.println("");
		}
		
	}
	
	/**
	 *  KMI에서 count 조회 
	 * @return 개인키 개수 
	 */
	private static int getKmiCount(String startDate , String endDate) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			/*
			System.out.println("");
			System.out.println("####### KMI GET COUNT START #######");
			System.out.println("####### startDate: "+startDate);
			System.out.println("####### endDate: "+endDate);
			*/
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pw);

			// kmi_cert 
			String sql = "SELECT COUNT(*) FROM kmi_cert WHERE TO_CHAR(insert_date, 'yyyy-mm-dd') BETWEEN ? AND ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
				System.out.println("####### result : "+result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		//System.out.println("####### KMI GET COUNT END #######");
		//System.out.println("");
		return result;
	}
	
	/**
	 *  KMI에서 UserDn 조회
	 * @param index 
	 * @return UserDn
	 */
	/**
	 * 케이사인 KMI에서 UserDn 조회
	 * @param index 
	 * @return UserDn
	 */
	private static String getUserDn(int index, String startDate , String endDate) {
		// Get User DN
		String rownum = "";
		String userDn = "";
		String insertDate = "";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			/*
			System.out.println("");
			System.out.println("####### KMI GET USERDN START #######");
			System.out.println("####### index: "+index);
			System.out.println("####### startDate: "+startDate);
			System.out.println("####### endDate: "+endDate);
			*/
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pw);
			
			// kmi_cert 
			String sql = "SELECT	"
					+ "b.r, user_dn, insert_date FROM	( "
					+ "SELECT rownum r, user_dn, insert_date FROM	( "
					+ "SELECT user_dn, insert_date FROM kmi_cert ) a "
					+ "WHERE	TO_CHAR(insert_date,'yyyy-mm-dd') BETWEEN ? AND ? ) b "
					+ "WHERE	r = ?";
			//AND TO_CHAR(insert_date, 'yyyy-mm-dd') BETWEEN #{startDate} AND #{endDate}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			pstmt.setInt(3, index);
			
			rs = pstmt.executeQuery();

			while(rs.next()) {
				
				rownum = rs.getString("r");
				userDn = rs.getString("user_dn");
				insertDate = rs.getString("insert_date"); 
				/*
				System.out.println("####### rownum: "+rownum);
				System.out.println("####### userDn: "+userDn);
				System.out.println("####### insertDate: "+insertDate);
				*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {rs.close();} catch (Exception rse) {}
			try {pstmt.close();} catch (Exception pstmte) {}
			try {conn.close();} catch (Exception conne) {}
		}
		//System.out.println("####### KMI GET USERDN END #######");
		return userDn;
	}
	
/*
	- java 파일 컴파일 하기
	 [root@dev test] javac . HelloWorld.java

	- 컴파일 된 class를 실행시키기
	 [root@dev test] java HelloWorld.class
	 
	 - jar 파일 생성 example (Hello.jar 생성)
	 [root@dev test] jar -cvmf manifest.txt Hello.jar *.class
	=============================================================
	<<외부 jar 파일을 classpath에 추가한 후 컴파일하고 실행하기>>
	
	* javac -d . -cp jackson-core-asl-1.8.5.jar:jackson-mapper-asl-1.8.5.jar HelloWorldJson.java	
	컴파일 단계 시 JSON 라이브러리를 필요로 하기 때문에 클래스 패스에 jar를 추가해 줘야 한다.
	
	* java -cp jackson-core-asl-1.8.5.jar:jackson-mapper-asl-1.8.5.jar:. packagetest.json.HelloWorldJson
	컴파일 단계와 동일하게 JSON 라이브러리를 클래스 패스에 추가
	
	* java -cp ./*:. packagetest.json.HelloWorldJson
	외부 jar 파일을 classpath에 많이 추가해야 하는 경우가 발생하면 * 를 통해서 간단하게 해결할 수 있다.
	=============================================================
	<<Linux에서 Java jar 파일 백그라운드 실행 및 로그남기기>>
	
	Linux에서 jar파일을 백그라운드(&)로 실행 시킬 때 아래 명령어를 사용한다.
	Java -jar  filename.jar &
	
	하지만 프로세스를 죽이게 되면 서버도 죽고 로그도 볼수 없기 때문에 log 파일을 만들어 사용하고 싶었는데 아래 명령어로 로그파일을 손쉽게 만들 수 있었다.
	java -jar filename.jar 2>&1 > filename.log &
	filename.log에 System.out.print , Error 등과 같이 console에 출력되는 로그가 filename.log에 저장된다. 


		** 2>&1의 의미
		표준출력이 전달되는 곳으로 표준에러를 전달하라 라는 의미로
		표준출력과 표준에러를 로그파일에 남기기 위해서 사용하는 것 같다.  

		0 : 표준입력
		1 : 표준출력
		2 : 표준에러

*/
}
