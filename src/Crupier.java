import java.util.List;

public class Crupier extends Jugador {
    private Carta cartaOculta;  //Carta oculta a los demas

    // Constructor
    public Crupier() {
        super("Crupier");  
    }

    // Asignar carta oculta (solo usada al repartir las primeras cartas)
    public void recibirCartaOculta(Carta carta) {
        this.cartaOculta = carta;
        super.recibirCarta(carta);  //Añade la carta a la mano para calcular puntaje
    }

    //Revelar la carta oculta al inicio de su turno
    public void revelarCartaOculta() {
        if (cartaOculta != null) {
            System.out.println("[Crupier] Revela carta oculta: " + cartaOculta);
            cartaOculta = null;  //Ya no está oculta
        }
    }

    //Sobrescribe mostrarMano para ocultar la primera carta al inicio
    @Override
    public String mostrarMano() {
        if (cartaOculta == null) {
            return super.mostrarMano();  //Muestra todas las cartas si no hay oculta
        }
        return "[Carta Oculta] " + super.getMano().get(1);  //Ej: "[Carta Oculta] 7♠"
    }

    //Lógica automática del crupier (pedir cartas hasta tener 17+)
    public void jugarTurno(Mazo mazo) {
        System.out.println("\n--- Turno del Crupier ---");
        revelarCartaOculta();
        System.out.println("Mano actual: " + mostrarMano() + " | Puntaje: " + getPuntaje());
        //Si el crupier no saca minimo 17 con las dos primeras cartas esta obligado a sacar mas, hasta superar este puntaje y automaticamente planatarse o volar 
        while (getPuntaje() < 17) {
            Carta nuevaCarta = mazo.sacarCarta();
            recibirCarta(nuevaCarta);
            System.out.println("El crupier pide carta: " + nuevaCarta);
            System.out.println("Nuevo puntaje: " + getPuntaje());
        }
        //Condicion para saber si el crupier volo o se planta 
        if (volo()) {
            System.out.println("¡El crupier se pasó de 21!");
        } else {
            System.out.println("El crupier se planta con " + getPuntaje());
        }
    }
}