package id.ac.ui.cs.advprog.bidmart.catalog.repository;

import id.ac.ui.cs.advprog.bidmart.catalog.model.Listing;
import id.ac.ui.cs.advprog.bidmart.catalog.model.ListingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID>, JpaSpecificationExecutor<Listing> {

    List<Listing> findBySellerIdOrderByCreatedAtDesc(UUID sellerId);

    Page<Listing> findByCategoryId(UUID categoryId, Pageable pageable);

    Page<Listing> findByStatus(ListingStatus status, Pageable pageable);

    Page<Listing> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Modifying
    @Query("""
        UPDATE Listing l
        SET l.currentPrice = :newPrice,
            l.bidCount     = :bidCount
        WHERE l.id = :id
        """)
    int syncPrice(
            @Param("id")       UUID id,
            @Param("newPrice") BigDecimal newPrice,
            @Param("bidCount") int bidCount
    );

        @Query("""
            SELECT l FROM Listing l
            WHERE l.status = :status
              AND l.categoryId = COALESCE(:categoryId, l.categoryId)
              AND l.currentPrice >= COALESCE(:minPrice, l.currentPrice)
              AND l.currentPrice <= COALESCE(:maxPrice, l.currentPrice)
              AND (l.activatedAt IS NULL OR
               FUNCTION('TIMESTAMPADD', SECOND, l.auctionDuration, l.activatedAt)
               <= COALESCE(:endsBefore,
                       FUNCTION('TIMESTAMPADD', SECOND, l.auctionDuration, l.activatedAt)))
              AND (LOWER(l.title) LIKE LOWER(CONCAT('%', COALESCE(:keyword, ''), '%')) OR
               LOWER(COALESCE(l.description, '')) LIKE LOWER(CONCAT('%', COALESCE(:keyword, ''), '%')))
            """)
        Page<Listing> findByFilters(
            @Param("keyword")    String keyword,
            @Param("categoryId") UUID categoryId,
            @Param("status")     ListingStatus status,
            @Param("minPrice")   BigDecimal minPrice,
            @Param("maxPrice")   BigDecimal maxPrice,
            @Param("endsBefore") Instant endsBefore,
            Pageable pageable
        );

    boolean existsByIdAndSellerId(UUID id, UUID sellerId);
}
