package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.Vote;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.notification.NotificationType;
import com.nsl.webmapia.game.domain.notification.NotificationBody;
import com.nsl.webmapia.game.domain.skill.ActivatedSkillInfo;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class GameServiceImpl implements GameService {
    private final Map<CharacterCode, Character> characters;
    private final SkillManager skillManager;
    private final UserRepository userRepository;
    private final List<Vote> votes;
    private final List<ActivatedSkillInfo> activatedSkills;

    @Autowired
    public GameServiceImpl(Wolf wolf,
                           Betrayer betrayer,
                           Citizen citizen,
                           Detective detective,
                           Follower follower,
                           Guard guard,
                           HumanMouse humanMouse,
                           Mediumship mediumship,
                           Murderer murderer,
                           Nobility nobility,
                           Predictor predictor,
                           SecretSociety secretSociety,
                           Soldier soldier,
                           Successor successor,
                           Templar templar,
                           SkillManager skillManager,
                           UserRepository userRepository) {
        characters = new HashMap<>();
        characters.put(CharacterCode.WOLF, wolf);
        characters.put(CharacterCode.BETRAYER, betrayer);
        characters.put(CharacterCode.DETECTIVE, detective);
        characters.put(CharacterCode.FOLLOWER, follower);
        characters.put(CharacterCode.CITIZEN, citizen);
        characters.put(CharacterCode.GUARD, guard);
        characters.put(CharacterCode.HUMAN_MOUSE, humanMouse);
        characters.put(CharacterCode.MEDIUMSHIP, mediumship);
        characters.put(CharacterCode.MURDERER, murderer);
        characters.put(CharacterCode.NOBILITY, nobility);
        characters.put(CharacterCode.PREDICTOR, predictor);
        characters.put(CharacterCode.SECRET_SOCIETY, secretSociety);
        characters.put(CharacterCode.SOLDIER, soldier);
        characters.put(CharacterCode.SUCCESSOR, successor);
        characters.put(CharacterCode.TEMPLAR, templar);
        this.skillManager = skillManager;
        this.userRepository = userRepository;
        this.votes = new ArrayList<>();
        this.activatedSkills = new ArrayList<>();
    }

    @Override
    public List<NotificationBody<Character>> generateCharacters(Map<CharacterCode, Integer> characterDistribution) {
        List<User> users = userRepository.findAll();
        List<NotificationBody<Character>> characterNotifications = new ArrayList<>();
        Collections.shuffle(users);
        int count = 0;
        Set<CharacterCode> codes = characterDistribution.keySet();
        for (CharacterCode code : codes) {
            int num = characterDistribution.get(code);
            for (int i = 0; i < num; i++) {
                User user = users.get(count);
                Character character = characters.get(code);
                user.setCharacter(character);
                characterNotifications.add(NotificationBody.<Character>builder()
                        .notificationType(NotificationType.NOTIFY_WHICH_CHARACTER_ALLOCATED)
                        .receiver(user)
                        .data(character)
                        .build());
                count++;
            }
        }
        return characterNotifications;
    }

    @Override
    public void stepForward() {

    }

    @Override
    public void acceptVote(Long voterId, Long subjectId) {
        User voter = userRepository.findById(voterId).orElseThrow();
        User subject = userRepository.findById(subjectId).orElseThrow();
        votes.add(new Vote(
                voter.getCharacter().getCharacterCode() == CharacterCode.NOBILITY
                ? 2
                : 1, voter, subject));
    }

    @Override
    public NotificationBody<User> processVotes() {
        Map<User, Integer> total = new HashMap<>();
        for (Vote vote : votes) {
            if (total.containsKey(vote.getSubject())) {
                total.replace(vote.getSubject(), total.get(vote.getSubject()) + vote.getTheNumberOfVote());
            } else {
                total.put(vote.getSubject(), vote.getTheNumberOfVote());
            }
        }
        List<Integer> values = ((List<Integer>)total.values());
        values.sort(Comparator.comparingInt(s -> s));
        User mostUser = null;
        for (User u : total.keySet()) {
            if (total.get(u).equals(values.get(0))) {
                mostUser = u;
                break;
            }
        }
        return values.get(0).equals(values.get(1))
                ? NotificationBody.<User>builder()
                .data(null)
                .notificationType(NotificationType.INVALID_VOTE)
                .receiver(null)
                .build()
                : NotificationBody.<User>builder()
                .data(mostUser)
                .notificationType(NotificationType.EXECUTE_BY_VOTE)
                .receiver(null)
                .build();
    }

    @Override
    public Long addUser() {
        Random random = new Random();
        long generatedId = random.nextLong(100000L, 999999L);
        while (userRepository.containsKey(generatedId)) {
            generatedId = random.nextLong();
        }
        User user = new User(generatedId);
        userRepository.save(user);
        return generatedId;
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
    public List<NotificationBody<SkillEffect>> processSkills() {
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
        List<NotificationBody<SkillEffect>> notificationBodies = new ArrayList<>();
        skillManager.getSkillEffects().forEach(se -> {
            if (se.getReceiverUser() == null) {
                notificationBodies.add(NotificationBody.<SkillEffect>builder()
                        .receiver(null)
                        .notificationType(NotificationType.SKILL_PUBLIC)
                        .data(se)
                        .build());
            } else {
                notificationBodies.add(NotificationBody.<SkillEffect>builder()
                        .receiver(se.getReceiverUser())
                        .notificationType(NotificationType.SKILL_PRIVATE)
                        .data(se)
                        .build());
            }
        });
        return notificationBodies;
    }

    public List<ActivatedSkillInfo> getActivatedSkills() {
        return activatedSkills;
    }
}
