package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.phidget22.PhidgetException;
import com.phidget22.VoltageInput;
import com.phidget22.VoltageInputVoltageChangeEvent;

public class TouchSensor {

	public static void main(String args[]) {
		Connection conn;
		try {
			String url = "jdbc:mysql://localhost:3306/sensor";
			conn = DriverManager.getConnection(url, "root", "secret");

			while (true) {
				try {
					VoltageInput voltageInput0 = new VoltageInput();
					// Se agrega el listener para el cambio de voltaje
					voltageInput0.addVoltageChangeListener((VoltageInputVoltageChangeEvent e) -> {
						System.out.println("Voltaje: " + e.getVoltage());
						if (e.getVoltage() == 0) {
							try {

								// Se crea el objeto statement.
								Statement stmt = conn.createStatement();
								ResultSet rs;
								String item = "1";

								// Se agrega la sentencia a ejecutar al statement.
								rs = stmt.executeQuery("SELECT cantidad FROM TouchSensor;");
								while (rs.next()) {

									// Se ejecuta y se obtiene el resultado de forma de string.
									item = rs.getString("cantidad");
								}

								// Se ejecuta la sentencia para agregar un nuevo valor a la tabla de
								// TouchSensor.
								String statementString = "UPDATE TouchSensor SET cantidad="
										+ (Integer.parseInt(item) + 1) + " WHERE id = 1";
								stmt.executeUpdate(statementString);

							} catch (Exception ex) {
								System.err.println(ex);
							}
						}
					});
					voltageInput0.open(5000);
					voltageInput0.close();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e2) {
						e2.printStackTrace();
					}
				} catch (PhidgetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
