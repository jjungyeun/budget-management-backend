# PoGa (포가: 지출 및 예산 관리 서비스)

PoGa는 사용자가 예산 및 지출 관리를 통해 개인의 소비 습관을 파악하는 데 도움을 주는 서비스입니다.

- `PoGa`는 `Positive 가계부`의 줄임말입니다. 늘 예산을 초과하여 Negative한 가계부가 아닌, 절약을 잘 실천해서 Positive한 가계부를 만들어나가자는 의미입니다.

<br/>

## Table of Contents

- [개요](#개요)
- [Skils](#skils)
- [프로젝트 진행 및 이슈 관리](#프로젝트-진행-및-이슈-관리)
- [ERD](#erd)
- [API Reference](#api-reference)
- [Running Tests](#running-tests)
- [구현과정(설계 및 의도)](#구현과정설계-및-의도)
- [TIL 및 회고](#tiltoday-i-learn-및-회고)
- [References](#references)

<br/>

## 서비스 소개

- 예산을 직접 설정하고 그에 맞게 지출하는지 지속적으로 확인하며 개인의 소비습관 개선에 도움을 주는 서비스입니다.
- 설정된 예산 및 지금까지의 지출에 따라, 오늘 지출하면 좋을 금액을 추천해줍니다.
- 지난달 대비, 지난주 대비 오늘까지의 지출을 비교하는 통계를 제공합니다.

<br/>

## Skils

<div align=center> 
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/h2-4479A1?style=for-the-badge">

<br/>

<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/Github Actions-2088FF?style=for-the-badge&logo=Githubactions&logoColor=white">
<img src="https://img.shields.io/badge/AWS Ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">

<br/>

<img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white">
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
<img src="https://img.shields.io/badge/erd cloud-%23000000.svg?style=for-the-badge&logo=diagrams.net&logoColor=white">
</div>

<br/>

## 프로젝트 진행 및 이슈 관리

![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
<img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white">


### 요구사항 분석 및 일정 관리 - Notion

[요구사항 분석 - Link](https://wonwonjung.notion.site/7d25f7d956b74345b9bce7d973d951cf?pvs=4)

![task-management](https://github.com/jjungyeun/budget-management-backend/assets/29030538/0ffadc3e-b82a-495f-8c1b-78e86f0f36c0)

### 이슈 관리 - Github
![issue-management](https://github.com/jjungyeun/budget-management-backend/assets/29030538/57a012cb-5ddc-448e-baee-a9052fb66e3b)

<br/>

## ERD
![erd](https://github.com/jjungyeun/budget-management-backend/assets/29030538/f48aac0b-de6c-4feb-87ca-8d2a86c0e5e3)


<br/>

## API Reference

### 회원 API

<details>
<summary>회원 가입 - click</summary>

#### Request

`POST /api/members`

```json
{
  "account": "계정",
  "password": "비밀번호",
  "nickname": "닉네임",
  "push_option": true
}
```

| Field         | Type     | Description         |
|:--------------|:---------|:--------------------|
| `account`     | `string` | **(Required)** 계정   |
| `password`    | `string` | **(Required)** 비밀번호 |
| `nickname`    | `string` | **(Required)** 닉네임  |
| `push_option` | `string` | 푸시 옵션 동의 여부         |

#### Response

```text
200 OK
```

</details>

<details>
<summary>로그인 - click</summary>

#### Request

`POST /api/logins`

```json
{
  "account": "계정",
  "password": "비밀번호"
}
```

#### Response

```json
{
  "accesstoken": "eljlksadjfklsdajfsfklsajsdadsdasfsdaf"
}
```

</details>

<details>
<summary>사용자 정보 조회 - click</summary>

#### Request

`GET /api/members`

```text
Authentication: Bearer {JWT}
```

#### Response

```json
{
  "account": "test1234",
  "nickname": "닉네임",
  "push_option": false
}
```

</details>

<details>
<summary>사용자 설정 업데이트 - click</summary>

#### Request

`PUT /api/members`

```text
Authentication: Bearer {JWT}
```

```json
{
  "nickname": "닉네임",
  "push_option": false
}
```

#### Response

```json
{
  "account": "test1234",
  "nickname": "닉네임",
  "push_option": false
}
```

</details>

### 카테고리 API

<details>
<summary>카테고리 목록 조회 - click</summary>

#### Request

`GET /api/categories`

```text
Authentication: Bearer {JWT}
```

#### Response

```json
{
  "categories": [
    {
      "id": 1,
      "name": "식비"
    },
    {
      "id": 2,
      "name": "교통"
    }
  ]
}
```

</details>

### 예산 API

<details>
<summary>예산 조회 - click</summary>

#### Request

`GET /api/budgets`

```text
Authentication: Bearer {JWT}
```

#### Response

```json
{
  "budgets": [
    {
      "category_id": 1,
      "category_name": "식비",
      "amount": 300000
    },
    {
      "category_id": 2,
      "category_name": "교통",
      "amount": 150000
    }
  ]
}
```

</details>
<details>
<summary>예산 설정 - click</summary>

#### Request

`POST /api/budgets`

```text
Authentication: Bearer {JWT}
```

```json
{
  "budgets": [
    {
      "category_id": 1,
      "amount": 300000
    },
    {
      "category_id": 2,
      "amount": 150000
    }
  ]
}
```

#### Response

```text
200 OK
```

</details>

<details>
<summary>예산 추천 - click</summary>

#### Request

`GET /api/budgets/recommend`

```text
Authentication: Bearer {JWT}
```

| Query Parameter | Type  | Description                                          |
|:----------------|:------|:-----------------------------------------------------|
| `total_amount`  | `int` | **(Required)** 예산 총액. 해당 금액을 기준으로 각 카테고리의 예산을 추천해준다. |

#### Response

```json
{
  "budgets": [
    {
      "category_id": 1,
      "category_name": "식비",
      "amount": 300000
    },
    {
      "category_id": 2,
      "category_name": "교통",
      "amount": 150000
    }
  ]
}
```

</details>

### 지출 API

<details>
<summary>지출 추가 - click</summary>

#### Request

`POST /api/expenses`

```text
Authentication: Bearer {JWT}
```

```json
{
  "expended_at": "2023-11-09T17:12:00",
  "amount": 12000,
  "category": "식비",
  "memo": "점심으로 마라탕",
  "is_excluded_sum": false
}
```

| Field             | Type       | Description                                     |
|:------------------|:-----------|:------------------------------------------------|
| `expended_at`     | `datetime` | **(Required)** 지출 일시                            |
| `amount`          | `int`      | **(Required)** 지출 금액                            |
| `category`        | `string`   | **(Required)** 카테고리                             |
| `memo`            | `string`   | 메모                                              |
| `is_excluded_sum` | `boolean`  | 지출 합계 제외 여부. True인 경우 지출 목록 합계 또는 통계에 포함되지 않는다. |

#### Response

```text
200 OK
```

</details>
<details>
<summary>지출 수정 - click</summary>

#### Request

`PUT /api/expenses/{expense_id}`

```text
Authentication: Bearer {JWT}
```

```json
{
  "expended_at": "2023-11-09T17:12:00",
  "amount": 12000,
  "category": "식비",
  "memo": "점심으로 마라탕",
  "is_excluded_sum": false
}
```

| Field             | Type       | Description                                     |
|:------------------|:-----------|:------------------------------------------------|
| `expended_at`     | `datetime` | **(Required)** 지출 일시                            |
| `amount`          | `int`      | **(Required)** 지출 금액                            |
| `category`        | `string`   | **(Required)** 카테고리                             |
| `memo`            | `string`   | 메모                                              |
| `is_excluded_sum` | `boolean`  | 지출 합계 제외 여부. True인 경우 지출 목록 합계 또는 통계에 포함되지 않는다. |

#### Response

```text
200 OK
```

```json
{
  "expense_id": 10,
  "expended_at": "2023-11-09T18:12:00",
  "amount": 12500,
  "category": "식비",
  "memo": "저녁으로 마라탕",
  "is_excluded_sum": false
}
```

</details>
<details>
<summary>지출 삭제 - click</summary>

#### Request

`DELETE /api/expenses/{expense_id}`

```text
Authentication: Bearer {JWT}
```

#### Response

```text
200 OK
```

</details>

<details>
<summary>지출 상세 조회 - click</summary>

#### Request

`GET /api/expenses/{expense_id}`

```text
Authentication: Bearer {JWT}
```

#### Response

```text
200 OK
```

```json
{
  "expense_id": 10,
  "expended_at": "2023-11-09T18:12:00",
  "amount": 12500,
  "category": "식비",
  "memo": "저녁으로 마라탕",
  "is_excluded_sum": false
}
```

| Field             | Type       | Description                                     |
|:------------------|:-----------|:------------------------------------------------|
| `expense_id`      | `long`     | 지출 고유 아이디                                       |
| `expended_at`     | `datetime` | 지출 일시                                           |
| `amount`          | `int`      | 지출 금액                                           |
| `category`        | `string`   | 카테고리                                            |
| `memo`            | `string`   | 메모                                              |
| `is_excluded_sum` | `boolean`  | 지출 합계 제외 여부. True인 경우 지출 목록 합계 또는 통계에 포함되지 않는다. |

</details>
<details>
<summary>지출 목록 조회 - click</summary>

#### Request

`GET /api/expenses`

```text
Authentication: Bearer {JWT}
```

| Query Parameter | Type           | Description                                                                                                                      |
|:----------------|:---------------|:---------------------------------------------------------------------------------------------------------------------------------|
| `start_date`    | `date`         | **(Required)** 조회 시작 날짜                                                                                                          |
| `end_date`      | `date`         | **(Required)** 조회 종료 날짜                                                                                                          |
| `min_amount`    | `int`          | 조회할 최소 금액                                                                                                                        |
| `max_amount`    | `int`          | 조회할 최대 금액                                                                                                                        |
| `order_by`      | `string`       | 정렬 기준 `date`(날짜) 또는 `amount`(금액)와 정렬 방향 `asc`(오름차순) 또는 `desc`(내림차순)을 조합한 값 <br> ex) `date:asc` (날짜 오름차순), `amount:desc`(금액 내림차순) |
| `search`        | `string`       | 지출 메모에 검색어가 포함된 경우만 조회                                                                                                           |
| `category`      | `string array` | 해당 카테고리의 지출만 조회 (`,`로 구분) <br> value → 식비, 교통, 여가, 건강, 생활, 경조사, 기타 <br> ex) `category=교통,생활`                                     |

#### Response

```text
200 OK
```

```json
{
  "total_amount": 135000,
  "category_amounts": [
    {
      "category": "식비",
      "amount": 119000
    },
    {
      "category": "교통",
      "amount": 16000
    }
  ],
  "expenses": [
    {
      "expense_id": 111,
      "expended_at": "2023-11-09T11:30:00",
      "amount": 12000,
      "category": "식비",
      "memo": "점심으로 마라탕"
    },
    {
      "expense_id": 15,
      "expended_at": "2023-10-31T12:30:00",
      "amount": 17000,
      "category": "식비",
      "memo": "XX이랑 마라탕&꿔..."
    },
    {
      "expense_id": 12,
      "expended_at": "2023-10-31T11:50:00",
      "amount": 3700,
      "category": "교통",
      "memo": "마라탕 먹으러 강남..."
    }
  ]
}
```

| Field              | Type          | Description |
|:-------------------|:--------------|:------------|
| `total_amount`     | `int`         | 조회된 지출들의 총액 |
| `category_amounts` | `object list` | 카테고리별 지출 총액 |
| `expenses`         | `object list` | 각 지출의 정보    |

</details>

### 지출 컨설팅 API

<details>
<summary>오늘의 지출 추천 - click</summary>

#### Request

`GET /api/expenses/todays-recommend`

```text
Authentication: Bearer {JWT}
```

#### Response

```text
200 OK
```

```json
{
  "budget": 35000,
  "ment": "지출을 잘 조절하고 계세요! 앞으로도 화이팅!"
}
```

| Field          | Type     | Description           |
|:---------------|:---------|:----------------------|
| `today_budget` | `int`    | 예산을 만족하기 위해 오늘 지출할 금액 |
| `ment`         | `string` | 사용자의 예산/지출 상황에 맞는 멘트  |

</details>
<details>
<summary>오늘의 지출 안내 - click</summary>

#### Request

`GET /api/expenses/todays-feedback`

```text
Authentication: Bearer {JWT}
```

#### Response

```text
200 OK
```

```json
{
  "budget": 35000,
  "expense": 45000,
  "risk": 128,
  "categories": [
    {
      "category": "식비",
      "expense": 37000
    },
    {
      "category": "생활",
      "expense": 8000
    }
  ]
}
```

| Field     | Type            | Description           |
|:----------|:----------------|:----------------------|
| `budget`  | `int`           | 예산을 만족하기 위해 오늘 지출했어야 할 금액 |
| `expense` | `int`           | 오늘 사용한 총액  |
| `risk`    | `int (percent)` | 위험도: 오늘 지출했어야 할 금액 대비 오늘 실제로 지출한 금액 (%)  |
| `categories` | `object list`           | 카테고리별 오늘 지출 총액  |

</details>

### 통계 API

<details>
<summary>지난달 대비 지출 통계 - click</summary>

#### Request

`GET /api/statistics/month`

```text
Authentication: Bearer {JWT}
```

#### Response

```text
200 OK
```

```json
{
  "last_expense": 100000,
  "this_expense": 170000,
  "increase_rate": 170,
  "categories": [
    {
      "category": "식비",
      "last_expense": 37000,
      "this_expense": 45000,
      "increase_rate": 121
    }
  ]
}
```

| Field     | Type            | Description                                                     |
|:----------|:----------------|:----------------------------------------------------------------|
| `last_expense`  | `int`           | 지난 달 N일까지 사용한 총액                                                |
| `this_expense` | `int`           | 이번 달 오늘(N일)까지 사용한 총액                                            |
| `increase_rate`    | `int (percent)` | 지난달 대비 이번달에 사용한 금액의 증가율 (%)                                     |
| `categories` | `object list`           | 카테고리별 지난달 총액, 이번달 총액 및 증가율 <br> * 이번 달 오늘(N일)까지 지출이 있는 카테고리만 제공 |

</details>
<details>
<summary>지난 요일 대비 지출 통계 - click</summary>

#### Request

`GET /api/statistics/day-of-week`

```text
Authentication: Bearer {JWT}
```

#### Response

```text
200 OK
```

```json
{
  "last_expense": 100000,
  "this_expense": 170000,
  "increase_rate": 170,
  "categories": [
    {
      "category": "식비",
      "last_expense": 37000,
      "this_expense": 45000,
      "increase_rate": 121
    }
  ]
}
```

| Field     | Type            | Description                                                     |
|:----------|:----------------|:----------------------------------------------------------------|
| `last_expense`  | `int`           | 지난주 N요일에 사용한 총액                                            |
| `this_expense` | `int`           | 오늘(N요일) 사용한 총액                                            |
| `increase_rate`    | `int (percent)` | 지난주 N요일 대비 오늘 사용한 금액의 증가율 (%)                                     |
| `categories` | `object list`           | 카테고리별 지난주 N요일 총액, 오늘 총액 및 증가율 <br> * 오늘 지출이 있는 카테고리만 제공 |

</details>



<br/>

## Running Tests

> Test Result ScreenShot ![Static Badge](https://img.shields.io/badge/Test_Passed-36/36-green)
![test_result](https://github.com/jjungyeun/budget-management-backend/assets/29030538/b98f4a3b-ee16-4ecd-908b-99af88b52f18)

<br/>

## 구현과정(설계 및 의도)


<details>

<summary>DB Primary Key 선정 - click</summary>

#### 논의 사항

- 각 테이블의 unique한 컬럼(또는 컬럼의 조합)을 PK로 사용하는 것이 좋을지, 아니면 서비스 내부적으로만 사용되는 임의의 ID 컬럼을 추가하여 사용하는 것이 좋을지

#### 조사 내용

- 별도 ID 컬럼이 필요한 이유
  - 실무에서는 언젠가 요구조건이 바뀔 수 있기 때문에 비즈니스와 관련 없는 컬럼을 ID로 지정하는 것이 좋다.
  - 비즈니스와 관련된 컬럼을 PK로 지정하게 되면 해당 컬럼에 대한 요구조건이 변경될 경우 이를 FK로 사용하는 모든 테이블에 영향이 간다.
  - 단, 컬럼이 명확한 값을 가지며 PK조건을 완전히 만족하고, 미래에도 변할 가능성이 없다면 PK로 설정해도 좋다.
- Auto Increment ID
  - 분산형 시스템 사용 시 여러 데이터베이스 끼리 동기화가 잘 이루어지지 않으면, ID가 중복되어 생성될 수 있다.
  - 키를 예측하기 쉽기 때문에 SQL Injection 공격에 취약해질 수 있다.
- UUID
  - 100% unique한 것은 아니지만 충돌할 가능성이 굉장히 낮다. (10^-38 정도의 확률)
  - UUID를 사용하려면 BINARY(16), VARCHAR 등을 사용해야 한다.
    - int 타입을 쓰는 auto increment보다 메모리를 더 많이 차지하고, 서버에서 UUID를 생성해서 넣어줘야 하므로 INSERT 시간이 더 걸린다는 단점이 있다.

#### 결론

- 단일 DB라면 AUTO_INCREMENT 사용, 다중 DB를 사용하는 분산형 환경이면 데이터 일관성을 위해 UUID를 사용하는 것을 고려하는 것이 좋다.
- `보드미`에서는 단일 DB를 사용하며 id가 예측되는 상황에 예민하지 않기 때문에 Auto Increment ID를 PK로 사용하였다.
  - 단, 보드마다 보드 코드를 UUID로 생성하여 보드에 접근하는 URL에는 숫자 키가 아닌 코드를 사용하였다.

</details>

<details>
<summary>엔티티 생성 방식 - click</summary>

#### 가능한 방식

1. 생성자
  - 모든 필드를 포함한 생성자를 만들어 사용한다.
  - 필드가 많은 경우 생성자 사용시 인자를 넣는 순서가 헷갈릴 수 있다.
2. static 생성 메서드
  - 엔티티 생성 과정에서 비즈니스 로직이 들어가거나, 의미있는 메서드 이름이 필요한 경우세 사용한다.
  - 1번 방식과 마찬가지로 파라미터가 많으면 사용에 어려움을 느낄 수 있다.
3. Builder
  - builder 패턴을 활용하여 파라미터가 많은 경우 사용성을 높일 수 있다.
    - Lombok의 `@Builder`를 사용하여 간편하게 만들 수 있다.

#### 결론

- 필드가 적은 경우(2개 이하)에만 생성자 방식을 사용하고 그 외에는 모두 Builder 방식을 사용하기로 결정하였다.
- 생성자(Builder 포함) 파라미터에서 DB의 PK가 되는 id를 제외하여 개발자가 실수로 id를 지정하지 않도록 하였다.
- 생성자(Builder 포함) 내에 엔티티 필드의 유효성을 검증하는 로직을 추가하였다.
  - ex) 보드의 `startDate`는 `endDate` 보다 이후일 수 없다.

</details>


<details>
<summary>Custom 예외 클래스 구현 - click</summary>

- 서버단의 예상치 못한 오류를 제외하고, 서비스 로직이 실행되는 동안 발생하는 예측 가능한 예외가 발생하는 경우에 동일한 형식의 응답을 던져주고자 하였다.
  - 클라이언트와 공유된 예외 코드를 사용하여, 클라이언트에서 각 상황에 대한 대처를 할 수 있다.
  - 문제 상황이 발생했을 때 서버 오류로 인식되지 않고 클라이언트 오류라는 것을 알릴 수 있다.
- 생성자로 예외 코드를 전달받는 CustomException 클래스를 구현하고, 해당 예외가 발생했을 때 ExceptionHandler를 통해 동일한 형식의 응답을 반환한다.
  - CustomException에서 사용하는 예외코드는 응답 메시지를 포함한 Enum 클래스를 정의하여, 필요 시 계속해서 값을 추가할 수 있도록 하였다.

```java
@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;
    private final HttpStatus status;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }
}


public enum ErrorCode {
  DUPLICATE_ACCOUNT(HttpStatus.BAD_REQUEST, "중복된 계정입니다."),
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
  LOGIN_FAILED(HttpStatus.BAD_REQUEST, "잘못된 아이디 또는 비밀번호입니다."),

  // AUTH
  EXPIRE_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
  EMPTY_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST, "인증헤더가 비어있습니다.")
}
```

</details>

<br/>


## TIL(Today I Learn) 및 회고

<details>
<summary>QueryDsl로 다이나믹 쿼리 생성하기 - click</summary>

- 현 서비스의 `지출 목록 조회` API에서는 다양한 검색 조건을 사용한다.
  - 검색어, 카테고리 분류, 최대/최소 금액 등의 조건이 들어갈수도 안들어갈 수도 있다.
  - JPQL이나 Native Query를 사용하면 조건이 있는 경우와 없는 경우를 나누어서 query string을 조합해야 한다.
- QueryDsl에서는 BooleanExpression라는 조건식 클래스를 이용해서 조건이 있든 없든 간편하게 where절을 사용할 수 있다.
```java
public List<Expense> getExpense(String search, List<String> categories) {
        return queryFactory.selectFrom(expense)
                .where(
                    memoLike(search),
                    categoryIn(categories)
                )
                .fetch();
    }

private BooleanExpression memoLike(String search) {
        return StringUtils.hasText(search) ? expense.memo.contains(search) : null;
        }

private BooleanExpression categoryIn(List<String> categories) {
        return categories.isEmpty() ? null : category.name.in(categories);
        }
```
</details>

<details>
<summary>Java Record의 활용 - click</summary>

- Java 11을 사용할 때는 DTO같은 VO 클래스가 필요해도 Lombok의 @Data를 붙이거나 몇몇 어노테이션을 추가해 사용하였다.
- 코틀린을 사용했을 때는 data class를 잘 활용했었기에 Java에서도 16버전부터 정식으로 추가된 Record를 활용해보았다.
- 클래스명 뒤에 소괄호로 클래스의 필드를 선언한다.
  - 필드는 final로 선언되며, 자동적으로 모든 필드를 포함한 생성자 및 각 필드에 대한 조회 메소드가 생성된다.
```java
public record MemberDetailDto(
        String account,
        String nickname,
        Boolean pushOption
) { }
```

</details>

<details>
<summary>카테고리별 예산 평균 계산의 비동기 처리 - click</summary>

- 현재 작성된 코드는 사용자가 예산을 설정할 때마다 카테고리별 비율을 계산하여 카테고리 테이블을 업데이트 한다.
- 카테고리별 예산 평균의 경우 실시간성이 중요한 정보가 아니기 때문에 비동기처리하여 사용자가 예산 설정 시 계산 과정을 기다리지 않도록 할 수 있다.
- **추후 구현 예정**

</details>

<details>
<summary>JWT 인증 필터 적용 - click</summary>

- 현재 작성된 코드는 인증이 필요한 API에 인증용 어노테이션을 붙여 JWT로부터 사용자 정보를 알아낸다.
- 본 서비스에서는 회원가입 및 로그인을 제외하고 모든 API에 인증이 필요하므로, 전체 API에 인증을 적용하고 두 API를 제외하는 것이 알맞다.
  - 인증 어노테이션을 깜빡하는 실수를 방지할 수 있으며, 매번 어노테이션을 다는 반복 코드도 줄일 수 있다.
- 서블릿 필터로 인증 필터를 만들고 두 API만 제외하여 적용하면 앞으로 추가되는 API에도 자동으로 인증을 적용할 수 있다.
- **추후 구현 예정**

</details>

<br/>


## References

- [Spring Boot 3.x.x에 QueryDsl 설정하기](https://www.inflearn.com/questions/779498/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8-3-0-querydsl-%EC%84%A4%EC%A0%95-%EA%B4%80%EB%A0%A8)
- [스프링에서 @Async로 비동기처리하기](https://springboot.tistory.com/38)
- [Spring Scheduler 테스트하기](https://silvergoni.tistory.com/entry/use-awaitility%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC-%EB%94%9C%EB%A0%88%EC%9D%B4-%ED%85%8C%EC%8A%A4%ED%8A%B8%ED%95%98%EA%B8%B0)


