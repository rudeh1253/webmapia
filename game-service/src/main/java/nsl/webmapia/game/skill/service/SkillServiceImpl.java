package nsl.webmapia.game.skill.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.repository.ActivatedSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private ActivatedSkillRepository activatedSkillRepository;

    @Override
    public List<SkillType> getAvailableSkills(int gameInstanceId, String memberId) {
        return null;
    }
}
