package com.example.srttata.home;

public class DataPojo {
    private Results[] results;

    private String status;

    public Results[] getResults ()
    {
        return results;
    }

    public void setResults (Results[] results)
    {
        this.results = results;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+", status = "+status+"]";
    }

    public Count[] count;

    public Count[] getCount() {
        return count;
    }

    public class Count
    {
        private String count;

        private _id _id;

        public String getCount ()
        {
            return count;
        }

        public void setCount (String count)
        {
            this.count = count;
        }

        public _id get_id ()
        {
            return _id;
        }

        public void set_id (_id _id)
        {
            this._id = _id;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [count = "+count+", _id = "+_id+"]";
        }
        public class _id
        {
            private String ageing;

            public String getAgeing ()
            {
                return ageing;
            }

            public void setAgeing (String ageing)
            {
                this.ageing = ageing;
            }

            @Override
            public String toString()
            {
                return "ClassPojo [ageing = "+ageing+"]";
            }
        }


    }

    public class Results
    {
        private String alarmInt;

        private String ageing;

        private String contactPhones;

        private String orderType;

        private String contactFullAddress;

        private String otherCharges;

        private String totalOrderValue;

        private String additionalSrtOffer1;

        private String quotationChange;

        private String dmsPaymentsReceived;

        private String additionalSrtOffer2;

        private String totalSrtOffer1;

        private String corporateSrtOffer2;

        private String totalSrtOffer2;

        private String corporateSrtOffer1;

        private Docs[] docs;

        private String financeExecutive;

        private String bookingStatus;

        private String finalPaymentReceived;

        private String paymentReceivedDate;

        private String nomineePanGst;

        private String opportunity;

        private String pendingDocsCount;

        private String quantityInvoiced;

        private String lob;

        private String financier;

        private String financeOption;

        private String cashSrtOffer1;

        private String allocatedQuantity;

        private String insuranceType1;

        private String financeAmount;

        private String cashSrtOffer2;

        private String insuranceType2;

        private String approximatepaymentDate;

        private String othersTataOffer;

        private String quantityRequested;

        private String rm2;

        private String rm1;

        private String rm3;

        private String cashTataOffer;

        private String ca;

        private String crnNumber;

        private String log_id;

        private String nomineeRelationship;

        private String totalTataOffer;

        private String corporateTataOffer;

        private String financedBy;

        private String team;

        private String parentProductLine;

        private String totalPaymentsReceived;

        private String othersHeading;

        private String exchangeSrtOffer1;

        private String accountPhone;

        private String additionalTataOffer;

        private String exchangeSrtOffer2;

        private String messageCount;

        private String dmsOrderValue;

        private String exchangeTataOffer;

        private String accountFullAddress;

        private String salesTeam;

        private String productLine;

        private String customerType;

        private String varient;

        private String customerEmail;

        private String __v;

        private String stockStatus;

        private String orderNo;

        private String othersSrtOffer2;

        private String contactName;

        private String accountType;

        private String othersSrtOffer1;

        private String hypothecation;

        private String _id;

        private String orderDate;

        private String hsn;

        private String color;

        private String orderStatus;

        private String gstin;

        private String alternateMobileToggle;

        private String alternateMobileNumber;

        private String invoiceNumber;

        private String accountLocation;

        private String arnNumber;

        private String insuranceTataOffer;

        private String nomineeDob;

        private String receiptId;

        private String balancePayment;

        private String insuranceSrtOffer2;

        private String insuranceSrtOffer1;

        private String accessoriesSrtOffer2;

        private String accessoriesSrtOffer1;

        private String expectedDeliveryDate;

        private String nomineeName;

        private String invoiceDate;

        private String accessoriesTataOffer;

        private String purchaseOrder;

        private String orderCategory;

        private AddDocs[] addDocs;

        private String account;

        private String exeRemarks;

        private String alarm;

        private String alarmDate;
        public String getExeRemarks() {
            return exeRemarks;
        }

        public String getAlarmDate() {
            return alarmDate;
        }

        public String getAlarm() {
            return alarm;
        }

        public String getAlarmInt() {
            return alarmInt;
        }

        public String getContactPhones ()
        {
            return contactPhones;
        }

        public void setContactPhones (String contactPhones)
        {
            this.contactPhones = contactPhones;
        }

        public String getOrderType ()
        {
            return orderType;
        }

        public void setOrderType (String orderType)
        {
            this.orderType = orderType;
        }

        public String getContactFullAddress ()
        {
            return contactFullAddress;
        }

        public void setContactFullAddress (String contactFullAddress)
        {
            this.contactFullAddress = contactFullAddress;
        }

        public String getOtherCharges ()
        {
            return otherCharges;
        }

        public void setOtherCharges (String otherCharges)
        {
            this.otherCharges = otherCharges;
        }

        public String getTotalOrderValue ()
        {
            return totalOrderValue;
        }

        public void setTotalOrderValue (String totalOrderValue)
        {
            this.totalOrderValue = totalOrderValue;
        }

        public String getAdditionalSrtOffer1 ()
    {
        return additionalSrtOffer1;
    }

        public void setAdditionalSrtOffer1 (String additionalSrtOffer1)
        {
            this.additionalSrtOffer1 = additionalSrtOffer1;
        }

        public String getQuotationChange ()
        {
            return quotationChange;
        }

        public void setQuotationChange (String quotationChange)
        {
            this.quotationChange = quotationChange;
        }

        public String getDmsPaymentsReceived ()
        {
            return dmsPaymentsReceived;
        }

        public void setDmsPaymentsReceived (String dmsPaymentsReceived)
        {
            this.dmsPaymentsReceived = dmsPaymentsReceived;
        }

        public String getAdditionalSrtOffer2 ()
    {
        return additionalSrtOffer2;
    }

        public void setAdditionalSrtOffer2 (String additionalSrtOffer2)
        {
            this.additionalSrtOffer2 = additionalSrtOffer2;
        }



        public String getTotalSrtOffer1 ()
    {
        return totalSrtOffer1;
    }

        public void setTotalSrtOffer1 (String totalSrtOffer1)
        {
            this.totalSrtOffer1 = totalSrtOffer1;
        }

        public String getCorporateSrtOffer2 ()
    {
        return corporateSrtOffer2;
    }

        public void setCorporateSrtOffer2 (String corporateSrtOffer2)
        {
            this.corporateSrtOffer2 = corporateSrtOffer2;
        }

        public String getTotalSrtOffer2 ()
    {
        return totalSrtOffer2;
    }

        public void setTotalSrtOffer2 (String totalSrtOffer2)
        {
            this.totalSrtOffer2 = totalSrtOffer2;
        }

        public String getCorporateSrtOffer1 ()
    {
        return corporateSrtOffer1;
    }

        public void setCorporateSrtOffer1 (String corporateSrtOffer1)
        {
            this.corporateSrtOffer1 = corporateSrtOffer1;
        }

        public String getAgeing() {
            return ageing;
        }

        public Docs[] getDocs ()
        {
            return docs;
        }

        public void setDocs (Docs[] docs)
        {
            this.docs = docs;
        }

        public void setAddDocs (AddDocs[] addDocs)
        {
            this.addDocs = addDocs;
        }


        public AddDocs[] getAddDocs ()
        {
            return addDocs;
        }

        public String getFinanceExecutive ()
        {
            return financeExecutive;
        }

        public void setFinanceExecutive (String financeExecutive)
        {
            this.financeExecutive = financeExecutive;
        }

        public String getBookingStatus ()
        {
            return bookingStatus;
        }

        public void setBookingStatus (String bookingStatus)
        {
            this.bookingStatus = bookingStatus;
        }

        public String getFinalPaymentReceived ()
        {
            return finalPaymentReceived;
        }

        public void setFinalPaymentReceived (String finalPaymentReceived)
        {
            this.finalPaymentReceived = finalPaymentReceived;
        }

        public String getPaymentReceivedDate ()
        {
            return paymentReceivedDate;
        }

        public void setPaymentReceivedDate (String paymentReceivedDate)
        {
            this.paymentReceivedDate = paymentReceivedDate;
        }

        public String getNomineePanGst ()
    {
        return nomineePanGst;
    }

        public void setNomineePanGst (String nomineePanGst)
        {
            this.nomineePanGst = nomineePanGst;
        }

        public String getOpportunity ()
        {
            return opportunity;
        }

        public void setOpportunity (String opportunity)
        {
            this.opportunity = opportunity;
        }

        public String getPendingDocsCount ()
        {
            return pendingDocsCount;
        }

        public void setPendingDocsCount (String pendingDocsCount)
        {
            this.pendingDocsCount = pendingDocsCount;
        }

        public String getQuantityInvoiced ()
        {
            return quantityInvoiced;
        }

        public void setQuantityInvoiced (String quantityInvoiced)
        {
            this.quantityInvoiced = quantityInvoiced;
        }

        public String getLob ()
        {
            return lob;
        }

        public void setLob (String lob)
        {
            this.lob = lob;
        }

        public String getFinancier ()
        {
            return financier;
        }

        public void setFinancier (String financier)
        {
            this.financier = financier;
        }

        public String getFinanceOption ()
        {
            return financeOption;
        }

        public void setFinanceOption (String financeOption)
        {
            this.financeOption = financeOption;
        }

        public String getCashSrtOffer1 ()
    {
        return cashSrtOffer1;
    }

        public void setCashSrtOffer1 (String cashSrtOffer1)
        {
            this.cashSrtOffer1 = cashSrtOffer1;
        }

        public String getAllocatedQuantity ()
        {
            return allocatedQuantity;
        }

        public void setAllocatedQuantity (String allocatedQuantity)
        {
            this.allocatedQuantity = allocatedQuantity;
        }

        public String getInsuranceType1 ()
    {
        return insuranceType1;
    }

        public void setInsuranceType1 (String insuranceType1)
        {
            this.insuranceType1 = insuranceType1;
        }

        public String getFinanceAmount ()
        {
            return financeAmount;
        }

        public void setFinanceAmount (String financeAmount)
        {
            this.financeAmount = financeAmount;
        }

        public String getCashSrtOffer2 ()
    {
        return cashSrtOffer2;
    }

        public void setCashSrtOffer2 (String cashSrtOffer2)
        {
            this.cashSrtOffer2 = cashSrtOffer2;
        }

        public String getInsuranceType2 ()
    {
        return insuranceType2;
    }

        public void setInsuranceType2 (String insuranceType2)
        {
            this.insuranceType2 = insuranceType2;
        }

        public String getApproximatepaymentDate ()
        {
            return approximatepaymentDate;
        }

        public void setApproximatepaymentDate (String approximatepaymentDate)
        {
            this.approximatepaymentDate = approximatepaymentDate;
        }

        public String getOthersTataOffer ()
    {
        return othersTataOffer;
    }

        public void setOthersTataOffer (String othersTataOffer)
        {
            this.othersTataOffer = othersTataOffer;
        }

        public String getQuantityRequested ()
        {
            return quantityRequested;
        }

        public void setQuantityRequested (String quantityRequested)
        {
            this.quantityRequested = quantityRequested;
        }

        public String getRm2 ()
    {
        return rm2;
    }

        public void setRm2 (String rm2)
        {
            this.rm2 = rm2;
        }

        public String getRm1 ()
        {
            return rm1;
        }

        public void setRm1 (String rm1)
        {
            this.rm1 = rm1;
        }

        public String getRm3 ()
    {
        return rm3;
    }

        public void setRm3 (String rm3)
        {
            this.rm3 = rm3;
        }

        public String getCashTataOffer ()
    {
        return cashTataOffer;
    }

        public void setCashTataOffer (String cashTataOffer)
        {
            this.cashTataOffer = cashTataOffer;
        }

        public String getCa ()
        {
            return ca;
        }

        public void setCa (String ca)
        {
            this.ca = ca;
        }

        public String getCrnNumber ()
        {
            return crnNumber;
        }

        public void setCrnNumber (String crnNumber)
        {
            this.crnNumber = crnNumber;
        }

        public String getLog_id ()
    {
        return log_id;
    }

        public void setLog_id (String log_id)
        {
            this.log_id = log_id;
        }

        public String getNomineeRelationship ()
    {
        return nomineeRelationship;
    }

        public void setNomineeRelationship (String nomineeRelationship)
        {
            this.nomineeRelationship = nomineeRelationship;
        }

        public String getTotalTataOffer ()
    {
        return totalTataOffer;
    }

        public void setTotalTataOffer (String totalTataOffer)
        {
            this.totalTataOffer = totalTataOffer;
        }

        public String getCorporateTataOffer ()
    {
        return corporateTataOffer;
    }

        public void setCorporateTataOffer (String corporateTataOffer)
        {
            this.corporateTataOffer = corporateTataOffer;
        }

        public String getFinancedBy ()
        {
            return financedBy;
        }

        public void setFinancedBy (String financedBy)
        {
            this.financedBy = financedBy;
        }

        public String getTeam ()
        {
            return team;
        }

        public void setTeam (String team)
        {
            this.team = team;
        }

        public String getParentProductLine ()
        {
            return parentProductLine;
        }

        public void setParentProductLine (String parentProductLine)
        {
            this.parentProductLine = parentProductLine;
        }

        public String getTotalPaymentsReceived ()
        {
            return totalPaymentsReceived;
        }

        public void setTotalPaymentsReceived (String totalPaymentsReceived)
        {
            this.totalPaymentsReceived = totalPaymentsReceived;
        }

        public String getOthersHeading ()
    {
        return othersHeading;
    }

        public void setOthersHeading (String othersHeading)
        {
            this.othersHeading = othersHeading;
        }

        public String getExchangeSrtOffer1 ()
    {
        return exchangeSrtOffer1;
    }

        public void setExchangeSrtOffer1 (String exchangeSrtOffer1)
        {
            this.exchangeSrtOffer1 = exchangeSrtOffer1;
        }

        public String getAccountPhone ()
        {
            return accountPhone;
        }

        public void setAccountPhone (String accountPhone)
        {
            this.accountPhone = accountPhone;
        }

        public String getAdditionalTataOffer ()
    {
        return additionalTataOffer;
    }

        public void setAdditionalTataOffer (String additionalTataOffer)
        {
            this.additionalTataOffer = additionalTataOffer;
        }

        public String getExchangeSrtOffer2 ()
    {
        return exchangeSrtOffer2;
    }

        public void setExchangeSrtOffer2 (String exchangeSrtOffer2)
        {
            this.exchangeSrtOffer2 = exchangeSrtOffer2;
        }

        public String getMessageCount ()
        {
            return messageCount;
        }

        public void setMessageCount (String messageCount)
        {
            this.messageCount = messageCount;
        }

        public String getDmsOrderValue ()
        {
            return dmsOrderValue;
        }

        public void setDmsOrderValue (String dmsOrderValue)
        {
            this.dmsOrderValue = dmsOrderValue;
        }

        public String getExchangeTataOffer ()
    {
        return exchangeTataOffer;
    }

        public void setExchangeTataOffer (String exchangeTataOffer)
        {
            this.exchangeTataOffer = exchangeTataOffer;
        }

        public String getAccountFullAddress ()
        {
            return accountFullAddress;
        }

        public void setAccountFullAddress (String accountFullAddress)
        {
            this.accountFullAddress = accountFullAddress;
        }

        public String getSalesTeam ()
        {
            return salesTeam;
        }

        public void setSalesTeam (String salesTeam)
        {
            this.salesTeam = salesTeam;
        }

        public String getProductLine ()
        {
            return productLine;
        }

        public void setProductLine (String productLine)
        {
            this.productLine = productLine;
        }

        public String getCustomerType ()
        {
            return customerType;
        }

        public void setCustomerType (String customerType)
        {
            this.customerType = customerType;
        }

        public String getVarient ()
    {
        return varient;
    }

        public void setVarient (String varient)
        {
            this.varient = varient;
        }

        public String getCustomerEmail ()
    {
        return customerEmail;
    }

        public void setCustomerEmail (String customerEmail)
        {
            this.customerEmail = customerEmail;
        }

        public String get__v ()
        {
            return __v;
        }

        public void set__v (String __v)
        {
            this.__v = __v;
        }

        public String getStockStatus ()
    {
        return stockStatus;
    }

        public void setStockStatus (String stockStatus)
        {
            this.stockStatus = stockStatus;
        }

        public String getOrderNo ()
        {
            return orderNo;
        }

        public void setOrderNo (String orderNo)
        {
            this.orderNo = orderNo;
        }

        public String getOthersSrtOffer2 ()
    {
        return othersSrtOffer2;
    }

        public void setOthersSrtOffer2 (String othersSrtOffer2)
        {
            this.othersSrtOffer2 = othersSrtOffer2;
        }

        public String getContactName ()
        {
            return contactName;
        }

        public void setContactName (String contactName)
        {
            this.contactName = contactName;
        }

        public String getAccountType ()
        {
            return accountType;
        }

        public void setAccountType (String accountType)
        {
            this.accountType = accountType;
        }

        public String getOthersSrtOffer1 ()
    {
        return othersSrtOffer1;
    }

        public void setOthersSrtOffer1 (String othersSrtOffer1)
        {
            this.othersSrtOffer1 = othersSrtOffer1;
        }

        public String getHypothecation ()
        {
            return hypothecation;
        }

        public void setHypothecation (String hypothecation)
        {
            this.hypothecation = hypothecation;
        }

        public String get_id ()
        {
            return _id;
        }

        public void set_id (String _id)
        {
            this._id = _id;
        }

        public String getOrderDate ()
        {
            return orderDate;
        }

        public void setOrderDate (String orderDate)
        {
            this.orderDate = orderDate;
        }

        public String getHsn ()
        {
            return hsn;
        }

        public void setHsn (String hsn)
        {
            this.hsn = hsn;
        }

        public String getColor ()
    {
        return color;
    }

        public void setColor (String color)
        {
            this.color = color;
        }

        public String getOrderStatus ()
        {
            return orderStatus;
        }

        public void setOrderStatus (String orderStatus)
        {
            this.orderStatus = orderStatus;
        }

        public String getGstin ()
        {
            return gstin;
        }

        public void setGstin (String gstin)
        {
            this.gstin = gstin;
        }

        public String getAlternateMobileToggle ()
        {
            return alternateMobileToggle;
        }

        public void setAlternateMobileToggle (String alternateMobileToggle)
        {
            this.alternateMobileToggle = alternateMobileToggle;
        }

        public String getAlternateMobileNumber ()
    {
        return alternateMobileNumber;
    }

        public void setAlternateMobileNumber (String alternateMobileNumber)
        {
            this.alternateMobileNumber = alternateMobileNumber;
        }

        public String getInvoiceNumber ()
    {
        return invoiceNumber;
    }

        public void setInvoiceNumber (String invoiceNumber)
        {
            this.invoiceNumber = invoiceNumber;
        }

        public String getAccountLocation ()
        {
            return accountLocation;
        }

        public void setAccountLocation (String accountLocation)
        {
            this.accountLocation = accountLocation;
        }

        public String getArnNumber ()
        {
            return arnNumber;
        }

        public void setArnNumber (String arnNumber)
        {
            this.arnNumber = arnNumber;
        }

        public String getInsuranceTataOffer ()
    {
        return insuranceTataOffer;
    }

        public void setInsuranceTataOffer (String insuranceTataOffer)
        {
            this.insuranceTataOffer = insuranceTataOffer;
        }

        public String getNomineeDob ()
    {
        return nomineeDob;
    }

        public void setNomineeDob (String nomineeDob)
        {
            this.nomineeDob = nomineeDob;
        }

        public String getReceiptId ()
        {
            return receiptId;
        }

        public void setReceiptId (String receiptId)
        {
            this.receiptId = receiptId;
        }

        public String getBalancePayment ()
        {
            return balancePayment;
        }

        public void setBalancePayment (String balancePayment)
        {
            this.balancePayment = balancePayment;
        }

        public String getInsuranceSrtOffer2 ()
    {
        return insuranceSrtOffer2;
    }

        public void setInsuranceSrtOffer2 (String insuranceSrtOffer2)
        {
            this.insuranceSrtOffer2 = insuranceSrtOffer2;
        }

        public String getInsuranceSrtOffer1 ()
    {
        return insuranceSrtOffer1;
    }

        public void setInsuranceSrtOffer1 (String insuranceSrtOffer1)
        {
            this.insuranceSrtOffer1 = insuranceSrtOffer1;
        }

        public String getAccessoriesSrtOffer2 ()
    {
        return accessoriesSrtOffer2;
    }

        public void setAccessoriesSrtOffer2 (String accessoriesSrtOffer2)
        {
            this.accessoriesSrtOffer2 = accessoriesSrtOffer2;
        }

        public String getAccessoriesSrtOffer1 ()
    {
        return accessoriesSrtOffer1;
    }

        public void setAccessoriesSrtOffer1 (String accessoriesSrtOffer1)
        {
            this.accessoriesSrtOffer1 = accessoriesSrtOffer1;
        }

        public String getExpectedDeliveryDate ()
    {
        return expectedDeliveryDate;
    }

        public void setExpectedDeliveryDate (String expectedDeliveryDate)
        {
            this.expectedDeliveryDate = expectedDeliveryDate;
        }

        public String getNomineeName ()
    {
        return nomineeName;
    }

        public void setNomineeName (String nomineeName)
        {
            this.nomineeName = nomineeName;
        }

        public String getInvoiceDate ()
    {
        return invoiceDate;
    }

        public void setInvoiceDate (String invoiceDate)
        {
            this.invoiceDate = invoiceDate;
        }

        public String getAccessoriesTataOffer ()
              {
        return accessoriesTataOffer;
             }

        public void setAccessoriesTataOffer (String accessoriesTataOffer)
        {
            this.accessoriesTataOffer = accessoriesTataOffer;
        }

        public String getPurchaseOrder ()
        {
            return purchaseOrder;
        }

        public void setPurchaseOrder (String purchaseOrder)
        {
            this.purchaseOrder = purchaseOrder;
        }

        public String getOrderCategory ()
        {
            return orderCategory;
        }

        public void setOrderCategory (String orderCategory)
        {
            this.orderCategory = orderCategory;
        }


        public String getAccount ()
        {
            return account;
        }

        public void setAccount (String account)
        {
            this.account = account;
        }


        @Override
        public String toString()
        {
            return "ClassPojo [contactPhones = "+contactPhones+", orderType = "+orderType+", contactFullAddress = "+contactFullAddress+", otherCharges = "+otherCharges+", totalOrderValue = "+totalOrderValue+", additionalSrtOffer1 = "+additionalSrtOffer1+", quotationChange = "+quotationChange+", dmsPaymentsReceived = "+dmsPaymentsReceived+", additionalSrtOffer2 = "+additionalSrtOffer2+", totalSrtOffer1 = "+totalSrtOffer1+", corporateSrtOffer2 = "+corporateSrtOffer2+", totalSrtOffer2 = "+totalSrtOffer2+", corporateSrtOffer1 = "+corporateSrtOffer1+", docs = "+docs+", financeExecutive = "+financeExecutive+", bookingStatus = "+bookingStatus+", finalPaymentReceived = "+finalPaymentReceived+", paymentReceivedDate = "+paymentReceivedDate+", nomineePanGst = "+nomineePanGst+", opportunity = "+opportunity+", pendingDocsCount = "+pendingDocsCount+", quantityInvoiced = "+quantityInvoiced+", lob = "+lob+", financier = "+financier+", financeOption = "+financeOption+", cashSrtOffer1 = "+cashSrtOffer1+", allocatedQuantity = "+allocatedQuantity+", insuranceType1 = "+insuranceType1+", financeAmount = "+financeAmount+", cashSrtOffer2 = "+cashSrtOffer2+", insuranceType2 = "+insuranceType2+", approximatepaymentDate = "+approximatepaymentDate+", othersTataOffer = "+othersTataOffer+", quantityRequested = "+quantityRequested+", rm2 = "+rm2+", rm1 = "+rm1+", rm3 = "+rm3+", cashTataOffer = "+cashTataOffer+", ca = "+ca+", crnNumber = "+crnNumber+", log_id = "+log_id+", nomineeRelationship = "+nomineeRelationship+", totalTataOffer = "+totalTataOffer+", corporateTataOffer = "+corporateTataOffer+", financedBy = "+financedBy+", team = "+team+", parentProductLine = "+parentProductLine+", totalPaymentsReceived = "+totalPaymentsReceived+", othersHeading = "+othersHeading+", exchangeSrtOffer1 = "+exchangeSrtOffer1+", accountPhone = "+accountPhone+", additionalTataOffer = "+additionalTataOffer+", exchangeSrtOffer2 = "+exchangeSrtOffer2+", messageCount = "+messageCount+", dmsOrderValue = "+dmsOrderValue+", exchangeTataOffer = "+exchangeTataOffer+", accountFullAddress = "+accountFullAddress+", salesTeam = "+salesTeam+", productLine = "+productLine+", customerType = "+customerType+", varient = "+varient+", customerEmail = "+customerEmail+", __v = "+__v+", stockStatus = "+stockStatus+", orderNo = "+orderNo+", othersSrtOffer2 = "+othersSrtOffer2+", contactName = "+contactName+", accountType = "+accountType+", othersSrtOffer1 = "+othersSrtOffer1+", hypothecation = "+hypothecation+", _id = "+_id+", orderDate = "+orderDate+", hsn = "+hsn+", color = "+color+", orderStatus = "+orderStatus+", gstin = "+gstin+", alternateMobileToggle = "+alternateMobileToggle+", alternateMobileNumber = "+alternateMobileNumber+", invoiceNumber = "+invoiceNumber+", accountLocation = "+accountLocation+", arnNumber = "+arnNumber+", insuranceTataOffer = "+insuranceTataOffer+", nomineeDob = "+nomineeDob+", receiptId = "+receiptId+", balancePayment = "+balancePayment+", insuranceSrtOffer2 = "+insuranceSrtOffer2+", insuranceSrtOffer1 = "+insuranceSrtOffer1+", accessoriesSrtOffer2 = "+accessoriesSrtOffer2+", accessoriesSrtOffer1 = "+accessoriesSrtOffer1+", expectedDeliveryDate = "+expectedDeliveryDate+", nomineeName = "+nomineeName+", invoiceDate = "+invoiceDate+", accessoriesTataOffer = "+accessoriesTataOffer+", purchaseOrder = "+purchaseOrder+", orderCategory = "+orderCategory+", addDocs = "+addDocs+", account = "+account+"]";
        }
        public class AddDocs
        {
            private String collectedDate;

            private String checkedDate;

            private String checked;

            private String collected;

            private String proof;

            public String getChecked ()
            {
                return checked;
            }

            public void setChecked (String checked)
            {
                this.checked = checked;
            }

            public String getCollected ()
            {
                return collected;
            }

            public void setCollected (String collected)
            {
                this.collected = collected;
            }

            public String getProof ()
            {
                return proof;
            }

            public String getDate() {
                return collectedDate;
            }
            public void setProof (String proof)
            {
                this.proof = proof;
            }


            public String getCheckedDate() {
                return checkedDate;
            }

            @Override
            public String toString()
            {
                return "ClassPojo [checked = "+checked+", collected = "+collected+", proof = "+proof+"]";
            }
        }
        public class Docs
        {
            private String collectedDate;

            private String checkedDate;

            private String checked;

            private String collected;

            private String proof;

            public String getChecked ()
            {
                return checked;
            }

            public void setChecked (String checked)
            {
                this.checked = checked;
            }

            public String getCollected ()
            {
                return collected;
            }

            public void setCollected (String collected)
            {
                this.collected = collected;
            }

            public String getProof ()
            {
                return proof;
            }

            public String getDate() {
                return collectedDate;
            }
            public void setProof (String proof)
            {
                this.proof = proof;
            }


            public String getCheckedDate() {
                return checkedDate;
            }

            @Override
            public String toString()
            {
                return "ClassPojo [checked = "+checked+", collected = "+collected+", proof = "+proof+"]";
            }
        }
    }
}
