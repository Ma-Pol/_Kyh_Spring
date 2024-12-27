package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * JDBC - DataSource 사용
 */
@Slf4j
public class MemberRepositoryV1 implements MemberRepository {
    private final DataSource dataSource;

    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) throws SQLException {
        String sql = "INSERT INTO member(member_id, money) VALUES(?, ?);";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // DB 연결
            conn = getConnection();

            // DB 에 전송할 SQL 저장
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());

            // SQL 전송(데이터 변경)
            // 전송한 SQL 에 영향을 받은 DB row 수 반환
            int resultSize = pstmt.executeUpdate();
            log.info("result size={}", resultSize);

            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }

    @Override
    public Member findById(String memberId) throws SQLException {
        String sql = "SELECT * FROM member WHERE member_id = ?;";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);

            // SQL 전송(데이터 조회)
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();

                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));

                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public void update(String memberId, Integer money) throws SQLException {
        String sql = "UPDATE member SET money=? WHERE member_id=?;";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("result size={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }

    @Override
    public void delete(String memberId) throws SQLException {
        String sql = "DELETE FROM member WHERE member_id=?;";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("result size={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }

    private Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        log.info("get connection={}, class={}", conn, conn.getClass());

        return conn;
    }
}
