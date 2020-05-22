# Java ArrayList Sort

## 개요

JS 만 만지다 JavaScript ArrayList를 sort 해야 하는 일이 생김.  
DB 애서 이미 ORDER BY 해서 꺼내온 두 개의 List를 merge 한 List 였는데,  
요소들은 항적 데이터 `{ 선박 ID(ship_id), 위치정보(geom), 항적 시간(date(초)) }` 로 이루어 져 있고, 시간으로 정렬 해야 했음.

```
  public class Record {
    String  tbship_id;
    String  geom;
    int     sec;
  }
```

하도 JS 만 만졌더니 간단한 로직인데 1시간 정도 삽질 했고 블로그에 남기기로 함.

## 방법 1

Record 객체를 Comparable 상속 받게 한뒤 compareTo 메소드를 오버라이드, 필요한 곳에서 정렬 메소드를 호출.
compareTo 메소드는 int를 반환해야 하며 **오름차순 정렬**일 경우, 다음을 반환해야 함.

- 기준점이 비교대상보다 크다면 양수.
- 기준점이 비교대상과 같다면 0.
- 기준점이 비교대상보다 작다면 음수.

Record.java

```
  public class Record implements Comparable<Record> {
    String  tbship_id;
    String  geom;
    int     sec;

    /* ... */

    @Override
    public int compareTo(Record o) {
      if( this.cal < o.cal ) return -1;
      else if (this.cal == o.cal ) return 0;
      else return 1;
    }
  }
```

TrackDAOImpl.java

```
  /* ... */
  Collections.sort(mergedList);
```

## 방법 2

정렬이 필요한 곳에서 Comparator 객체를 생성한 뒤 compare 메소드를 오버라이드 후, 정렬 메소드를 호출.  
compare 메소드는 앞의 방법과 같이 짜야함.

```
  Comparator<Record> compare = new Comparator<Record>() {

  @Override
  public int compare(Record prev, Record curr) {
    if( prev.getCal() < curr.getCal() ) return -1;
    else if (prev.getCal() == curr.getCal() ) return 0;
    else return 1;
    }
  };

  Collections.sort(mergedList, compare);
```

## 방법 3

Collection.sort() 메소드의 두번째 인자로 익명 객체를 넣어 줌..  
방법 1, 2를 보면 쉽게 이해할 수 있음.

```
  Collections.sort(mergedList, (Comparator<Record>) (Record prv, Record cur) -> {
    if( prv.getCal() < cur.getCal() ) return -1;
    else if (prv.getCal() == cur.getCal() ) return 0;
    else return 1;
  });
```

## 그 외

개인적으로는 세번째 방법이 낫겠다 싶은데 이유는 객체의 compareTo 메소드에 다른 항목에 대한 비교가 필요할 경우가 있기 때문.
