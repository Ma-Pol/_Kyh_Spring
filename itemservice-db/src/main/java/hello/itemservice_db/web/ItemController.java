package hello.itemservice_db.web;

import hello.itemservice_db.domain.Item;
import hello.itemservice_db.repository.ItemSearchCond;
import hello.itemservice_db.repository.ItemUpdateDto;
import hello.itemservice_db.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public String items(@ModelAttribute("itemSearch") ItemSearchCond itemSearch, Model model) {
        List<Item> items = itemService.findItems(itemSearch);

        model.addAttribute("items", items);

        return "items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemService.findById(itemId).get();

        model.addAttribute("item", item);

        return "item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemService.save(item);

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("added", true);

        return "redirect:/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemService.findById(itemId).get();

        model.addAttribute("item", item);

        return "editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(
            @PathVariable Long itemId,
            @ModelAttribute ItemUpdateDto updateParam,
            RedirectAttributes redirectAttributes
    ) {
        itemService.update(itemId, updateParam);

        redirectAttributes.addAttribute("edited", true);

        return "redirect:/items/{itemId}";
    }

}
