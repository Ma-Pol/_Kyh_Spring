package hello.jdbc.service;

import java.sql.SQLException;

public interface MemberServiceEx {
    void accountTransfer(String fromId, String toId, int money) throws SQLException;
}
