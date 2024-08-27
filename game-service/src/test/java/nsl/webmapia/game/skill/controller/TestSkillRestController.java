package nsl.webmapia.game.skill.controller;

import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.service.SkillService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class TestSkillRestController {
    // TODO: More sophisticated testcases are needed here

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SkillService skillService;

    @Test
    void getAvailableSkillsOfMember() throws Exception {
        when(this.skillService.getAvailableSkills(10000, "sample-member"))
                .thenReturn(
                        Map.of(
                                SkillType.KILL, List.of("member-1", "member-2"),
                                SkillType.BEHEAD, List.of("member-1", "member-2")
                        )
                );

        final String expectedResponseBody = """
                {
                    "statusCode": 200,
                    "statusCodeSeries": 2,
                    "message": "",
                    "content": {
                        "KILL": ["member-1", "member-2"],
                        "BEHEAD": ["member-1", "member-2"]
                    }
                }
                """;

        this.mockMvc.perform(
                        get("/game-instances/{gameInstanceId}/available-skills/{memberId}",
                                10000, "sample-member"))
                .andExpect(content().json(expectedResponseBody));
    }

    @DisplayName("URL: /game-instance/{gameInstanceId}/skills/activate - without problem")
    @Test
    void activatedSkill_success() throws Exception {
        this.mockMvc.perform(
                        post("/game-instance/{gameInstanceId}/skills/activate", 10000)
                                .header("Content-Type", "application/json;charset=utf-8")
                                .content("""
                                        {
                                            "activatorId": "sample-activator",
                                            "targetId": "sample-target",
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