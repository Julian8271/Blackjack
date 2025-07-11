import java.util.ArrayList;
import java.util.List;
public class Jugador {

	private String nombre;
	//Lista de cartas que sera la mano del jugador
	private List<Carta> mano;
	//Variable para almacenar el puntaje
	private int puntaje;
	//Cantidad quemada de fichas del jugador
	private int fichas;
	//Variable que se encarga de la ronda actual de apuestas
	private int apuestaActual;
	//Lista que va a contoner la posible segunda mano
	private List<Carta> segundaMano;
	//Variables que actuan como las de la apuesta de la mano original
	private int apuestaSegundaMano;
	private int puntajeSegundaMano;
	private int seguro;

	public Jugador(String nombre) {
		this.nombre = nombre;
		//La mano de combierte en una ArrayList por el mismo hecho anterior
		this.mano = new ArrayList<>();
		//Puntaje inicial de cero
		this.puntaje = 0;
		//Fichas opcionales
		this.fichas = 100;
		this.apuestaActual = 0;
		//Incializacion de dichos atributos para la segunda mano 
		this.segundaMano = new ArrayList<>();
		this.apuestaSegundaMano = 0;
		this.puntajeSegundaMano = 0;
        this.seguro = 0;

	}
	//Metodo basico de apuesta
	public void apostar(int cantidad) throws BlackjackException {
		//Condicion para ver si la apuesta es valida
		if (cantidad <= 0) {
		    //Usamos la clase que creamos para las excepciones personalizadas
            throw new BlackjackException("La apuesta debe ser positiva.");
        } 
        if (cantidad > fichas) {
            throw new BlackjackException("Fichas insuficientes. Tienes: " + fichas);
        }
        fichas -= cantidad;
        apuestaActual = cantidad;
	}
//Metodo para dividir la mano del jugador 
	public void dividir() {
		if(puedeDividir()) {
			segundaMano.add(mano.remove(1));
			apuestaSegundaMano = apuestaActual;
			fichas -= apuestaActual;
		}
	}
//Aqui se añaden las cartas a la segunda mano 
	public void recibirCartaSegundaMano(Carta carta) {
		segundaMano.add(carta);
		actualizarPuntajeSegundaMano();
	}
//Se actualiza el puntaje de la segunda mano como si fuera la original 
	private void actualizarPuntajeSegundaMano() {
		puntajeSegundaMano = 0;
		int contadorAses = 0;
		for (Carta carta : segundaMano) {
			puntajeSegundaMano += carta.getValorNumerico();
			if (carta.getValor().equals("As")) {
				contadorAses++;
			}
		}
		//Logica para validar la variabilidad de los ases
		while (puntajeSegundaMano > 21 && contadorAses > 0) {
			puntajeSegundaMano -= 10;
			contadorAses--;
		}
	}
    //Determinar si se obtuvo el puntaje maximo de primeras
	public boolean tieneBlackjack() {
		return mano.size() == 2 && getPuntaje() == 21;
	}
//Metodo que permite saber si se puede dividir la mano 
	public boolean puedeDividir() {
		return mano.size() == 2 && mano.get(0).getValor().equals(mano.get(1).getValor());
	}
	// metodo para ganar fichas si gana la ronda
	public void ganarApuesta() {
		//Si el jugador gana recibe el doble de lo que puso de apuesta
		this.fichas += apuestaActual * 2;
		apuestaActual = 0;
	}
	//Aca se gana la aouesat en la segunda mano 
	public void ganarApuestaSegundaMano(){
	    this.fichas += apuestaActual * 2;
	    apuestaActual = 0;
	}
	//Aca se pierde en la segunda mano 
	public void perderApuestaSegundaMano(){
	    apuestaActual = 0;
	}
	//Aca se empata en la segunda mano 
	public void empateApuestaSegundaMano(){
	    this.fichas = apuestaActual;
	    apuestaActual = 0;
	}

	//metodo para perder fichas en caso de perder la ronda
	public void perderApuesta() {
		//Aca la las fichas ya fueron descontadas del jugador y lo unico que pasa es su apuesta pasa a cero
		apuestaActual = 0;
	}
	//metodo si el juego resulta en un empate
	public void empateApuesta() {
		//Aca se regresan las fichas gastadas por el jugador
		this.fichas += apuestaActual;
		apuestaActual = 0;
	}

	//Metodo para recibir una carta y que se agregue a la mano del jugador
	public void recibirCarta(Carta carta) {
		//if(Juego.volverAJugar) mano.clear();
		mano.add(carta);
		//Actualiza el puntaje
		actualizarPuntaje();
	}
	//Metodo privado solo en jugador para que actualice su puntaje en cada turno
	private void actualizarPuntaje() {
		puntaje = 0;
		int contadorAses = 0;
		for (Carta carta : mano) {
			//Va agregando el valor de la carta sacada a el puntaje del jugador
			puntaje += carta.getValorNumerico();
			if(carta.getValor().equals("As")) {
				//Cuenta el numero de As que van saliendo
				contadorAses++;
			}
		}
		//Ciclo para manejar la logica de cambio de valor de los As en caso de superar el 21
		while (puntaje > 21 && contadorAses > 0) {
			puntaje -= 10;
			contadorAses--;
		}
	}
	//Metodo para verificar si el jugador se paso
	public boolean volo() {
		return puntaje > 21;
	}
	//Metodo para mostrar la mano del jugador en cada turno
	public String mostrarMano() {
		//Se hace un StringBuilder que es un tipo de String mutable que se puede ir cambiando a lo largo del turnos para que se muestre la mano
		StringBuilder sb = new StringBuilder();
		for (Carta carta: mano) {
			//Aca va a agregando las cartas al StringBuilder para que forme este
			sb.append(carta.toString()).append(" ");
		}
		//Luego de armado se retorna el StringBuilder como String normal y se comvierte a minuscula
		return sb.toString().trim();
	}
	public void rendirse() {
       fichas += apuestaActual / 2; // Recupera la mitad
       apuestaActual = 0;
       mano.clear(); // Limpia las cartas (fin de la ronda para este jugador)
	}
	//Metodo para doblar las apuestas
	public boolean doblarApuesta(){
	    //Verificacion de que se pueda hacer dicha apuesta 
	    if(fichas >= apuestaActual){
	        fichas -= apuestaActual;
	        apuestaActual *= 2;
	        return true;
	    }
	    return false;
	}
	//Metodo de para apostar por la modalidad seguro 
	public void apostarSeguro(int cantidad){
	    if(cantidad > 0 && cantidad <= fichas){
	        setSeguro(cantidad);
	        fichas -= cantidad;
	    }
	}
	
	//Metodos para obtener los atributos de jugador y usarlos en la clase juego
	public String getNombre() {
		return nombre;
	}

	public int getPuntaje() {
		return puntaje;
	}

	public List<Carta> getMano() {
		return mano;
	}

	public int getFichas() {
		return fichas;
	}

	public int getApuestaActual() {
		return apuestaActual;
	}

	public boolean segundaManoVolo() {
		return puntajeSegundaMano > 21;
	}

	public List<Carta> getSegundaMano() {
		return segundaMano;
	}

	public int getPuntajeSegundaMano() {
		return puntajeSegundaMano;
	}
	
	public int getSeguro(){
	    return seguro;
	}
	public void ganarFichas(int cantidad){
	    this.fichas += cantidad;
	}
	//Metodo para setear la czntidad de apuesta en seguro 
	public void setSeguro(int cantidad){
	    this.seguro = cantidad;
	}
	public void setPuntaje(int cantidad){
	   puntaje = cantidad;
	}
	
	public void setPuntajeSegundaMano(int cantidad){
	   puntajeSegundaMano = cantidad;
	}
	public void setApuestaActual(int cantidad){
	    apuestaActual = cantidad;
	}
	
	public void setApuestaSegundaMano(int cantidad){
	    apuestaSegundaMano = cantidad;
	}
	
	
}