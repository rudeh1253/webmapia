package nsl.webmapia.game.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * DTO class for request page. Although there is Pageable class in
 * Spring Data framework, this class has been defined in order
 * for usage of pagination independent to Spring framework.
 *
 * @author PGD
 * @see PageWrapper
 */
@RequiredArgsConstructor
@Getter
@ToString
public class PageDto {
    private final Integer page;
    private final Integer pageSize;
}
