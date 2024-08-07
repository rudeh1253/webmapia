package nsl.webmapia.game.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * DTO class for response page. Although there is Page class in
 * Spring Data framework, this class has been defined in order
 * for usage of pagination independent to Spring framework.
 *
 * @author PGD
 * @see PageDto
 */
@RequiredArgsConstructor
@Getter
@ToString
public class PageWrapper<D> {
    private final int page;
    private final int totalPage;
    private final long totalElementCount;
    private final List<D> elements;
}
