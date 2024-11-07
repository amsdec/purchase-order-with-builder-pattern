package org.github.amsdec.builders;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.github.amsdec.PurchaseOrderStringParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XlsxBuilderTest {

    private PurchaseOrderStringParser parser;

    private XlsxBuilder builder;

    @Before
    public void setup() {
        this.builder = new XlsxBuilder();
        this.parser = new PurchaseOrderStringParser(this.builder);
    }

    @Test
    public void nullStringGeneratesNullResult() {
        this.parser.parse(null);

        this.assertTable("");
    }

    @Test
    public void emptyStringGeneratesEmptyResult() {
        this.parser.parse("");

        this.assertTable("");
    }

    @Test
    public void headerWithNumberOnlyGeneratesResultWithNumber() {
        this.parser.parse("10001");

        this.assertTable("" //
                + "Número de orden de compra|10001"//
                + "");
    }

    @Test
    public void headerWithNumberAndDateOnlyGeneratesResultWithNumberAndDate() {
        this.parser.parse("10001|2021-10-22 17:13:31");

        this.assertTable("" //
                + "Número de orden de compra|10001\n"//
                + "Fecha|2021-10-22 17:13:31" //
                + "");
    }

    @Test
    public void fullHeaderGeneratesResultWithFullHeder() {
        this.parser.parse("10001|2021-10-22 17:13:31|Ofificina");

        this.assertTable("" //
                + "Número de orden de compra|10001\n"//
                + "Fecha|2021-10-22 17:13:31\n" //
                + "Entregar en|Ofificina" //
                + "");
    }

    @Test
    public void fullHeaderAndRequestorRfcOnlyGeneratesRequestorWithRfc() {
        this.parser.parse("10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174");

        this.assertTable("" //
                + "Número de orden de compra|10001\n"//
                + "Fecha|2021-10-22 17:13:31\n" //
                + "Entregar en|Ofificina\n" //
                + "\n" //
                + "Solicitante\n" //
                + "RFC|Nombre\n" //
                + "FOC140516174" //
                + "");
    }

    @Test
    public void fullHeaderAndFullRequestorGeneratesFullRequestor() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.");

        this.assertTable("" //
                + "Número de orden de compra|10001\n"//
                + "Fecha|2021-10-22 17:13:31\n" //
                + "Entregar en|Ofificina\n" //
                + "\n" //
                + "Solicitante\n" //
                + "RFC|Nombre\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V." //
                + "");
    }

    @Test
    public void fullHeaderAndFullRequestorAndFullProviderGeneratesFullProvider() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval");

        this.assertTable("" //
                + "Número de orden de compra|10001\n"//
                + "Fecha|2021-10-22 17:13:31\n" //
                + "Entregar en|Ofificina\n" //
                + "\n" //
                + "Solicitante\n" //
                + "RFC|Nombre\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "\n" //
                + "Proveedor\n" //
                + "RFC|Nombre\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval" //
                + "");
    }

    @Test
    public void productWithIdOnlyGeneratesZeroQuantity() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "P1234567890");

        this.assertTable("" //
                + "Número de orden de compra|10001\n"//
                + "Fecha|2021-10-22 17:13:31\n" //
                + "Entregar en|Ofificina\n" //
                + "\n" //
                + "Solicitante\n" //
                + "RFC|Nombre\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "\n" //
                + "Proveedor\n" //
                + "RFC|Nombre\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "\n" //
                + "Productos\n" //
                + "ID|Descripción|Cantidad\n" //
                + "P1234567890|P1234567890|0" //
                + "");
    }

    @Test
    public void fullProductGeneratesWithFullProduct() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "P1234567890|5|Caja de 100 tornillos");

        this.assertTable("" //
                + "Número de orden de compra|10001\n"//
                + "Fecha|2021-10-22 17:13:31\n" //
                + "Entregar en|Ofificina\n" //
                + "\n" //
                + "Solicitante\n" //
                + "RFC|Nombre\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "\n" //
                + "Proveedor\n" //
                + "RFC|Nombre\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "\n" //
                + "Productos\n" //
                + "ID|Descripción|Cantidad\n" //
                + "P1234567890|Caja de 100 tornillos|5" //
                + "");
    }

    @Test
    public void threeProductsGeneratesWithThreeProducts() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "P1234567890|5|Caja de 100 tornillos\n" //
                + "P0000012345|5|Caja de 100 tuercas\n" //
                + "P0000056789|5|Caja de 100 arandelas\n" //
                + "");

        this.assertTable("" //
                + "Número de orden de compra|10001\n"//
                + "Fecha|2021-10-22 17:13:31\n" //
                + "Entregar en|Ofificina\n" //
                + "\n" //
                + "Solicitante\n" //
                + "RFC|Nombre\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "\n" //
                + "Proveedor\n" //
                + "RFC|Nombre\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "\n" //
                + "Productos\n" //
                + "ID|Descripción|Cantidad\n" //
                + "P1234567890|Caja de 100 tornillos|5\n" //
                + "P0000012345|Caja de 100 tuercas|5\n" //
                + "P0000056789|Caja de 100 arandelas|5" //
                + "");
    }

    private void assertTable(final String expected) {
        Assert.assertEquals(expected, this.getTable(this.builder.getWb()));
    }

    public String getTable(final Workbook wb) {
        final Iterator<Row> rows = wb.getSheetAt(0).rowIterator();
        final StringBuilder table = new StringBuilder();

        while (rows.hasNext()) {
            final Row row = rows.next();

            final Iterator<Cell> cells = row.cellIterator();

            while (cells.hasNext()) {
                final Cell cell = cells.next();
                table.append(cell.getStringCellValue());
                if (cells.hasNext()) {
                    table.append("|");
                }
            }
            if (rows.hasNext()) {
                table.append("\n");
            }

        }

        return table.toString();
    }

}
