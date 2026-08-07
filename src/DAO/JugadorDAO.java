package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import CONEXION.Conexion;
import INTERFACES.InterJugadorDAO;
import MODELS.Jugador;
public class JugadorDAO implements InterJugadorDAO {

    @Override
    public boolean guardar(Jugador jugador) {
        try {
            Connection con = Conexion.conectar();
            String sql = "INSERT INTO jugadores "
                    + "(nicknmae,wins,lose,tie) "
                    + "VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, jugador.getNickname());
            ps.setInt(2, jugador.getWins());
            ps.setInt(3, jugador.getLose());
            ps.setInt(4, jugador.getTie());
            ps.execute();
            con.close();

            return true;
        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Jugador> listar() {
        ArrayList<Jugador> lista = new ArrayList<>();

        try {
            Connection con = Conexion.conectar();

            String sql = "SELECT * FROM jugadores ORDER BY id";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Jugador jugador = new Jugador(0, null, 0, 0, 0);

                jugador.setId(rs.getInt("id"));
                jugador.setNickname(rs.getString("nicknmae"));
                jugador.setWins(rs.getInt("wins"));
                jugador.setLose(rs.getInt("lose"));
                jugador.setTie(rs.getInt("tie"));
                lista.add(jugador);
            }
            con.close();
        }
        catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }
        return lista;
    }

    @Override
    public Jugador buscar(int id) {
        Jugador jugador = null;

        try {
            Connection con = Conexion.conectar();
            String sql = "SELECT * FROM jugadores WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                jugador = new Jugador(0, null, 0, 0, 0);

                jugador.setId(rs.getInt("id"));
                jugador.setNickname(rs.getString("nicknmae"));
                jugador.setWins(rs.getInt("wins"));
                jugador.setLose(rs.getInt("lose"));
                jugador.setTie(rs.getInt("tie"));
            }
            con.close();
        }
        catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }
        return jugador;
    }

    @Override
    public boolean editar(Jugador jugador) {
        try {
            Connection con = Conexion.conectar();

            String sql = "UPDATE jugadores SET nicknmae = ?, wins = ?, lose = ?, tie = ? WHERE id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, jugador.getNickname());
            ps.setInt(2, jugador.getWins());
            ps.setInt(3, jugador.getLose());
            ps.setInt(4, jugador.getTie());
            ps.setInt(5, jugador.getId());

            ps.executeUpdate();

            ps.close();
            con.close();

            return true;

        } catch (Exception e) {
            System.out.println("Error: " + e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public Jugador buscarPorNickname(String nickname) {
        Jugador jugador = null;

        try {
            Connection con = Conexion.conectar();
            String sql = "SELECT * FROM jugadores WHERE nicknmae = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nickname);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                jugador = new Jugador(
                        rs.getInt("id"),
                        rs.getString("nicknmae"),
                        rs.getInt("wins"),
                        rs.getInt("lose"),
                        rs.getInt("tie")
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return jugador;
    }
}
