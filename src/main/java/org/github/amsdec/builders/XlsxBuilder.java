package org.github.amsdec.builders;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxBuilder extends ExcelBuilder {

    public XlsxBuilder() {
        super(new XSSFWorkbook());
    }

}
