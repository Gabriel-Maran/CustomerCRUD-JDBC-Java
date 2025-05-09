package com.empresa.repository;

import com.empresa.connection.ConnectionFactory;
import com.empresa.dominio.Costumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.empresa.exception.EmailAlreadyPresent;

import java.sql.*;
import java.util.Optional;

public class CostumerRepository {
    private static final Logger log = LogManager.getLogger(CostumerRepository.class);

    public static void save(Costumer costumer) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Cliente (nome, email, telefone) VALUES (?, ?, ?)")) {

                ps.setString(1, costumer.getNome());
                ps.setString(2, costumer.getEmail());

                if (costumer.getTelefone() != null) {
                    ps.setString(3, costumer.getTelefone());
                } else {
                    ps.setNull(3, Types.VARCHAR);
                }

                ps.executeUpdate();
                conn.commit(); // Confirma transação
                log.info("Successfully saved {}", costumer.getNome());

            } catch (SQLException e) {
                conn.rollback();
                if (e.getSQLState().equals("23505")) {
                    throw new EmailAlreadyPresent("Email is already registred" + costumer.getEmail());
                }
                log.error("Error while trying to save the costumer: " + costumer.getNome(), e);
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            log.error("Error while trying to save {}", costumer.getNome());
            throw new RuntimeException("Error while trying connecting to DataBase", e);
        }
    }

    public static Optional<Costumer> findById(int id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createFindById(conn, id);
             ResultSet rs = ps.executeQuery()) {
            log.info("Finding costumer id: {}", id);
            if (!rs.next()) {
                log.info("No costumer found with id: {}", id);
                return Optional.empty();
            }
            Costumer costumer = extractCostumerFromResultSet(rs);
            return Optional.of(costumer);
        } catch (SQLException e) {
            log.error("Error while trying to find costumer by id: {}", id, e);
        }
        return Optional.empty();
    }

    private static PreparedStatement createFindById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

    private static Costumer extractCostumerFromResultSet(ResultSet rs) throws SQLException {
        return new Costumer(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("telefone")
        );
    }
}
