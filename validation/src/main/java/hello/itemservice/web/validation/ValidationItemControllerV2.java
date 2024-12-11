package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());

        return "validation/v2/addForm";
    }

    //    @PostMapping("/add")
    public String addItemV1(
            @ModelAttribute Item item,
            BindingResult bindingResult, // 반드시 @ModelAttribute~ 다음에 와야 한다.
            RedirectAttributes redirectAttributes
    ) {
        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError(
                    "item",
                    "itemName",
                    "상품 이름은 필수입니다"
            ));
        }
        if (item.getPrice() == null || item.getPrice() < 1_000 || item.getPrice() > 1_000_000) {
            bindingResult.addError(new FieldError(
                    "item",
                    "price",
                    "가격은 1,000 ~ 1,000,000 까지 허용합니다."
            ));
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError(
                    "item",
                    "quantity",
                    "수량은 최대 9,999 까지 허용합니다."
            ));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10_000) {
                bindingResult.addError(new ObjectError(
                        "item",
                        "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice
                ));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로 이동
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("added", true);

        return "redirect:/validation/v2/items/{itemId}";
    }

    //    @PostMapping("/add")
    public String addItemV2(
            @ModelAttribute Item item,
            BindingResult bindingResult, // 반드시 @ModelAttribute~ 다음에 와야 한다.
            RedirectAttributes redirectAttributes
    ) {
        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError(
                    "item", // 오류가 발생한 객체 이름
                    "itemName", // 오류가 발생한 필드
                    item.getItemName(), // 클라이언트에서 전달된 값(거절된 값) <= 사용자 입력 값을 저장
                    false, // 타입 오류인지, 검증 실패인지 구분하는 값
                    null, // 메시지 코드
                    null, // 메시지에서 사용하는 인자
                    "상품 이름은 필수입니다" // 기본 오류 메시지
            ));
        }
        if (item.getPrice() == null || item.getPrice() < 1_000 || item.getPrice() > 1_000_000) {
            bindingResult.addError(new FieldError(
                    "item",
                    "price",
                    item.getPrice(),
                    false,
                    null,
                    null,
                    "가격은 1,000 ~ 1,000,000 까지 허용합니다."
            ));
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError(
                    "item",
                    "quantity",
                    item.getQuantity(),
                    false,
                    null,
                    null,
                    "수량은 최대 9,999 까지 허용합니다."
            ));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10_000) {
                bindingResult.addError(new ObjectError(
                        "item",
                        null,
                        null,
                        "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice
                ));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로 이동
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("added", true);

        return "redirect:/validation/v2/items/{itemId}";
    }

    //    @PostMapping("/add")
    public String addItemV3(
            @ModelAttribute Item item,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError(
                    "item", // 오류가 발생한 객체 이름
                    "itemName", // 오류가 발생한 필드
                    item.getItemName(), // 클라이언트에서 전달된 값(거절된 값) <= 사용자 입력 값을 저장
                    false, // 타입 오류인지, 검증 실패인지 구분하는 값
                    new String[]{"required.item.itemName"}, // 메시지 코드
                    null, // 메시지에서 사용하는 인자
                    null // 기본 오류 메시지
            ));
        }
        if (item.getPrice() == null || item.getPrice() < 1_000 || item.getPrice() > 1_000_000) {
            bindingResult.addError(new FieldError(
                    "item",
                    "price",
                    item.getPrice(),
                    false,
                    new String[]{"range.item.price"},
                    new Object[]{1_000, 1_000_000},
                    null
            ));
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError(
                    "item",
                    "quantity",
                    item.getQuantity(),
                    false,
                    new String[]{"max.item.quantity"},
                    new Object[]{9999},
                    null
            ));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10_000) {
                bindingResult.addError(new ObjectError(
                        "item",
                        new String[]{"totalPriceMin"},
                        new Object[]{10000, resultPrice},
                        null
                ));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로 이동
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("added", true);

        return "redirect:/validation/v2/items/{itemId}";
    }

    //    @PostMapping("/add")
    public String addItemV4(
            @ModelAttribute Item item,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        // 검증 로직
        // ValidationUtils: Empty, 공백문자 같은 단순한 기능만 사용할 때 고려해볼만 함
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");

        if (item.getPrice() == null || item.getPrice() < 1_000 || item.getPrice() > 1_000_000) {
            bindingResult.rejectValue("price", "range", new Object[]{1_000, 1_000_000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10_000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 검증에 실패하면 다시 입력 폼으로 이동
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("added", true);

        return "redirect:/validation/v2/items/{itemId}";
    }

    private final ItemValidator itemValidator;

    //    @PostMapping("/add")
    public String addItemV5(
            @ModelAttribute Item item,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        // 검증 로직
        itemValidator.validate(item, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("added", true);

        return "redirect:/validation/v2/items/{itemId}";
    }

    @InitBinder // 이 컨트롤러에만 영향을 준다. 글로벌 설정: ItemServiceApplication.java
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(itemValidator);
    }

    /**
     * &#064;Validated:  검증기를 실행하라는 애노테이션 <br />
     * - WebDataBinder 에 addValidators 한 검증기를 찾아서 실행한다. <br />
     * - 여러 검증기가 등록되어 있다면 검증기의 supports() 메서드를 사용한다.
     */
    @PostMapping("/add")
    public String addItemV6(
            @Validated @ModelAttribute Item item,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        // 검증 로직
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("added", true);

        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        itemRepository.update(itemId, item);
        redirectAttributes.addAttribute("edited", true);

        return "redirect:/validation/v2/items/{itemId}";
    }

}

