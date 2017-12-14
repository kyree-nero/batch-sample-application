package sample.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import sample.web.controllers.BatchController;

@EnableWebMvc
@Profile("master")
@ComponentScan(basePackageClasses=BatchController.class)
public class WebConfiguration {

}
