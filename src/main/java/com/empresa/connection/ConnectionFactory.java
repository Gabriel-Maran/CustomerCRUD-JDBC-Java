package com.empresa.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        // ATENÇÃO: Credenciais simplificadas para fins didáticos.
        // Em produção, use variáveis de ambiente ou um arquivo de configuração seguro
        String url = "jdbc:mysql://localhost:3306/empresa";
        String user = "root";
        String password = "root";

        return DriverManager.getConnection(url, user, password);
    }
}
