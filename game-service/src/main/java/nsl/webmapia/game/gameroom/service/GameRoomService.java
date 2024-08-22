package nsl.webmapia.game.gameroom.service;

import nsl.webmapia.game.common.dto.PageWrapper;
import nsl.webmapia.game.gameroom.dto.GameRoomDto;
import nsl.webmapia.game.gameroom.dto.request.GameRoomRequestDto;
import nsl.webmapia.game.gameroom.dto.response.GameRoomCreationResponseDto;

import java.util.NoSuchElementException;

/**
 * GameRoomService interface
 *
 * @author PGD
 */
public interface GameRoomService {

    /**
     * Create a room.
     *
     * @param roomName  the name of GameRoom to be created
     * @param creatorId the memberId of creator of the room
     * @return DTO
     */
    GameRoomCreationResponseDto createRoom(String roomName, String creatorId);

    /**
     * Find GameRoom instance of roomId given as parameter and
     * return information packaging into GameRoomDto object.
     *
     * @param roomId of GameRoom id to find
     * @return GameRoomDto instance containing info of GameRoom to find
     * @throws NoSuchElementException when can't find GameRoom of roomId
     */
    GameRoomDto getGameRoom(int roomId) throws NoSuchElementException;

    @Deprecated
    /**
     * <p>Find GameRoom instances given page. Information of GameRoom is wrapped in GameRoomDto
     * object. This method queries GameRoom without any condition, so it will find GameRoom instances
     * from a set of every GameRoom instances. GameRoom instances are sorted by creationTime in descending order.
     * <p>As no page size is provided,
     * the page size will be set to the default value defined in {@link nsl.webmapia.game.common.NumberConstants}.
     *
     * @param page of element list
     * @return GameRoomDto contains GameRoom instances sorted by creationTime in descending order. The size of
     * collection will be 0 if no element exists
     */
    PageWrapper<GameRoomDto> getGameRooms(int page);

    @Deprecated
    /**
     * <p>Find GameRoom instances given page. Information of GameRoom is wrapped in GameRoomDto
     * object. This method queries GameRoom without any condition, so it will find GameRoom instances
     * from a set of every GameRoom instances. GameRoom instances are ordered by creationTime descending.
     *
     * @param page     of element list
     * @param pageSize the size of page
     * @return GameRoomDto contains GameRoom instances sorted by creationTime in descending order. The size of
     * collection will be 0 if no element exists
     */
    PageWrapper<GameRoomDto> getGameRooms(int page, int pageSize);

    @Deprecated
    /**
     * <p>Find GameRoom instances given roomName and page. The result of query includes
     * GameRoom instances of roomName containing roomName string.
     * <p>As no page size is provided,
     * the page size will be set to the default value defined in {@link nsl.webmapia.game.common.NumberConstants}.
     *
     * @param roomName name of room to query
     * @param page     of element list
     * @return GameRoomDto instances of roomName containing query keyword (roomName), wrapped with
     * {@link PageWrapper} object
     */
    PageWrapper<GameRoomDto> getGameRooms(String roomName, int page);

    @Deprecated
    /**
     * <p>Find GameRoom instances given roomName and page. The result of query includes
     * GameRoom instances of roomName containing roomName string.
     *
     * @param roomName name of room to query
     * @param page     of element list
     * @param pageSize the size of page
     * @return GameRoomDto instances of roomName containing query keyword (roomName), wrapped with
     * {@link PageWrapper} object
     */
    PageWrapper<GameRoomDto> getGameRooms(String roomName, int page, int pageSize);

    /**
     * <p>Find GameRoom instances given search DTO. The result of query includes
     * GameRoom instances of roomName containing roomName string.
     *
     * @param dto specifying search condition
     * @return GameRoomDto instances of roomName containing query keyword (roomName), wrapped with
     * {@link PageWrapper} object
     */
    PageWrapper<GameRoomDto> getGameRooms(GameRoomRequestDto dto);
}
