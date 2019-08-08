# 배포

디렉터리: TBD

* `docker-compose.yml`: ~/app
* Application log: ~/app/log
* Jenkins home directory: ~/app/jenkins_home
* Mysql data: ~/app/db_data

## 초기 설정

다음 명령어가 도커를 설치한 뒤 및 Mysql과 Jenkins 컨테이너를 실행한다.

```bash
$ wget -O - https://raw.githubusercontent.com/Laterality/jwp-blog/step4/scripts/init.sh | bash
```

스크립트 실행 중 마지막으로 출력되는 행이 Jenkins 초기 비밀번호이다.

`localhost:8000`로 접속, 초기 비밀번호 입력

`Install Suggested Plugins` 선택

관리자 계정 생성

## Github 계정 세팅

### Access Token 발급

GitHub → 우측 상단 프로필 → Settings → Developer Settings → Personal access tokens → Generate new token 선택

**admin:repo_hook** 체크한 뒤 토큰 발급

Jenkins 설정 → GiHub 항목에서 Add GitHub Server

Name은 임의로 설정

API URL은 기본값 사용

Credentials에 Add

    Kind를 Secret Text로 선택한 뒤 Secret 값에 발급받은 토큰 입력 후 Add

    Test connection으로 접속 확인

## 프로젝트 생성

`New item` 선택

프로젝트 이름 입력 후 `Pipeline` 선택하여 생성

General 항목의 GitHub project 선택 후 저장소 주소 입력

Build Triggers 항목의 GitHub hook trigger for GITScm polling 체크

Pipeline 항목의 Definition을 Pipeline script from SCM으로 선택, SCM은 Git으로 선택 후 저장소 주소와 GitHub 계정, 빌드할 브랜치명 입력

저장

이후 코드 commit & push하여 자동으로 빌드 되는지 확인
