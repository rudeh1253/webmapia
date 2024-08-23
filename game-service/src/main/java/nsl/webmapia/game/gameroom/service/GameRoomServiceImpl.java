package nsl.webmapia.game.gameroom.service;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.common.dto.PageDto;
import nsl.webmapia.game.common.dto.PageWrapper;
import nsl.webmapia.game.gameroom.dto.GameRoomDto;
import nsl.webmapia.game.gameroom.dto.request.GameRoomRequestDto;
import nsl.webmapia.game.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.gameroom.entity.GameRoom;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

/**
 * Implementation of {@link GameRoomService}. This object is registered as Spring Bean.
 *
 * @author PGD
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GameRoomServiceImpl implements GameRoomService {
    private final GameRoomRepository gameRoomRepository;
    private final ParticipationRepository participationRepository;

    @Override
    public GameRoomCreationResponseDto createRoom(String roomName, String creatorId) {
        GameRoom newGameRoom = new GameRoom(roomName, creatorId, LocalDateTime.now());
        int generatedNumber = this.gameRoomRepository.save(newGameRoom);
        this.participationRepository.save(new Participation(creatorId, newGameRoom));

        newGameRoom.setRoomId(generatedNumber);
        return GameRoomCreationResponseDto.of(newGameRoom);
    }

    @Override
    public GameRoomDto getGameRoom(int roomId) throws NoSuchElementException {
        return GameRoomDto.of(this.gameRoomRepository.findById(roomId).orElseThrow(NoSuchElementException::new));
    }

    @Override
    public PageWrapper<GameRoomDto> getGameRooms(int page) {
        return domainToDto(this.gameRoomRepository.findAll(new PageDto(page, null)));
    }

    @Override
    public PageWrapper<GameRoomDto> getGameRooms(int page, int pageSize) {
        return domainToDto(this.gameRoomRepository.findAll(new PageDto(page, pageSize)));
    }

    @Override
    public PageWrapper<GameRoomDto> getGameRooms(String roomName, int page) {
        return domainToDto(this.gameRoomRepository.findByRoomName(roomName, new PageDto(page, null)));
    }

    @Override
    public PageWrapper<GameRoomDto> getGameRooms(String roomName, int page, int pageSize) {
        return domainToDto(this.gameRoomRepository.findByRoomName(roomName, new PageDto(page, pageSize)));
    }

    @Override
    public PageWrapper<GameRoomDto> getGameRooms(GameRoomRequestDto dto) {
        Integer page = dto.getPage();
        Integer pageSize = dto.getPageSize();
        String roomName = dto.getRoomName();

        if (pageSize == null && roomName == null) {
            return getGameRooms(page);
        } else if (pageSize == null) {
            return getGameRooms(roomName, page);
        } else if (roomName == null) {
            return getGameRooms(page, pageSize);
        } else {
            return getGameRooms(roomName, page, pageSize);
        }
    }

    private PageWrapper<GameRoomDto> domainToDto(PageWrapper<GameRoom> domain) {
        return new PageWrapper<>(
                domain.getPage(),
                domain.getTotalPage(),
                domain.getTotalElementCount(),
                domain.getElements().stream().map(GameRoomDto::of).toList()
        );
    }
}
