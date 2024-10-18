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
    public Optional<CharacterAssignment> findById(Integer id) {
        return this.characterAssignmentJpaRepository.findById(id);
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
    public void updateLifeById(Integer characterAssignmentId, int life) {
        CharacterAssignment characterAssignment = this.characterAssignmentJpaRepository.findById(characterAssignmentId)
                .orElseThrow(NoSuchElementException::new);
        characterAssignment.setLife(life);
    }
}
