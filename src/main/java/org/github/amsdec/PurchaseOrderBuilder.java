package org.github.amsdec;

public interface PurchaseOrderBuilder {

    void addPurchaseOrderNumber(String number);

    void addDate(String date);

    void addDeliveryAddress(String address);

    void addRequestorRfc(String rfc);

    void addRequestorName(String requestor);

    void addProviderRfc(String rfc);

    void addProviderName(String provider);

    void addProduct(String productId);

    void setProductQuantity(String quantity);

    void setProductDescription(String description);
}
