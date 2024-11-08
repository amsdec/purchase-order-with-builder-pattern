package org.github.amsdec.builders;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.github.amsdec.PurchaseOrderBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import lombok.Getter;

public class HtmlBuilder implements PurchaseOrderBuilder {

    @Getter
    private final Document doc;

    private final Element body;

    private Element table;

    public HtmlBuilder() throws ParserConfigurationException {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();

        this.doc = db.newDocument();
        this.body = this.doc.createElement("body");

        final Element html = this.doc.createElement("html");
        html.appendChild(this.doc.createElement("head"));
        html.appendChild(this.body);
        this.doc.appendChild(html);
    }

    @Override
    public void addPurchaseOrderNumber(final String number) {
        this.addHeader("h1", number);
    }

    @Override
    public void addDate(final String date) {
        this.addHeader("h3", date);
    }

    @Override
    public void addDeliveryAddress(final String address) {
        this.addHeader("h3", String.format("Entregar en: %s", address));
    }

    @Override
    public void addRequestorRfc(final String rfc) {
        this.addRfc(rfc, "Solicitante");
    }

    @Override
    public void addRequestorName(final String name) {
        this.addName(name);
    }

    @Override
    public void addProviderRfc(final String rfc) {
        this.addRfc(rfc, "Proveedor");
    }

    @Override
    public void addProviderName(final String name) {
        this.addName(name);
    }

    @Override
    public void addProduct(final String productId) {
        if (!"table".equals(this.body.getLastChild().getNodeName())) {
            this.table = this.doc.createElement("table");
            this.body.appendChild(this.table);

            this.table.appendChild(this.doc.createElement("theader"));
            this.addRow("th", "ID", "Descripción", "Cantidad");

            this.table.appendChild(this.doc.createElement("tbody"));
        }

        this.addRow("td", productId, productId, "0");
    }

    @Override
    public void setProductQuantity(final String quantity) {
        this.body.getLastChild().getLastChild().getLastChild().getLastChild().setTextContent(quantity);
    }

    @Override
    public void setProductDescription(final String description) {
        this.body.getLastChild().getLastChild().getLastChild().getLastChild().getPreviousSibling()
                .setTextContent(description);
    }

    private void addHeader(final String tagName, final String format) {
        final Element header = this.doc.createElement(tagName);
        header.setTextContent(format);
        this.body.appendChild(header);
    }

    private void addRfc(final String rfc, final String tipo) {
        this.body.appendChild(this.doc.createElement("hr"));
        this.addHeader("h3", String.format("%s: %s", tipo, rfc));
    }

    private void addName(final String name) {
        final Node h3 = this.body.getLastChild();
        h3.setTextContent(String.format("%s - %s", h3.getTextContent(), name));
    }

    private void addRow(final String columnTag, final String... columnData) {
        final Element tr = this.doc.createElement("tr");
        this.table.getLastChild().appendChild(tr);

        for (final String data : columnData) {
            tr.appendChild(this.doc.createElement(columnTag));
            tr.getLastChild().setTextContent(data);
        }
    }

}
