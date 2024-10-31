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

public class DomBuilderTest {

    private PurchaseOrderStringParser parser;

    private DomBuilder builder;

    @Before
    public void setup() throws ParserConfigurationException {
        this.builder = new DomBuilder();
        this.parser = new PurchaseOrderStringParser(this.builder);
    }

    @Test
    public void nullStringGeneratesNullResult() {
        this.parser.parse(null);

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder/>"//
                + "");
    }

    @Test
    public void emptyStringGeneratesEmptyResult() {
        this.parser.parse("");

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder/>"//
                + "");
    }

    @Test
    public void headerWithNumberOnlyGeneratesResultWithNumber() {
        this.parser.parse("10001");

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder number=\"10001\"/>"//
                + "");
    }

    @Test
    public void headerWithNumberOnlyWithPipeGeneratesResultWithNumber() {
        this.parser.parse("10001|");

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder number=\"10001\"/>"//
                + "");
    }

    @Test
    public void headerWithNumberAndDateOnlyGeneratesResultWithNumberAndDate() {
        this.parser.parse("10001|2021-10-22 17:13:31");

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder date=\"2021-10-22 17:13:31\" number=\"10001\"/>"//
                + "");
    }

    @Test
    public void fullHeaderGeneratesResultWithFullHeder() {
        this.parser.parse("10001|2021-10-22 17:13:31|Ofificina");

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder date=\"2021-10-22 17:13:31\" deliveryTo=\"Ofificina\" number=\"10001\"/>"//
                + "");
    }

    @Test
    public void fullHeaderAndRequestorRfcOnlyGeneratesRequestorWithRfc() {
        this.parser.parse("10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174");

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder date=\"2021-10-22 17:13:31\" deliveryTo=\"Ofificina\" number=\"10001\">" //
                + "<Requestor rfc=\"FOC140516174\"/>" //
                + "</PurchaseOrder>"//
                + "");
    }

    @Test
    public void fullHeaderAndFullRequestorGeneratesFullRequestor() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.");

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder date=\"2021-10-22 17:13:31\" deliveryTo=\"Ofificina\" number=\"10001\">" //
                + "<Requestor name=\"Focaltec S.A.P.I. de C.V.\" rfc=\"FOC140516174\"/>" //
                + "</PurchaseOrder>"//
                + "");
    }

    @Test
    public void fullHeaderAndFullRequestorAndFullProviderGeneratesFullProvider() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval");

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder date=\"2021-10-22 17:13:31\" deliveryTo=\"Ofificina\" number=\"10001\">" //
                + "<Requestor name=\"Focaltec S.A.P.I. de C.V.\" rfc=\"FOC140516174\"/>" //
                + "<Provider name=\"Alberto Montellano Sandoval\" rfc=\"MOSA8311152G0\"/>" //
                + "</PurchaseOrder>"//
                + "");
    }

    @Test
    public void productWithIdOnlyGeneratesZeroQuantity() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "P1234567890");

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder date=\"2021-10-22 17:13:31\" deliveryTo=\"Ofificina\" number=\"10001\">" //
                + "<Requestor name=\"Focaltec S.A.P.I. de C.V.\" rfc=\"FOC140516174\"/>" //
                + "<Provider name=\"Alberto Montellano Sandoval\" rfc=\"MOSA8311152G0\"/>" //
                + "<Products>" //
                + "<Product description=\"P1234567890\" id=\"P1234567890\" quantity=\"0\"/>" //
                + "</Products>" //
                + "</PurchaseOrder>"//
                + "");
    }

    @Test
    public void fullProductGeneratesWithFullProduct() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "P1234567890|5|Caja de 100 tornillos");

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder date=\"2021-10-22 17:13:31\" deliveryTo=\"Ofificina\" number=\"10001\">" //
                + "<Requestor name=\"Focaltec S.A.P.I. de C.V.\" rfc=\"FOC140516174\"/>" //
                + "<Provider name=\"Alberto Montellano Sandoval\" rfc=\"MOSA8311152G0\"/>" //
                + "<Products>" //
                + "<Product description=\"Caja de 100 tornillos\" id=\"P1234567890\" quantity=\"5\"/>" //
                + "</Products>" //
                + "</PurchaseOrder>"//
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

        this.assertXml("" //
                + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //
                + "<PurchaseOrder date=\"2021-10-22 17:13:31\" deliveryTo=\"Ofificina\" number=\"10001\">" //
                + "<Requestor name=\"Focaltec S.A.P.I. de C.V.\" rfc=\"FOC140516174\"/>" //
                + "<Provider name=\"Alberto Montellano Sandoval\" rfc=\"MOSA8311152G0\"/>" //
                + "<Products>" //
                + "<Product description=\"Caja de 100 tornillos\" id=\"P1234567890\" quantity=\"5\"/>" //
                + "<Product description=\"Caja de 100 tuercas\" id=\"P0000012345\" quantity=\"5\"/>" //
                + "<Product description=\"Caja de 100 arandelas\" id=\"P0000056789\" quantity=\"5\"/>" //
                + "</Products>" //
                + "</PurchaseOrder>"//
                + "");
    }

    private void assertXml(final String expected) {
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
