package hello.jdbc.service;

import java.sql.SQLException;

public interface MemberService {
    void accountTransfer(String fromId, String toId, int money) throws SQLException;
}
