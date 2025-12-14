package gpb.gamesswapservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication(
        scanBasePackages = {"gpb.gamesswapservice", "gpb.gamesswapapi", "events"}
//        exclude = {DataSourceAutoConfiguration.class}
)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class GamesSwapServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamesSwapServiceApplication.class, args);
    }

}
