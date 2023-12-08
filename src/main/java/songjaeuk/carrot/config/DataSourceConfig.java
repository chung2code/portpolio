package songjaeuk.carrot.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    @Bean
    public HikariDataSource dataSource()
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/carrotdb");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }

}
