# 계층형 쿼리
개발을 하다보면 메뉴구성, 조직도 등 같은 **테이블 내**에서 **계층적**으로 데이터를 처리해야 하는 경우가 많다.  
자주 사용 되기도 하고, 이번 프로젝트에서 이 부분에 대한 삽질을 많이 했기 때문에 정리를 해본다.  
계층형(hirarchical) 구조는 계급적, 수직적인 관계로 2진 트리 관계라고 할 수 있다.  
실습은 Oracle emp 를 이용하며 설치 여건이 안되므로 [**livesql**](https://livesql.oracle.com/)를 이용한다.  
사용법은 [**여기**](https://jeaha.dev/10?category=818108)서 보면 된다.

## 계층 구조와 용어
(계층 구조와 용어 이미지 만들어 올리기.)
- 노드 (node) : 테이블 상의 각각의 row를 노드라고 한다.
- 루트 (root) : 트리 구조의 최상위에 있는 노드를 의미한다.
- 부모 (parent) : 부모노드. 트리 구조에서 상위에 있는 노드를 부모라고 한다.
- 자식 (child) : 자식노드. 트리 구조에서 하위에 있는 노드를 자식이라고 한다.
- 리프 (leaf) : 리프노드 혹은 말단노드. 하위에 연결된 노드가 없는, 자식이 없는 노드이다.
- 레벨 (level) : 트리구조에서 각각의 계층을 의미한다. 루트부터 순차적으로 1씩 올라간다.
  
- - -
  
## SELF JOIN
테이블 개수가 1개이며 각 ROW가 상위 코드로 부모 자식 관계로 연결 되어 셀프 조인을 사용한다.  
최상단 노드의 경우 상위 코드가 NULL 이므로 외부 조인도 사용한다.

```SQL
SELECT
    C.ENAME
    , C.EMPNO
    , P.ENAME AS MGR_NN
FROM
    SCOTT.EMP C
INNER JOIN
    SCOTT.EMP P
ON
    C.MGR = P.EMPNO(+)
```
결과
```LOG
ENAME   EMPNO  MGR_NN
- - - - - - - - - - - -
BLAKE   7698    KING
CLARK   7782    KING
JONES   7566    KING
ALLEN   7499    BLAKE
WARD    7521    BLAKE
MARTIN  7654    BLAKE
TURNER  7844    BLAKE
JAMES   7900    BLAKE
MILLER  7934    CLARK
SCOTT   7788    JONES
FORD    7902    JONES
ADAMS   7876    SCOTT
SMITH   7369    FORD
KING    7839     - 
```

동일한 테이블 EMP 를 각각 C, P 로별칭을 주어 셀프 조인 하였고,  
C가 하위 ROW, P가 상위 ROW임을 알 수 있다.  
계층적인 결과가 나왔지만, 계층형 구조(TREE)와는 다른 결과이다.  

  
- - -
  
## START WITH & CONNECT BY
START WITH ... CONNECT BY 절은 계층형 정보를 표현하기 위한 목적으로 나온 조건이다.

문법
```SQL
SELECT
    [컬럼 ...]
FROM
    테이블
START WITH [최상위 조건]
CONNECT BY [NOCYCLE] [PRIOR] [계층 구조 조건];
```
예제
```SQL
SELECT
    C.ENAME
    , C.EMPNO
    , P.ENAME AS MGR_NN
FROM
    SCOTT.EMP C
INNER JOIN
    SCOTT.EMP P
ON
    C.MGR = P.EMPNO(+)
START WITH C.MGR IS NULL
CONNECT BY PRIOR C.EMPNO = C.MGR
```
결과
```LOG
ENAME   EMPNO  MGR_NN
- - - - - - - - - - - -
KING    7839     - 
JONES   7566    KING
SCOTT   7788    JONES
ADAMS   7876    SCOTT
FORD    7902    JONES
SMITH   7369    FORD
BLAKE   7698    KING
ALLEN   7499    BLAKE
WARD    7521    BLAKE
MARTIN  7654    BLAKE
TURNER  7844    BLAKE
JAMES   7900    BLAKE
CLARK   7782    KING
MILLER  7934    CLARK
```
**PRIOR** 키워드는 이전 결과의 컬럼을 참조하는 키워드로 현재 행의 MGR을 이전 행의 ENPNO와 연결한다는 의미이다.  
  
- - -
  
## LEVEL
계층 레벨(depth level)을 나타낸다.

```SQL
SELECT
    C.ENAME
    , C.EMPNO
    , P.ENAME AS MGR_NN
    , LEVEL
FROM
    SCOTT.EMP C
INNER JOIN
    SCOTT.EMP P
ON
    C.MGR = P.EMPNO(+)
START WITH C.MGR IS NULL
CONNECT BY PRIOR C.EMPNO = C.MGR;
```
```LOG
ENAME  EMPNO  MGR_NN  LEVEL
- - - - - - - - - - - - - - -
KING    7839     -      1
JONES   7566    KING    2
SCOTT   7788    JONES   3
ADAMS   7876    SCOTT   4
FORD    7902    JONES   3
SMITH   7369    FORD    4
BLAKE   7698    KING    2
ALLEN   7499    BLAKE   3
WARD    7521    BLAKE   3
MARTIN  7654    BLAKE   3
TURNER  7844    BLAKE   3
JAMES   7900    BLAKE   3
CLARK   7782    KING    2
MILLER  7934    CLARK   3
```

레벨별로 들여쓰기를 하면 쿼리 결과 볼때 편하다.
```SQL
SELECT
    LPAD( ' ', 2 * (LEVEL - 1 ) ) || C.ENAME AS ENAME
    , C.EMPNO
    , P.ENAME AS MGR_NN
    , LEVEL
FROM
    SCOTT.EMP C
INNER JOIN
    SCOTT.EMP P
ON
    C.MGR = P.EMPNO(+)
START WITH C.MGR IS NULL
CONNECT BY PRIOR C.EMPNO = C.MGR;
```
```LOG
ENAME       EMPNO  MGR_NN  LEVEL
- - - - - - - - - - - - - - - - -
KING         7839   -        1
  JONES      7566  KING      2
    SCOTT    7788  JONES     3
      ADAMS  7876  SCOTT     4
    FORD     7902  JONES     3
      SMITH  7369  FORD      4
  BLAKE      7698  KING      2
    ALLEN    7499  BLAKE     3
    WARD     7521  BLAKE     3
    MARTIN   7654  BLAKE     3
    TURNER   7844  BLAKE     3
    JAMES    7900  BLAKE     3
  CLARK      7782  KING      2
    MILLER   7934  CLARK     3
```
  
- - -
  
## ORDER SIBLINGS BY
계층형 쿼리에서 ORDER BY 절을 사용하면 계층의 상관 관계가가 유지되지 않은체 정렬이 되어 버린다.  
이 문제로 한참 삽질을 했었는데, **ORDER SIBLINGS BY**를 사용하면 계층 구조를 유지하면서 정렬을 할 수 있다.  

### 일반 ORDER BY를 사용했을 경우
```SQL
SELECT
    LPAD( ' ', 2 * (LEVEL - 1 ) ) || C.ENAME AS ENAME
    , C.EMPNO
    , P.ENAME AS MGR_NN
FROM
    SCOTT.EMP C
INNER JOIN
    SCOTT.EMP P
ON
    C.MGR = P.EMPNO(+)
START WITH C.MGR IS NULL
CONNECT BY NOCYCLE PRIOR C.EMPNO = C.MGR
ORDER BY EMPNO;
```
```LOG
ENAME         EMPNO  MGR_NN
- - - - - - - - - - - - - - -
      SMITH   7369    FORD
    ALLEN     7499    BLAKE
    WARD      7521    BLAKE
  JONES       7566    KING
    MARTIN    7654    BLAKE
  BLAKE       7698    KING
  CLARK       7782    KING
    SCOTT     7788    JONES
KING          7839     - 
    TURNER    7844    BLAKE
      ADAMS   7876    SCOTT
    JAMES     7900    BLAKE
    FORD      7902    JONES
    MILLER    7934    CLARK

```

### ORDER SIBLINGS BY를 할 경우
```SQL
SELECT
    C.ENAME
    , C.EMPNO
    , P.ENAME AS MGR_NN
FROM
    SCOTT.EMP C
INNER JOIN
    SCOTT.EMP P
ON
    C.MGR = P.EMPNO(+)
START WITH C.MGR IS NULL
CONNECT BY NOCYCLE PRIOR C.EMPNO = C.MGR
ORDER SIBLINGS BY EMPNO;
```
```LOG
ENAME         EMPNO  MGR_NN
- - - - - - - - - - - - - - -
KING          7839   - 
  JONES       7566  KING
    SCOTT     7788  JONES
      ADAMS   7876  SCOTT
    FORD      7902  JONES
      SMITH   7369  FORD
  BLAKE       7698  KING
    ALLEN     7499  BLAKE
    WARD      7521  BLAKE
    MARTIN    7654  BLAKE
    TURNER    7844  BLAKE
    JAMES     7900  BLAKE
  CLARK       7782  KING
    MILLER    7934  CLARK

```
  
- - -
  
## CONNECT_BY_ISLEAF
계층 구조에서 ROW의 최하위 레벨 여부의 반환이다.  
해당 ROW가 말단 노드라면 1 아니라면 0을 반환한다.  
```SQL
SELECT
    LPAD( ' ', 2 * (LEVEL - 1 ) ) || C.ENAME AS ENAME
    , C.EMPNO
    , P.ENAME AS MGR_NN
    , CONNECT_BY_ISLEAF LEAF
FROM
    SCOTT.EMP C
INNER JOIN
    SCOTT.EMP P
ON
    C.MGR = P.EMPNO(+)
START WITH C.MGR IS NULL
CONNECT BY NOCYCLE PRIOR C.EMPNO = C.MGR
ORDER SIBLINGS BY EMPNO;
```
```LOG
ENAME        EMPNO  MGR_NN  LEAF
- - - - - - - - - - - - - - - - -
KING          7839   -        0
  JONES       7566  KING      0
    SCOTT     7788  JONES     0
      ADAMS   7876  SCOTT     1
    FORD      7902  JONES     0
      SMITH   7369  FORD      1
  BLAKE       7698  KING      0
    ALLEN     7499  BLAKE     1
    WARD      7521  BLAKE     1
    MARTIN    7654  BLAKE     1
    TURNER    7844  BLAKE     1
    JAMES     7900  BLAKE     1
  CLARK       7782  KING      0
    MILLER    7934  CLARK     1

```
  
- - -
  
## CONNECT_BY_ROOT
계층구조 쿼리에서 LEVEL이 0인 최상위 로우의 정보를 얻어 올 수 있다.

```SQL
SELECT
    LPAD( ' ', 2 * (LEVEL - 1 ) ) || C.ENAME AS ENAME
    , C.EMPNO
    , P.ENAME AS MGR_NN
    , CONNECT_BY_ROOT C.ENAME ROOTNANME
FROM
    SCOTT.EMP C
INNER JOIN
    SCOTT.EMP P
ON
    C.MGR = P.EMPNO(+)
START WITH C.MGR IS NULL
CONNECT BY NOCYCLE PRIOR C.EMPNO = C.MGR
ORDER SIBLINGS BY EMPNO;
```
```LOG
ENAME        EMPNO  MGR_NN  ROOTNANME
- - - - - - - - - - - - - - - - - - - -
KING          7839   -        KING
  JONES       7566  KING      KING
    SCOTT     7788  JONES     KING
      ADAMS   7876  SCOTT     KING
    FORD      7902  JONES     KING
      SMITH   7369  FORD      KING
  BLAKE       7698  KING      KING
    ALLEN     7499  BLAKE     KING
    WARD      7521  BLAKE     KING
    MARTIN    7654  BLAKE     KING
    TURNER    7844  BLAKE     KING
    JAMES     7900  BLAKE     KING
  CLARK       7782  KING      KING
    MILLER    7934  CLARK     KING
```

## SYS_CONNECT_BY_PATH
계층 구조에서 ROOT노드부터 현제 노드까지의 PATH 정보를 가져올 수 있다.

```SQL
SELECT
    LPAD( ' ', 2 * (LEVEL - 1 ) ) || C.ENAME AS ENAME
    , C.EMPNO
    , P.ENAME AS MGR_NN
    , SYS_CONNECT_BY_PATH( C.ENAME, '/') PATH
FROM
    SCOTT.EMP C
INNER JOIN
    SCOTT.EMP P
ON
    C.MGR = P.EMPNO(+)
START WITH C.MGR IS NULL
CONNECT BY NOCYCLE PRIOR C.EMPNO = C.MGR
ORDER SIBLINGS BY EMPNO;
```
```LOG
ENAME         EMPNO  MGR_NN  PATH
- - - - - - - - - - - - - - - - - - - - - - - - - - - -
KING          7839     -      /KING
  JONES       7566    KING    /KING/JONES
    SCOTT     7788    JONES   /KING/JONES/SCOTT
      ADAMS   7876    SCOTT   /KING/JONES/SCOTT/ADAMS
    FORD      7902    JONES   /KING/JONES/FORD
      SMITH   7369    FORD    /KING/JONES/FORD/SMITH
  BLAKE       7698    KING    /KING/BLAKE
    ALLEN     7499    BLAKE   /KING/BLAKE/ALLEN
    WARD      7521    BLAKE   /KING/BLAKE/WARD
    MARTIN    7654    BLAKE   /KING/BLAKE/MARTIN
    TURNER    7844    BLAKE   /KING/BLAKE/TURNER
    JAMES     7900    BLAKE   /KING/BLAKE/JAMES
  CLARK       7782    KING    /KING/CLARK
    MILLER    7934    CLARK   /KING/CLARK/MILLER
```