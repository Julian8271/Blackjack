public class BlackjackException extends Exception{
    //Manejo basico de errores personalizados
    public BlackjackException (String mensaje){
        super("ERROR" + mensaje);
    }
}
