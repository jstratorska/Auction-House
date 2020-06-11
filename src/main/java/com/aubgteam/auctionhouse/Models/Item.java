package com.aubgteam.auctionhouse.Models;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.cache.spi.TimestampsRegion;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(schema = "auctiondb")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne
    @JoinColumn
    private Category category;

    private double reserve_price;

    @ManyToOne
    @JoinColumn
    private User sellerId;

    private double evaluation;

    @ManyToOne
    @JoinColumn
    private User highestBidder;

    private String description;

    private int paidFor = 0;

    @OneToOne(mappedBy = "approved_item",cascade = CascadeType.ALL)
    private ApprovedItem approvedItem;


    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn
    private Image image;



}
