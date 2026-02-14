# Stock Trading Security Lab

## 경고 (필독)
**이 프로젝트는 교육용/로컬 실습 전용입니다.**

- 실서비스 배포 금지
- 외부 타겟 공격/점검 금지
- 반드시 로컬 환경(`localhost`)에서만 실행

## 프로젝트 개요
주식 모의 트레이딩 웹앱을 기반으로 OWASP Top 10:2025 관점의 보안 학습을 수행하는 실습 랩입니다.

- Backend: Spring Boot (Java 21)
- Frontend: React (JavaScript, Vite)
- DB: PostgreSQL (Docker Compose)
- 모드: `secure` / `vulnerable`

## 저장소 구조 선택
이 저장소는 **A안(브랜치 분리)** 을 채택합니다.

- `secure` 브랜치: 보안 통제가 적용된 기준 구현
- `vulnerable` 브랜치: 동일 기능 + 학습용 취약점 제한 반영
- 공통 문서: `main` 기준으로 유지/동기화

선택 이유:
- 기능 코드를 폴더로 이중 관리하지 않아 비교 시 변경 이력을 추적하기 쉽습니다.
- `git diff secure..vulnerable`로 학습 포인트를 명확히 확인할 수 있습니다.

## 학습 문서
- 로드맵: `docs/labs/index.md`
- 항목별 실습: `docs/labs/*.md`
- secure vs vulnerable 차이 설명: `docs/diffs/`

## OWASP Top 10:2025 실습 매핑
| ID | 항목 | 대표 시나리오 |
|---|---|---|
| A01 | Broken Access Control | 일반 사용자의 관리자 API 접근 시도 |
| A02 | Cryptographic Failures | 약한 비밀번호 정책/토큰 관리 비교 |
| A03 | Injection | 주문/종목 검색 입력 처리 비교 |
| A04 | Insecure Design | 비즈니스 규칙(잔고/보유수량) 검증 누락 |
| A05 | Security Misconfiguration | CORS/Swagger/에러노출 설정 비교 |
| A06 | Vulnerable and Outdated Components | 의존성 업데이트/고정 관리 |
| A07 | Identification and Authentication Failures | 로그인 세션/JWT 처리 취약점 비교 |
| A08 | Software and Data Integrity Failures | 시드/설정 파일 무결성 검증 |
| A09 | Security Logging and Monitoring Failures | 로그인 실패/권한 거부 감사 추적 |
| A10 | Server-Side Request Forgery (SSRF) | 외부 시세 연동 가정 시 URL 검증 |

## 빠른 실행 (Docker)
```bash
docker compose up --build
```

기본 포트(로컬 바인딩):
- Backend API: `127.0.0.1:8080`
- Frontend: `127.0.0.1:5173`
- PostgreSQL: `127.0.0.1:5432`

## 시드 계정
- 일반 사용자: `user1 / User1234!`
- 관리자: `admin1 / Admin1234!`

## 모드 전환 방법
```bash
git checkout secure
# 또는
git checkout vulnerable
```

브랜치 전환 후:
```bash
docker compose down
docker compose up --build
```

## 생성/수정 파일 목록
아래 파일은 초기 구성 기준입니다.

- 문서: `README.md`, `CONTRIBUTING.md`, `docs/labs/index.md`, `docs/labs/*.md`, `docs/diffs/*.md`
- 백엔드: `backend/**`
- 프론트엔드: `frontend/**`
- 인프라: `docker-compose.yml`, `.github/workflows/ci.yml`

## 권장 학습 순서
1. `docs/labs/index.md` 로 전체 시나리오 확인
2. `secure` 브랜치에서 기능/테스트 동작 확인
3. `vulnerable` 브랜치로 전환 후 차이 확인
4. `docs/diffs` 문서로 취약점별 코드 비교
