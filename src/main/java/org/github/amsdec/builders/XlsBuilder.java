package org.github.amsdec.builders;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class XlsBuilder extends ExcelBuilder {

    public XlsBuilder() {
        super(new HSSFWorkbook());
    }

}
