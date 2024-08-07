package nsl.webmapia.game.gameroom.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.common.NumberConstants;
import nsl.webmapia.game.common.dto.PageDto;
import nsl.webmapia.game.common.dto.PageWrapper;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.repository.jparepository.GameRoomJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SpringDataJpaGameRoomRepository implements GameRoomRepository {
    private final GameRoomJpaRepository jpaRepository;

    @Override
    public int save(GameRoom gameRoom) {
        GameRoom saved = this.jpaRepository.save(gameRoom);
        log.debug("saved={}", saved);
        return saved.getRoomId();
    }

    @Override
    public Optional<GameRoom> findById(int roomId) {
        return this.jpaRepository.findById(roomId);
    }

    @Override
    public PageWrapper<GameRoom> findAll(PageDto pageDto) {
        Page<GameRoom> found = this.jpaRepository.findAll(getPageable(pageDto));
        return new PageWrapper<>(
                found.getPageable().getPageNumber(),
                found.getTotalPages(),
                found.getTotalElements(),
                found.getContent()
        );
    }

    @Override
    public PageWrapper<GameRoom> findByRoomName(String roomName, PageDto pageDto) {
        Page<GameRoom> gameRoomPage =
                this.jpaRepository.findByRoomNameLike("%" + roomName + "%", getPageable(pageDto));
        return new PageWrapper<>(
                pageDto.getPage(),
                gameRoomPage.getTotalPages(),
                gameRoomPage.getTotalElements(),
                gameRoomPage.getContent()
        );
    }

    private Pageable getPageable(PageDto pageDto) {
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();
        return PageRequest.of(
                page == null ? 1 : page,
                pageSize == null ? NumberConstants.DEFAULT_QUERY_PAGE_SIZE.get() : pageSize
        );
    }

    @Override
    public boolean update(GameRoomUpdateDto dto) {
        Optional<GameRoom> findGameRoomOp = this.jpaRepository.findById(dto.getRoomId());
        if (findGameRoomOp.isEmpty()) {
            return false;
        }
        GameRoom findGameRoom = findGameRoomOp.get();
        if (dto.getHostMemberId() != null) {
            findGameRoom.setHostMemberId(dto.getHostMemberId());
        }
        if (dto.getRoomName() != null) {
            findGameRoom.setRoomName(dto.getRoomName());
        }
        return true;
    }

    @Override
    public boolean deleteById(int roomId) {
        this.jpaRepository.deleteById(roomId);
        return true;
    }
}
