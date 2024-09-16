<div style="display: flex;justify-content: center"><img src="https://github.com/user-attachments/assets/df5a2568-aa6e-4669-b869-801cde0f0460" width="300px" alt="TakeEat 로고"></div>

<h3>간편한 포장 주문, 빠른 픽업, TakeEat 포장 주문 플랫폼</h3>
<p>사용자가 손쉽게 온라인으로 포장 주문을 하고, 가게는 신속하게 준비하여 기다림 없이 픽업할 수 있는 편리한 주문 서비스 플랫폼입니다.</p>

<br>

## 🛠️ 기술 스택

<p align ="center"><img src="https://github.com/user-attachments/assets/ec94dea6-fba4-4fc2-b18f-63f0f7216f7c" width = 100%></p> 

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

## 💻 주요 기능 및 페이지 소개

<table>
	<tbody>
		<tr>
			<th>사장님 로그인</th>
			<th>사장님 회원가입</th>
		</tr>
		<tr>
			<td><img width="650px" src="https://github.com/user-attachments/assets/3639d98e-0667-470e-af0c-c064f083c31f" alt="랜딩 페이지"/></td>
			<td><img width="650px" src="https://github.com/user-attachments/assets/92bbf0ab-c0f4-47dd-b31a-671ed152f782" alt="다크모드 기능"/></td>
		</tr>		
		<tr>
			<td>- 모든 페이지에서 반응형 UI 지원<br>- 해당 서비스의 특징 및 사용법 안내<br>- 실시간 인기게시글 확인</td>
			<td>- 모든 페이지에서 다크모드 지원</td>
		</tr>	
		<tr>
			<th>일반회원 가입 및 로그인</th>
			<th>가게 등록</th>
		</tr>
		<tr>
		<td><img width="400px" src="public/readme-images/login.gif" alt="로그인 기능"/></td>
			<td><img width="400px" src="public/readme-images/register.gif" alt="회원가입 기능"/></td>
		</tr>
		<tr>
			<td>- 로그인 반응형 모달<br>- 소셜 로그인 기능</td>
			<td>- 회원가입 기능<br>-  기능</td>
		</tr>
		<tr>
			<th>메뉴 등록</th>
			<th>메뉴 옵션 저장</th>
		</tr>
		<tr>
			<td><img width="400px" src="public/readme-images/mypage.gif" alt="마이 페이지"/></td>
			<td><img width="400px" src="public/readme-images/mypage-edit.gif" alt="프로필 변경하기"/></td>
		</tr>
		<tr>
			<td>- 개인 프로필 관리<br>- 관심 게시글 및 작성 게시물 모아보기<br>- 지원 현황 및 상태 확인<br>- 계정 탈퇴 기능</td>
			<td>- 아바타 이미지 변경 기능<br>- 본인을 나타내는 해시태그<br>- 포지션, 경력 등의 정보 추가 및 수정 기능</td>
		</tr>
		<tr>
			<th>GPS기반 주변 가게 검색</th>
			<th>주소 입력을 통한 가게 검색</th>
		</tr>
		<tr>
			<td><img width="400px" src="public/readme-images/post-project.gif" alt="프로젝트 자랑 게시글 작성"/></td>
			<td><img width="400px" src="public/readme-images/detail-recruit.gif" alt="모집 게시글 상세 페이지"/></td>
		</tr>
		<tr>
			<td>- 잘못된 데이터 입력을 에러 메시지로 경고<br>- 프로젝트 대표이미지 등록 가능</td>
			<td>- 모집중인 포지션에 지원 가능<br>- 게시글 작성자인 경우 수정/삭제 가능<br>- 댓글 등록 및 수정, 삭제 가능</td>
		</tr>
		<tr>
			<th>카테고리, 거리, 리뷰를 통한 가게 검색</th>
			<th>가게 정보 및 리뷰 상세 보기</th>
		</tr>
		<tr>
			<td><img width="400px" src="public/readme-images/recruits-page.gif" alt="팀원 모집 게시판"/></td>
			<td><img width="400px" src="public/readme-images/post-recruit.gif" alt="팀원 모집 게시글 작성"/></td>
		</tr>
		<tr>
			<td>- 게시글을 무한 스크롤 형태로 빠르게 로딩<br>- 원하는 기술 태그 또는 내용의 게시글 검색</td>
			<td>- 팀원 모집 게시글 작성 기능<br>- 모집할 포지션 설정 및 기술 태그 검색 후 추가</td>
		</tr>
				<tr>
			<th>장바구니 메뉴 추가 및 수정</th>
			<th>결제 및 주문상세 내역보기</th>
		</tr>
		<tr>
			<td><img width="400px" src="public/readme-images/alarm.gif" alt="알람 이벤트"/></td>
			<td><img width="400px" src="public/readme-images/like-board.gif" alt="게시물 관심목록 추가"/></td>
		</tr>
		<tr>
			<td>- 댓글 발생 알람 기능<br>- 모집 게시판 참여자 발생 알람 기능</td>
			<td>- 마이페이지 관심 목록 추가 기능</td>
		</tr>
		<tr>
			<th>사장님 - 주문 접수 페이지</th>
			<th>주문접수 및 알림</th>
		</tr>
		<tr>
			<td><img width="400px" src="public/readme-images/teamManage.gif" alt="팀관리 모달"/></td>
			<td><img width="400px" src="public/readme-images/skeleton.gif" alt="스켈레톤 기능"/></td>
    	<tr>
    	<tr>
    		<td>- 지원자 관리 및 팀원 관리 반응형 모달</td>
			<td>- 레이아웃 시프트 방지를 위한 스켈레톤 컴포넌트</td>
    	</tr>
    <tr>
			<th>주문완료 및 알림</th>
			<th>사장님 주문 조회</th>
		</tr>
		<tr>
			<td><img width="400px" src="public/readme-images/landing-page.gif" alt="랜딩 페이지"/></td>
			<td><img width="400px" src="public/readme-images/darkmode.gif" alt="다크모드 기능"/></td>
		</tr>		
		<tr>
			<td>- 모든 페이지에서 반응형 UI 지원<br>- 해당 서비스의 특징 및 사용법 안내<br>- 실시간 인기게시글 확인</td>
			<td>- 모든 페이지에서 다크모드 지원</td>
		</tr>
    <tr>
			<th>마이페이지 주문 내역</th>
			<th>고객 - 리뷰 작성</th>
		</tr>
		<tr>
			<td><img width="400px" src="public/readme-images/landing-page.gif" alt="랜딩 페이지"/></td>
			<td><img width="400px" src="public/readme-images/darkmode.gif" alt="다크모드 기능"/></td>
		</tr>		
		<tr>
			<td>- 모든 페이지에서 반응형 UI 지원<br>- 해당 서비스의 특징 및 사용법 안내<br>- 실시간 인기게시글 확인</td>
			<td>- 모든 페이지에서 다크모드 지원</td>
		</tr>	
    <tr>
			<th>고객 - 리뷰 관리</th>
			<th>사장님 - 리뷰 작성</th>
		</tr>
		<tr>
			<td><img width="400px" src="public/readme-images/landing-page.gif" alt="랜딩 페이지"/></td>
			<td><img width="400px" src="public/readme-images/darkmode.gif" alt="다크모드 기능"/></td>
		</tr>		
		<tr>
			<td>- 모든 페이지에서 반응형 UI 지원<br>- 해당 서비스의 특징 및 사용법 안내<br>- 실시간 인기게시글 확인</td>
			<td>- 모든 페이지에서 다크모드 지원</td>
		</tr>	
    <tr>
			<th>사장님 - 리뷰 답글 관리</th>
			<th>가게 상태 변경</th>
		</tr>
		<tr>
			<td><img width="400px" src="public/readme-images/landing-page.gif" alt="랜딩 페이지"/></td>
			<td><img width="400px" src="public/readme-images/darkmode.gif" alt="다크모드 기능"/></td>
		</tr>		
		<tr>
			<td>- 모든 페이지에서 반응형 UI 지원<br>- 해당 서비스의 특징 및 사용법 안내<br>- 실시간 인기게시글 확인</td>
			<td>- 모든 페이지에서 다크모드 지원</td>
		</tr>	
    <tr>
			<th>준비중인 가게 주문 불가</th>
		</tr>
		<tr>
			<td><img width="400px" src="public/readme-images/landing-page.gif" alt="랜딩 페이지"/></td>
		</tr>		
		<tr>
			<td>- 모든 페이지에서 반응형 UI 지원<br>- 해당 서비스의 특징 및 사용법 안내<br>- 실시간 인기게시글 확인</td>
		</tr>	
    </tbody>

</table>

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
