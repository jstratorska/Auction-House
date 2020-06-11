package com.aubgteam.auctionhouse.Controllers;

import com.aubgteam.auctionhouse.Models.ApprovedItem;
import com.aubgteam.auctionhouse.Models.Image;
import com.aubgteam.auctionhouse.Models.Item;
import com.aubgteam.auctionhouse.Models.*;
import com.aubgteam.auctionhouse.Repositories.CategoryRepository;
import com.aubgteam.auctionhouse.Repositories.ItemRepository;
import com.aubgteam.auctionhouse.Repositories.UserRepository;
import com.aubgteam.auctionhouse.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ApprovedItemService approvedItemService;

    @Autowired
    private FollowService followService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MailService mailService;

    @RequestMapping({"/admin/items_admin/{pending}","/admin/items_admin/"})
    public String viewItemAsAdmin(Model model, @PathVariable(name="pending",required = false) boolean pending)
     {
         List<Item> listOfItems =  itemService.listAll();
         if(pending) {
             List<ApprovedItem> listOfApprovedItems = approvedItemService.listAll();

             for(ApprovedItem t: listOfApprovedItems)
             {
                 listOfItems.remove(itemService.get(t.getId()));
             }
         }

        model.addAttribute("pending", pending);
        model.addAttribute("term", "");
        model.addAttribute("listOfItems", listOfItems);
        model.addAttribute("imageService", imageService);

        return "/admin/items_admin";
    }

    @RequestMapping("/new")
    public String showNewItemPage(Model model)
    {
        Item item = new Item();
            model.addAttribute("item", item);
            model.addAttribute("categories", categoryRepository.findAll());
            return "new_item";
    }

    @RequestMapping(value = {"/save_item/"}, method = RequestMethod.POST)
    public String saveItem(@ModelAttribute("imagePath") MultipartFile imagePath, @ModelAttribute("item") Item item)
    {
        boolean newItem = false;
        try
        {
            String t= StringUtils.cleanPath(imagePath.getOriginalFilename());

            if(!t.equals("")) {
                long id = 0;
                if (item.getId() != 0 && itemService.get(item.getId()).getImage() != null) {
                    id = itemService.get(item.getId()).getImage().getId();
                    item.setSellerId(itemService.get(item.getId()).getSellerId());

                }
                else
                {
                    item.setSellerId(userService.findByUsername(userService.getLoggedInUsername()));
                    newItem = true;
                }
                Image savedImage = imageService.save(imagePath);
                item.setImage(savedImage);
                if (0 != id) {
                    imageService.delete(id);
                }

            }
            else
            {
                Item tempItem = itemService.get(item.getId());
                item.setImage(tempItem.getImage());
                item.setSellerId(tempItem.getSellerId());
            }

            itemService.save(item);
            if(newItem)
            {
                mailService.sendEmailToAdmins(userService.getAllAdmins(), item.getId());
            }

                return "redirect:/admin/items_admin/";

        }
        catch (Exception e) {
            return "redirect:/admin/items_admin/";
        }
    }

    @RequestMapping("/admin/edit/{id}")
    public ModelAndView showEditItemPage(@PathVariable(name = "id") long id) {
        ModelAndView mav = new ModelAndView("/admin/edit_item");
        Item item = itemService.get(id);
        mav.addObject("item", item);
        mav.addObject("categories", categoryRepository.findAll());
        return mav;
    }

    @RequestMapping("/admin/delete/{id}")
    public String deleteItem(@PathVariable(name = "id") int id) {
        itemService.delete(id);
        return "redirect:/admin/items_admin/";
    }

    @RequestMapping({"/admin/details/{id}"})
    public String viewDetailsOfItem(Model model, @PathVariable(name="id") int id)
    {
        Item item =  itemService.get(id);
        model.addAttribute("item", item);
        model.addAttribute("imageService", imageService);

        return "/admin/details_item";
    }

    @RequestMapping(value = "/admin/search_item/{pending}/{term}")
    public String showMatchedItems(Model model, @PathVariable (name = "term") String term, @PathVariable(name="pending") boolean pending)
    {
        List<Item> listOfMatchedApprovedItems = itemService.search(term, pending);

        model.addAttribute("pending",pending);
        model.addAttribute("term", term);
        model.addAttribute("imageService", imageService);
        model.addAttribute("listOfItems", listOfMatchedApprovedItems);
        return "/admin/items_admin";
    }

    @RequestMapping("/follow/{id}")
    public String followItem(@PathVariable(name="id") int id)
    {
        String username = userService.getLoggedInUsername();
        User user= userService.findByUsername(username);
        Item item = itemService.get(id);

        Follow follow = new Follow(user.getId(), item.getId());
        followService.save(follow)        ;
        return "redirect:/item/{id}";
    }

}