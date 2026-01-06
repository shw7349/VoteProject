# VoteProject
투표 시스템 토이 프로젝트 (Kotlin)

**Kotlin + Spring Boot 기반의 투표 / 설문 시스템 토이 프로젝트**

DDD(Domain-Driven Design) 스타일로 **도메인 규칙 중심 설계**와  
**계층별 책임 분리 (application / domain / infrastructure / presentation)** 를 연습하기 위해 제작했습니다.

---

## 프로젝트 목표

- 투표/설문 도메인을 **Aggregate 중심**으로 모델링
- 비즈니스 규칙을 **도메인 계층**에 명확히 위치
- JPA + H2 기반의 간단하지만 **실무에 가까운 구조** 설계
- 도메인 / 유스케이스 / 인프라 / API **테스트 계층 분리**

---

## 핵심 기능 (MVP)

- 투표 생성
- 투표 조회 (목록 / 단건)
- 투표 참여 (**1인 1표 보장**)
- 투표 결과 조회
- 마감된 투표 차단

---

## 기술 스택

- **Language**: Kotlin
- **Framework**: Spring Boot 3.4+
- **Build Tool**: Gradle (Kotlin DSL)
- **Persistence**: Spring Data JPA
- **Database**: H2 (In-memory)
- **Test**: JUnit5, Spring Boot Test, Mockito
- **JDK**: 21

---

## 프로젝트 구조

```text
com.voteproject
 ├─ domain              // 핵심 비즈니스 규칙
 │   └─ poll
 │       ├─ Poll              // Aggregate Root
 │       ├─ PollOption
 │       ├─ Vote
 │       ├─ VoterId           // Value Object
 │       ├─ PollStatus
 │       ├─ PollRepository    // 도메인 포트
 │       └─ exception
 │
 ├─ application         // 유스케이스 계층
 │   └─ poll
 │       ├─ PollCommandService
 │       ├─ PollQueryService
 │       └─ command
 │
 ├─ infrastructure      // DB/JPA 구현
 │   └─ persistence
 │       ├─ SpringDataPollJpa
 │       └─ PollRepositoryAdapter
 │
 └─ presentation        // REST API
     └─ poll
         ├─ PollController
         └─ dto
```
## 도메인 설계 포인트

Poll = Aggregate Root

Vote, PollOption은 외부에서 직접 생성 불가

1인 1표 규칙

도메인 로직 + DB 유니크 제약 이중 방어

상태 기반 설계

OPEN / CLOSED 상태에 따른 행위 제한

Value Object

VoterId는 불변 객체로 동일성 보장

## 도메인 설계 포인트

- **Poll = Aggregate Root**
    - `Vote`, `PollOption`은 외부에서 직접 생성 불가
- **1인 1표 규칙**
    - 도메인 로직 + DB 유니크 제약 **이중 방어**
- **상태 기반 설계**
    - `OPEN / CLOSED` 상태에 따른 행위 제한
- **Value Object**
    - `VoterId`는 불변 객체로 동일성 보장

---

## 실행 방법

### 1) 프로젝트 실행

```
bash

./gradlew bootRun
```
또는 IntelliJ에서

VoteProjectApplication.kt 실행

### 2) H2 콘솔 접속

URL: http://localhost:8080/h2-console

JDBC URL: 
```
jdbc:h2:mem:voteproject
```
Username: sa

Password: (비어 있음)

---

## API 명세 (요약)

### 투표 생성
```
bash

POST /polls
```
```
json

{
  "title": "점심 뭐먹지?",
  "options": ["국밥", "초밥"]
}
```
### 투표 목록 조회
```
bash

GET /polls
```

### 투표 참여
```
bash

POST /polls/{pollId}/votes
```
```
json

{
"voterId": "user1",
"optionId": 1
}
```

### 투표 결과 조회
```
bash

GET /polls/{pollId}/results
```

---
## 테스트 전략
### Domain Test

- **비즈니스 규칙 검증**

- **Spring/JPA 없이 순수 Kotlin 테스트**

### Application Test

- **유스케이스 흐름 검증**

- **In-memory Repository 사용**

### Infrastructure Test

- **JPA 매핑 / Cascade / 저장 검증**

- **@DataJpaTest 사용**

### Presentation Test

- **Controller 슬라이스 테스트**

- **@WebMvcTest**

- **Spring Boot 3.4 기준 @MockitoBean 사용**

