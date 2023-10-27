package src.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import src.dataBase.DBConnection;
import src.dataBase.DBManaging;
import src.vista.Ventana;

public class Controlador implements ActionListener {
	private static Controlador controlador;
	private ArrayList<Object[]> detalles = new ArrayList<Object[]>();
	private ArrayList<Integer> ensambleList = new ArrayList<Integer>();
	private float valor, iva, valorTotal;
	static Ventana ventana;
	public Connection connection;
	private Persona persona;
	private String empleado;
	Object[] ensambles;

	public static void main(String arg[]) {
		Controlador.getControlador();
	}

	private Controlador() {

	}

	public static Controlador getControlador() {
		if (controlador == null) {
			controlador = new Controlador();
			ventana = new Ventana();
			ventana.setController(controlador);
			ventana.setVisible(true);
		}
		return controlador;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(ventana.connectBtn)) {
			JOptionPane.showMessageDialog(ventana, ventana.fields, "conectar con la base de datos",
					JOptionPane.PLAIN_MESSAGE);
			connection = DBConnection.getConnection(ventana.urlField.getText(), ventana.userField.getText(),
					ventana.passwordField.getText());
			ventana.okBtn.setEnabled(true);
			try {
				ensambles = DBManaging.buscarEnsambles(connection);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource().equals(ventana.okBtn)) {
			try {
				if (!connection.isClosed()) {
					if (DBManaging.searchRecord("empleado", "codempleado", ventana.employeeField.getText(),
							connection)) {
						System.out.println("se ha encontrado al empleado");
						empleado = ventana.employeeField.getText();
						ventana.okBtn.setEnabled(false);
						ventana.okBtn_2.setEnabled(true);
					}else {
						JOptionPane.showMessageDialog(ventana, "No se encontro empleado", "ERROR !!" , JOptionPane.ERROR_MESSAGE);
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource().equals(ventana.okBtn_2)) {
			try {
				if (!connection.isClosed()) {
					persona = DBManaging.buscarPersona(ventana.customerField.getText(), connection);
					if(!persona.id.equals(null)) {
						ventana.okBtn_2.setEnabled(false);
						ventana.doneBtn.setEnabled(true);
						ventana.detalleComboBox.setEnabled(true);
						ventana.comboBox.setEnabled(true);
						JOptionPane.showMessageDialog(ventana, "bienvenido " + persona.nombre);
					}else {
						JOptionPane.showMessageDialog(ventana, "registrar al cliente", "error!!", JOptionPane.ERROR_MESSAGE);
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource().equals(ventana.detalleComboBox)) {
			switch (ventana.detalleComboBox.getSelectedIndex()) {
			case 0:
				ventana.comboBox.removeAllItems();
				for(int i = 0; i < ensambles.length; i++) {
					ventana.comboBox.insertItemAt(ensambles[i], i);
				}
				break;
			case 1:
				break;
			}
		}
		if(e.getSource().equals(ventana.comboBox)) {
			int consecutivo = Integer.parseInt((String)ventana.comboBox.getSelectedItem());
			ventana.comboBox.removeItem(ventana.comboBox.getSelectedItem());
			try {
				Object[][] detalle = DBManaging.consultarDetalleEnsamble(consecutivo, connection);
				ensambleList.add(consecutivo); 
				Object[] ensamble = {"", "50.00", "01"};
				detalles.add(ensamble);
 				for(int i = 0; i < detalle.length; i++) {
					Object[] registro = {detalle[i][0], detalle[i][1], detalle[i][2], 1, "02"};
					detalles.add(registro);
				}
				ventana.model.getDataVector().removeAllElements();
				ventana.table.updateUI();
				
				for(int i = 0; i < detalles.size(); i++) {	
					Object[] row = {i+1 , (detalles.get(i)[detalles.get(i).length - 1] == "01")? "Ensamble" : "Producto", detalles.get(i)[0], 1, detalles.get(i)[1]};
					ventana.model.addRow(row);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource().equals(ventana.doneBtn)) {
			valor = 0f;
			for(int i = 0; i < detalles.size(); i++) {
				valor += Float.parseFloat(String.valueOf(detalles.get(i)[1]));
			}
			ventana.productosLbl.setText(String.valueOf(valor));
			iva = (float) (valor*0.16);
			ventana.ivaLbl.setText(String.valueOf(iva));
			valorTotal = iva+valor;
			ventana.totalLbl.setText(String.valueOf(valorTotal));
			ventana.doneBtn.setEnabled(false);
			ventana.detalleComboBox.setEnabled(false);
			ventana.comboBox.setEnabled(false);
			ventana.acceptBtn.setEnabled(true);
		}
		if(e.getSource().equals(ventana.acceptBtn)) {
			DBManaging.crearFactura(connection, persona, empleado, valor);
			for(int i = 0, j = 0; i < detalles.size(); i++) {
				if(detalles.get(i).length == 3) {
					DBManaging.crearDetalleFactura(connection, DBManaging.ultimaFactura(connection), i+1, 1, Float.parseFloat(String.valueOf(detalles.get(i)[1])), ensambleList.get(j));
					DBManaging.updateEnsamble(connection, ensambleList.get(j));
					j++;
				}
				if(detalles.get(i).length == 5) {
					System.out.println("holi");
					DBManaging.crearDetalleFactura(connection, DBManaging.ultimaFactura(connection),i+1, (int)detalles.get(i)[2], Integer.parseInt(String.valueOf(detalles.get(i)[0])), 1, Float.parseFloat(String.valueOf(detalles.get(i)[0])));
					DBManaging.updateProducto(connection, Integer.parseInt(String.valueOf(detalles.get(i)[0])), Integer.parseInt(String.valueOf(detalles.get(i)[2])));
				}
			}
			ventana.acceptBtn.setEnabled(false);
			ventana.okBtn.setEnabled(true);
			ventana.productosLbl.setText("");
			ventana.ivaLbl.setText("");
			ventana.totalLbl.setText("");
		}
	}

}
