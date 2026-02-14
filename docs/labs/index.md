# OWASP Top 10:2025 Labs Roadmap

## 주의
이 문서는 로컬 학습 전용입니다. 외부 타겟에 적용하지 마세요.

## 매핑 표
| ID | 항목 | 이 랩의 대표 시나리오 |
|---|---|---|
| A01 | Broken Access Control | 관리자 종목 관리 API를 일반 사용자에게 차단 |
| A02 | Cryptographic Failures | 비밀번호 해시/토큰 저장/전송 정책 비교 |
| A03 | Injection | 검색/필터 파라미터 처리와 안전한 쿼리 |
| A04 | Insecure Design | 잔고/보유수량 비즈니스 검증 |
| A05 | Security Misconfiguration | Swagger/CORS/에러 핸들링 기본 설정 |
| A06 | Vulnerable and Outdated Components | 의존성 버전 관리와 점검 |
| A07 | Identification and Authentication Failures | 로그인 실패 처리와 세션/JWT 통제 |
| A08 | Software and Data Integrity Failures | 시드/배포 아티팩트 신뢰 경계 |
| A09 | Security Logging and Monitoring Failures | 로그인/권한거부/주요 변경 감사 로그 |
| A10 | SSRF | 외부 시세 URL 입력 시 검증/허용 목록 |

## 문서 목록
- `docs/labs/a01-broken-access-control.md`
- `docs/labs/a02-cryptographic-failures.md`
- `docs/labs/a03-injection.md`
- `docs/labs/a04-insecure-design.md`
- `docs/labs/a05-security-misconfiguration.md`
- `docs/labs/a06-vulnerable-components.md`
- `docs/labs/a07-auth-failures.md`
- `docs/labs/a08-data-integrity.md`
- `docs/labs/a09-logging-monitoring.md`
- `docs/labs/a10-ssrf.md`
