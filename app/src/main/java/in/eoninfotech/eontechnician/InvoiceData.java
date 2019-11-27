package in.eoninfotech.eontechnician;

import java.math.BigDecimal;
import java.util.Date;

import in.eoninfotech.eontechnician.Responses.LogDetail;

/**
 * Created by root on 13/10/18.
 */

public class InvoiceData  {

    public int id;
    public int invoiceNumber;
    public Date invoiceDate;
    public String customerName;
    public String customerAddress;
    public BigDecimal invoiceAmount;
    public BigDecimal amountDue;

}



