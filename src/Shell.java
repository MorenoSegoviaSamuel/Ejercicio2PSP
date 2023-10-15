import java.util.Scanner;

/**
 * @author MorenoSegoviaSamuel
 * La clase Shell representa un shell de línea de comandos simple que permite al usuario
 * ingresar comandos y realizar operaciones especiales como "last-command" y "exit".
 */

public class Shell {
    private static Command lastExecutedCommand;

    /**
     * El método principal de la aplicación que inicia el shell.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en este programa).
     */

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String userInput = scanner.nextLine();

            if (userInput.isEmpty()) {
                continue; // Ignora comandos vacíos
            }

            if (userInput.equals("exit")) {
                System.out.println("Saliendo del shell.");
                break;
            } else if (userInput.equals("last-command")) {
                if (lastExecutedCommand == null) {
                    System.out.println("No se ha ejecutado ningún comando aún.");
                } else {
                    System.out.println(lastExecutedCommand.toString());
                }
            } else {
                Command command = new Command(userInput);
                command.execute();

                if (command.getOutputRedirect().isEmpty()) {
                    String output = command.executeAndGetOutput();
                    System.out.println(output);
                }

                lastExecutedCommand = command;
            }
        }
    }
}