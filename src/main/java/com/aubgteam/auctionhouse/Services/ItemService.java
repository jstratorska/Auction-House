package com.aubgteam.auctionhouse.Services;

import com.aubgteam.auctionhouse.Models.ApprovedItem;
import com.aubgteam.auctionhouse.Models.Image;
import com.aubgteam.auctionhouse.Models.Item;
import com.aubgteam.auctionhouse.Repositories.ApprovedItemRepository;
import com.aubgteam.auctionhouse.Repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ApprovedItemService approvedItemService;

    public List<Item> listAll() {
        return itemRepository.findAll();
    }

    public void save(Item item) {

        itemRepository.save(item);
    }

    public Item get(long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public void delete(long id)
    {
        itemRepository.deleteById(id);
    }

    public List<Item> search(String term, boolean pending)
    {
        term = term.toLowerCase();
        List<Item> listOfPendingItems = this.listAll();
        List<Item> listOfMatchedPendingItems = new ArrayList<>();
        for (Item item: listOfPendingItems) {
            if( item.getName().toLowerCase().contains(term)
                    || item.getDescription().toLowerCase().contains(term))
            {
                if((pending && approvedItemService.get(item.getId()) == null) || !pending)
                {
                    listOfMatchedPendingItems.add(item);
                }
            }
        }
        return listOfMatchedPendingItems;
    }
}
