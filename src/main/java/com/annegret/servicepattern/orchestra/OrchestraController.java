package com.annegret.servicepattern.orchestra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class OrchestraController {

    static Logger logger=LoggerFactory.getLogger(OrchestraController.class);

    @RequestMapping(method = RequestMethod.GET, value ="process")
    public String processed(@RequestParam("inputString") String inputString) throws IOException {
        return " process:"+ process(inputString);
    }

    private String process(String inputString) throws IOException {

        logger.info("Start process: "+inputString);

        if (inputString.length()>=0) {
            try {
                String mappedString = mapping(inputString);
                logger.info("Mapped string: "+mappedString);

                String filteredString = filter(mappedString);
                logger.info("Filtered string: "+filteredString);
                return filteredString;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  "";

    }

    private String mapping(String inputString) throws IOException {
        if (inputString.length() > 0) {

            return callService("http://localhost:8082/mapping", "inputString", inputString);


        }
        return "";
    }

    private String filter(String inputString) throws IOException {
        if (inputString.length() > 0) {

            return callService("http://localhost:8084/filter", "inputString", inputString);


        }
        return "";

    }

    private String callService(String urlAsString, String key, String parameter)  throws IOException {
        try {
            String urlParameter = urlAsString+"?"+key+"="+parameter;
            URL url=new URL(urlParameter);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) {
                return String.valueOf(conn.getResponseCode());
                //use http response enum
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;

            output = br.readLine();
            //think about timeouts
            conn.disconnect();

            return output;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

}
