package me.grison.monmet;

import com.google.gson.GsonBuilder;
import me.grison.monmet.timetable.StopsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        System.out.println("Now running MonMet.");
        System.out.println("----------------");
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(ctx.getBean(StopsService.class).getAllLines()));
    }

}