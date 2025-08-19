package ru.orlov.adrift;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdConsistencyTests {

    @Test
    void fieldsAreDeletedAfterAdDeletion() {
    }

    @Test
    void optionsAreDeletedAfterAdDeletion() {
    }

    @Test
    void noOrphanFieldsAfterAdEdit() {
    }

    @Test
    void noOrphanOptionsAfterAdEdit() {
    }

}
