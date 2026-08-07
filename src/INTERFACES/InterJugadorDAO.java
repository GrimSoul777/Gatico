package INTERFACES;

import MODELS.Jugador;

import java.util.ArrayList;

public interface InterJugadorDAO {
    boolean guardar(Jugador jugador);

    ArrayList<Jugador> listar();

    Jugador buscar(int id);

    Jugador buscarPorNickname(String nickname);

    boolean editar(Jugador jugador);
}
