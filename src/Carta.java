
public class Carta {
    // Atributos de una carta individual
    private String palo;  // Ej: "Corazones", "Picas", etc.
    private String valor; // Ej: "As", "2", ..., "K"

    // Constructor de una carta 
    public Carta(String palo, String valor) {
        this.palo = palo;
        this.valor = valor;
    }


    public int getValorNumerico() {
        if (valor.equals("As")) {
            return 11; // El As vale 11 por defecto, pero en caso de que el puntaje se pase este tomara un valor de 1
        } else if (valor.equalsIgnoreCase("J") || valor.equalsIgnoreCase("Q") || valor.equalsIgnoreCase("K")) {
            return 10;
        } else {
            // Convierte el valor (String) a número (ej: "7" → 7) mediante un casteo 
            return Integer.parseInt(valor);
        }
    }

    public String toString() {
        //Sobreecribimos el metodo toString para que nos muestre las cartas con sus simbolos 
        //Los simbolos estaran guardados en una variable aparte 
        char simboloPalo = ' ';
        switch (palo) {
            case "Corazones": simboloPalo = '♥'; break;
            case "Diamantes": simboloPalo = '♦'; break;
            case "Picas": simboloPalo = '♠'; break;
            case "Treboles": simboloPalo = '♣'; break;
        }
        //Se retorna la concatenacion para que la salida visual sea la esperada
        return valor + simboloPalo;
    }
    //Metodo para obtener el valor de la carta 
    public String getValor(){
        return valor;
    }

}