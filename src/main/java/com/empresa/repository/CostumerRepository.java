package com.empresa.repository;

import com.empresa.connection.ConnectionFactory;
import com.empresa.dominio.Costumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.empresa.exception.EmailAlreadyPresent;
import com.empresa.exception.DatabaseException;

import java.sql.*;
import java.util.Optional;

public class CostumerRepository {
    private static final Logger log = LogManager.getLogger(CostumerRepository.class);

    public static void save(Costumer costumer) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement
                    ("INSERT INTO Cliente (nome, email, telefone) VALUES (?, ?, ?)")) {
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
                if (e.getSQLState().equals("23505")) { // Will be true when its tried to insert a row that would violate a unique index
                    throw new EmailAlreadyPresent("Error, email is already registred" + costumer.getEmail());
                }
                log.error("Error while trying to save the costumer: " + costumer.getNome(), e);
                throw new DatabaseException("Error while trying save a new Consumer", e);
            }
        } catch (SQLException e) {
            log.error("Error while trying to save {}", costumer.getNome());
            throw new DatabaseException("Error while trying connecting to DataBase", e);
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
            log.error("Error finding customer by id: {}", id, e);
            throw new DatabaseException("Error while trying to find the costumer", e);
        }
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

    public static Optional<Costumer> findByEmail(String email) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createFindByEmail(conn, email);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                log.info("No costumer found with email: {}", email);
                return Optional.empty();
            }
            Costumer costumer = extractCostumerFromResultSet(rs);
            return Optional.of(costumer);
        } catch (SQLException e) {
            throw new DatabaseException("Error while trying to find the costumer", e);
        }
    }

    private static PreparedStatement createFindByEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        return ps;
    }

    public static void deleteById(int id) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            Optional<Costumer> costumer = CostumerRepository.findById(id);
            if (costumer.isEmpty()) {
                log.info("Costumer not found with id: {}", id);
                return;
            }
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Cliente WHERE id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                conn.commit();
                log.info("Successfully deleted {}", id);
            } catch (SQLException e) {
                conn.rollback();
                log.error("Error while trying to dele the costumer with id: {}", id, e);
                throw new DatabaseException("Error while trying to delete the costumer", e);
            }
        } catch (SQLException e) {
            log.error("Error while trying to delete the costumer with id: {}", id, e);
            throw new DatabaseException("Error while trying to connect to DataBase", e);
        }
    }

    public static void deleteByEmail(String email) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            Optional<Costumer> costumer = CostumerRepository.findByEmail(email);
            if (costumer.isEmpty()) {
                log.info("Costumer not found with email: {}", email);
                return;
            }
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Cliente WHERE email = ?")) {
                ps.setString(1, email);
                ps.executeUpdate();
                conn.commit();
                log.info("Successfully deleted {}", email);
            }catch (SQLException e){
                conn.rollback();
                log.error("Error while trying to remove the costumer by email: {}", email, e);
                throw new DatabaseException("Error while trying to remove the costumer", e);
            }
        } catch (SQLException e) {
            log.error("Error while trying to delete user by email: {}", email, e);
            throw new DatabaseException("Error while trying to connect to Database" + email, e);
        }
    }
}
