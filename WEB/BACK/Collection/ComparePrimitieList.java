
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Primitive Type을 담고 있는 두개의 List
 * 이미 있는 값 origin
 * 새로운 값 newbie
 * 를 비교해서, update 할 list와 insert할 list delete 할 list를 구분한다.
 */
public class ComparePrivitiveList {

  private static Logger logger = LoggerFactory.getLogger(ListTest.class);

  public static void main(String[] args) {
// -> Initialize
    String head = "AUS MSG ";

    //  기존 리스트
    List<String> origin = new ArrayList<>();
    for( int i = 0; i <= 7; i++ ) {
      origin.add( head + String.valueOf(i) );
    }
    logger.info("origin : {}, {}", origin.toString(), origin.size());

    //  새 리스트
    List<String> newbie = new ArrayList<>();
    for( int i = 5; i <= 10; i++ ) {
      newbie.add( head + String.valueOf(i) );
    }
    logger.info("newbie : {}, {}", newbie.toString(), newbie.size());


    //  업데이트 할 것
    List<String> toUpdate  = new ArrayList<>();

    //  삭제할 것
    List<String> toDelete = new ArrayList<>();

    //  인서트할 것
    List<String> toInsert = new ArrayList<>();

// <- Initialize

    for(String newb : newbie) {
      if( !origin.contains(newb)) {
        toInsert.add(newb);
      } else {
        toUpdate.add(newb);
      }
    }

    for(String orgn : origin) {
      if( !newbie.contains(orgn) ) {
        toDelete.add(orgn);
      }
    }

    logger.info("toInsert : {}, {}", toInsert, toInsert.size());
    logger.info("toUpdate : {}, {}", toUpdate, toUpdate.size());
    logger.info("toDelete : {}, {}", toDelete, toDelete.size());

  }
}
