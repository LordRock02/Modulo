package src.vista;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import src.controlador.Controlador;

public class Ventana extends JFrame {
	
	public JButton connectBtn, okBtn, okBtn_2, doneBtn, acceptBtn;
	public JTextField urlField, userField, passwordField;
	public Object[] fields = new Object[6];
	public JTextField employeeField, customerField;
	public JComboBox detalleComboBox, comboBox;
	public JScrollPane scroll;
	public JTable table;
	public DefaultTableModel model;
	public JLabel totalLbl, ivaLbl, productosLbl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana frame = new Ventana();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Ventana() {
		this.setSize(692,727);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		fields[0] = "URL";
		urlField = new JTextField();
		fields[1] = urlField;
		fields[2] = "USER NAME";
		userField = new JTextField();
		fields[3] = userField;
		fields[4] = "PASSWORD";
		passwordField = new JTextField();
		fields[5] = passwordField;
		
		connectBtn = new JButton("conectar");
		connectBtn.setBounds(47, 50, 97, 29);
		getContentPane().add(connectBtn);
		
		employeeField = new JTextField();
		employeeField.setBounds(177, 51, 147, 29);
		getContentPane().add(employeeField);
		employeeField.setColumns(10);
		
		customerField = new JTextField();
		customerField.setColumns(10);
		customerField.setBounds(422, 51, 147, 29);
		getContentPane().add(customerField);
		
		JLabel lblNewLabel = new JLabel("Empleado");
		lblNewLabel.setBounds(177, 32, 71, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblCliente = new JLabel("Cliente");
		lblCliente.setBounds(422, 32, 71, 14);
		getContentPane().add(lblCliente);
		
		okBtn = new JButton("OK");
		okBtn.setBounds(333, 46, 53, 39);
		okBtn.setEnabled(false);
		getContentPane().add(okBtn);
		
		okBtn_2 = new JButton("OK");
		okBtn_2.setBounds(579, 46, 53, 39);
		okBtn_2.setEnabled(false);
		getContentPane().add(okBtn_2);
		
		doneBtn = new JButton("terminar");
		doneBtn.setBounds(513, 132, 97, 26);
		doneBtn.setEnabled(false);
		getContentPane().add(doneBtn);
		
		acceptBtn = new JButton("aceptar");
		acceptBtn.setBounds(289, 587, 97, 26);
		acceptBtn.setEnabled(false);
		getContentPane().add(acceptBtn);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		String detalles[] = {"Ensamble", "Producto"};
		
		detalleComboBox = new JComboBox(detalles);
		detalleComboBox.setBounds(47, 126, 104, 29);
		detalleComboBox.setEnabled(false);
		getContentPane().add(detalleComboBox);
		
		comboBox = new JComboBox();
		comboBox.setBounds(333, 129, 109, 29);
		comboBox.setEnabled(false);
		getContentPane().add(comboBox);
		
		model = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.addColumn("Item");
		model.addColumn("Tipo Detalle");
		model.addColumn("Ref Producto");
		model.addColumn("Cantidad");
		model.addColumn("Precio");
		
		scroll = new JScrollPane();
		scroll.setBounds(87, 207, 504, 351);
		getContentPane().add(scroll);
		
		table = new JTable();
		table.setModel(model);
		table.getTableHeader().setResizingAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowSelectionAllowed(false);
		table.getTableHeader().setBackground(new Color(39, 85, 120));
		table.getTableHeader().setForeground(new Color(220, 220, 220));
		scroll.setViewportView(table);
		
		JLabel lblNewLabel_1 = new JLabel("Total Productos: ");
		lblNewLabel_1.setBounds(33, 638, 97, 29);
		getContentPane().add(lblNewLabel_1);
		
		productosLbl = new JLabel("");
		productosLbl.setBounds(135, 638, 62, 22);
		getContentPane().add(productosLbl);
		
		JLabel lblNewLabel_1_1 = new JLabel("IVA:");
		lblNewLabel_1_1.setBounds(248, 638, 44, 29);
		getContentPane().add(lblNewLabel_1_1);
		
		ivaLbl = new JLabel("");
		ivaLbl.setBounds(302, 638, 62, 22);
		getContentPane().add(ivaLbl);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Total:");
		lblNewLabel_1_1_1.setBounds(431, 638, 62, 29);
		getContentPane().add(lblNewLabel_1_1_1);
		
		totalLbl = new JLabel("");
		totalLbl.setBounds(488, 638, 81, 22);
		getContentPane().add(totalLbl);
	}
	public void setController(Controlador ctrl) {
		connectBtn.addActionListener(ctrl);
		okBtn.addActionListener(ctrl);
		okBtn_2.addActionListener(ctrl);
		doneBtn.addActionListener(ctrl);
		acceptBtn.addActionListener(ctrl);
		detalleComboBox.addActionListener(ctrl);
		comboBox.addActionListener(ctrl);
	}
}
