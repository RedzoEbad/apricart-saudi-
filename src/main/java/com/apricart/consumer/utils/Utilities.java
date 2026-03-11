package com.apricart.consumer.utils;


import com.apricart.consumer.security.constants.DateConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public static String getOrderId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_MILLI_SECONDS_PATTERN);
        return formatter.format(LocalDateTime.now());
    }
    public static boolean isValidSaudiPhoneNumber(String phoneNumber) {
        return phoneNumber != null &&
                phoneNumber.replaceAll("\\s", "")
                .matches("^((?:[+?0?0?966]+)(?:\\s?\\d{2})(?:\\s?\\d{7}))$");

    }

    public static String cleanSaudiPhoneNumber(String rawPhoneNumber) {
        if (StringUtils.isEmpty(rawPhoneNumber)) return null;

        String phoneNumber = rawPhoneNumber.replaceAll("\\s", "").replaceAll("[^\\d]", "");

        if (phoneNumber.startsWith("+966")) {
            phoneNumber = phoneNumber.substring(1);
        }

        if (phoneNumber.startsWith("05")) {
            phoneNumber = "+966" + phoneNumber.substring(1);
        }

        return phoneNumber;
    }


    public static String capitalize(String str) {
        if(str == null) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String removeFirst(String str, int count) {
        if(str == null) return str;
        return str.substring(count);
    }

    public static String removeLast(String str, int count) {
        if(str == null) return str;
        return str.substring(0, str.length()-count);
    }

    public static String getFirst(String str, int count) {
        if(str == null) return str;
        return str.substring(0, count);
    }

    public static Double toTwoDigits(Double number) {
        DecimalFormat newFormat = new DecimalFormat("#.##");
        return Double.valueOf(newFormat.format(number));
    }

    public static String getStringAfterCharacter(String text, String separator) {
        int sepPos = text.indexOf(separator);
        if (sepPos == -1) { return text; }
        return text.substring(sepPos + separator.length());
    }

    public static String getMonth(String no) {
        if (no.equalsIgnoreCase("1")) { return "JAN"; }
        else if (no.equalsIgnoreCase("2")) { return "FEB"; }
        else if (no.equalsIgnoreCase("3")) { return "MAR"; }
        else if (no.equalsIgnoreCase("4")) { return "APR"; }
        else if (no.equalsIgnoreCase("5")) { return "May"; }
        else if (no.equalsIgnoreCase("6")) { return "JUN"; }
        else if (no.equalsIgnoreCase("7")) { return "JUL"; }
        else if (no.equalsIgnoreCase("8")) { return "Aug"; }
        else if (no.equalsIgnoreCase("9")) { return "SEP"; }
        else if (no.equalsIgnoreCase("10")) { return "OCT"; }
        else if (no.equalsIgnoreCase("11")) { return "NOV"; }
        else if (no.equalsIgnoreCase("12")) { return "DEC"; }
        return "";
    }

    public static String getMonth3Char(String no) {
        if (no.equalsIgnoreCase("JANUARY")) { return "Jan"; }
        else if (no.equalsIgnoreCase("FEBRUARY")) { return "Feb"; }
        else if (no.equalsIgnoreCase("MARCH")) { return "Mar"; }
        else if (no.equalsIgnoreCase("APRIL")) { return "Apr"; }
        else if (no.equalsIgnoreCase("MAY")) { return "May"; }
        else if (no.equalsIgnoreCase("JUNE")) { return "Jun"; }
        else if (no.equalsIgnoreCase("JULY")) { return "Jul"; }
        else if (no.equalsIgnoreCase("AUGUST")) { return "Aug"; }
        else if (no.equalsIgnoreCase("SEPTEMBER")) { return "Sep"; }
        else if (no.equalsIgnoreCase("OCTOBER")) { return "Oct"; }
        else if (no.equalsIgnoreCase("NOVEMBER")) { return "Nov"; }
        else if (no.equalsIgnoreCase("DECEMBER")) { return "Dec"; }
        return "";
    }

    public static Double toFourDigits(Double number) {
        return number;
    }

    public static double getIntFromString(String text) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(text);
        while(m.find()) {
            System.out.println(m.group());
            return Double.parseDouble(m.group());
        }
        return 0;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public static ZonedDateTime toZoneDateTime(String datetime) {
        return ZonedDateTime.parse(datetime.replace(" ","T").concat("+05:00"), DateTimeFormatter.ISO_DATE_TIME);
    }


    public static String getRemoteIP(RequestAttributes requestAttributes) {
        String[] IP_HEADER_NAMES = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        if (requestAttributes == null) {
            return "0.0.0.0";
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String ip = Arrays.asList(IP_HEADER_NAMES)
                .stream()
                .map(request::getHeader)
                .filter(h -> h != null && h.length() != 0 && !"unknown".equalsIgnoreCase(h))
                .map(h -> h.split(",")[0])
                .reduce("", (h1, h2) -> h1 + ":" + h2);
        return ip + request.getRemoteAddr();
    }

    public static long calculateDistance(String origin_lat, String origin_long, String destination_lat, String destination_long) {
        try {
            return calculateDistanceMeters(origin_lat, origin_long,destination_lat,destination_long)/1000;
        } catch (Exception e) { return 0L; }
    }

    public static String getObject(String url, boolean auth, String token) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        if (auth) {
//            con.setRequestProperty("Authorization", "Bearer 9pxzh40ufzyte2ex705qcezufzkcpesr");
            con.setRequestProperty("Authorization", token);
        }
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public static long calculateDistanceMeters(String origin_lat, String origin_long, String destination_lat, String destination_long) {
        // https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=24.881308,67.060220&destinations=24.879498,67.063009&key=AIzaSyAW0FlOWDg1vtzZaYSLSvVKJaYCRHdp1rs

//        String key = "AIzaSyAXP6UjbB3CN-Dgalm2g6PMeoVIfdyS6-s" // ApricartApp
        String key = "AIzaSyB-VSGrsQGK26wBb3kYEoiYVA0j5ZX9O1w"; // ApricartApp
//        String key = "AIzaSyBCdUlcuc_K2zH9CWXCj_6BEhJ30Fo5u3A"; // ApricartApp
//        String key = ""; // ApricartApp
//        String key = "AIzaSyAW0FlOWDg1vtzZaYSLSvVKJaYCRHdp1rs"; // some one else's
        String base_url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=";
        String url = base_url.concat(origin_lat).concat(",").concat(origin_long).concat("&destinations=").concat(destination_lat).concat(",").concat(destination_long).concat("&key=").concat(key);
//        System.out.println(url);
        try {
            String response = Utilities.getObject(url, false, "");
            JSONObject main_obj = new JSONObject(response);
            JSONArray rows = main_obj.getJSONArray("rows");
            JSONObject row_item = rows.getJSONObject(0);
            JSONArray elements = row_item.getJSONArray("elements");
            JSONObject elements_item = elements.getJSONObject(0);
            JSONObject distance = elements_item.getJSONObject("distance");
            long value = distance.getLong("value");
            System.out.println("value: "+value);
            return value;
        } catch (Exception e) { return 0L; }
    }

    public static String generateOTP() {
        return RandomStringUtils.randomNumeric(4);
    }



    public static String getFormatString(int precision, double numericValue) {
        StringBuilder pattern = new StringBuilder("#,##0");
        for (int i = 0; i < precision; i++) {
            if (i == 0) {
                pattern.append(".");
            }
            pattern.append("0");
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(pattern.toString(), symbols);

        return decimalFormat.format(numericValue);

    }



}
