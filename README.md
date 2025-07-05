
<p align="center">
<br>
  <img src="https://github.com/user-attachments/assets/dc861e51-ba08-4a8d-ad85-1c8129c8a21d" width="150" alt="InMyHand Logo">
  <br>
</p>

<h1 align="center">🍳 InMyHand Project</h1>

<p align="center">
  <strong>내 손 안의 똑똑한 레시피 비서, InMyHand</strong>
### 프론트 코드는 여러 사정으로 인해 공개드리기 어려운 점 이해해 주시기 바랍니다.
### 현재 프로젝트에는 백엔드 코드만 있습니다.

 
  <br>
  <a href="#-주요-기능">주요 기능</a> •
  <a href="#-기술-스택">기술 스택</a> •
  <a href="#-프로젝트-구조">프로젝트 구조</a> •
  <a href="#-설치-및-실행-방법">설치 및 실행</a> •
  <a href="#-api-엔드포인트">API 엔드포인트</a>
</p>

---

InMyHand는 사용자의 냉장고 속 재료를 효율적으로 관리하고, AI를 활용하여 맞춤형 레시피를 추천해주는 지능형 웹 애플리케이션입니다. 단순히 레시피를 제공하는 것을 넘어, 식재료 낭비를 줄이고 즐거운 요리 경험을 선사하는 것을 목표로 합니다.

---

## ✨ 주요 기능

- **🍎 냉장고 관리**: 사용자가 보유한 식재료를 등록하고, 유통기한을 관리하며, 재고를 파악할 수 있습니다.
- **🤖 AI 레시피 추천**: 냉장고 속 재료를 기반으로 AI가 만들 수 있는 요리의 레시피를 추천합니다.
- **🔍 레시피 검색 및 상세 정보**: 다양한 조건으로 레시피를 검색하고, 상세한 조리 과정과 영양 정보를 확인할 수 있습니다.
- **👤 사용자 맞춤형 기능**: 나만의 레시피를 등록하고, 마음에 드는 레시피를 스크랩하며, 댓글로 소통할 수 있는 마이페이지를 제공합니다.
- **🛠️ 관리자 페이지**: 사용자 및 레시피 데이터를 관리할 수 있는 관리자 기능을 제공합니다.

---

## 🛠️ 기술 스택

### **Backend**
<p>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle"/>
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis"/>
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" alt="Spring Security"/>
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white" alt="JWT"/>
  <img src="https://img.shields.io/badge/OAuth2-24292E?style=for-the-badge&logo=oauth&logoColor=white" alt="OAuth2"/>
  <img src="https://img.shields.io/badge/Google_Cloud-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white" alt="Google Cloud"/>
  <img src="https://img.shields.io/badge/Amazon_AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white" alt="AWS"/>
</p>

### **Frontend**
<p>
  <img src="https://img.shields.io/badge/eXBuilder6-7A52CC?style=for-the-badge" alt="eXBuilder6"/>
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white" alt="HTML5"/>
  <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white" alt="CSS3"/>
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black" alt="JavaScript"/>
</p>

### **CI/CD**
<p>
  <img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white" alt="GitHub Actions"/>
</p>

---

## 📂 프로젝트 구조

```
inmyhand-main/
├── .github/workflows/  # GitHub Actions 워크플로우 (CI/CD)
├── clx-src/            # eXBuilder6 프론트엔드 소스 코드
│   ├── app/            # 화면 UI(clx) 및 로직(js)
│   ├── data/           # 프론트엔드용 데이터 모듈
│   └── theme/          # 커스텀 테마 및 스타일시트
├── files/              # TF-IDF 캐시 등 데이터 파일
├── src/main/           # Spring Boot 백엔드 소스 코드
│   ├── java/           # Java 소스 코드 (Controller, Service, Repository 등)
│   └── resources/      # application.properties, static 파일 등
├── build.gradle        # 프로젝트 의존성 및 빌드 설정
└── gradlew             # Gradle Wrapper
```

---

## 🚀 설치 및 실행 방법

1.  **프로젝트 클론**
    ```bash
    git clone https://github.com/your-username/inmyhand-main.git
    cd inmyhand-main
    ```

2.  **설정 파일 구성**
    `src/main/resources/application.properties` (또는 `application.yml`) 파일을 열고, 본인의 환경에 맞게 데이터베이스, Redis, JWT Secret Key, OAuth 2.0 클라이언트 정보, GCP Vertex AI 키 등을 설정합니다.

    ```properties
    # Database
    spring.datasource.url=jdbc:postgresql://localhost:5432/inmyhand
    spring.datasource.username=your-db-username
    spring.datasource.password=your-db-password

    # Redis
    spring.redis.host=localhost
    spring.redis.port=6379

    # JWT
    jwt.secret.key=your-super-secret-key

    # Google Cloud Vertex AI
    spring.ai.vertex.ai.gemini.project-id=your-gcp-project-id
    spring.ai.vertex.ai.gemini.location=your-gcp-location

    # ... 기타 설정
    ```

3.  **프로젝트 빌드**
    ```bash
    ./gradlew build
    ```

4.  **애플리케이션 실행**
    ```bash
    java -jar build/libs/refrigerator-0.0.1-SNAPSHOT.jar
    ```
    또는 Gradle을 통해 직접 실행할 수도 있습니다.
    ```bash
    ./gradlew bootRun
    ```

---

## 🔑 API 엔드포인트

### **👮‍♂️ 관리자 (Admin)**

| Method | Endpoint | 설명 |
| :--- | :--- | :--- |
| `GET` | `/api/admin/users` | 모든 사용자 목록 조회 |
| `POST` | `/api/admin/user/update` | 사용자 정보 업데이트 |
| `GET` | `/api/admin/recipe/{id}` | 특정 사용자의 레시피 목록 조회 |
| `POST` | `/api/admin/user/search` | 사용자 검색 |
| `POST` | `/api/admin/check` | 관리자 권한 확인 |
| `GET` | `/api/server/check` | 서버 인증 확인 |

### **👤 사용자 & 인증 (User & Auth)**

| Method | Endpoint | 설명 |
| :--- | :--- | :--- |
| `POST` | `/api/users/login` | 로컬 로그인 |
| `POST` | `/api/users/logout` | 로그아웃 |
| `POST` | `/api/users/login/check` | 로그인 상태 확인 |
| `POST` | `/api/user/register` | 로컬 회원가입 |
| `POST` | `/api/user/auth/verify-email` | 이메일 인증 요청 |
| `POST` | `/api/user/auth/verify-code` | 이메일 인증 코드 확인 |
| `POST` | `/api/user/check/local` | 로컬 계정 여부 확인 |

### **📄 마이페이지 (MyPage)**

| Method | Endpoint | 설명 |
| :--- | :--- | :--- |
| `POST` | `/api/myInfo/getMyInfo` | 내 정보 조회 |
| `POST` | `/api/myInfo/setMyInfo` | 내 정보 수정 |
| `POST` | `/api/myInfo/reset/password` | 비밀번호 재설정 요청 |
| `POST` | `/api/myInfo/change/password` | 비밀번호 변경 |
| `POST` | `/api/mypage/nickname` | 닉네임 조회 |
| `POST` | `/api/mypage/profile` | 프로필 정보 조회 |
| `POST` | `/api/mypage/myrefre` | 내 냉장고 재료 정보 조회 |
| `GET` | `/api/mypage/myrecipe/like` | 내가 좋아요 한 레시피 목록 조회 |
| `GET` | `/api/mypage/myrecipe/write` | 내가 작성한 레시피 목록 조회 |
| `GET` | `/api/mypage/mycomment` | 내가 작성한 댓글 목록 조회 |

### **🍲 레시피 (Recipe)**

| Method | Endpoint | 설명 |
| :--- | :--- | :--- |
| `POST` | `/api/recipes` | 전체 레시피 목록 조회 (페이징) |
| `POST` | `/api/recipes/popular` | 인기 레시피 목록 조회 |
| `POST` | `/api/recipes/sort` | 레시피 정렬 조회 (난이도, 칼로리 등) |
| `POST` | `/api/recipes/{id}` | 레시피 상세 조회 |
| `POST` | `/api/recipes/similar/{id}` | 유사 레시피 추천 |
| `POST` | `/api/recipes/search` | 레시피 검색 |
| `POST` | `/api/recipes/create` | 새 레시피 생성 |
| `PUT` | `/api/recipes/{recipeId}` | 레시피 수정 |
| `DELETE` | `/api/recipes/{recipeId}` | 레시피 삭제 |
| `POST` | `/api/recipes/comments/{recipeId}` | 레시피 댓글 등록 |
| `DELETE` | `/api/recipes/comments/delete/{commentId}` | 레시피 댓글 삭제 |
| `POST` | `/api/recipes/likes/{recipeId}` | 레시피 좋아요 토글 |
| `POST` | `/api/recipes/likes/check/{recipeId}` | 레시피 좋아요 여부 확인 |
| `POST` | `/api/recipes/nutrient/{recipeId}` | 레시피 영양 정보 분석 |
| `POST` | `/api/recipes/recommend` | AI 레시피 추천 |
| `POST` | `/api/recipes/views/{recipeId}` | 레시피 조회수 증가 |
| `GET` | `/api/recipe/search/popular` | 인기 검색어 목록 조회 |

### **🍎 냉장고 (Fridge)**

| Method | Endpoint | 설명 |
| :--- | :--- | :--- |
| `POST` | `/api/fridge/change` | 내 기본 냉장고의 모든 식재료 조회 |
| `POST` | `/api/fridge/change/click` | 특정 냉장고의 모든 식재료 조회 |
| `POST` | `/api/fridge/checkRoles` | 특정 냉장고에 대한 내 권한 확인 |
| `POST` | `/api/fridge/members` | 특정 냉장고의 참여 멤버 목록 조회 |
| `POST` | `/api/fridge/myFridgeList` | 내가 참여중인 모든 냉장고 목록 조회 |
| `POST` | `/api/fridge/create/foodList` | 식재료 일괄 추가 |
| `POST` | `/api/fridge/update/foodList` | 식재료 정보 수정 및 삭제 |
| `POST` | `/api/fridge/search` | 식재료 카테고리 검색 |

### **🤝 냉장고 그룹 & 초대 (Fridge Group & Invite)**

| Method | Endpoint | 설명 |
| :--- | :--- | :--- |
| `POST` | `/api/fridge/role/checkbox` | 모든 역할(권한) 목록 조회 |
| `POST` | `/api/fridge/role/pending/groupList` | 초대 대기중인 냉장고 목록 조회 |
| `POST` | `/api/fridge/role/group/edit` | 그룹 멤버 권한 수정 및 삭제 |
| `POST` | `/api/ocr/search` | 사용자 검색 (초대용) |
| `POST` | `/api/ocr/invite` | 냉장고에 사용자 초대 |
| `POST` | `/api/ocr/accept` | 냉장고 초대 수락 |
| `POST` | `/api/ocr/delete` | 냉장고 초대 거절/삭제 |

### **🧾 OCR & 파일 업로드 (OCR & File Upload)**

| Method | Endpoint | 설명 |
| :--- | :--- | :--- |
| `POST` | `/api/files/upload-image` | 이미지 파일 업로드 |
| `POST` | `/api/files/ocr-upload-test` | 영수증 OCR 및 식재료 정보 추출 |
| `POST` | `/api/ocr/create` | OCR로 추출된 식재료 정보를 냉장고에 추가 |

### **🌿 건강 정보 (Health Info)**

| Method | Endpoint | 설명 |
| :--- | :--- | :--- |
| `POST` | `/api/healthInfo/allergy` | 내 알러지 정보 조회 |
| `POST` | `/api/healthInfo/hate_food` | 내 기피 음식 정보 조회 |
| `POST` | `/api/healthInfo/health_interest` | 내 건강 관심사 정보 조회 |
| `POST` | `/api/healthInfo/search/health_interest` | 건강 관심사 카테고리 검색 |
| `POST` | `/api/healthInfo/search/ingredients` | 식재료 카테고리 검색 |
| `POST` | `/api/healthInfo/save` | 건강 정보 저장 |

### **💳 결제 (Payment)**

| Method | Endpoint | 설명 |
| :--- | :--- | :--- |
| `POST` | `/payment/confirm` | 토스페이먼츠 결제 승인 |
| `GET` | `/payment/findall` | 모든 구독 정보 조회 (테스트용) |
| `GET` | `/payment/finduser` | 내 구독 정보 조회 |

---

 
