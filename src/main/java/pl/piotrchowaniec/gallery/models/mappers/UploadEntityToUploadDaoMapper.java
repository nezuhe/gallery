package pl.piotrchowaniec.gallery.models.mappers;

import pl.piotrchowaniec.gallery.models.daos.UploadDao;
import pl.piotrchowaniec.gallery.models.entities.UploadEntity;

public class UploadEntityToUploadDaoMapper extends Mapper<UploadEntity, UploadDao> {

    @Override
    public UploadDao map(UploadEntity key) {
        UploadDao uploadDao = new UploadDao();
        uploadDao.setUploadId(key.getUploadId());
        uploadDao.setName(key.getName());
        uploadDao.setSurname(key.getSurname());
        uploadDao.setEmail(key.getEmail());
        uploadDao.setUploadDate(key.getUploadDate());
        uploadDao.setStatus(key.getStatus());
        uploadDao.setPhotosQuantity(key.getUploadedFilesList().size());
        return uploadDao;
    }
}
