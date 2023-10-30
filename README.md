# Smiley-Android-App



<hr>

<div align="center">
   <img src="https://raw.githubusercontent.com/Donghyeon0915/DataRepository/master/Smiley/smiley_cover3.png" width="100%"/> &nbsp;&nbsp;
</div>

## 🏆 수상 내역

- 📅 **2022.11.25** - 🎖️ **2022 통합 성과 경진대회** - 🏅 **장려상 (총장상)**
- 📅 **2022.12.08** - 🎖️ **2022 LINC 3.0 캡스톤 디자인 경진대회** - 🥈 **우수상 (총장상)**
- 📅 **2023.09.21** - 🎖️ **2023 의료. 보건. 생활대학 아이디어 & 발명 경진대회** - 🥈 **2등상 (산학협력단장상)**
- 📅 **2023.10.12** - 🎖️ **2023년 한이음 ICT멘토링 공모전** - 🏆 **한국정보산업연합회장상**

<br>

## 💡 주제

- 교정 자동화 & 관리 어플리케이션
- BLE + 압력 센서를 이용한 주걱턱 교정기, 교정기 케이스 착용 시간 측정

<br>

## 📝 설명

***Smiley***는 사용자의 **교정기 착용 여부** 및 **착용 시간**을 **실시간**으로 감지하여 교정 관리를 **자동화**하며, 

**착용 시간 관리, 일일 미션, 예약 관리, 챗봇 상담** 등 **교정에 관한 모든 편의기능을 앱에서 제공**합니다.

<br>


## 🖥️ 시연 영상

[![Video Label](http://img.youtube.com/vi/VlU84cp0GD8/0.jpg)](https://youtu.be/VlU84cp0GD8)


## ✨ 주요 기능

- **교정 장치 착용 시간 측정**
    - BLE 센서와 압력 센서를 통해 교정 장치 착용 여부 및 시간을 측정합니다.
    - 교정기 착용을 앱으로 관리할 수 있습니다.
- **안면 상태 측정**
    - 인공지능을 통해 안면 상태를 자세히 분석합니다.
    - 변화 과정을 한 눈에 볼 수 있어 동기부여를 극대화할 수 있습니다.
- **티어 & 뱃지 시스템**
    - 교정기 착용 시간, 일일 미션 달성 등 여러 활동으로 경험치와 뱃지를 획득할 수 있습니다.
    - 사용자가 더 이상 교정을 치료로 느끼지 않고 재미 요소로 느낄 수 있도록 하기 위한 기능입니다.
- **병원 예약 관리**
    - 병원 예약 관리 및 길 찾기 기능을 제공합니다.
    - 병원에 예약을 하고, 찾아가기 위해 다른 어플을 쓸 필요가 없습니다.
- **약품 성분 검사**
    - 치아 이동에 영향을 주는 약품을 판별합니다.
    - 의사의 상담을 받기 힘든 상황에서 급히 약을 복용 해야 할 때 유용한 기능입니다.
- **챗봇 상담 & FAQ**
    - 앱 내에서 교정에 관한 대부분의 정보를 얻을 수 있습니다.
    - 교정 중 궁금한 사항이 생겨도 병원 진료 날짜까지 기다리지 않아도 됩니다.

<!--
---
## 🖥️ 동작 화면


| 온보딩 | 회원 가입 | 병원 등록 |
|:---------:|:--------:|:------------:|
| <img src="https://raw.githubusercontent.com/Donghyeon0915/DataRepository/master/Smiley/screenshot/gif/on_boarding.gif" width="250"/>| <img src="https://raw.githubusercontent.com/Donghyeon0915/DataRepository/master/Smiley/screenshot/gif/sign_up.gif" width="250"/> | <img src="https://raw.githubusercontent.com/Donghyeon0915/DataRepository/master/Smiley/screenshot/gif/add_hospital.gif" width="250"/> | 

| 홈화면 | 예약 목록 | 프로필 |
|:---------:|:--------:|:------------:|
| <img src="https://raw.githubusercontent.com/Donghyeon0915/DataRepository/master/Smiley/screenshot/home_screen.png" width="250"/>| <img src="https://raw.githubusercontent.com/Donghyeon0915/DataRepository/master/Smiley/screenshot/reserv_screen.png" width="250"/> |<img src="https://raw.githubusercontent.com/Donghyeon0915/DataRepository/master/Smiley/screenshot/profile_screen.png" width="250"/> | 

| 약품 검색 | 장치 검색 |
|:---------:|:--------:|
| <img src="https://raw.githubusercontent.com/Donghyeon0915/DataRepository/master/Smiley/screenshot/gif/medicine_search.gif" width="250"/> | <img src="https://raw.githubusercontent.com/Donghyeon0915/DataRepository/master/Smiley/screenshot/gif/bluetooth.gif" width="250"/> |
---
-->

<br>

## 💫 팀 협업 방식

- **Github Issue & Pull-Request** 관리를 통해 팀원 간의 업무 파악 및 진행 사항을 기록하고, 디자인, 프론트, 백엔드 파트들 간의 협업을 위해 **Figma, Swagger** 등의 툴을 사용하였습니다.
- **Github CI/CD**를 통해 통합 및 배포 환경을 자동화하였으며, 코딩 컨벤션을 맞추는 등의 방식으로 더 나은 코드를 작성할 수 있도록 노력하였습니다.

<br>

---

## 😀 새로 적용된 기술

### **1. Clean Architecture, Multi-Module**
<br>

**[ 문제 ]**

MVVM 패턴만 적용했을 때, 앱이 복잡해질 수록 코드의 종속성이 높아지고, 코드의 역할을 분리하는 것이 점점 더 어려워지는 것을 경험했습니다.

**[ 적용 ]**

- *MVVM 패턴 :** presentation layer와 logic layer의 분리를 위해 적용하였습니다.
- **Clean Architecture :** 각 계층을 적절하게 분리하여 의존성을 낮추었습니다.
- **Multi Module :** 각 기능을 모듈화하여 구조를 쉽게 파악할 수 있도록 하였습니다.

**[ 결과 ]**

계층 분리로 코드의 분리가 확실히 이루어져, 기능 추가에도 부담이 없는 구조로 만들 수 있었습니다. 또한 코드의 유지보수도 용이해졌습니다.

<br>
<br>

### **2. Github Actions를 통한 CI/CD**
<br>

**[ 문제 ]**

디자이너 및 멘토분과 프로젝트를 진행하면서 테스트 및 피드백 용도로 Apk 파일을 전송해야하는 상황이었습니다.

직접 Apk 파일을 추출해서 전송하는 방법은 매우 번거럽고 비효율적이기 때문에 Github Actions로 이를 자동화하기로 결정하였습니다.

**[ 적용 ]**

Github Actions을 통해 CI/CD를 적용하고, Github에 Release를 업로드하면 등록된 사용자에게 카카오톡 메세지가 전송되도록 구현하였습니다.

- **CI/CD 적용:** Github Actions를 사용하여 빌드, 테스트, 배포 자동화 작업을 수행하도록 조치하였습니다.
- **Release 업로드 시 카카오톡 메시지 발송:** Release Webhook과 Gmail 자동 전달을 사용하여 Release 업로드 시 등록된 사용자들에게 카카오톡 메시지가 전송되도록 연동하였습니다.

**[ 결과 ]**

자동화 된 작업으로 인해서 업무의 효율성이 높아졌으며, 이에 따라 업무의 복잡도도 감소되었습니다.
<br><br>

### **3. Coroutine-Flow**
<br>

**[ 문제 ]**
 
Coroutine-Flow를 이용하지 않고, 비동기 통신을 하는 경우 Callback이 중첩 되어 Callback Hell에 빠지는 상황이 발생했습니다. 

또한, 코드가 다수의 Callback과 Thread로 이루어져 유지 보수성도 낮아지게 되었습니다.

**[ 적용 ]**

Coroutine-Flow를 사용하여 네트워크 요청을 비동기적으로 처리하고, 비동기 처리 결과를 특정한 조건에 맞게 처리할 수 있도록 구현하였습니다.

**[ 결과 ]**

1. **가독성 증가 :** Callback Hell을 벗어나 Coroutine-Flow를 사용하면 코드가 간결해지기 때문에, 가독성이 향상되었습니다.
2. **개발 효율성 증가 :** Coroutine-Flow를 활용하여 네트워크 요청 처리를 비동기화하면서, 개발 효율성도 향상되었습니다.
<br> <br>

### **4. Hilt**
<br>

**[ 문제 ]**

Hilt를 사용하지 않고 직접 의존성을 관리하는 경우, 코드가 복잡해지며 유지 보수가 어려워지는 경험을 했습니다. 

Hilt를 사용하지 않으면, 초기화의 순서, 생명주기 관리 등이 모두 개발자가 직접 처리해야하므로 Hilt를 적용하기로 결정하였습니다.

**[ 적용 ]**

Hilt를 사용하여 의존성 관리를 효율적으로 처리하고 개발 프로세스에서 발생할 수 있는 문제점을 최소화하였습니다.
 
**[ 결과 ]**

1. **유지 보수성 증가 :** Hilt를 사용하면 복잡한 의존성 관리 코드를 줄일 수 있으며, 유지보수가 간편해졌습니다.
2. **가독성 증가 :** Hilt를 활용하여 객체 생성 과정을 최적화하여, 가독성을 향상시켰습니다.

<br>
<br>

### **5. StateFlow를 통한 UI 상태 관리**
<br>

**[ 문제 ]**
 
StateFlow와 State 패턴을 사용하지 않았을 때, UI 상태 처리가 복잡해지는 상황을 겪었습니다. 

통신 결과를 받아오기 위해 다수의 LiveData 변수를 생성하였고, 이는 곧 가독성 및 유지 보수성을 저하시키는 큰 원인이 되었습니다.

또한, 클린 아키텍쳐를 적용하면서 안드로이드 플랫폼에 종속적인 LiveData 대신 StateFlow를 사용하기로 결정하였습니다.

**[ 적용 ]**

1. **StateFlow로 UI 상태 전달 :** UI의 상태를 StateFlow를 사용하여 전달하고, 이를 통해 UI 업데이트를 제어하도록 구현하였습니다.
2. **State 패턴을 사용한 UI 상태 관리 :** 상태 패턴을 구현하여 각각의 상태가 처리해야 하는 로직을 캡슐화하여 UI 상태를 관리하였습니다.

**[ 결과 ]**

1. **상태 관리 효율성 증가 :** 복잡한 상태 관리도 쉬워졌으며, 상태가 추가 되거나 변경 되는 경우에 받는 영향을 최소화할 수 있었습니다.

<br><br>
---

## 🤗 트러블 슈팅

> 💡 **내부 DB를 활용한 네트워크 요청 최적화**

- 서버로의 트래픽 집중 문제를 해결하기 위해, 날짜별 착용 시간을 내부 DB에 저장하고 사용자가 앱에 접속 시 미 전송 데이터만 서버로 보내도록 로직을 개선했습니다.
- 결과적으로, 트래픽이 분산되어 서버의 대용량 트래픽 처리 능력이 향상되었습니다.


> 💡 **검색 시 서버 부하를 고려한 캐시 전략 (평균 응답 시간 2,000ms → 200ms)**

- 앱에서 공공 데이터 포탈로부터 많은 의약품 및 병원 정보를 불러와 초기 로딩 시간이 길고 서버 부담이 컸습니다.
- 이를 해결하기 위해 Retrofit을 사용해 데이터를 캐싱하고 expire time을 길게 주어 초기 응답 시간을 2,000ms에서 200ms로 대폭 줄였습니다.
- 이로 인해 서버의 부담이 감소하고 앱 성능이 개선되었습니다.


> 💡 **주변 병원 데이터 요청 최적화**

- 주변 치과 조회 기능에서 사용자의 위치가 변경되거나 지도가 움직일 때 주변 치과를 실시간으로 갱신해야 했습니다.
- 중복 요청을 최소화하기 위해 일정 반경 이상을 이동한 경우에만 갱신 요청을 보내도록 하였습니다.
- 또한, Coroutine과 Flow를 사용하여 비동기 처리를 통해 끊김 없이 화면을 유지하며 빠르게 데이터를 처리하였고, 결과적으로 사용자에게 부드러운 앱 경험을 제공하였습니다.


> 💡 **StateFlow 중간값 이슈**

- StateFlow를 사용해 UI 상태를 관리하던 중, 로딩 상태와 통신 결과 상태가 동시에 방출되어 로딩 값이 무시되는 현상이 발생했습니다.
- StateFlow가 중간값을 보장하지 않는다는 것이 원인이었음을 알게 되었습니다.
- 이를 해결하기 위해 로딩과 같은 UI 적인 부분은 상태 클래스로 만들지 않고 View에서 처리하도록 하여 문제를 해결했습니다.


## 📘 ERD

> 테이블 구조도

![Smiley Erd - Night](https://github.com/DEU-Smiley/Smiley-Android-App/assets/63500239/61cf1960-4310-4f45-b8cb-e040f8257e67)


## 👨‍ 👨‍👦팀 구성
<div align="center">

|김동현 ``` Android Dev ```| 김준 ```Backend Dev``` | 김성찬 ```Backend Dev``` |
|:-:|:-:|:-:|
|<img src="https://avatars.githubusercontent.com/u/63500239?v=4" width=130>| <img src="https://avatars.githubusercontent.com/u/30451538?v=4" width=130> | <img src="https://avatars.githubusercontent.com/u/115801420?v=4" width=130> |
|[@donghyeon0915](https://github.com/Donghyeon0915)| [@rlawns0327](https://github.com/rlawns0327)| [@tjdckscert](https://github.com/tjdckscert)|

</div>
