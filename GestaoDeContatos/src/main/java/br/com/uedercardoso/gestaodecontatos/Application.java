package br.com.uedercardoso.gestaodecontatos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Application {
	
	//private static String HOST = "http://localhost:4200"; 
	/*private static String[] LINK = {HOST,
									HOST+"/adicionar",
									HOST+"/atualizar/contato/{codContato}"};*/
	
	//https://spring.io/guides/gs/rest-service-cors/
	//http://andreybleme.com/2016-11-27/cors-spring/
	//https://www.tektutorialshub.com/angular/angular-pass-url-parameters-query-strings/
	//https://medium.com/@gigioSouza/resolvendo-o-problema-do-cors-com-angular-2-e-o-angular-cli-7f7cb7aab3c2
    /*@SuppressWarnings("deprecation")
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(LINK);
            }
        };
    }*/
	   
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
	}
	
}