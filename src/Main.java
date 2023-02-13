import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args){
        try {
            new KVServer().start();
            new HttpTaskServer().start();
        } catch (IOException e) {
            System.out.println("Ошибка при запуске сервера");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}