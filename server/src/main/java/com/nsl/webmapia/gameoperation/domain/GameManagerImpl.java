package com.nsl.webmapia.gameoperation.domain;

import com.nsl.webmapia.character.*;
import com.nsl.webmapia.character.Character;
import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.skill.domain.ActivatedSkillInfo;
import com.nsl.webmapia.skill.domain.SkillEffect;
import com.nsl.webmapia.skill.domain.SkillType;
import com.nsl.webmapia.user.repository.UserRepository;
import com.nsl.webmapia.user.domain.User;

import java.util.*;

public class GameManagerImpl implements GameManager {
    private static final GamePhase[] PHASE_ORDER = {
            GamePhase.NIGHT,
            GamePhase.DAYTIME,
            GamePhase.VOTE,
            GamePhase.EXECUTION
    };

    private final Long GAME_ID;
    private SkillManager skillManager;
    private final UserRepository userRepository;
    private final List<Vote> votes;
    private final List<ActivatedSkillInfo> activatedSkills;

    private String gameName;
    private Long hostId;
    private GameSetting gameSetting;
    private int currentPhase;
    private boolean gameStarted;

    public GameManagerImpl(Long gameId,
                           UserRepository userRepository) {
        this.GAME_ID = gameId;
        this.userRepository = userRepository;
        this.votes = new ArrayList<>();
        this.activatedSkills = new ArrayList<>();
        this.gameStarted = false;
        currentPhase = 0;
    }

    @Override
    public void onGameStart() {
        this.gameStarted = true;
        this.skillManager = new SkillManager();
    }

    @Override
    public boolean hasGameStarted() {
        return this.gameStarted;
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
        return userRepository.findById(hostId).orElse(null);
    }

    @Override
    public List<User> generateCharacters(Map<CharacterCode, Integer> characterDistribution) {
        List<User> users = userRepository.findAll();
        Collections.shuffle(users);
        Map<CharacterCode, Character> characters = getCharacters();
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

    private Map<CharacterCode, Character> getCharacters() {
        return Map.ofEntries(
                Map.entry(CharacterCode.WOLF, new Wolf()),
                Map.entry(CharacterCode.BETRAYER, new Betrayer()),
                Map.entry(CharacterCode.DETECTIVE, new Detective()),
                Map.entry(CharacterCode.FOLLOWER, new Follower()),
                Map.entry(CharacterCode.CITIZEN, new Citizen()),
                Map.entry(CharacterCode.GUARD, new Guard()),
                Map.entry(CharacterCode.HUMAN_MOUSE, new HumanMouse()),
                Map.entry(CharacterCode.MEDIUMSHIP, new Mediumship()),
                Map.entry(CharacterCode.MURDERER, new Murderer()),
                Map.entry(CharacterCode.NOBILITY, new Nobility()),
                Map.entry(CharacterCode.PREDICTOR, new Predictor()),
                Map.entry(CharacterCode.SECRET_SOCIETY, new SecretSociety()),
                Map.entry(CharacterCode.SOLDIER, new Soldier()),
                Map.entry(CharacterCode.SUCCESSOR, new Successor()),
                Map.entry(CharacterCode.TEMPLAR, new Templar())
        );
    }

    @Override
    public Faction postPhase() {
        List<User> users = userRepository.findAll();
        List<User> aliveUsers = users.stream().filter(user -> !user.isDead()).toList();
        List<User> wolfUsers = aliveUsers.stream()
                .filter(user -> user.getCharacter().getFaction() == Faction.WOLF)
                .toList();
        List<User> humanUsers = aliveUsers.stream()
                .filter(user -> user.getCharacter().getFaction() == Faction.HUMAN)
                .toList();
        List<User> humanMouseUsers = aliveUsers.stream()
                .filter(user -> user.getCharacter().getFaction() == Faction.HUMAN_MOUSE)
                .toList();

        Faction winner = null;
        if (wolfUsers.size() >= humanUsers.size()) {
            if (wolfUsers.size() == 1
                    && humanUsers.size() == 1
                    && humanUsers.stream().anyMatch(user -> user.getCharacter().getCharacterCode() == CharacterCode.TEMPLAR)) {
                winner = Faction.HUMAN;
            } else {
                winner = Faction.WOLF;
            }
        } else if (wolfUsers.size() == 0) {
            winner = Faction.HUMAN;
        }
        if (winner != null && humanMouseUsers.size() >= 1) {
            winner = Faction.HUMAN_MOUSE;
        }
        if (winner == null) {
            stepForward();
        }
        return winner;
    }

    private void stepForward() {
        currentPhase = (currentPhase + 1) % PHASE_ORDER.length;
    }

    @Override
    public GamePhase currentPhase() {
        return PHASE_ORDER[currentPhase];
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
                        activatedSkills.add(act.activateSkill(tar, this.skillManager, skillType))));
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
        List<SkillEffect> skillEffects = skillManager.getSkillEffects();
        skillManager.clearSkillEffects();
        activatedSkills.clear();
        return skillEffects;
    }

    @Override
    public synchronized boolean endPhase(Long userId) {
        User sentUser = userRepository.findById(userId).orElseThrow();
        sentUser.setPhaseEnd(true);
        List<User> users = userRepository.findAll();
        boolean hasPhaseEnded = users.stream().filter(user -> !user.isPhaseEnd() && !user.isDead()).toList().size() == 0;
        users.forEach(user -> System.out.println(user));
        if (hasPhaseEnded) {
            users.forEach((user) -> user.setPhaseEnd(false));
        }
        return hasPhaseEnded;
    }

    @Override
    public synchronized boolean endGame(Long userId) {
        User sentUser = userRepository.findById(userId).orElseThrow();
        sentUser.setPhaseEnd(true);
        List<User> users = userRepository.findAll();
        for (User user : users) {
            System.out.println(user);
        }
        boolean hasGameEnded = users.stream().filter(user -> !user.isPhaseEnd()).toList().size() == 0;
        return hasGameEnded;
    }

    @Override
    public void clearGame() {
        this.gameStarted = false;
        this.skillManager.clearSkillEffects();
        this.votes.clear();
        this.activatedSkills.clear();
        this.gameStarted = false;
        this.currentPhase = 0;
        this.userRepository.findAll().stream().forEach(user -> {
            user.setDead(false);
            user.setPhaseEnd(false);
            user.setCharacter(null);
        });
    }

    public List<ActivatedSkillInfo> getActivatedSkills() {
        return activatedSkills;
    }
}
