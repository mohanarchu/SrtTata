package com.example.srttata.details;

import com.example.srttata.home.DataPojo;

import java.util.List;

public class SharedArray {


    public static List<DataPojo.Results> result;
    public static List<DataPojo.Results> filterResult;
    public static void setArray(List<DataPojo.Results> results){
        result = results;
    }

    public static List<DataPojo.Results> getResult() {
        return result;
    }

    public static List<DataPojo.Results> getFilterResult() {
        return filterResult;
    }

    public static void setFilterResult(List<DataPojo.Results> filterResult) {
        SharedArray.filterResult = filterResult;
    }
}