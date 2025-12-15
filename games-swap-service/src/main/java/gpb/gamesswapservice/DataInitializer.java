package gpb.gamesswapservice;

import gpb.gamesswapservice.entity.Game;
import gpb.gamesswapservice.entity.Owner;
import gpb.gamesswapservice.repository.GameRepository;
import gpb.gamesswapservice.repository.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

    private final OwnerRepository ownerRepository;
    private final GameRepository gameRepository;

    @Autowired
    public DataInitializer(OwnerRepository ownerRepository, GameRepository gameRepository) {
        this.ownerRepository = ownerRepository;
        this.gameRepository = gameRepository;
    }



    @Override
    public void run(String... args) {
        LOG.info("Начинаю инициализацию тестовых данных...");

        if (ownerRepository.count() == 0) {
            LOG.info("Таблица owners пуста — создаю тестовых владельцев...");

            Owner owner1 = new Owner("Анна","Петрова", "Father", "anna@example.com");
            Owner owner2 = new Owner("Иван","Сидоров", "Father", "ivan@example.com");

            ownerRepository.save(owner1);
            LOG.info("Создан владелец: ID={}, имя={}", owner1.getId(), owner1.getFirstName());

            ownerRepository.save(owner2);
            LOG.info("Создан владелец: ID={}, имя={}", owner2.getId(), owner2.getFirstName());

            Game game1 = new Game("TEST", 1499d, "TESTTESTTEST", owner1);
            Game game2 = new Game("Cyberpunk 2077", 2999d, "Игра про будущее в стиле Киберпанк",owner2);
            Game game3 = new Game("Minecraft", 999d, "Песочница с кубиками", owner2);

            LOG.info("{} !!!!!!!",game1.getId());

            owner1.addGame(game1);
            owner1.addGame(game2);
            owner2.addGame(game3);

            gameRepository.save(game1);
            gameRepository.save(game2);

            ownerRepository.save(owner1);
            ownerRepository.save(owner2);

            LOG.info("Созданы игры:");
            LOG.info(" - {} (владелец: {})", game1.getTitle(), owner1.getFirstName());
            LOG.info(" - {} (владелец: {})", game2.getTitle(), owner1.getFirstName());
            LOG.info(" - {} (владелец: {})", game3.getTitle(), owner2.getFirstName());

        } else {
            LOG.info("Таблица owners не пуста — пропускаю инициализацию.");
        }

        LOG.info("Инициализация завершена.");
    }
}