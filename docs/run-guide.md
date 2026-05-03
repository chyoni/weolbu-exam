# 애플리케이션 실행 절차

이 문서만 따라하면 개발 지식 없이도 애플리케이션을 실행하고 API를 직접 테스트할 수 있습니다.

---

## 1. 이 문서로 무엇을 할 수 있나

- 회원 가입 / 강의 등록 / 수강 신청 API 서버를 내 컴퓨터에서 실행합니다.
- 브라우저에서 Swagger UI를 열어 API를 직접 호출하고 결과를 확인합니다.

---

## 2. 준비물

### 2.1 운영체제

- macOS (M1/M2/Intel 모두 가능)
- Windows 10 이상

### 2.2 JDK 21 설치

이 애플리케이션은 **Java 21**이 필요합니다. 이미 설치되어 있다면 2.3으로 건너뜁니다.

**macOS**

1. [https://adoptium.net](https://adoptium.net) 접속
2. **Temurin 21 (LTS)** 선택 → **macOS** → **aarch64**(M1/M2 Mac) 또는 **x64**(Intel Mac) → `.pkg` 다운로드
3. 다운로드된 `.pkg` 파일을 더블클릭해 설치
4. 설치 완료 후 터미널을 열고 아래 명령어 입력:
   ```
   java -version
   ```
5. 결과에 `openjdk version "21"...`이 보이면 성공

**Windows**

1. [https://adoptium.net](https://adoptium.net) 접속
2. **Temurin 21 (LTS)** 선택 → **Windows** → **x64** → `.msi` 다운로드
3. 다운로드된 `.msi` 파일을 더블클릭해 설치 (설치 중 "Add to PATH" 옵션을 반드시 체크)
4. 설치 완료 후 **명령 프롬프트(cmd)** 또는 **PowerShell**을 새로 열고:
   ```
   java -version
   ```
5. 결과에 `openjdk version "21"...`이 보이면 성공

### 2.3 프로젝트 폴더 확인

프로젝트 폴더(`weolbu-exam`) 안에 아래 파일들이 있어야 합니다:

```
weolbu-exam/
├── gradlew          ← macOS/Linux용 실행 파일
├── gradlew.bat      ← Windows용 실행 파일
├── build.gradle.kts
└── src/
```

---

## 3. 실행 단계

### 3.1 터미널(명령 프롬프트) 열기

- **macOS**: Spotlight 검색(`Cmd + Space`) → `터미널` 또는 `Terminal` 입력 → 열기
- **Windows**: 시작 메뉴 → `cmd` 검색 → **명령 프롬프트** 열기 (또는 PowerShell)

### 3.2 프로젝트 폴더로 이동

아래 명령어에서 `/실제/경로/weolbu-exam` 부분을 프로젝트가 있는 실제 경로로 바꿔 입력합니다.

**macOS**
```bash
cd /실제/경로/weolbu-exam
```

**Windows**
```
cd C:\실제\경로\weolbu-exam
```

폴더 이동 후 `ls`(macOS) 또는 `dir`(Windows)을 입력해 `gradlew` 파일이 보이면 올바른 위치입니다.

### 3.3 빌드

> 처음 실행 시 필요한 파일들을 인터넷에서 내려받으므로 **5~10분** 정도 걸릴 수 있습니다. 인터넷 연결이 필요합니다.

**macOS**
```bash
./gradlew build
```

**Windows**
```
gradlew.bat build
```

> macOS에서 `Permission denied` 오류가 나오면 아래를 먼저 실행한 뒤 다시 시도합니다:
> ```bash
> chmod +x ./gradlew
> ```

터미널에 마지막으로 아래 메시지가 나오면 빌드 성공입니다:
```
BUILD SUCCESSFUL in Xs
```

### 3.4 실행

> **중요**: 아래 명령어에서 `--args='--spring.profiles.active=local'` 부분을 **반드시** 포함해야 합니다. 빠뜨리면 애플리케이션이 실행되지 않습니다.

**macOS**
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

**Windows**
```
gradlew.bat bootRun --args="--spring.profiles.active=local"
```

터미널에 아래와 비슷한 메시지가 나오면 실행 성공입니다:
```
Started WeolbuExamApplication in 3.5 seconds (process running for 4.1)
```

이 상태에서 터미널 창을 닫지 않고 그대로 두면 서버가 계속 동작합니다.

### 3.5 종료

서버를 종료하려면 서버가 실행 중인 터미널 창에서:

- **macOS**: `Ctrl + C`
- **Windows**: `Ctrl + C`

---

## 4. 실행 확인

### 4.1 Swagger UI 접속

브라우저(Chrome, Safari, Edge 등)를 열고 주소창에 아래 주소를 입력합니다:

```
http://localhost:8080/swagger-ui.html
```

아래 화면이 나오면 성공입니다:

- 상단에 API 목록이 보입니다 (강의 등록, 강의 조회, 수강 신청, 회원 생성·수정, 회원 조회 총 5개 그룹)
- 각 그룹을 클릭하면 세부 API를 볼 수 있습니다

### 4.2 OpenAPI 문서 (선택)

API 스펙을 JSON 형태로 보려면:
```
http://localhost:8080/v3/api-docs
```

---

## 5. 샘플 시나리오 따라하기

Swagger UI(`http://localhost:8080/swagger-ui.html`)에서 아래 순서대로 따라합니다.

각 API는 클릭 → **Try it out** 버튼 → 내용 입력 → **Execute** 버튼 순으로 사용합니다.

### 5.1 강사 회원 가입

`회원 생성 및 수정 API` → `POST /api/v1/members` → **Try it out** → 아래 내용으로 교체 → **Execute**

```json
{
    "name": "강사홍길동",
    "email": "instructor@example.com",
    "phoneNumber": "01011112222",
    "rawPassword": "Secret1",
    "role": "INSTRUCTOR"
}
```

응답에서 `"memberId": 1` (실제 숫자)을 확인하고 메모해둡니다.

### 5.2 수강생 회원 가입

같은 API로 수강생도 가입합니다:

```json
{
    "name": "수강생김철수",
    "email": "student@example.com",
    "phoneNumber": "01022223333",
    "rawPassword": "Secret2",
    "role": "STUDENT"
}
```

응답에서 수강생의 `"memberId"` (예: `2`)도 메모해둡니다.

### 5.3 강의 등록

`강의 등록 관련 API` → `POST /api/v1/courses` → **Try it out** → 아래 내용 입력 (강사 memberId로 수정) → **Execute**

```json
{
    "instructorId": 1,
    "title": "내집마련 기초반",
    "capacity": 1,
    "price": 200000
}
```

> `capacity`를 `1`로 설정하면 5.6단계에서 정원 초과를 직접 확인할 수 있습니다.

응답에서 `"courseId"` (예: `1`)를 메모해둡니다.

### 5.4 강의 목록 조회

`강의 조회 관련 API` → `GET /api/v1/courses` → **Try it out** → **Execute**

방금 등록한 강의가 `"enrollCount": 0`으로 보이면 정상입니다.

### 5.5 수강 신청

`수강신청 API` → `POST /api/v1/enrollments` → **Try it out** → 아래 내용 입력 (수강생 memberId, courseId 수정) → **Execute**

```json
{
    "applicantId": 2,
    "courseIds": [1]
}
```

응답에서 `"enrollCount": 1`이 보이면 성공입니다.

### 5.6 중복 신청 시도 (에러 확인)

5.5와 동일한 내용으로 다시 **Execute**합니다.

```json
{
    "success": false,
    "errorMessage": "이미 신청한 강의입니다: 내집마련 기초반"
}
```

위와 같은 에러 응답이 오면 정상입니다.

### 5.7 정원 초과 시도 (에러 확인)

강사 회원(memberId: 1)으로 같은 강의를 신청합니다 (`capacity=1`이므로 이미 마감):

```json
{
    "applicantId": 1,
    "courseIds": [1]
}
```

```json
{
    "success": false,
    "errorMessage": "최대 수강 인원을 초과했습니다"
}
```

위와 같은 에러 응답이 오면 정상입니다.

---

## 6. 자주 만나는 문제

### "Failed to determine a suitable driver class"

`--spring.profiles.active=local`이 없거나 철자가 틀렸습니다.  
정확히 입력했는지 확인하고 다시 실행합니다.

### "Web server failed to start. Port 8080 was already in use"

이미 8080 포트를 사용하는 다른 프로그램이 있습니다.

- **macOS**: 다른 터미널 창에서 `lsof -i :8080`으로 점유 프로세스를 확인한 뒤 종료하거나, 컴퓨터를 재시작합니다.
- **Windows**: 작업 관리자에서 해당 프로세스를 종료하거나 컴퓨터를 재시작합니다.

### "JAVA_HOME is set to an invalid directory" 또는 `java: command not found`

JDK가 설치되지 않았거나 설치 후 터미널을 닫았다가 새로 열지 않았습니다.  
JDK 21 재설치 후 터미널을 새로 열어서 `java -version`을 확인합니다.

### macOS에서 `./gradlew: Permission denied`

```bash
chmod +x ./gradlew
```

위 명령어를 실행한 뒤 다시 빌드합니다.

### 실행 후 데이터가 사라졌습니다

이 애플리케이션은 H2 인메모리 데이터베이스를 사용합니다. 서버를 종료하고 다시 시작하면 모든 데이터가 초기화되는 것이 정상 동작입니다.

---

## 7. 다음 단계

- API 전체 스펙 보기: [`docs/api-spec.md`](api-spec.md)
- 도메인 모델 및 요구사항: [`docs/domain-model.md`](domain-model.md)
