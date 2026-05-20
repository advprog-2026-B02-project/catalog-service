package id.ac.ui.cs.advprog.bidmart.catalog.repository;

import id.ac.ui.cs.advprog.bidmart.catalog.model.Listing;
import id.ac.ui.cs.advprog.bidmart.catalog.model.ListingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
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

    boolean existsByIdAndSellerId(UUID id, UUID sellerId);
}
