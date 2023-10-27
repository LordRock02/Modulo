package src.dataBase;

import java.sql.Connection;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import src.controlador.Persona;

public class DBManaging {
	//public static final SimpleDateFormat ymd = new SimpleDateFormat("YYYY-MM-DD");
	private DBManaging() {

	}

	/**
	 * 
	 * @param table      tabla en la que se requiere buscar el registro
	 * @param field      campo al que pertenece el dato a buscar
	 * @param data       dato a verificar
	 * @param connection conecci�n con la base de datos SQL
	 * @return devuelve true si el registro ha sido encontrado
	 * @throws SQLException
	 */
	public static boolean searchRecord(String table, String field, String data, Connection connection)
			throws SQLException {
		System.out.println(connection.isClosed());
		String sql = "SELECT  ";
		sql += "X." + field + "\nFROM " + table;
		sql += " X WHERE lower(" + "X." + field + ") = '" + data.toLowerCase() + "';";
		System.out.println(sql);
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static Persona buscarPersona(String data, Connection connection) throws SQLException {
		Persona persona = null;
		System.out.println(connection.isClosed());
		String sql = "SELECT  ";
		sql += "X.idtipopersona, X.idpersona, X.nompersona || ' ' || x.apellpersona" + "\nFROM persona";
		sql += " X WHERE lower(" + "X.idenpersona) = '" + data.toLowerCase() + "';";
		System.out.println(sql);
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) {
				persona = new Persona(result.getString(1), result.getString(2), result.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return persona;
	}

	public static Object[] buscarEnsambles(Connection connection) throws SQLException {
		String nombre = null;
		ArrayList<String> ensambles = new ArrayList();
		System.out.println(connection.isClosed());
		String sql = "SELECT  ";
		sql += "X.consecensamble" + "\nFROM ensamble X";
		sql += " WHERE X.facturado = false;";
		System.out.println(sql);
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				ensambles.add(result.getString("consecensamble"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ensambles.toArray();
	}

	public static Object[][] consultarDetalleEnsamble(int consecutivo, Connection connection) throws SQLException {
		String nombre = null;
		Object row[][] = null;
		ArrayList<String> referencia = new ArrayList();
		ArrayList<Float> precio = new ArrayList();
		ArrayList<Integer> numero = new ArrayList();
		System.out.println(connection.isClosed());
		String sql = "select I.idrefe ReferenciaProducto, I.valor Valor, I.noinvenfk numero\r\n"
				+ "    from detalleensamble D, inventario I";
		sql += "        where D.idrefe = I.idrefe and\r\n" + "              D.noinvenfk = I.noinvenfk and\r\n"
				+ "              D.consecensamble = " + consecutivo + ";";
		System.out.println(sql);
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				referencia.add(result.getString("referenciaproducto"));
				precio.add(result.getFloat("valor"));
				numero.add(result.getInt("numero"));
			}
			row = new Object[referencia.size()][3];
			for (int i = 0; i < row.length; i++) {
				row[i][0] = referencia.get(i);
				row[i][1] = precio.get(i);
				row[i][2] = numero.get(i);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}
	public static int ultimaFactura(Connection connection) {
		int numeroFactura = 0;
		String sql = "SELECT factura.nfactura FROM factura;";
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				numeroFactura = (result.getInt(1) > numeroFactura) ? result.getInt(1) : numeroFactura;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(numeroFactura);
		return numeroFactura;
	}

	public static void crearFactura(Connection connection, Persona persona, String codigoEmpleado, float valorFactura) {
		Calendar calendar = Calendar.getInstance();
		String currentDate = String.valueOf(calendar.get(Calendar.YEAR)) + "-" + String.valueOf(calendar.get(Calendar.MONTH)+1) + "-" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		String sql = "INSERT INTO factura (nfactura, idtipofac, idtipopersona, idpersona, idformap, codempleado, fechafactura, valorfactura) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, ultimaFactura(connection)+1);
			statement.setString(2, "01");
			statement.setString(3, persona.tipoPersona);
			statement.setString(4, persona.id);
			statement.setString(5, "04");
			statement.setString(6, codigoEmpleado);
			statement.setDate(7, java.sql.Date.valueOf(currentDate));
			statement.setFloat(8, valorFactura);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static void crearDetalleFactura(Connection connection, int nFactura, int item, int noInventario, int idReferencia, int cantidad, float valor) {
		String sql = "INSERT INTO detallefactura (nfactura, item, idtipodeta, idrefe, noinvenfk, cantidad, precio)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, nFactura);
			statement.setInt(2, item);
			statement.setString(3, "02");
			statement.setInt(4, idReferencia);
			statement.setInt(5, noInventario);
			statement.setInt(6, cantidad);
			statement.setFloat(7, valor);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void crearDetalleFactura(Connection connection, int nFactura, int item, int cantidad, float valor, int consecEnsamble) {
		String sql = "INSERT INTO detallefactura (nfactura, item, idtipodeta, cantidad, precio, ensamblefk)"
				+ " VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, nFactura);
			statement.setInt(2, item);
			statement.setString(3, "01");
			statement.setInt(4, cantidad);
			statement.setFloat(5, valor);
			statement.setInt(6, consecEnsamble);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void updateEnsamble(Connection connection, int consecEnsamble) {
		String sql = "UPDATE ensamble SET facturado = true WHERE consecensamble =" + consecEnsamble + ";";
		System.out.println(sql);
		try {
			Statement statement = connection.createStatement();
			statement.executeQuery(sql);
			
		}catch(SQLException e) {
			
		}
	}
	public static void updateProducto(Connection connection, int referencia, int noInven) {
		String sql = "UPDATE inventario SET  nfactura = " + ultimaFactura(connection) + " WHERE noinvenfk = " + noInven
				+ " and idrefe =" + referencia + ";";
		
		System.out.println(sql);
		try {
			Statement statement = connection.createStatement();
			statement.executeQuery(sql);
			sql = "UPDATE inventario SET idevento = '02' WHERE noinvenfk = " + noInven
					+ " and idrefe =" + referencia + ";";
			System.out.println(sql);
			Statement statement2 = connection.createStatement();
			statement2.executeQuery(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
