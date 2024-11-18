package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import controller.Conexao;
import model.Cliente;
import model.Usuario;

public class DAO {
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    private static String CADASTRAR_CLIENTE = " INSERT INTO CLIENTE (ID, NOME, CPFCNPJ, EMAIL, TELEFONE, ENDERECO) VALUES (NULL, ?, ?, ?, ?, ?) ";
    private static String CONSULTAR_CLIENTE = " SELECT * FROM CLIENTE WHERE ID = ? ";
    private static String ALTERAR_CLIENTE = " UPDATE CLIENTE SET NOME = ?, CPFCNPJ = ?, EMAIL = ?, TELEFONE = ?, ENDERECO = ? WHERE ID = ? ";
    private static String EXCLUIR_CLIENTE = " DELETE FROM CLIENTE WHERE ID = ? ";
    private static String LISTAR_CLIENTES = " SELECT * FROM CLIENTE WHERE 1=1 ";
    private static String CONSULTAR_USUARIO = " SELECT USUARIO, SENHA FROM USUARIO WHERE USUARIO = ? AND SENHA = ? ";

    public DAO() {}

    public void cadastrarCliente(Cliente cliente) {
        Connection connection = Conexao.getConn().abrirConexao();
        try {
            preparedStatement = connection.prepareStatement(CADASTRAR_CLIENTE);
            int i = 1;
            preparedStatement.setString(i++, cliente.getNome());
            preparedStatement.setString(i++, cliente.getCpfCnpj());
            preparedStatement.setString(i++, cliente.getEmail());
            preparedStatement.setString(i++, cliente.getTelefone());
            preparedStatement.setString(i++, cliente.getEndereco());
            preparedStatement.execute();
            connection.commit();
            JOptionPane.showMessageDialog(null, "Cliente incluído com sucesso ");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            fecharConexao();
        }
    }

    public Cliente consultarCliente(String id) throws Exception {
        Connection connection = Conexao.getConn().abrirConexao();
        Cliente cliente = null;
        try {
            preparedStatement = connection.prepareStatement(CONSULTAR_CLIENTE);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                cliente = new Cliente(resultSet.getString("ID"), resultSet.getString("nome"), resultSet.getString("CPFCNPJ"),
                        resultSet.getString("EMAIL"), resultSet.getString("TELEFONE"), resultSet.getString("ENDERECO"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            fecharConexao();
        }
        if (cliente == null) {
            JOptionPane.showMessageDialog(null, "Não foi possível localizar o cliente selecionado ", "", JOptionPane.WARNING_MESSAGE);
            throw new Exception("Não foi possível localizar o cliente selecionado");
        }
        return cliente;
    }

    public void alterarCliente(String id, Cliente cliente) {
        Connection connection = Conexao.getConn().abrirConexao();
        try {
            preparedStatement = connection.prepareStatement(ALTERAR_CLIENTE);
            int i = 1;
            preparedStatement.setString(i++, cliente.getNome());
            preparedStatement.setString(i++, cliente.getCpfCnpj());
            preparedStatement.setString(i++, cliente.getEmail());
            preparedStatement.setString(i++, cliente.getTelefone());
            preparedStatement.setString(i++, cliente.getEndereco());
            preparedStatement.setString(i++, id);
            preparedStatement.execute();
            connection.commit();
            JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso ");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            fecharConexao();
        }
    }

    public void excluirCliente(String id) {
        Connection connection = Conexao.getConn().abrirConexao();
        try {
            preparedStatement = connection.prepareStatement(EXCLUIR_CLIENTE);
            preparedStatement.setString(1, id);
            preparedStatement.execute();
            connection.commit();
            JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso ");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            fecharConexao();
        }
    }

    public ArrayList<Cliente> listarClientes() throws Exception {
        Connection connection = Conexao.getConn().abrirConexao();
        ArrayList<Cliente> clientes = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(LISTAR_CLIENTES);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                clientes.add(new Cliente(resultSet.getString("ID"), resultSet.getString("nome"), resultSet.getString("CPFCNPJ"),
                        resultSet.getString("EMAIL"), resultSet.getString("TELEFONE"), resultSet.getString("ENDERECO")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            fecharConexao();
        }
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há clientes cadastrados", "", JOptionPane.WARNING_MESSAGE);
            throw new Exception("Não há clientes cadastrados");
        }
        return clientes;
    }

    public Usuario consultarUsuario(String nomeUsuario, String senhaCriptografada) throws Exception { //WHILE????????????????????????????????????????
        Connection connection = Conexao.getConn().abrirConexao();
        Usuario usuario = null;  //Cliente cliente = null ?
        
        try {
            preparedStatement = connection.prepareStatement(CONSULTAR_USUARIO);
            
            //int i = 1; ?
            
            preparedStatement.setString(1, nomeUsuario);
            preparedStatement.setString(2, senhaCriptografada);
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                usuario = new Usuario(resultSet.getInt("ID"), resultSet.getString("USUARIO"), resultSet.getString("SENHA"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            fecharConexao();
        }
        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Não foi possível localizar o usuário", "", JOptionPane.WARNING_MESSAGE);
            throw new Exception("Não foi possível localizar o usuário selecionado");
        }
        return usuario;
    }

    private void fecharConexao() {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            Conexao.getConn().fecharConexao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
