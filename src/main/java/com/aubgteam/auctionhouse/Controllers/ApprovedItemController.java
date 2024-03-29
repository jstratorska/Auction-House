package com.aubgteam.auctionhouse.Controllers;

import com.aubgteam.auctionhouse.Models.ApprovedItem;
import com.aubgteam.auctionhouse.Models.Item;
import com.aubgteam.auctionhouse.Services.ApprovedItemService;
import com.aubgteam.auctionhouse.Services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;

@Controller
public class ApprovedItemController {

    @Autowired
    private ApprovedItemService approvedItemService;
    @Autowired
    private ItemService itemService;



    @RequestMapping("/items")
    public String viewApprovedItems(Model model)
    {

        List<ApprovedItem> listOfItems = approvedItemService.listAll();
        model.addAttribute("listOfItems", listOfItems);
        return "items";
    }

    @RequestMapping("/admin/approve/{id}")
    public String showNewApprovedItemPage(Model model, @PathVariable(name = "id") long item_id
    ) {
        ApprovedItem approvedItem1 = new ApprovedItem();

        approvedItem1.setId(item_id);

        model.addAttribute("approvedItem", approvedItem1);
//        model.addAttribute("item", itemService.get(item_id));

//
        return "/admin/new_approved_item";
    }

    @RequestMapping(value = "/save_approved_item", method = RequestMethod.POST)
    public String saveApprovedItem(@ModelAttribute("approvedItem") ApprovedItem approvedItem1
    ) {
//        approvedItem.setApproved_item_id(aItem.getItem_id());
//        approvedItem.setApproved_item(aItem);
        Item item = itemService.get(approvedItem1.getId());
        approvedItem1.setApproved_item(item);
        approvedItemService.save(approvedItem1);
//        itemService.saveApprovedItem(item, approvedItem);
        return "redirect:/admin/approved_items/";
//        return (Long.toString(approvedItem.getApproved_item_id()));
    }

    @RequestMapping("/admin/edit_approved_item/{id}")
    public String editApprovedItem(Model model, @PathVariable (name ="id")long id)    {
        model.addAttribute("approvedItem", approvedItemService.get(id));
        return "/admin/edit_approved_item";
    }

    @RequestMapping("/admin/delete_approved_item/{id}")
    public String deleteApprovedItem(@PathVariable (name="id") long id)
    {
        approvedItemService.delete(id);
        return "redirect:/admin/approved_items/";
    }

    @RequestMapping("/admin/approved_items")
    public String viewApprovedItemHomePage(Model model) {
        List<ApprovedItem> listOfApprovedItems = approvedItemService.listAll();
        model.addAttribute("listOfApprovedItems", listOfApprovedItems);
        return "/admin/approved_items";
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam String keyword)
    { ModelAndView mav = new ModelAndView("search.html");
       List<ApprovedItem> result = approvedItemService.search(keyword);

        mav.addObject("result", result);
        return mav;
    }

}
