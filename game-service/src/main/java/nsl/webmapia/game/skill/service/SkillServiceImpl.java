package nsl.webmapia.game.skill.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.skill.domain.SkillType;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    @Override
    public List<SkillType> getAvailableSkills(int gameInstanceId, String memberId) {
        return null;
    }
}
