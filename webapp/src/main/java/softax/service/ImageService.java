package softax.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StorageService storageService;
//
//    public Game uploadGamePhoto(Long gameId, MultipartFile file) {
//        Game game = gameDao.getById(gameId);
//        if (game == null) {
//            log.debug("Try to upload image for missing game with id '{}'.", gameId);
//            throw new AppLogicException(ErrorCodes.NO_SUCH_GAME);
//        }
//
//        deleteGamePicture(game.getPhoto());
//
//        String filename = createPhotoFilename(game, file.getOriginalFilename());
//        Path path = Paths.get(gamePicturePath, filename);
//        storageService.store(file, path.getParent(), path.getFileName().toString());
//
//        game.setPhoto(String.format("%s/%s", StaticResourceConfiguration.GAME_PHOTO_URL_PREFIX, filename));
//        gameDao.update(game);
//        log.info("Game photo has been changed for '{}'.", gameId);
//
//        return game;
//    }

}
