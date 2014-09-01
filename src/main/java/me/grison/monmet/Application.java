package me.grison.monmet;

import com.google.gson.GsonBuilder;
import me.grison.monmet.timetable.StopsService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Arrays;

/**
 * MonMet' application.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        ClassPathResource res = new ClassPathResource("splash.txt");
        for (String l: IOUtils.readLines(res.getInputStream())) {
            System.out.println(l);
        }
    }

}