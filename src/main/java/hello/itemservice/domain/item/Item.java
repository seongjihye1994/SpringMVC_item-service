package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;
    // Integer를 쓴 이유는?
    // 서비스상 price 가 null 이 될 수도 있기 떄문에.
    // 참고로 int 형에는 null 이 들어가지 못한다.
    // null 은 객체만 가능.

    public Item() {

    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
