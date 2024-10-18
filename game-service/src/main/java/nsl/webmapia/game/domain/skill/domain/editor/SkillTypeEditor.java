package nsl.webmapia.game.domain.skill.domain.editor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nsl.webmapia.game.domain.skill.domain.SkillType;

import java.beans.PropertyEditorSupport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkillTypeEditor extends PropertyEditorSupport {
    private static final SkillTypeEditor SINGLETON = new SkillTypeEditor();

    public static SkillTypeEditor getInstance() {
        return SINGLETON;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        super.setValue(SkillType.valueOf(text));
    }

    @Override
    public String getAsText() {
        return ((SkillType)super.getValue()).name();
    }
}
