package hello.itemservice.web.item.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final 멤버변수를 모두 포함하는 생성자를 자동 생성
public class BasicItemController {
    private final ItemRepository itemRepository;

    @GetMapping("")
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();

        model.addAttribute("items", items);

        return "basic/items"; // resources/templates/basic/items.html
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        model.addAttribute("item", item);

        return "basic/item"; // resources/templates/basic/item.html
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm"; // resources/templates/basic/addForm.html
    }

    //    @PostMapping("/add")
    public String addItemV1(
            @RequestParam String itemName,
            @RequestParam Integer price,
            @RequestParam Integer quantity,
            Model model
    ) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item"; // resources/templates/basic/item.html
    }

    /**
     * &#064;ModelAttribute("item")  Item item <br />
     * model.addAttribute("item", item); 자동 추가
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        itemRepository.save(item);

//        model.addAttribute("item", item); // 자동 추가, 생략 가능

        return "basic/item"; // resources/templates/basic/item.html
    }

    /**
     * &#064;ModelAttribute name 생략 가능 <br />
     * model.addAttribute("item", item); 자동 추가 <br />
     * 생략 시, model에 저장되는 name은 클래스명 첫 글자만 소문자로 변경해 등록: Item -> item
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);

        return "basic/item"; // resources/templates/basic/item.html
    }

    /**
     * &#064;ModelAttribute 전체 생략 가능 <br />
     * model.addAttribute("item", item); 자동 추가 <br />
     * <br />
     * 새로 고침 시 무한 호출 문제 발생 -> PRG 패턴 적용
     */
//    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);

        return "basic/item"; // resources/templates/basic/item.html
    }

    /**
     * PRG 패턴: Post/Redirect/Get <br />
     * <br />
     * URL에 변수를 더해 사용하는 방식은 URL 인코딩이 되지 않기 때문에 권장하지 않는다. <br />
     * RedirectAttributes를 사용하자.
     */
//    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);

        return "redirect:/basic/items/" + item.getId();
    }

    /**
     * RedirectAttributes 사용
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("added", true);

        return "redirect:/basic/items/{itemId}"; // /basic/items/{itemId}?added=true
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        model.addAttribute("item", item);

        return "basic/editForm"; // resources/templates/basic/editForm.html
    }

    @PostMapping("/{itemId}/edit")
    public String edit(
            @PathVariable Long itemId,
            @ModelAttribute Item item,
            RedirectAttributes redirectAttributes
    ) {
        itemRepository.update(itemId, item);
        redirectAttributes.addAttribute("edited", true);

        return "redirect:/basic/items/{itemId}"; // /basic/items/{itemId}?edited=true
    }

    /**
     * 테스트용 데이터 추가 <br />
     * <br />
     * &#064;PostConstruct <br />
     * - 해당 빈의 의존관계가 모두 주입되고 난 뒤, 초기화 용도로 즉시 호출된다.
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }
}
