package com.annegret.servicepattern.orchestra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@EnableAutoConfiguration
@Configuration
public class OrchestraController {

    static Logger logger=LoggerFactory.getLogger(OrchestraController.class);

    @Value("${mapperurl}")
    private String mapperurl;

    @Value("${filterurl}")
    private String filterurl;


    @GetMapping(value = "/process/{inputString}")
    @ResponseBody
    public String processed(@PathVariable("inputString") String inputString) throws IOException {
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


            return callService(mapperurl, inputString);


        }
        return "";
    }

    private String filter(String inputString) throws IOException {
        if (inputString.length() > 0) {


            return callService(filterurl, inputString);


        }
        return "";

    }

    private String callService(String urlAsString, String parameter)  throws IOException {
        try {

            String urlParameter = urlAsString+parameter;
            URL url=new URL(urlParameter);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "According masterdata not found");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;

            output = br.readLine();
            //TODO think about timeouts
            conn.disconnect();

            return output;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

}
