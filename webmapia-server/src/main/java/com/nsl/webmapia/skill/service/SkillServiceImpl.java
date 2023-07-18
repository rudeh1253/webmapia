package com.nsl.webmapia.skill.service;

import com.nsl.webmapia.character.*;
import com.nsl.webmapia.character.Character;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.domain.GameManager;
import com.nsl.webmapia.skill.dto.SkillResultDTO;
import com.nsl.webmapia.gameoperation.repository.GameRepository;
import com.nsl.webmapia.skill.domain.SkillEffect;
import com.nsl.webmapia.skill.domain.SkillType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SkillServiceImpl implements SkillService {
    private final Map<CharacterCode, Character> characters;
    private final GameRepository gameRepository;

    @Autowired
    public SkillServiceImpl(Characters characters, GameRepository gameRepository) {
        this.characters = characters.getCharacters();
        this.gameRepository = gameRepository;
    }

    @Override
    public void activateSkill(Long gameId, Long activatorId, Long targetId, SkillType skillType) {
        GameManager game = findGameManager(gameId);
        game.activateSkill(activatorId, targetId, skillType);
    }

    @Override
    public List<SkillResultDTO> processSkills(Long gameId) {
        GameManager game = findGameManager(gameId);
        List<SkillEffect> skillEffects = game.processSkills();
        final List<SkillResultDTO> result = new ArrayList<>();
        skillEffects.forEach(se -> {
            if (se.getReceiverUser() == null) {
                result.add(SkillResultDTO.from(gameId, se, NotificationType.SKILL_PUBLIC));
            } else {
                result.add(SkillResultDTO.from(gameId, se, NotificationType.SKILL_PRIVATE));
            }
        });
        return result;
    }

    private GameManager findGameManager(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow();
    }
}
