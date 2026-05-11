package id.ac.ui.cs.advprog.bidmart.catalog.event;

import id.ac.ui.cs.advprog.bidmart.catalog.service.ListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogBiddingEventListener {

    private final ListingService listingService;

    @KafkaListener(topics = "auction.bid-placed", groupId = "catalog-service")
    public void onBidPlaced(BidPlacedEvent event) {
        if (event.getListingId() == null || event.getCurrentPrice() == null) {
            log.warn("Ignoring auction.bid-placed without listingId/currentPrice: auctionId={}", event.getAuctionId());
            return;
        }

        int bidCount = event.getBidCount() != null ? event.getBidCount() : 0;
        listingService.syncPrice(event.getListingId(), event.getCurrentPrice(), bidCount);
    }
}
