package controller;

import dao.BD;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    
 /* private static String DRIVER = "org.sqlite.JDBC";
    private static String BD = "jdbc:sqlite:resources/bdclientes.db";
    private static Connection conexao;*/
    
    //Variáveis para os detalhes de conexão
    private static final String url = "jdbc:mysql://localhost:3306/bdclientes";
    private static final String user = "root";
    private static final String password = "root";
    private static Connection conexao;     
    private static Conexao conn; //instancia

    private Conexao() {}

    public static Conexao getConn() {
        if(conn == null) {
            conn = new Conexao();
        }
        return conn;
    }

    public Connection abrirConexao() {
       try {
            // Carregar o driver JDBC para MySQL
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            // Estabelecer a conexão com o banco de dados
            conexao = DriverManager.getConnection(url, user, password);
            conexao.setAutoCommit(false); 
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Erro ao conectar com o banco de dados: " + e.getMessage()); 
        }
        return null;
    }

    public void fecharConexao() {
        try {
            if(conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar a conexao: "+e.getMessage());
        } finally {
            conexao = null;
        }
    }
}