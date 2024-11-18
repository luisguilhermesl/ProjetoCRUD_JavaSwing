package model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class ModeloTabela extends DefaultTableModel {
    private static final String[] colunas = { "ID", "CPF/CNPJ", "Nome", "E-mail", "Telefone", "Endereço" };
    private ArrayList<Cliente> clientes;

    public ModeloTabela(ArrayList<Cliente> clientes) {
        super();
        this.clientes = clientes;
    }

    @Override
    public int getRowCount() {
        return clientes.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) { //Pode ser feito com if else
        Cliente cliente = clientes.get(rowIndex);
        switch(columnIndex) {
            case 0: return cliente.getId();
            case 1: return cliente.getCpfCnpj();
            case 2: return cliente.getNome();
            case 3: return cliente.getEmail();
            case 4: return cliente.getTelefone();
            case 5: return cliente.getEndereco();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }
}
