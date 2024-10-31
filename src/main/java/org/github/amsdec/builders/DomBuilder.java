package org.github.amsdec.builders;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.github.amsdec.PurchaseOrderBuilder;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import lombok.Getter;

public class DomBuilder implements PurchaseOrderBuilder {

    @Getter
    private final Document doc;

    private final Element purchaseOrder;

    private Element requestor;

    private Element provider;

    private Element products;

    public DomBuilder() throws ParserConfigurationException {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();
        this.doc = db.newDocument();

        this.purchaseOrder = this.doc.createElement("PurchaseOrder");
        this.doc.appendChild(this.purchaseOrder);
    }

    @Override
    public void addPurchaseOrderNumber(final String number) {
        this.addAttributeToElement(this.purchaseOrder, "number", number);
    }

    @Override
    public void addDate(final String date) {
        this.addAttributeToElement(this.purchaseOrder, "date", date);
    }

    @Override
    public void addDeliveryAddress(final String address) {
        this.addAttributeToElement(this.purchaseOrder, "deliveryTo", address);
    }

    @Override
    public void addRequestorRfc(final String rfc) {
        this.requestor = this.doc.createElement("Requestor");
        this.purchaseOrder.appendChild(this.requestor);
        this.addAttributeToElement(this.requestor, "rfc", rfc);
    }

    @Override
    public void addRequestorName(final String name) {
        this.addAttributeToElement(this.requestor, "name", name);
    }

    @Override
    public void addProviderRfc(final String rfc) {
        this.provider = this.doc.createElement("Provider");
        this.purchaseOrder.appendChild(this.provider);
        this.addAttributeToElement(this.provider, "rfc", rfc);
    }

    @Override
    public void addProviderName(final String name) {
        this.addAttributeToElement(this.provider, "name", name);
    }

    @Override
    public void addProduct(final String productId) {
        if (this.products == null) {
            this.products = this.doc.createElement("Products");
            this.purchaseOrder.appendChild(this.products);
        }

        final Element product = this.doc.createElement("Product");
        this.products.appendChild(product);

        this.addAttributeToElement(product, "id", productId);
        this.addAttributeToElement(product, "quantity", "0");
        this.addAttributeToElement(product, "description", productId);
    }

    @Override
    public void setProductQuantity(final String quantity) {
        final Element product = (Element) this.products.getLastChild();

        this.addAttributeToElement(product, "quantity", quantity);
    }

    @Override
    public void setProductDescription(final String description) {
        final Element product = (Element) this.products.getLastChild();

        this.addAttributeToElement(product, "description", description);
    }

    private void addAttributeToElement(final Element element, final String attributeName, final String value) {
        final Attr attribute = this.doc.createAttribute(attributeName);
        attribute.setValue(value);
        element.setAttributeNode(attribute);
    }

}
