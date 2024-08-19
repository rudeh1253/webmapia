package nsl.webmapia.game.character.repository;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaCharacterAssignmentRepository implements CharacterAssignmentRepository {
    private final CharacterAssignmentJpaRepository characterAssignmentJpaRepository;

    @Override
    public void save(CharacterAssignment characterAssignment) {
        this.characterAssignmentJpaRepository.save(characterAssignment);
    }

    @Override
    public List<CharacterAssignment> findByGameInstanceId(int gameInstanceId) {
        return this.characterAssignmentJpaRepository.findByGameInstanceId(gameInstanceId);
    }

    @Override
    public List<CharacterAssignment> findDeadCharacterAssignmentsByGameInstanceId(int gameInstanceId) {
        return this.characterAssignmentJpaRepository.findDeadCharacterAssignmentsByGameInstanceId(gameInstanceId);
    }

    @Override
    public List<CharacterAssignment> findAliveCharacterAssignmentsByGameInstanceId(int gameInstanceId) {
        return this.characterAssignmentJpaRepository.findAliveCharacterAssignmentsByGameInstanceId(gameInstanceId);
    }

    @Override
    public Optional<CharacterAssignment> findByGameInstanceIdAndMemberId(int gameInstanceId, String memberId) {
        return this.characterAssignmentJpaRepository.findByGameInstanceIdAndMemberId(gameInstanceId, memberId);
    }

    @Override
    public void updateLifeByGameInstanceIdAndMemberId(int gameInstanceId, String memberId, int life) {
        CharacterAssignment characterAssignment = this.characterAssignmentJpaRepository.findByGameInstanceIdAndMemberId(gameInstanceId, memberId)
                .orElseThrow(NoSuchElementException::new);
        characterAssignment.setLife(life);
    }
}
