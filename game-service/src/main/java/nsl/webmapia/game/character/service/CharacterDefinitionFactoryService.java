package nsl.webmapia.game.character.service;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.character.domain.CharacterCode;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CharacterDefinitionFactoryService {
    private final List<CharacterDefinitionService> characterDefinitionServices;
    private final Map<CharacterCode, CharacterDefinitionService> characterCodeToCharacterDefinition = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        characterDefinitionServices.forEach((cd) ->
                this.characterCodeToCharacterDefinition.put(cd.getCharacterCode(), cd));
    }

    public CharacterDefinitionService getCharacterDefinitionOfCharacterCode(CharacterCode characterCode) {
        if (isCharacterCodeNotContained(characterCode)) {
            throw new IllegalArgumentException("No such character code=" + characterCode.name());
        }
        return this.characterCodeToCharacterDefinition.get(characterCode);
    }

    private boolean isCharacterCodeNotContained(CharacterCode characterCode) {
        return !this.characterCodeToCharacterDefinition.containsKey(characterCode);
    }
}
