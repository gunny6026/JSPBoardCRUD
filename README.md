# 서블릿 & JSP

`2021.05 리팩토링 시작`

### `컨텍스트 초기화 매개변수 사용`
- JDBC 드라이버와 DB 연결정보가 전부 동일함
- 각각의 서블릿 마다 초기화 매개변수를 선언하는 것은 번거롭고 낭비다.
- 모든 서블릿들이 공유하는 매개변수 선언

### `ServletContextListner와 객체 공유`
- 서블릿에 요청을 처리하기 위한 dao 인스턴스가 매번 생성됨
- 요청을 처리 할 때 마다 객체를 만들면 많은 가비지가 생성된다
- 여러 서블릿이 사용하는 dao 객체는 서로 공유하자
- 메모리 관리나 실행 속도 측면에서 좋다.
- 서블릿을 통해 객체를 공유하는 것이 아닌 웹 애플리케이션 이벤트를 이용

### `DB 커넥션풀 사용`
- Statement, PreparedStatement 객체에는 롤백 기능이 없다.
- 커넥션 객체를 통해서만 롤백을 수행 할 수 있다.
- 커넥션의 롤백 기능 사용 -> 해당 커넥션의 모든 작업도 롤백
- SQL 작업시 DB 커넥션 생성 -> 실행속도 느려지고 많은 가비지 생성
- DB 커넥션풀 사용 -> 요청 시 별도의 커넥션 객체 사용 & 사용 한 커넥션 객체 버리지 않고 풀에 보관 (가비지 생성 X) 

### `DataSource와 JNDI`
- DataSource는 서버에서 관리한다. -> 데이터베이스나 JDBC
드라이버가 변경되더라도 애플리케이션을 바꿀 필요가 없다.
- DataSource를 사용하면 Connection 과 Statement 객체를
풀링할 수 있고, 분산 트랜잭션을 다룰 수 있다.
- DataSource는 자체적으로 커넥션풀 기능을 구현하기 때문에
웹 애플리케이션 쪽에서 따로 작업할 것이 없어 매우 편리함


### `프런트 컨트롤러의 도입`
- 요청 데이터를 처리하는 코드 & 모델과 뷰를 제어하는 코드가 중복됨
- 프런트 컨트롤러 , 페이지 컨트롤러 두 개의 역할로 나눔
- 페이지 컨트롤러 전환( 서블릿 -> 일반 클래스)
- 일반 클래스는 서블릿에 기술에 종속되지 않음 -> 재사용성이 더 높아짐
- 또한, web.xml 파일에 등록할 필요가 없어 유지보수가 쉬워짐
- 프론트 컨트롤러에서 페이지 컨트롤러 작업 위임(포워드&인클루딩 -> 메서드 호출)

### `페이지 컨트롤러의 진화`

- 페이지 컨트롤러를 서블릿 -> 일반 클래스로 전환함
- BoardListServlet , BoardDeleteServlet.. 등 삭제
- BoardListController , UserAddController..  생성
- 서블릿은 Get 요청과 Post 요청을 구분할 수 있음
- 일반 클래스는 클라이언트 요청에 대해 Get 과 Post를 구분 할 수 없음
- Map 객체에 VO 객체가 있으면 Post 요청 , 없으면 Get 요청으로 간주함

### `DI를 이용한 빈 의존성 관리`
- 의존 객체와의 결합도 증가에 따라 문제가 발생할 수 있다.
- 의존 객체에 변경이 발생하면 의존 객체를 사용하는 모든 코드를 변경해야 한다.
- 대체가 어렵다(MySQL 데이터베이스 -> 오라클 변경시 일부 SQL문을 변경해야함)
- 의존 객체를 외부에서 주입하자.
- Controller에 Dao를 주입 받기 위한 인스턴스 변수, 셋터 메서드 추가

### `프로퍼티를 이용한 객체 관리`
- application-context.properties & ApplicationContext 클래스 생성
- 페이지 컨트롤러 & DAO 추가 시 프로퍼티 파일에 정보 추가
- ContextLoaderListener 소스사 간결해짐
- web.xml 파일에 프로퍼티 파일에 대한 경로 설정

### `애노테이션을 이용한 객체 관리`
- @Component 애노테이션 정의
- DAO & 페이지 컨트롤러에 애노테이션 적용 
- 프로퍼티파일에서 DAO & 페이지 컨트롤러 정보 제거
- ApplicationContext 클래스 변경 -> @Component 애노테이션이 붙은 클래스를 찾는 메소드 추가
- Reflections 라이브러리 , pom.xml에 설정
