package com.aubgteam.auctionhouse.Repositories;

        import com.aubgteam.auctionhouse.Models.Category;
        import com.aubgteam.auctionhouse.Models.Item;

        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.query.Param;
        import org.springframework.transaction.annotation.Transactional;

        import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {
        @Transactional
        void deleteByCategoryId(Long id);

}
