package com.sekulska.services.impl;

import com.sekulska.datacheck.DataChecker;
import com.sekulska.datacheck.ResourcesNotFoundException;
import com.sekulska.datacheck.PriceData;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class DailyCurrencyServiceTest {

    @Mock
    private DataChecker dataChecker;

    @InjectMocks
    private DailyCurrencyService cut;

    private String createJsonString() {

        return " {\"Time Series FX (Daily)\": {\n" +
                "        \"2019-05-16\": {\n" +
                "            \"1. open\": \"1.1165\",\n" +
                "            \"2. high\": \"1.1169\",\n" +
                "            \"3. low\": \"1.1154\",\n" +
                "            \"4. close\": \"1.1158\"\n" +
                "        },\n" +
                "        \"2019-05-18\": {\n" +
                "            \"1. open\": \"1.1174\",\n" +
                "            \"2. high\": \"1.1184\",\n" +
                "            \"3. low\": \"1.1154\",\n" +
                "            \"4. close\": \"1.1158\"\n" +
                "        },\n" +
                "        \"2019-05-17\": {\n" +
                "            \"1. open\": \"1.1206\",\n" +
                "            \"2. high\": \"1.1224\",\n" +
                "            \"3. low\": \"1.1164\",\n" +
                "            \"4. close\": \"1.1174\"\n" +
                "        }}}";
    }

    private String createErrorJsonString() {
        return " {\"Error Message\": \"message\"}";
    }

    @Test
    public void testCheckIfPriceDataHasCorrectStructure() throws IOException, JSONException {
        Mockito.when(dataChecker.getPriceData(new HashMap<>())).thenReturn(createJsonString());

        List<PriceData> priceDataList = cut.getPriceData(new HashMap<>());

        assertEquals("2019-05-16", priceDataList.get(0).getDate());
        assertEquals("1.1165", priceDataList.get(0).getPrice());

        assertEquals("2019-05-17", priceDataList.get(1).getDate());
        assertEquals("1.1206", priceDataList.get(1).getPrice());

        assertEquals("2019-05-18", priceDataList.get(2).getDate());
        assertEquals("1.1174", priceDataList.get(2).getPrice());
    }

    @Test
    public void testCheckIfReturnedListIsSortedAscending() throws IOException, JSONException {
        Mockito.when(dataChecker.getPriceData(new HashMap<>())).thenReturn(createJsonString());

        List<PriceData> priceDataList = cut.getPriceData(new HashMap<>());

        assertEquals("2019-05-16", priceDataList.get(0).getDate());

        assertEquals("2019-05-17", priceDataList.get(1).getDate());

        assertEquals("2019-05-18", priceDataList.get(2).getDate());
    }

    @Test(expected = ResourcesNotFoundException.class)
    public void testCheckIfErrorMessageIsHandled() throws JSONException {
        cut.convertFromResponseBody(createErrorJsonString());
    }


}