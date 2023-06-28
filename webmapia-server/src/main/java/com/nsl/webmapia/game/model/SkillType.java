package com.nsl.webmapia.game.model;

/**
 * Types of skill
 * KILL: kill a user
 * EXTERMINATE: kill a user, and unable to defend
 * INVESTIGATE_DEAD_CHARACTER: investigate information of dead character
 * INVESTIGATE_ALIVE_CHARACTER: investigate information of alive character
 * DEFENSE: defend from KILL, but unable to defend EXTERMINATE
 */
public enum SkillType {
    KILL,
    EXTERMINATE,
    INVESTIGATE_DEAD_CHARACTER,
    INVESTIGATE_ALIVE_CHARACTER,
    DEFENSE
}
