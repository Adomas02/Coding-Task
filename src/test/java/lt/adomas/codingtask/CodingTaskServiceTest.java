package lt.adomas.codingtask;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
class CodingTaskServiceTest {
    private final CodingTaskService codingTaskService;

    @Autowired
    CodingTaskServiceTest(CodingTaskService codingTaskService) {
        this.codingTaskService = codingTaskService;
    }

    @Test
    void shouldGroupByFile() {

        var testInputFileContent = "bb AA Cc aa tEst-word:d ((house))    ---great-Work@---";

        var actualResult = codingTaskService.groupingByFiles(testInputFileContent);

        assertEquals("AA", actualResult.get("Result_A-G.txt").get(0).getWord());
        assertEquals(2, actualResult.get("Result_A-G.txt").get(0).getFrequency());

        assertEquals("TEST-WORD", actualResult.get("Result_O-U.txt").get(0).getWord());
        assertEquals(1, actualResult.get("Result_O-U.txt").get(0).getFrequency());

        assertEquals("HOUSE", actualResult.get("Result_H-N.txt").get(0).getWord());
        assertEquals(1, actualResult.get("Result_H-N.txt").get(0).getFrequency());

        assertEquals("GREAT-WORK", actualResult.get("Result_A-G.txt").get(4).getWord());
        assertEquals(1, actualResult.get("Result_A-G.txt").get(4).getFrequency());

    }

}