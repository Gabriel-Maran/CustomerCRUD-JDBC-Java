package com.empresa.repository;

import com.empresa.connection.ConnectionFactory;
import com.empresa.dominio.Cliente;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.empresa.exception.EmailAlreadyPresent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ClienteRepository {
    private static final Logger log = LogManager.getLogger(ClienteRepository.class);

    public static void save(Cliente cliente) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createSave(conn, cliente);) {
            log.info("Trying to save {}", cliente.getNome());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                log.info("Successfully saved {}", cliente.getNome());
            } else {
                log.info("Failed to save {}", cliente.getNome());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static PreparedStatement createSave(Connection conn, Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, email, telefone) VALUES (?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cliente.getNome());
        ps.setString(2, cliente.getEmail());
        if (cliente.getTelefone() != null) {
            ps.setString(3, cliente.getTelefone());
        } else {
            ps.setString(3, " ");
        }
        return ps;
    }

    private static boolean checkUniqueEmail(Cliente cliente) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createGetEqualEmails(conn, cliente);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                throw new EmailAlreadyPresent("Email já cadastrado: " + cliente.getEmail());
            }
        } catch (SQLException e) {
            log.info("Error while trying to connect to database", e);
            return false;
        }
        return true;
    }

    private static PreparedStatement createGetEqualEmails(Connection conn, Cliente cliente) throws SQLException {
        String sql = "SELECT email FROM cliente WHERE email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cliente.getEmail());
        return ps;
    }

    public static Optional<Cliente> findById(int id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createFindById(conn, id);
             ResultSet rs = ps.executeQuery()) {
            log.info("Finding cliente id: {}", id);
            if (!rs.next()) {
                log.info("No cliente found with id: {}", id);
                return Optional.empty();
            }
            Cliente cliente = new Cliente(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone"));
            return Optional.of(cliente);
        } catch (SQLException e) {
            log.error("Error while trying to find cliente by id: {}", id, e);
        }
        return Optional.empty();
    }

    private static PreparedStatement createFindById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }


}
