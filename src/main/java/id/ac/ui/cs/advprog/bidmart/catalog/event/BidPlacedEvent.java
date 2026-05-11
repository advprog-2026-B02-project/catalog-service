package id.ac.ui.cs.advprog.bidmart.catalog.event;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPlacedEvent {
    private UUID eventId;
    private UUID auctionId;
    private UUID listingId;

    @JsonAlias({"newPrice", "amount"})
    private BigDecimal currentPrice;

    private Integer bidCount;
    private LocalDateTime occurredAt;
}
