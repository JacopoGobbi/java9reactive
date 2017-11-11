package com.inosphere.java9meetup;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;


public class MeetupTest {
    @Test
    public void shouldConsumeAllElements() {
        List<Integer> retailPrices = List.of(100, 200, 300, 400);
        List<Integer> expectedNetProfit = List.of(900, 1900, 2900, 3900);
        List<Integer> expectedRevenue = List.of(1000, 2000, 3000, 4000);

        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        TaxAndCostsProcessor<Integer, Integer> taxAndCostsProcessor = new TaxAndCostsProcessor<>(r -> r - 10);
        publisher.subscribe(taxAndCostsProcessor);

        BusinessSubscriber<Integer> netProfitSubscriber = new BusinessSubscriber<>(item -> item * 10, "Net profit");
        taxAndCostsProcessor.subscribe(netProfitSubscriber);

        BusinessSubscriber<Integer> revenue = new BusinessSubscriber<>(item -> item * 10, "Revenue");
        publisher.subscribe(revenue);

        retailPrices.forEach(publisher::submit);
        publisher.close();

        await().atMost(1, TimeUnit.SECONDS)
                .until(() -> assertThat(netProfitSubscriber.results).containsExactlyElementsOf(expectedNetProfit));

        await().atMost(1, TimeUnit.SECONDS)
                .until(() -> assertThat(revenue.results).containsExactlyElementsOf(expectedRevenue));
    }
}
