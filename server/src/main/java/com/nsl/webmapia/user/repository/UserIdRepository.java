package com.nsl.webmapia.user.repository;

public interface UserIdRepository {
    void addId(Long id);
    boolean exist(Long id);
    void remove(Long id);
}
