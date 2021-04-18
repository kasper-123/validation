package be.vdab.validation;

import be.vdab.validation.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ProductTest {
    private Validator validator;
    private Product product;

    @BeforeEach
    void beforeEach(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator= factory.getValidator();
        product= new Product();
        product.setAankoopPrijs(BigDecimal.ONE);
        product.setVerkoopPrijs(BigDecimal.TEN);
    }

    @Test
    void correctProduct(){

        assertThat(validator.validate(product)).isEmpty();

    }
    @ParameterizedTest @ValueSource(strings={"-1","-1.234","123456798"})
    void verkeerdeAankoopPrijzen(String prijs){
        product.setAankoopPrijs(new BigDecimal(prijs));
        assertThat(validator.validate(product))
                .isNotEmpty();
    }


    @Test
    void aankoopPrijs10EnVerkoopPrijsIsVerkeerd(){
        product.setAankoopPrijs(BigDecimal.TEN);
        product.setVerkoopPrijs(BigDecimal.ONE);
        assertThat(validator.validate(product)).isNotEmpty();
    }

}
