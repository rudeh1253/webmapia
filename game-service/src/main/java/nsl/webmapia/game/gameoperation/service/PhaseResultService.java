package nsl.webmapia.game.gameoperation.service;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhaseResultService {
    private final GameInstanceRepository gameInstanceRepository;


}
