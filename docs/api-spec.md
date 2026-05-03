# API 스펙 문서

## 1. 개요

| 항목 | 값 |
|------|-----|
| Base URL | `http://localhost:8080` |
| Content-Type | `application/json; charset=UTF-8` |
| 인증 | 없음 (과제 범위 외) |
| 시간 형식 | ISO-8601 LocalDateTime (예: `2026-05-03T10:00:00`) |

Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 2. 공통 응답

### 2.1 `ApiResponse<T>`

모든 응답은 `ApiResponse<T>`로 감싸진다. `null`인 필드는 JSON에서 생략된다 (`@JsonInclude(NON_NULL)`).

| 필드 | 타입 | 설명 |
|------|------|------|
| `data` | `T` (nullable) | 성공 시 응답 데이터. 반환할 데이터가 없으면 생략됨 |
| `success` | `boolean` | 항상 포함. 성공이면 `true`, 실패면 `false` |
| `errorMessage` | `String` (nullable) | 실패 시 오류 메시지. 성공 시 생략됨 |

**성공 (데이터 있음)**
```json
{
  "data": { ... },
  "success": true
}
```

**성공 (데이터 없음)**
```json
{
  "success": true
}
```

**실패**
```json
{
  "success": false,
  "errorMessage": "오류 메시지"
}
```

### 2.2 `PagedResponse<T>`

강의 목록 조회(`GET /api/v1/courses`)의 `data` 필드 타입.

| 필드 | 타입 | 설명 |
|------|------|------|
| `content` | `List<T>` | 현재 페이지의 데이터 목록 |
| `page` | `int` | 현재 페이지 번호 (0부터 시작) |
| `size` | `int` | 페이지당 항목 수 |
| `totalElements` | `long` | 전체 항목 수 |
| `totalPages` | `int` | 전체 페이지 수 |

**예시**
```json
{
  "data": {
    "content": [ { ... }, { ... } ],
    "page": 0,
    "size": 20,
    "totalElements": 42,
    "totalPages": 3
  },
  "success": true
}
```

### 2.3 에러 응답 규약

입력 검증은 서비스 계층에서 수행되며, 도메인 규칙 위반은 도메인 메서드에서 발생한다. 예외 종류에 따라 아래와 같이 HTTP 상태 코드가 결정된다.

| 예외 | HTTP 상태 | 주요 발생 상황 |
|------|-----------|----------------|
| `IllegalArgumentException` | 400 Bad Request | 잘못된 이메일 형식, 잘못된 핸드폰 번호 형식 |
| `IllegalStateException` | 400 Bad Request | 정원 초과, 중복 수강 신청, 비밀번호 불일치 |
| `ConstraintViolationException` | 400 Bad Request | Bean Validation 위반 (이름 길이, 비밀번호 길이 등) |
| `NoSuchElementException` | 404 Not Found | 존재하지 않는 회원 ID, 강의 ID |

---

## 3. 도메인 Enum

### 3.1 `MemberRole`

| 값 | 설명 |
|----|------|
| `INSTRUCTOR` | 강사 — 강의를 등록할 수 있음 |
| `STUDENT` | 수강생 — 강의를 수강 신청할 수 있음. 강사도 수강 신청 가능 |

### 3.2 `CourseSortType`

강의 목록 조회 시 `sort` 쿼리 파라미터에 사용한다.

| 값 | 설명 |
|----|------|
| `CREATED_AT_DESC` | 최근 등록순 (기본값) |
| `ENROLL_COUNT_DESC` | 신청자 많은 순 |
| `ENROLL_RATE_DESC` | 신청률 높은 순 (신청자 수 ÷ 최대 수강 인원) |

---

## 4. 엔드포인트 명세

---

### 4.1 회원 가입

| 항목 | 값 |
|------|-----|
| Method | `POST` |
| Path | `/api/v1/members` |
| 응답 status | `202 Accepted` |

#### 요청 본문

| 필드 | 타입 | 필수 | 설명 | 검증 |
|------|------|------|------|------|
| `name` | `String` | ✔ | 이름 | 최소 3자, 최대 50자 |
| `email` | `String` | ✔ | 이메일 | 이메일 형식 (`xxx@xxx.xx`) |
| `phoneNumber` | `String` | ✔ | 핸드폰 번호 | 한국 핸드폰 번호 형식 (예: `01012345678`, `010-1234-5678`) |
| `rawPassword` | `String` | ✔ | 비밀번호 | 최소 6자, 최대 10자. 영문 소문자·대문자·숫자 중 최소 2종 조합 |
| `role` | `MemberRole` | ✔ | 회원 유형 | `INSTRUCTOR` 또는 `STUDENT` |

**요청 예시**
```json
{
    "name": "홍길동",
    "email": "noreply@example.com",
    "phoneNumber": "01011112222",
    "rawPassword": "Secret123",
    "role": "STUDENT"
}
```

#### 응답 본문 (`data` 필드)

| 필드 | 타입 | 설명 |
|------|------|------|
| `memberId` | `Long` | 생성된 회원 ID |
| `name` | `String` | 이름 |
| `email` | `String` | 이메일 |
| `phoneNumber` | `String` | 핸드폰 번호 |
| `role` | `MemberRole` | 회원 유형 |

**성공 예시 (202)**
```json
{
  "data": {
    "memberId": 1,
    "name": "홍길동",
    "email": "noreply@example.com",
    "phoneNumber": "01011112222",
    "role": "STUDENT"
  },
  "success": true
}
```

#### 가능한 에러

| status | errorMessage 예시 | 원인 |
|--------|-------------------|------|
| 400 | `"유효하지 않은 이메일 형식입니다"` | 이메일 형식 불일치 |
| 400 | `"유효하지 않은 핸드폰 번호입니다"` | 핸드폰 번호 형식 불일치 |
| 400 | Bean Validation 메시지 | 이름 길이, 비밀번호 길이 등 조건 미충족 |

---

### 4.2 회원 조회

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| Path | `/api/v1/members/{memberId}` |
| 응답 status | `200 OK` |

#### 경로 파라미터

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `memberId` | `Long` | 조회할 회원 ID |

#### 응답 본문 (`data` 필드)

| 필드 | 타입 | 설명 |
|------|------|------|
| `memberId` | `Long` | 회원 ID |
| `name` | `String` | 이름 |
| `email` | `String` | 이메일 |
| `phoneNumber` | `String` | 핸드폰 번호 |
| `role` | `MemberRole` | 회원 유형 |
| `createdAt` | `LocalDateTime` | 가입일시 |
| `updatedAt` | `LocalDateTime` | 정보 수정일시 |

**성공 예시 (200)**
```json
{
  "data": {
    "memberId": 1,
    "name": "홍길동",
    "email": "noreply@example.com",
    "phoneNumber": "01011112222",
    "role": "STUDENT",
    "createdAt": "2026-05-03T10:00:00",
    "updatedAt": "2026-05-03T10:00:00"
  },
  "success": true
}
```

#### 가능한 에러

| status | errorMessage 예시 | 원인 |
|--------|-------------------|------|
| 404 | `"회원을 찾을 수 없습니다"` | 존재하지 않는 memberId |

---

### 4.3 핸드폰 번호 변경

| 항목 | 값 |
|------|-----|
| Method | `PUT` |
| Path | `/api/v1/members/{memberId}/phone` |
| 응답 status | `200 OK` |

#### 경로 파라미터

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `memberId` | `Long` | 변경할 회원 ID |

#### 쿼리 파라미터

> 이 엔드포인트는 요청 **본문이 없습니다**. `phoneNumber`는 URL 쿼리 파라미터로 전달합니다.

| 파라미터 | 타입 | 필수 | 설명 | 예시 |
|----------|------|------|------|------|
| `phoneNumber` | `String` | ✔ | 변경할 핸드폰 번호 | `01011112222` |

**요청 예시**
```
PUT /api/v1/members/1/phone?phoneNumber=01099998888
```

#### 응답 본문

데이터 없이 성공 여부만 반환한다.

```json
{
  "success": true
}
```

#### 가능한 에러

| status | errorMessage 예시 | 원인 |
|--------|-------------------|------|
| 400 | `"유효하지 않은 핸드폰 번호입니다"` | 핸드폰 번호 형식 불일치 |
| 404 | `"회원을 찾을 수 없습니다"` | 존재하지 않는 memberId |

---

### 4.4 비밀번호 변경

| 항목 | 값 |
|------|-----|
| Method | `PUT` |
| Path | `/api/v1/members/{memberId}/password` |
| 응답 status | `200 OK` |

#### 경로 파라미터

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `memberId` | `Long` | 변경할 회원 ID |

#### 요청 본문

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `prevPassword` | `String` | ✔ | 현재 비밀번호 |
| `newPassword` | `String` | ✔ | 변경할 비밀번호 |

**요청 예시**
```json
{
    "prevPassword": "Secret",
    "newPassword": "newSecret"
}
```

#### 응답 본문

데이터 없이 성공 여부만 반환한다.

```json
{
  "success": true
}
```

#### 가능한 에러

| status | errorMessage 예시 | 원인 |
|--------|-------------------|------|
| 400 | `"비밀번호가 일치하지 않습니다"` | prevPassword가 현재 비밀번호와 다름 |
| 404 | `"회원을 찾을 수 없습니다"` | 존재하지 않는 memberId |

---

### 4.5 회원 역할 변경

| 항목 | 값 |
|------|-----|
| Method | `PUT` |
| Path | `/api/v1/members/{memberId}/role` |
| 응답 status | `200 OK` |

#### 경로 파라미터

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `memberId` | `Long` | 변경할 회원 ID |

#### 쿼리 파라미터

> 이 엔드포인트는 요청 **본문이 없습니다**. `role`은 URL 쿼리 파라미터로 전달합니다.

| 파라미터 | 타입 | 필수 | 설명 | 예시 |
|----------|------|------|------|------|
| `role` | `MemberRole` | ✔ | 변경할 회원 유형 | `INSTRUCTOR` |

**요청 예시**
```
PUT /api/v1/members/1/role?role=INSTRUCTOR
```

#### 응답 본문

데이터 없이 성공 여부만 반환한다.

```json
{
  "success": true
}
```

#### 가능한 에러

| status | errorMessage 예시 | 원인 |
|--------|-------------------|------|
| 404 | `"회원을 찾을 수 없습니다"` | 존재하지 않는 memberId |

---

### 4.6 강의 등록

| 항목 | 값 |
|------|-----|
| Method | `POST` |
| Path | `/api/v1/courses` |
| 응답 status | `202 Accepted` |

#### 동작 규칙

- `INSTRUCTOR` 역할의 회원만 강의를 등록할 수 있다. `STUDENT` 회원이 요청하면 400 에러가 발생한다.

#### 요청 본문

| 필드 | 타입 | 필수 | 설명 | 검증 |
|------|------|------|------|------|
| `instructorId` | `Long` | ✔ | 강사 회원 ID | - |
| `title` | `String` | ✔ | 강의명 | 최소 3자, 최대 200자 |
| `capacity` | `Integer` | ✔ | 최대 수강 인원 | null 불가 |
| `price` | `Long` | ✔ | 강의 가격 (원) | null 불가 |

**요청 예시**
```json
{
    "instructorId": 1,
    "title": "너나위의 내집마련 기초반",
    "capacity": 30,
    "price": 200000
}
```

#### 응답 본문 (`data` 필드)

| 필드 | 타입 | 설명 |
|------|------|------|
| `courseId` | `Long` | 생성된 강의 ID |
| `title` | `String` | 강의명 |
| `capacity` | `Integer` | 최대 수강 인원 |
| `price` | `Long` | 강의 가격 |
| `instructorName` | `String` | 강사 이름 |

**성공 예시 (202)**
```json
{
  "data": {
    "courseId": 1,
    "title": "너나위의 내집마련 기초반",
    "capacity": 30,
    "price": 200000,
    "instructorName": "홍길동"
  },
  "success": true
}
```

#### 가능한 에러

| status | errorMessage 예시 | 원인 |
|--------|-------------------|------|
| 400 | `"강사만 강의를 등록할 수 있습니다"` | STUDENT 회원이 요청 |
| 400 | Bean Validation 메시지 | 강의명 길이, capacity/price null 등 조건 미충족 |
| 404 | `"회원을 찾을 수 없습니다"` | 존재하지 않는 instructorId |

---

### 4.7 강의 목록 조회

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| Path | `/api/v1/courses` |
| 응답 status | `200 OK` |

#### 쿼리 파라미터

| 파라미터 | 타입 | 기본값 | 설명 |
|----------|------|--------|------|
| `sort` | `CourseSortType` | `CREATED_AT_DESC` | 정렬 기준 |
| `page` | `int` | `0` | 페이지 번호 (0부터 시작) |
| `size` | `int` | `20` | 페이지당 항목 수 (최대 100) |

**요청 예시**
```
GET /api/v1/courses
GET /api/v1/courses?sort=ENROLL_COUNT_DESC&page=0&size=20
GET /api/v1/courses?sort=ENROLL_RATE_DESC&page=1&size=10
```

#### 응답 본문 (`data` 필드 — `PagedResponse<CourseListItemResponse>`)

`content` 배열의 각 항목(`CourseListItemResponse`):

| 필드 | 타입 | 설명 |
|------|------|------|
| `courseId` | `Long` | 강의 ID |
| `title` | `String` | 강의명 |
| `price` | `Long` | 강의 가격 |
| `instructorName` | `String` | 강사 이름 |
| `enrollCount` | `int` | 현재 신청자 수 |
| `capacity` | `Integer` | 최대 수강 인원 |

**성공 예시 (200)**
```json
{
  "data": {
    "content": [
      {
        "courseId": 1,
        "title": "너나위의 내집마련 기초반",
        "price": 200000,
        "instructorName": "홍길동",
        "enrollCount": 15,
        "capacity": 30
      }
    ],
    "page": 0,
    "size": 20,
    "totalElements": 1,
    "totalPages": 1
  },
  "success": true
}
```

---

### 4.8 수강 신청

| 항목 | 값 |
|------|-----|
| Method | `POST` |
| Path | `/api/v1/enrollments` |
| 응답 status | `202 Accepted` |

#### 동작 규칙

- 한 요청에서 여러 강의를 한 번에 신청할 수 있다.
- **All-or-Nothing**: 한 강의라도 실패(정원 초과 / 중복 신청 / 존재하지 않는 강의)하면 **전체 롤백**된다.
- 동일 요청 내 중복된 `courseId`는 1건으로 처리된다.
- 인기 강좌 동시 신청은 비관적 락으로 직렬화되어 정확히 `capacity`까지 선착순으로 처리된다.
- `INSTRUCTOR` 회원도 수강 신청이 가능하다.

#### 요청 본문

| 필드 | 타입 | 필수 | 설명 | 검증 |
|------|------|------|------|------|
| `applicantId` | `Long` | ✔ | 신청자 회원 ID | - |
| `courseIds` | `List<Long>` | ✔ | 신청할 강의 ID 목록 | 비어있을 수 없음 |

**요청 예시**
```json
{
    "applicantId": 1,
    "courseIds": [1, 2, 3]
}
```

#### 응답 본문 (`data` 필드 — `List<EnrollmentApplyResponse>`)

신청한 강의 수만큼의 배열로 반환된다.

| 필드 | 타입 | 설명 |
|------|------|------|
| `enrollmentId` | `Long` | 수강 신청 ID |
| `courseId` | `Long` | 강의 ID |
| `title` | `String` | 강의명 |
| `applicantId` | `Long` | 신청자 회원 ID |
| `enrollCount` | `int` | 신청 후 현재 신청자 수 |
| `capacity` | `Integer` | 최대 수강 인원 |

**성공 예시 (202)**
```json
{
  "data": [
    {
      "enrollmentId": 10,
      "courseId": 1,
      "title": "너나위의 내집마련 기초반",
      "applicantId": 2,
      "enrollCount": 1,
      "capacity": 30
    }
  ],
  "success": true
}
```

#### 가능한 에러

| status | errorMessage 예시 | 원인 |
|--------|-------------------|------|
| 400 | `"최대 수강 인원을 초과했습니다"` | 정원이 마감된 강의 신청 |
| 400 | `"이미 신청한 강의입니다: 너나위의 내집마련 기초반"` | 동일 회원이 같은 강의를 재신청 |
| 404 | `"강의를 찾을 수 없습니다: 99"` | 존재하지 않는 courseId |
| 404 | `"회원을 찾을 수 없습니다"` | 존재하지 않는 applicantId |

---

## 5. 검증 규칙 요약

| 필드 | 규칙 | 적용 엔드포인트 |
|------|------|----------------|
| `name` | 최소 3자, 최대 50자 | POST /api/v1/members |
| `email` | 이메일 형식 (`^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$`) | POST /api/v1/members |
| `phoneNumber` | 한국 핸드폰 번호 (`^01[016789]-?\d{3,4}-?\d{4}$`) | POST /api/v1/members, PUT /phone |
| `rawPassword` | 최소 6자, 최대 10자. 영문 소문자·대문자·숫자 중 2종 이상 조합 | POST /api/v1/members |
| `role` | `INSTRUCTOR` 또는 `STUDENT` | POST /api/v1/members |
| `title` (강의명) | 최소 3자, 최대 200자 | POST /api/v1/courses |
| `capacity` | null 불가 | POST /api/v1/courses |
| `price` | null 불가 | POST /api/v1/courses |
| `courseIds` | 비어있을 수 없음 | POST /api/v1/enrollments |

---

## 6. 비즈니스 규칙

### 회원
- 이메일은 시스템 내 고유해야 한다(중복 가입 불가).
- 비밀번호는 저장 시 BCrypt로 해시된다. 원문은 저장되지 않는다.

### 강의
- `INSTRUCTOR` 역할의 회원만 강의를 등록할 수 있다.
- 강의 등록 후 `enrollCount`는 0에서 시작하며 수강 신청 시마다 1 증가한다.

### 수강 신청
- 모든 회원(`INSTRUCTOR`, `STUDENT`)이 수강 신청 가능하다.
- 동일 회원이 동일 강의에 중복 신청할 수 없다.
- `enrollCount`가 `capacity`에 도달하면 더 이상 신청할 수 없다.
- 한 요청 내 여러 강의 신청은 All-or-Nothing 트랜잭션으로 처리된다.
- 동시 신청 시 비관적 락(`SELECT ... FOR UPDATE`)으로 강의별로 직렬화되어, 정확히 `capacity`명까지만 선착순으로 신청 완료된다.
