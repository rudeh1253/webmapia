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
    public List<CharacterAssignment> findByGameRoomId(int gameRoomId) {
        return this.characterAssignmentJpaRepository.findByGameRoomId(gameRoomId);
    }

    @Override
    public List<CharacterAssignment> findDeadCharacterAssignmentsByGameRoomId(int gameRoomId) {
        return this.characterAssignmentJpaRepository.findDeadCharacterAssignmentsByGameRoomId(gameRoomId);
    }

    @Override
    public List<CharacterAssignment> findAliveCharacterAssignmentsByGameRoomId(int gameRoomId) {
        return this.characterAssignmentJpaRepository.findAliveCharacterAssignmentsByGameRoomId(gameRoomId);
    }

    @Override
    public Optional<CharacterAssignment> findByGameInstanceIdAndMemberId(int gameInstanceId, String memberId) {
        return this.characterAssignmentJpaRepository.findByGameInstanceIdAndMemberId(gameInstanceId, memberId);
    }

    @Override
    public Optional<CharacterAssignment> findByGameRoomIdAndMemberId(int gameRoomId, String memberId) {
        return this.characterAssignmentJpaRepository.findByGameRoomIdAndMemberId(gameRoomId, memberId);
    }

    @Override
    public void updateLifeByGameRoomIdAndMemberId(int gameRoomId, String memberId, int life) {
        CharacterAssignment characterAssignment = this.characterAssignmentJpaRepository.findByGameRoomIdAndMemberId(gameRoomId, memberId)
                .orElseThrow(NoSuchElementException::new);
        characterAssignment.setLife(life);
    }
}
