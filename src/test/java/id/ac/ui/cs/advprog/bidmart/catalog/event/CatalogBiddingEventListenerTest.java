package id.ac.ui.cs.advprog.bidmart.catalog.event;

import id.ac.ui.cs.advprog.bidmart.catalog.service.ListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class CatalogBiddingEventListenerTest {

    private ListingService listingService;
    private CatalogBiddingEventListener listener;

    @BeforeEach
    void setUp() {
        listingService = mock(ListingService.class);
        listener = new CatalogBiddingEventListener(listingService);
    }

    @Test
    void onBidPlaced_syncsListingPrice() {
        UUID listingId = UUID.randomUUID();
        BigDecimal price = new BigDecimal("7500000");

        listener.onBidPlaced(BidPlacedEvent.builder()
                .listingId(listingId)
                .currentPrice(price)
                .bidCount(12)
                .build());

        verify(listingService).syncPrice(listingId, price, 12);
    }

    @Test
    void onBidPlaced_ignoresIncompleteEvent() {
        listener.onBidPlaced(BidPlacedEvent.builder()
                .auctionId(UUID.randomUUID())
                .bidCount(12)
                .build());

        verify(listingService, never()).syncPrice(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyInt());
    }
}
