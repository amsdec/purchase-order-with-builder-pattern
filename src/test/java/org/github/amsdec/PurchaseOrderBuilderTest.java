package org.github.amsdec;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import lombok.Getter;
import lombok.Setter;

public class PurchaseOrderBuilderTest implements PurchaseOrderBuilder {

    private final PurchaseOrder result = new PurchaseOrder();

    private PurchaseOrderStringParser parser;

    @Before
    public void setup() {
        this.parser = new PurchaseOrderStringParser(this);
    }

    @Test
    public void nullStringGeneratesNullResult() {
        this.parser.parse(null);

        Assert.assertEquals("", this.result.toString());
    }

    @Test
    public void emptyStringGeneratesEmptyResult() {
        this.parser.parse("");

        Assert.assertEquals("", this.result.toString());
    }

    @Test
    public void headerWithNumberOnlyGeneratesResultWithNumber() {
        this.parser.parse("10001");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "", this.result.toString());
    }

    @Test
    public void headerWithNumberOnlyWithPipeGeneratesResultWithNumber() {
        this.parser.parse("10001|");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "", this.result.toString());
    }

    @Test
    public void headerWithNumberAndDateOnlyGeneratesResultWithNumberAndDate() {
        this.parser.parse("10001|2021-10-22 17:13:31");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "", this.result.toString());
    }

    @Test
    public void headerWithNumberAndDateWithPipeOnlyGeneratesResultWithNumberAndDate() {
        this.parser.parse("10001|2021-10-22 17:13:31|");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "", this.result.toString());
    }

    @Test
    public void fullHeaderGeneratesResultWithFullHeder() {
        this.parser.parse("10001|2021-10-22 17:13:31|Ofificina");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "ADD:Ofificina\n" //
                + "", this.result.toString());
    }

    @Test
    public void fullHeaderAndEmptyRequestorGeneratesEmptyRequestor() {
        this.parser.parse("10001|2021-10-22 17:13:31|Ofificina\n");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "ADD:Ofificina\n" //
                + "", this.result.toString());
    }

    @Test
    public void fullHeaderAndRequestorRfcOnlyGeneratesRequestorWithRfc() {
        this.parser.parse("10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "ADD:Ofificina\n" //
                + "R-R:FOC140516174\n" //
                + "", this.result.toString());
    }

    @Test
    public void fullHeaderAndFullRequestorGeneratesFullRequestor() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "ADD:Ofificina\n" //
                + "R-R:FOC140516174\n" //
                + "R-N:Focaltec S.A.P.I. de C.V.\n" //
                + "", this.result.toString());
    }

    @Test
    public void fullHeaderAndFullRequestorAndProviderRfcOnlyGeneratesProviderWithRfc() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "ADD:Ofificina\n" //
                + "R-R:FOC140516174\n" //
                + "R-N:Focaltec S.A.P.I. de C.V.\n" //
                + "P-R:MOSA8311152G0\n" //
                + "", this.result.toString());
    }

    @Test
    public void fullHeaderAndFullRequestorAndFullProviderGeneratesFullProvider() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "ADD:Ofificina\n" //
                + "R-R:FOC140516174\n" //
                + "R-N:Focaltec S.A.P.I. de C.V.\n" //
                + "P-R:MOSA8311152G0\n" //
                + "P-N:Alberto Montellano Sandoval\n" //
                + "", this.result.toString());
    }

    @Test
    public void productWithIdOnlyGeneratesZeroQuantity() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "P1234567890");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "ADD:Ofificina\n" //
                + "R-R:FOC140516174\n" //
                + "R-N:Focaltec S.A.P.I. de C.V.\n" //
                + "P-R:MOSA8311152G0\n" //
                + "P-N:Alberto Montellano Sandoval\n" //
                + "P1-ID:P1234567890\n" //
                + "P1-Q:0\n" //
                + "P1-D:P1234567890\n" //
                + "", this.result.toString());
    }

    @Test
    public void productWithIdAndQuantityOnlyGeneratesWithQuantity() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "P1234567890|5");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "ADD:Ofificina\n" //
                + "R-R:FOC140516174\n" //
                + "R-N:Focaltec S.A.P.I. de C.V.\n" //
                + "P-R:MOSA8311152G0\n" //
                + "P-N:Alberto Montellano Sandoval\n" //
                + "P1-ID:P1234567890\n" //
                + "P1-Q:5\n" //
                + "P1-D:P1234567890\n" //
                + "", this.result.toString());
    }

    @Test
    public void fullProductGeneratesWithFullProduct() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "P1234567890|5|Caja de 100 tornillos");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "ADD:Ofificina\n" //
                + "R-R:FOC140516174\n" //
                + "R-N:Focaltec S.A.P.I. de C.V.\n" //
                + "P-R:MOSA8311152G0\n" //
                + "P-N:Alberto Montellano Sandoval\n" //
                + "P1-ID:P1234567890\n" //
                + "P1-Q:5\n" //
                + "P1-D:Caja de 100 tornillos\n" //
                + "", this.result.toString());
    }

    @Test
    public void twoProductsGeneratesWithTwoProducts() {
        this.parser.parse("" //
                + "10001|2021-10-22 17:13:31|Ofificina\n" //
                + "FOC140516174|Focaltec S.A.P.I. de C.V.\n" //
                + "MOSA8311152G0|Alberto Montellano Sandoval\n" //
                + "P1234567890|5|Caja de 100 tornillos\n" //
                + "P0000012345|5|Caja de 100 tuercas\n" //
                + "");

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "ADD:Ofificina\n" //
                + "R-R:FOC140516174\n" //
                + "R-N:Focaltec S.A.P.I. de C.V.\n" //
                + "P-R:MOSA8311152G0\n" //
                + "P-N:Alberto Montellano Sandoval\n" //
                + "P1-ID:P1234567890\n" //
                + "P1-Q:5\n" //
                + "P1-D:Caja de 100 tornillos\n" //
                + "P2-ID:P0000012345\n" //
                + "P2-Q:5\n" //
                + "P2-D:Caja de 100 tuercas\n" //
                + "", this.result.toString());
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

        Assert.assertEquals("" //
                + "N:10001\n" //
                + "D:2021-10-22 17:13:31\n" //
                + "ADD:Ofificina\n" //
                + "R-R:FOC140516174\n" //
                + "R-N:Focaltec S.A.P.I. de C.V.\n" //
                + "P-R:MOSA8311152G0\n" //
                + "P-N:Alberto Montellano Sandoval\n" //
                + "P1-ID:P1234567890\n" //
                + "P1-Q:5\n" //
                + "P1-D:Caja de 100 tornillos\n" //
                + "P2-ID:P0000012345\n" //
                + "P2-Q:5\n" //
                + "P2-D:Caja de 100 tuercas\n" //
                + "P3-ID:P0000056789\n" //
                + "P3-Q:5\n" //
                + "P3-D:Caja de 100 arandelas\n" //
                + "", this.result.toString());
    }

    @Override
    public void addPurchaseOrderNumber(final String number) {
        this.result.setNumber(number);
    }

    @Override
    public void addDate(final String date) {
        this.result.setDate(date);
    }

    @Override
    public void addDeliveryAddress(final String address) {
        this.result.setDeliveryAddress(address);
    }

    @Override
    public void addRequestorRfc(final String rfc) {
        this.result.setRequestorRfc(rfc);
    }

    @Override
    public void addRequestorName(final String requestor) {
        this.result.setRequestorName(requestor);
    }

    @Override
    public void addProviderRfc(final String rfc) {
        this.result.setProviderRfc(rfc);
    }

    @Override
    public void addProviderName(final String provider) {
        this.result.setProviderName(provider);
    }

    @Override
    public void addProduct(final String productId) {
        this.result.addProductId(productId);
    }

    @Override
    public void setProductQuantity(final String quantity) {
        this.result.addProductQuantity(quantity);
    }

    @Override
    public void setProductDescription(final String description) {
        this.result.addProductDescription(description);
    }

}

class PurchaseOrder {

    @Setter
    private String number;

    @Setter
    private String date;

    @Setter
    private String deliveryAddress;

    @Setter
    private String requestorRfc;

    @Setter
    private String requestorName;

    @Setter
    private String providerRfc;

    @Setter
    private String providerName;

    private final List<PurchaseOrderProduct> products = new ArrayList<>();

    private PurchaseOrderProduct currentProduct;

    public void addProductId(final String productId) {
        this.currentProduct = new PurchaseOrderProduct();
        this.currentProduct.setId(productId);
        this.currentProduct.setQuantity("0");
        this.currentProduct.setDescription(productId);

        this.products.add(this.currentProduct);
    }

    public void addProductQuantity(final String productQuantity) {
        this.currentProduct.setQuantity(productQuantity);
    }

    public void addProductDescription(final String productDescription) {
        this.currentProduct.setDescription(productDescription);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        if (this.number != null) {
            this.addData(result, "N:", this.number);
        }
        if (this.date != null) {
            this.addData(result, "D:", this.date);
        }
        if (this.deliveryAddress != null) {
            this.addData(result, "ADD:", this.deliveryAddress);
        }
        if (this.requestorRfc != null) {
            this.addData(result, "R-R:", this.requestorRfc);
        }
        if (this.requestorName != null) {
            this.addData(result, "R-N:", this.requestorName);
        }
        if (this.providerRfc != null) {
            this.addData(result, "P-R:", this.providerRfc);
        }
        if (this.providerName != null) {
            this.addData(result, "P-N:", this.providerName);
        }

        if (this.products != null) {
            for (int i = 0; i < this.products.size(); i++) {
                final int productIndex = i + 1;
                this.addData(result, String.format("P%s-ID:", productIndex), this.products.get(i).getId());
                this.addData(result, String.format("P%s-Q:", productIndex), this.products.get(i).getQuantity());
                this.addData(result, String.format("P%s-D:", productIndex), this.products.get(i).getDescription());
            }
        }

        return result.toString();
    }

    private void addData(final StringBuilder result, final String prefix, final String data) {
        result.append(prefix);
        result.append(data);
        result.append("\n");
    }

}

@Setter
@Getter
class PurchaseOrderProduct {

    private String id;

    private String quantity;

    private String description;
}
