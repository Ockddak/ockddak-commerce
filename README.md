# 확장 가능한 이벤트 기반 전자상거래 플랫폼
대용량 트래픽을 처리할 수 있는 이벤트 기반의 전자상거래 플랫폼을 구축하는 프로젝트입니다. 모놀리식 아키텍처로 시작하여 점진적으로 마이크로서비스 아키텍처(MSA)로 전환하는 과정을 통해 확장성과 유연성을 확보하는 것을 목표로 합니다.

## 🚀 학습 목표
* **대용량 트래픽 처리 경험 부족 해소:** 실제 트래픽 상황을 가정하여 시스템 성능 개선 및 병목 지점 해결 능력 향상.
* **핵심 기술 스택 숙련:** Spring Boot (Java 21), Docker, Redis, k6, GitHub Actions, Prometheus, Grafana, Kafka 등 최신 백엔드 기술 학습 및 활용.
* **모놀리식 → MSA 전환 경험:** Strangler Fig Pattern 등을 활용하여 점진적으로 아키텍처를 전환하며 복잡한 시스템 설계 및 운영 능력 습득.

## 🛠️ 기술 스택 (예상)

### 백엔드
* **언어:** Java 21
* **프레임워크:** Spring Boot 3.x
* **ORM:** Spring Data JPA
* **데이터베이스:** PostgreSQL (또는 MySQL)
* **캐싱:** Redis
* **메시지 브로커:** Apache Kafka

### 인프라 및 DevOps
* **컨테이너:** Docker, Docker Compose
* **CI/CD:** GitHub Actions
* **모니터링:** Prometheus, Grafana
* **부하 테스트:** k6

### 아키텍처
* 모놀리식 (초기)
* 마이크로서비스 아키텍처 (MSA)
* 이벤트 기반 아키텍처
* 사가(Saga) 패턴
