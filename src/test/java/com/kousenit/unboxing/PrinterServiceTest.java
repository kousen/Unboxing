package com.kousenit.unboxing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrinterServiceTest {
    private final PrinterService printerService = new PrinterService();

    @Test
    void printPdf() {
        String result = printerService.printPdf("src/main/resources/pg1661-images-3.epub");
        System.out.println(result);
        assertThat(result).containsAnyOf(
                "Printed",
                "No printer services available");
    }
}