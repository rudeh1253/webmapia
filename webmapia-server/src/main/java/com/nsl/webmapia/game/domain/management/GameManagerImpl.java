package com.nsl.webmapia.game.domain.management;

import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.Vote;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.UserRepository;

import java.util.*;

public class GameManagerImpl implements GameManager {
    private final Long GAME_ID;
    private final Map<CharacterCode, Character> characters;
    private final SkillManager skillManager;
    private final UserRepository userRepository;
    private final List<Vote> votes;
    private final List<ActivatedSkillInfo> activatedSkills;

    private String gameName;
    private Long hostId;
    private GameSetting gameSetting;
    private long elapsedSeconds;

    public GameManagerImpl(Long gameId,
                           Map<CharacterCode, Character> characters,
                           SkillManager skillManager,
                           UserRepository userRepository) {
        this.GAME_ID = gameId;
        this.characters = characters;
        this.skillManager = skillManager;
        this.userRepository = userRepository;
        this.votes = new ArrayList<>();
        this.activatedSkills = new ArrayList<>();
        this.elapsedSeconds = 0L;
    }

    @Override
    public void onGameStart(GameSetting gameSetting) {
        this.gameSetting = gameSetting;
    }

    @Override
    public void onOneSecondElapsed() {
        elapsedSeconds++;
    }

    @Override
    public Long getGameId() {
        return GAME_ID;
    }

    @Override
    public void setGameName(String name) {
        this.gameName = name;
    }

    @Override
    public String getGameName() {
        return this.gameName;
    }

    @Override
    public void setHost(Long hostId) {
        this.hostId = hostId;
    }

    @Override
    public User getHost() {
        return userRepository.findById(hostId).orElseThrow();
    }

    @Override
    public List<User> generateCharacters(Map<CharacterCode, Integer> characterDistribution) {
        List<User> users = userRepository.findAll();
        Collections.shuffle(users);
        int count = 0;
        Set<CharacterCode> codes = characterDistribution.keySet();
        for (CharacterCode code : codes) {
            int num = characterDistribution.get(code);
            for (int i = 0; i < num; i++) {
                User user = users.get(count);
                Character character = characters.get(code);
                user.setCharacter(character);
                count++;
            }
        }
        return users;
    }

    @Override
    public void stepForward() {

    }

    @Override
    public void acceptVote(Long voterId, Long subjectId) {
        User voter = userRepository.findById(voterId).orElseThrow();
        User subject = userRepository.findById(subjectId).orElseThrow();
        votes.add(voter.vote(subject));
    }

    @Override
    public User processVotes() {
        Map<User, Integer> total = new HashMap<>();
        for (Vote vote : votes) {
            if (total.containsKey(vote.getSubject())) {
                total.replace(vote.getSubject(), total.get(vote.getSubject()) + vote.getTheNumberOfVote());
            } else {
                total.put(vote.getSubject(), vote.getTheNumberOfVote());
            }
        }

        User mostUser = null;
        boolean tie = false;
        for (User u : total.keySet()) {
            if (mostUser == null || total.get(u) > total.get(mostUser)) {
                mostUser = u;
                tie = false;
            } else if (total.get(u).equals(total.get(mostUser))) {
                tie = true;
            }
        }

        votes.clear();

        return tie ? null : mostUser;
    }

    @Override
    public void addUser(Long userId) {
        User user = new User(userId);
        userRepository.save(user);
    }

    @Override
    public void addUser(Long userId, String userName) {
        User user = new User(userId, userName);
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getOneUser(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> removeUser(Long userId) {
        return userRepository.deleteUserById(userId);
    }

    @Override
    public void activateSkill(Long activatorId, Long targetId, SkillType skillType) {
        Optional<User> activator = userRepository.findById(activatorId);
        Optional<User> target = userRepository.findById(targetId);
        // TODO if activatorId doesn't exist in userRepository as a key, throw an exception.
        activator.ifPresent(act ->
                target.ifPresent(tar ->
                        activatedSkills.add(act.activateSkill(tar, skillType))));
    }

    @Override
    public List<SkillEffect> processSkills() {
        activatedSkills.sort((left, right) -> {
            if (left.getSkillType() == SkillType.EXTERMINATE) {
                return -1;
            } else if (right.getSkillType() == SkillType.EXTERMINATE) {
                return 1;
            } else if (left.getSkillType() == SkillType.KILL) {
                return -1;
            } else if (right.getSkillType() == SkillType.KILL) {
                return 1;
            } else if (left.getSkillType() == SkillType.GUARD) {
                return -1;
            } else if (right.getSkillType() == SkillType.GUARD) {
                return -1;
            } else {
                return 0;
            }
        });

        for (ActivatedSkillInfo activatedSkill : activatedSkills) {
            User src = activatedSkill.getActivator();
            User tar = activatedSkill.getTarget();
            SkillType type = activatedSkill.getSkillType();
            if (activatedSkill.getSkillCondition().isSuccess(src, tar, type)) {
                activatedSkill.getOnSkillSucceed().onSkillSucceed(src, tar, type);
            } else {
                activatedSkill.getOnSkillFail().onSkillFail(src, tar, type);
            }
        }
        return skillManager.getSkillEffects();
    }

    public List<ActivatedSkillInfo> getActivatedSkills() {
        return activatedSkills;
    }
}