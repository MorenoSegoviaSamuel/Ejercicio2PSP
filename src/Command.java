import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author MorenoSegoviaSamuel
 * La clase Command representa un comando que puede ser ejecutado en un shell y proporciona
 * funcionalidades para ejecutar el comando, obtener su salida y mostrar información relacionada.
 */

public class Command {
    private String[] argumentos;
    private String outputRedirect;
    private String pid;
    private String commandOutput;
    private int exitValue;

    /**
     * Constructor que recibe un array de argumentos y una redirección de salida.
     *
     * @param arguments     Array de argumentos del comando.
     * @param outputRedirect Nombre de archivo donde se redirige la salida (cadena vacía si no).
     */
    public Command(String[] arguments, String outputRedirect) {
        this.argumentos = arguments;
        this.outputRedirect = outputRedirect;
    }

    /**
     * Constructor que recibe una cadena con argumentos y una posible redirección de salida.
     *
     * @param commandString Cadena con argumentos y redirección.
     */

    public Command(String commandString) {
        if (commandString.contains(">")) {
            String[] parts = commandString.split(">");
            String argsPart = parts[0].trim();
            String redirectPart = parts[1].trim();
            this.argumentos = argsPart.split("\\s+");
            this.outputRedirect = redirectPart;
        } else {
            this.argumentos = commandString.split("\\s+");
            this.outputRedirect = "";
        }
    }

    /**
     * Ejecuta el comando y captura su salida, PID y valor de salida.
     */

    public void execute() {
        try {
            Process process = new ProcessBuilder(argumentos).start();
            this.pid = String.valueOf(process.pid());

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder outputBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line).append("\n");
            }

            process.waitFor();
            this.exitValue = process.exitValue();
            this.commandOutput = outputBuilder.toString();

        } catch (IOException e) {
            System.err.println("Error al crear el proceso: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Error al esperar la finalización del proceso: " + e.getMessage());
        }
    }

    /**
     * Ejecuta el comando y devuelve su salida estándar como una cadena, o cadena vacía si se ha redirigido.
     *
     * @return Salida estándar del comando.
     */

    public String executeAndGetOutput() {
        if (!outputRedirect.isEmpty()) {
            return ""; // Salida estándar ha sido redirigida
        }

        if (commandOutput == null) {
            execute();
        }

        return commandOutput;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder("Información del comando:\n");
        info.append("Comando: ").append(String.join(" ", argumentos)).append("\n");
        info.append("Numero de parametros: ").append(argumentos.length).append("\n");
        info.append("Parametros: ");
        for (String arg : argumentos) {
            info.append(arg).append(" ");
        }
        info.append("\n");

        if (pid != null) {
            info.append("PID: ").append(pid).append("\n");
            if (outputRedirect.isEmpty()) {
                info.append("Command Output:\n").append(commandOutput);
            }
            info.append("Valor de salida: ").append(exitValue).append("\n");
        } else {
            info.append("El comando no ha sido ejecutado todavia.\n");
        }

        return info.toString();
    }

    /**
     * Obtiene la redirección de la salida estándar.
     *
     * @return Nombre del archivo donde se redirige la salida (cadena vacía si no se redirige).
     */

    public String getOutputRedirect() {
        return outputRedirect;
    }
}
