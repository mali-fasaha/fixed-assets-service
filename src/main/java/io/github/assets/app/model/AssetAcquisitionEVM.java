package io.github.assets.app.model;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class AssetAcquisitionEVM implements Serializable {
    @ExcelRow
    private int rowIndex;

    @ExcelCell(0)
    private String acquisitionMonth;

    @ExcelCell(1)
    private String assetSerial;

    @ExcelCell(2)
    private String serviceOutletCode;

    @ExcelCell(3)
    private String acquisitionTransactionIdNumber;

    @ExcelCell(4)
    private String acquisitionTransactionDate;

    @ExcelCell(5)
    private String assetCategory;

    @ExcelCell(6)
    private String description;

    @ExcelCell(7)
    private double purchaseAmount;

    @ExcelCell(8)
    private String assetDealerName;

    @ExcelCell(9)
    private String assetInvoiceNumber;

    private long timestamp;
    private String messageToken;
}
