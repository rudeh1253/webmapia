package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.GameManagerImpl;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.repository.GameRepository;
import com.nsl.webmapia.game.repository.MemoryUserRepository;
import com.nsl.webmapia.game.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceImplTest {
    Long gameId;
    GameService gameService;
    GameManager gameManager;
    UserRepository userRepository;
    GameRepository gameRepository;

    @BeforeEach
    public void initialize() {
        gameId = 1L;
        userRepository = new MemoryUserRepository();
        SkillManager skillManager = new SkillManager();
        Wolf wolf = new Wolf(skillManager);
        Betrayer betrayer = new Betrayer(skillManager);
        Citizen citizen = new Citizen(skillManager);
        Detective detective = new Detective(skillManager);
        Follower follower = new Follower(skillManager);
        Guard guard = new Guard(skillManager);
        HumanMouse humanMouse = new HumanMouse(skillManager);
        Mediumship mediumship = new Mediumship(skillManager);
        Murderer murderer = new Murderer(skillManager);
        Nobility nobility = new Nobility(skillManager);
        Predictor predictor = new Predictor(skillManager);
        SecretSociety secretSociety = new SecretSociety(skillManager);
        Soldier soldier = new Soldier(skillManager);
        Templar templar = new Templar(skillManager);
        Successor successor = new Successor(skillManager);
        Map<CharacterCode, Character> characters = new HashMap<>();
        characters.put(CharacterCode.WOLF, wolf);
        characters.put(CharacterCode.BETRAYER, betrayer);
        characters.put(CharacterCode.CITIZEN, citizen);
        characters.put(CharacterCode.DETECTIVE, detective);
        characters.put(CharacterCode.FOLLOWER, follower);
        characters.put(CharacterCode.GUARD, guard);
        characters.put(CharacterCode.HUMAN_MOUSE, humanMouse);
        characters.put(CharacterCode.MEDIUMSHIP, mediumship);
        characters.put(CharacterCode.MURDERER, murderer);
        characters.put(CharacterCode.NOBILITY, nobility);
        characters.put(CharacterCode.PREDICTOR, predictor);
        characters.put(CharacterCode.SECRET_SOCIETY, secretSociety);
        characters.put(CharacterCode.SOLDIER, soldier);
        characters.put(CharacterCode.TEMPLAR, templar);
        characters.put(CharacterCode.SUCCESSOR, successor);
        gameManager = new GameManagerImpl(gameId, characters, skillManager, userRepository);
        gameService = new GameServiceImpl(wolf, betrayer, citizen, detective, follower, guard, humanMouse, mediumship,
                murderer, nobility, predictor, secretSociety, soldier, successor, templar, gameRepository);
    }

    @Test
    void generateCharacters() {
    }

    @Test
    void stepForward() {
    }

    @Test
    void acceptVote() {
    }

    @Test
    void processVotes() {
    }

    @Test
    void addUser() {
    }

    @Test
    void removeUser() {
    }

    @Test
    void activateSkill() {
    }

    @Test
    void processSkills() {
    }

    @Test
    void createNewGame() {
    }
}