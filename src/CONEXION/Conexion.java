package CONEXION;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    public static Connection conectar() {
        Connection con = null;

        try {
            String servidor = "localhost";
            String puerto = "1433";
            String baseDatos = "exentar";

            String usuario = "sa";
            String password = "fentanilo";

            String url = "jdbc:sqlserver://"
                    + servidor + ":"
                    + puerto
                    + ";databaseName="
                    + baseDatos
                    + ";encrypt=true;"
                    + "trustServerCertificate=true;";

            con = DriverManager.getConnection(
                    url,
                    usuario,
                    password
            );

        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al conectar con la base de datos.");
        }

        return con;
    }
}