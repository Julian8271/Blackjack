
public class Blackjack {

    public static void main(String[] args) {
        //Iniciamos un objeto de tipo juego ya que este inicia el resto de clases para el juego 
        Juego juego = new Juego();
        //Creamos a los jugadores que queremos
        juego.agregarJugador("Ana");
        juego.agregarJugador("Luis");
        //Iniciamos la partida 
        juego.iniciarPartida();
    }
    
}