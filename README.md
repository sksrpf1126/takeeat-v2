<div style="display: flex;justify-content: center"><img src="https://github.com/user-attachments/assets/df5a2568-aa6e-4669-b869-801cde0f0460" width="300px" alt="TakeEat 로고"></div>

<h3>간편한 포장 주문, 빠른 픽업, TakeEat 포장 주문 플랫폼</h3>
<p>사용자가 손쉽게 온라인으로 포장 주문을 하고, 가게는 신속하게 준비하여 기다림 없이 픽업할 수 있는 편리한 주문 서비스 플랫폼입니다.</p>

<br>

## 🛠️ 기술 스택

<p align ="center"><img src="https://github.com/user-attachments/assets/30b5ff6a-ae1e-4977-97c9-f9aa5ae2b044" width = 100%></p> 

<br>

## 📃 ERD

[ERD 링크](https://www.erdcloud.com/d/2s4ga5PPwZtm8bnDM)  

  <p align = "center">
  <img src="https://github.com/user-attachments/assets/365e4e75-2287-4654-a04b-c59e784b3b41" width = 100%>  

<br>

## 💻 담당한 기능 및 페이지 소개

### 담당한 기능

- ERD 및 Figma를 통한 화면 설계
- Redis 기반 이메일 인증을 통한 회원가입  
- Spring Security의 Session 기반 Form Login 및 OAuth2 로그인
- WebSocket + STOMP를 통한 회원 알림 기능 및 가게 주문 접수 처리
- 결제 외부 API를 통한 주문 결제 처리
- 인증, 인가 예외 및 공통 예외 처리
- AWS S3 기반 이미지 업로드 공통 로직 구현

### 담당한 페이지

 <table>
	<tbody>
		<tr>
			<th>사장님 로그인</th>
			<th>사장님 회원가입</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/3639d98e-0667-470e-af0c-c064f083c31f" alt="사장님 로그인"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/92bbf0ab-c0f4-47dd-b31a-671ed152f782" alt="사장님 회원가입"/></td>
		</tr>		
		<tr>
			<td>- 사장님일 경우의 로그인 기능<br>- 사장님 계정만이 가게를 등록할 수 있다.</td>
			<td>- 사장님일 경우의 회원가입 기능</td>
		</tr>
		<tr>
			<th>일반회원 가입 및 로그인</th>
			<th>결제 및 주문상세 내역보기</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/a03f1819-4717-4757-bfdb-000cee50a7b2" alt="일반회원 가입 및 로그인"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/28db64ea-cf84-487d-8df8-ddb1f637231b" alt="결제 및 주문상세 내역보기"/></td>
		</tr>
		<tr>
			<td>- 일반회원 가입 및 로그인 기능</td>
			<td>- 장바구니에 담긴 메뉴 결제<br>- 결제이후 주문상세 내역 보기</td>
		</tr>
		<tr>
			<th>사장님 - 주문 접수 페이지</th>
			<th>주문접수 및 알림</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/e181e4f8-806e-4fba-8c48-3e1e8b8c673a" alt="사장님 - 주문 접수 페이지"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/46e135c0-eaad-4e3c-94fa-85b90bd311a9" alt="주문접수 및 알림"/></td>
	    	<tr>
	    	<tr>
    			<td>- 가게에 접수된 주문을 접수하고 처리하는 페이지</td>
			<td>- 주문을 접수함과 동시에 고객에게 알림 전송</td>
	    	</tr>
	    	<tr>
			<th>주문완료 및 알림</th>
			<th>사장님 주문 조회</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/03f4591b-c617-4a5b-a6d8-9859a9fe1851" alt="주문완료 및 알림"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/7556b731-a214-481b-9199-f36d22f7157c" alt="사장님 주문 조회"/></td>
		</tr>		
		<tr>
			<td>- 가게에서 주문을 완료함과 동시에 고객에게 알림 전송</td>
			<td>- 대기, 거절, 취소, 완료 등 주문상태에 따라 주문을 조회</td>
		</tr>
      </tbody>

</table>

<br>

### 💻 그 외 전체적인 기능 및 페이지

<details>
	<summary>기능 및 페이지 보기</summary>

<table>
	<tbody>
		<tr>
			<th>가게 등록</th>
			<th>메뉴 등록</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/ebe6443f-1265-40df-bb08-5fd30cb176b3" alt="가게 등록"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/8532d981-b224-419f-960c-2f58330abcd9" alt="메뉴 등록"/></td>
		</tr>
		<tr>
			<td>- 가게 등록 이후 메뉴를 설정하는 페이지<br>- 메뉴 카테고리 및 메뉴정보를 등록</td>
			<td>- 사장님일 경우 가게 등록하는 페이지<br>- 가게의 여러 정보를 설정</td>
		</tr>
		<tr>
			<th>메뉴 옵션 저장</th>
			<th>GPS기반 주변 가게 검색</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/dab5d89e-83ba-48d7-9f75-21156b83b5eb" alt="메뉴 옵션 저장"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/8731911c-33e1-4b62-a988-a9408bbafb9a" alt="GPS기반 주변 가게 검색"/></td>
		</tr>
		<tr>
			<td>- 메뉴 등록 이후 메뉴 옵션을 설정하는 페이지<br>메뉴 하나에 들어갈 수 있는 여러 옵션을 설정</td>
			<td>- 디바이스의 GPS를 통한 주변 가게 검색 페이지<br>- 일정 거리에 포함된 가게를 검색</td>
		</tr>
		<tr>
			<th>주소 입력을 통한 가게 검색</th>
			<th>카테고리, 거리, 리뷰를 통한 가게 검색</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/d32ca467-e89f-4bac-a873-085faca2761f" alt="주소 입력을 통한 가게 검색"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/8eec7fc4-2336-4e7c-8d9a-a4cd6a65f3b2" alt="카테고리, 거리, 리뷰를 통한 가게 검색"/></td>
		</tr>
		<tr>
			<td>- 특정 주소를 기준으로 가게를 검색</td>
			<td>- 원하는 카테고리, 거리, 리뷰를 통해 원하는 가게를 검색</td>
		</tr>
		<tr>
			<th>가게 정보 및 리뷰 상세 보기</th>
			<th>장바구니 메뉴 추가 및 수정</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/96682543-90a6-4969-871d-e4211c0860d6" alt="가게 정보 및 리뷰 상세 보기"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/6edc2034-b20b-4c1e-90ec-b2969d1fefd7" alt="장바구니 메뉴 추가 및 수정"/></td>
		</tr>
		<tr>
			<td>- 가게의 상세 정보를 확인<br>- 가게에 작성된 리뷰를 확인</td>
			<td>- 장바구니에 메뉴를 추가<br>- 장바구니에 넣어둔 메뉴를 수정</td>
		</tr>
    		<tr>
			<th>마이페이지 주문 내역</th>
			<th>고객 - 리뷰 작성</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/e0f545b1-5620-4d6b-b8ef-cedf947bfb1a" alt="마이페이지 주문 내역"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/458563ac-1659-4576-b896-f023a74982c6" alt="고객 - 리뷰 작성"/></td>
		</tr>		
		<tr>
			<td>- 가게에 주문한 내역을 모아서 볼 수 있는 페이지<br>한 가게당 하나의 리뷰만 작성 가능</td>
			<td>- 주문한 가게에 리뷰를 작성하는 페이지</td>
		</tr>	
    <tr>
			<th>고객 - 리뷰 관리</th>
			<th>사장님 - 리뷰 작성</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/bb34fb40-b209-4913-b77a-543302e77fc2" alt="고객 - 리뷰 관리"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/61eea9e9-2042-4b03-b9f4-cc386c54371c" alt="사장님 - 리뷰 작성"/></td>
		</tr>		
		<tr>
			<td>- 자신이 작성한 리뷰를 삭제 또는 수정할 수 있는 페이지</td>
			<td>- 사장님이 가게에 등록되어 있는 리뷰에 대해 답글 형식으로 작성</td>
		</tr>	
    <tr>
			<th>사장님 - 리뷰 답글 관리</th>
			<th>가게 상태 변경</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/9effa372-a31c-4b7a-9cb4-ba0b735477c5" alt="사장님 - 리뷰 답글 관리"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/856595b4-b584-4ed6-a44f-0b5def5cc5a0" alt="가게 상태 변경"/></td>
		</tr>		
		<tr>
			<td>- 답글로 작성한 리뷰에 대하여 수정 또는 삭제를 할 수 있는 페이지</td>
			<td>- 가게의 사정에 따라 오픈 여부 결정 가능</td>
		</tr>	
    <tr>
			<th>준비중인 가게 주문 불가</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/44913e37-6f09-430b-9069-f30a97057dc9" alt="준비중인 가게 주문 불가"/></td>
		</tr>		
		<tr>
			<td>- 오픈이 되지 않은 가게에 대해서는 주문 불가</td>
		</tr>	
    </tbody>

</table>

</details>

<br>

## 부딪힌 기술적 이슈 해결 과정

### 회원가입 이메일 발송 성능 개선  
[[@Async 관련 포스팅]](https://github.com/sksrpf1126/study/blob/main/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/%40Async%20%EB%8F%99%EC%9E%91%EC%9B%90%EB%A6%AC.md)  
[[Redis 이메일 인증 관련 포스팅]](https://github.com/sksrpf1126/study/blob/main/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/RDB%EC%99%80%20Redis%20%EC%9D%B4%EB%A9%94%EC%9D%BC%20%EC%9D%B8%EC%A6%9D.md)  

이메일 인증 과정에서 발생하는 응답 속도 문제를 개선하기 위해 @Async를 적용하여 비동기 방식으로 이메일을 발송하게끔 처리했습니다. 이로 인해, 이메일 인증 코드 발송 응답 속도가 3.85초에서 0.61초로 크게 단축되었습니다.  
또한 Redis를 도입하여 이메일 인증 코드의 저장과 조회를 캐시화함으로써, 데이터베이스 부하를 줄였고, 2,000건의 요청 처리 시 저장 속도는 5.8배, 조회 성능은 9.5배 개선되었습니다. 많은 트래픽의 상황을 가정하여 성능 최적화와 빠른 응답을 가능하게 했습니다.

### SSR 방식에서 API 예외와 페이지 요청 예외 처리 [[관련 포스팅]](https://github.com/sksrpf1126/study/blob/main/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/SSR%EB%B0%A9%EC%8B%9D%EC%97%90%EC%84%9C%EC%9D%98%20%EA%B3%B5%ED%86%B5%20%EC%98%88%EC%99%B8%EC%B2%98%EB%A6%AC.md)  
SSR(서버 사이드 렌더링) 방식에서 API 호출 시 발생하는 예외와 페이지 요청 시 발생하는 예외를 어떻게 처리할지에 대해 고민을 했습니다. API 요청은 주로 JSON 응답을 반환하는 반면, 페이지 요청은 HTML 페이지를 렌더링하는 방식이므로, 예외를 반환해주는 것 또한 서로 다른 방식으로 응답을 해주어야 했습니다.  
이를 해결하기 위해 API 예외 처리는 JSON으로 응답하고, 페이지 요청 예외는 에러 페이지를 렌더링하는 두 가지 방식으로 공통 예외 처리 클래스를 구현하여, 각 상황에 맞는 적절한 에러 처리 방식을 적용했습니다.

### WebSocket + STOMP에서 인증된 사용자 정보 처리 [[관련 포스팅]](https://github.com/sksrpf1126/study/blob/main/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/Websocket%EC%97%90%EC%84%9C%20%EC%9D%B8%EC%A6%9D%20%EA%B0%9D%EC%B2%B4%20%EC%B2%98%EB%A6%AC.md)  
WebSocket 통신에서는 HTTP 통신과는 다르게 처음 연결 이후 끊기기 전까지 양방향 통신이 이루어집니다.  
이러한 실시간 통신에서도 인증된 사용자 정보를 다루는 방법을 고민했습니다. 이를 해결하기 위해 Principal 객체를 활용하여 WebSocket 연결 시 인증 정보를 유지하고, 이를 통해 사용자의 요청을 식별할 수 있도록 하였습니다.  
또한, Argument Resolver를 사용하여 컨트롤러 메서드에서 인증된 사용자 정보를 간편하게 접근할 수 있도록 하여, 반복되는 형변환 과정을 생략할 수 있도록 하였습니다.  

### 동일 아이디의 경우 단 하나의 세션만 연결하도록 처리 [[관련 포스팅]](https://github.com/sksrpf1126/study/blob/main/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/%EC%A4%91%EB%B3%B5%20%EB%A1%9C%EA%B7%B8%EC%9D%B8%20%EB%B0%A9%EC%A7%80.md)  
동일한 사용자 아이디로 여러 디바이스나 브라우저에서 동시 로그인 시, 보안과 리소스 관리 측면에서 문제가 발생할 수 있습니다.  
이를 해결하기 위해 Spring Security에서 제공하는 동시 세션 제어 기능을 사용하려 했으나, 정상적으로 동작하지 않는 문제를 발견했습니다.  
원인 분석을 위해 내부 코드를 확인한 결과, 커스텀 인증 객체에서 `equals와 hashcode 메서드`가 오버라이딩되지 않아 동일한 아이디임에도 불구하고 같은 세션으로 인식되지 않는 문제를 확인했습니다. 이를 수정하여, 하나의 아이디로 단일 세션만 유지되도록 문제를 해결하였습니다.

<br>

## 🌳 폴더 구조

```
src─|
    java
    ├─common : Exception관련 파일이나 공통 클레스(aop, validation, converter, image upload) 파일로 구성
    ├─config : 각종 설정파일 구성
    ├─controller : 컨트롤러 파일 보관
    ├─domain
    │  ├─[domain name] : 각 도메인마다 엔티티파일과 연관 enum 파일로 구성
    ├─dto
    │  ├─[domain name] : 각 도메인의 dto 파일 보관
    ├─repository : 레파지토리 파일 보관
    ├─security : spring security 파일 구성
    ├─service : 서비스 파일 보관
    
    resources
    ├─ static
    │     ├─ css (css 파일 보관)
    │     ├─ image (image 파일 보관)
    │     ├─ js (js 파일 보관)
    |
    |
    ├─ template : 화면에 렌더링되는 HTML 파일 보관

```

<br>

## 🧑‍💻 만든 사람들

이름순

<table>
	<tbody>
		<tr>
			<th><img width="150px" src="https://github.com/GYUNGAEEEE.png" alt="김경애"/></th>
			<th><img width="150px" src="https://github.com/Jurioh0603.png" alt="오주리"/></th>
			<th><img width="150px" src="https://github.com/sksrpf1126.png" alt="임성현"/></th>
		</tr>		
		<tr>
			<th><a href="https://github.com/GYUNGAEEEE" target="_blank">김경애</a></th>
			<th><a href="https://github.com/Jurioh0603" target="_blank">오주리</a></th>
			<th><a href="https://github.com/sksrpf1126" target="_blank">임성현</a></th>
		</tr>	
	</tbody>
</table>
