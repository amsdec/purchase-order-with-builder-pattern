package org.github.amsdec;

import java.util.function.BiConsumer;

public class PurchaseOrderStringParser {

    private final PurchaseOrderBuilder builder;

    public PurchaseOrderStringParser(final PurchaseOrderBuilder builder) {
        this.builder = builder;
    }

    public void parse(final String string) {
        if (string == null) {
            return;
        }

        this.parseLines(string);

    }

    private void parseLines(final String string) {
        final String[] lines = string.split("\n");

        for (int j = 0; j < lines.length; j++) {
            this.parseLine(j, lines[j]);
        }
    }

    private void parseLine(final int j, final String line) {
        final String[] columns = line.split("\\|");

        if (j == 0) {
            this.parseLineColumns(columns, this::parseHeaderColumn);
        }
        if (j == 1) {
            this.parseLineColumns(columns, this::parseRequestorColumn);
        }
        if (j == 2) {
            this.parseLineColumns(columns, this::parseProviderColumn);
        }
        if (j >= 3) {
            this.parseLineColumns(columns, this::parseProductColumn);
        }
    }

    private void parseLineColumns(final String[] columns, final BiConsumer<Integer, String> columnParser) {
        for (int i = 0; i < columns.length; i++) {
            if ("".equals(columns[i])) {
                continue;
            }

            columnParser.accept(i, columns[i]);
        }
    }

    private void parseHeaderColumn(final int i, final String column) {
        if (i == 0) {
            this.builder.addPurchaseOrderNumber(column);
        }
        if (i == 1) {
            this.builder.addDate(column);
        }
        if (i == 2) {
            this.builder.addDeliveryAddress(column);
        }
    }

    private void parseRequestorColumn(final int i, final String column) {
        if (i == 0) {
            this.builder.addRequestorRfc(column);
        }
        if (i == 1) {
            this.builder.addRequestorName(column);
        }
    }

    private void parseProviderColumn(final int i, final String column) {
        if (i == 0) {
            this.builder.addProviderRfc(column);
        }
        if (i == 1) {
            this.builder.addProviderName(column);
        }
    }

    private void parseProductColumn(final int i, final String column) {
        if (i == 0) {
            this.builder.addProduct(column);
        }
        if (i == 1) {
            this.builder.setProductQuantity(column);
        }
        if (i == 2) {
            this.builder.setProductDescription(column);
        }
    }
}
