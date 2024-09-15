package nsl.webmapia.game.vote;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestVoteMessageController {

    @LocalServerPort
    int serverPort;

    // TODO
}
