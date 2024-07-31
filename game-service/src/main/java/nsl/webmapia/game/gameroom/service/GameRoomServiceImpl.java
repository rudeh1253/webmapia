package nsl.webmapia.game.gameroom.service;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.common.dto.PageDto;
import nsl.webmapia.game.common.dto.PageWrapper;
import nsl.webmapia.game.gameroom.domain.GameRoom;
import nsl.webmapia.game.gameroom.dto.GameRoomDto;
import nsl.webmapia.game.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.member.domain.Member;
import nsl.webmapia.game.member.dto.MemberDto;
import nsl.webmapia.game.member.service.MemberService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Implementation of {@link GameRoomService}. This object is registered as Spring Bean.
 *
 * @author PGD
 */
@Service
@RequiredArgsConstructor
public class GameRoomServiceImpl implements GameRoomService {
    private final GameRoomRepository gameRoomRepository;
    private final MemberService memberService;

    @Override
    public GameRoomCreationResponseDto createRoom(String roomName, String creatorId) {
        MemberDto hostDto = this.memberService.findMemberById(creatorId);
        Member host = new Member(hostDto.getMemberId(), hostDto.getNickname());

        GameRoom newGameRoom = new GameRoom(roomName, host, LocalDateTime.now(), List.of(host));
        int generatedNumber = this.gameRoomRepository.save(newGameRoom);
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
    public PageWrapper<GameRoomDto> getGameRoomsByRoomName(String roomName, int page) {
        return domainToDto(this.gameRoomRepository.findByRoomName(roomName, new PageDto(page, null)));
    }

    @Override
    public PageWrapper<GameRoomDto> getGameRoomsByRoomName(String roomName, int page, int pageSize) {
        return domainToDto(this.gameRoomRepository.findByRoomName(roomName, new PageDto(page, pageSize)));
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
