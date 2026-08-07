import CONEXION.Conexion;
import DAO.JugadorDAO;
import INTERFACES.InterJugadorDAO;
import MODELS.Jugador;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        boolean seguir = true;
        JugadorDAO dao = new JugadorDAO();

        do {
            try {
                System.out.println();
                System.out.println("MENU PRINCIPAL");
                System.out.println("1) Registrar Jugador");
                System.out.println("2) Mostrar jugadores");
                System.out.println("3) Jugar");
                System.out.println("4) Salir");
                System.out.println("Seleccionar");
                int opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1:
                        System.out.println("Ingresa el nombre del jugador");
                        String nickname = sc.nextLine().trim();
                        do {
                            if (nickname.isEmpty()) {
                                System.out.println("Vuelve a intentarlo");
                                System.out.println("Ingresa el nombre del jugador");
                                nickname = sc.nextLine().trim();
                            }
                        } while (nickname.isEmpty());
                        //COMPROBAR QUE ESE NICKNAME YA EXISTA
                        Jugador jugadorExistente = dao.buscarPorNickname(nickname);
                        if (jugadorExistente != null) {
                            System.out.println("Ese nickname ya existe.");
                            return;
                        }
                        System.out.println("JUGADOR CREADO CORRECTAMENTE");
                        dao.guardar(new Jugador(0, nickname, 0, 0, 0));
                        break;

                    case 2:
                        if (dao.listar().isEmpty()) {
                            System.out.println("No hay jugadores registrados.");
                            return;
                        }

                        System.out.println("TODOS LOS JUGADORES");
                        System.out.println("=======================================");
                        for (Jugador jugador : dao.listar()) {
                            System.out.println("ID: " + jugador.getId());
                            System.out.println("Nickname: " + jugador.getNickname());
                            System.out.println("WINS: " + jugador.getWins());
                            System.out.println("LOSES: " + jugador.getLose());
                            System.out.println("TIES: " + jugador.getTie());
                            System.out.println("===================================");
                        }
                        break;

                    case 3:
                        System.out.println("JUGADORES DISPONIBLES");
                        System.out.println("==============================================");
                        for (Jugador jugador : dao.listar()) {
                            System.out.println("ID: " + jugador.getId());
                            System.out.println("Nickname: " + jugador.getNickname());
                            System.out.println("===============================================");
                        }
                        System.out.println();
                        System.out.println("Seleccionar jugador 1 (Ingrese el nickname)");
                        String j1 = sc.nextLine().trim();
                        System.out.println("Seleccionar jugador 2 (Ingrese el nickname)");
                        String j2 = sc.nextLine().trim();

                        Jugador jugador1 = dao.buscarPorNickname(j1);
                        Jugador jugador2 = dao.buscarPorNickname(j2);

                        // Validar jugador 1
                        if (jugador1 == null) {
                            System.out.println("El jugador '" + j1 + "' no existe.");
                            break;
                        }

                        // Validar jugador 2
                        if (jugador2 == null) {
                            System.out.println("El jugador '" + j2 + "' no existe.");
                            break;
                        }

                        // Validar que no sean el mismo
                        if (jugador1.getId() == jugador2.getId()) {
                            System.out.println("No puedes jugar contra el mismo jugador.");
                            break;
                        }

                        char simboloJugador1;
                        char simboloJugador2;

                        if (random.nextBoolean()) {
                            simboloJugador1 = 'X';
                            simboloJugador2 = 'O';
                        } else {
                            simboloJugador1 = 'O';
                            simboloJugador2 = 'X';
                        }

                        boolean empiezaJugador1 = random.nextBoolean();
                        Jugador jugadorActual;
                        char simboloActual;

                        if (empiezaJugador1) {
                            jugadorActual = jugador1;
                            simboloActual = simboloJugador1;
                        } else {
                            jugadorActual = jugador2;
                            simboloActual = simboloJugador2;
                        }

                        System.out.println("PARTIDA INICIADA DEL GATO");
                        System.out.println("INFORMACION GENERAL");
                        System.out.println(jugador1.getNickname() + " = " + simboloJugador1);
                        System.out.println(jugador2.getNickname() + " = " + simboloJugador2);
                        System.out.println();

                        System.out.println("Comienza: " + jugadorActual.getNickname() + " (" + simboloActual + ")");
                        // CREAR TABLERO
                        char[][] tablero = new char[3][3];

                        // Llenar tablero con espacios vacios
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                tablero[i][j] = ' ';
                            }
                        }

                        // JUGAR
                        boolean partidaTerminada = false;
                        while (!partidaTerminada) {
                            mostrarTablero(tablero);
                            System.out.println();
                            System.out.println("Turno de " + jugadorActual.getNickname() + " (" + simboloActual + ")");

                            int posicion;
                            while (true) {
                                System.out.print("Selecciona una posicion (1-9): ");

                                try {
                                    posicion = Integer.parseInt(sc.nextLine());
                                } catch (Exception e) {
                                    System.out.println("Ingresa un numero valido.");
                                    continue;
                                }

                                if (posicion < 1 || posicion > 9) {
                                    System.out.println("La posicion debe estar entre 1 y 9."
                                    );
                                    continue;
                                }

                                //Aqui se selecciona que fila y columna es dependiendo de lo que ingrese el jugador
                                int fila = (posicion - 1) / 3;
                                int columna = (posicion - 1) % 3;

                                if (tablero[fila][columna] != ' ') {
                                    System.out.println("Esa posicion ya esta ocupada.");
                                    continue;
                                }
                                tablero[fila][columna] = simboloActual;
                                break;
                            }

                            // VERIFICAR GANADOR
                            if (hayGanador(tablero, simboloActual)) {
                                mostrarTablero(tablero);

                                System.out.println();
                                System.out.println("GANADOR: " + jugadorActual.getNickname());

                                // Actualizar estadisticas
                                jugadorActual.setWins(jugadorActual.getWins() + 1);

                                Jugador jugadorPerdedor;
                                if (jugadorActual.getId() == jugador1.getId()) {
                                    jugadorPerdedor = jugador2;
                                } else {
                                    jugadorPerdedor = jugador1;
                                }

                                jugadorPerdedor.setLose(jugadorPerdedor.getLose() + 1);

                                dao.editar(jugadorActual);
                                dao.editar(jugadorPerdedor);

                                partidaTerminada = true;
                            }

                            //VERIFICAR SI HAY EMPATE ANTICIPADO
                            else if (empateAnticipado(tablero, simboloActual)) {
                                mostrarTablero(tablero);

                                System.out.println();
                                System.out.println("YA NADIE PUEDE GANAR");
                                System.out.println("EMPATE ANTICIPADO");

                                jugador1.setTie(jugador1.getTie() + 1);
                                jugador2.setTie(jugador2.getTie() + 1);

                                dao.editar(jugador1);
                                dao.editar(jugador2);

                                partidaTerminada = true;
                            }

                            // VERIFICAR EMPATE SI EL TABLERO ESTA LLENO
                            else if (tableroLleno(tablero)) {
                                mostrarTablero(tablero);

                                System.out.println();
                                System.out.println("EMPATE.");

                                jugador1.setTie(jugador1.getTie() + 1);
                                jugador2.setTie(jugador2.getTie() + 1);

                                dao.editar(jugador1);
                                dao.editar(jugador2);

                                partidaTerminada = true;
                            }

                            // CAMBIAR TURNO
                            else {
                                if (jugadorActual.getId() == jugador1.getId()) {
                                    jugadorActual = jugador2;
                                    simboloActual = simboloJugador2;
                                } else {
                                    jugadorActual = jugador1;
                                    simboloActual = simboloJugador1;
                                }
                            }
                        }
                        System.out.println();
                        System.out.println("Partida finalizada.");
                        break;
                    //PRUEBA
                    /*int s1, s2, s3, s4, s5, s6, s7, s8 = 0;
                    System.out.println("Ingrese donde quiere poner su signo");
                    int eleccion=sc.nextInt();

                    if (eleccion==1) {
                        s1=1;
                    } else if (eleccion==2) {
                        s2=1;
                    } else if (eleccion==3) {
                        s3=1;
                    }
                    //char[][] tablero = new char[3][3];
                    //char[] jugadores = new char[3];
                    /*
                    char[][] tablero{
                    {'', '', ''},
                    {'', '', ''},
                    {'', '', ''}
                    }

                     */
                    //public void mostrar()
                    //mostrar
                    /*
                    for (int i = 0; i < tablero.length; i++) {
                        System.out.println(tablero[i][0]);
                        System.out.println(tablero[i][1]);
                        if (i<2){
                            System.out.println(tablero[i][2]);
                        }
                    }


                     */



                /*
                //VALIDAR SI HAY 2 JUGADORES Y QUE NO SE SELECCIONEN LOS MISMOS
                ArrayList<Object> juego = new ArrayList<>();
                try{
                        ('','',''
                        '', '', ''
                        '', '', '')

                    System.out.println("Inicia partida...");
                    System.out.println("Empieza");
                    System.out.println("Seleccionar fila: (0-2)");
                    System.out.println("Seleccionar columna: ");

                } catch (Exception e) {
                    System.out.println("Ingrese caracteres validos");
                }
                break;
                 */
                    case 4:
                        System.out.println("SALIENDO...");
                        seguir = false;
                        break;

                    default:
                        System.out.println("Opcion no valida");
                }
            } catch (Exception e) {
                System.out.println("Error: INGRESE CARACTERS VALIDOS");
            }
        } while (seguir==true);
    }

    // MOSTRAR TABLERO
    public static void mostrarTablero (char[][] tablero){
        System.out.println();
        System.out.println(
                        " " + tablero[0][0] +
                        " | " + tablero[0][1] +
                        " | " + tablero[0][2]
        );

        System.out.println("---+---+---");
        System.out.println(
                " " + tablero[1][0] +
                        " | " + tablero[1][1] +
                        " | " + tablero[1][2]
        );

        System.out.println("---+---+---");
        System.out.println(
                " " + tablero[2][0] +
                        " | " + tablero[2][1] +
                        " | " + tablero[2][2]
        );
    }

    // COMPROBAR GANADOR
    public static boolean hayGanador (char[][] tablero, char simbolo){
        // Filas
        for (int i = 0; i < 3; i++) {
            if (
                    tablero[i][0] == simbolo &&
                            tablero[i][1] == simbolo &&
                            tablero[i][2] == simbolo
            ) {
                return true;
            }
        }

        // Columnas
        for (int i = 0; i < 3; i++) {
            if (
                    tablero[0][i] == simbolo &&
                            tablero[1][i] == simbolo &&
                            tablero[2][i] == simbolo
            ) {
                return true;
            }
        }

        // Diagonal hacia la derecha
        if (
                tablero[0][0] == simbolo &&
                        tablero[1][1] == simbolo &&
                        tablero[2][2] == simbolo
        ) {
            return true;
        }

        // Diagonal hacia la izquierda
        if (
                tablero[0][2] == simbolo &&
                        tablero[1][1] == simbolo &&
                        tablero[2][0] == simbolo
        ) {
            return true;
        }
        return false;
    }

    // COMPROBAR SI TABLERO ESTA LLENO
    public static boolean tableroLleno ( char[][] tablero){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    /*
    public static boolean empateAnticipado(char[][] tablero) {
        //FILAS
        for (int i = 0; i < 3; i++) {
            boolean hayX = false;
            boolean hayO = false;

            if (tablero[i][0] == 'X' ||
                    tablero[i][1] == 'X' ||
                    tablero[i][2] == 'X') {

                hayX = true;
            }

            if (tablero[i][0] == 'O' ||
                    tablero[i][1] == 'O' ||
                    tablero[i][2] == 'O') {

                hayO = true;
            }

            //Si hay X y O, esta fila ya está bloqueada
            if (hayX && hayO) {
                continue;
            }

            //Todavía existe posibilidad de ganar
            return false;
        }

        //COLUMNAS
        for (int i = 0; i < 3; i++) {
            boolean hayX = false;
            boolean hayO = false;

            if (tablero[0][i] == 'X' ||
                    tablero[1][i] == 'X' ||
                    tablero[2][i] == 'X') {

                hayX = true;
            }

            if (tablero[0][i] == 'O' ||
                    tablero[1][i] == 'O' ||
                    tablero[2][i] == 'O') {

                hayO = true;
            }

            if (hayX && hayO) {
                continue;
            }
            return false;
        }

        //DIAGONAL HACIA LA DERECHA
        boolean hayX = false;
        boolean hayO = false;

        if (tablero[0][0] == 'X' ||
                tablero[1][1] == 'X' ||
                tablero[2][2] == 'X') {

            hayX = true;
        }

        if (tablero[0][0] == 'O' ||
                tablero[1][1] == 'O' ||
                tablero[2][2] == 'O') {

            hayO = true;
        }

        //SI NO HAY X y 0 aun se puede ganar
        if (!(hayX && hayO)) {
            return false;
        }

        //DIAGONAL HACI LA IZQUIERDA
        hayX = false;
        hayO = false;

        if (tablero[0][2] == 'X' ||
                tablero[1][1] == 'X' ||
                tablero[2][0] == 'X') {

            hayX = true;
        }

        if (tablero[0][2] == 'O' ||
                tablero[1][1] == 'O' ||
                tablero[2][0] == 'O') {

            hayO = true;
        }

        if (!(hayX && hayO)) {
            return false;
        }

        // Ninguna línea puede producir un ganador
        return true;
    }
     */

    public static int espaciosVacios(char[][] tablero) {
        int espacios = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (tablero[i][j] == ' ') {
                    espacios++;
                }
            }
        }

        return espacios;
    }

    public static boolean empateAnticipado(char[][] tablero, char simbolo) {
        if (espaciosVacios(tablero) != 2) {
            return false;
        }

        //REVISA LUGARES VACIOS
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                //ESTO REALIZA UNA JUGADA ANTICIPADA PARA VER SI EL JUGADOR QUE ESTE EN EL TURNO AUN PUEDE GANAR SI ES QUE QUEDAN DOS ESPACIOS
                if (tablero[i][j] == ' ') {
                    //PONER SIMBOLO
                    tablero[i][j] = simbolo;


                    if (hayGanador(tablero, simbolo)) {
                        tablero[i][j] = ' ';
                        return false;
                    }

                    //DESACE LA JUGADA
                    tablero[i][j] = ' ';
                }
            }
        }
        // Ninguna de las 2 jugadas permite ganar
        return true;
    }
}
