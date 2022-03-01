package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final 키워드 붙으면 생성자 바로 생성
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        // 상품 저장
        itemRepository.save(item);

        model.addAttribute("item", item);

        // 상품 저장 후 상품 상세 페이지로 이동
        return "basic/item";
    }

    /**
     * @ModelAttribute 를 사용하면
     * model.addAttribute("item", item)
     * 을 자동으로 설정해준다.
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {

        // @ModelAttribute 는
        // 1. 자동으로 객체를 생성해준다.
        // 2. model.addAttribute 를 자동으로 해준다.

        // 상품 저장
        itemRepository.save(item);

        // 상품 저장 후 상품 상세 페이지로 이동
        return "basic/item";
    }

    /**
     * @ModelAttribute 에서 () 를 생략하면
     * 스프링이 디폴트로 객체 이름의 첫 글자를 소문자로 바꿔서
     * model.addAttribute() 를 자동으로 해준다.
     *
     * 즉, 아래 코드에서는 Item 객체의 첫 글자를 소문자로 바꾼 item 변수를
     * model.addAttribute("item", item) 자동으로 생성해준다.
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, Model model) {

        // 상품 저장
        itemRepository.save(item);

        // 상품 저장 후 상품 상세 페이지로 이동
        return "basic/item";
    }

    /**
     * @ModelAttribute 자체도 생략 가능하다.
     *
     * 넘어오는 파라미터가 String이나 int 같은 단순 타입이면 @RequestParam 이 자동으로 적용되고,
     * 객체가 넘어오면 @ModelAttribute 가 자동으로 적용된다.
     */
    // @PostMapping("/add")
    public String addItemV4(Item item, Model model) {

        // 상품 저장
        itemRepository.save(item);

        // 상품 저장 후 상품 상세 페이지로 이동
        return "basic/item";
    }

    // @PostMapping("/add")
    public String addItemV5(Item item, Model model) {

        // 상품 저장
        itemRepository.save(item);

        // 상품 저장 후 상품 상세 페이지로 리다이렉트
        return "redirect:/basic/items/" + item.getId();
    }

    /**
     * RedirectAttributes 는 말 그대로 Redirect 할 때 속성(attribute)을 추가하는 것.
     * addItemV5 처럼 리턴 시 + getId 로 url에 직접 넣어 리다이랙트 하면
     * id 처럼 영문은 상관없지만, 한글과 같은 문자가 들어가면 인코딩이 되지 않아 위험하다.
     * 그래서 Redirect 할 때 파라미터를 같이 넘길 수 있는 RedirectAttribute 속성을
     * 사용하면 된다.
     *
     * redirectAttributes.addAttribute("key", value)
     * 여기서 key 값이 return url에 작성한 파라미터로 치환된다.
     * status 는 그 뒤에 쿼리 파라미터 형식으로 들어간다.
     *
     * /basic/items/1?status=true
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {

        // 상품 저장
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        // 상품 저장 후 상품 상세 페이지로 리다이렉트시 파라미터를 붙여서 리다이렉트
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);

        // 상세페이지 조회 컨트롤러 재호출 (리다이렉트)
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct // 스프링에서 제공하는 어노테이션 기반의 메소드 초기화 기능
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
