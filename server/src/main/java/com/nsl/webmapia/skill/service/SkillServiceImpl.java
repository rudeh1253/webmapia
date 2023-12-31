package com.nsl.webmapia.skill.service;

import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.domain.GameManager;
import com.nsl.webmapia.gameoperation.repository.GameRepository;
import com.nsl.webmapia.skill.domain.SkillEffect;
import com.nsl.webmapia.skill.domain.SkillType;
import com.nsl.webmapia.skill.dto.SkillResultResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {
    private final GameRepository gameRepository;

    @Autowired
    public SkillServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void activateSkill(Long gameId, Long activatorId, Long targetId, SkillType skillType) {
        GameManager game = findGameManager(gameId);
        game.activateSkill(activatorId, targetId, skillType);
    }

    @Override
    public List<SkillResultResponseDTO> processSkills(Long gameId) {
        GameManager game = findGameManager(gameId);
        List<SkillEffect> skillEffects = game.processSkills();
        final List<SkillResultResponseDTO> result = new ArrayList<>();
        skillEffects.forEach(se -> {
            if (se.getReceiverUser() == null) {
                result.add(SkillResultResponseDTO.from(gameId, se, NotificationType.SKILL_PUBLIC));
            } else {
                result.add(SkillResultResponseDTO.from(gameId, se, NotificationType.SKILL_PRIVATE));
            }
        });
        return result;
    }

    private GameManager findGameManager(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow();
    }
}
