# 한국사 끝판왕
한국사 문제 생성 어플리케이션 -난이도 구분을 통한 단계적 한국사 문제 생성

#KorQuiz폴더 : 모바일 앱에서 퀴즈 유형, 문제 유형, 난이도를 직접 선택한 키워드를 java
소켓통신을 이용하여 서버가 받고 문제 생성 후에 다시 소켓통신을 통해 문제를 
모바일 앱에 보낸다. 
※class
KorQuiz\test2\src\testdb2\ChatServer2.java //서버 실행
KorQuiz\test2\src\testdb2\ServerThread2.java //소켓 연결 ,문제 생성
              
#one폴더     : 풀고 싶은 퀴즈유형을 선택하여 풀어볼 수 있고 결과 확인 가능
※class
ImageLoaderTask.java //문화재 이미지를 url을 이용하여 ftp서버에서 받아옴
MainMenu.java //무작위 / 사용자 선택 퀴즈 유형 선택
RandomOptionMenu.java //랜덤 유형 문제 수, 난이도 선택
RandomMainQuiz.java //랜덤 문제 생성 및 풀이
SelectOptionMenu.java //문제 유형 사용자 선택 메뉴
SelectMainQuiz.java //사용자 선택 문제 생성 및 풀이
ResultList.java //문제풀이 결과
ResultView.java //문제 해설
SplashActivity.java //로딩 화면

#korquizDB.sql  : MySQL로 작성한 한국사 문제자료 데이터베이스 덤프파일
