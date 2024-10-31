package org.github.amsdec.builders;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.github.amsdec.PurchaseOrderBuilder;

import lombok.Getter;

public class XlsBuilder implements PurchaseOrderBuilder {

    @Getter
    private final Workbook wb;

    private final Sheet sheet;

    private boolean productsAdded;

    public XlsBuilder() {
        this.wb = new HSSFWorkbook();
        this.sheet = this.wb.createSheet();
    }

    @Override
    public void addPurchaseOrderNumber(final String number) {
        this.addRowWithLabelAndValue("Número de orden de compra", number);
    }

    @Override
    public void addDate(final String date) {
        this.addRowWithLabelAndValue("Fecha", date);
    }

    @Override
    public void addDeliveryAddress(final String address) {
        this.addRowWithLabelAndValue("Entregar en", address);

    }

    @Override
    public void addRequestorRfc(final String rfc) {
        this.sheet.createRow(this.sheet.getLastRowNum() + 1);
        this.addTabledSection("Solicitante", "RFC", "Nombre");

        this.sheet.createRow(this.sheet.getLastRowNum() + 1);
        this.addTableCellOnLastRow(rfc);
    }

    @Override
    public void addRequestorName(final String requestor) {
        this.addTableCellOnLastRow(requestor);
    }

    @Override
    public void addProviderRfc(final String rfc) {
        this.sheet.createRow(this.sheet.getLastRowNum() + 1);
        this.addTabledSection("Proveedor", "RFC", "Nombre");

        this.sheet.createRow(this.sheet.getLastRowNum() + 1);
        this.addTableCellOnLastRow(rfc);
    }

    @Override
    public void addProviderName(final String provider) {
        this.addTableCellOnLastRow(provider);
    }

    @Override
    public void addProduct(final String productId) {
        if (!this.productsAdded) {
            this.sheet.createRow(this.sheet.getLastRowNum() + 1);
            this.addTabledSection("Productos", "ID", "Descripción", "Cantidad");
            this.productsAdded = true;
        }

        this.sheet.createRow(this.sheet.getLastRowNum() + 1);
        this.addTableCellOnLastRow(productId);
        this.addTableCellOnLastRow(productId);
        this.addTableCellOnLastRow("0");
    }

    @Override
    public void setProductQuantity(final String quantity) {
        final Row row = this.sheet.getRow(this.sheet.getLastRowNum());

        final Cell cell = row.getCell(2);
        cell.setCellValue(quantity);
    }

    @Override
    public void setProductDescription(final String description) {
        final Row row = this.sheet.getRow(this.sheet.getLastRowNum());

        final Cell cell = row.getCell(1);
        cell.setCellValue(description);
    }

    private void addRowWithLabelAndValue(final String label, final String value) {
        final Row row = this.sheet.createRow(this.sheet.getLastRowNum() + 1);

        this.addCellAndSetText(label, row);
        this.addCellAndSetText(value, row);
    }

    private void addTabledSection(final String sectionTitle, final String... tableHeaders) {
        this.addSectionTitle(sectionTitle);

        this.addTableHeaders(tableHeaders);
    }

    private void addSectionTitle(final String sectionTitle) {
        final Row sectionRow = this.sheet.createRow(this.sheet.getLastRowNum() + 1);

        this.addCellAndSetText(sectionTitle, sectionRow);
    }

    private void addTableHeaders(final String... headers) {
        final Row headerRow = this.sheet.createRow(this.sheet.getLastRowNum() + 1);

        for (final String header : headers) {
            this.addCellAndSetText(header, headerRow);
        }
    }

    private void addTableCellOnLastRow(final String value) {
        final Row row = this.sheet.getRow(this.sheet.getLastRowNum());

        this.addCellAndSetText(value, row);
    }

    private void addCellAndSetText(final String value, final Row row) {
        final Cell cell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum());
        cell.setCellValue(value);
    }

}
