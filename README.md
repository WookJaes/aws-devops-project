# AWS 기반 팀원 소개 서비스

## LV0. 요금 폭탄 방지 AWS Budget 설정

<img width="2844" height="1318" alt="Image" src="https://github.com/user-attachments/assets/e90cce22-fd54-434a-b607-cf4e2c9a147c" />
<br/><br/>

## LV1. 네트워크 구축 및 핵심 기능 배포

### EC2 정보

- EC2 Public IP: `3.35.219.226`
- Health Check: `http://3.35.219.226:8080/actuator/health`

### 배포 검증

<img width="898" height="454" alt="Image" src="https://github.com/user-attachments/assets/a63340cf-0f62-4c68-8314-6529715fe140" />
<br/><br/>

## LV2. DB 분리 및 보안 연결하기

### Actuator Info URL

- Info URL: `http://3.35.219.226:8080/actuator/info`

### 검증
<img width="868" height="362" alt="Image" src="https://github.com/user-attachments/assets/a1b18f74-abfb-409f-adc9-03df44c22c4c" />
<br/>

### RDS 보안 그룹

- EC2에서만 접근할 수 있도록 IP 주소가 아닌 EC2 보안 그룹을 인바운드 규칙으로 설정했습니다.
- EC2 보안 그룹 ID: `sg-028f623fa2e747d7b`

<img width="2872" height="1448" alt="Image" src="https://github.com/user-attachments/assets/96c305d1-1096-4612-a7eb-86ba0ca9198c" />
<br/><br/>

## LV3. 프로필 사진 기능 추가와 권한 관리

### Presigned URL 검증

`http://3.35.219.226:8080/api/members/1/profile-image` 요청 시 유효기간 7일의 Presigned URL을 발급하도록 구현했습니다.

<img width="2610" height="1324" alt="Image" src="https://github.com/user-attachments/assets/80ac6a1d-7b6c-46e3-a658-686c059d37c1" />
<br/><br/>

## LV4. Docker & CI/CD 파이프라인 구축

### Github Actions 배포 성공

- `master` 브랜치에 push하면 Github Actions에서 Gradle Build/Test를 수행합니다.
- 빌드된 Docker 이미지를 Docker Hub(`wookjaejeong/cloud-app:latest`)에 push합니다.
- 이후 EC2에 SSH로 접속하여 최신 이미지를 pull 받고 기존 컨테이너를 교체 실행합니다.

<img width="2130" height="636" alt="Image" src="https://github.com/user-attachments/assets/1e36068b-5960-49af-8700-257c60a7585e" />

### EC2 Docker 컨테이너 실행 확인

- EC2에 접속한 뒤 `docker ps` 명령어로 실행 중인 컨테이너를 확인했습니다.

<img width="2798" height="104" alt="Image" src="https://github.com/user-attachments/assets/f8ef56e6-d4ee-4572-a724-b52d3bd51b5e" />
<br/><br/>

## LV5. 고가용성 아키텍처와 보안 도메인 연결 (ALB + ASG + HTTPS)

### HTTPS 도메인 적용

- HTTPS 적용 도메인 URL: `https://api.wookjae.click`
- Health Check URL: `https://api.wookjae.click/actuator/health`
- Actuator Info URL: `https://api.wookjae.click/actuator/info`

### Target Group 상태

<img width="2874" height="1310" alt="Image" src="https://github.com/user-attachments/assets/316dac09-9286-421f-a5c4-67cb567ef38c" />
<br/><br/>

## LV6. 글로벌 성능 최적화 (CloudFront CDN)

### CloudFront CDN 적용

- CloudFront 배포 도메인: `https://d10m4tny445qnt.cloudfront.net`
- CloudFront 이미지 URL: `https://d10m4tny445qnt.cloudfront.net/uploads/1_c545bd46-9b7d-450b-a716-2154ea0260df_pheladii-human-9814976.png`

<img width="2880" height="1696" alt="Image" src="https://github.com/user-attachments/assets/256355ac-94c5-4e08-b873-a9c27d3aa1c1" />
