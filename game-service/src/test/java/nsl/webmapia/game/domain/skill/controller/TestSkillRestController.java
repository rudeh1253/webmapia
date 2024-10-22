package nsl.webmapia.game.domain.skill.controller;

import nsl.webmapia.game.domain.skill.domain.SkillType;
import nsl.webmapia.game.domain.skill.service.SkillService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkillRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class TestSkillRestController {
    // TODO: More sophisticated testcases are needed here

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SkillService skillService;

    @Test
    void getAvailableSkillsOfMember() throws Exception {
        when(this.skillService.getAvailableSkills(10000, 1000))
                .thenReturn(
                        Map.of(
                                SkillType.KILL, List.of(1001, 1002),
                                SkillType.BEHEAD, List.of(1001, 1002)
                        )
                );

        final String expectedResponseBody = """
                {
                    "statusCode": 200,
                    "statusCodeSeries": 2,
                    "message": "",
                    "content": {
                        "KILL": [1001, 1002],
                        "BEHEAD": [1001, 1002]
                    }
                }
                """;

        this.mockMvc.perform(
                        get("/game/game-instances/{gameInstanceId}/available-skills/{memberId}",
                                10000, 1000))
                .andExpect(content().json(expectedResponseBody));
    }

    @DisplayName("URL: /game/game-instance/{gameInstanceId}/skills/activate - without problem")
    @Test
    void activatedSkill_success() throws Exception {
        this.mockMvc.perform(
                        post("/game/game-instance/{gameInstanceId}/skills/activate", 10000)
                                .header("Content-Type", "application/json;charset=utf-8")
                                .content("""
                                        {
                                            "activatorCharacterAssignmentId": 1000,
                                            "targetCharacterAssignmentId": 1001,
                                            "skillType": "KILL"
                                        }
                                        """))
                .andDo(log())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            statusCode: 200,
                            statusCodeSeries: 2,
                            message: "Skill activation succeeded",
                            content: null
                        }
                        """));
    }
}