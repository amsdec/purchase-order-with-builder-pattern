package org.github.amsdec.builders;

import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.github.amsdec.PurchaseOrderStringParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class HtmlBuilderTest {

    private PurchaseOrderStringParser parser;

    private HtmlBuilder builder;

    @Before
    public void setup() throws ParserConfigurationException {
        this.builder = new HtmlBuilder();
        this.parser = new PurchaseOrderStringParser(this.builder);
    }

    @Test
    public void nullStringGeneratesNullResult() {
        this.parser.parse(null);

        this.assertHtml("" //
                + "<html>\n" //
                + "<head>\n" //
                + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" //
                + "</head>\n" //
                + "<body></body>\n" //
                + "</html>\n" //
                + "");
    }

    @Test
    public void emptyStringGeneratesEmptyResult() {
        this.parser.parse("");

        this.assertHtml("" //
                + "<html>\n" //
                + "<head>\n" //
                + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" //
                + "</head>\n" //
                + "<body></body>\n" //
                + "</html>\n" //
                + "");
    }

    @Test
    public void headerWithNumberOnlyGeneratesResultWithNumber() {
        this.parser.parse("10001");

        this.assertHtml("" //
                + "<html>\n" //
                + "<head>\n" //
                + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" //
                + "</head>\n" //
                + "<body>\n" //
                + "<h1>10001</h1>\n" //
                + "</body>\n" //
                + "</html>\n" //
                + "");
    }

    @Test
    public void headerWithNumberAndDateOnlyGeneratesResultWithNumberAndDate() {
        this.parser.parse("10001|2021-10-22 17:13:31");

        this.assertHtml("" //
                + "<html>\n" //
                + "<head>\n" //
                + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" //
                + "</head>\n" //
                + "<body>\n" //
                + "<h1>10001</h1>\n" //
                + "<h3>2021-10-22 17:13:31</h3>\n" //
                + "</body>\n" //
                + "</html>\n" //
                + "");
    }

    @Test
    public void fullHeaderGeneratesResultWithFullHeder() {
        this.parser.parse("10001|2021-10-22 17:13:31|Ofificina");

        this.assertHtml("" //
                + "<html>\n" //
                + "<head>\n" //
                + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" //
                + "</head>\n" //
                + "<body>\n" //
                + "<h1>10001</h1>\n" //
                + "<h3>2021-10-22 17:13:31</h3>\n" //
                + "<h3>Entregar en: Ofificina</h3>\n" //
                + "</body>\n" //
                + "</html>\n" //
                + "");
    }

    @Test
    public void fullHeaderAndRequestorRfcOnlyGeneratesRequestorWithRfc() {
        this.parser.parse("10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174");

        this.assertHtml("" //
                + "<html>\n" //
                + "<head>\n" //
                + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" //
                + "</head>\n" //
                + "<body>\n" //
                + "<h1>10001</h1>\n" //
                + "<h3>2021-10-22 17:13:31</h3>\n" //
                + "<h3>Entregar en: Ofificina</h3>\n" //
                + "<hr>\n" //
                + "<h3>Solicitante: FOC140516174</h3>\n" //
                + "</body>\n" //
                + "</html>\n" //
                + "");
    }

    @Test
    public void fullHeaderAndFullRequestorGeneratesFullRequestor() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.");

        this.assertHtml("" //
                + "<html>\n" //
                + "<head>\n" //
                + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" //
                + "</head>\n" //
                + "<body>\n" //
                + "<h1>10001</h1>\n" //
                + "<h3>2021-10-22 17:13:31</h3>\n" //
                + "<h3>Entregar en: Ofificina</h3>\n" //
                + "<hr>\n" //
                + "<h3>Solicitante: FOC140516174 - Focaltec S.A.P.I. de C.V.</h3>\n" //
                + "</body>\n" //
                + "</html>\n" //
                + "");
    }

    @Test
    public void fullHeaderAndFullRequestorAndFullProviderGeneratesFullProvider() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval");

        this.assertHtml("" //
                + "<html>\n" //
                + "<head>\n" //
                + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" //
                + "</head>\n" //
                + "<body>\n" //
                + "<h1>10001</h1>\n" //
                + "<h3>2021-10-22 17:13:31</h3>\n" //
                + "<h3>Entregar en: Ofificina</h3>\n" //
                + "<hr>\n" //
                + "<h3>Solicitante: FOC140516174 - Focaltec S.A.P.I. de C.V.</h3>\n" //
                + "<hr>\n" //
                + "<h3>Proveedor: MOSA8311152G0 - Alberto Montellano Sandoval</h3>\n" //
                + "</body>\n" //
                + "</html>\n" //
                + "");
    }

    @Test
    public void productWithIdOnlyGeneratesZeroQuantity() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "P1234567890");

        this.assertHtml("" //
                + "<html>\n" //
                + "<head>\n" //
                + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" //
                + "</head>\n" //
                + "<body>\n" //
                + "<h1>10001</h1>\n" //
                + "<h3>2021-10-22 17:13:31</h3>\n" //
                + "<h3>Entregar en: Ofificina</h3>\n" //
                + "<hr>\n" //
                + "<h3>Solicitante: FOC140516174 - Focaltec S.A.P.I. de C.V.</h3>\n" //
                + "<hr>\n" //
                + "<h3>Proveedor: MOSA8311152G0 - Alberto Montellano Sandoval</h3>\n" //
                + "<table>\n" //
                + "<theader>\n" //
                + "<tr>\n" //
                + "<th>ID</th><th>Descripci&oacute;n</th><th>Cantidad</th>\n" //
                + "</tr>\n" //
                + "</theader>\n" //
                + "<tbody>\n" //
                + "<tr>\n" //
                + "<td>P1234567890</td><td>P1234567890</td><td>0</td>\n" //
                + "</tr>\n" //
                + "</tbody>\n" //
                + "</table>\n" //
                + "</body>\n" //
                + "</html>\n" //
                + "");
    }

    @Test
    public void fullProductGeneratesWithFullProduct() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "P1234567890|5|Caja de 100 tornillos");

        this.assertHtml("" //
                + "<html>\n" //
                + "<head>\n" //
                + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" //
                + "</head>\n" //
                + "<body>\n" //
                + "<h1>10001</h1>\n" //
                + "<h3>2021-10-22 17:13:31</h3>\n" //
                + "<h3>Entregar en: Ofificina</h3>\n" //
                + "<hr>\n" //
                + "<h3>Solicitante: FOC140516174 - Focaltec S.A.P.I. de C.V.</h3>\n" //
                + "<hr>\n" //
                + "<h3>Proveedor: MOSA8311152G0 - Alberto Montellano Sandoval</h3>\n" //
                + "<table>\n" //
                + "<theader>\n" //
                + "<tr>\n" //
                + "<th>ID</th><th>Descripci&oacute;n</th><th>Cantidad</th>\n" //
                + "</tr>\n" //
                + "</theader>\n" //
                + "<tbody>\n" //
                + "<tr>\n" //
                + "<td>P1234567890</td><td>Caja de 100 tornillos</td><td>5</td>\n" //
                + "</tr>\n" //
                + "</tbody>\n" //
                + "</table>\n" //
                + "</body>\n" //
                + "</html>\n" //
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

        this.assertHtml("" //
                + "<html>\n" //
                + "<head>\n" //
                + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" //
                + "</head>\n" //
                + "<body>\n" //
                + "<h1>10001</h1>\n" //
                + "<h3>2021-10-22 17:13:31</h3>\n" //
                + "<h3>Entregar en: Ofificina</h3>\n" //
                + "<hr>\n" //
                + "<h3>Solicitante: FOC140516174 - Focaltec S.A.P.I. de C.V.</h3>\n" //
                + "<hr>\n" //
                + "<h3>Proveedor: MOSA8311152G0 - Alberto Montellano Sandoval</h3>\n" //
                + "<table>\n" //
                + "<theader>\n" //
                + "<tr>\n" //
                + "<th>ID</th><th>Descripci&oacute;n</th><th>Cantidad</th>\n" //
                + "</tr>\n" //
                + "</theader>\n" //
                + "<tbody>\n" //
                + "<tr>\n" //
                + "<td>P1234567890</td><td>Caja de 100 tornillos</td><td>5</td>\n" //
                + "</tr>\n" //
                + "<tr>\n" //
                + "<td>P0000012345</td><td>Caja de 100 tuercas</td><td>5</td>\n" //
                + "</tr>\n" //
                + "<tr>\n" //
                + "<td>P0000056789</td><td>Caja de 100 arandelas</td><td>5</td>\n" //
                + "</tr>\n" //
                + "</tbody>\n" //
                + "</table>\n" //
                + "</body>\n" //
                + "</html>\n" //
                + "");
    }

    private void assertHtml(final String expected) {
        Assert.assertEquals(expected, this.getXml(this.builder.getDoc()));
    }

    public String getXml(final Document doc) {
        try {
            final DOMSource domSource = new DOMSource(doc);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (final TransformerException e) {
            e.printStackTrace();
        }

        return "";
    }
}
