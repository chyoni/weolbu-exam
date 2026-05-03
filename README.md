# 월급쟁이부자들 과제 — 강의 등록 / 수강 신청 API

회원 가입 후 강사가 강의를 등록하고 수강생이 수강 신청하는 Spring Boot API 서버.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| 언어 / 런타임 | Java 21 |
| 빌드 | Gradle 9.4.1 (Kotlin DSL) |
| 프레임워크 | Spring Boot 4.0.6 (Web, Data JPA, Validation) |
| 데이터베이스 | H2 in-memory (MySQL 호환 모드) |
| ORM / 쿼리 | Spring Data JPA, Querydsl 6.10.1 |
| API 문서 | springdoc-openapi 3.0.3 (Swagger UI) |
| 테스트 | JUnit 5, Mockito, AssertJ |
| 코드 포맷 | Spotless (Google Java Format AOSP, 4-space) |

---

## 빠른 시작

```bash
# 1. 빌드
./gradlew build

# 2. 실행 (--spring.profiles.active=local 필수)
./gradlew bootRun --args='--spring.profiles.active=local'
```

> `--spring.profiles.active=local`을 빠뜨리면 datasource가 설정되지 않아 실행되지 않습니다.

브라우저에서 Swagger UI 열기:
```
http://localhost:8080/swagger-ui.html
```

Windows에서는 `./gradlew` 대신 `gradlew.bat`을 사용합니다.

---

## 문서

| 문서 | 설명 |
|------|------|
| [실행 절차](docs/run-guide.md) | JDK 설치부터 API 테스트까지 단계별 안내 |
| [API 스펙](docs/api-spec.md) | 전체 엔드포인트 요청/응답 명세 |
| [도메인 모델 / 요구사항](docs/domain-model.md) | 도메인 설계 및 구현 요구사항 |
| [도메인 사전](docs/dictionary.md) | 용어 정의 |

---

## 주요 기능

- 회원 가입 / 조회 / 정보 변경 (핸드폰 번호, 비밀번호, 역할)
- 강사 회원의 강의 등록
- 강의 목록 조회 (최근 등록순 / 신청자 많은 순 / 신청률 높은 순, 페이징)
- 수강 신청 (다중 강의 동시 신청, All-or-Nothing 트랜잭션, 비관적 락 기반 선착순 처리)

---

## 아키텍처

헥사고날 아키텍처 + 클린 아키텍처. DDD 핵심 철학 적용.

```
src/main/java/cwchoiit/weolbuexam/
├── domain/                  도메인 엔티티, Value Object, 페이로드 (프레임워크 독립)
├── application/
│   ├── provided/            인바운드 유스케이스 포트 (인터페이스)
│   ├── required/            아웃바운드 리포지토리 포트 (인터페이스)
│   ├── member/              회원 유스케이스 구현
│   ├── course/              강의 유스케이스 구현
│   └── enrollment/          수강신청 유스케이스 구현
├── adapter/
│   ├── in/web/              REST 컨트롤러, Request/Response DTO, Swagger 스펙 인터페이스
│   └── out/security/        비밀번호 인코더 구현체 (BCrypt)
└── infrastructure/          Querydsl 설정
```

---

## 테스트 / 코드 포맷

```bash
# 전체 테스트 실행
./gradlew test

# 코드 포맷 자동 정리
./gradlew spotlessApply
```
