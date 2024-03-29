package com.aubgteam.auctionhouse.Services;


import com.aubgteam.auctionhouse.Models.ApprovedItem;
import com.aubgteam.auctionhouse.Models.Item;
import com.aubgteam.auctionhouse.Repositories.ApprovedItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ApprovedItemService {

    @Autowired
    private ApprovedItemRepository approvedItemRepository;

    public List<ApprovedItem> listAll() {
        return approvedItemRepository.findAll();
    }

    public void save(ApprovedItem approvedItem) {
        approvedItemRepository.save(approvedItem);
    }

    public ApprovedItem get(long id) {
        return approvedItemRepository.findById(id).orElse(null);
    }

    public void delete(long id) {

        approvedItemRepository.deleteById(id);
    }

    public List<ApprovedItem> search(String keyword)
    {
        return approvedItemRepository.search(keyword);
    }

    public List<ApprovedItem> getSoldItems(java.sql.Date end_date){ return approvedItemRepository.soldItems(end_date);}

}
