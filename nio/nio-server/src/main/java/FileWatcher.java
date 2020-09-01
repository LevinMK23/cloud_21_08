import java.io.IOException;
import java.nio.file.*;

public class FileWatcher {

    /**
     * Регистрация слушателя изменений содержимого папки
     * @param path - должен быть директорией
     */
    public void registerWatcher(Path path) throws IOException, InterruptedException {

        // получаем ссылку
        WatchService watchService = FileSystems
                .getDefault()
                .newWatchService();

        // регистрируем вотчера
        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        while (true) {
            System.out.println("Ждем события");
            // блокирующая операция ждем ключ событий
            WatchKey watchKey = watchService.take();
            if (watchKey.isValid()) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    System.out.println("Тип события: " + event.kind());
                    System.out.println("Ресурс который изменился: " + event.context());
                    System.out.println("Количество событий: " + event.count());
                }
                watchKey.reset();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new FileWatcher().registerWatcher(Paths.get("dir"));
    }
}
