import java.util.ArrayList;
import java.util.List;
public class Jugador{
    
    private String nombre;
    //Lista de cartas que sera la mano del jugador 
    private List<Carta> mano;
    //Variable para almacenar el puntaje
    private int puntaje;
    
    public Jugador(String nombre){
        this.nombre = nombre;
        //La mano de combierte en una ArrayList por el mismo hecho anterior 
        this.mano = new ArrayList<>();
        //Puntaje inicial de cero 
        this.puntaje = 0;
    }
    //Metodo para recibir una carta y que se agregue a la mano del jugador 
    public void recibirCarta(Carta carta){
        mano.add(carta);
        //Actualiza el puntaje 
        actualizarPuntaje();
    }
    //Metodo privado solo en jugador para que actualice su puntaje en cada turno 
    private void actualizarPuntaje(){
        puntaje = 0;
        int contadorAses = 0;
        for (Carta carta : mano){
            //Va agregando el valor de la carta sacada a el puntaje del jugador 
            puntaje += carta.getValorNumerico();
            if(carta.getValor().equals("As")){
                //Cuenta el numero de As que van saliendo 
                contadorAses++;
            }
        }
        //Ciclo para manejar la logica de cambio de valor de los As en caso de superar el 21
        while (puntaje > 21 && contadorAses > 0){
            puntaje -= 10;
            contadorAses--;
        }
    }
    //Metodo para verificar si el jugador se paso 
    public boolean volo(){
        return puntaje > 21;
    }
    //Metodo para mostrar la mano del jugador en cada turno 
    public String mostrarMano(){
        //Se hace un StringBuilder que es un tipo de String mutable que se puede ir cambiando a lo largo del turnos para que se muestre la mano 
        StringBuilder sb = new StringBuilder();
        for (Carta carta: mano){
            //Aca va a agregando las cartas al StringBuilder para que forme este 
            sb.append(carta.toString()).append(" ");
        }
        //Luego de armado se retorna el StringBuilder como String normal y se comvierte a minuscula 
        return sb.toString().trim();
    }
    //Metodos para obtener los atributos de jugador y usarlos en la clase juego 
    public String getNombre(){
        return nombre;
    }
    
    public int getPuntaje(){
        return puntaje;
    }
    
    public List<Carta> getMano(){
        return mano;
    }
}